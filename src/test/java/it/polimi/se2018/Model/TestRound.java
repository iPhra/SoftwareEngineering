package it.polimi.se2018.Model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestRound {
    private List<Integer> playersOrder;
    private int roundNumber;
    private Round round;

    @Before
    public void init(){
        Random random = new Random();
        playersOrder = Arrays.asList(1,2,3,4,4,3,2,1);
        roundNumber = random.nextInt(10)+1;
        round = new Round(playersOrder,roundNumber);

    }

    @Test
    public void testGetRoundNumber(){
        Assert.assertEquals(roundNumber,round.getRoundNumber());
        if (roundNumber<10) Assert.assertNotEquals(roundNumber+1,round.getRoundNumber());
        else Assert.assertNotEquals(roundNumber-1,round.getRoundNumber());
        Assert.assertNotEquals(24,round.getRoundNumber());
        Assert.assertNotEquals(-46,round.getRoundNumber());
    }

    @Test
    public void testGetCurrentPlayerIndex(){
        Assert.assertNotEquals(playersOrder.size(),round.getCurrentPlayerIndex());
        Assert.assertNotEquals(-12,round.getCurrentPlayerIndex());
    }


}
