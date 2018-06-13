package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.view.ClientModel;
import it.polimi.se2018.mvc.controller.ModelView;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;

import java.util.List;

class GUIModel extends ClientModel {
    private final GUIView guiView;

    GUIModel(GUIView guiView, int playerID) {
        super(playerID);
        this.guiView = guiView;
    }
}
