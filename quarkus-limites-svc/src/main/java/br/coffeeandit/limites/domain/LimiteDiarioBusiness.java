package br.coffeeandit.limites.domain;

import br.coffeeandit.limites.dto.RequisicaoTransacaoDTO;
import br.coffeeandit.limites.model.LimiteDiario;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@ApplicationScoped
public class LimiteDiarioBusiness {

    @ConfigProperty(name = "limite.valorTotal")
    BigDecimal valorTotal;

    @Inject
    LimiteDiarioRepository limiteDiarioRepository;

    private static final Logger LOG = Logger.getLogger(LimiteDiarioBusiness.class);


    @Transactional
    public LimiteDiario inserirLimiteDiario(final LimiteDiario newLimiteDiario) {

        var limiteDiario = new LimiteDiario();
        limiteDiario.setValor(newLimiteDiario.getValor());
        limiteDiario.setAgencia(newLimiteDiario.getAgencia());
        limiteDiario.setConta(newLimiteDiario.getConta());
        limiteDiario.setData(LocalDate.now());
        PanacheEntityBase.persist(limiteDiario);

        return limiteDiario;

    }

    @Transactional
    public RequisicaoTransacaoDTO limiteDiario(final RequisicaoTransacaoDTO transactionDTO) {
        var buscarLimiteDiario = limiteDiarioRepository.buscarLimiteDiario(
                transactionDTO.getConta().getCodigoAgencia(),
                transactionDTO.getConta().getCodigoConta());

        LimiteDiario limiteDiario;

        if (buscarLimiteDiario.isEmpty()) {
            limiteDiario = new LimiteDiario();
            limiteDiario.setAgencia(transactionDTO.getConta().getCodigoAgencia());
            limiteDiario.setConta(transactionDTO.getConta().getCodigoConta());
            limiteDiario.setValor(valorTotal);
            limiteDiario.setData(LocalDate.now());
            PanacheEntityBase.persist(limiteDiario);
        } else {
            limiteDiario = buscarLimiteDiario.get();
        }
        LOG.info("Limite diario da conta " + limiteDiario);

        if (limiteDiario.getValor().compareTo(transactionDTO.getValor()) < 0) {

            transactionDTO.suspeitaFraude();
            LOG.info("Transação excede valor diario " + transactionDTO);


        } else if (transactionDTO.getValor().compareTo(BigDecimal.valueOf(10000L)) > 0) {

            transactionDTO.analiseHumana();
            LOG.info("Transação está em Análise Humana " + transactionDTO);


        } else {
            transactionDTO.analisada();
            LOG.info("Transação analisada " + transactionDTO);
            limiteDiario.setValor(limiteDiario.getValor().subtract(transactionDTO.getValor()));
            PanacheEntityBase.persist(limiteDiario);
        }

        return transactionDTO;


    }

    public Optional<LimiteDiario> buscarLimiteDiario(final Long codigoAgencia, final Long codigoConta) {
        return limiteDiarioRepository.buscarLimiteDiario(codigoAgencia, codigoConta);
    }

}
