package it.polimi.se2018.model;

import it.polimi.se2018.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.model.objectives.privateobjectives.ShadesOfGreenObjective;
import it.polimi.se2018.network.messages.Coordinate;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class TestPlayer {
    private String name;
    private int id;
    private Map map;
    private int mapLevel;
    private PrivateObjective privateObjective;
    private Player player;

    @Before
    public void init() {
        name = "name";
        Random random = new Random();
        id = random.nextInt();
        Square[][] matrix = new Square[1][1];
        matrix[0][0] = new Square(Color.BLUE, 5, new Coordinate(2, 3));
        mapLevel = random.nextInt(3) + 1;
        map = new Map("title", mapLevel, matrix);
        privateObjective = ShadesOfGreenObjective.instance("imagePath", "title");
        player = new Player(name, id, map, privateObjective);
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
        assertEquals(map, player.getMap());
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


}


