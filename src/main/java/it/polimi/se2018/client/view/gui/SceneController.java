package it.polimi.se2018.client.view.gui;

import javafx.scene.Scene;

interface SceneController {

    void setClientGUI(ClientGUI clientGUI);

    void changeScene(Scene scene);

    Scene getScene();
}
