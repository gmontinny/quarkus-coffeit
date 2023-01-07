package br.coffeeandit.limites.dto;


import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema
public enum SituacaoEnum {

    ACEITA_PROCESSAMENTO,
    LIMITE_EXCEDIDO,
    ANALISADA,
    NAO_ANALISADA,
    EM_ANALISE_HUMANA,
    APROVADA,
    EM_SUSPEITA_FRAUDE,
    RISCO_CONFIRMADO,
    CONFIRMADA_USUARIO;


}
