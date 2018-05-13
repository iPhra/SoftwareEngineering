package it.polimi.se2018.model;

import it.polimi.se2018.Database;
import it.polimi.se2018.model.objectives.privateobjectives.ShadesOfGreenObjective;
import it.polimi.se2018.model.objectives.publicobjectives.DeepShadesObjective;
import it.polimi.se2018.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.model.objectives.publicobjectives.RowColorVarietyObjective;
import it.polimi.se2018.model.toolcards.*;
import org.junit.Before;
import org.junit.Test;

import javax.tools.Tool;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class TestBoard {
    private List<Player> players;
    private ToolCard[] toolCards;
    private PublicObjective[] publicObjectives;
    private Board board;
    private Player player1;
    private Player player2;

    @Before
    public void init(){
        Database database = new Database();
        database.standardWhiteMatrix();
        Square[][] matrix = database.getMatrix();
        ShadesOfGreenObjective shadesOfGreenObjective= ShadesOfGreenObjective.instance("imagePath","title");
        Map map = new Map("BasicMap",0,matrix);
        player1 = new Player("player1",1,map,shadesOfGreenObjective);
        player2 = new Player("player2",2,map,shadesOfGreenObjective);
        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        toolCards = new ToolCard[3];
        toolCards[0] = new EglomiseBrush("imagePath1","title1",false);
        toolCards[1] = new FluxBrush("imagePath2","title2",false);
        toolCards[2] = new TapWheel("imagePath3","title3",false);
        publicObjectives = new PublicObjective[2];
        publicObjectives[0] = DeepShadesObjective.instance("imagePath1","title1");
        publicObjectives[1] = RowColorVarietyObjective.instance("imagePath2","title2");
        board = new Board(1, players,"boardImagePath", toolCards, publicObjectives);

    }

    @Test
    public void testGetDraftPool(){
        Bag bag = new Bag(5, 90);
        DraftPool draftPool=new DraftPool();
        draftPool.fillDraftPool(bag.drawDice(players.size()));
        assertEquals(draftPool, board.getDraftPool());
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

    @Test
    public void testGetBag(){
        Bag bag = new Bag(5, 90);
        assertEquals(bag, board.getBag());
    }

    @Test
    public void testGetRoundTracker(){
        RoundTracker roundTracker = new RoundTracker(10);
        assertEquals(roundTracker, board.getRoundTracker());
    }

    @Test
    public void testGetPlayers(){
        assertEquals(players, board.getPlayers());
    }

    @Test
    public void testGetImagePath(){
        assertEquals("boardImagePath", board.getImagePath());
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
    public void testGetId(){
        assertEquals(1, board.getId());
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
        assertEquals(player2, board.getPlayerByIndex(player2.getId()));

    }

    @Test
    public void testGetPlayerByIndexException(){
        try{
            Player playerGot = board.getPlayerByIndex(200);
        }
        catch(InvalidParameterException e){
            return;
        }
        fail();
    }

    @Test
    public void testModelViewCopy(){
        ModelView modelView = new ModelView(board);
        assertEquals(modelView, board.modelViewCopy());
    }

    @Test
    public void testSetToolCard(){
        ToolCard toolCard = new GrozingPliers("imagePath","title",false);
        board.setToolCard(toolCard,1);
        assertEquals(toolCard,board.getToolCards()[1]);
    }
}
