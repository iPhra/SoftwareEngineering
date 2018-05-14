package it.polimi.se2018.network.messages.responses;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;

public class TestToolCardResponse {
    List<String> playerRequests;
    ToolCardResponse toolCardResponse;

    @Before
    public void init() {
        playerRequests= Arrays.asList("pradella","is","my","king");
        toolCardResponse= new ToolCardResponse(0);
    }

    @Test
    public void testHandle() {
        toolCardResponse.handle(new ResponseHandler() {
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
                fail();

            }

            @Override
            public void handleResponse(ToolCardResponse toolCardResponse) {
            }
        });
    }
}
