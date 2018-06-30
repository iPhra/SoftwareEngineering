package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.GameInstance;
import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.view.ServerView;
import it.polimi.se2018.network.messages.requests.DraftMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestController {
    private GameInstance gameInstance;
    private ServerView serverView;

    @Before
    public void init() {
        gameInstance = new GameInstance();
        gameInstance.createGame();
        serverView = gameInstance.getManager().getServerView();
    }

    @Test
    public void testDrafting() {
        serverView.handleNetworkInput(new DraftMessage(1,1,2));
        Assert.assertEquals(new Die(5, Color.YELLOW),gameInstance.getManager().getModel().getPlayerByID(1).getDieInHand());
    }
}
