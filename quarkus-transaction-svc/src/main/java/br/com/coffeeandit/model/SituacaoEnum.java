package br.com.coffeeandit.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema
public enum SituacaoEnum {

    ACEITA_PROCESSAMENTO,
    LIMITE_EXCEDIDO,
    APROVADA,
    ANALISADA,
    NAO_ANALISADA,
    EM_ANALISE_HUMANA,
    EM_SUSPEITA_FRAUDE,
    RISCO_CONFIRMADO,
    REJEITADA,
    CONFIRMADA_USUARIO;


}
