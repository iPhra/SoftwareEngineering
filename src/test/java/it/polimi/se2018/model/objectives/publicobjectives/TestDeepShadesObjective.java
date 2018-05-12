package it.polimi.se2018.model.objectives.publicobjectives;

import it.polimi.se2018.Database;
import it.polimi.se2018.model.Color;
import it.polimi.se2018.model.Map;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;
import it.polimi.se2018.model.objectives.privateobjectives.ShadesOfGreenObjective;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestDeepShadesObjective {
    private Square[][] matrix;
    private Database database;
    private DeepShadesObjective deepShadesObjective;
    ShadesOfGreenObjective shadesOfGreenObjective;
    private Player player;

    @Before
    public void init(){
        database = new Database();
        database.standardWhiteMatrix();
        matrix = database.getMatrix();
        deepShadesObjective = DeepShadesObjective.instance("imagePath","title");
        Map map = new Map("BasicMap",0, matrix);
        shadesOfGreenObjective= ShadesOfGreenObjective.instance("imagePath","title");
        player = new Player("name",1,map,shadesOfGreenObjective);
    }

    @Test
    public void testEvalPoints(){
        assertEquals(0, deepShadesObjective.evalPoints(player));
        database.sixSameColoredDice(Color.GREEN);
        matrix = database.getMatrix();
        Map map1 = new Map("sixSameColoredDiceMap",0, matrix);
        player = new Player("name",1,map1,shadesOfGreenObjective);
        assertEquals(4, deepShadesObjective.evalPoints(player));
        database.initMatrixFullOfDice();
        matrix = database.getMatrix();
        Map map2 = new Map("FullOfDiceMap",0, matrix);
        player = new Player("name",1,map2,shadesOfGreenObjective);
        assertEquals(12, deepShadesObjective.evalPoints(player));
    }
}
