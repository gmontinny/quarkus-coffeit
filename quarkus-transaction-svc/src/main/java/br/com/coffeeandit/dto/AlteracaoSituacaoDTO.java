package br.com.coffeeandit.dto;

import br.com.coffeeandit.model.SituacaoEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AlteracaoSituacaoDTO {


    private SituacaoEnum situacao;
}
