package br.com.coffeeandit.events.observer;

public interface TransactionObserver<E> {

    void doObserver(E item);
}
