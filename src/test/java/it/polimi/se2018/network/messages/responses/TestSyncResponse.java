package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.network.messages.responses.sync.SyncResponse;
import it.polimi.se2018.network.messages.responses.sync.SyncResponseHandler;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class TestSyncResponse {

    @Test
    public void test() {
        int value = new Random().nextInt();
        SyncResponse syncResponse = new SyncResponse(value) {
            @Override
            public void handle(SyncResponseHandler handler) {
            }
        };
        Assert.assertEquals(value, syncResponse.getPlayer());
        Assert.assertNotEquals(null, syncResponse.getPlayer());
    }
}
