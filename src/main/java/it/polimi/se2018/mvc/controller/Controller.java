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

    /**
     * This lock is used to ensure that timer thread and the thread making moves don't interfer with each other
     */
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

    /**
     * This method reads the duration of the timer from a configuration file
     */
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

    /**
     * This method launches a thread that wakes up after the timer has ran out
     */
    private void startTimer() {
        clock = new WaitingThread(timeout, this);
        clock.start();
    }

    /**
     * This method notify an error to a specific player in the game
     * @param playerID id of the player that has to receive the message
     * @param message is the message of error that is generated during an operation
     */
    private void notifyError(int playerID, String message) {
        TextResponse response = new TextResponse(playerID);
        response.setDescription(message);
        view.handleNetworkOutput(response);
    }

    /**
     * This method is called when the turn of a player end
     * @param player is the player of whom the turn is ended
     * @param backToDraftPool to understand if the die in hand of the player has to return to the draftpool or not
     */
    private void refreshPlayer(Player player, boolean backToDraftPool) {
        player.setHasUsedCard(false);
        player.setHasDraftedDie(false);
        if (player.hasDieInHand()) {
            Die die = player.getDieInHand();
            player.dropDieInHand();
            if(backToDraftPool) model.getDraftPool().addToDraftPool(die);
        }
        model.incrementStateID();
    }

    /**
     * This method is called when match end. It generate and send a endGameResponse to the players
     * @param passingPlayer the reference to the last player
     * @param timeout if timeout is occured controller has to notify it to the current player
     */
    private void endMatch(Player passingPlayer, boolean timeout) {
        gameManager.setMatchPlaying(false);
        if(timeout) view.handleNetworkOutput(new TimeUpResponse(passingPlayer.getId()));
        for(Player player : model.getPlayers()) {
            EndGameResponse endGameResponse = new EndGameResponse(player.getId());
            List<Player> scoreBoard = playersScoreBoard();
            endGameResponse.setScoreBoardResponse(scoreBoard,false);
            endGameResponse.setPlayerPlaying(false);
            view.handleNetworkOutput(endGameResponse);
        }
        gameManager.endGame();
    }

    /**
     * This method is called when round end. It end the turn and generate a new round or end the game
     * @param player is the player who passed the turn
     * @param timeout is a boolean to understand if tinmeout is occured. If it ends controller has to notify it to the current player
     */
    private void endRound(Player player, boolean timeout) {
        model.setRound(model.getRound().changeRound());
        model.getRoundTracker().updateRoundTracker(model.getDraftPool().getAllDice());
        model.getDraftPool().fillDraftPool(model.getBag().drawDice(model.getPlayersNumber()));
        refreshPlayer(player,false);
        int round = model.getRound().getRoundNumber() - 1;
        if(timeout) view.handleNetworkOutput(new TimeUpResponse(player.getId()));
        model.createModelViews(PLAYER + player.getName() + " passed the turn. Round "+round+ " ends. It's "+ model.getPlayerByID(model.getRound().getCurrentPlayerID()).getName()+"'s turn.");
        startTimer();
    }

    /**
     * This method is called when turn end. It end the turn and go to the next one
     * @param player is the player who turn is ended
     * @param timeout is a boolean to understand if timeout is occured. If it endws controller has to notify it to the current player
     */
    private void endTurn(Player player, boolean timeout) {
        model.getRound().changeTurn();
        refreshPlayer(player,true);
        if(timeout) view.handleNetworkOutput(new TimeUpResponse(player.getId()));
        model.createDraftPoolResponse(PLAYER + player.getName() + " passed the turn.\nIt's "+ model.getPlayerByID(model.getRound().getCurrentPlayerID()).getName()+"'s turn.");
        startTimer();
    }

    /**
     * This method handle a draft message from the player. Check if the player could draft and call the method of the model to draft the choosen die
     * @param draftMessage is the message comes from the player
     * @throws NoDieException if the position of the draftpool choosen by the player don't have a die
     */
    private void draft(DraftMessage draftMessage) throws NoDieException {
        Player player = model.getPlayerByID(draftMessage.getPlayerID());
        Die die = model.getDraftPool().getDie(draftMessage.getDraftPoolPosition());
        model.getDraftPool().removeFromDraftPool(die);
        player.setDieInHand(die);
        player.setHasDraftedDie(true);
    }

    /**
     * This method place the die
     * @param diePlacer is the placer used to place the die
     * @throws InvalidPlacementException if the die can not go to the choosen position
     */
    private void placeDie(DiePlacer diePlacer) throws InvalidPlacementException {
        diePlacer.placeDie();
    }

    /**
     * Check is the message receive is legit. If it's the turn of the player who give the message and the message it is of the current turn
     * @param message is the message received from the client
     */
    private void checkInput(Message message){
        if(gameManager.isMatchPlaying()) {
            Player player = model.getPlayerByID(message.getPlayerID());
            if (!model.getRound().isYourTurn(player) || message.getStateID() != model.getStateID()) notifyError(message.getPlayerID(), "It's not your turn!");
            else message.handle(this);
        }
        else message.handle(this); //if this is a setup response i don't have to check if the turn is correct
    }

    /**
     * Generate the final standing of players according to public and private objective and other points
     * @return a ordered list of players based on their scores
     */
    private List<Player> playersScoreBoard(){
        List<Player> sortedPlayers = new ArrayList<>(model.getPlayers());
        sortedPlayers.sort(new ScoreComparator(Arrays.asList(model.getPublicObjectives()), model.getRound()));
        Collections.reverse(sortedPlayers);
        return sortedPlayers;
    }

    void setModel(Board model) {
        this.model = model;
    }

    /**
     * Yhis method is called when a player remains alone in a game
     * @param windowSelection is true if the player remains alone during the selection of window
     * @param lastPlayerID is the ID of the player in game
     */
    void endMatchAsLast(boolean windowSelection, int lastPlayerID) {
        gameManager.setMatchPlaying(false);
        EndGameResponse endGameResponse = new EndGameResponse(lastPlayerID);
        endGameResponse.setScoreBoardResponse(new ArrayList<>(),true);
        endGameResponse.setPlayerPlaying(windowSelection || model.getRound().getCurrentPlayerID()==lastPlayerID); //true if it's the turn of the last player remaining, or if he was in window selection
        view.handleNetworkOutput(endGameResponse);
        gameManager.endGame();
    }

    /**
     * This method starts the match, it's called by {@link GameManager}
     */
    public void startMatch() {
        toolCardController = new ToolCardController(model);
        model.incrementStateID();
        StringBuilder description = new StringBuilder();
        description.append("List of player is: ");
        for(Player player : model.getPlayers()) {
            description.append(player.getName());
            if(model.getPlayers().indexOf(player)!=model.getPlayers().size()-1) description.append(", ");
        }
        description.append("\n\n");
        model.createModelViews(description.toString());
        startTimer();
    }

    @Override
    public void handleMove(SetupMessage setupMessage){
        gameManager.createPlayer(setupMessage);
    }

    @Override
    public void handleMove(InputMessage inputMessage) {
        Player player = model.getPlayerByID(inputMessage.getPlayerID());
        try {
            player.getDieInHand().setValue(inputMessage.getDieValue());
            model.createModelUpdateResponse(PLAYER + player.getName() + " used Flux Remover: \nhe/she moved the drafted die to the bag and received " + player.getDieInHand().getValue()+ " " + player.getDieInHand().getColor()+"\n");
        } catch (DieException e) {
            notifyError(inputMessage.getPlayerID(), e.getMessage());
        }
    }

    //use a toolcard
    @Override
    public void handleMove(ToolCardMessage toolCardMessage) {
        Player player = model.getPlayerByID(toolCardMessage.getPlayerID());
        try {
            model.getToolCards()[player.getCardInUse()].handle(toolCardController,toolCardMessage);
        }
        catch (ToolCardException e) {
            player.dropCardInUse();
            notifyError(toolCardMessage.getPlayerID(), e.getMessage());
        }
    }

    @Override
    public void handleMove(ToolCardRequestMessage toolCardRequestMessage) {
        Player player = model.getPlayerByID(toolCardRequestMessage.getPlayerID());
        if(player.hasUsedCard()) notifyError(toolCardRequestMessage.getPlayerID(), "You have already used a Tool Card!");
        else {
            ToolCard toolCard = model.getToolCards()[toolCardRequestMessage.getToolCardNumber()];
            if (toolCard.handleCheck(new ToolCardChecker(model), model.getToolCardsUsage()[toolCardRequestMessage.getToolCardNumber()], player)) {
                player.setCardInUse(toolCardRequestMessage.getToolCardNumber());
                view.handleNetworkOutput(new ToolCardResponse(toolCardRequestMessage.getPlayerID(), toolCardRequestMessage.getToolCardNumber()));
            }
            else notifyError(toolCardRequestMessage.getPlayerID(), "You can't use that Tool Card!");
        }
    }

    @Override
    public void handleMove(PlaceMessage placeMessage) {
        Player player = model.getPlayerByID(placeMessage.getPlayerID());
        if(!player.hasDieInHand()) notifyError(placeMessage.getPlayerID(), "You haven't selected a die!");
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
                notifyError(placeMessage.getPlayerID(), "You can't placed the die there!");
            }
        }
    }

    @Override
    public void handleMove(DraftMessage draftMessage) {
        Player player = model.getPlayerByID(draftMessage.getPlayerID());
        if (player.hasDraftedDie()) notifyError(draftMessage.getPlayerID(), "You have already drafted!");
        else {
            try {
                draft(draftMessage);
                Die die= model.getPlayerByID(model.getRound().getCurrentPlayerID()).getDieInHand();
                model.createDraftPoolResponse(PLAYER + player.getName() + " drafted a " + die.getValue() + " " + die.getColor());
            } catch (NoDieException e) {
                notifyError(draftMessage.getPlayerID(), "The die you want to draft does not exist!");
            }
        }
    }

    @Override
    public void handleMove(PassMessage passMessage) {
        clock.interrupt();
        Player player = model.getPlayerByID(passMessage.getPlayerID());
        if (model.getRound().getRoundNumber()!= Board.ROUNDSNUMBER && model.getRound().isLastTurn()) {
            endRound(player,passMessage.isHalt());
        }
        else if (model.getRound().getRoundNumber() == Board.ROUNDSNUMBER && model.getRound().isLastTurn()) {
            endMatch(player,passMessage.isHalt());
        }
        else {
            endTurn(player,passMessage.isHalt());
        }
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
