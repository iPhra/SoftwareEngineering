package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.GameInstance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestGameManager {
    private GameManager gameManager;

    @Before
    public void init() {
        GameInstance gameInstance = new GameInstance();
        gameInstance.createGame();
        gameManager = gameInstance.getManager();
    }

    @Test
    public void testParameters() {
        Assert.assertTrue(gameManager.isMatchCreated());
        Assert.assertTrue(gameManager.isMatchPlaying());
        Assert.assertEquals(4,gameManager.playersNumber());
        for(int i=1;i<=4;i++) {
            Assert.assertEquals("Player " +String.valueOf(i),gameManager.getNicknameById(i));
        }
    }

    @Test
    public void testDisconnection() {
        for(int i=1; i<=4; i++) {
            Assert.assertFalse(gameManager.isDisconnected(i)); //everyone is still connected
        }

        gameManager.setDisconnected(1); //disconnect player 1
        Assert.assertTrue(gameManager.isDisconnected(1));

        gameManager.setDisconnected(2); //disconnect player 2
        Assert.assertTrue(gameManager.isDisconnected(2));

        gameManager.setDisconnected(3); //disconnect player 3
        Assert.assertTrue(gameManager.isDisconnected(3));
        Assert.assertFalse(gameManager.isMatchPlaying()); //match is over, player 4 won
    }

    @Test
    public void testReconnection() {
        gameManager.setDisconnected(1); //disconnect player 1
        Assert.assertTrue(gameManager.isDisconnected(1));

        gameManager.setReconnected(1,null);
        Assert.assertFalse(gameManager.isDisconnected(1));
    }
}
