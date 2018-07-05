package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.network.messages.responses.sync.SyncResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static junit.framework.TestCase.fail;

public class TestEndGameResponse {
    private EndGameResponse response;
    private int playerID;

    @Before
    public void init() {
        playerID = new Random().nextInt();
        response = new EndGameResponse(playerID);
    }

    @Test
    public void testGettersAndSetters() {
        Assert.assertEquals(playerID, response.getPlayerID());

        response.setPlayerPlaying(false);
        Assert.assertFalse(response.isPlayerPlaying());

        response.setScoreBoardResponse(new ArrayList<>(),true);
        Assert.assertTrue(response.getScoreBoardResponse().isLastPlayer());
        Assert.assertEquals(new ArrayList<>(),response.getScoreBoardResponse().getSortedPlayersScores());
        Assert.assertEquals(new ArrayList<>(),response.getScoreBoardResponse().getSortedPlayersNames());
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
                fail();
            }

            @Override
            public void handleResponse(EndGameResponse endGameResponse) {
            }
        });
    }
}
