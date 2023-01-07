package br.com.coffeeandit.model;

import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity(name = "pessoas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class People {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @NotNull(message = "Informar o código da Agência.")
    @Schema(description = "Código da Agência")
    private String nome;

    @Column
    @NotNull(message = "Informar o código da Agência.")
    @Schema(description = "Código da Agência")
    private String cpfCnpj;

    @NotNull(message = "Informar o código da Agência.")
    @Schema(description = "Código da Agência")
    private Long agencia;

    @NotNull(message = "Informar o código da Conta.")
    @Schema(description = "Código da Conta")
    private Long conta;

}
