package it.polimi.se2018.network.connections;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketHandler implements Runnable {
    private Server server;
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private boolean isOpen;

    SocketHandler(Server server, ServerSocket serverSocket) {
        this.server = server;
        this.serverSocket = serverSocket;
        pool = Executors.newCachedThreadPool();
    }

    public void run(){
        isOpen = true;
        while(isOpen){
            try {
                Socket client = serverSocket.accept();
                ServerConnection socketServerConnection = new SocketServerConnection(client, server);
                pool.submit((SocketServerConnection) socketServerConnection);
            }catch(IOException e){
                System.err.println(e.getMessage());
            }
        }
    }
}

