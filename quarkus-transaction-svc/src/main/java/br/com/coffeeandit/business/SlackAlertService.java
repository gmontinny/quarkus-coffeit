package br.com.coffeeandit.business;

import br.com.coffeeandit.client.SlackClient;
import br.com.coffeeandit.dto.SlackMessage;
import br.com.coffeeandit.dto.TemplateSlack;
import br.com.coffeeandit.dto.TransacaoDTO;
import br.com.coffeeandit.events.KafkaEvents;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Singleton
public class SlackAlertService {

    private ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOG = Logger.getLogger(SlackAlertService.class);


    @Inject
    @RestClient
    private SlackClient slackClient;

    @ConfigProperty(name = "slack.imagem")
    private String imagem;

    @ConfigProperty(name = "slack.imagem")
    private String imagem2;

    @ConfigProperty(name = "slack.message")
    private String message;

    @ConfigProperty(name = "slack.author")
    private String author;
    @ConfigProperty(name = "transaction.riskValue")
    String riskValue;

    public void enviarMensagem(final TransacaoDTO transacaoDTO) {
        final Optional<String> template = template(transacaoDTO);
        if (template.isPresent()) {
            final Response response = slackClient.callSlack(template.get());
            log.info("Response Slack " + response.getStatus());
        }
    }

    private Optional<String> template(final TransacaoDTO transactionDTO) {
        var templateSlack = new TemplateSlack();
        templateSlack.setText(message);
        addField(templateSlack, transactionDTO);
        try {
            return Optional.of(objectMapper.writeValueAsString(templateSlack));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    private void addField(final TemplateSlack templateSlack, final TransacaoDTO transactionDTO) {
        final List<SlackMessage> attachments = new ArrayList<>();
        final var messageDTO = new SlackMessage();
        var messageBody = "%s no valor de R$  %s da conta %d, agência %d foi marcada como suspeita de fraude para %s CPF %d na conta %s da " +
                "agência" +
                " %s no " +
                "banco %d";
        messageDTO.setAuthor(author);
        messageDTO.setTitle("Notificação de Transação.");
        messageDTO.setAuthorIcon(imagem);
        messageDTO.setImageUrl(imagem);

        if (transactionDTO.getValor().compareTo(new BigDecimal(riskValue)) > 0) {
            messageDTO.setAuthorIcon(imagem2);
            messageDTO.setImageUrl(imagem2);


        }
        messageDTO.addField("Detalhes:", String.format(messageBody, transactionDTO.getTipoTransacao().toString(),
                transactionDTO.getValor().toPlainString(),
                transactionDTO.getConta().getCodigoConta().longValue(),
                transactionDTO.getConta().getCodigoAgencia().longValue(),
                transactionDTO.getBeneficiario().getNomeFavorecido(),
                transactionDTO.getBeneficiario().getCPF().longValue(),
                transactionDTO.getBeneficiario().getConta(), transactionDTO.getBeneficiario().getAgencia(),
                transactionDTO.getBeneficiario().getCodigoBanco()));
        attachments.add(messageDTO);
        templateSlack.setAttachments(attachments);
    }
}
