package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.GameInstance;
import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.view.ServerView;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.DraftMessage;
import it.polimi.se2018.network.messages.requests.PassMessage;
import it.polimi.se2018.network.messages.requests.PlaceMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestController {
    private GameInstance gameInstance;
    private ServerView serverView;
    private  Board model;

    @Before
    public void init() {
        gameInstance = new GameInstance();
        gameInstance.createGame();
        serverView = gameInstance.getManager().getServerView();
        model = gameInstance.getManager().getModel();
    }

    @Test
    public void testDraftingAndPlacing() {
        Assert.assertEquals(9, model.getDraftPool().getAllDice().size()); //initial size is 2n+1

        serverView.handleNetworkInput(new DraftMessage(1,1,10)); //fail because die does not exist
        Assert.assertFalse(model.getPlayerByID(1).hasDraftedDie());

        serverView.handleNetworkInput(new DraftMessage(1,1,2)); //successful drafting
        Assert.assertTrue(model.getPlayerByID(1).hasDraftedDie());
        Assert.assertEquals(new Die(5, Color.YELLOW), model.getPlayerByID(1).getDieInHand());
        Assert.assertEquals(8, model.getDraftPool().getAllDice().size()); //size is decreased

        serverView.handleNetworkInput(new DraftMessage(1,1,3)); //fail because i already drafted
        Assert.assertEquals(new Die(5, Color.YELLOW), model.getPlayerByID(1).getDieInHand());
        Assert.assertEquals(8, model.getDraftPool().getAllDice().size()); //size has not changed

        serverView.handleNetworkInput(new DraftMessage(2,1,3)); //fail because it's not his turn
        Assert.assertFalse(model.getPlayerByID(2).hasDieInHand());
        Assert.assertEquals(8, model.getDraftPool().getAllDice().size()); //size has not changed

        serverView.handleNetworkInput(new PassMessage(1,1,false)); //player 1 passes
        Assert.assertEquals(2, model.getStateID()); //stateID is now 2
        Assert.assertFalse(model.getPlayerByID(1).hasDraftedDie());
        Assert.assertFalse(model.getPlayerByID(1).hasDieInHand()); //die is back in the draft pool, he did not place it

        serverView.handleNetworkInput(new DraftMessage(2,1,1)); //fail because of wrong stateID
        Assert.assertFalse(model.getPlayerByID(2).hasDieInHand());
        Assert.assertEquals(9, model.getDraftPool().getAllDice().size()); //size is back to 9

        serverView.handleNetworkInput(new DraftMessage(2,2,1)); //succesful drafting
        Assert.assertTrue(model.getPlayerByID(2).hasDraftedDie());
        Assert.assertEquals(new Die(6, Color.BLUE), model.getPlayerByID(2).getDieInHand());
        Assert.assertEquals(8, model.getDraftPool().getAllDice().size()); //size has decreased

        serverView.handleNetworkInput(new PlaceMessage(2,2,new Coordinate(0,3))); //failed placement
        Assert.assertTrue(model.getPlayerByID(2).hasDieInHand()); //die is still in hand
        Assert.assertTrue(model.getPlayerByID(2).hasDraftedDie());
        Assert.assertEquals(null, model.getPlayerByID(2).getWindow().getDie(new Coordinate(0,4)));

        serverView.handleNetworkInput(new PlaceMessage(2,2,new Coordinate(0,4))); //successful placement
        Assert.assertFalse(model.getPlayerByID(2).hasDieInHand()); //die is not in hand
        Assert.assertTrue(model.getPlayerByID(2).hasDraftedDie());
        Assert.assertEquals(new Die(6,Color.BLUE), model.getPlayerByID(2).getWindow().getDie(new Coordinate(0,4)));
    }
}
