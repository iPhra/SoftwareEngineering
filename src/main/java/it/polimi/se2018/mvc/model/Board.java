package it.polimi.se2018.mvc.model;

import it.polimi.se2018.mvc.controller.ModelView;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.network.messages.responses.sync.modelupdates.ModelViewResponse;
import it.polimi.se2018.network.messages.responses.sync.modelupdates.DraftPoolResponse;
import it.polimi.se2018.network.messages.responses.sync.modelupdates.ModelUpdateResponse;
import it.polimi.se2018.network.messages.responses.sync.modelupdates.RoundTrackerResponse;
import it.polimi.se2018.network.messages.responses.sync.modelupdates.WindowResponse;
import it.polimi.se2018.utils.Observable;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the board, that is considered the ClientData in MVC
 */
public class Board extends Observable<Response> {

    /**
     * This is the number of colors in the game, 5 in our instance
     */
    private static final int COLORSNUMBER = 5;

    /**
     * This is the number of dice in the game, 90 in our instance
     */
    private static final int DICENUMBER = 90;

    /**
     * This is the number of rounds in one game
     */
    public static final int ROUNDSNUMBER = 10;

    /**
     * This is the number of tool cards in one game
     */
    private static final int TOOLCARDSNUMBER = 3;
    private Round round;
    private final int playersNumber;
    private final List<Player> players;
    private final DraftPool draftPool;

    /**
     * This attribute is used to save, for each tool card, the information if it was used
     */
    private final boolean[] toolCardsUsage;
    private ToolCard[] toolCards;
    private final PublicObjective[] publicObjectives;
    private final Bag bag;
    private final RoundTracker roundTracker;
    private int stateID;

    public Board(List<Player> players, ToolCard[] toolCards, PublicObjective[] publicObjectives) {
        this.players = players;
        this.playersNumber=players.size();
        ArrayList<Integer> playersId = new ArrayList<>(playersNumber);
        for (Player player : players) {
            playersId.add(player.getId());
        }
        round = new Round(playersId, 1);
        toolCardsUsage = new boolean[TOOLCARDSNUMBER];
        for(int i=0; i<TOOLCARDSNUMBER; i++) {
            toolCardsUsage[i]=false;
        }
        this.toolCards=toolCards;
        this.publicObjectives = publicObjectives;
        bag = new Bag(COLORSNUMBER, DICENUMBER);
        roundTracker = new RoundTracker(ROUNDSNUMBER);
        draftPool = new DraftPool();
        draftPool.fillDraftPool(bag.drawDice(playersNumber));
        stateID = 0;
    }

    public void incrementStateID() {
        stateID++;
    }

    public int getStateID() {
        return stateID;
    }

    public DraftPool getDraftPool() {
        return draftPool;
    }

    public Round getRound() {
        return round;
    }

    public Bag getBag() {
        return bag;
    }

    public RoundTracker getRoundTracker() {
        return roundTracker;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public ToolCard[] getToolCards() {
        return toolCards;
    }

    public PublicObjective[] getPublicObjectives() {
        return publicObjectives;
    }

    public int getPlayersNumber() {return playersNumber;}

    public void setRound(Round round) {
        this.round=round;
    }

    public void setToolCards(ToolCard[] toolCards) {
        this.toolCards = toolCards;
    }

    /**
     * @param id it's the ID of the player that user wants to get
     * @return the player with the given ID, if exists
     * @throws InvalidParameterException if there is no player with that ID
     */
    public Player getPlayerByID(int id){
        for(Player player: players) {
            if(player.getId()==id) return player;
        }
        throw new InvalidParameterException();
    }

    public boolean[] getToolCardsUsage() {
        return toolCardsUsage;
    }

    /**
     * Sets as true the usage of a tool card, it's called when it was used
     * @param index it's the index of the tool card that has to be setted as already used
     */
    public void setAlreadyUsed(int index) {
        toolCardsUsage[index]=true;
    }

    /**
     * Creates and sends a model view response to each player
     * Used when the game begins, when a player reconnects or when a round ends
     * @param description is the string to set as description of the message
     */
    public void createModelViews(String description) {
        for(Player player: players) {
            ModelViewResponse response = new ModelViewResponse(this,new ModelView(this),player.getId());
            response.setDescription(description);
            notify(response);
        }
    }

    /**
     * Creates and sends a draft pool response to each player
     * Used when the {@link DraftPool} is updated
     * @param description is the string to set as description of the message
     */
    public void createDraftPoolResponse(String description) {
        for(Player player: players) {
            DraftPoolResponse response = new DraftPoolResponse(player.getId(),this);
            response.setDescription(description);
            notify(response);
        }
    }

    /**
     * Creates and sends a round tracker response to each player
     * Used when the {@link RoundTracker} is updated
     * @param description is the string to set as description of the message
     */
    public void createRoundTrackerResponse(String description) {
        for(Player player: players) {
            RoundTrackerResponse response = new RoundTrackerResponse(player.getId(),this);
            response.setDescription(description);
            notify(response);
        }
    }

    /**
     * Creates and sends a window response to each player
     * Used when a {@link Window} is updated
     * @param description is the string to set as description of the message
     * @param playerWindowID is the ID of the player whose window is being updated
     */
    public void createWindowResponse(String description, int playerWindowID) {
        for(Player player: players) {
            WindowResponse response = new WindowResponse(player.getId(),this,playerWindowID);
            response.setDescription(description);
            notify(response);
        }
    }

    /**
     * Creates and sends a model update to each player
     * Used when the state of the game is updated (i.e. die in hand, tool card usage)
     * @param description is the string to set as description of the message
     */
    public void createModelUpdateResponse(String description) {
        for(Player player: players) {
            ModelUpdateResponse response = new ModelUpdateResponse(player.getId(),stateID,this);
            response.setDescription(description);
            notify(response);
        }
    }
}
