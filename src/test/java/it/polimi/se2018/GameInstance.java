package it.polimi.se2018;

import it.polimi.se2018.mvc.controller.GameManager;
import it.polimi.se2018.mvc.model.Round;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.view.ServerView;
import it.polimi.se2018.network.Server;
import it.polimi.se2018.network.messages.requests.SetupMessage;

import java.util.ArrayList;
import java.util.List;

public class GameInstance {
    private GameManager manager;
    private ServerView serverView;

    private void createPlayers() {
        for(int i=1; i<5; i++) {
            manager.addPlayerName(i, "Player " + String.valueOf(i));
            manager.addPlayerID(i);
        }
    }

    private void createSetupMessages() {
        WindowDatabase database = new WindowDatabase();
        List<Window> windows = new ArrayList<>();
        windows.add(database.generateWindowByTitle("KALEIDOSCOPIC DREAM"));
        windows.add(database.generateWindowByTitle("FRACTAL DROPS"));
        windows.add(database.generateWindowByTitle("LUX MUNDI"));
        windows.add(database.generateWindowByTitle("GRAVITAS"));
        for(int i=1;i<5;i++) {
            serverView.handleNetworkInput(new SetupMessage(i,0,windows.get(i-1)));
        }
    }

    private void createFirstRound() {
        List<Integer> order = new ArrayList<>();
        for(int i=1;i<5;i++) {
            order.add(i);
        }
        manager.getModel().setRound(new Round(order,1));
    }

    public GameManager getManager() {
        return manager;
    }

    public void createGame() {
        manager = new GameManager(new Server());
        serverView = new ServerView();
        manager.setServerView(serverView);
        manager.startSetup();
        createPlayers();
        manager.sendWindows();
        createSetupMessages();
        createFirstRound();
    }
}
