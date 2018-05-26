package it.polimi.se2018.client.view.gui;

import javafx.stage.Stage;

public class StartingSceneController extends SceneController{
    private ClientGUI clientGUI;
    private Stage stage;

    public void socketButtonClicked(){
        clientGUI.createSocketConnection();
    }

    public void RMIButtonClicked(){
        clientGUI.createRMIConnection();
    }
}
