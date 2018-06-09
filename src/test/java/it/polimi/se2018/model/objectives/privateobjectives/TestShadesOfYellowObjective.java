package it.polimi.se2018.model.objectives.privateobjectives;

import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.Database;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.ShadesOfYellowObjective;
import it.polimi.se2018.utils.WindowBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestShadesOfYellowObjective {
    private Square[][] matrix;
    private Database database;
    private ShadesOfYellowObjective shadesOfYellowObjective;
    private Player player;

    @Before
    public void init() {
        database = new Database();
        database.standardWhiteMatrix();
        matrix = database.getMatrix();
        shadesOfYellowObjective =ShadesOfYellowObjective.instance("imagePath");
        shadesOfYellowObjective =ShadesOfYellowObjective.instance("imagePath");
        Window window = new Window("BasicMap",0,matrix,WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window, shadesOfYellowObjective);
    }

    @Test
    public void testEvalPoints() {
        assertEquals(0, shadesOfYellowObjective.evalPoints(player));
        database.sixSameColoredDice(Color.YELLOW);
        matrix = database.getMatrix();
        Window window = new Window("sixSameColoredDiceMap",0,matrix, WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window, shadesOfYellowObjective);
        assertEquals(21, shadesOfYellowObjective.evalPoints(player));
    }

    @Test
    public void testToString() {
        String result = "Private Objective:" + "\n" + "Title: \"" + "Shades of Yellow\"      Effect: \"" + "Sum of values on yellow dice" + "\"";
        Assert.assertEquals(result,shadesOfYellowObjective.toString());
    }
}
