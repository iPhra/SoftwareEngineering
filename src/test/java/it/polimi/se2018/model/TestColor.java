package it.polimi.se2018.model;

import it.polimi.se2018.mvc.model.Color;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testGetAbbreviation() {
        Assert.assertEquals("G", Color.GREEN.getAbbreviation());
        Assert.assertEquals("R", Color.RED.getAbbreviation());
        Assert.assertEquals("B", Color.BLUE.getAbbreviation());
        Assert.assertEquals("P", Color.PURPLE.getAbbreviation());
        Assert.assertEquals("W", Color.WHITE.getAbbreviation());
        Assert.assertEquals("Y", Color.YELLOW.getAbbreviation());
    }

    @Test
    public void testGetAllAbbreviations() {
        List<String> result = new ArrayList<>();
        result.add("G");
        result.add("Y");
        result.add("B");
        result.add("P");
        result.add("R");
        result.add("W");
        Assert.assertTrue(result.containsAll(Color.getAllAbbreviations()) && Color.getAllAbbreviations().containsAll(result));
    }
}
