package it.polimi.se2018.model.objectives.privateobjectives;

import it.polimi.se2018.model.Color;
import it.polimi.se2018.model.Map;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;
import it.polimi.se2018.Database;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestShadesOfRedObjective {
    private Square[][] matrix;
    private Database database;
    private ShadesOfRedObjective shadesOfRedObjective;
    private Player player;

    @Before
    public void init(){
        database = new Database();
        database.standardWhiteMatrix();
        matrix = database.getMatrix();
        shadesOfRedObjective =ShadesOfRedObjective.instance("imagePath","title");
        Map map = new Map("BasicMap",0,matrix);
        player = new Player("name",1,map, shadesOfRedObjective);
    }

    @Test
    public void testEvalPoints(){
        assertEquals(0, shadesOfRedObjective.evalPoints(player));
        database.sixSameColoredDice(Color.RED);
        matrix = database.getMatrix();
        Map map = new Map("sixSameColoredDiceMap",0,matrix);
        player = new Player("name",1,map, shadesOfRedObjective);
        assertEquals(6, shadesOfRedObjective.evalPoints(player));
    }
}
