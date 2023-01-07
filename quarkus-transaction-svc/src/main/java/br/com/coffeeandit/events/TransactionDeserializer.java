package br.com.coffeeandit.events;

import br.com.coffeeandit.dto.TransacaoDTO;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class TransactionDeserializer extends ObjectMapperDeserializer<TransacaoDTO> {
    public TransactionDeserializer() {
        super(TransacaoDTO.class);
    }
}
