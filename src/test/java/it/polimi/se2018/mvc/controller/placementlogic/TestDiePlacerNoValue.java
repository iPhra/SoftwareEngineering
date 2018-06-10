package it.polimi.se2018.mvc.controller.placementlogic;

import it.polimi.se2018.Database;
import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.utils.exceptions.InvalidPlacementException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class TestDiePlacerNoValue {

    @Test
    public void testCheckCondition() {
        Window window = new Database().generateWindowByTitle("KALEIDOSCOPIC DREAM");
        Die die = new Die(2, Color.BLUE);

        try {
            DiePlacerNoValue placer = new DiePlacerNoValue(die, new Coordinate(2, 0), window);
            Assert.assertFalse(placer.checkCondition());

            placer = new DiePlacerNoValue(die, new Coordinate(1, 2), window);
            Assert.assertFalse(placer.checkCondition());

            DiePlacerFirst placer0 = new DiePlacerFirst(new Die(2, Color.YELLOW), new Coordinate(0, 0), window);
            placer0.placeDie();

            placer = new DiePlacerNoValue(die,new Coordinate(0,0),window);
            Assert.assertFalse(placer.checkCondition());

            placer = new DiePlacerNoValue(die,new Coordinate(0,1),window);
            Assert.assertTrue(placer.checkCondition());
            placer.placeDie();

            placer = new DiePlacerNoValue(die,new Coordinate(1,2),window);
            Assert.assertTrue(placer.checkCondition());

            placer = new DiePlacerNoValue(die,new Coordinate(1,1),window);
            Assert.assertFalse(placer.checkCondition());

        }
        catch(InvalidPlacementException e) {fail();}


    }
}
