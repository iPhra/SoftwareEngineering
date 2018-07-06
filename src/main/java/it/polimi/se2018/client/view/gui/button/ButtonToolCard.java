package it.polimi.se2018.client.view.gui.button;

import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityHandler;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class control the button of tool card
 */
public class ButtonToolCard extends ButtonGame {
    private final int toolCardNumber;
    private final boolean used;

    public ButtonToolCard(int toolCardNumber, ToolCard toolCard, boolean used) {
        this.toolCardNumber = toolCardNumber;
        this.used = used;
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

    /**
     * This method set the image of this button. If it's already used, the image has a text "USED"
     * @param imageUrl it's the url of the image
     */
    private void setImage(String imageUrl){
        if(used) imageUrl = imageUrl.split(".png")[0]+"_used.png";
        ImageView imageView = new ImageView(new Image(imageUrl));
        imageView.setFitWidth(181);
        imageView.setFitHeight(253);
        setGraphic(imageView);
    }
}