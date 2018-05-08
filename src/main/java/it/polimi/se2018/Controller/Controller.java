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
        if (!model.getRound().isYourTurn(player)) model.notify(new TextResponse(player,"It's not your turn"));
        else message.handle(this);
    }

    //use a toolcard
    @Override
    public void performMove(ToolCardMessage toolCardMessage) {
        Player player = toolCardMessage.getPlayer();
        if(model.getRound().hasUsedCard()) model.notify(new TextResponse(player,"You have already used a Tool Card"));
        else {
            ToolCard toolCard = model.getToolCards()[toolCardMessage.getToolCardNumber()];
            int cost = toolCard.isAlreadyUsed()? 2:1;
            if(player.getFavorPoints()<cost) model.notify(new TextResponse(player,"Not enough favor points"));
            else {
                try {
                    toolCard.useCard(toolCardMessage);
                    player.setFavorPoints(player.getFavorPoints()-cost);
                    updateToolCard(toolCard,toolCardMessage);
                    model.notify(new ModelViewResponse(model));
                }
                catch (ToolCardException e) {model.notify(new TextResponse(player,e.getMessage()));}
            }
        }
    }

    //place a die
    @Override
    public void performMove(PlaceMessage placeMessage) {
        Player player = placeMessage.getPlayer();
        if(!player.hasDieInHand()) model.notify(new TextResponse(player,"You haven't selected a die"));
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
                model.notify(new ModelViewResponse(model));
            }
            catch(InvalidPlacementException e) {model.notify(new TextResponse(player,"You can't place the die there"));}
        }
    }

    //draft a die
    @Override
    public void performMove(DraftMessage draftMessage) {
        Player player = draftMessage.getPlayer();
        if (model.getRound().hasDraftedDie()) model.notify(new TextResponse(player,"You have already drafted"));
        else {
            try {
                draft(draftMessage);
                model.notify(new ModelViewResponse(model));
            } catch (NoDieException e) {
                model.notify(new TextResponse(player,"The die you want to draft does not exit"));
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
            Player nextPlayer = model.getPlayerByIndex(model.getRound().getCurrentPlayerIndex());
            model.notify(new TurnStartResponse(nextPlayer));
            model.notify(new ModelViewResponse(model));
        }
    }

    private void endRound() {
        if (model.getRound().getRoundNumber() == Board.ROUNDSNUMBER) {
            this.endMatch();
        }
        else {
            startRound();
            Player nextPlayer = model.getPlayerByIndex(model.getRound().getCurrentPlayerIndex());
            model.notify(new TurnStartResponse(nextPlayer));
            model.notify(new ModelViewResponse(model));
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
        model.notify(new ModelViewResponse(model));
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
