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

public class TestDiePlacerNoColor {

    @Test
    public void testCheckCondition() {
        Window window = new Database().generateWindowByTitle("KALEIDOSCOPIC DREAM");
        Die die = new Die(1, Color.YELLOW);

        try {
            DiePlacerNoColor placer = new DiePlacerNoColor(die, new Coordinate(0, 1), window);
            Assert.assertFalse(placer.checkCondition());

            placer = new DiePlacerNoColor(die, new Coordinate(1, 2), window);
            Assert.assertFalse(placer.checkCondition());

            DiePlacerFirst placer0 = new DiePlacerFirst(new Die(2, Color.YELLOW), new Coordinate(0, 0), window);
            placer0.placeDie();

            placer = new DiePlacerNoColor(new Die(2,Color.YELLOW), new Coordinate(0, 1), window);
            Assert.assertFalse(placer.checkCondition());

            placer = new DiePlacerNoColor(die, new Coordinate(0, 1), window);
            Assert.assertTrue(placer.checkCondition());
            placer.placeDie();
            Assert.assertFalse(placer.checkCondition());
        }
        catch(InvalidPlacementException e) {fail();}


    }
}
