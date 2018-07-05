package it.polimi.se2018.network.messages.responses.sync.modelupdates;

import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.network.messages.responses.sync.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;

public class TestInputResponse {
    private InputResponse response;

    @Before
    public void init() {
        response = new InputResponse(1, Color.RED);
    }

    @Test
    public void testGetters() {
        Assert.assertEquals(1, response.getPlayerID());
        Assert.assertEquals(Color.RED,response.getColor());
    }

    @Test
    public void testHandle() {
        response.handle(new SyncResponseHandler() {
            @Override
            public void handleResponse(ModelViewResponse modelViewResponse) {
                fail();
            }

            @Override
            public void handleResponse(TextResponse textResponse) {
                fail();
            }

            @Override
            public void handleResponse(ToolCardResponse toolCardResponse) {
                fail();
            }

            @Override
            public void handleResponse(SetupResponse setupResponse) {fail();}

            @Override
            public void handleResponse(InputResponse inputMessage) {
            }

            @Override
            public void handleResponse(ScoreBoardResponse scoreBoardResponse) {
                fail();
            }

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
