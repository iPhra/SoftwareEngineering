package it.polimi.se2018.client.view.gui.button;

import it.polimi.se2018.client.view.gui.stategui.State;
import javafx.scene.control.Button;

public abstract class ButtonGame extends Button {

    //aggiungere i booleani che servono per checkCondition (stai draftando, mettendo una roba nella window etc)
    public void checkCondition(ButtonCheckUsabilityHandler handler, State currentState){}
}
