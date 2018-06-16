package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.GUIClient;
import it.polimi.se2018.client.view.gui.button.*;
import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.PassMessage;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.ChangeActionException;
import it.polimi.se2018.utils.exceptions.HaltException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameSceneController implements SceneController, Initializable{
    private final GUIView guiView;
    private final GUIModel guiModel;
    private GUIClient guiClient;
    private final int playerID;
    private WindowSceneController windowSceneController;
    private List<ButtonGame> buttons;
    private ToolCardMessage toolCardMessage;
    private final ToolCardGUI toolCardGUI;
    private State currentState;
    private Stage stage;
    private List<String> sortedPlayersNames;
    private List<Integer> sortedPlayersScores;
    private List<ButtonSquare> windowPlayerButtons;
    private List<ButtonDraftPool> draftPoolButtons;
    private List<List<MenuItemRoundTracker>> roundTrackerMenuItems;
    private List<ButtonToolCard> toolCardButtons;

    @FXML
    private BorderPane borderPane;

    @FXML
    private GridPane topGridPane;

    @FXML
    private GridPane botGridPane;

    @FXML
    private GridPane rightGridPane;

    @FXML
    private Label serviceLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label favorPointsLabel;

    public GameSceneController(GUIController guiController) {
        this.guiView = guiController.getGuiView();
        this.guiModel = guiController.getGuiModel();
        this.playerID = guiController.getPlayerID();
        toolCardGUI = new ToolCardGUI(this);
        currentState = new StateTurn(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        //this padding must be fixed
        borderPane.setPadding(new Insets(100,100,150,50));
        windowPlayerButtons = new ArrayList<>();
        for(Square[] row : guiModel.getBoard().getPlayerWindows().get(guiModel.getBoard().getPlayerID().indexOf(playerID))){
            for(Square square : row){
                windowPlayerButtons.add(new ButtonSquare(playerID, new Coordinate(square.getRow(), square.getCol()),square));
            }
        }
        toolCardButtons = new ArrayList<>();
        for(int i=0; i < guiModel.getToolCards().size(); i++){
            toolCardButtons.add(new ButtonToolCard(playerID, i, guiModel.getToolCards().get(i)));
        }
        draftPoolButtons = new ArrayList<>();
        for(Die die : guiModel.getBoard().getDraftPool()){
            draftPoolButtons.add(new ButtonDraftPool(playerID, die));
        }
        roundTrackerMenuItems = new ArrayList<>();
        List<List<Die>> roundTracker = new ArrayList<>(guiModel.getBoard().getRoundTracker());
        for(int i=0; i < roundTracker.size(); i++){
            List<MenuItemRoundTracker> singleRound = new ArrayList<>();
            for(int j=0; j < roundTracker.get(0).size(); j++){
                singleRound.add(new MenuItemRoundTracker(playerID,new Coordinate(i,j),roundTracker.get(i).get(j)));
                roundTrackerMenuItems.add(singleRound);
            }
        }

        setRightGridpane();
        setBotGridPane();
        setTopGridpane();
        setLeft();
        //to be implemented: initialization of draftpool and roundtracker
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public GUIModel getGuiModel() {
        return guiModel;
    }

    public ToolCardMessage getToolCardMessage() {
        return toolCardMessage;
    }

    public GUIView getGuiView() {
        return guiView;
    }

    public int getPlayerID() {
        return playerID;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void disableAllButton(){
        for(ButtonSquare buttonSquare : windowPlayerButtons){
            buttonSquare.disarm();
        }
        for(ButtonDraftPool buttonDraftPool : draftPoolButtons){
            buttonDraftPool.disarm();
        }
        for(ButtonToolCard buttonToolCard : toolCardButtons){
            buttonToolCard.disarm();
        }
        for(List<MenuItemRoundTracker> round : roundTrackerMenuItems){
            for(MenuItemRoundTracker menuItemRoundTracker : round){
                menuItemRoundTracker.setDisable(true);
            }
        }
        //to be implented: disarm the other buttons, for example the one that shows the roundtracker
        refresh();
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public void setToolCardMessage(ToolCardMessage toolCardMessage) {
        this.toolCardMessage = toolCardMessage;
    }

    public void setAllButton(){
        for(ButtonSquare buttonSquare : windowPlayerButtons){
            buttonSquare.checkCondition(currentState.getButtonCheckUsabilityHandler());
        }
        for(ButtonDraftPool buttonDraftPool : draftPoolButtons){
            buttonDraftPool.checkCondition(currentState.getButtonCheckUsabilityHandler());
        }
        for(ButtonToolCard buttonToolCard : toolCardButtons){
            buttonToolCard.checkCondition(currentState.getButtonCheckUsabilityHandler());
        }
        for(List<MenuItemRoundTracker> round : roundTrackerMenuItems){
            for(MenuItemRoundTracker menuItemRoundTracker : round){
                menuItemRoundTracker.checkCondition(currentState.getButtonCheckUsabilityHandler());
            }
        }
    }

    public void sendToolCardMessage() {
        guiView.handleNetworkOutput(toolCardMessage);
        toolCardMessage = null;
    }

    //togliere le eccezioni e capire se servono o come gestirle
    //This method is called by network input when you receive an ack that allow you to use the toolcard
    public void useToolCard(int toolCardIndex) throws ChangeActionException, HaltException {
        ToolCard toolCard = guiModel.getToolCards().get(toolCardIndex);
        toolCardMessage = new ToolCardMessage(playerID, guiModel.getBoard().getStateID(), toolCardIndex);
        toolCard.handleGUI(toolCardGUI, toolCardIndex);
    }

    //This method is called by the controller of the button pass turn
    public void passTurnButtonClicked() {
        guiView.handleNetworkOutput(new PassMessage(playerID, guiModel.getBoard().getStateID(), false));
        disableAllButton();
    }


    //This method is called by controller of square button and round tracker button
    //Current state know how handle input
    public void buttonCoordinateClicked(Coordinate coordinate) {
        currentState.doActionWindow(coordinate);
    }

    //This method is called by controller of draft pool button
    public void buttonDraftPoolClicked(int drafPoolPosition){
        currentState.doActionDraftPool(drafPoolPosition);
    }

    //This method is called by controller of tool cards
    public void buttonToolCardClicked(int toolCardIndex){
        currentState.doActionToolCard(toolCardIndex);
    }

    public void setClientGUI(GUIClient guiClient) {
        this.guiClient = guiClient;
    }

    public void setSortedPlayersNames(List<String> sortedPlayersNames) {
        this.sortedPlayersNames = sortedPlayersNames;
    }

    public void setSortedPlayersScores(List<Integer> sortedPlayersScores) {
        this.sortedPlayersScores = sortedPlayersScores;
    }

    @Override
    public void changeScene(Scene scene) {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/ScoreBoardScene.fxml")));
        try {
            ScoreBoardSceneController scoreBoardSceneController = new ScoreBoardSceneController(sortedPlayersNames, sortedPlayersScores);
            loader.setController(scoreBoardSceneController);
            Parent root = loader.load();
            ((GUIView) guiClient.getGUIView()).getGuiController().setSceneController(scoreBoardSceneController);
            stage.setWidth(600);
            stage.setHeight(623);
            scoreBoardSceneController.setStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    @Override
    public Scene getScene() {
        return borderPane.getScene();
    }

    private void setRightGridpane(){
        List<Square[][]> enemyWindows = new ArrayList<>(guiModel.getBoard().getPlayerWindows());
        enemyWindows.remove(guiModel.getBoard().getPlayerID().indexOf(playerID));
        for(int i=0; i < enemyWindows.size(); i++){
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/windowEnemyScene.fxml")));
            loader.setController(new WindowEnemySceneController(enemyWindows.get(i)));
            try {
                Node node = loader.load();
                Pane pane = new Pane();
                pane.getChildren().add(node);
                pane.setMaxWidth(206);
                pane.setMaxHeight(182);
                rightGridPane.add(pane,0,i);
            } catch (IOException e) {
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.ALL,e.getMessage());
            }
        }
    }

    private void setBotGridPane(){
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/windowScene.fxml")));
        loader.setController((new WindowSceneController(windowPlayerButtons,this)));
        try{
            Node node = loader.load();
            Pane pane = new Pane();
            pane.getChildren().add(node);
            pane.setMaxWidth(206);
            pane.setMaxHeight(182);
            botGridPane.add(pane,2,0);
        }catch(IOException e){
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
        serviceLabel.setText("Game just started");
        int myIndex = guiModel.getBoard().getPlayerID().indexOf(playerID);
        nameLabel.setText(guiModel.getBoard().getPlayerNames().get(myIndex));
        favorPointsLabel.setText(String.valueOf(guiModel.getBoard().getPlayerFavorPoint().get(myIndex)));
    }

    private void setTopGridpane(){
        for(int i=0; i < toolCardButtons.size(); i++){
            topGridPane.add(toolCardButtons.get(i),i,0);
        }
        for(int i=0; i < guiModel.getPublicObjectives().size(); i++){
            ImageView imageView = new ImageView(new Image(guiModel.getPublicObjectives().get(i).getImagePath()));
            imageView.setFitHeight(211);
            imageView.setFitWidth(151);
            topGridPane.add(imageView,i,1);
        }
        topGridPane.setVgap(200);
        topGridPane.setHgap(5);
    }

    private void setLeft(){
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/draftPoolScene.fxml")));
        loader.setController((new DraftPoolSceneController(draftPoolButtons,this)));
        try{
            Node node = loader.load();
            Pane pane = new Pane();
            pane.getChildren().add(node);
            pane.setMaxWidth(500);
            pane.setMaxHeight(25);
            borderPane.setLeft(pane);
        }catch(IOException e){
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    public void refresh(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setTopGridpane();
                setRightGridpane();
                setBotGridPane();
                //to be implemented: initialization of draftpool and roundtracker
            }
        });
    }

    void setText(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //setta il testo
            }
        });
    }
}
