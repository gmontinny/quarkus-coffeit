package br.com.coffeeandit.events.observer;


import br.com.coffeeandit.business.SlackAlertService;
import br.com.coffeeandit.dto.TransacaoDTO;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SlackTransactionObserver implements TransactionObserver<TransacaoDTO> {

    @Inject
    private SlackAlertService slackAlertService;


    @Override
    public void doObserver(final TransacaoDTO item) {

        slackAlertService.enviarMensagem(item);

    }
}
