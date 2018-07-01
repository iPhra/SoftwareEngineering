package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.GameInstance;
import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.toolcards.*;
import it.polimi.se2018.mvc.view.ServerView;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.DraftMessage;
import it.polimi.se2018.network.messages.requests.PassMessage;
import it.polimi.se2018.network.messages.requests.PlaceMessage;
import it.polimi.se2018.network.messages.requests.ToolCardRequestMessage;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.fail;

public class TestController {
    private ServerView serverView;
    private Board model;

    private void createDraftPool() {
        List<Die> dice = new ArrayList<>();
        dice.add(new Die(6, Color.RED));
        dice.add(new Die(6, Color.BLUE));
        dice.add(new Die(5, Color.YELLOW));
        dice.add(new Die(4, Color.GREEN));
        dice.add(new Die(4, Color.GREEN));
        dice.add(new Die(3, Color.PURPLE));
        dice.add(new Die(2, Color.YELLOW));
        dice.add(new Die(2, Color.BLUE));
        dice.add(new Die(1, Color.RED));
        model.getDraftPool().fillDraftPool(dice);
    }

    private void createToolCards() {
        ToolCard[] toolCards = new ToolCard[3];
        toolCards[0] = Lathekin.instance();
        toolCards[1] = GlazingHammer.instance();
        toolCards[2] = LensCutter.instance();
        model.setToolCards(toolCards);
    }

    @Before
    public void init() {
        GameInstance gameInstance = new GameInstance();
        gameInstance.createGame();
        serverView = gameInstance.getManager().getServerView();
        model = gameInstance.getManager().getModel();
        createDraftPool();
        createToolCards();
    }

    @Test
    public void testDraftingAndPlacing() {
        Assert.assertEquals(9, model.getDraftPool().getAllDice().size()); //initial size is 2n+1

        serverView.handleNetworkInput(new DraftMessage(1,1,10)); //fail because die does not exist
        Assert.assertFalse(model.getPlayerByID(1).hasDraftedDie());

        serverView.handleNetworkInput(new PlaceMessage(1,1,new Coordinate(0,3))); //fail because i haven't drafted
        Assert.assertEquals(null, model.getPlayerByID(1).getWindow().getDie(new Coordinate(0,3)));

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

        serverView.handleNetworkInput(new PassMessage(2,2,false)); //player 2 passes
        serverView.handleNetworkInput(new PassMessage(3,3,false)); //player 3 passes

        serverView.handleNetworkInput(new DraftMessage(4,4,0)); //succesful drafting
        Assert.assertEquals(new Die(6, Color.RED), model.getPlayerByID(4).getDieInHand());
        serverView.handleNetworkInput(new PlaceMessage(4,4,new Coordinate(0,4))); //successful placement
        Assert.assertEquals(new Die(6,Color.RED), model.getPlayerByID(4).getWindow().getDie(new Coordinate(0,4)));

        serverView.handleNetworkInput(new PassMessage(4,4,false)); //player 4 passes

        serverView.handleNetworkInput(new DraftMessage(4,5,0)); //succesful drafting
        Assert.assertEquals(new Die(4, Color.GREEN), model.getPlayerByID(4).getDieInHand());
        serverView.handleNetworkInput(new PlaceMessage(4,5,new Coordinate(1,4))); //successful placement
        Assert.assertEquals(new Die(4,Color.GREEN), model.getPlayerByID(4).getWindow().getDie(new Coordinate(1,4)));
    }

    @Test
    public void testPassing() {
        for(int i=1; i<=80; i++) {
            int current = model.getRound().getCurrentPlayerID();
            Assert.assertEquals(i, model.getStateID()); //stateID is now increased
            Assert.assertFalse(model.getPlayerByID(current).hasDraftedDie()); //no die drafted
            Assert.assertFalse(model.getPlayerByID(current).hasUsedCard()); //no tool card used
            serverView.handleNetworkInput(new PassMessage(current,i,false)); //pass
        }
    }

    @Test
    public void testToolCardRequests() {
        serverView.handleNetworkInput(new ToolCardRequestMessage(1,1,1)); //fail because i can't use it now
        try {
            model.getPlayerByID(1).getCardInUse();
            fail();
        }
        catch(ToolCardException ignored) {
            //correct
        }
    }
}
