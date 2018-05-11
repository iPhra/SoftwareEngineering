package it.polimi.se2018.network.messages.requests;

import it.polimi.se2018.network.messages.Coordinate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class TestPlaceMessage {
    PlaceMessage placeMessage;
    Coordinate coordinate;
    Random random;

    @Before
    public void init() {
        random = new Random();
        coordinate = new Coordinate(random.nextInt(),random.nextInt());
        placeMessage = new PlaceMessage(null, coordinate);
    }

    @Test
    public void testGetFinalPosition() {
        Assert.assertEquals(coordinate,placeMessage.getFinalPosition());
    }

    @Test
    public void testSetFinalPosition() {
        Coordinate coordinate = new Coordinate(random.nextInt(),random.nextInt());
        placeMessage.setFinalPosition(coordinate);
        Assert.assertEquals(coordinate,placeMessage.getFinalPosition());
    }
    
}
