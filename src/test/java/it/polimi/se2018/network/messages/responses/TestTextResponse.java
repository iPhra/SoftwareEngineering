package it.polimi.se2018.network.messages.responses;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class TestTextResponse {
    TextResponse textResponse;
    String message;

    @Before
    public void init() {
        message="test";
        textResponse = new TextResponse(0,message);
    }

    @Test
    public void testGetMessage() {
        Assert.assertEquals(message,textResponse.getMessage());
        Assert.assertNotEquals("testo",textResponse.getMessage());
    }

    @Test
    public void testHandle() {
        textResponse.handle(new ResponseHandler() {
            @Override
            public void handleResponse(ModelViewResponse modelViewResponse) {
                fail();
            }

            @Override
            public void handleResponse(TextResponse textResponse) {
            }

            @Override
            public void handleResponse(TurnStartResponse turnStartResponse) {
                fail();
            }

            @Override
            public void handleResponse(ToolCardResponse toolCardResponse) {
                fail();
            }
        });
    }
}
