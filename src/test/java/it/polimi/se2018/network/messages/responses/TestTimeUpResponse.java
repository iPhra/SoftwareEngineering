package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.network.messages.responses.sync.SyncResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.fail;

public class TestTimeUpResponse {
    private TimeUpResponse response;
    private int playerID;

    @Before
    public void init() {
        playerID = new Random().nextInt();
        response = new TimeUpResponse(playerID);
    }

    @Test
    public void testGetters() {
        Assert.assertEquals(playerID, response.getPlayerID());
    }

    @Test
    public void testHandleClass() {
        response.handleClass(new ResponseHandler() {
            @Override
            public void handleResponse(DisconnectionResponse disconnectionResponse) {
                fail();
            }

            @Override
            public void handleResponse(ReconnectionNotificationResponse reconnectionNotificationResponse) {
                fail();
            }

            @Override
            public void handleResponse(TimeUpResponse timeUpResponse) {
            }

            @Override
            public void handleResponse(SyncResponse syncResponse) {
                fail();
            }

            @Override
            public void handleResponse(EndGameResponse endGameResponse) {
                fail();
            }
        });
    }
}
