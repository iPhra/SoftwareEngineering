package it.polimi.se2018.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Parametric redefinition of Observable class
 * @param <T> is the generic class to be observed on
 */
public class Observable<T> {

    private final List<Observer<T>> observers = new ArrayList<>();

    public void register(Observer<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void notify(T message){
        synchronized (observers) {
            for(Observer<T> observer : observers){
                observer.update(message);
            }
        }
    }

}
