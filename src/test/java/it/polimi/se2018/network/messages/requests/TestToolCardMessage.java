package it.polimi.se2018.network.messages.requests;

import it.polimi.se2018.network.messages.Coordinate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.fail;

public class TestToolCardMessage {
    private ToolCardMessage toolCardMessage;
    private int toolCardNumber;

    @Before
    public void init() {
        Random random = new Random();
        toolCardNumber = random.nextInt();
        toolCardMessage = new ToolCardMessage(0,0,toolCardNumber);
        toolCardMessage.addDraftPoolPosition(7);
        toolCardMessage.addFinalPosition(new Coordinate(5,-235));
        toolCardMessage.addFinalPosition(new Coordinate(12,-32));
        toolCardMessage.addRoundTrackerPosition(new Coordinate(3,1245));
        toolCardMessage.addStartingPosition(new Coordinate(-23532,3244444));
        toolCardMessage.addStartingPosition(new Coordinate(-22,0));
        toolCardMessage.setCondition(true);
    }

    @Test
    public void testGetters() {
        Assert.assertEquals(7,toolCardMessage.getDraftPoolPosition());
        Assert.assertNotEquals(-5,toolCardMessage.getDraftPoolPosition());
        Assert.assertEquals(toolCardNumber,toolCardMessage.getToolCardNumber());
        Assert.assertNotEquals(toolCardNumber/2,toolCardMessage.getToolCardNumber());
        Assert.assertEquals(new Coordinate(5,-235),toolCardMessage.getFinalPosition().get(0));
        Assert.assertNotEquals(new Coordinate(5,-233),toolCardMessage.getFinalPosition().get(0));
        Assert.assertEquals(new Coordinate(12,-32),toolCardMessage.getFinalPosition().get(1));
        Assert.assertNotEquals(new Coordinate(11,-32),toolCardMessage.getFinalPosition().get(1));
        Assert.assertEquals(new Coordinate(3,1245),toolCardMessage.getRoundTrackerPosition());
        Assert.assertNotEquals(new Coordinate(3,12433335),toolCardMessage.getRoundTrackerPosition());
        Assert.assertEquals(new Coordinate(-23532,3244444),toolCardMessage.getStartingPosition().get(0));
        Assert.assertNotEquals(new Coordinate(-23532,3),toolCardMessage.getStartingPosition().get(0));
        Assert.assertEquals(new Coordinate(-22,0),toolCardMessage.getStartingPosition().get(1));
        Assert.assertNotEquals(new Coordinate(+22,1),toolCardMessage.getStartingPosition().get(1));
    }

    @Test
    public void testHandle() {
        toolCardMessage.handle(new MessageHandler() {
            @Override
            public void handleMove(ToolCardMessage toolCardMessage) {
            }

            @Override
            public void handleMove(PassMessage passMessage) {
                fail();
            }

            @Override
            public void handleMove(PlaceMessage placeMessage) {
                fail();
            }

            @Override
            public void handleMove(DraftMessage draftMessage) {
                fail();
            }

            @Override
            public void handleMove(ToolCardRequestMessage toolCardRequestMessage) {
                fail();
            }

            @Override
            public void handleMove(SetupMessage setupMessage) {fail();}

            @Override
            public void handleMove(InputMessage inputMessage) {fail();}
        });
    }

    @Test
    public void testIsCondition() {
        Assert.assertTrue(toolCardMessage.isCondition());
        toolCardMessage.setCondition(false);
        Assert.assertFalse(toolCardMessage.isCondition());
    }
}
