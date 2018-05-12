package it.polimi.se2018.network.messages.responses;

import org.junit.Test;

import static org.junit.Assert.fail;

public class TestTurnStartResponse {

    @Test
    public void testHandle() {
        TurnStartResponse message = new TurnStartResponse(null);
        message.handle(new ResponseHandler() {
            @Override
            public void handleResponse(ModelViewResponse modelViewResponse) {
                fail();
            }

            @Override
            public void handleResponse(TextResponse textResponse) {
                fail();
            }

            @Override
            public void handleResponse(TurnStartResponse turnStartResponse) {
            }

            @Override
            public void handleResponse(ToolCardResponse toolCardResponse) {
                fail();
            }
        });
    }
}
