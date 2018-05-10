package it.polimi.se2018.Model;

import it.polimi.se2018.Model.Objectives.PrivateObjectives.PrivateObjective;

public class Player {
    private final String name;
    private final int id;
    private final Map map;
    private final PrivateObjective privateObjective;
    private int favorPoints;
    private int score;
    private boolean isFirstMove;
    private Die dieInHand;
    private boolean hasDraftedDie;
    private boolean hasUsedCard;

    public Player(String name, int id, Map map, PrivateObjective privateObjective) {
        this.name=name;
        this.favorPoints=map.getLevel();
        this.id=id;
        this.map=map;
        this.privateObjective=privateObjective;
        isFirstMove=false;
        hasDraftedDie=false;
        hasUsedCard=false;
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

    public Map getMap() {
        return map;
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

    public boolean hasDieInHand() {
        return dieInHand!=null;
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
}
