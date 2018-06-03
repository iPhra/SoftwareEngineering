package it.polimi.se2018.model;

import it.polimi.se2018.mvc.model.Bag;
import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.fail;

public class TestBag {
    private final int colorsNumber = 5;

    private Bag bag;

    @Before
    public void init() {
        int diceNumer = 90;
        bag = new Bag(colorsNumber, diceNumer);
    }

    @Test
    public void testInvalidParameterException() {
        try {
            bag = new Bag(97, 7);
        }
        catch (InvalidParameterException e) {
            return;
        }
        fail();
    }

    @Test
    public void testExtractDie()  {
        try {
            bag.extractDie();
        }
        catch (NoDieException e) {
            fail();
        }
    }

    @Test
    public void testExtractDieException()  {
        try {
            bag = new Bag(colorsNumber, 0);
            bag.extractDie();
        }
        catch (NoDieException e) {
            return;
        }
        fail();
    }

    @Test
    public void testInsertDie() {
        Random random = new Random();
        Die die = new Die (random.nextInt(6) + 1, Color.values()[random.nextInt(5)]);
        bag.insertDie(die);
    }

    @Test
    public void testDrawDie() {
        List<Die> dieList = bag.drawDice(5);
        Assert.assertTrue(!dieList.isEmpty());
        Assert.assertTrue(bag.drawDice(99).isEmpty());
    }
}
