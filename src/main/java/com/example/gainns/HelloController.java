package com.example.gainns;

import javafx.fxml.FXML;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.image.Image;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private Label hiText;
    @FXML
    private Image fish = new Image("file:Fish_irl.png");    
    @FXML
    private BackgroundImage bg = new BackgroundImage(fish, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, null);
    @FXML
    private BackgroundImage[] bgList = {bg};
    @FXML 
    private Background bgObj = new Background(bgList);
    
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    
    @FXML
    protected void onHiButtonClick() {
        hiText.setText("Hi");
    }
}