package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.Database;
import it.polimi.se2018.model.Map;
import it.polimi.se2018.model.Player;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class TestResponse {

    @Test
    public void test() {
        int value = new Random().nextInt();
        Response response = new Response(value) {
            @Override
            public void handle(ResponseHandler handler) {
            }
        };
        Assert.assertEquals(value,response.getPlayer());
        Assert.assertNotEquals(null,response.getPlayer());
    }
}
