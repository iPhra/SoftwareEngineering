package it.polimi.se2018.Model;

import it.polimi.se2018.Exceptions.DieException;
import it.polimi.se2018.Exceptions.NoDieException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.fail;

public class TestDie {
    private Die die;
    private Color color;
    private int value;

    @Before
    public void init() {
        Random random = new Random();
        color= Color.values()[random.nextInt(6)+1];
        value= random.nextInt(6)+1;
        die = new Die(value,color);
    }

    @Test
    public void testGetValue() {
        Assert.assertEquals(value,die.getValue());
        if (value<6) Assert.assertNotEquals(value+1,die.getValue());
        else Assert.assertNotEquals(value-1,die.getValue());
        Assert.assertNotEquals(10,die.getValue());
    }

    @Test
    public void testGetColor() {
        Assert.assertEquals(color,die.getColor());
        if (Color.fromColor(color)<5) Assert.assertNotEquals(Color.values()[Color.fromColor(color)+1],die.getColor());
        else Assert.assertNotEquals(Color.values()[Color.fromColor(color)-1],die.getColor());
    }

    //can't test this!
    @Test
    public void testRollDie() {
        die.rollDie();
    }

    @Test
    public void testFlipDie() {
        die.flipDie();
        Assert.assertEquals(7-value,die.getValue());
    }

    @Test
    public void testSetValue() throws DieException{
        int random = new Random().nextInt(6)+1;
        die.setValue(random);
        Assert.assertEquals(random,die.getValue());
    }

    @Test
    public void testSetValueException() {
        try {
            die.setValue(8);
        }
        catch (DieException e) {
            return;
        }
        fail();
    }

    @Test
    public void testModelViewCopy() {
        Die newDie = die.modelViewCopy();
        Assert.assertEquals(newDie,die);
    }
}
