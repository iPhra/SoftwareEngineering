package it.polimi.se2018.mvc.model.objectives.publicobjectives;

import it.polimi.se2018.WindowDatabase;
import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.ShadesOfGreenObjective;
import it.polimi.se2018.utils.WindowBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMediumShadesObjective {
    private Square[][] matrix;
    private WindowDatabase windowDatabase;
    private MediumShadesObjective mediumShadesObjective;
    private ShadesOfGreenObjective shadesOfGreenObjective;
    private Player player;

    @Before
    public void init(){
        windowDatabase = new WindowDatabase();
        windowDatabase.standardWhiteMatrix();
        matrix = windowDatabase.getMatrix();
        mediumShadesObjective = MediumShadesObjective.instance();
        mediumShadesObjective = MediumShadesObjective.instance();
        Window window = new Window("BasicMap",0, matrix, WindowBuilder.getLevelPaths().get(0));
        shadesOfGreenObjective= ShadesOfGreenObjective.instance();
        player = new Player("name",1, window,shadesOfGreenObjective);
    }

    @Test
    public void testEvalPoints(){
        assertEquals(0, mediumShadesObjective.evalPoints(player));
        windowDatabase.sixSameColoredDice(Color.GREEN);
        matrix = windowDatabase.getMatrix();
        Window window1 = new Window("sixSameColoredDiceMap",0, matrix,WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window1,shadesOfGreenObjective);
        assertEquals(2, mediumShadesObjective.evalPoints(player));
        windowDatabase.initMatrixFullOfDice();
        matrix = windowDatabase.getMatrix();
        Window window2 = new Window("FullOfDiceMap",0, matrix,WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window2,shadesOfGreenObjective);
        assertEquals(6, mediumShadesObjective.evalPoints(player));
    }
}
