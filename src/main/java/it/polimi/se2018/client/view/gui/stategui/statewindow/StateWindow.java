package it.polimi.se2018.client.view.gui.stategui.statewindow;

import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import javafx.application.Platform;

/**
 * This state is reach during the usage of the toolcard that nedd position of the widnow
 */
public abstract class StateWindow extends State {

    @Override
    public void doActionDraftPool(int draftPoolPosition) {
        //This has to be empty
    }

    @Override
    public void doActionToolCard(int toolCardIndex) {
        if (toolCardIndex == gameSceneController.getToolCardMessage().getToolCardNumber()) {
            gameSceneController.setToolCardMessage(null);
            Platform.runLater(() -> {
                changeState(new StateTurn(gameSceneController));
                gameSceneController.setAllButton();
            });
        }
    }
}