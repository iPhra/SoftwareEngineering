package it.polimi.se2018.mvc.model;

import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.RoundTracker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestRoundTracker {
    /*private RoundTracker roundTracker;
    private List<Die> dice1;
    private List<Die> dice2;

    @Before
    public void init(){
        roundTracker = new RoundTracker(2);
        Die die1 = new Die(3, Color.GREEN);
        Die die2 = new Die(5,Color.BLUE);
        Die die3 = new Die(1,Color.PURPLE);
        Die die4 = new Die(6,Color.RED);
        dice1 = new ArrayList<>();
        dice1.add(die1);
        dice1.add(die2);
        dice1.add(die3);
        dice1.add(die4);
        roundTracker.updateRoundTracker(dice1);
        Die die5 = new Die(4,Color.RED);
        Die die6 = new Die(3,Color.BLUE);
        Die die7 = new Die(1,Color.YELLOW);
        Die die8 = new Die(5,Color.GREEN);
        dice2 = new ArrayList<>();
        dice2.add(die1);
        dice2.add(die2);
        dice2.add(die3);
        dice2.add(die4);
        roundTracker.updateRoundTracker(dice2);
    }

    @Test
    public void testIsVoid() {
        Assert.assertTrue(!roundTracker.isVoid());
        Assert.assertTrue(new RoundTracker(3).isVoid());
    }

    @Test
    public void testGetDie(){
        for(int i=0;i<4;i++){
            assertEquals(dice1.get(i),roundTracker.getDie(0,i));
        }
        for(int i=0;i<4;i++){
            assertEquals(dice2.get(i),roundTracker.getDie(1,i));
        }
    }

    @Test
    public void testContains() {
        assertTrue(roundTracker.contains(dice1.get(2)));
        assertTrue(!roundTracker.contains(new Die(4, Color.BLUE)));
    }

    //Non puoi rimuovere il dado mentre stai usando l'iteratore, cerca di risolvere in qualche modo
    @Test
    public void testRemoveFromRoundTracker(){
        Die die = roundTracker.getDie(0,3);
        roundTracker.removeFromRoundTracker(die);
        assertTrue(!roundTracker.contains(die));
    }

    @Test
    public void testAddToRoundTracker(){
        Die die = new Die(3,Color.RED);
        roundTracker.addToRoundTracker(1,die);
        assertEquals(die,roundTracker.getDie(1,4));
    }

    @Test
    public void testGetTurn() {
        Assert.assertEquals(2,roundTracker.getRound());
    }

    @Test
    public void testModelViewCopy() {
        assertTrue(dice1.containsAll(roundTracker.modelViewCopy().get(0)) && roundTracker.modelViewCopy().get(0).containsAll(dice1));
        assertTrue(dice2.containsAll(roundTracker.modelViewCopy().get(1)) && roundTracker.modelViewCopy().get(1).containsAll(dice2));
        roundTracker=new RoundTracker(10);
        assertTrue(dice1.containsAll(roundTracker.modelViewCopy().get(0)) && roundTracker.modelViewCopy().get(0).containsAll(new ArrayList<>()));
    }

    @Test
    public void testEquals() {
        RoundTracker roundTracker1 = new RoundTracker(2);
        roundTracker1.updateRoundTracker(dice1);
        roundTracker1.updateRoundTracker(dice2);
        Assert.assertEquals(roundTracker, roundTracker1);
        Assert.assertEquals(roundTracker1.hashCode(), roundTracker.hashCode());
        Assert.assertNotEquals(new Object(), roundTracker);
        Assert.assertNotEquals(null, roundTracker);
        Assert.assertEquals(roundTracker, roundTracker);
        Assert.assertNotEquals(new RoundTracker(3), roundTracker);
    }*/
}
