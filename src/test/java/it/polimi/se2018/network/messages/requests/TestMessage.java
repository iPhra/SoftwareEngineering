package it.polimi.se2018.network.messages.requests;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class TestMessage {

    @Test
    public void testGetPlayer() {
        int value = new Random().nextInt();
        Message message = new Message(value,0) {
            @Override
            public void handle(MessageHandler handler) {
            }
        };
        Assert.assertEquals(value,message.getPlayerID());
        Assert.assertNotEquals(null,message.getPlayerID());
    }
}
