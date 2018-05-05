package it.polimi.se2018.Controller;

import it.polimi.se2018.Exceptions.InvalidPlacementException;
import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Messages.*;
import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Model.ToolCards.ToolCard;
import it.polimi.se2018.Utils.Observer;
import it.polimi.se2018.View.ServerView;

import java.security.InvalidParameterException;
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
    private void checkInput(Message message){
        Player player = message.getPlayer();
        if (!model.getRound().isYourTurn(player)) view.messageService("It's not your turn",player);
        else message.handle(this);
    }

    //use a toolcard
    @Override
    public void performMove(ToolCardMessage toolCardMessage) {
        Player player = toolCardMessage.getPlayer();
        if(model.getRound().hasUsedCard()) view.messageService("You have already used a Tool Card",player);
        else {
            ToolCard toolCard = model.getToolCards()[toolCardMessage.getToolCardNumber()];
            if(player.getFavorPoints()<(toolCard.isAlreadyUsed()? 2:1)) view.messageService("Not enough favor points",player);
            else {
                toolCard.useCard(toolCardMessage);
                player.setFavorPoints(player.getFavorPoints()-(toolCard.isAlreadyUsed()? 2:1));
                model.setToolCard(toolCard.setAlreadyUsed(),toolCardMessage.getToolCardNumber());
                model.getRound().setHasUsedCard(true);
            }
        }
    }

    //place a die
    @Override
    public void performMove(PlaceMessage placeMessage) {
        if(!placeMessage.getPlayer().hasDieInHand()) view.messageService("You haven't selected a die!",placeMessage.getPlayer());
        else {
            try {
                Die die = placeMessage.getPlayer().getDieInHand();
                if(placeMessage.getPlayer().isFirstMove()) {
                    placeMessage.getPlayer().getMap().placeDieOnEdge(die,placeMessage.getFinalPosition());
                    placeMessage.getPlayer().setFirstMove(true);
                }
                else placeMessage.getPlayer().getMap().placeDie(die,placeMessage.getFinalPosition());
                placeMessage.getPlayer().setDieInHand(null);
            }
            catch(InvalidPlacementException e) {view.messageService("You can't place the die there",placeMessage.getPlayer());}
        }
    }

    //draft a die
    @Override
    public void performMove(DraftMessage draftMessage) {
        if (model.getRound().hasDraftedDie()) view.messageService("You have already drafted!",draftMessage.getPlayer());
        else {
            try {
                Die die = model.getDraftPool().getDie(draftMessage.getDraftPoolPosition());
                model.getDraftPool().removeFromDraftPool(die);
                draftMessage.getPlayer().setDieInHand(die);
                model.getRound().setHasDraftedDie(true);
            } catch (InvalidParameterException e) {
                view.messageService("The die you want to draft does not exist", draftMessage.getPlayer());
            }
        }
    }

    //pass
    @Override
    public void performMove(PassMessage passMessage) {
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

    private void endRound() {
        if (model.getRound().getRoundNumber() == Board.ROUNDSNUMBER) {
            this.endMatch();
        }
        else {
            model.setRound(model.getRound().changeRound());
            model.getRoundTracker().updateRoundTracker((ArrayList<Die>)model.getDraftPool().modelViewCopy());
            model.getDraftPool().emptyDraftPool();
            startRound();
            view.messageService("It's your turn",model.getPlayerByIndex((model.getRound().getCurrentPlayerIndex())));
        }
    }

    //evaluates point for all players and broadcasts each score to each player
    private void evaluatePoints() {}

    private void startRound() {
        model.getDraftPool().fillDraftPool(model.getBag().drawDice(model.getPlayersNumber()));
    }

    private void endMatch() {
    }

    @Override
    public void update(Message input) {
        checkInput(input);
    }

}
