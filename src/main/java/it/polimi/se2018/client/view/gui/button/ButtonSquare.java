package it.polimi.se2018.client.view.gui.button;

import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityHandler;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.network.messages.Coordinate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonSquare extends ButtonGame {
    private final int playerID;
    private final Coordinate coordinate;
    private Boolean usable;
    private final String squareImage;
    private Die die;

    public ButtonSquare(int playerID, Coordinate coordinate, Square square) {
        this.playerID = playerID;
        this.coordinate = coordinate;
        squareImage = square.getConstraintPath();
        setImage(squareImage);
        disarm();
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    //this method is called when ia die is put on this square
    public void setDie(Die die) {
        this.die = die;
        setImage("./dice/"+ die.getColor().getAbbreviation()+ die.getValue()+ ".png");
    }

    //this method is called where the die is removed from this square
    public void removeDie(){
        die = null;
        setImage(squareImage);
    }

    @Override
    public void checkCondition(ButtonCheckUsabilityHandler handler){
        usable = handler.checkUsability(this);
        if(usable) arm();
        else disarm();
    }

    private void setImage(String imageUrl){
        ImageView imageView = new ImageView(new Image(imageUrl));
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        setGraphic(imageView);
    }
}