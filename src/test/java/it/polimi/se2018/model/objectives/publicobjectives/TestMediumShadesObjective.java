package it.polimi.se2018.model.objectives.publicobjectives;

import it.polimi.se2018.Database;
import it.polimi.se2018.model.Color;
import it.polimi.se2018.model.Window;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;
import it.polimi.se2018.model.objectives.privateobjectives.ShadesOfGreenObjective;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMediumShadesObjective {
    private Square[][] matrix;
    private Database database;
    private MediumShadesObjective mediumShadesObjective;
    ShadesOfGreenObjective shadesOfGreenObjective;
    private Player player;

    @Before
    public void init(){
        database = new Database();
        database.standardWhiteMatrix();
        matrix = database.getMatrix();
        mediumShadesObjective = MediumShadesObjective.instance("title");
        Window window = new Window("BasicMap",0, matrix);
        shadesOfGreenObjective= ShadesOfGreenObjective.instance("title");
        player = new Player("name",1, window,shadesOfGreenObjective);
    }

    @Test
    public void testEvalPoints(){
        assertEquals(0, mediumShadesObjective.evalPoints(player));
        database.sixSameColoredDice(Color.GREEN);
        matrix = database.getMatrix();
        Window window1 = new Window("sixSameColoredDiceMap",0, matrix);
        player = new Player("name",1, window1,shadesOfGreenObjective);
        assertEquals(2, mediumShadesObjective.evalPoints(player));
        database.initMatrixFullOfDice();
        matrix = database.getMatrix();
        Window window2 = new Window("FullOfDiceMap",0, matrix);
        player = new Player("name",1, window2,shadesOfGreenObjective);
        assertEquals(6, mediumShadesObjective.evalPoints(player));
    }
}
