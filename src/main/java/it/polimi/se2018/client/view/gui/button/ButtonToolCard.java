package it.polimi.se2018.client.view.gui.button;

import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityHandler;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonToolCard extends ButtonGame {
    private final int toolCardNumber;

    public ButtonToolCard(int toolCardNumber, ToolCard toolCard) {
        this.toolCardNumber = toolCardNumber;
        setImage(toolCard.getImagePath());
        setDisable(true);
    }

    public int getToolCardNumber() {
        return toolCardNumber;
    }

    @Override
    public void checkCondition(ButtonCheckUsabilityHandler handler){
        boolean usable = handler.checkUsability(this);
        if(usable) setDisable(false);
        else setDisable(true);
    }

    private void setImage(String imageUrl){
        ImageView imageView = new ImageView(new Image(imageUrl));
        imageView.setFitWidth(181);
        imageView.setFitHeight(253);
        setGraphic(imageView);
    }
}