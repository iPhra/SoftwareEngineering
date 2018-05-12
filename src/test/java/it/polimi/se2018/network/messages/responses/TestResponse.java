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
        Player player = new Player("test",new Random().nextInt(),new Database().getDefaultMaps().get(0).getKey(),null);
        Response response = new Response(player) {
            @Override
            public void handle(ResponseHandler handler) {
            }
        };
        Assert.assertEquals(player,response.getPlayer());
        Assert.assertNotEquals(null,response.getPlayer());
    }
}
