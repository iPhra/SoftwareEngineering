package it.polimi.se2018.network.messages.responses.sync;

import it.polimi.se2018.network.messages.responses.sync.modelupdates.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.fail;

public class TestReconnectionResponse {
    private ReconnectionResponse response;
    private int playerID;

    @Before
    public void init() {
        playerID = new Random().nextInt();
        response = new ReconnectionResponse(playerID,true);
    }

    @Test
    public void testGetters() {
        Assert.assertEquals(playerID, response.getPlayerID());
        Assert.assertTrue(response.isWindowSelectionOver());

        response.setPlayersNumber(4);
        Assert.assertEquals(4,response.getPlayersNumber());

        response.setModelViewResponse(null);
        Assert.assertNull(response.getModelViewResponse());
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
            public void handleResponse(InputResponse inputMessage) {fail();}

            @Override
            public void handleResponse(ScoreBoardResponse scoreBoardResponse) {
                fail();
            }

            @Override
            public void handleResponse(ReconnectionResponse reconnectionResponse) {
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
