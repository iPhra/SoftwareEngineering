package it.polimi.se2018.client.view.gui.button;

import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityHandler;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.network.messages.Coordinate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonSquare extends ButtonGame {
    private final Coordinate coordinate;

    public ButtonSquare(Coordinate coordinate, Square square) {
        this.coordinate = coordinate;
        String squareImage = square.getConstraintPath();
        setImage(squareImage);
        setDisable(true);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    //this method is called when ia die is put on this square
    public void setDie(Die die) {
        setImage("/dice/"+ die.getColor().getAbbreviation()+ die.getValue()+ ".png");
    }

    @Override
    public void checkCondition(ButtonCheckUsabilityHandler handler){
        boolean usable = handler.checkUsability(this);
        if(usable) setDisable(false);
        else setDisable(true);
    }

    private void setImage(String imageUrl){
        ImageView imageView = new ImageView(new Image(imageUrl));
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        setGraphic(imageView);
    }
}