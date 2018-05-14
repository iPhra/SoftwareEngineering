package it.polimi.se2018.network.messages.requests;

import it.polimi.se2018.network.messages.Coordinate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.fail;

public class TestPlaceMessage {
    PlaceMessage placeMessage;
    Coordinate coordinate;
    Random random;

    @Before
    public void init() {
        random = new Random();
        coordinate = new Coordinate(random.nextInt(),random.nextInt());
        placeMessage = new PlaceMessage(0, coordinate);
    }

    @Test
    public void testGetFinalPosition() {
        Assert.assertEquals(coordinate,placeMessage.getFinalPosition());
        Assert.assertNotEquals(new Coordinate(coordinate.getRow()+1,coordinate.getCol()-1),placeMessage.getFinalPosition());
    }

    @Test
    public void testSetFinalPosition() {
        Coordinate coordinate = new Coordinate(random.nextInt(),random.nextInt());
        placeMessage.setFinalPosition(coordinate);
        Assert.assertEquals(coordinate,placeMessage.getFinalPosition());
    }

    @Test
    public void testHandle() {
        placeMessage.handle(new MessageHandler() {
            @Override
            public void performMove(ToolCardMessage toolCardMessage) {
                fail();
            }

            @Override
            public void performMove(PassMessage passMessage) {
                fail();
            }

            @Override
            public void performMove(PlaceMessage placeMessage) {
            }

            @Override
            public void performMove(DraftMessage draftMessage) {
                fail();
            }

            @Override
            public void performMove(ToolCardRequestMessage toolCardRequestMessage) {
                fail();
            }
        });
    }
    
}
