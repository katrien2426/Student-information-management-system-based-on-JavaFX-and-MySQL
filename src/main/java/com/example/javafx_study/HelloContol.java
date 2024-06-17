package com.example.javafx_study;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloContol {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}