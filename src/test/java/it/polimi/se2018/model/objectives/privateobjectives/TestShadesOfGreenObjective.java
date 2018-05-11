package it.polimi.se2018.model.objectives.privateobjectives;

import it.polimi.se2018.model.Color;
import it.polimi.se2018.model.Map;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;
import it.polimi.se2018.model.objectives.DataBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestShadesOfGreenObjective {
    private Square[][] matrix;
    private DataBase database;
    private ShadesOfGreenObjective shadesOfGreenObjective;
    private Player player;

    @Before
    public void init(){
        database = new DataBase();
        database.initBasicMatrix();
        matrix = database.getMatrix();
        shadesOfGreenObjective=ShadesOfGreenObjective.instance("imagePath","title");
        Map map = new Map("BasicMap",0,"imagePath",matrix);
        player = new Player("name",1,map,shadesOfGreenObjective);
    }

    @Test
    public void testEvalPoints(){
        assertEquals(0,shadesOfGreenObjective.evalPoints(player));
        database.sixSameColoredDice(Color.GREEN);
        matrix = database.getMatrix();
        Map map = new Map("sixSameColoredDiceMap",0,"imagePath",matrix);
        player = new Player("name",1,map,shadesOfGreenObjective);
        assertEquals(6,shadesOfGreenObjective.evalPoints(player));
    }
}
