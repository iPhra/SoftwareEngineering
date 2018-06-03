package it.polimi.se2018.model.objectives.privateobjectives;

import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.Database;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.ShadesOfGreenObjective;
import it.polimi.se2018.utils.WindowBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestShadesOfGreenObjective {
    private Square[][] matrix;
    private Database database;
    private ShadesOfGreenObjective shadesOfGreenObjective;
    private Player player;

    @Before
    public void init(){
        database = new Database();
        database.standardWhiteMatrix();
        matrix = database.getMatrix();
        shadesOfGreenObjective=ShadesOfGreenObjective.instance("title", "imagePath");
        shadesOfGreenObjective=ShadesOfGreenObjective.instance("title", "imagePath");
        Window window = new Window("BasicMap",0,matrix, WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window,shadesOfGreenObjective);
    }

    @Test
    public void testEvalPoints(){
        assertEquals(0,shadesOfGreenObjective.evalPoints(player));
        database.sixSameColoredDice(Color.GREEN);
        matrix = database.getMatrix();
        Window window = new Window("sixSameColoredDiceMap",0,matrix,WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window,shadesOfGreenObjective);
        assertEquals(21,shadesOfGreenObjective.evalPoints(player));
    }
}
