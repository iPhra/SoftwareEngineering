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
        toolCardMessage = new ToolCardMessage(null,random.nextInt(),toolCardNumber);
        toolCardMessage.addDraftPoolPosition(7);
        toolCardMessage.addFinalPosition(new Coordinate(5,-235));
        toolCardMessage.addFinalPosition(new Coordinate(12,-32));
        toolCardMessage.addRoundTrackerPosition(new Coordinate(3,1245));
        toolCardMessage.addStartingPosition(new Coordinate(-23532,3244444));
        toolCardMessage.addStartingPosition(new Coordinate(-22,0));
        toolCardMessage.addValue(325);
    }

    @Test
    public void testGetters() {
        Assert.assertEquals(7,toolCardMessage.getDraftPoolPosition());
        Assert.assertEquals(toolCardNumber,toolCardMessage.getToolCardNumber());
        Assert.assertEquals(325,toolCardMessage.getValue());
        Assert.assertEquals(new Coordinate(5,-235),toolCardMessage.getFinalPosition().get(0));
        Assert.assertEquals(new Coordinate(12,-32),toolCardMessage.getFinalPosition().get(1));
        Assert.assertEquals(new Coordinate(3,1245),toolCardMessage.getRoundTrackerPosition());
        Assert.assertEquals(new Coordinate(-23532,3244444),toolCardMessage.getStartingPosition().get(0));
        Assert.assertEquals(new Coordinate(-22,0),toolCardMessage.getStartingPosition().get(1));
    }

    @Test
    public void testHandle() {
        toolCardMessage.handle(new MessageHandler() {
            @Override
            public void performMove(ToolCardMessage toolCardMessage) {
            }

            @Override
            public void performMove(PassMessage passMessage) {
                fail();
            }

            @Override
            public void performMove(PlaceMessage placeMessage) {
                fail();

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
