package it.polimi.se2018.client.view.gui.button;

import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityHandler;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.network.messages.Coordinate;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MenuItemRoundTracker extends MenuItem {
    private final int playerID;
    private final Coordinate coordinate;
    private Boolean usable;
    private Die die;

    public MenuItemRoundTracker(int playerID, Coordinate coordinate, Die die) {
        this.playerID = playerID;
        this.coordinate = coordinate;
        this.die = die;
        ImageView imageView = new ImageView(new Image("./dice/"+ die.getColor().getAbbreviation()+ die.getValue()+ ".png"));
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        setGraphic(imageView);
        setDisable(true);
    }

    public void checkCondition(ButtonCheckUsabilityHandler handler){
        usable = handler.checkUsability(this);
        if(usable) setDisable(false);
        else setDisable(true);
    }
}
