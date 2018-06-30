package it.polimi.se2018.mvc.model;

import it.polimi.se2018.MapDatabase;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.ShadesOfGreenObjective;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.DeepShadesObjective;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.RowColorVarietyObjective;
import it.polimi.se2018.mvc.model.toolcards.EglomiseBrush;
import it.polimi.se2018.mvc.model.toolcards.FluxBrush;
import it.polimi.se2018.mvc.model.toolcards.TapWheel;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.utils.WindowBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestBoard {
    private List<Player> players;
    private ToolCard[] toolCards;
    private PublicObjective[] publicObjectives;
    private Board board;
    private Player player2;

    @Before
    public void init(){
        MapDatabase mapDatabase = new MapDatabase();
        mapDatabase.standardWhiteMatrix();
        Square[][] matrix = mapDatabase.getMatrix();
        ShadesOfGreenObjective shadesOfGreenObjective= ShadesOfGreenObjective.instance("imagePath");
        Window window = new Window("BasicMap",0,matrix, WindowBuilder.getLevelPaths().get(0));
        Player player1 = new Player("player1",1, window,shadesOfGreenObjective);
        player2 = new Player("player2",2, window,shadesOfGreenObjective);
        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        toolCards = new ToolCard[3];
        toolCards[0] = EglomiseBrush.instance("imagePath");
        toolCards[1] = FluxBrush.instance("imagePath");
        toolCards[2] = TapWheel.instance("imagePath");
        publicObjectives = new PublicObjective[2];
        publicObjectives[0] = DeepShadesObjective.instance("imagePath");
        publicObjectives[1] = RowColorVarietyObjective.instance("imagePath");
        board = new Board (players,toolCards, publicObjectives);
    }

    /*@Test
    public void testGetDraftPool(){
        Bag bag = new Bag(5, 90);
        DraftPool draftPool=new DraftPool();
        draftPool.fillDraftPool(bag.drawDice(players.size()));
        assertEquals(draftPool, board.getDraftPool());
    }*/

    @Test
    public void testPlayersNumber() {
        Assert.assertEquals(2,board.getPlayersNumber());
    }

    @Test
    public void testStateID() {
        Assert.assertEquals(0,board.getStateID());
        board.incrementStateID();
        Assert.assertEquals(1,board.getStateID());
    }

    @Test
    public void testGetRound(){
        ArrayList<Integer> playersId = new ArrayList<>(players.size());
        for (Player player : players) {
            playersId.add(player.getId());
        }
        Round round = new Round(playersId, 1);
        assertEquals(round, board.getRound());
    }

    /*@Test
    public void testGetBag(){
        Bag bag = new Bag(5, 90);
        Assert.assertTrue(bag.equals(board.getBag()));
    }

    @Test
    public void testGetRoundTracker(){
        RoundTracker roundTracker = new RoundTracker(Board.ROUNDSNUMBER);
        assertEquals(roundTracker, board.getRoundTracker());
    }*/

    @Test
    public void testGetPlayers(){
        assertEquals(players, board.getPlayers());
    }

    @Test
    public void testGetToolCards(){
        ToolCard[] toolCardsGot = board.getToolCards();
        assertEquals(toolCards.length,toolCardsGot.length);
        for(int i=0; i<toolCards.length; i++)
            assertEquals(toolCards[i], toolCardsGot[i]);
    }

    @Test
    public void testGetPublicObjectives(){
        PublicObjective[] publicObjectivesGot = board.getPublicObjectives();
        assertEquals(publicObjectives.length,publicObjectivesGot.length);
        for(int i=0; i<publicObjectives.length; i++)
            assertEquals(publicObjectives[i],publicObjectivesGot[i]);
    }

    @Test
    public void testSetRound(){
        ArrayList<Integer> playersId = new ArrayList<>(players.size());
        for (int i=0;i<12;i++) {
            playersId.add(i);
        }
        Round round = new Round(playersId, 5);
        board.setRound(round);
        assertEquals(round, board.getRound());
    }

    @Test
    public void testGetPlayerByIndex() throws InvalidParameterException {
        assertEquals(player2, board.getPlayerByID(player2.getId()));
    }

    @Test
    public void testGetPlayerByIndexException(){
        try{
            board.getPlayerByID(200);
        }
        catch(InvalidParameterException e){
            return;
        }
        fail();
    }

    @Test
    public void testToolCardUsage() {
        Assert.assertEquals(true,!board.getToolCardsUsage()[0]);
        board.setAlreadyUsed(0);
        Assert.assertEquals(true,board.getToolCardsUsage()[0]);
        Assert.assertEquals(false,board.getToolCardsUsage()[1]);
        Assert.assertEquals(false,board.getToolCardsUsage()[2]);
    }

    @Test
    public void testNotifies() {
        board.createModelUpdateResponse("");
        board.createWindowResponse("",2);
        board.createDraftPoolResponse("");
        board.createRoundTrackerResponse("");
        board.createModelViews("");
    }
}
