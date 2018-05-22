package it.polimi.se2018.network.messages.requests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;

public class TestToolCardRequestMessage {

    ToolCardRequestMessage toolCardRequestMessage;

    @Before
    public void init() {
        toolCardRequestMessage = new ToolCardRequestMessage(0,6);
    }

    @Test
    public void testGetToolCardNumber() {
        Assert.assertEquals(6,toolCardRequestMessage.getToolCardNumber());
    }

    @Test
    public void testHandle() {
        toolCardRequestMessage.handle(new MessageHandler() {
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
                fail();
            }

            @Override
            public void handleMove(ToolCardRequestMessage toolCardRequestMessage) {
            }

            @Override
            public void handleMove(SetupMessage setupMessage) {fail();}

            @Override
            public void handleMove(InputMessage inputMessage) {fail();}
        });
    }
}
