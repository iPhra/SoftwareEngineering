package it.polimi.se2018.Utils;

public interface Observer<T> {

    void update(T message);

}
