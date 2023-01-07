package br.coffeeandit.limites.domain;

import br.coffeeandit.limites.dto.RequisicaoTransacaoDTO;
import br.coffeeandit.limites.dto.SituacaoEnum;
import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
public class TransactionConsumer {

    private static final Logger LOG = Logger.getLogger(TransactionConsumer.class);

    @Inject
    LimiteDiarioBusiness limiteDiarioBusiness;

    @Incoming("transaction")
    @Retry(delay = 10, maxRetries = 5)
    @Blocking
    @Outgoing("transaction_extorno")
    public RequisicaoTransacaoDTO consume(final Message<RequisicaoTransacaoDTO> message) {
        final RequisicaoTransacaoDTO transacaoDTO = message.getPayload();
        LOG.info("Análise de limite: " + transacaoDTO);
        message.ack();
        if (SituacaoEnum.ACEITA_PROCESSAMENTO.equals(transacaoDTO.getSituacao())) {
            return limiteDiarioBusiness.limiteDiario(transacaoDTO);
        }
        return transacaoDTO;
    }
}
