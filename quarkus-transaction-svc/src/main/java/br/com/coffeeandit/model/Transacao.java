package br.com.coffeeandit.model;

import br.com.coffeeandit.dto.BeneficiatioDto;
import br.com.coffeeandit.dto.Conta;
import br.com.coffeeandit.dto.TransacaoDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Getter
@Setter
@EqualsAndHashCode(of = "uuid", callSuper = false)
@ToString
@MongoEntity(collection = "Transactions")
public class Transacao extends PanacheMongoEntity {

    private BigDecimal valor;
    private LocalDateTime data;
    private String uuid;
    private SituacaoEnum situacao;
    @Schema(description = "Conta de origem da transação")
    private Conta conta;
    @Schema(description = "Beneficiário da transação")
    private BeneficiatioDto beneficiario;

    public void naoAnalisada() {
        setSituacao(SituacaoEnum.NAO_ANALISADA);
    }

    public void analisada() {
        setSituacao(SituacaoEnum.ANALISADA);
    }

    public void rejeitada() {
        setSituacao(SituacaoEnum.REJEITADA);
    }

    public void suspeitaFraude() {
        setSituacao(SituacaoEnum.EM_SUSPEITA_FRAUDE);
    }

    public void analiseHumana() {
        setSituacao(SituacaoEnum.EM_ANALISE_HUMANA);
    }

    public void aprovada() {
        setSituacao(SituacaoEnum.APROVADA);
    }

    public boolean isAnalisada() {

        return getSituacao().equals(SituacaoEnum.ANALISADA);
    }

    public static List<Transacao> findByAgenciaConta(final Long agencia, final Long conta) {
        return list("conta.codigoAgencia = ?1 and conta.codigoConta = ?2", agencia, conta);
    }

    public static Transacao findByUuid(UUID uuid) {
        return find("uuid = ?1", uuid.toString()).firstResult();
    }

    @BsonIgnore
    @JsonIgnore
    private Function<Transacao, TransacaoDTO> transform = transacao -> {
        var transacaoDTO = new TransacaoDTO();
        transacaoDTO.setData(transacao.getData());
        transacaoDTO.setSituacao(transacao.getSituacao());
        transacaoDTO.setUuid(UUID.fromString(transacao.getUuid()));
        transacaoDTO.setValor(transacao.getValor());
        transacaoDTO.setConta(transacao.getConta());
        transacaoDTO.setBeneficiario(transacao.getBeneficiario());
        return transacaoDTO;
    };

    @BsonIgnore
    @JsonIgnore
    public TransacaoDTO convert() {
        return transform.apply(this);
    }
}
