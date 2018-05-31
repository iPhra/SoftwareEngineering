package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.mvc.controller.placementlogic.DiePlacer;
import it.polimi.se2018.mvc.controller.placementlogic.DiePlacerFirst;
import it.polimi.se2018.mvc.view.ServerView;
import it.polimi.se2018.network.messages.responses.TurnEndResponse;
import it.polimi.se2018.network.messages.responses.sync.*;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.utils.exceptions.*;
import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.network.messages.requests.*;
import it.polimi.se2018.mvc.controller.placementlogic.DiePlacerNormal;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Controller implements Observer<Message>, MessageHandler, Stopper {
    private static final String PLAYER = "Player ";
    private Board model;
    private final ServerView view;
    private ToolCardController toolCardController;
    private final GameManager gameManager;
    private final ReentrantLock lock;

    public Controller(GameManager gameManager, ServerView view) {
        super();
        this.view = view;
        this.gameManager = gameManager;
        lock = new ReentrantLock();
    }

    private void startTimer() {
        Duration timeout = Duration.ofSeconds(6000);
        WaitingThread alarm = new WaitingThread(timeout, this);
        alarm.start();
    }

    private void endRound(Player player) {
        model.setRound(model.getRound().changeRound());
        model.getRoundTracker().updateRoundTracker(model.getDraftPool().getAllDice());
        model.getDraftPool().fillDraftPool(model.getBag().drawDice(model.getPlayersNumber()));
        player.setHasUsedCard(false);
        player.setHasDraftedDie(false);
        model.incrementStateID();
        int round = model.getRound().getRoundNumber() - 1;
        view.handleNetworkOutput(new TurnEndResponse(player.getId()));
        createModelViews(PLAYER + player.getName() + " passed the turn. Round " + round + " ends");
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
        view.handleNetworkOutput(new TurnEndResponse(player.getId()));
        createModelViews(PLAYER + player.getName() + " passed the turn.");

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

    //reads player, checks if it's his turn, call handleMove
    private void checkInput(Message message){
        lock.lock();
        Player player = model.getPlayerByID(message.getPlayerID());
        if (!(model.getRound().isYourTurn(player) || message.getStateID()==model.getStateID()))
            view.handleNetworkOutput(new TextResponse(message.getPlayerID(),"It's not your turn"));
        else message.handle(this);
        lock.unlock();
    }

    private List<Player> playersScoreBoard(){
        List<Player> sortedPlayers = model.getPlayers();
        sortedPlayers.sort(new ScoreComparator(Arrays.asList(model.getPublicObjectives()), model.getRound()));
        Collections.reverse(sortedPlayers);
        return sortedPlayers;
    }

    //called by endTurn method when match ends
    void endMatch() {
        List<Player> scoreBoard = playersScoreBoard();
        for(Player player : model.getPlayers())
            view.handleNetworkOutput(new ScoreBoardResponse(player.getId(),scoreBoard));
        gameManager.endGame();
    }

    void createModelViews(String description) {
        for(Player player: model.getPlayers()) {
            ModelViewResponse modelViewResponse = new ModelViewResponse(model.modelViewCopy(),player.getId());
            modelViewResponse.setDescription(description);
            model.notify(modelViewResponse);
        }
    }

    void setModel(Board model) {
        this.model = model;
    }

    @Override
    public void handleMove(SetupMessage setupMessage){
        lock.lock();
        gameManager.createPlayer(setupMessage);
        lock.unlock();
    }

    @Override
    public void handleMove(InputMessage inputMessage) {
        lock.lock();
        Player player = model.getPlayerByID(inputMessage.getPlayerID());
        try {
            player.getDieInHand().setValue(inputMessage.getDieValue());
            createModelViews(PLAYER + player.getName() + " used Flux Remover: \nhe/she moved the drafted die to the bag and received " + player.getDieInHand().getValue()+ " " + player.getDieInHand().getColor());
        } catch (DieException e) {
            view.handleNetworkOutput(new TextResponse(inputMessage.getPlayerID(), e.getMessage()));
        }
        finally {lock.unlock();}
    }

    //use a toolcards
    @Override
    public void handleMove(ToolCardMessage toolCardMessage) {
        lock.lock();
        Player player = model.getPlayerByID(toolCardMessage.getPlayerID());
        try {
            model.getToolCards()[player.getCardInUse()].handle(toolCardController,toolCardMessage);
        }
        catch (ToolCardException e) {
            model.notify(new TextResponse(toolCardMessage.getPlayerID(),e.getMessage()));
        }
        finally {
            lock.unlock();
        }
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
                view.handleNetworkOutput(new ToolCardResponse(toolCardRequestMessage.getPlayerID(), toolCardRequestMessage.getToolCardNumber()));
            }
            else view.handleNetworkOutput(new TextResponse(toolCardRequestMessage.getPlayerID(),"You can't use that Tool Card!"));
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
                createModelViews(PLAYER + player.getName() + " placed the drafted die in " + placeMessage.getFinalPosition().getDescription());
            }
            catch(InvalidPlacementException e) {
                view.handleNetworkOutput(new TextResponse(placeMessage.getPlayerID(),"You can't place the die there"));}
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
                Die die= model.getPlayerByID(model.getRound().getCurrentPlayerID()).getDieInHand();
                createModelViews(PLAYER + player.getName() + " drafted the die " + die.getValue() + " " + die.getColor());
            } catch (NoDieException e) {
                view.handleNetworkOutput(new TextResponse(draftMessage.getPlayerID(),"The die you want to draft does not exit"));
            }
        }
        lock.unlock();
    }

    //endTurn
    @Override
    public void handleMove(PassMessage passMessage) {
        lock.lock();
        Player player = model.getPlayerByID(passMessage.getPlayerID());
        if (model.getRound().getRoundNumber()!= Board.ROUNDSNUMBER && model.getRound().isLastTurn()) {
            endRound(player);
        }
        else if (model.getRound().getRoundNumber() == Board.ROUNDSNUMBER && model.getRound().isLastTurn()) {
            endMatch();
        }
        else {
            endTurn(player);
        }
        lock.unlock();
    }

    public void startMatch() {
        toolCardController = new ToolCardController(model,this);
        model.incrementStateID();
        StringBuilder description = new StringBuilder();
        description.append("List of player is: ");
        for(Player player : model.getPlayers()) {
            description.append(player.getName());
            description.append(" ");
        }
        description.append("\n\n");
        createModelViews(description.toString());
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
    public void halt(String message) {
        lock.lock();
        handleMove(new PassMessage(model.getRound().getCurrentPlayerID(),model.getStateID()));
        lock.unlock();
    }
}
