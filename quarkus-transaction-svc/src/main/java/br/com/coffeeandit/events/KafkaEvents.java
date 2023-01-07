package br.com.coffeeandit.events;

import br.com.coffeeandit.business.TransacaoService;
import br.com.coffeeandit.dto.TransacaoDTO;
import br.com.coffeeandit.events.observer.TransactionObserverService;
import br.com.coffeeandit.model.SituacaoEnum;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.*;
import org.jboss.logging.Logger;
import org.reactivestreams.Publisher;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class KafkaEvents {

    private static final Logger LOG = Logger.getLogger(KafkaEvents.class);

    @Inject
    @Channel("liquidacao_in")
    private Multi<TransacaoDTO> transactions;

    @Inject
    private TransacaoService service;

    @Inject
    private TransactionObserverService transactionObserverService;

    @Inject
    @Channel("liquidacao")
    Emitter<Record<String, TransacaoDTO>> emitter;

    @Incoming("transaction")
    public CompletionStage<Void> receive(final Message<TransacaoDTO> msg) {
        LOG.info("Mensagem recebida transaction ->  " + msg.getPayload());
        return CompletableFuture.runAsync(() -> processMessage(msg)).thenRun(msg::ack);
    }

    @Incoming("extorno")
    public CompletionStage<Void> onConsumeExtorno(final Message<TransacaoDTO> msg) {
        return CompletableFuture.runAsync(() -> processarRetorno(msg)).thenRun(msg::ack);

    }


    @Incoming("liquidacao_in")
    @Outgoing("liquidacao_sse")
    public Multi<TransacaoDTO> liquidacao(final Multi<TransacaoDTO> msg) {
        LOG.info("Liquidação recebida para SSE " + msg);
        return msg;
    }


    public void notification(final TransacaoDTO transaction) {
        transactionObserverService.notification(transaction);
    }

    private void processarRetorno(Message<TransacaoDTO> msg) {
        try {
            var transaction = getTransaction(msg);
            LOG.info("Transação retornada da Análise " + transaction);

            if (!SituacaoEnum.ANALISADA.equals(transaction.getSituacao())) {
                LOG.info("Solicitação de alteração de status da transação " + transaction);
                if (SituacaoEnum.EM_ANALISE_HUMANA.equals(transaction.getSituacao())) {
                    notification(transaction);

                }

            } else {
                LOG.info("Transação Analisada " + transaction);
                final TransacaoDTO transacaoDTO = service.aprovarTransacao(transaction);
                if (transacaoDTO.isRejeitada()) {
                    notification(transaction);
                } else {
                    enviarLiquidacao(transaction);
                }
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        ;
    }

    private void enviarLiquidacao(final TransacaoDTO transaction) {
        emitter.send(Record.of(transaction.getUuid().toString(), transaction));


    }

    private TransacaoDTO getTransaction(final Message<TransacaoDTO> msg) {
        TransacaoDTO transacaoDTO = msg.getPayload();
        if (Objects.isNull(transacaoDTO.getSituacao())) {
            transacaoDTO.setSituacao(SituacaoEnum.NAO_ANALISADA);
        }
        transacaoDTO.setData(LocalDateTime.now());
        return transacaoDTO;
    }

    private void processMessage(final Message<TransacaoDTO> msg) {
        var transacao = msg.getPayload();
        transacao.convert().persist();
    }

}
