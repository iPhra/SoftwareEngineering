package it.polimi.se2018.network.messages.requests;

import org.junit.Test;

import static junit.framework.TestCase.fail;

public class TestPassMessage {

    @Test
    public void testHandle() {
        PassMessage passMessage = new PassMessage(0);
        passMessage.handle(new MessageHandler() {
            @Override
            public void performMove(ToolCardMessage toolCardMessage) {
                fail();
            }

            @Override
            public void performMove(PassMessage passMessage) {
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
