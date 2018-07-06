package it.polimi.se2018.client.view.gui.controllers;

import it.polimi.se2018.client.view.gui.*;
import it.polimi.se2018.client.view.gui.button.*;
import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.PassMessage;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
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
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameSceneController extends MatchHandler implements SceneController, Initializable {
    private final GUIView guiView;
    private final GUIData guiModel;
    private final int playerID;
    private ImageView dieInHandImageView;
    private ImageView privateObjectiveImageView;
    private Pane windowPane;
    private ToolCardMessage toolCardMessage;
    private final ToolCardGUI toolCardGUI;
    private State currentState;
    private List<ButtonSquare> windowPlayerButtons;
    private List<ButtonDraftPool> draftPoolButtons;
    private List<List<MenuItemRoundTracker>> roundTrackerMenuItems;
    private List<ButtonToolCard> toolCardButtons;
    private ButtonPass buttonPass;

    @FXML
    private BorderPane borderPane;

    @FXML
    private GridPane botGridPane;

    @FXML
    private GridPane rightGridPane;

    @FXML
    private GridPane leftGridPane;

    @FXML
    private Label serviceLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label favorPointsLabel;

    public GameSceneController(GUILogic guiLogic) {
        this.guiView = guiLogic.getGuiView();
        this.guiModel = guiLogic.getGuiModel();
        this.playerID = guiLogic.getPlayerID();
        toolCardGUI = new ToolCardGUI(this);
        currentState = new StateTurn(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        buttonPass = new ButtonPass();
        buttonPass.setText("Pass");
        buttonPass.setOnAction(e -> passTurnButtonClicked());
        borderPane.setPadding(new Insets(120,20,110,20));
        borderPane.setStyle("-fx-background-image:url('otherimages/spiral-light-stained-glass.jpg');");
        windowPlayerButtons = new ArrayList<>();
        toolCardButtons = new ArrayList<>();
        draftPoolButtons = new ArrayList<>();
        roundTrackerMenuItems = new ArrayList<>();
        instantiateButtons();
        setRightGridpane();
        setBotGridPane();
        setLeftGridpane();
        checkTurn();
    }

    private void setRightGridpane(){
        List<Square[][]> enemyWindows = new ArrayList<>(guiModel.getBoard().getPlayerWindows());
        int indexOfThisPlayer = guiModel.getBoard().getPlayerID().indexOf(playerID);
        enemyWindows.remove(indexOfThisPlayer);
        List<String> enemyNames = new ArrayList<>(guiModel.getBoard().getPlayerNames());
        enemyNames.remove(indexOfThisPlayer);
        int j=0;
        for(int i=0; i < enemyWindows.size(); i++){
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/windowEnemyScene.fxml")));
            loader.setController(new WindowEnemySceneController(enemyWindows.get(i)));
            Label label = new Label();
            label.setText(enemyNames.get(i));
            label.setFont(Font.font("System", FontWeight.BOLD, 14));
            label.setTextFill(Paint.valueOf("RED"));
            rightGridPane.add(label,0,j);
            try {
                Node node = loader.load();
                Pane pane = new Pane();
                pane.getChildren().add(node);
                pane.setMaxWidth(206);
                pane.setMaxHeight(182);
                rightGridPane.add(pane,0,j+1);
                GridPane.setMargin(pane,new Insets(0,0,400,0));
                j++;
            } catch (IOException e) {
                e.printStackTrace();
            }
            rightGridPane.setVgap(200);
        }
    }

    private void setBotGridPane(){
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/windowScene.fxml")));
        loader.setController((new WindowSceneController(windowPlayerButtons,this)));
        try{
            Node node = loader.load();
            windowPane = new Pane();
            windowPane.getChildren().add(node);
            windowPane.setMaxWidth(321);
            windowPane.setMaxHeight(283);
            botGridPane.add(windowPane,1,0);
            GridPane.setMargin(windowPane, new Insets(0,20,160,0));
        }catch(IOException e){
            e.printStackTrace();
        }
        int myIndex = guiModel.getBoard().getPlayerID().indexOf(playerID);
        nameLabel.setText(guiModel.getBoard().getPlayerNames().get(myIndex));
        favorPointsLabel.setText("You have " + guiModel.getBoard().getPlayerFavorPoint().get(myIndex) + " Favor Points");
        if(guiModel.getBoard().getCurrentPlayerID()==playerID && guiModel.getBoard().hasDieInHand()){
            Die dieInHand = guiModel.getBoard().getDieInHand();
            dieInHandImageView = new ImageView(new Image("/dice/"+ dieInHand.getColor().getAbbreviation()+ dieInHand.getValue()+ ".png"));
            dieInHandImageView.setFitWidth(50);
            dieInHandImageView.setFitHeight(50);
            botGridPane.add(dieInHandImageView,2,0);
            GridPane.setMargin(dieInHandImageView, new Insets(0,0,0,100));
        }
        privateObjectiveImageView = new ImageView(new Image(guiModel.getPrivateObjective().getImagePath()));
        privateObjectiveImageView.setFitWidth(181);
        privateObjectiveImageView.setFitHeight(253);
        botGridPane.add(privateObjectiveImageView,3,2);
        GridPane.setMargin(privateObjectiveImageView, new Insets(0,0,0,30));
        botGridPane.add(buttonPass,4,2);
        GridPane.setMargin(buttonPass, new Insets(0,0,0,30));
        botGridPane.setHgap(80);
    }

    private void setLeftGridpane(){
        boolean[] toolCardPanesHaveImage = new boolean[3];
        for(int i=0; i<3; i++) toolCardPanesHaveImage[i] = false;
        for(int i=0; i < toolCardButtons.size(); i++){
            Pane pane = new Pane();
            pane.getChildren().add(toolCardButtons.get(i));
            pane.setOnMouseEntered(e -> {
                ButtonToolCard buttonToolCard = (ButtonToolCard)((Pane) e.getSource()).getChildren().get(0);
                if(buttonToolCard.isDisabled()){
                    int toolCardNumber = buttonToolCard.getToolCardNumber();
                    ImageView imageView = new ImageView(new Image(guiModel.getToolCards().get(toolCardNumber).getImagePath()));
                    imageView.setFitWidth(181);
                    imageView.setFitHeight(253);
                    ((Pane) e.getSource()).getChildren().add(imageView);
                    toolCardPanesHaveImage[toolCardNumber] = true;
                }
            });
            pane.setOnMouseExited(e -> {
                ButtonToolCard buttonToolCard = (ButtonToolCard)((Pane) e.getSource()).getChildren().get(0);
                int toolCardNumber = buttonToolCard.getToolCardNumber();
                if(toolCardPanesHaveImage[toolCardNumber]){
                    ((Pane) e.getSource()).getChildren().remove(1);
                    toolCardPanesHaveImage[toolCardNumber] = false;
                }
            });
            leftGridPane.add(pane,i,0);
            GridPane.setMargin(pane, new Insets(0,0,180,0));
        }
        for(int i=0; i < guiModel.getPublicObjectives().size(); i++){
            ImageView imageView = new ImageView(new Image(guiModel.getPublicObjectives().get(i).getImagePath()));
            imageView.setFitWidth(181);
            imageView.setFitHeight(253);
            leftGridPane.add(imageView,i,1);
        }
        Pane draftPoolPane = paneDraftPool();
        leftGridPane.add(draftPoolPane,3,0);
        Pane roundTrackerPane = paneRoundTracker();
        leftGridPane.add(roundTrackerPane,3,1);
        leftGridPane.setVgap(320);
        leftGridPane.setHgap(100);
    }

    /**
     * This method loads the draftPool
     * @return a pane containing the draftPool
     */
    private Pane paneDraftPool(){
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/draftPoolScene.fxml")));
        loader.setController(new DraftPoolSceneController(draftPoolButtons,this));
        Pane pane = new Pane();
        try{
            Node node = loader.load();
            pane.getChildren().add(node);
            pane.setMaxWidth(500);
            pane.setMaxHeight(25);
        }catch(IOException e){
            e.printStackTrace();
        }
        return pane;
    }

    /**
     * This method loads the roundTracker
     * @return a pane containing the roundTracker
     */
    private Pane paneRoundTracker(){
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/RoundTrackerScene.fxml")));
        loader.setController(new RoundTrackerSceneController(roundTrackerMenuItems, this));
        Pane pane = new Pane();
        try{
            Node node = loader.load();
            pane.getChildren().add(node);
            pane.setMaxWidth(473);
            pane.setMaxHeight(99);
        }catch(IOException e){
            e.printStackTrace();
        }
        return pane;
    }

    private void setDraftPoolButtons(){
        for(Die die : guiModel.getBoard().getDraftPool()){
            draftPoolButtons.add(new ButtonDraftPool(die));
        }
    }

    private void setRoundTrackerMenuItems(){
        List<List<Die>> roundTracker = new ArrayList<>(guiModel.getBoard().getRoundTracker());
        for(int i=0; i < roundTracker.size(); i++){
            List<MenuItemRoundTracker> singleRound = new ArrayList<>();
            for(int j=0; j < roundTracker.get(i).size(); j++){
                singleRound.add(new MenuItemRoundTracker(new Coordinate(i,j),roundTracker.get(i).get(j)));
            }
            roundTrackerMenuItems.add(singleRound);
        }
    }

    private void setWindowPlayerButtons(){
        for(Square[] row : guiModel.getBoard().getPlayerWindows().get(guiModel.getBoard().getPlayerID().indexOf(playerID))){
            for(Square square : row){
                ButtonSquare  buttonSquare = new ButtonSquare(new Coordinate(square.getRow(), square.getCol()),square);
                if (!square.isEmpty()) buttonSquare.setDie(square.getDie());
                windowPlayerButtons.add(buttonSquare);
            }
        }
    }

    private void setToolCardButtons(){
        for(int i=0; i < guiModel.getToolCards().size(); i++){
            toolCardButtons.add(new ButtonToolCard(i, guiModel.getToolCards().get(i),guiModel.getBoard().getToolCardUsage().get(i)));
            //here you define what happens if you click on a toolcard
            toolCardButtons.get(i).setOnAction(e -> buttonToolCardClicked(((ButtonToolCard)e.getSource()).getToolCardNumber()));
        }
    }

    /**
     * This method refreshes reloads the sub-fxmls and puts in them the lists of buttons. So, it requires that those lists
     * have been updated already before calling this
     */
    private void refreshAll(){
            leftGridPane.getChildren().clear();
            botGridPane.getChildren().remove(windowPane);
            botGridPane.getChildren().remove(dieInHandImageView);
            botGridPane.getChildren().remove(privateObjectiveImageView);
            botGridPane.getChildren().remove(buttonPass);
            rightGridPane.getChildren().clear();
            setLeftGridpane();
            setRightGridpane();
            setBotGridPane();
    }

    private void checkTurn(){
        if (playerID != guiModel.getBoard().getCurrentPlayerID()) disableAllButton();
        else setAllButton();
    }

    /**
     * This method clears all the lists of buttons, after that, you typically call the method instantiateButtons
     */
    private void clearAllButtonLists(){
        windowPlayerButtons.clear();
        draftPoolButtons.clear();
        roundTrackerMenuItems.clear();
        toolCardButtons.clear();
    }

    /**
     * This method fills the lists of buttons with the buttons. Requires that at first those list don't contain elements
     */
    private void instantiateButtons(){
        setWindowPlayerButtons();
        setDraftPoolButtons();
        setRoundTrackerMenuItems();
        setToolCardButtons();
    }


    //This method is called by the controller of the button pass turn
    private void passTurnButtonClicked() {
        guiView.handleNetworkOutput(new PassMessage(playerID, guiModel.getBoard().getStateID(), false));
        disableAllButton();
    }

    //This method is called by controller of tool cards
    private void buttonToolCardClicked(int toolCardIndex){
        currentState.doActionToolCard(toolCardIndex);
    }

    /**
     * This method is called when the toolcard that lets you choose if increase or decrease by one the number of the die
     * is used. It opens a new stage in which the player can choose. This stage will close after the choice and will
     * send a message
     */
    public void createPlusOrMinusWindow(){
        GameSceneController gameSceneController = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/ChoosePlusOrMinusScene.fxml")));
                try {
                    Parent root = loader.load();
                    ChoosePlusOrMinusSceneController controller = loader.getController();
                    controller.setGameSceneController(gameSceneController);
                    Stage secondaryStage = new Stage();
                    secondaryStage.setTitle("Choose plus one or minus one");
                    secondaryStage.setScene(new Scene(root, 403, 119));
                    secondaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This method is called when the toolcard that lets you move up to two dice is used. In particular, it's called
     * when the player has already moved a die. It opens a new stage in which the player can choose if he wants to move
     * another die or not. This stage will close after the choice and will send a message
     */
    public void createNumberWindow(){
        GameSceneController gameSceneController = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/ChooseNumberScene.fxml")));
                try {
                    Parent root = loader.load();
                    ChooseNumberSceneController controller = loader.getController();
                    controller.setGameSceneController(gameSceneController);
                    Stage secondaryStage = new Stage();
                    secondaryStage.setTitle("Choose the value");
                    secondaryStage.setScene(new Scene(root, 446, 261));
                    secondaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This method is called when the toolcard that lets you choose the number of the die is used. It opens a new stage
     * in which the player can choose. This stage will close after the choice and will send a message
     */
    public void createOneOrTwo(){
        GameSceneController gameSceneController = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/OneOrTwoDice.fxml")));
                try {
                    Parent root = loader.load();
                    AnotherDieYesOrNoSceneController controller = loader.getController();
                    controller.setGameSceneController(gameSceneController);
                    Stage secondaryStage = new Stage();
                    secondaryStage.setTitle("Do you want to move one or two dice?");
                    secondaryStage.setScene(new Scene(root, 279, 143));
                    secondaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This method is called when something happened and you need to comunicate it to the player
     * @param text it's the text you want to show
     */
    public void setText(String text) {
        serviceLabel.setText(text);
    }

    public GUIData getGuiModel() {
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

    /**
     * This method disables all the buttons in the lists. After that, it calls method refreshAll in order to reloads the
     * sub-fxmls and put in them the updated buttons
     */
    public void disableAllButton(){
        for(ButtonSquare buttonSquare : windowPlayerButtons){
            buttonSquare.setDisable(true);
        }
        for(ButtonDraftPool buttonDraftPool : draftPoolButtons){
            buttonDraftPool.setDisable(true);
        }
        for(ButtonToolCard buttonToolCard : toolCardButtons){
            buttonToolCard.setDisable(true);
        }
        for(List<MenuItemRoundTracker> round : roundTrackerMenuItems){
            for(MenuItemRoundTracker menuItemRoundTracker : round){
                menuItemRoundTracker.setDisable(true);
            }
        }
        buttonPass.setDisable(true);
        refreshAll();
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public void setToolCardMessage(ToolCardMessage toolCardMessage) {
        this.toolCardMessage = toolCardMessage;
    }

    /**
     * This method sets disable true or false on every button of the lists, based on the currentState. After that, it
     * calls method refreshAll in order to reloads the sub-fxmls and put in them the updated buttons
     */
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
        buttonPass.checkCondition(currentState.getButtonCheckUsabilityHandler());
        refreshAll();
    }

    public void sendToolCardMessage() {
        guiView.handleNetworkOutput(toolCardMessage);
        toolCardMessage = null;
    }

    //This method is called by network input when you receive an ack that allow you to use the toolcard
    public void useToolCard(int toolCardIndex) {
        ToolCard toolCard = guiModel.getToolCards().get(toolCardIndex);
        toolCardMessage = new ToolCardMessage(playerID, guiModel.getBoard().getStateID(), toolCardIndex);
        toolCard.handleGUI(toolCardGUI, toolCardIndex);
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

    @Override
    public Scene getScene() {
        return borderPane.getScene();
    }

    @Override
    public void changeScene(Scene scene) {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/ScoreBoardScene.fxml")));
            try {
                ScoreBoardSceneController scoreBoardSceneController = new ScoreBoardSceneController(sortedPlayersNames, sortedPlayersScores, isLastPlayer);
                scoreBoardSceneController.setGuiClient(guiClient);
                loader.setController(scoreBoardSceneController);
                Parent root = loader.load();
                guiClient.getGUIView().getGuiLogic().setSceneController(scoreBoardSceneController);
                stage.setWidth(600);
                stage.setHeight(623);
                scoreBoardSceneController.setStage(stage);
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This method is called in {@link GUILogic} when it's needed to refresh something in the scene. Actually,
     * it refreshes everything. Also, it calls the method checkTurn
     */
    public void clearAndRefreshAll(){
        clearAllButtonLists();
        instantiateButtons();
        checkTurn();
    }
}