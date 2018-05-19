package it.polimi.se2018.network.messages.responses;

import org.junit.Test;

import static org.junit.Assert.fail;

public class TestTurnStartResponse {

    @Test
    public void testHandle() {
        TurnStartResponse message = new TurnStartResponse(0);
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

            @Override
            public void handleResponse(SetupResponse setupResponse) {fail();}

            @Override
            public void handleResponse(InputResponse inputMessage) {fail();}
        });
    }
}
