package it.polimi.se2018.network.messages.requests;

import org.junit.Test;

import static junit.framework.TestCase.fail;

public class TestPassMessage {

    @Test
    public void testHandle() {
        PassMessage passMessage = new PassMessage(0,0,false);
        passMessage.handle(new MessageHandler() {
            @Override
            public void handleMove(ToolCardMessage toolCardMessage) {
                fail();
            }

            @Override
            public void handleMove(PassMessage passMessage) {
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
}
