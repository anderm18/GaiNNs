package com.example.gainns;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;

public class HelloController {
    @FXML
    private Label welcomeText;
    
    private BackgroundImage bgImage = new BackgroundImage(
    		new Image("file:Fish_irl.png"), 
    		BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
    		BackgroundPosition.CENTER, null); 
    private BackgroundImage[] bgList = {bgImage};
    
    @FXML 
    private Background bgObj = new Background(bgList);
    
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}