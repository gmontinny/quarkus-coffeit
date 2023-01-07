package br.coffeeandit.limites.dto;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class Conta implements Serializable {

    private static final long serialVersionUID = 2806412403585360625L;

    @Schema(description = "Código da Agência")
    @NotNull(message = "Informar o código da Agência.")
    private Long codigoAgencia;
    @Schema(description = "Código da Conta")
    @NotNull(message = "Informar o código da Conta.")
    private Long codigoConta;

    public Conta() {
    }

    public Long getCodigoAgencia() {
        return codigoAgencia;
    }

    public void setCodigoAgencia(Long codigoAgencia) {
        this.codigoAgencia = codigoAgencia;
    }

    public Long getCodigoConta() {
        return codigoConta;
    }

    public void setCodigoConta(Long codigoConta) {
        this.codigoConta = codigoConta;
    }

    @Override
    public String toString() {
        return "Conta{" +
                "codigoAgencia=" + codigoAgencia +
                ", codigoConta=" + codigoConta +
                '}';
    }
}
