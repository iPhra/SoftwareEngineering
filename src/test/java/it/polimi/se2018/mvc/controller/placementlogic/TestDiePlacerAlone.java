package it.polimi.se2018.mvc.controller.placementlogic;

import it.polimi.se2018.WindowDatabase;
import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.utils.exceptions.InvalidPlacementException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class TestDiePlacerAlone {

    @Test
    public void testCheckCondition() {
        Window window = new WindowDatabase().generateWindowByTitle("KALEIDOSCOPIC DREAM");
        Die die = new Die(4, Color.RED);
        DiePlacerAlone placer = new DiePlacerAlone(die,new Coordinate(1,4), window);
        Assert.assertTrue(placer.checkCondition());

        placer = new DiePlacerAlone(die,new Coordinate(0,1), window);
        Assert.assertTrue(!placer.checkCondition());

        placer = new DiePlacerAlone(die,new Coordinate(0,4), window);
        Assert.assertTrue(!placer.checkCondition());

        placer = new DiePlacerAlone(die,new Coordinate(2,2), window);
        Assert.assertTrue(placer.checkCondition());

        placer = new DiePlacerAlone(die,new Coordinate(0,3), window);
        window.getSquare(new Coordinate(0,3)).setDie(new Die(3,Color.BLUE));
        Assert.assertTrue(!placer.checkCondition());

        placer = new DiePlacerAlone(die,new Coordinate(0,4), window);
        Assert.assertTrue(!placer.checkCondition());

        placer = new DiePlacerAlone(die,new Coordinate(0,0), window);
        Assert.assertTrue(!placer.checkCondition());

        placer = new DiePlacerAlone(die,new Coordinate(0,2), window);
        try {
            placer.placeDie();
            Assert.assertEquals(die, window.getSquare(new Coordinate(0,2)).getDie());
        }
        catch (InvalidPlacementException e) {
            fail();
        }

        placer = new DiePlacerAlone(die,new Coordinate(0,4), window);
        try {
            placer.placeDie();
            fail();
        }
        catch (InvalidPlacementException ignored) {
        }
    }
}
