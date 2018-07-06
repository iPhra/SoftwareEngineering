package it.polimi.se2018.client.view.gui.button;


import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityHandler;
import it.polimi.se2018.mvc.model.Die;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonDraftPool extends ButtonGame {
    private int position;

    public ButtonDraftPool(Die die) {
        ImageView imageView = new ImageView(new Image("/dice/"+ die.getColor().getAbbreviation()+ die.getValue()+ ".png"));
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        setGraphic(imageView);
        setDisable(true);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void checkCondition(ButtonCheckUsabilityHandler handler){
        boolean usable = handler.checkUsability(this);
        if(usable) setDisable(false);
        else setDisable(true);
    }
}
