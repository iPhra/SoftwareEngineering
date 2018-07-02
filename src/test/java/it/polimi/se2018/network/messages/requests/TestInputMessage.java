package it.polimi.se2018.network.messages.requests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.fail;

public class TestInputMessage {
    private InputMessage inputMessage;
    private int dieValue;

    @Before
    public void init() {
        dieValue = new Random().nextInt(6)+1;
        inputMessage = new InputMessage(0,0,dieValue);
    }

    @Test
    public void testGetDieValue() {
        Assert.assertEquals(dieValue,inputMessage.getDieValue());
    }

    @Test
    public void testHandle() {
        inputMessage.handle(new MessageHandler() {
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
                fail();
            }

            @Override
            public void handleMove(SetupMessage setupMessage) {fail();}

            @Override
            public void handleMove(InputMessage inputMessage) {}
        });
    }
}
