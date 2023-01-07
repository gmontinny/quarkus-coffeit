package br.com.coffeeandit.business;

import br.com.coffeeandit.dto.TransacaoDTO;
import br.com.coffeeandit.model.Transacao;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class TransacaoService {

    public static final int LIMITE_MAXIMO = 10000;

    public boolean analisarTransacao(final TransacaoDTO transacaoDTO) {
        var optionalTransacaoDTO = buscarTransacao(transacaoDTO);
        if (optionalTransacaoDTO.isPresent()) {
            final TransacaoDTO dto = optionalTransacaoDTO.get();
            if (dto.getValor().compareTo(BigDecimal.valueOf(LIMITE_MAXIMO)) < 0) {
                dto.analisada();
                merge(dto);
                return true;
            }
        }
        return false;

    }

    public TransacaoDTO aprovarTransacao(final TransacaoDTO transacaoDTO) {
        var optionalTransacaoDTO = buscarTransacao(transacaoDTO);
        if (optionalTransacaoDTO.isPresent()) {
            final TransacaoDTO dto = optionalTransacaoDTO.get();
            if (analisarTransacao(dto)) {
                dto.aprovada();
                atualizarTransacao(dto);
                return dto;

            } else {
                rejeitarTransacao(transacaoDTO);
            }
        }
        throw new IllegalArgumentException("Não foi possivel atualizar a transacao.");

    }

    public void atualizarTransacao(TransacaoDTO dto) {
        var transacao = Transacao.findByUuid(dto.getUuid());
        transacao.setSituacao(dto.getSituacao());
        transacao.persistOrUpdate();
    }

    public TransacaoDTO inserirTransacao(final TransacaoDTO transacaoDTO) {
        transacaoDTO.setUuid(UUID.randomUUID());
        transacaoDTO.naoAnalisada();
        merge(transacaoDTO);
        var optionalTransacaoDTO = buscarTransacao(transacaoDTO);
        if (optionalTransacaoDTO.isPresent()) {
            return optionalTransacaoDTO.get();
        }
        throw new IllegalStateException("Não foi possivel inserir a transação");
    }

    private Optional<TransacaoDTO> buscarTransacao(TransacaoDTO transacaoDTO) {
        return Optional.ofNullable(Transacao.findByUuid(transacaoDTO.getUuid()).convert());
    }

    public TransacaoDTO rejeitarTransacao(final TransacaoDTO transacaoDTO) {
        var optionalTransacaoDTO = buscarTransacao(transacaoDTO);
        if (optionalTransacaoDTO.isPresent()) {
            final TransacaoDTO dto = optionalTransacaoDTO.get();
            dto.rejeitada();
            atualizarTransacao(dto);
            return dto;
        }
        throw new IllegalArgumentException("Não foi possivel localizar a transacao");
    }

    private void merge(TransacaoDTO dto) {
        var transacao = dto.convert();
        transacao.persistOrUpdate();
    }
}
