package it.polimi.se2018.Model;

import it.polimi.se2018.Exceptions.NoDieException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TestDraftPool {
    private DraftPool draftPool;
    private List<Die> dice;

    @Before
    public void init() {
        Die die1 = new Die(3,Color.GREEN);
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
            Assert.assertEquals(dice.get(i),draftPool.getDie(i));
        }
    }

    @Test
    public void testContains() {
        assertTrue(draftPool.contains(dice.get(3)));
        assertTrue(!draftPool.contains(new Die(4,Color.RED)));
    }

    @Test
    public void testGetDieException() throws NoDieException {
        try {
            draftPool.getDie(10);
        }
        catch (NoDieException e) {
            return;
        }
        fail();
    }

    @Test
    public void testGetDieException2() throws NoDieException {
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
        Assert.assertTrue(draftPool.getAllDice().containsAll(dice) && dice.containsAll(draftPool.getAllDice()));
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
    public void testRemoveFromDraftPoolException() throws NoDieException {
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
        Assert.assertTrue(dice.containsAll(draftPool.modelViewCopy()) && draftPool.modelViewCopy().containsAll(dice));
    }

}
