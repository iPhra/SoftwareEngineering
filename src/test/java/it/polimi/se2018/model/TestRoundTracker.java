package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestRoundTracker {
    private RoundTracker roundTracker;
    private ArrayList<Die> dice1;
    private ArrayList<Die> dice2;

    //non puoi estrarre a caso un valore e fare il roundtracker grande quanto quel valore, perché tu poi lo riempi dando
    //per scontato che sia grande almeno 2 posizioni l'array, mentre potresti estrarre meno di 2
    @Before
    public void init(){
        roundTracker = new RoundTracker(2);
        Die die1 = new Die(3,Color.GREEN);
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
}
