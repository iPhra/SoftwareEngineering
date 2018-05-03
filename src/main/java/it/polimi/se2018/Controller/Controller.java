package it.polimi.se2018.Controller;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Moves.MoveMessage;
import it.polimi.se2018.Utils.Observer;
import it.polimi.se2018.View.ServerView;

import java.util.ArrayList;

public class Controller implements Observer<MoveMessage> {
    private final Board model;
    private final ServerView view;

    public Controller(Board model, ServerView view) {
        super();
        this.model = model;
        this.view = view;
    }

    //reads player, checks if it's his turn, reads move id, calls right methods
    private void performMove(MoveMessage move){
    }

    //draftPool.removeFromDraftPool(die);
    //player.setDieInHand(die);
    //round.setHasDraftedDie(true);
    private void draft(MoveMessage move) {}

    //calcola il punteggio, setta la score del player desiderato
    private void evaluatePoints(MoveMessage move) {}

    //Player::getFavorPoints() deve essere coerente con ToolCard::isAlreadyUsed()   (o fai tre if o sdoppi il codice)
    //ToolCard::useCard()
    //Player::setFavorPoints()
    //ToolCard::setAlreadyUsed()
    //Round::setHasUsedCard()
    private void useToolCard(MoveMessage move) {}

    //player.getMap().placeDie(player.getDieInHand(),row,col);
    //player.setDieInHand(null);
    private void placeDie(MoveMessage move) {}

    private void initDraftPool() {
        model.getDraftPool().fillDraftPool(model.getBag().drawDice(model.getPlayersNumber()));
    }

    public void startRound() {
        initDraftPool();
    }

    private void endRound() {
        if (model.getRound().getRoundNumber() == model.ROUNDSNUMBER) {
            this.endMatch();
        }
        else {
            model.setRound(model.getRound().changeRound());
            model.getRoundTracker().updateRoundTracker((ArrayList<Die>)model.getDraftPool().modelViewCopy());
            model.getDraftPool().emptyDraftPool();
        }
    }

    public void pass() {
        if (model.getRound().isLastTurn()) {
            this.endRound();
        }
        else {
            model.getRound().changeTurn();
        }
    }

    private void endMatch() {
    }

    @Override
    public void update(MoveMessage moveMessage) {
        performMove(moveMessage);
    }
}
