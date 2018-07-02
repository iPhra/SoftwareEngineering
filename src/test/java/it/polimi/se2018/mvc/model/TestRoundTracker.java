package it.polimi.se2018.mvc.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestRoundTracker {
    private RoundTracker roundTracker;
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
        Die die7 = new Die(1,Color.BLUE);
        Die die8 = new Die(5,Color.GREEN);
        dice2 = new ArrayList<>();
        dice2.add(die5);
        dice2.add(die6);
        dice2.add(die7);
        dice2.add(die8);
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
            Assert.assertEquals(dice1.get(i),roundTracker.getDie(0,i));
        }
        for(int i=0;i<4;i++){
            Assert.assertEquals(dice2.get(i),roundTracker.getDie(1,i));
        }
    }

    @Test
    public void testPopDie(){
        RoundTracker oneDieRoundTracker = new RoundTracker(2);
        List<Die> dice = new ArrayList<>();
        dice.add(new Die(2,Color.GREEN));
        oneDieRoundTracker.updateRoundTracker(dice);
        oneDieRoundTracker.popDie(0,0);
        Assert.assertTrue(oneDieRoundTracker.isVoid());
    }


    @Test
    public void testContainsColor(){
        Assert.assertTrue(roundTracker.containsColor(Color.GREEN));
        Assert.assertTrue(roundTracker.containsColor(Color.BLUE));
        Assert.assertTrue(roundTracker.containsColor(Color.RED));
        Assert.assertTrue(roundTracker.containsColor(Color.PURPLE));
        Assert.assertFalse(roundTracker.containsColor(Color.YELLOW));
        Assert.assertFalse(roundTracker.containsColor(Color.WHITE));
        RoundTracker noDiceRoundTracker = new RoundTracker(10);
        Assert.assertFalse(noDiceRoundTracker.containsColor(Color.GREEN));
        Assert.assertFalse(noDiceRoundTracker.containsColor(Color.BLUE));
        Assert.assertFalse(noDiceRoundTracker.containsColor(Color.RED));
        Assert.assertFalse(noDiceRoundTracker.containsColor(Color.PURPLE));
        Assert.assertFalse(noDiceRoundTracker.containsColor(Color.YELLOW));
        Assert.assertFalse(noDiceRoundTracker.containsColor(Color.WHITE));
    }

    @Test
    public void testAddToRoundTracker(){
        Die die = new Die(3,Color.RED);
        roundTracker.addToRoundTracker(1,die);
        Assert.assertEquals(die,roundTracker.getDie(1,4));
    }

    @Test
    public void testModelViewCopy() {
        Assert.assertTrue(dice1.containsAll(roundTracker.modelViewCopy().get(0)) && roundTracker.modelViewCopy().get(0).containsAll(dice1));
        Assert.assertTrue(dice2.containsAll(roundTracker.modelViewCopy().get(1)) && roundTracker.modelViewCopy().get(1).containsAll(dice2));
        roundTracker=new RoundTracker(10);
        Assert.assertTrue(dice1.containsAll(roundTracker.modelViewCopy().get(0)) && roundTracker.modelViewCopy().get(0).containsAll(new ArrayList<>()));
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
    }

}
