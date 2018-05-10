package it.polimi.se2018.network.messages;

import org.junit.Assert;
import org.junit.Test;

public class TestCoordinate {

    @Test
    public void testGetters() {
        Coordinate coordinate = new Coordinate(4,5);
        Assert.assertEquals(4,coordinate.getRow());
        Assert.assertEquals(5,coordinate.getCol());
    }
}
