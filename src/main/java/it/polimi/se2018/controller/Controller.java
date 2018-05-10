package it.polimi.se2018.controller;

import it.polimi.se2018.exceptions.InvalidPlacementException;
import it.polimi.se2018.exceptions.NoDieException;
import it.polimi.se2018.exceptions.ToolCardException;
import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.network.messages.requests.*;
import it.polimi.se2018.network.messages.responses.ModelViewResponse;
import it.polimi.se2018.network.messages.responses.TextResponse;
import it.polimi.se2018.network.messages.responses.ToolCardResponse;
import it.polimi.se2018.network.messages.responses.TurnStartResponse;
import it.polimi.se2018.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.model.placementlogic.DiePlacer;
import it.polimi.se2018.model.placementlogic.DiePlacerFirst;
import it.polimi.se2018.model.placementlogic.DiePlacerNormal;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.toolcards.ToolCard;
import it.polimi.se2018.utils.Observer;

public class Controller implements Observer<Message>, MessageHandler {
    private final Board model;
    private final ToolCardController toolCardController;

    public Controller(Board model) {
        super();
        this.model = model;
        toolCardController = new ToolCardController(model);
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
        try {
            ToolCard toolCard = player.getCardInUse();
            toolCard.handle(toolCardController,toolCardMessage);
            updateToolCard(toolCard,toolCardMessage);
            model.notify(new ModelViewResponse(model));
        }
        catch (ToolCardException e) {model.notify(new TextResponse(player,e.getMessage()));}
    }

    @Override
    public void performMove(ToolCardRequestMessage toolCardRequestMessage) {
        Player player = toolCardRequestMessage.getPlayer();
        if(player.hasUsedCard()) model.notify(new TextResponse(player,"You have already used a Tool Card"));
        else {
            ToolCard toolCard = model.getToolCards()[toolCardRequestMessage.getToolCardNumber()];
            int cost = toolCard.isAlreadyUsed()? 2:1;
            if(player.getFavorPoints()<cost) model.notify(new TextResponse(player,"Not enough favor points"));
            else {
                player.setCardInUse(toolCard);
                model.notify(new ToolCardResponse(player,toolCard.getPlayerRequests()));
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
        if (player.hasDraftedDie()) model.notify(new TextResponse(player,"You have already drafted"));
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
        Player player = passMessage.getPlayer();
        if (model.getRound().isLastTurn()) {
            this.endRound();
        }
        else {
            pass(player);
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
        Player player = toolCardMessage.getPlayer();
        model.setToolCard(toolCard.setAlreadyUsed(),toolCardMessage.getToolCardNumber());
        player.setFavorPoints(player.getFavorPoints()-(toolCard.isAlreadyUsed()? 2:1));
        player.setHasUsedCard(true);
        player.setCardInUse(null);
    }

    private void draft(DraftMessage draftMessage) throws NoDieException {
        Die die = model.getDraftPool().getDie(draftMessage.getDraftPoolPosition());
        model.getDraftPool().removeFromDraftPool(die);
        draftMessage.getPlayer().setDieInHand(die);
        draftMessage.getPlayer().setHasDraftedDie(true);
    }

    private void pass(Player player) {
        model.getRound().changeTurn();
        player.setHasUsedCard(false);
        player.setHasDraftedDie(false);
        if (player.hasDieInHand()) {
            Die die = player.getDieInHand();
            player.setDieInHand(null);
            model.getDraftPool().addToDraftPool(die);
        }
    }

    private void placeDie(DiePlacer diePlacer) throws InvalidPlacementException {
        diePlacer.placeDie();
    }

    @Override
    public void update(Message input) {
        checkInput(input);
    }

}
