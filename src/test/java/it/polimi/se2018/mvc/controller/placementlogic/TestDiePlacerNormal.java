package it.polimi.se2018.mvc.controller.placementlogic;

import it.polimi.se2018.MapDatabase;
import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.utils.exceptions.InvalidPlacementException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;


public class TestDiePlacerNormal {

    @Test
    public void testCheckCondition(){
        Window window = new MapDatabase().generateWindowByTitle("KALEIDOSCOPIC DREAM");
        Die die = new Die(2, Color.BLUE);
        try{
            //check !isEmpty
            DiePlacerFirst placer0 = new DiePlacerFirst(new Die(4, Color.YELLOW), new Coordinate(0, 2), window);
            placer0.placeDie();
            DiePlacerNormal placer = new DiePlacerNormal(new Die(1, Color.YELLOW), new Coordinate(0,2),window);
            Assert.assertFalse(placer.checkCondition());

            //check !sameColor
            placer = new DiePlacerNormal(die, new Coordinate(2,2),window);
            Assert.assertFalse(placer.checkCondition());

            //check !sameValue
            placer = new DiePlacerNormal(die, new Coordinate(1,2),window);
            Assert.assertFalse(placer.checkCondition());

            //check !isValueOk
            placer = new DiePlacerNormal(new Die(4,Color.GREEN), new Coordinate(0,1),window);
            Assert.assertFalse(placer.checkCondition());

            //check !isColorOk
            placer = new DiePlacerNormal(new Die(3,Color.YELLOW), new Coordinate(0,3),window);
            Assert.assertFalse(placer.checkCondition());

            //check !hasSurroundingDice
            placer = new DiePlacerNormal(die, new Coordinate(3,2),window);
            Assert.assertFalse(placer.checkCondition());

        }catch(InvalidPlacementException e){
            fail();
        }
    }
}
