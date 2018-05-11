package it.polimi.se2018.network.messages;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestCoordinate {
    Coordinate coordinate;

    @Before
    public void init() {
        coordinate = new Coordinate(4,5);
    }

    @Test
    public void testGetters() {
        Assert.assertEquals(4,coordinate.getRow());
        Assert.assertEquals(5,coordinate.getCol());
    }

    @Test
    public void testEquals(){
        Assert.assertEquals(coordinate,new Coordinate(4,5));
        Assert.assertEquals(coordinate,coordinate);
        Assert.assertNotEquals(coordinate,new Coordinate(5,5));
        Assert.assertNotEquals(coordinate,new Object());
        Assert.assertNotEquals(coordinate,null);
        Assert.assertNotEquals(coordinate,new Coordinate(4,6));
        Assert.assertEquals(coordinate.hashCode(),new Coordinate(4,5).hashCode());
        Assert.assertNotEquals(coordinate.hashCode(),new Coordinate(5,5).hashCode());
    }
}
