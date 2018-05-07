package it.polimi.se2018.Controller;

import it.polimi.se2018.Exceptions.InvalidPlacementException;
import it.polimi.se2018.Exceptions.NoDieException;
import it.polimi.se2018.Exceptions.ToolCardException;
import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Messages.*;
import it.polimi.se2018.Model.Objectives.PublicObjectives.PublicObjective;
import it.polimi.se2018.Model.PlacementLogic.DiePlacer;
import it.polimi.se2018.Model.PlacementLogic.DiePlacerFirst;
import it.polimi.se2018.Model.PlacementLogic.DiePlacerNormal;
import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Model.ToolCards.ToolCard;
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
            int cost = toolCard.isAlreadyUsed()? 2:1;
            if(player.getFavorPoints()<cost) view.messageService("Not enough favor points",player);
            else {
                try {
                    toolCard.useCard(toolCardMessage);
                    player.setFavorPoints(player.getFavorPoints()-cost);
                    updateToolCard(toolCard,toolCardMessage);
                }
                catch (ToolCardException e) {view.messageService(e.getMessage(),player);}
            }
        }
    }

    //place a die
    @Override
    public void performMove(PlaceMessage placeMessage) {
        Player player = placeMessage.getPlayer();
        if(!player.hasDieInHand()) view.messageService("You haven't selected a die!",player);
        else {
            try {
                Die die = player.getDieInHand();
                if(player.isFirstMove()) {
                    placeDie(new DiePlacerFirst(die,placeMessage.getFinalPosition(),player.getMap()));
                    player.setFirstMove(true);
                }
                else {
                    placeDie(new DiePlacerNormal(die,placeMessage.getFinalPosition(),player.getMap()));
                }
                player.setDieInHand(null);
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
                draft(draftMessage);
            } catch (NoDieException e) {
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
            if (passMessage.getPlayer().hasDieInHand()) {
               dropDie(passMessage);
            }
            view.messageService("It's your turn",model.getPlayerByIndex((model.getRound().getCurrentPlayerIndex()))); //notifies to the next player that it's his turn
        }
    }

    private void endRound() {
        if (model.getRound().getRoundNumber() == Board.ROUNDSNUMBER) {
            this.endMatch();
        }
        else {
            startRound();
            view.messageService("It's your turn",model.getPlayerByIndex((model.getRound().getCurrentPlayerIndex())));
        }
    }

    //evaluates point for all players
    private void evaluatePoints() {
        for(Player player: model.getPlayers()) {
            int score = player.getPrivateObjective().evalPoints(player);
            for(PublicObjective pub: model.getPublicObjectives()) {
                score+=pub.evalPoints(player);
            }
            score+=player.getFavorPoints();
            score-=player.getMap().countEmptySlots();
            player.setScore(score);
        }
    }

    private void startRound() {
        model.setRound(model.getRound().changeRound());
        model.getRoundTracker().updateRoundTracker(model.getDraftPool().getAllDice());
        model.getDraftPool().fillDraftPool(model.getBag().drawDice(model.getPlayersNumber()));
    }

    private void endMatch() {
        evaluatePoints();
    }

    private void updateToolCard(ToolCard toolCard, ToolCardMessage toolCardMessage) {
        model.setToolCard(toolCard.setAlreadyUsed(),toolCardMessage.getToolCardNumber());
        model.getRound().setHasUsedCard(true);
    }

    private void draft(DraftMessage draftMessage) throws NoDieException {
        Die die = model.getDraftPool().getDie(draftMessage.getDraftPoolPosition());
        model.getDraftPool().removeFromDraftPool(die);
        draftMessage.getPlayer().setDieInHand(die);
        model.getRound().setHasDraftedDie(true);
    }

    private void dropDie(PassMessage passMessage) {
        Die die = passMessage.getPlayer().getDieInHand();
        passMessage.getPlayer().setDieInHand(null);
        model.getDraftPool().addToDraftPool(die);
    }

    private void placeDie(DiePlacer diePlacer) throws InvalidPlacementException {
        diePlacer.placeDie();
    }

    @Override
    public void update(Message input) {
        checkInput(input);
    }

}
