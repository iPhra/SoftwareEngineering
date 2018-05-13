package it.polimi.se2018.model;

import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.fail;

public class TestBag {
    public int colorsNumber = 5;
    public int diceNumer = 90;
    public Bag bag;

    @Before
    public void init() {
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
    public void testExtractDie() throws NoDieException {
        Die die = bag.extractDie();
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
        dieList = bag.drawDice(99);
    }
}
