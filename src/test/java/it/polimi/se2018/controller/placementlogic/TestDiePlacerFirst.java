package it.polimi.se2018.controller.placementlogic;

import it.polimi.se2018.Database;
import it.polimi.se2018.mvc.controller.placementlogic.DiePlacerFirst;
import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.utils.exceptions.InvalidPlacementException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class TestDiePlacerFirst {

    @Test
    public void testCheckCondition() {
        Window window = new Database().getDefaultMaps().get(0).getKey();
        Die die = new Die(1, Color.YELLOW);
        DiePlacerFirst placer = new DiePlacerFirst(die,new Coordinate(1,3), window);
        Assert.assertTrue(!placer.checkCondition());

        placer = new DiePlacerFirst(die,new Coordinate(0,0), window);
        Assert.assertTrue(placer.checkCondition());

        placer = new DiePlacerFirst(new Die(3,Color.BLUE),new Coordinate(0,0), window);
        Assert.assertTrue(!placer.checkCondition());

        placer = new DiePlacerFirst(new Die(4,Color.BLUE),new Coordinate(1,4), window);
        Assert.assertTrue(placer.checkCondition());

        placer = new DiePlacerFirst(die,new Coordinate(0,4), window);
        Assert.assertTrue(placer.checkCondition());

        placer = new DiePlacerFirst(new Die(3,Color.RED),new Coordinate(0,4), window);
        Assert.assertTrue(!placer.checkCondition());

        placer = new DiePlacerFirst(die,new Coordinate(3,0), window);
        Assert.assertTrue(!placer.checkCondition());

        placer = new DiePlacerFirst(new Die(2, Color.GREEN),new Coordinate(3,0), window);
        Assert.assertTrue(placer.checkCondition());

        placer = new DiePlacerFirst(new Die(4, Color.GREEN),new Coordinate(3,0), window);
        Assert.assertTrue(!placer.checkCondition());

        placer = new DiePlacerFirst(die,new Coordinate(3,4), window);
        Assert.assertTrue(placer.checkCondition());

        placer = new DiePlacerFirst(new Die(4,Color.GREEN),new Coordinate(3,4), window);
        Assert.assertTrue(!placer.checkCondition());

        window.getSquare(new Coordinate(0,0)).setDie(new Die(4,Color.YELLOW));
        placer = new DiePlacerFirst(die,new Coordinate(0,0), window);
        Assert.assertTrue(!placer.checkCondition());

        placer = new DiePlacerFirst(die,new Coordinate(0,2), window);
        try {
            placer.placeDie();
            Assert.assertEquals(die, window.getSquare(new Coordinate(0,2)).getDie());
        }
        catch (InvalidPlacementException e) {
            fail();
        }

        placer = new DiePlacerFirst(die,new Coordinate(0,1), window);
        try {
            placer.placeDie();
            fail();
        }
        catch (InvalidPlacementException e) {
        }

    }
}
