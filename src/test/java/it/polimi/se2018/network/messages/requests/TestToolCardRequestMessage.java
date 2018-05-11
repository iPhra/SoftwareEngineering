package it.polimi.se2018.network.messages.requests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;

public class TestToolCardRequestMessage {

    ToolCardRequestMessage toolCardRequestMessage;

    @Before
    public void init() {
        toolCardRequestMessage = new ToolCardRequestMessage(null,6);
    }

    @Test
    public void testGetToolCardNumber() {
        Assert.assertEquals(6,toolCardRequestMessage.getToolCardNumber());
    }

    @Test
    public void testHandle() {
        toolCardRequestMessage.handle(new MessageHandler() {
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
                fail();
            }

            @Override
            public void performMove(ToolCardRequestMessage toolCardRequestMessage) {
            }
        });
    }
}
