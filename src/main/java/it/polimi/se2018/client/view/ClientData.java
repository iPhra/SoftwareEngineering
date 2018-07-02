package it.polimi.se2018.client.view;

import it.polimi.se2018.network.messages.responses.sync.modelupdates.ModelView;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;

import java.util.List;

public class ClientData {
    protected final int playerID;
    protected int playersNumber;
    protected List<String> playerNames;
    protected ModelView board;
    protected List<ToolCard> toolCards;
    protected PrivateObjective privateObjective;
    protected List<PublicObjective> publicObjectives;

    protected ClientData(int playerID) {
        this.playerID = playerID;
    }

    public List<PublicObjective> getPublicObjectives() {
        return publicObjectives;
    }

    public PrivateObjective getPrivateObjective() {
        return privateObjective;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    public ModelView getBoard() {
        return board;
    }

    public void setBoard(ModelView board) {
        if(playerNames==null) playerNames = board.getPlayerNames();
        this.board = board;
    }

    public List<ToolCard> getToolCards() {
        return toolCards;
    }

    public void setToolCards(List<ToolCard> toolCards) {
        this.toolCards = toolCards;
    }

    public void setPrivateObjective(PrivateObjective privateObjective) {
        this.privateObjective = privateObjective;
    }

    public void setPublicObjectives(List<PublicObjective> publicObjectives) {
        this.publicObjectives = publicObjectives;
    }
}
