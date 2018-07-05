package it.polimi.se2018.mvc.model.toolcards;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestToolCard {
     private GrozingPliers grozingPliers;

    @Before
    public void init(){
        grozingPliers = GrozingPliers.instance();
    }

    @Test
    public void testGetImagePath(){
        Assert.assertEquals("/toolcards/grozling_pliers.png",grozingPliers.getImagePath());
    }

    @Test
    public void testToString(){
        Assert.assertEquals("Title: \"" + "Grozing Pliers" + "\", Effect: \"" + "After drafting, increase or decrease the value of the drafted die by 1" + "\"", grozingPliers.toString());
    }
}
