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
        draftMessage = new DraftMessage(null,position);
    }

    @Test
    public void testGetDraftPoolPosition() {
        Assert.assertEquals(position, draftMessage.getDraftPoolPosition());
    }

    @Test
    public void testSetDraftPoolPosition() {
        int newpos = new Random().nextInt();
        draftMessage.setDraftPoolPosition(newpos);
        Assert.assertEquals(newpos, draftMessage.getDraftPoolPosition());
    }

    @Test
    public void testHandle() {
        draftMessage.handle(new MessageHandler() {
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
                fail();

            }

            @Override
            public void performMove(DraftMessage draftMessage) {

            }

            @Override
            public void performMove(ToolCardRequestMessage toolCardRequestMessage) {
                fail();

            }
        });
    }
}
