package br.coffeeandit.limites.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NamedQuery(name = "LimiteDiario.findByAgenciaAndConta", query = "SELECT li FROM LimiteDiario li WHERE li.agencia = :agencia AND li.conta= :conta and li.data = :data")
public class LimiteDiario extends PanacheEntity {

    @NotNull(message = "Informar o código da Agência.")
    @Schema(description = "Código da Agência")
    private Long agencia;
    @NotNull(message = "Informar o código da Conta.")
    @Schema(description = "Código da Conta")
    private Long conta;
    @Schema(description = "Data de Limite")
    @NotNull(message = "Data do Limite")
    private LocalDate data;
    @Schema(description = "Valor de Limite")
    @NotNull(message = "valor do limite.")
    private BigDecimal valor;


    public Long getAgencia() {
        return agencia;
    }

    public void setAgencia(Long agencia) {
        this.agencia = agencia;
    }

    public Long getConta() {
        return conta;
    }

    public void setConta(Long conta) {
        this.conta = conta;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }


}
