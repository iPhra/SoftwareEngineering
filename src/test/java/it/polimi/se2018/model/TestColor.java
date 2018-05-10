package it.polimi.se2018.model;

import org.junit.Assert;
import org.junit.Test;

public class TestColor {

    @Test
    public void testFromColor() {
        Assert.assertEquals(0,Color.fromColor(Color.BLUE));
        Assert.assertEquals(1,Color.fromColor(Color.RED));
        Assert.assertEquals(2,Color.fromColor(Color.GREEN));
        Assert.assertEquals(3,Color.fromColor(Color.YELLOW));
        Assert.assertEquals(4,Color.fromColor(Color.PURPLE));
        Assert.assertEquals(5,Color.fromColor(Color.WHITE));
        Assert.assertEquals(-1,Color.fromColor(null));
    }
}
