package it.polimi.se2018.controller;

import it.polimi.se2018.network.messages.responses.*;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.exceptions.*;
import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.network.messages.requests.*;
import it.polimi.se2018.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.controller.placementlogic.DiePlacer;
import it.polimi.se2018.controller.placementlogic.DiePlacerFirst;
import it.polimi.se2018.controller.placementlogic.DiePlacerNormal;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.toolcards.ToolCard;
import it.polimi.se2018.utils.Observer;

public class Controller extends Observable<Message> implements Observer<Message>, MessageHandler {
    private Board model;
    private final ToolCardController toolCardController;

    public Controller() {
        super();
        toolCardController = new ToolCardController(model);
    }

    public void setModel(Board model) {
        this.model = model;
    }

    //reads player, checks if it's his turn, call performMove
    private void checkInput(Message message){
        Player player = model.getPlayerByIndex(message.getPlayerID());
        if (!model.getRound().isYourTurn(player)) model.notify(new TextResponse(message.getPlayerID(),"It's not your turn"));
        else message.handle(this);
    }

    @Override
    public void performMove(SetupMessage setupMessage){
        notify(setupMessage);
    }

    //use a toolcards
    @Override
    public void performMove(ToolCardMessage toolCardMessage) {
        Player player = model.getPlayerByIndex(toolCardMessage.getPlayerID());
        try {
            ToolCard toolCard = model.getToolCards()[player.getCardInUse()];
            Response response = toolCard.handle(toolCardController,toolCardMessage);
            model.notify(response);
        }
        catch (ToolCardException e) {model.notify(new TextResponse(toolCardMessage.getPlayerID(),e.getMessage()));}
    }

    @Override
    public void performMove(InputMessage inputMessage) {
        Player player = model.getPlayerByIndex(inputMessage.getPlayerID());
        try {
            player.getDieInHand().setValue(inputMessage.getDieValue());
            model.notify(new ModelViewResponse(model.modelViewCopy()));
        } catch (DieException e) {
            model.notify(new TextResponse(inputMessage.getPlayerID(), e.getMessage()));
        }
    }

    @Override
    public void performMove(ToolCardRequestMessage toolCardRequestMessage) {
        Player player = model.getPlayerByIndex(toolCardRequestMessage.getPlayerID());
        if(player.hasUsedCard()) model.notify(new TextResponse(toolCardRequestMessage.getPlayerID(),"You have already used a Tool Card"));
        else {
            int cost = model.getToolCardsUsage()[toolCardRequestMessage.getToolCardNumber()]? 2:1;
            if(player.getFavorPoints()<cost) model.notify(new TextResponse(toolCardRequestMessage.getPlayerID(),"Not enough favor points"));
            else {
                player.setCardInUse(toolCardRequestMessage.getToolCardNumber());
                model.notify(new ToolCardResponse(toolCardRequestMessage.getPlayerID(),toolCardRequestMessage.getToolCardNumber()));
            }
        }
    }

    //place a die
    @Override
    public void performMove(PlaceMessage placeMessage) {
        Player player = model.getPlayerByIndex(placeMessage.getPlayerID());
        if(!player.hasDieInHand()) model.notify(new TextResponse(placeMessage.getPlayerID(),"You haven't selected a die"));
        else {
            try {
                Die die = player.getDieInHand();
                if(player.isFirstMove()) {
                    placeDie(new DiePlacerFirst(die,placeMessage.getFinalPosition(),player.getWindow()));
                    player.setFirstMove(true);
                }
                else {
                    placeDie(new DiePlacerNormal(die,placeMessage.getFinalPosition(),player.getWindow()));
                }
                player.dropDieInHand();
                model.notify(new ModelViewResponse(model.modelViewCopy()));
            }
            catch(InvalidPlacementException e) {model.notify(new TextResponse(placeMessage.getPlayerID(),"You can't place the die there"));}
        }
    }

    //draft a die
    @Override
    public void performMove(DraftMessage draftMessage) {
        Player player = model.getPlayerByIndex(draftMessage.getPlayerID());
        if (player.hasDraftedDie()) model.notify(new TextResponse(draftMessage.getPlayerID(),"You have already drafted"));
        else {
            try {
                draft(draftMessage);
                model.notify(new ModelViewResponse(model.modelViewCopy()));
            } catch (NoDieException e) {
                model.notify(new TextResponse(draftMessage.getPlayerID(),"The die you want to draft does not exit"));
            }
        }
    }

    //pass
    @Override
    public void performMove(PassMessage passMessage) {
        Player player = model.getPlayerByIndex(passMessage.getPlayerID());
        if (model.getRound().isLastTurn()) {
            this.endRound();
        }
        else {
            pass(player);
            int nextPlayerID = model.getRound().getCurrentPlayerIndex();
            model.notify(new TurnStartResponse(nextPlayerID));
            model.notify(new ModelViewResponse(model.modelViewCopy()));
        }
    }

    private void endRound() {
        if (model.getRound().getRoundNumber() == Board.ROUNDSNUMBER) {
            this.endMatch();
        }
        else {
            startRound();
            int nextPlayerID = model.getRound().getCurrentPlayerIndex();
            model.notify(new ModelViewResponse(model.modelViewCopy()));
            model.notify(new TurnStartResponse(nextPlayerID));
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
            score-=player.getWindow().countEmptySlots();
            player.setScore(score);
        }
    }

    private void startRound() {
        model.setRound(model.getRound().changeRound());
        model.getRoundTracker().updateRoundTracker(model.getDraftPool().getAllDice());
        model.getDraftPool().fillDraftPool(model.getBag().drawDice(model.getPlayersNumber()));
    }

    private void endMatch() {
        model.notify(new ModelViewResponse(model.modelViewCopy()));
        evaluatePoints();
    }

    private void draft(DraftMessage draftMessage) throws NoDieException {
        Player player = model.getPlayerByIndex(draftMessage.getPlayerID());
        Die die = model.getDraftPool().getDie(draftMessage.getDraftPoolPosition());
        model.getDraftPool().removeFromDraftPool(die);
        player.setDieInHand(die);
        player.setHasDraftedDie(true);
    }

    private void pass(Player player) {
        model.getRound().changeTurn();
        player.setHasUsedCard(false);
        player.setHasDraftedDie(false);
        if (player.hasDieInHand()) {
            Die die = player.getDieInHand();
            player.dropDieInHand();
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
