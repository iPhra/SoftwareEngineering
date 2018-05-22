package it.polimi.se2018.network.messages.requests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.fail;

//Can't test handle method, it will be part of integration testing
public class TestDraftMessage {
    private DraftMessage draftMessage;
    private int position;

    @Before
    public void init() {
        position=new Random().nextInt();
        draftMessage = new DraftMessage(0,position);
    }

    @Test
    public void testGetDraftPoolPosition() {
        Assert.assertNotEquals(position-1, draftMessage.getDraftPoolPosition());
        Assert.assertEquals(position, draftMessage.getDraftPoolPosition());
    }

    @Test
    public void testSetDraftPoolPosition() {
        int newpos = new Random().nextInt();
        draftMessage.setDraftPoolPosition(newpos);
        Assert.assertEquals(newpos, draftMessage.getDraftPoolPosition());
        Assert.assertNotEquals(newpos-1, draftMessage.getDraftPoolPosition());
    }

    @Test
    public void testHandle() {
        draftMessage.handle(new MessageHandler() {
            @Override
            public void handleMove(ToolCardMessage toolCardMessage) {
                fail();
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
}
