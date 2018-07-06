package it.polimi.se2018.client.view.gui.button;

import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityHandler;

/**
 * This button control the button pass
 */
public class ButtonPass extends ButtonGame{

    public ButtonPass() {
        setDisable(true);
    }

    @Override
    public void checkCondition(ButtonCheckUsabilityHandler handler){
        boolean usable = handler.checkUsability(this);
        if(usable) setDisable(false);
        else setDisable(true);
    }
}
