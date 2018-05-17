package it.polimi.se2018.model;

import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.model.objectives.privateobjectives.PrivateObjective;

public class Player {
    private final String name;
    private final int id;
    private final Window window;
    private final PrivateObjective privateObjective;
    private int favorPoints;
    private int score;
    private boolean isFirstMove; //true if this player hasn't moved yet
    private Die dieInHand;
    private int cardInUse;
    private boolean hasDraftedDie;
    private boolean hasUsedCard;

    public Player(String name, int id, Window window, PrivateObjective privateObjective) {
        this.name = name;
        this.favorPoints = window.getLevel();
        this.id = id;
        this.window = window;
        this.privateObjective = privateObjective;
        isFirstMove = false;
        hasDraftedDie = false;
        hasUsedCard = false;
    }

    public String getName() {
        return name;
    }

    public int getFavorPoints() {
        return favorPoints;
    }

    public void setFavorPoints(int favorPoints) {
        this.favorPoints = favorPoints;
    }

    public int getId() {
        return id;
    }

    public Window getWindow() {
        return window;
    }

    public PrivateObjective getPrivateObjective() {
        return privateObjective;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }

    public Die getDieInHand() {
        return dieInHand;
    }

    public void setDieInHand(Die draftedDie) {
        this.dieInHand = draftedDie;
    }

    public void dropDieInHand() {dieInHand = null;}

    public boolean hasDieInHand() {
        return dieInHand != null;
    }

    public boolean hasDraftedDie() {
        return hasDraftedDie;
    }

    public void setHasDraftedDie(boolean hasDraftedDie) {
        this.hasDraftedDie = hasDraftedDie;
    }

    public boolean hasUsedCard() {
        return hasUsedCard;
    }

    public void setHasUsedCard(boolean hasUsedCard) {
        this.hasUsedCard = hasUsedCard;
    }

    public int getCardInUse() throws ToolCardException {
        if(cardInUse==-1) throw new ToolCardException("You haven't selected a Tool Card");
        return cardInUse;
    }

    public void setCardInUse(int cardInUse) {
        this.cardInUse = cardInUse;
    }

    public void dropCardInUse() {cardInUse = -1;}
}
