package it.polimi.se2018.client;


import it.polimi.se2018.client.network.ClientConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class Client {
    protected boolean setup;
    protected int playerID;
    protected String nickname;
    boolean isSocket;
    int port;
    String ip;
    ClientConnection clientConnection;

    public Client() {
        setup = true;
    }

    public abstract void setGameEnded();
    public abstract void setDisconnected();

    public void setSocket(boolean socket) {
        isSocket = socket;
    }

    public boolean isSocket() {
        return isSocket;
    }

    public void setDefaultParams() {
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("NetworkProperties.txt");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String[] tokens = sb.toString().split(";");
            ip = tokens[0].split(":")[1];
            port = Integer.valueOf((isSocket? tokens[2].split(":")[1] : tokens[1].split(":")[1]));
        }
        catch (IOException e) {
            System.exit(1);
        }
    }
}
