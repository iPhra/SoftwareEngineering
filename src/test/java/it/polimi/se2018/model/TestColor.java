package it.polimi.se2018.model;

import it.polimi.se2018.mvc.model.Color;
import org.junit.Assert;
import org.junit.Test;

public class TestColor {

    @Test
    public void testFromColor() {
        Assert.assertEquals(0, Color.fromColor(Color.BLUE));
        Assert.assertEquals(1,Color.fromColor(Color.RED));
        Assert.assertEquals(2,Color.fromColor(Color.GREEN));
        Assert.assertEquals(3,Color.fromColor(Color.YELLOW));
        Assert.assertEquals(4,Color.fromColor(Color.PURPLE));
        Assert.assertEquals(5,Color.fromColor(Color.WHITE));
        Assert.assertEquals(-1,Color.fromColor(null));
    }

    @Test
    public void testFromAbbreviation() {
        Assert.assertEquals(Color.RED,Color.fromAbbreviation("R"));
        Assert.assertEquals(Color.GREEN,Color.fromAbbreviation("G"));
        Assert.assertEquals(Color.PURPLE,Color.fromAbbreviation("P"));
        Assert.assertEquals(Color.YELLOW,Color.fromAbbreviation("Y"));
        Assert.assertEquals(Color.BLUE,Color.fromAbbreviation("B"));
        Assert.assertEquals(Color.WHITE,Color.fromAbbreviation("W"));
        Assert.assertEquals(null,Color.fromAbbreviation("Z"));
    }
}
