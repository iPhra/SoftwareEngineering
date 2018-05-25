package it.polimi.se2018.network.messages;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class TestCoordinate {
    private Coordinate coordinate;
    private int row;
    private int col;

    @Before
    public void init() {
        Random random = new Random();
        row = random.nextInt();
        col = random.nextInt();
        coordinate = new Coordinate(row,col);
    }

    @Test
    public void testGetters() {
        Assert.assertEquals(row,coordinate.getRow());
        Assert.assertEquals(col,coordinate.getCol());
    }

    @Test
    public void testEquals(){
        Assert.assertEquals(coordinate,new Coordinate(row,col));
        Assert.assertEquals(coordinate,coordinate);
        Assert.assertNotEquals(coordinate,new Coordinate(row+1,col));
        Assert.assertNotEquals(coordinate,new Object());
        Assert.assertNotEquals(coordinate,null);
        Assert.assertNotEquals(coordinate,new Coordinate(row,col+1));
        Assert.assertEquals(coordinate.hashCode(),new Coordinate(row,col).hashCode());
        Assert.assertNotEquals(coordinate.hashCode(),new Coordinate(row+1,col).hashCode());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals(coordinate.getDescription(), "row " + row + " col " + col);
    }
}
