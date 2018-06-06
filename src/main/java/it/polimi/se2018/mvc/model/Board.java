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
 * This class represents the board, that is considered the Model in the used pattern MVC
 * @author Emilio Imperiali
 */
public class Board extends Observable<Response> {
    /**
     * value of colors in the game, 5 in our instance
     */
    private static final int COLORSNUMBER = 5;

    /**
     * value of dice in the game, 90 in our instance
     */
    private static final int DICENUMBER = 90;

    /**
     * value of rounds in one game
     */
    public static final int ROUNDSNUMBER = 2;
    /**
     * value of tool cards in one game
     */
    private static final int TOOLCARDSNUMBER = 3;
    private Round round;
    private final int playersNumber;
    private final List<Player> players;
    private final DraftPool draftPool; //draft pool
    /**
     * This attribute is used to save, for each tool card, the information if it was used
     */
    private final boolean[] toolCardsUsage;
    private final ToolCard[] toolCards;
    private final PublicObjective[] publicObjectives; //array che contiene le carte degli obbiettivi pubblici
    private final Bag bag; //ha il riferimento al sacchetto dei dadi
    private final RoundTracker roundTracker; //ha il riferimento al roundTracker
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
        draftPool=new DraftPool();
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

    /**
     * Sets the new given round when it starts
     * @param round it's the new round
     */
    public void setRound(Round round) {
        this.round=round;
    }

    /**
     *
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


    public void createModelViews(String description) {
        for(Player player: players) {
            ModelViewResponse response = new ModelViewResponse(new ModelView(this),player.getId());
            response.setDescription(description);
            notify(response);
        }
    }

    public void createDraftPoolResponse(String description) {
        for(Player player: players) {
            DraftPoolResponse response = new DraftPoolResponse(player.getId(),this);
            response.setDescription(description);
            notify(response);
        }
    }

    public void createRoundTrackerResponse(String description) {
        for(Player player: players) {
            RoundTrackerResponse response = new RoundTrackerResponse(player.getId(),this);
            response.setDescription(description);
            notify(response);
        }
    }

    public void createWindowResponse(String description, int playerWindowID) {
        for(Player player: players) {
            WindowResponse response = new WindowResponse(player.getId(),this,playerWindowID);
            response.setDescription(description);
            notify(response);
        }
    }

    public void createModelUpdateResponse(String description) {
        for(Player player: players) {
            ModelUpdateResponse response = new ModelUpdateResponse(player.getId(),stateID,this);
            response.setDescription(description);
            notify(response);
        }
    }

    /**
     * @return this toolCardsUsage
     */
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
}
