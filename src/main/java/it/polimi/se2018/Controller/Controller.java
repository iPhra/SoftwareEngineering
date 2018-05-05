package it.polimi.se2018.Controller;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Messages.*;
import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Utils.Observer;
import it.polimi.se2018.View.ServerView;

import java.util.ArrayList;

public class Controller implements Observer<Message>, MessageHandler {
    private final Board model;
    private final ServerView view;

    public Controller(Board model, ServerView view) {
        super();
        this.model = model;
        this.view = view;
    }

    //reads player, checks if it's his turn, call performMove
    public void handleMessage(Message message){
        Player player = message.getPlayer();
        if (!model.getRound().isYourTurn(player)) view.messageService("It's not your turn",player);
        else performMove(message);
    }

    public void performMove(Message message) {}

    //use a toolcard
    //Player::getFavorPoints() deve essere coerente con ToolCard::isAlreadyUsed()   (o fai tre if o sdoppi il codice)
    //ToolCard::useCard()
    //Player::setFavorPoints()
    //ToolCard::setAlreadyUsed()
    //Round::setHasUsedCard()
    private void performMove(ToolCardMessage toolCardMessage) {}

    //place a die
    //player.getMap().placeDie(player.getDieInHand(),row,col);
    //player.setDieInHand(null);
    private void performMove(PlaceMessage placeMessage) {}

    //draft a die
    private void performMove(DraftMessage draftMessage) {
        if (model.getRound().hasDraftedDie()) view.messageService("Already drafted!",draftMessage.getPlayer());
        try {
            Die die = model.getDraftPool().getDie(draftMessage.getDraftPoolPosition());
            model.getDraftPool().removeFromDraftPool(die);
            draftMessage.getPlayer().setDieInHand(die);
            model.getRound().setHasDraftedDie(true);
        }
        catch(Exception e) {
            view.messageService("The die you want to draft does not exist",draftMessage.getPlayer());
        }
    }

    //pass
    private void performMove(PassMessage passMessage) {
        if (model.getRound().isLastTurn()) {
            this.endRound();
        }
        else {
            model.getRound().changeTurn();
            if (passMessage.getPlayer().hasDieInHand()) { //if given player has a die in his hand, we put it back in the draftpool
                Die die = passMessage.getPlayer().getDieInHand();
                passMessage.getPlayer().setDieInHand(null);
                model.getDraftPool().addToDraftPool(die);
            }
            view.messageService("It's your turn",model.getPlayerByIndex((model.getRound().getCurrentPlayerIndex()))); //notifies to the next player that it's his turn
        }
    }

    //evaluates point for all players and broadcasts each score to each player
    private void evaluatePoints() {}

    private void initDraftPool() {
        model.getDraftPool().fillDraftPool(model.getBag().drawDice(model.getPlayersNumber()));
    }

    private void startRound() {
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
            view.messageService("It's your turn",model.getPlayerByIndex((model.getRound().getCurrentPlayerIndex())));
        }
    }


    private void endMatch() {
    }

    @Override
    public void update(Message input) {
    }
}
