package it.polimi.se2018.model.objectives.publicobjectives;

import it.polimi.se2018.Database;
import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.ShadesOfGreenObjective;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.ColumnColorVarietyObjective;
import it.polimi.se2018.utils.WindowBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestColumnColorVarietyObjective {
    private Square[][] matrix;
    private Database database;
    private ColumnColorVarietyObjective columnColorVarietyObjective;
    private ShadesOfGreenObjective shadesOfGreenObjective;
    private Player player;

    @Before
    public void init(){
        database = new Database();
        database.standardWhiteMatrix();
        matrix = database.getMatrix();
        columnColorVarietyObjective = ColumnColorVarietyObjective.instance("imagePath");
        columnColorVarietyObjective = ColumnColorVarietyObjective.instance("imagePath");
        Window window = new Window("BasicMap",0, matrix,WindowBuilder.getLevelPaths().get(0));
        shadesOfGreenObjective= ShadesOfGreenObjective.instance("imagePath");
        player = new Player("name",1, window,shadesOfGreenObjective);
    }

    @Test
    public void testEvalPoints(){
        assertEquals(0, columnColorVarietyObjective.evalPoints(player));
        database.sixSameColoredDice(Color.GREEN);
        matrix = database.getMatrix();
        Window window1 = new Window("sixSameColoredDiceMap",0, matrix,WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window1,shadesOfGreenObjective);
        assertEquals(0, columnColorVarietyObjective.evalPoints(player));
        database.initMatrixFullOfDice();
        matrix = database.getMatrix();
        Window window2 = new Window("FullOfDiceMap",0, matrix, WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window2,shadesOfGreenObjective);
        assertEquals(5, columnColorVarietyObjective.evalPoints(player));
    }
}
