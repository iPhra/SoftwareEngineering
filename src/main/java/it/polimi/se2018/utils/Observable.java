package it.polimi.se2018.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Parametric redefinition of Observable class
 * @param <T> is the generic class to be observed on
 */
public class Observable<T> {

    /**
     * This is the list of all observers of the class
     */
    private final List<Observer<T>> observers = new ArrayList<>();

    /**
     * Used to register an observer of this class
     * @param observer is the class observing this class
     */
    public void register(Observer<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Used to notify every observer
     * @param message is the message to nofiy to the observer
     */
    public void notify(T message){
        synchronized (observers) {
            for(Observer<T> observer : observers){
                observer.update(message);
            }
        }
    }

}
