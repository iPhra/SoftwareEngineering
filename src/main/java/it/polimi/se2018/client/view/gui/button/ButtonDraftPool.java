package it.polimi.se2018.client.view.gui.button;


import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityHandler;
import it.polimi.se2018.mvc.model.Die;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonDraftPool extends ButtonGame {
    private final int playerID;
    private Boolean usable;
    private final Die die;
    private int position;

    public ButtonDraftPool(int playerID, Die die) {
        this.die = die;
        this.playerID = playerID;
        ImageView imageView = new ImageView(new Image("./dice/"+ die.getColor().getAbbreviation()+ die.getValue()+ ".png"));
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        setGraphic(imageView);
        disarm();
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void checkCondition(ButtonCheckUsabilityHandler handler){
        usable = handler.checkUsability(this);
        if(usable) arm();
        else disarm();
    }
}
