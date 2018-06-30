package it.polimi.se2018.client.view.gui.controllers;

import it.polimi.se2018.client.view.gui.button.ButtonDraftPool;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

class DraftPoolSceneController implements Initializable{
    private final List<ButtonDraftPool> buttons;
    private final GameSceneController gameSceneController;

    @FXML
    private GridPane gridPane;

    DraftPoolSceneController(List<ButtonDraftPool> buttons,GameSceneController gameSceneController){
        this.buttons = buttons;
        this.gameSceneController = gameSceneController;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(int i=0; i < buttons.size(); i++){
            gridPane.add(buttons.get(i),i,0);
            buttons.get(i).setPosition(i);
            buttons.get(i).setOnAction(e -> gameSceneController.buttonDraftPoolClicked(((ButtonDraftPool)e.getSource()).getPosition()));
        }
    }
}
