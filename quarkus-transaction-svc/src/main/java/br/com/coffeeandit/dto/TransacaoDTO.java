package br.com.coffeeandit.dto;

import br.com.coffeeandit.model.SituacaoEnum;
import br.com.coffeeandit.model.Transacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@Getter
@Setter
@Schema(description = "Objeto de transporte para o envio de uma promessa de transação")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransacaoDTO {


    @JsonIgnore
    private Function<TransacaoDTO, Transacao> transform = transacaoDTO -> {
        var transacao = new Transacao();
        transacao.setData(transacaoDTO.getData());
        transacao.setSituacao(transacaoDTO.getSituacao());
        transacao.setUuid(transacaoDTO.getUuid().toString());
        transacao.setValor(transacaoDTO.getValor());
        transacao.setConta(transacaoDTO.getConta());
        transacao.setBeneficiario(transacaoDTO.getBeneficiario());
        return transacao;
    };

    private static final long serialVersionUID = 2806421523585360625L;
    @Schema(description = "Valor da transação")
    private BigDecimal valor;
    @Schema(description = "Data/hora/minuto e segundo da transação")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime data;
    @Schema(description = "Conta de origem da transação")
    private Conta conta;
    @Schema(description = "Beneficiário da transação")
    private BeneficiatioDto beneficiario;
    @Schema(description = "Tipo de transação")
    private TipoTransacao tipoTransacao;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransacaoDTO that = (TransacaoDTO) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Schema(description = "Código de identificação da transação")
    private UUID uuid;
    @Schema(description = "Situação da transação")
    private SituacaoEnum situacao;

    public void naoAnalisada() {
        setSituacao(SituacaoEnum.NAO_ANALISADA);
    }


    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public BeneficiatioDto getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(BeneficiatioDto beneficiario) {
        this.beneficiario = beneficiario;
    }

    public TipoTransacao getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(TipoTransacao tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public SituacaoEnum getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoEnum situacao) {
        this.situacao = situacao;
    }

    @JsonIgnore
    public void analisada() {
        setSituacao(SituacaoEnum.ANALISADA);
    }

    public boolean isAnalisada() {
        return SituacaoEnum.ANALISADA.equals(getSituacao());
    }

    public void aprovada() {
        setSituacao(SituacaoEnum.APROVADA);
    }

    public void suspeitaFraude() {
        setSituacao(SituacaoEnum.EM_SUSPEITA_FRAUDE);
    }

    public void rejeitada() {
        setSituacao(SituacaoEnum.REJEITADA);
    }

    public boolean isRejeitada() {
        return SituacaoEnum.REJEITADA.equals(getSituacao());
    }

    public void analiseHumana() {
        setSituacao(SituacaoEnum.EM_ANALISE_HUMANA);
    }

    public void confirmadaUsuario() {
        setSituacao(SituacaoEnum.CONFIRMADA_USUARIO);
    }

    public void aceitaProcessamento() {
        setSituacao(SituacaoEnum.ACEITA_PROCESSAMENTO);
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "valor=" + valor +
                ", data=" + data +
                ", conta=" + conta +
                ", beneficiario=" + beneficiario +
                ", tipoTransacao=" + tipoTransacao +
                ", uuid=" + uuid +
                ", situacao=" + situacao +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @JsonIgnore
    public Transacao convert() {
        return transform.apply(this);
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    private String signature;
}





