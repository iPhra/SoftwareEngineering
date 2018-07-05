package it.polimi.se2018.network.messages.responses.sync;

import it.polimi.se2018.network.messages.responses.sync.*;
import it.polimi.se2018.network.messages.responses.sync.modelupdates.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class TestToolCardResponse {
    private ToolCardResponse toolCardResponse;

    @Before
    public void init() {
        toolCardResponse= new ToolCardResponse(0,0);
    }

    @Test
    public void testGetToolCardNumber() {
        Assert.assertEquals(0,toolCardResponse.getToolCardNumber());
    }

    @Test
    public void testHandle() {
        toolCardResponse.handle(new SyncResponseHandler() {
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
