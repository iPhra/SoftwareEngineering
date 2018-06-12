package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.mvc.controller.ModelView;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;

import java.util.List;

class GUIModel {
    private final GUIView guiView;
    private final int playerID;
    private int playersNumber;
    private List<String> playerNames;
    private ModelView board;
    private List<ToolCard> toolCards;
    private PrivateObjective privateObjective;
    private List<PublicObjective> publicObjectives;

    GUIModel(GUIView guiView, int playerID) {
        this.guiView = guiView;
        this. playerID = playerID;
    }

    int getPlayerID() {
        return playerID;
    }

    void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    ModelView getBoard() {
        return board;
    }

    void setBoard(ModelView board) {
        if(playerNames==null) playerNames = board.getPlayerNames();
        this.board = board;
    }

    List<ToolCard> getToolCards() {
        return toolCards;
    }

    void setToolCards(List<ToolCard> toolCards) {
        this.toolCards = toolCards;
    }

    void setPrivateObjective(PrivateObjective privateObjective) {
        this.privateObjective = privateObjective;
    }

    void setPublicObjectives(List<PublicObjective> publicObjectives) {
        this.publicObjectives = publicObjectives;
    }
}
