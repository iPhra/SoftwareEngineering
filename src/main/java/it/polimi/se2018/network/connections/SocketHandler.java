package it.polimi.se2018.network.connections;

import it.polimi.se2018.view.ServerView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketHandler implements Runnable{
    private Server server;
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private ServerView serverView;
    private boolean isOpen;

    SocketHandler(Server server, ServerSocket serverSocket, ExecutorService pool, ServerView serverView) {
        this.server = server;
        this.serverSocket = serverSocket;
        this.pool = pool;
        this.serverView = serverView;
    }

    public void run(){
        isOpen = true;
        while(isOpen){
            try {
                Socket client = serverSocket.accept();
                pool.submit(new SocketServerConnection(client, serverView));
            }catch(IOException e){
                System.err.println(e.getMessage());
            }
        }
    }
}

