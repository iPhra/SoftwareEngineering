package it.polimi.se2018.model.objectives.publicobjectives;

import it.polimi.se2018.Database;
import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.ShadesOfGreenObjective;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.ColorDiagonalsObjective;
import it.polimi.se2018.utils.WindowBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestColorDiagonalsObjective {
    private Square[][] matrix;
    private Database database;
    private ColorDiagonalsObjective colorDiagonalsObjective;
    private ShadesOfGreenObjective shadesOfGreenObjective;
    private Player player;

    @Before
    public void init() {
        database = new Database();
        database.standardWhiteMatrix();
        matrix = database.getMatrix();
        colorDiagonalsObjective = ColorDiagonalsObjective.instance("Color Diagonals", "imagePath");
        colorDiagonalsObjective = ColorDiagonalsObjective.instance("Color Diagonals", "imagePath");
        Window window = new Window("BasicMap",0, matrix, WindowBuilder.getLevelPaths().get(0));
        shadesOfGreenObjective= ShadesOfGreenObjective.instance("title", "imagePath");
        player = new Player("name",1, window,shadesOfGreenObjective);
    }

    @Test
    public void testEvalPoints() {
        assertEquals(0, colorDiagonalsObjective.evalPoints(player));
        database.sixSameColoredDice(Color.GREEN);
        matrix = database.getMatrix();
        Window window1 = new Window("sixSameColoredDiceMap",0,matrix,WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window1,shadesOfGreenObjective);
        assertEquals(5, colorDiagonalsObjective.evalPoints(player));
        database.initMatrixFullOfDice();
        matrix = database.getMatrix();
        Window window2 = new Window("FullOfDiceMap",0, matrix,WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window2,shadesOfGreenObjective);
        assertEquals(12, colorDiagonalsObjective.evalPoints(player));
    }

    @Test
    public void testToString() {
        StringBuilder spaces = new StringBuilder();
        for(int i=0; i<5; i++) {
            spaces.append(" ");
        }
        spaces.append("      ");
         Assert.assertEquals("Title: \"" + "Color Diagonals" +"\"" +  spaces.toString() + "Effect: \"" + "Number of diagonally adjacent dice with the same color" + "\"" + "\n",colorDiagonalsObjective.toString());
    }
}
