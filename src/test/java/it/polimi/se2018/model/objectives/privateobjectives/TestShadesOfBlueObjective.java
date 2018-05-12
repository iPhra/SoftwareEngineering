package it.polimi.se2018.model.objectives.privateobjectives;

import it.polimi.se2018.model.Color;
import it.polimi.se2018.model.Map;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;
import it.polimi.se2018.Database;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestShadesOfBlueObjective {
    private Square[][] matrix;
    private Database database;
    private ShadesOfBlueObjective shadesOfBlueObjective;
    private Player player;

    @Before
    public void init(){
        database = new Database();
        database.initBasicMatrix();
        matrix = database.getMatrix();
        shadesOfBlueObjective=ShadesOfBlueObjective.instance("imagePath","title");
        Map map = new Map("BasicMap",0,"imagePath",matrix);
        player = new Player("name",1,map,shadesOfBlueObjective);
    }

    @Test
    public void testEvalPoints(){
        assertEquals(0,shadesOfBlueObjective.evalPoints(player));
        database.sixSameColoredDice(Color.BLUE);
        matrix = database.getMatrix();
        Map map = new Map("sixSameColoredDiceMap",0,"imagePath",matrix);
        player = new Player("name",1,map,shadesOfBlueObjective);
        assertEquals(21,shadesOfBlueObjective.evalPoints(player));
    }
}
