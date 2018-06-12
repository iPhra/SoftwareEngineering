package it.polimi.se2018.client.view.gui.stategui;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityTurn;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.DraftMessage;
import it.polimi.se2018.network.messages.requests.PlaceMessage;
import it.polimi.se2018.network.messages.requests.ToolCardRequestMessage;

public class StateTurn extends State {

    public StateTurn(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
        buttonCheckUsabilityHandler = new ButtonCheckUsabilityTurn(gameSceneController);
    }

    @Override
    public void doActionWindow(Coordinate coordinate) {
        PlaceMessage placeMessage = new PlaceMessage(gameSceneController.getPlayerID(), gameSceneController.getStateID(), coordinate);
        //send message
        gameSceneController.getGuiView().handleNetworkOutput(placeMessage);
        changeState(new StateTurn(gameSceneController));
        gameSceneController.disableAllButton();
    }

    @Override
    public void doActionDraftPool(int draftPoolPosition) {
        gameSceneController.getGuiView().handleNetworkOutput(new DraftMessage(gameSceneController.getPlayerID(), gameSceneController.getStateID(), draftPoolPosition));
        changeState(new StateTurn(gameSceneController));
        gameSceneController.disableAllButton();
    }

    @Override
    public void doActionToolCard(int toolCardIndex) {
        gameSceneController.getGuiView().handleNetworkOutput(new ToolCardRequestMessage(gameSceneController.getPlayerID(), gameSceneController.getStateID(), toolCardIndex));
        changeState(new StateTurn(gameSceneController));
        gameSceneController.disableAllButton();
    }
}
