package it.polimi.se2018.network.connections.socket;

import it.polimi.se2018.network.Server;
import it.polimi.se2018.network.connections.ServerConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketHandler implements Runnable {
    private final Server server;
    private final ServerSocket serverSocket;
    private final ExecutorService pool;
    private final boolean isOpen;

    public SocketHandler(Server server, ServerSocket serverSocket) {
        this.server = server;
        this.serverSocket = serverSocket;
        pool = Executors.newCachedThreadPool();
        isOpen = true;
    }

    public void run() {
        try {
            while (isOpen) {
                Socket client = serverSocket.accept();
                ServerConnection socketServerConnection = new SocketServerConnection(client, server);
                pool.submit((SocketServerConnection) socketServerConnection);
            }
        }
        catch (IOException e) {
            System.exit(1);
        }
    }
}

