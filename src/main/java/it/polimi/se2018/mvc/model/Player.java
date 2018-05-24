package it.polimi.se2018.mvc.model;

import it.polimi.se2018.mvc.controller.Controller;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;

/**
 * This class represents a single player
 * @author Emilio Imperiali
 */
public class Player {

    /**
     * This is the name of the player
     */
    private final String name;

    /**
     * This is the unique ID of the player
     */
    private final int id;

    /**
     * This is the window chosen by the player
     */
    private final Window window;

    /**
     * This is the private objective of the player
     */
    private final PrivateObjective privateObjective;

    /**
     * This number identifies how many favor points the player has left
     */
    private int favorPoints;

    /**
     * This is the score of the player
     */
    private int score;

    /**
     * This is true if this player hasn't moved yet
     */
    private boolean isFirstMove;

    /**
     * This is true if this player has a die in hand (if he drafted a die)
     */
    private Die dieInHand;

    /**
     * This number identifies what tool card is the player using
     */
    private int cardInUse;

    /**
     * This is true if this player has drafted a die
     */
    private boolean hasDraftedDie;

    /**
     * This is true if player has used a tool card
     */
    private boolean hasUsedCard;

    public Player(String name, int id, Window window, PrivateObjective privateObjective) {
        this.name = name;
        this.favorPoints = window.getLevel();
        this.id = id;
        this.window = window;
        this.privateObjective = privateObjective;
        isFirstMove = true;
        hasDraftedDie = false;
        hasUsedCard = false;
    }

    /**
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * @return the number of favor points of the player
     */
    public int getFavorPoints() {
        return favorPoints;
    }

    /**
     * sets the number of favor points of the player
     * @param favorPoints it's the new number of favor points the this player will have after calling this method
     */
    public void setFavorPoints(int favorPoints) {
        this.favorPoints = favorPoints;
    }

    /**
     * @return the ID of this player
     */
    public int getId() {
        return id;
    }

    /**
     * @return the window of this player
     */
    public Window getWindow() {
        return window;
    }

    /**
     * @return the private objective of this player
     */
    public PrivateObjective getPrivateObjective() {
        return privateObjective;
    }

    /**
     * @return the score of this player
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score of this player
     * @param score it's the score that has to be setted
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return true if it's the first move of this player, false otherwise
     */
    public boolean isFirstMove() {
        return isFirstMove;
    }

    /**
     * Sets if it's or it's not the first move of this player
     * Used by {@link Controller} when arrives a PlaceMessage message: if
     * it's the first move of this player, the die is placed and then it has to be setted that isFirstMove
     * is no longer true
     * @param firstMove it's the new value of IsFirstMove. Because of how this method is used, this will
     *                  typically be always passed as false
     */
    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }

    /**
     * @return the die that this player has in hand
     */
    public Die getDieInHand() {
        return dieInHand;
    }

    /**
     * Sets the die in hand of this player
     * @param draftedDie it's the die that this player will have in hand after calling this method
     */
    public void setDieInHand(Die draftedDie) {
        this.dieInHand = draftedDie;
    }

    /**
     * Used when the player drops the die he has in hand, so dieInHand is setted as null
     */
    public void dropDieInHand() {dieInHand = null;}

    /**
     * @return true if this player has a die in hand
     */
    public boolean hasDieInHand() {
        return dieInHand != null;
    }

    /**
     * @return true if this player has drafted a die
     */
    public boolean hasDraftedDie() {
        return hasDraftedDie;
    }

    /**
     * Sets if this player has drafted a die or not
     * @param hasDraftedDie it's the boolean indicating the new value of hasDraftedDie
     */
    public void setHasDraftedDie(boolean hasDraftedDie) {
        this.hasDraftedDie = hasDraftedDie;
    }

    /**
     * @return true if this player has used a tool card
     */
    public boolean hasUsedCard() {
        return hasUsedCard;
    }

    /**
     * Sets if this player has used a tool card or not
     * @param hasUsedCard it's the boolean indicating the new value of hasUsedCard
     */
    public void setHasUsedCard(boolean hasUsedCard) {
        this.hasUsedCard = hasUsedCard;
    }

    /**
     * Used by {@link Controller} when it's received a ToolCardMessage message
     * @return a number indicating what tool card is the player using
     * @throws ToolCardException if the player hasn't selected any tool card
     */
    public int getCardInUse() throws ToolCardException {
        if(cardInUse==-1) throw new ToolCardException("You haven't selected a Tool Card");
        return cardInUse;
    }

    /**
     * Sets the number indicating what tool card is in use
     * @param cardInUse it's the number indicating the tool card in use that has to be setted
     */
    public void setCardInUse(int cardInUse) {
        this.cardInUse = cardInUse;
    }

    /**
     * Used to drop the tool card in use, so when the player isn't using a tool card anymore
     */
    public void dropCardInUse() {cardInUse = -1;}
}
