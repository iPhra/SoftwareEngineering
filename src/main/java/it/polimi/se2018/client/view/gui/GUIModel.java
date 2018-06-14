package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.view.ClientModel;

public class GUIModel extends ClientModel {
    private final GUIView guiView;

    GUIModel(GUIView guiView, int playerID) {
        super(playerID);
        this.guiView = guiView;
    }
}
