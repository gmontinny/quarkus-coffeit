package br.coffeeandit.limites.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema
public enum TipoTransacao {


    PAGAMENTO_TRIBUTOS,
    PAGAMENTO_TITULOS,
    TED,
    DOC;
}
