package it.polimi.se2018.network;
import it.polimi.se2018.mvc.controller.GameManager;
import it.polimi.se2018.network.connections.ServerConnection;
import it.polimi.se2018.network.connections.rmi.RMIManager;
import it.polimi.se2018.network.connections.socket.SocketHandler;
import it.polimi.se2018.utils.Stopper;
import it.polimi.se2018.utils.WaitingThread;
import it.polimi.se2018.mvc.view.ServerView;

import java.io.*;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Stopper {
    private static final int PORT = 1234;
    private static int matchID = 1;
    private static int playerNumber = 0; //identifies just the player, without the match
    private final Map<Integer, GameManager> matches;
    private WaitingThread clock;
    private RMIManager remoteManager;
    private SocketHandler socketHandler;

    private Server() {
        matches = new HashMap<>();
    }

    private void startTimer() {
        Duration timeout = Duration.ofSeconds(15);
        clock = new WaitingThread(timeout, this);
        clock.start();
    }

    private void startGame(GameManager manager) {
        clock.interrupt();
        incrementMatchID();
        manager.sendWindows();
    }

    private GameManager createManager(int match) {
        GameManager manager = new GameManager();
        manager.setServerView(new ServerView());
        manager.startSetup();
        matches.put(match,manager);
        return manager;
    }

    private void addPlayer(GameManager manager, int playerID, String playerName, ServerConnection serverConnection) {
        manager.addPlayerName(playerID,playerName);
        manager.addServerConnection(playerID, serverConnection);
        manager.addPlayerID(playerID);
        serverConnection.setServerView(manager.getServerView());
        manager.getServerView().addServerConnection(playerID,serverConnection);
    }

    private void removePlayer(int playerID) {
        int match = playerID/1000;
        GameManager manager = matches.get(match);
        if(manager.playersNumber()==2) {
            clock.interrupt();
        }
        manager.removePlayer(playerID);
    }

    private static void incrementMatchID() {matchID++;}

    private boolean isMatchFull() {
        return matches.get(matchID).playersNumber()==4;
    }

    private void createRMIRegistry () {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            remoteManager = new RMIManager(this,registry);
            registry.rebind("RemoteManager", UnicastRemoteObject.exportObject(remoteManager,0));
        }
        catch (RemoteException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    private void startSocketConnection(){
        try{
            ServerSocket serverSocket = new ServerSocket(PORT);
            socketHandler = new SocketHandler(this,serverSocket);
            Thread thread = new Thread(socketHandler);
            thread.start();
        }catch(IOException e){
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    private void close() {
        socketHandler.stop();
        remoteManager.closeConnection();
    }

    public void setPlayer(int playerID, String playerName, ServerConnection serverConnection) {
        int match = playerID/1000;
        GameManager manager;
        if(matches.get(match)==null) manager = createManager(match);
        else manager = matches.get(match);
        addPlayer(manager,playerID,playerName,serverConnection);
        if (manager.playersNumber() == 2) startTimer();
        if (isMatchFull()) startGame(manager);
    }

    public synchronized void handleDisconnection(int playerID) {
        int match = playerID/1000;
        GameManager manager = matches.get(match);
        if(manager.isMatchPlaying()) {
            manager.setDisconnected(playerID);
        }
        else {
            ServerConnection connection = manager.getServerConnection(playerID);
            removePlayer(playerID);
            connection.stop();
        }
    }

    public boolean checkName(int playerID, String playerName) {
        return !(matches.get(playerID/1000)==null || matches.get(playerID/1000).checkName(playerName));
    }

    public static int generateID() {
        playerNumber++;
        return (matchID*1000)+playerNumber;
    }

    public static void decrementID() {
        playerNumber--;
    }

    @Override
    public void halt(String message) {
        if (matches.get(matchID).playersNumber() >= 2) {
            matches.get(matchID).sendWindows();
            incrementMatchID();
        }
    }

    /*public int checkEmail(String email) {
        int playerID = -1;
        String filename = "resources\\setting.txt";
        BufferedReader br = null;
        FileReader fr = null;
        try {
            //br = new BufferedReader(new FileReader(filename));
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] parsedLine = sCurrentLine.split(" ");
                if (parsedLine[0] == email) {
                    if (parsedLine.length < 2) {
                        break;
                    }
                    else {
                        playerID = Integer.parseInt(parsedLine[1]);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return playerID;
    }

    public void writePlayerID(String email, int playerID) {
        String filename = "resources\\setting.txt";
        BufferedReader br = null;
        FileReader fr = null;

        boolean find = false;
        try {
            //br = new BufferedReader(new FileReader(FILENAME));
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] parsedLine = sCurrentLine.split(" ");
                if (parsedLine[0].equals(email)) {
                    find = true;
                    if (parsedLine.length > 1) {
                        break;
                    }
                    else {
                        playerID = Integer.parseInt(parsedLine[1]);
                        break;
                    }
                }
            }
            if (!find){
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                    writer.append(email + " " + playerID);
                    writer.append("\r\n");
                    writer.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }*/


    public static void main(String[] args) {
        Server server = new Server();
        server.startSocketConnection();
        server.createRMIRegistry();
        new PrintStream(System.out).println("Listening...");
    }
}
