package it.polimi.se2018.mvc.controller.placementlogic;

import it.polimi.se2018.mvc.controller.GameManager;
import it.polimi.se2018.mvc.view.ServerView;
import it.polimi.se2018.network.Server;
import org.junit.Assert;
import org.junit.Test;

public class TestGameManager {

    @Test
    public void testManager() {
        GameManager manager = new GameManager(new Server());
        ServerView serverView = new ServerView();
        manager.setServerView(serverView);

        Assert.assertFalse(manager.isMatchCreated());
        manager.startSetup();

        manager.addPlayerID(1);
        manager.addPlayerName(1,"Player 1");
        Assert.assertEquals("Player 1",manager.getNicknameById(1));
        Assert.assertFalse(manager.isMatchPlaying());

        manager.removePlayer(1);
        Assert.assertEquals(null,manager.getNicknameById(1));

        Assert.assertEquals(null,manager.getServerConnection(1));

        manager.addPlayerID(2);
        manager.addPlayerName(2,"Player 2");

        manager.sendWindows();
        Assert.assertTrue(manager.isMatchCreated());
        Assert.assertFalse(manager.isMatchPlaying());

        manager.halt("");
        Assert.assertTrue(manager.isMatchPlaying());
    }
}
