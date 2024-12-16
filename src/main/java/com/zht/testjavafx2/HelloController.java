package com.zht.testjavafx2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * @author Administrator
 */
public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application123!");
    }
}