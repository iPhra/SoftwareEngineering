package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.GameInstance;
import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.toolcards.*;
import it.polimi.se2018.mvc.view.ServerView;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.*;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestToolCardController {
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
        toolCards[0] = CopperFoilBurnisher.instance();
        toolCards[1] = CorkBackedStraightedge.instance();
        toolCards[2] = EglomiseBrush.instance();
        model.setToolCards(toolCards);
    }

    @Before
    public void init() {
        GameInstance gameInstance = new GameInstance();
        gameInstance.createGame();
        GameManager gameManager = gameInstance.getManager();
        serverView = gameManager.getServerView();
        model = gameManager.getModel();
        createDraftPool();
        createToolCards();
    }

    @Test
    public void testToolCardCopper() {
        try {
            Die die = model.getDraftPool().getDie(0);
            serverView.handleNetworkInput(new DraftMessage(1, 1, 0)); //6RED
            serverView.handleNetworkInput(new PlaceMessage(1, 1, new Coordinate(0, 3)));
            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 1, 0));
            ToolCardMessage toolCardMessage = new ToolCardMessage(1, 1, 0);
            toolCardMessage.addStartingPosition(new Coordinate(0, 3));
            toolCardMessage.addFinalPosition(new Coordinate(0, 0));
            serverView.handleNetworkInput(toolCardMessage); //fail because you can't move your first die
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(0, 0)).isEmpty()); //final position is empty
            Assert.assertEquals(die, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(0, 3)).getDie()); //starting position has 6R
            serverView.handleNetworkInput(new PassMessage(1, 1, false));
            serverView.handleNetworkInput(new PassMessage(2, 2, false));
            serverView.handleNetworkInput(new PassMessage(3, 3, false));
            serverView.handleNetworkInput(new PassMessage(4, 4, false));
            serverView.handleNetworkInput(new PassMessage(4, 5, false));
            serverView.handleNetworkInput(new PassMessage(3, 6, false));
            serverView.handleNetworkInput(new PassMessage(2, 7, false));

            Die dieTwo = model.getDraftPool().getDie(1);
            serverView.handleNetworkInput(new DraftMessage(1, 8, 1)); //5yellow
            serverView.handleNetworkInput(new PlaceMessage(1, 8, new Coordinate(1, 2)));

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 8, 0));
            ToolCardMessage toolCardMessage2 = new ToolCardMessage(1, 8, 0);
            toolCardMessage2.addStartingPosition(new Coordinate(1, 2));
            toolCardMessage2.addFinalPosition(new Coordinate(2, 2));
            serverView.handleNetworkInput(toolCardMessage2); //fail because you can't place the die in final position
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(2, 2)).isEmpty());
            Assert.assertEquals(dieTwo, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(1, 2)).getDie());

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 8, 0));
            ToolCardMessage toolCardMessage3 = new ToolCardMessage(1, 8, 0);
            toolCardMessage3.addStartingPosition(new Coordinate(3, 3));
            toolCardMessage3.addFinalPosition(new Coordinate(2, 2));
            serverView.handleNetworkInput(toolCardMessage3); //fail because you can't place the die in final position
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(3, 3)).isEmpty());
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(2, 2)).isEmpty());

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 8, 0));
            ToolCardMessage toolCardMessage4 = new ToolCardMessage(1, 8, 0);
            toolCardMessage4.addStartingPosition(new Coordinate(1, 2));
            toolCardMessage4.addFinalPosition(new Coordinate(1, 3));
            serverView.handleNetworkInput(toolCardMessage4); //move dieTwo to final position
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(1, 2)).isEmpty());
            Assert.assertEquals(dieTwo, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(1, 3)).getDie());
        } catch (NoDieException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToolCardCork() {
        try {
            serverView.handleNetworkInput(new DraftMessage(1, 1, 0)); //6RED
            serverView.handleNetworkInput(new PlaceMessage(1, 1, new Coordinate(0, 3)));
            serverView.handleNetworkInput(new PassMessage(1, 1, false));
            serverView.handleNetworkInput(new PassMessage(2, 2, false));
            serverView.handleNetworkInput(new PassMessage(3, 3, false));
            serverView.handleNetworkInput(new PassMessage(4, 4, false));
            serverView.handleNetworkInput(new PassMessage(4, 5, false));
            serverView.handleNetworkInput(new PassMessage(3, 6, false));
            serverView.handleNetworkInput(new PassMessage(2, 7, false));
            Die die = model.getDraftPool().getDie(1);
            serverView.handleNetworkInput(new DraftMessage(1, 8, 1)); //5yellow

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 8, 1));
            ToolCardMessage toolCardMessage = new ToolCardMessage(1, 8, 1);
            toolCardMessage.addFinalPosition(new Coordinate(3, 3));
            serverView.handleNetworkInput(toolCardMessage);
            Assert.assertTrue(model.getPlayerByID(1).hasDieInHand());
            Assert.assertEquals(die, model.getPlayerByID(1).getDieInHand());
            Assert.assertFalse(model.getPlayerByID(1).hasUsedCard());

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 8, 1));
            ToolCardMessage toolCardMessage2 = new ToolCardMessage(1, 8, 1);
            toolCardMessage2.addFinalPosition(new Coordinate(3, 1));
            serverView.handleNetworkInput(toolCardMessage2);
            Assert.assertFalse(model.getPlayerByID(1).hasDieInHand());
            Assert.assertEquals(die, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(3, 1)).getDie());
            Assert.assertTrue(model.getPlayerByID(1).hasUsedCard());
        } catch (NoDieException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testToolCardEglomise() {
        try {
            Die die = model.getDraftPool().getDie(0);
            serverView.handleNetworkInput(new DraftMessage(1, 1, 0)); //6RED
            serverView.handleNetworkInput(new PlaceMessage(1, 1, new Coordinate(0, 3)));
            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 1, 2));
            ToolCardMessage toolCardMessage = new ToolCardMessage(1, 1, 2);
            toolCardMessage.addStartingPosition(new Coordinate(0, 3));
            toolCardMessage.addFinalPosition(new Coordinate(0, 0));
            serverView.handleNetworkInput(toolCardMessage); //fail because you can't move your first die
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(0, 0)).isEmpty()); //final position is empty
            Assert.assertEquals(die, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(0, 3)).getDie()); //starting position has 6R
            serverView.handleNetworkInput(new PassMessage(1, 1, false));
            serverView.handleNetworkInput(new PassMessage(2, 2, false));
            serverView.handleNetworkInput(new PassMessage(3, 3, false));
            serverView.handleNetworkInput(new PassMessage(4, 4, false));
            serverView.handleNetworkInput(new PassMessage(4, 5, false));
            serverView.handleNetworkInput(new PassMessage(3, 6, false));
            serverView.handleNetworkInput(new PassMessage(2, 7, false));

            Die dieTwo = model.getDraftPool().getDie(1);
            serverView.handleNetworkInput(new DraftMessage(1, 8, 1)); //5yellow
            serverView.handleNetworkInput(new PlaceMessage(1, 8, new Coordinate(1, 2)));

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 8, 2));
            ToolCardMessage toolCardMessage2 = new ToolCardMessage(1, 8, 2);
            toolCardMessage2.addStartingPosition(new Coordinate(1, 2));
            toolCardMessage2.addFinalPosition(new Coordinate(2, 2));
            serverView.handleNetworkInput(toolCardMessage2); //fail because you can't place the die in final position
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(2, 2)).isEmpty());
            Assert.assertEquals(dieTwo, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(1, 2)).getDie());

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 8, 2));
            ToolCardMessage toolCardMessage3 = new ToolCardMessage(1, 8, 2);
            toolCardMessage3.addStartingPosition(new Coordinate(3, 3));
            toolCardMessage3.addFinalPosition(new Coordinate(2, 2));
            serverView.handleNetworkInput(toolCardMessage3); //fail because you can't place the die in final position
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(3, 3)).isEmpty());
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(2, 2)).isEmpty());

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 8, 2));
            ToolCardMessage toolCardMessage4 = new ToolCardMessage(1, 8, 2);
            toolCardMessage4.addStartingPosition(new Coordinate(1, 2));
            toolCardMessage4.addFinalPosition(new Coordinate(1, 3));
            serverView.handleNetworkInput(toolCardMessage4); //move dieTwo to final position
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(1, 2)).isEmpty());
            Assert.assertEquals(dieTwo, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(1, 3)).getDie());
        } catch (NoDieException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToolCardFluxBrush() {
        try {
            ToolCard[] toolCards = new ToolCard[3];
            toolCards[0] = FluxBrush.instance();
            toolCards[1] = CorkBackedStraightedge.instance();
            toolCards[2] = EglomiseBrush.instance();
            model.setToolCards(toolCards);
            Die die = model.getDraftPool().getDie(0);
            serverView.handleNetworkInput(new DraftMessage(1, 1, 0)); //6RED
            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 1, 0));
            ToolCardMessage toolCardMessage = new ToolCardMessage(1, 1, 0);
            serverView.handleNetworkInput(toolCardMessage);
            Assert.assertEquals(die.getColor(), model.getPlayerByID(1).getDieInHand().getColor());
        } catch (NoDieException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToolCardFluxRemover() {
        try {
            ToolCard[] toolCards = new ToolCard[3];
            toolCards[0] = FluxRemover.instance();
            toolCards[1] = CorkBackedStraightedge.instance();
            toolCards[2] = EglomiseBrush.instance();
            model.setToolCards(toolCards);
            Die die = model.getDraftPool().getDie(0);
            serverView.handleNetworkInput(new DraftMessage(1, 1, 0)); //6RED
            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 1, 0));
            ToolCardMessage toolCardMessage = new ToolCardMessage(1, 1, 0);
            serverView.handleNetworkInput(toolCardMessage);
        } catch (NoDieException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToolGlazingHammer() {
        try {
            ToolCard[] toolCards = new ToolCard[3];
            toolCards[0] = GlazingHammer.instance();
            toolCards[1] = CorkBackedStraightedge.instance();
            toolCards[2] = EglomiseBrush.instance();
            model.setToolCards(toolCards);
            serverView.handleNetworkInput(new PassMessage(1, 1, false));
            serverView.handleNetworkInput(new PassMessage(2, 2, false));
            serverView.handleNetworkInput(new PassMessage(3, 3, false));
            serverView.handleNetworkInput(new PassMessage(4, 4, false));
            serverView.handleNetworkInput(new PassMessage(4, 5, false));
            serverView.handleNetworkInput(new PassMessage(3, 6, false));
            serverView.handleNetworkInput(new PassMessage(2, 7, false));
            List<Die> dice = model.getDraftPool().getAllDice();
            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 8, 0));
            ToolCardMessage toolCardMessage = new ToolCardMessage(1, 8, 0);
            serverView.handleNetworkInput(toolCardMessage);
            int i = 0;
            for (Die die : dice) {
                Assert.assertEquals(die.getColor(), model.getDraftPool().getDie(i).getColor());
                i++;
            }
        } catch (NoDieException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToolCardGrozingPliers() {
        try {
            ToolCard[] toolCards = new ToolCard[3];
            toolCards[0] = GrozingPliers.instance();
            toolCards[1] = CorkBackedStraightedge.instance();
            toolCards[2] = EglomiseBrush.instance();
            model.setToolCards(toolCards);
            Die die = model.getDraftPool().getDie(0);
            serverView.handleNetworkInput(new DraftMessage(1, 1, 0)); //6RED

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 1, 0));
            ToolCardMessage toolCardMessage = new ToolCardMessage(1, 1, 0);
            toolCardMessage.setCondition(true);
            serverView.handleNetworkInput(toolCardMessage);
            Assert.assertFalse(model.getPlayerByID(1).hasUsedCard());

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 1, 0));
            ToolCardMessage toolCardMessage2 = new ToolCardMessage(1, 1, 0);
            toolCardMessage2.setCondition(false);
            serverView.handleNetworkInput(toolCardMessage2);
            Assert.assertTrue(model.getPlayerByID(1).hasUsedCard());
            Assert.assertEquals(die.getValue() - 1, model.getPlayerByID(1).getDieInHand().getValue());
        } catch (NoDieException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToolCardLathekin() {
        try {
            ToolCard[] toolCards = new ToolCard[3];
            toolCards[0] = Lathekin.instance();
            toolCards[1] = CorkBackedStraightedge.instance();
            toolCards[2] = EglomiseBrush.instance();
            model.setToolCards(toolCards);
            Die die = model.getDraftPool().getDie(0);
            Assert.assertEquals(die, new Die(6, Color.RED));
            serverView.handleNetworkInput(new DraftMessage(1, 1, 0)); //6RED
            serverView.handleNetworkInput(new PlaceMessage(1, 1, new Coordinate(0, 2)));
            serverView.handleNetworkInput(new PassMessage(1, 1, false));
            serverView.handleNetworkInput(new PassMessage(2, 2, false));
            serverView.handleNetworkInput(new PassMessage(3, 3, false));
            serverView.handleNetworkInput(new PassMessage(4, 4, false));
            serverView.handleNetworkInput(new PassMessage(4, 5, false));
            serverView.handleNetworkInput(new PassMessage(3, 6, false));
            serverView.handleNetworkInput(new PassMessage(2, 7, false));

            Die dieTwo = model.getDraftPool().getDie(1);
            Assert.assertEquals(dieTwo, new Die(5, Color.YELLOW));
            serverView.handleNetworkInput(new DraftMessage(1, 8, 1)); //5Y
            serverView.handleNetworkInput(new PlaceMessage(1, 8, new Coordinate(1, 2)));
            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 8, 0));
            ToolCardMessage toolCardMessage = new ToolCardMessage(1, 8, 0);
            toolCardMessage.addStartingPosition(new Coordinate(0, 2));
            toolCardMessage.addFinalPosition(new Coordinate(0, 0));
            toolCardMessage.addStartingPosition(new Coordinate(0, 0));
            toolCardMessage.addFinalPosition(new Coordinate(0, 0));
            serverView.handleNetworkInput(toolCardMessage);

            serverView.handleNetworkInput(new PassMessage(1, 8, false));
            serverView.handleNetworkInput(new PassMessage(2, 9, false));
            serverView.handleNetworkInput(new PassMessage(3, 10, false));
            serverView.handleNetworkInput(new PassMessage(4, 11, false));

            createDraftPool();
            Die dieThree = model.getDraftPool().getDie(4);
            Assert.assertEquals(dieThree, new Die(4, Color.GREEN));
            serverView.handleNetworkInput(new DraftMessage(1, 12, 4)); //4G
            serverView.handleNetworkInput(new PlaceMessage(1, 12, new Coordinate(1, 1)));
            Assert.assertEquals(dieThree, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(1, 1)).getDie());

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 12, 0));
            ToolCardMessage toolCardMessage2 = new ToolCardMessage(1, 12, 0);
            toolCardMessage2.addStartingPosition(new Coordinate(0, 2));
            toolCardMessage2.addFinalPosition(new Coordinate(0, 1));
            toolCardMessage2.addStartingPosition(new Coordinate(1, 2));
            toolCardMessage2.addFinalPosition(new Coordinate(1, 2));
            serverView.handleNetworkInput(toolCardMessage2);
            Assert.assertEquals(die, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(0, 2)).getDie());
            Assert.assertEquals(dieTwo, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(1, 2)).getDie());
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(0, 1)).isEmpty());
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(0, 1)).isEmpty());


            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 12, 0));
            ToolCardMessage toolCardMessage3 = new ToolCardMessage(1, 12, 0);
            toolCardMessage3.addStartingPosition(new Coordinate(0, 2));
            toolCardMessage3.addFinalPosition(new Coordinate(2, 2));
            toolCardMessage3.addStartingPosition(new Coordinate(1, 2));
            toolCardMessage3.addFinalPosition(new Coordinate(2, 1));
            serverView.handleNetworkInput(toolCardMessage3);
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(0, 2)).isEmpty());
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(1, 2)).isEmpty());
            Assert.assertEquals(die, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(2, 2)).getDie());
            Assert.assertEquals(dieTwo, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(2, 1)).getDie());


        } catch (NoDieException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToolCardLensCutter() {
        try {
            ToolCard[] toolCards = new ToolCard[3];
            toolCards[0] = LensCutter.instance();
            toolCards[1] = CorkBackedStraightedge.instance();
            toolCards[2] = EglomiseBrush.instance();
            model.setToolCards(toolCards);

            serverView.handleNetworkInput(new PassMessage(1, 1, false));
            serverView.handleNetworkInput(new PassMessage(2, 2, false));
            serverView.handleNetworkInput(new PassMessage(3, 3, false));
            serverView.handleNetworkInput(new PassMessage(4, 4, false));
            serverView.handleNetworkInput(new PassMessage(4, 5, false));
            serverView.handleNetworkInput(new PassMessage(3, 6, false));
            serverView.handleNetworkInput(new PassMessage(2, 7, false));
            serverView.handleNetworkInput(new PassMessage(1, 8, false));
            createDraftPool();
            Die die = model.getDraftPool().getDie(0);
            serverView.handleNetworkInput(new DraftMessage(2, 9, 0)); //6RED

            Die dieTwo = model.getRoundTracker().getDie(0, 2);
            serverView.handleNetworkInput(new ToolCardRequestMessage(2, 9, 0));
            ToolCardMessage toolCardMessage = new ToolCardMessage(2, 9, 0);
            toolCardMessage.addRoundTrackerPosition(new Coordinate(0, 2));
            serverView.handleNetworkInput(toolCardMessage);

            Assert.assertTrue(model.getPlayerByID(2).hasUsedCard());
            Assert.assertEquals(dieTwo, model.getPlayerByID(2).getDieInHand());
        } catch (NoDieException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToolCardRunningPliers() {
        try {
            ToolCard[] toolCards = new ToolCard[3];
            toolCards[0] = RunningPliers.instance();
            toolCards[1] = CorkBackedStraightedge.instance();
            toolCards[2] = EglomiseBrush.instance();
            model.setToolCards(toolCards);
            Die die = model.getDraftPool().getDie(0);
            Assert.assertEquals(die, new Die(6, Color.RED));
            serverView.handleNetworkInput(new DraftMessage(1, 1, 0)); //6RED
            serverView.handleNetworkInput(new PlaceMessage(1, 1, new Coordinate(0, 2)));
            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 1, 0));
            ToolCardMessage toolCardMessage = new ToolCardMessage(1, 1, 0);
            serverView.handleNetworkInput(toolCardMessage);
            Assert.assertFalse(model.getPlayerByID(1).hasDraftedDie());
            Die dieTwo = model.getDraftPool().getDie(1); //5Y
            serverView.handleNetworkInput(new DraftMessage(1, 1, 1)); //5Y
            Assert.assertTrue(model.getPlayerByID(1).hasDraftedDie());
            Assert.assertTrue(model.getPlayerByID(1).hasDieInHand());

        } catch (NoDieException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToolCardTapWheel() {
        try {
            ToolCard[] toolCards = new ToolCard[3];
            toolCards[0] = TapWheel.instance();
            toolCards[1] = CorkBackedStraightedge.instance();
            toolCards[2] = EglomiseBrush.instance();
            model.setToolCards(toolCards);
            Die die = model.getDraftPool().getDie(0);
            Assert.assertEquals(die, new Die(6, Color.RED));
            serverView.handleNetworkInput(new DraftMessage(1, 1, 0)); //6RED
            serverView.handleNetworkInput(new PlaceMessage(1, 1, new Coordinate(0, 2)));
            serverView.handleNetworkInput(new PassMessage(1, 1, false));
            serverView.handleNetworkInput(new PassMessage(2, 2, false));
            serverView.handleNetworkInput(new PassMessage(3, 3, false));
            serverView.handleNetworkInput(new PassMessage(4, 4, false));
            serverView.handleNetworkInput(new PassMessage(4, 5, false));
            serverView.handleNetworkInput(new PassMessage(3, 6, false));
            serverView.handleNetworkInput(new PassMessage(2, 7, false));

            Die dieTwo = model.getDraftPool().getDie(1);
            Assert.assertEquals(dieTwo, new Die(5, Color.YELLOW));
            serverView.handleNetworkInput(new DraftMessage(1, 8, 1)); //5Y
            serverView.handleNetworkInput(new PlaceMessage(1, 8, new Coordinate(1, 2)));

            serverView.handleNetworkInput(new PassMessage(1, 8, false));
            serverView.handleNetworkInput(new PassMessage(2, 9, false));
            serverView.handleNetworkInput(new PassMessage(3, 10, false));
            serverView.handleNetworkInput(new PassMessage(4, 11, false));

            createDraftPool();
            Die dieThree = model.getDraftPool().getDie(8);
            Assert.assertEquals(dieThree, new Die(1, Color.RED));
            serverView.handleNetworkInput(new DraftMessage(1, 12, 8)); //1R
            serverView.handleNetworkInput(new PlaceMessage(1, 12, new Coordinate(1, 1)));
            Assert.assertEquals(dieThree, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(1, 1)).getDie());

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 12, 0));
            ToolCardMessage toolCardMessage2 = new ToolCardMessage(1, 12, 0);
            toolCardMessage2.addStartingPosition(new Coordinate(0, 2));
            toolCardMessage2.addFinalPosition(new Coordinate(0, 1));
            toolCardMessage2.addStartingPosition(new Coordinate(1, 2));
            toolCardMessage2.addFinalPosition(new Coordinate(1, 2));
            toolCardMessage2.addRoundTrackerPosition(new Coordinate(0, 6));
            toolCardMessage2.setCondition(true);
            serverView.handleNetworkInput(toolCardMessage2);
            Assert.assertEquals(die, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(0, 2)).getDie());
            Assert.assertEquals(dieTwo, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(1, 2)).getDie());
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(0, 1)).isEmpty());
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(0, 1)).isEmpty());


            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 12, 0));
            ToolCardMessage toolCardMessage3 = new ToolCardMessage(1, 12, 0);
            toolCardMessage3.addStartingPosition(new Coordinate(0, 2));
            toolCardMessage3.addFinalPosition(new Coordinate(1, 3));
            toolCardMessage3.addStartingPosition(new Coordinate(1, 1));
            toolCardMessage3.addFinalPosition(new Coordinate(2, 2));
            toolCardMessage3.addRoundTrackerPosition(new Coordinate(0, 6));
            toolCardMessage3.setCondition(true);
            serverView.handleNetworkInput(toolCardMessage3);
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(0, 2)).isEmpty());
            Assert.assertTrue(model.getPlayerByID(1).getWindow().getSquare(new Coordinate(1, 1)).isEmpty());
            Assert.assertEquals(die, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(1, 3)).getDie());
            Assert.assertEquals(dieThree, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(2, 2)).getDie());
            serverView.handleNetworkInput(new PassMessage(1, 12, false));

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 13, 0));
            ToolCardMessage toolCardMessage4 = new ToolCardMessage(1, 13, 0);
            toolCardMessage4.addStartingPosition(new Coordinate(1, 3));
            toolCardMessage4.addFinalPosition(new Coordinate(3, 1));
            toolCardMessage4.addStartingPosition(new Coordinate(1, 2));
            toolCardMessage4.addFinalPosition(new Coordinate(3, 2));
            toolCardMessage4.addRoundTrackerPosition(new Coordinate(0, 6));
            toolCardMessage4.setCondition(true);
            serverView.handleNetworkInput(toolCardMessage4);
            Assert.assertEquals(die, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(1, 3)).getDie());
            Assert.assertEquals(dieTwo, model.getPlayerByID(1).getWindow().getSquare(new Coordinate(1, 2)).getDie());

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 13, 0));
            ToolCardMessage toolCardMessage5 = new ToolCardMessage(1, 13, 0);
            toolCardMessage5.addFinalPosition(new Coordinate(2, 3));
            toolCardMessage5.addStartingPosition(new Coordinate(1, 3));
            toolCardMessage5.addFinalPosition(new Coordinate(1, 3));
            toolCardMessage5.addStartingPosition(new Coordinate(2, 2));
            toolCardMessage5.addRoundTrackerPosition(new Coordinate(0, 6));
            toolCardMessage5.setCondition(true);
            serverView.handleNetworkInput(toolCardMessage5);

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 13, 0));
            ToolCardMessage toolCardMessage6 = new ToolCardMessage(1, 13, 0);
            toolCardMessage6.addFinalPosition(new Coordinate(0, 2));
            toolCardMessage6.addStartingPosition(new Coordinate(1, 3));
            toolCardMessage6.addStartingPosition(new Coordinate(2, 2));
            toolCardMessage6.addFinalPosition(new Coordinate(3, 3));
            toolCardMessage6.addRoundTrackerPosition(new Coordinate(0, 6));
            toolCardMessage6.setCondition(true);
            serverView.handleNetworkInput(toolCardMessage6);

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 13, 0));
            ToolCardMessage toolCardMessage7 = new ToolCardMessage(1, 13, 0);
            toolCardMessage7.addFinalPosition(new Coordinate(0, 2));
            toolCardMessage7.addStartingPosition(new Coordinate(1, 3));
            toolCardMessage7.addRoundTrackerPosition(new Coordinate(0, 1));
            toolCardMessage7.setCondition(false);
            serverView.handleNetworkInput(toolCardMessage7);

            serverView.handleNetworkInput(new ToolCardRequestMessage(1, 13, 0));
            ToolCardMessage toolCardMessage8 = new ToolCardMessage(1, 13, 0);
            toolCardMessage8.addFinalPosition(new Coordinate(0, 2));
            toolCardMessage8.addStartingPosition(new Coordinate(1, 3));
            toolCardMessage8.addRoundTrackerPosition(new Coordinate(0, 6));
            toolCardMessage8.setCondition(false);
            serverView.handleNetworkInput(toolCardMessage8);


        } catch (NoDieException e) {
            e.printStackTrace();
        }
    }

}