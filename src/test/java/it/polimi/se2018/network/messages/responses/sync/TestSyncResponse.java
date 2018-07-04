package it.polimi.se2018.network.messages.responses.sync;

import it.polimi.se2018.network.messages.responses.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.fail;

public class TestSyncResponse {
    private SyncResponse response;
    private int playerID;

    @Before
    public void init() {
        playerID = new Random().nextInt();
        response = new SyncResponse(playerID) {
            @Override
            public void handle(SyncResponseHandler handler) {
            }
        };
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
                fail();
            }

            @Override
            public void handleResponse(SyncResponse syncResponse) {
            }

            @Override
            public void handleResponse(EndGameResponse endGameResponse) {
                fail();
            }
        });
    }
}
