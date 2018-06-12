package it.polimi.se2018.client;

import javafx.application.Application;

public abstract class Client extends Application{
    private boolean disconnected;
    private boolean gameEnded;

    public Client() {
        disconnected = false;
        gameEnded = false;
    }

    abstract void startNewGame();
    abstract void handleDisconnection();

    synchronized void waitForAction() {
        while(!disconnected && !gameEnded) {
            try {
                this.wait();
            } catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        if(gameEnded) {
            gameEnded = false;
            startNewGame();
        }
        else {
            disconnected = false;
            handleDisconnection();
        }
    }

    public synchronized void setDisconnected() {
        disconnected = true;
        this.notifyAll();
    }

    public synchronized void setGameEnded() {
        gameEnded = true;
        this.notifyAll();
    }

}
