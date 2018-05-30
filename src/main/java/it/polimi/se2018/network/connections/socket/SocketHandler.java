package it.polimi.se2018.network.connections.socket;

import it.polimi.se2018.network.Server;
import it.polimi.se2018.network.connections.ServerConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketHandler implements Runnable {
    private final Server server;
    private final ServerSocket serverSocket;
    private final ExecutorService pool;
    private boolean isOpen;

    public SocketHandler(Server server, ServerSocket serverSocket) {
        this.server = server;
        this.serverSocket = serverSocket;
        pool = Executors.newCachedThreadPool();
        isOpen = true;
    }

    public void stop() {
        isOpen = false;
        pool.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
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
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }
}

