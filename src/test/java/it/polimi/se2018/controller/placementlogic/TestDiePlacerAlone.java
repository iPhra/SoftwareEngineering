package it.polimi.se2018.controller.placementlogic;

import it.polimi.se2018.Database;
import it.polimi.se2018.model.Color;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Map;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.utils.exceptions.InvalidPlacementException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class TestDiePlacerAlone {

    @Test
    public void testCheckCondition() {
        Map map = new Database().getDefaultMaps().get(0).getKey();
        Die die = new Die(4, Color.RED);
        DiePlacerAlone placer = new DiePlacerAlone(die,new Coordinate(1,4),map);
        Assert.assertTrue(placer.checkCondition());

        placer = new DiePlacerAlone(die,new Coordinate(0,1),map);
        Assert.assertTrue(!placer.checkCondition());

        placer = new DiePlacerAlone(die,new Coordinate(0,4),map);
        Assert.assertTrue(!placer.checkCondition());

        placer = new DiePlacerAlone(die,new Coordinate(2,2),map);
        Assert.assertTrue(placer.checkCondition());

        placer = new DiePlacerAlone(die,new Coordinate(0,3),map);
        map.getSquare(new Coordinate(0,3)).setDie(new Die(3,Color.BLUE));
        Assert.assertTrue(!placer.checkCondition());

        placer = new DiePlacerAlone(die,new Coordinate(0,4),map);
        Assert.assertTrue(!placer.checkCondition());

        placer = new DiePlacerAlone(die,new Coordinate(0,0),map);
        Assert.assertTrue(!placer.checkCondition());

        placer = new DiePlacerAlone(die,new Coordinate(0,2),map);
        try {
            placer.placeDie();
            Assert.assertEquals(die,map.getSquare(new Coordinate(0,2)).getDie());
        }
        catch (InvalidPlacementException e) {
            fail();
        }

        placer = new DiePlacerAlone(die,new Coordinate(0,4),map);
        try {
            placer.placeDie();
            fail();
        }
        catch (InvalidPlacementException e) {
        }
    }
}
