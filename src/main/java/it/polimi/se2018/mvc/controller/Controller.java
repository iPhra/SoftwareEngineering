package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.mvc.controller.placementlogic.DiePlacer;
import it.polimi.se2018.mvc.controller.placementlogic.DiePlacerFirst;
import it.polimi.se2018.mvc.view.ServerView;
import it.polimi.se2018.network.messages.responses.EndGameResponse;
import it.polimi.se2018.network.messages.responses.TimeUpResponse;
import it.polimi.se2018.network.messages.responses.sync.*;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.utils.exceptions.*;
import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.network.messages.requests.*;
import it.polimi.se2018.mvc.controller.placementlogic.DiePlacerNormal;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
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
    private WaitingThread clock;
    private Duration timeout;

    public Controller(GameManager gameManager, ServerView view) {
        super();
        this.view = view;
        this.gameManager = gameManager;
        lock = new ReentrantLock();
        getDuration();
    }

    private void getDuration() {
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("TimerProperties.txt");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String[] tokens = sb.toString().split(";");
            timeout = Duration.ofSeconds(Integer.parseInt(tokens[2].split(":")[1]));
        }
        catch (IOException e) {
            timeout = Duration.ofSeconds(200);
        }
    }

    private void startTimer() {
        clock = new WaitingThread(timeout, this);
        clock.start();
    }

    private void endRound(Player player,boolean stopped) {
        model.setRound(model.getRound().changeRound());
        model.getRoundTracker().updateRoundTracker(model.getDraftPool().getAllDice());
        model.getDraftPool().fillDraftPool(model.getBag().drawDice(model.getPlayersNumber()));
        player.setHasUsedCard(false);
        player.setHasDraftedDie(false);
        model.incrementStateID();
        int round = model.getRound().getRoundNumber() - 1;
        if(stopped) view.handleNetworkOutput(new TimeUpResponse(player.getId()));
        model.createModelUpdateResponse(PLAYER + player.getName() + " passed the turn. Round " + round + " ends");
        startTimer();
    }

    private void endTurn(Player player, boolean stopped) {
        model.getRound().changeTurn();
        player.setHasUsedCard(false);
        player.setHasDraftedDie(false);
        if (player.hasDieInHand()) {
            Die die = player.getDieInHand();
            player.dropDieInHand();
            model.getDraftPool().addToDraftPool(die);
        }
        model.incrementStateID();
        if(stopped) view.handleNetworkOutput(new TimeUpResponse(player.getId()));
        model.createDraftPoolResponse(PLAYER + player.getName() + " passed the turn.");
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
        if(gameManager.isMatchStarted()) {
            Player player = model.getPlayerByID(message.getPlayerID());
            if (!(model.getRound().isYourTurn(player) || message.getStateID() == model.getStateID()))
                view.handleNetworkOutput(new TextResponse(message.getPlayerID(), "It's not your turn"));
            else message.handle(this);
        }
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
    void endMatch(boolean lastPlayer, boolean playerPlaying, int lastPlayerID) {
        EndGameResponse endGameResponse = null;
        if(lastPlayer) {
            endGameResponse = new EndGameResponse(lastPlayerID);
            endGameResponse.setScoreBoardResponse(new ArrayList<>(),true);
            endGameResponse.setPlayerPlaying(playerPlaying || model.getRound().getCurrentPlayerID()==lastPlayerID);
        }
        else {
            for(Player player : model.getPlayers()) {
                endGameResponse = new EndGameResponse(player.getId());
                List<Player> scoreBoard = playersScoreBoard();
                endGameResponse.setScoreBoardResponse(scoreBoard,false);
                endGameResponse.setPlayerPlaying(false);
            }
        }
        view.handleNetworkOutput(endGameResponse);
        gameManager.endGame();
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
            model.createModelUpdateResponse(PLAYER + player.getName() + " used Flux Remover: \nhe/she moved the drafted die to the bag and received " + player.getDieInHand().getValue()+ " " + player.getDieInHand().getColor()+"\n");
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
            view.handleNetworkOutput(new TextResponse(toolCardMessage.getPlayerID(),e.getMessage()));
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
                model.createWindowResponse(PLAYER + player.getName() + " placed the drafted die in " + placeMessage.getFinalPosition().getDescription(),player.getId());
            }
            catch(InvalidPlacementException e) {
                view.handleNetworkOutput(new TextResponse(placeMessage.getPlayerID(),"You can't place the die there"));}
        }
        lock.unlock();
    }

    @Override
    public void handleMove(DraftMessage draftMessage) {
        lock.lock();
        Player player = model.getPlayerByID(draftMessage.getPlayerID());
        if (player.hasDraftedDie()) model.notify(new TextResponse(draftMessage.getPlayerID(),"You have already drafted"));
        else {
            try {
                draft(draftMessage);
                Die die= model.getPlayerByID(model.getRound().getCurrentPlayerID()).getDieInHand();
                model.createDraftPoolResponse(PLAYER + player.getName() + " drafted the die " + die.getValue() + " " + die.getColor());
            } catch (NoDieException e) {
                view.handleNetworkOutput(new TextResponse(draftMessage.getPlayerID(),"The die you want to draft does not exit"));
            }
        }
        lock.unlock();
    }

    @Override
    public void handleMove(PassMessage passMessage) {
        lock.lock();
        clock.interrupt();
        Player player = model.getPlayerByID(passMessage.getPlayerID());
        if (model.getRound().getRoundNumber()!= Board.ROUNDSNUMBER && model.getRound().isLastTurn()) {
            endRound(player,passMessage.isHalt());
        }
        else if (model.getRound().getRoundNumber() == Board.ROUNDSNUMBER && model.getRound().isLastTurn()) {
            endMatch(false, false,0);
        }
        else {
            endTurn(player,passMessage.isHalt());
        }
        lock.unlock();
    }

    public void startMatch() {
        toolCardController = new ToolCardController(model);
        model.incrementStateID();
        StringBuilder description = new StringBuilder();
        description.append("List of player is: ");
        for(Player player : model.getPlayers()) {
            description.append(player.getName());
            description.append(" ");
        }
        description.append("\n\n");
        model.createModelViews(description.toString());
        startTimer();
    }

    @Override
    public void update(Message input) {
        checkInput(input);
    }

    @Override
    public void halt(String message) {
        lock.lock();
        handleMove(new PassMessage(model.getRound().getCurrentPlayerID(),model.getStateID(),true));
        lock.unlock();
    }
}
