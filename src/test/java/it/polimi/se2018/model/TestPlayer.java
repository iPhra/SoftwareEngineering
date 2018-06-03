package it.polimi.se2018.model;

import it.polimi.se2018.mvc.model.*;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.ShadesOfGreenObjective;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.utils.WindowBuilder;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class TestPlayer {
    private String name;
    private int id;
    private Window window;
    private int mapLevel;
    private PrivateObjective privateObjective;
    private Player player;

    @Before
    public void init() {
        name = "name";
        Random random = new Random();
        id = random.nextInt();
        Square[][] matrix = new Square[1][1];
        matrix[0][0] = new Square(Color.BLUE, 0, new Coordinate(2, 3), WindowBuilder.getColorPaths().get("B"));
        mapLevel = random.nextInt(3) + 1;
        window = new Window("title", mapLevel, matrix,WindowBuilder.getLevelPaths().get(mapLevel));
        privateObjective = ShadesOfGreenObjective.instance("title", "imagePath");
        player = new Player(name, id, window, privateObjective);
    }

    @Test
    public void testGetName() {
        assertEquals(name, player.getName());
    }

    @Test
    public void testGetFavorPoints() {
        assertEquals(mapLevel, player.getFavorPoints());
    }

    @Test
    public void testSetFavorPoints() {
        Random random = new Random();
        int favorPoints = random.nextInt(3) + 1;
        player.setFavorPoints(favorPoints);
        assertEquals(player.getFavorPoints(), favorPoints);
    }

    @Test
    public void testGetId() {
        assertEquals(id, player.getId());
    }

    @Test
    public void testGetMap() {
        assertEquals(window, player.getWindow());
    }

    @Test
    public void testGetPrivateObjective() {
        assertEquals(privateObjective, player.getPrivateObjective());
    }

    @Test
    public void testSetScore() {
        Random random = new Random();
        int score = random.nextInt(20);
        player.setScore(score);
        assertEquals(score, player.getScore());
    }

    @Test
    public void testSetFirstMove() {
        Random random = new Random();
        boolean firstMove = random.nextBoolean();
        player.setFirstMove(firstMove);
        assertEquals(firstMove, player.isFirstMove());
    }

    @Test
    public void testSetDieInHand() {
        Die die = new Die(5, Color.PURPLE);
        player.setDieInHand(die);
        assertEquals(die, player.getDieInHand());
    }

    @Test
    public void testHasDieInHand() {
        player.dropDieInHand();
        assertFalse(player.hasDieInHand());
        Die die = new Die(2, Color.GREEN);
        player.setDieInHand(die);
        assertTrue(player.hasDieInHand());
        player.dropDieInHand();
        assertFalse(player.hasDieInHand());
    }

    @Test
    public void testSetHasDraftedDie() {
        Random random = new Random();
        boolean hasDraftedDie = random.nextBoolean();
        player.setHasDraftedDie(hasDraftedDie);
        assertEquals(hasDraftedDie, player.hasDraftedDie());
    }

    @Test
    public void testSetHasUsedCard() {
        Random random = new Random();
        boolean hasUsedCard = random.nextBoolean();
        player.setHasUsedCard(hasUsedCard);
        assertEquals(hasUsedCard, player.hasUsedCard());
    }

    @Test
    public void testDropCardInUse() {
        player.dropCardInUse();
        try {
            player.getCardInUse();
        }
        catch(ToolCardException e) {
            return;
        }
        fail();
    }

    @Test
    public void testSetCardInUse() {
        player.setCardInUse(1);
        try {
            Assert.assertEquals(1,player.getCardInUse());
        }
        catch(ToolCardException e) {
            fail();
        }
    }
}


