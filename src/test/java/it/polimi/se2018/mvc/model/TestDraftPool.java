package it.polimi.se2018.mvc.model;

import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.DraftPool;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class TestDraftPool {
    private DraftPool draftPool;
    private List<Die> dice;

    @Before
    public void init() {
        Die die1 = new Die(3, Color.GREEN);
        Die die2 = new Die(5,Color.BLUE);
        Die die3 = new Die(1,Color.PURPLE);
        Die die4 = new Die(7,Color.RED);
        dice = new ArrayList<>();
        dice.add(die1);
        dice.add(die2);
        dice.add(die3);
        dice.add(die4);
        draftPool = new DraftPool();
        draftPool.fillDraftPool(dice);
    }

    @Test
    public void testGetDie() throws NoDieException{
        for(int i=0; i<dice.size(); i++) {
            assertEquals(dice.get(i),draftPool.getDie(i));
            assertNotEquals(null,draftPool.getDie(i));
        }
    }

    @Test
    public void testContains() {
        assertTrue(draftPool.contains(dice.get(3)));
        assertTrue(!draftPool.contains(new Die(4,Color.RED)));
    }

    @Test
    public void testGetDieException() {
        try {
            draftPool.getDie(10);
        }
        catch (NoDieException e) {
            return;
        }
        fail();
    }

    @Test
    public void testGetDieException2()  {
        try {
            draftPool.getDie(-4);
        }
        catch (NoDieException e) {
            return;
        }
        fail();
    }

    @Test
    public void testGetAllDice() {
        assertTrue(draftPool.getAllDice().containsAll(dice) && dice.containsAll(draftPool.getAllDice()));
    }

    @Test
    public void testAddToDraftPool() throws NoDieException{
        Die die = new Die(3,Color.RED);
        draftPool.addToDraftPool(die);
        assertEquals(die,draftPool.getDie(4));
    }

    @Test
    public void testRemoveFromDraftPool() throws NoDieException{
        Die die = draftPool.getDie(2);
        draftPool.removeFromDraftPool(die);
        assertTrue(!draftPool.contains(die));
    }

    @Test
    public void testRemoveFromDraftPoolException()  {
        try {
            draftPool.removeFromDraftPool(new Die(5,Color.GREEN));
        }
        catch (NoDieException e) {
            return;
        }
        fail();
    }

    @Test
    public void testModelViewCopy() {
        assertTrue(draftPool.modelViewCopy().containsAll(draftPool.getAllDice()) && draftPool.getAllDice().containsAll(draftPool.modelViewCopy()));
        draftPool=new DraftPool();
        assertTrue(draftPool.modelViewCopy().containsAll(draftPool.getAllDice()) && draftPool.getAllDice().containsAll(draftPool.modelViewCopy()));
    }

    @Test
    public void testEquals() {
        DraftPool draftPool2 = new DraftPool();
        draftPool2.fillDraftPool(dice);
        Assert.assertEquals(draftPool,draftPool2);
        Assert.assertEquals(draftPool2.hashCode(),draftPool2.hashCode());
        Assert.assertNotEquals(new Object(), draftPool);
        Assert.assertNotEquals(null,draftPool);
        Assert.assertEquals(draftPool,draftPool);
    }
}
