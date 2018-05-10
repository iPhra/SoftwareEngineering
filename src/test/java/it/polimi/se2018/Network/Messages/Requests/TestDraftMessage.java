package it.polimi.se2018.Network.Messages.Requests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

//Can't test handle method, it will be part of integration testing
public class TestDraftMessage {
    private DraftMessage message;
    private int position;

    @Before
    public void init() {
        position=new Random().nextInt();
        message = new DraftMessage(null,position);
    }

    @Test
    public void testGetDraftPoolPosition() {
        Assert.assertEquals(position,message.getDraftPoolPosition());
    }

    @Test
    public void testSetDraftPoolPosition() {
        int newpos = new Random().nextInt();
        message.setDraftPoolPosition(newpos);
        Assert.assertEquals(newpos,message.getDraftPoolPosition());
    }
}
