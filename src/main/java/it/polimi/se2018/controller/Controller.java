package it.polimi.se2018.controller;

import it.polimi.se2018.network.messages.responses.*;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.utils.exceptions.*;
import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.network.messages.requests.*;
import it.polimi.se2018.controller.placementlogic.DiePlacer;
import it.polimi.se2018.controller.placementlogic.DiePlacerFirst;
import it.polimi.se2018.controller.placementlogic.DiePlacerNormal;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.toolcards.ToolCard;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Controller implements Observer<Message>, MessageHandler, Timing {
    private Board model;
    private ToolCardController toolCardController;
    private GameManager gameManager;
    private ReentrantLock lock;

    public Controller(GameManager gameManager) {
        super();
        this.gameManager = gameManager;
        lock = new ReentrantLock();
    }

    private void startTimer() {
        Duration timeout = Duration.ofSeconds(6000);
        WaitingThread alarm = new WaitingThread(timeout, this);
        alarm.start();
    }

    public void setModel(Board model) {
        this.model = model;
    }

    //reads player, checks if it's his turn, call handleMove
    private void checkInput(Message message){
        lock.lock();
        Player player = model.getPlayerByID(message.getPlayerID());
        if (!(model.getRound().isYourTurn(player) || message.getStateID()==model.getStateID()))
            model.notify(new TextResponse(message.getPlayerID(),"It's not your turn"));
        else message.handle(this);
        lock.unlock();
    }

    @Override
    public void handleMove(SetupMessage setupMessage){
        lock.lock();
        gameManager.createPlayer(setupMessage);
        lock.unlock();
    }

    //use a toolcards
    @Override
    public void handleMove(ToolCardMessage toolCardMessage) {
        lock.lock();
        Player player = model.getPlayerByID(toolCardMessage.getPlayerID());
        try {
            ToolCard toolCard = model.getToolCards()[player.getCardInUse()];
            Response response = toolCard.handle(toolCardController,toolCardMessage);
            model.notify(response);
        }
        catch (ToolCardException e) {model.notify(new TextResponse(toolCardMessage.getPlayerID(),e.getMessage()));}
        finally {lock.unlock();}
    }

    @Override
    public void handleMove(InputMessage inputMessage) {
        lock.lock();
        Player player = model.getPlayerByID(inputMessage.getPlayerID());
        try {
            player.getDieInHand().setValue(inputMessage.getDieValue());
            ModelViewResponse modelViewResponse = new ModelViewResponse(model.modelViewCopy());
            modelViewResponse.setDescription("Player " + player.getName() + " used Flux Remover: \nhe/she moved the die drafted to the bag and received the die " + player.getDieInHand().getValue()+ " " + player.getDieInHand().getColor());
            model.notify(modelViewResponse);
        } catch (DieException e) {
            model.notify(new TextResponse(inputMessage.getPlayerID(), e.getMessage()));
        }
        finally {lock.unlock();}
    }

    @Override
    public void handleMove(ToolCardRequestMessage toolCardRequestMessage) {
        lock.lock();
        Player player = model.getPlayerByID(toolCardRequestMessage.getPlayerID());
        if(player.hasUsedCard()) model.notify(new TextResponse(toolCardRequestMessage.getPlayerID(),"You have already used a Tool Card"));
        else {
            ToolCard toolCard = model.getToolCards()[toolCardRequestMessage.getToolCardNumber()];
            if (toolCard.handleCheck(new ToolCardChecker(model), model.getToolCardsUsage()[toolCardRequestMessage.getToolCardNumber()], player)) {
                player.setCardInUse(toolCardRequestMessage.getToolCardNumber());
                model.notify(new ToolCardResponse(toolCardRequestMessage.getPlayerID(), toolCardRequestMessage.getToolCardNumber()));
            }
            else model.notify(new TextResponse(toolCardRequestMessage.getPlayerID(),"You can't use that Tool Card!"));
        }
        lock.unlock();
    }

    //place a die
    @Override
    public void handleMove(PlaceMessage placeMessage) {
        lock.lock();
        Player player = model.getPlayerByID(placeMessage.getPlayerID());
        if(!player.hasDieInHand()) model.notify(new TextResponse(placeMessage.getPlayerID(),"You haven't selected a die"));
        else {
            try {
                Die die = player.getDieInHand();
                if(player.isFirstMove()) {
                    placeDie(new DiePlacerFirst(die,placeMessage.getFinalPosition(),player.getWindow()));
                    player.setFirstMove(false);
                }
                else {
                    placeDie(new DiePlacerNormal(die,placeMessage.getFinalPosition(),player.getWindow()));
                }
                player.dropDieInHand();
                ModelViewResponse modelViewResponse = new ModelViewResponse(model.modelViewCopy());
                modelViewResponse.setDescription("Player " + player.getName() + " placed the drafted die in " + placeMessage.getFinalPosition().getDescription());
                model.notify(modelViewResponse);
            }
            catch(InvalidPlacementException e) {model.notify(new TextResponse(placeMessage.getPlayerID(),"You can't place the die there"));}
        }
        lock.unlock();
    }

    //draft a die
    @Override
    public void handleMove(DraftMessage draftMessage) {
        lock.lock();
        Player player = model.getPlayerByID(draftMessage.getPlayerID());
        if (player.hasDraftedDie()) model.notify(new TextResponse(draftMessage.getPlayerID(),"You have already drafted"));
        else {
            try {
                draft(draftMessage);
                ModelViewResponse modelViewResponse = new ModelViewResponse(model.modelViewCopy());
                modelViewResponse.setDescription("Player " + player.getName() + " drafted the die " + modelViewResponse.getModelView().getDieInHand().getValue() + " " + modelViewResponse.getModelView().getDieInHand().getColor());
                model.notify(modelViewResponse);
            } catch (NoDieException e) {
                model.notify(new TextResponse(draftMessage.getPlayerID(),"The die you want to draft does not exit"));
            }
        }
        lock.unlock();
    }

    //endTurn
    @Override
    public void handleMove(PassMessage passMessage) {
        lock.lock();
        Player player = model.getPlayerByID(passMessage.getPlayerID());
        if (model.getRound().isLastTurn()) {
            this.endRound(player);
        }
        else if (model.getRound().getRoundNumber() == Board.ROUNDSNUMBER) {
            this.endMatch();
        }
        else {
            endTurn(player);
        }
        lock.unlock();
    }

    private List<Player> playersScoreBoard(){
        List<Player> sortedPlayers = model.getPlayers();
        Arrays.sort(sortedPlayers.toArray(new Player[0]), new ScoreComparator(Arrays.asList(model.getPublicObjectives()), model.getRound()));
        return sortedPlayers;
    }

    //called by endTurn method when match ends
    private void endMatch() {
        for(Player player : model.getPlayers())
            model.notify(new ScoreBoardResponse(player.getId(),playersScoreBoard()));
        gameManager.endGame();
    }

    private void endRound(Player player) {
        model.setRound(model.getRound().changeRound());
        model.getRoundTracker().updateRoundTracker(model.getDraftPool().getAllDice());
        model.getDraftPool().fillDraftPool(model.getBag().drawDice(model.getPlayersNumber()));
        player.setHasUsedCard(false);
        player.setHasDraftedDie(false);
        model.incrementStateID();
        ModelViewResponse modelViewResponse = new ModelViewResponse(model.modelViewCopy());
        int round = model.getRound().getRoundNumber() - 1;
        modelViewResponse.setDescription("Player " + player.getName() + " passed the turn. Round " + round + " ends");
        model.notify(modelViewResponse);
        startTimer();
    }

    private void endTurn(Player player) {
        model.getRound().changeTurn();
        player.setHasUsedCard(false);
        player.setHasDraftedDie(false);
        if (player.hasDieInHand()) {
            Die die = player.getDieInHand();
            player.dropDieInHand();
            model.getDraftPool().addToDraftPool(die);
        }
        model.incrementStateID();
        ModelViewResponse modelViewResponse = new ModelViewResponse(model.modelViewCopy());
        modelViewResponse.setDescription("Player " + player.getName() + " passed the turn.");
        model.notify(modelViewResponse);
        startTimer();
    }

    private void draft(DraftMessage draftMessage) throws NoDieException {
        Player player = model.getPlayerByID(draftMessage.getPlayerID());
        Die die = model.getDraftPool().getDie(draftMessage.getDraftPoolPosition());
        model.getDraftPool().removeFromDraftPool(die);
        player.setDieInHand(die);
        player.setHasDraftedDie(true);
    }

    private void placeDie(DiePlacer diePlacer) throws InvalidPlacementException {
        diePlacer.placeDie();
    }

    public void startMatch() {
        toolCardController = new ToolCardController(model);
        model.incrementStateID();
        model.notify(new ModelViewResponse(model.modelViewCopy()));
        startTimer();
    }

    @Override
    public void update(Message input) {
        if (input instanceof SetupMessage)
            input.handle(this);
        else
            checkInput(input);
    }

    @Override
    public void wakeUp() {
        lock.lock();
        handleMove(new PassMessage(model.getRound().getCurrentPlayerIndex(),model.getStateID()));
        lock.unlock();
    }
}
