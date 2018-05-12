package it.polimi.se2018.network.messages.requests;

import it.polimi.se2018.Database;
import it.polimi.se2018.model.Map;
import it.polimi.se2018.model.Player;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class TestMessage {

    @Test
    public void testGetPlayer() {
        Player player = new Player("test",new Random().nextInt(),new Database().getDefaultMaps().get(0).getKey(),null);
        Message message = new Message(player) {
            @Override
            public void handle(MessageHandler handler) {
            }
        };
        Assert.assertEquals(player,message.getPlayer());
        Assert.assertNotEquals(null,message.getPlayer());
    }
}
