package br.coffeeandit.config;

import br.coffeeandit.limites.dto.RequisicaoTransacaoDTO;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class TransactionDeserializer extends ObjectMapperDeserializer<RequisicaoTransacaoDTO> {
    public TransactionDeserializer() {
        super(RequisicaoTransacaoDTO.class);
    }
}
