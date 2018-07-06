package it.polimi.se2018.client.view.gui.controllers;

import javafx.scene.Scene;

/**
 * This interface is implemented by the scene controllers of the main fxmls. sub-fxmls, like roundtracker's one, don't
 * implement this interface
 */
public interface SceneController {

    void changeScene(Scene scene);

    Scene getScene();
}
