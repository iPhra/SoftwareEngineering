package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.network.messages.responses.sync.*;
import it.polimi.se2018.network.messages.responses.sync.modelupdates.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class TestTextResponse {
    private TextResponse textResponse;
    private String message;

    @Before
    public void init() {
        message="test";
        textResponse = new TextResponse(0);
        textResponse.setDescription(message);
    }

    @Test
    public void testGetMessage() {
        Assert.assertEquals(message,textResponse.getDescription());
        Assert.assertNotEquals("testo",textResponse.getDescription());
    }

    @Test
    public void testHandle() {
        textResponse.handle(new SyncResponseHandler() {
            @Override
            public void handleResponse(ModelViewResponse modelViewResponse) {
                fail();
            }

            @Override
            public void handleResponse(TextResponse textResponse) {
            }

            @Override
            public void handleResponse(ToolCardResponse toolCardResponse) {
                fail();
            }

            @Override
            public void handleResponse(SetupResponse setupResponse) {fail();}

            @Override
            public void handleResponse(InputResponse inputMessage) {fail();}

            @Override
            public void handleResponse(ScoreBoardResponse scoreBoardResponse) {fail();}

            @Override
            public void handleResponse(ReconnectionResponse reconnectionResponse) {
                fail();
            }

            @Override
            public void handleResponse(DraftPoolResponse draftPoolResponse) {
                fail();
            }

            @Override
            public void handleResponse(RoundTrackerResponse roundTrackerResponse) {
                fail();
            }

            @Override
            public void handleResponse(WindowResponse windowResponse) {
                fail();
            }

            @Override
            public void handleResponse(ModelUpdateResponse modelUpdateResponse) {
                fail();
            }
        });
    }
}
