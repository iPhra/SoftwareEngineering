package it.polimi.se2018.client.view.gui.controllers;

import it.polimi.se2018.network.messages.requests.InputMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * This is the controller of the scene that is used to ask to the player the value to give to the extract die
 * during the usage of {@link it.polimi.se2018.mvc.model.toolcards.GlazingHammer}
 */
public class ChooseNumberSceneController {

    /**
     * This is the scene of the game
     */
    private GameSceneController gameSceneController;

    /**
     * This is the button that the player clicks if he wants "1"
     */
    @FXML
    private Button button1;

    /**
     * This is the button that the player clicks if he wants "2"
     */
    @FXML
    private Button button2;

    /**
     * This is the button that the player clicks if he wants "3"
     */
    @FXML
    private Button button3;

    /**
     * This is the button that the player clicks if he wants "4"
     */
    @FXML
    private Button button4;

    /**
     * This is the button that the player clicks if he wants "5"
     */
    @FXML
    private Button button5;

    /**
     * This is the button that the player clicks if he wants "6"
     */
    @FXML
    private Button button6;

    public void setGameSceneController(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    /**
     * This method is called when button1 is clicked and sends to the server the choice of the player
     */
    public void button1Clicked(){
        sendChoiceToServer(1);
    }

    /**
     * This method is called when button2 is clicked and sends to the server the choice of the player
     */
    public void button2Clicked(){
        sendChoiceToServer(2);
    }

    /**
     * This method is called when button3 is clicked and sends to the server the choice of the player
     */
    public void button3Clicked(){
        sendChoiceToServer(3);
    }

    /**
     * This method is called when button4 is clicked and sends to the server the choice of the player
     */
    public void button4Clicked(){
        sendChoiceToServer(4);
    }

    /**
     * This method is called when button5 is clicked and sends to the server the choice of the player
     */
    public void button5Clicked(){
        sendChoiceToServer(5);
    }

    /**
     * This method is called when button6 is clicked and sends to the server the choice of the player
     */
    public void button6Clicked(){
        sendChoiceToServer(6);
    }

    /**
     * This method sends the choice to the server
     * @param choice it's che number chosen
     */
    private void sendChoiceToServer(int choice){
        gameSceneController.getGuiView().handleNetworkOutput(new InputMessage(gameSceneController.getPlayerID(), gameSceneController.getGuiModel().getBoard().getStateID(), choice));
        Stage stage = (Stage)button1.getScene().getWindow();
        stage.close();
    }
}
