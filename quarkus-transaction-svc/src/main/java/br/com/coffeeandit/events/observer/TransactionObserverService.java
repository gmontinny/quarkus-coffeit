package br.com.coffeeandit.events.observer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class TransactionObserverService<E> {

    private List<TransactionObserver> observers = new ArrayList<>();

    @Inject
    private SlackTransactionObserver slackTransactionObserver;

    @PostConstruct
    public void addAllObservers() {
        observers.add(slackTransactionObserver);
    }

    public void addObserver(TransactionObserver transactionObserver) {
        observers.add(transactionObserver);
    }

    public void notification(E item) {
        observers.parallelStream().forEach(transactionObserver -> {
            transactionObserver.doObserver(item);
        });
    }
}
