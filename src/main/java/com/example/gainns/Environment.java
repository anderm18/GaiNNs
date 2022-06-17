package com.example.gainns;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class Environment extends Application {

    private Rectangle createFloor(Scene scene) {
        Rectangle rectangle = new Rectangle(1500, 100);
        rectangle.widthProperty().bind(scene.widthProperty()); //keep as wide as window
        rectangle.setFill(Color.valueOf("#F5E799"));
        return rectangle;
    }

    //Menu bar is good for creating for general menus, not really for drag and drop. We probably
    //should use it to make a general menu (File, Edit, Help, etc).
    
    @Override
    public void start(Stage stage) throws IOException {
    
        AnchorPane root = new AnchorPane(); //AnchorPane had better functions then border pane
        root.setStyle("-fx-background-color: #99F0F5");
        Scene scene = new Scene(root, 1500, 800);
        Rectangle floorRect = createFloor(scene); //floor
        HBox floor = new HBox(0, floorRect);
        ShapesMenu shapesMenu = new ShapesMenu(); //menu for shapes
        shapesMenu.createMenu(scene);
        HBox sMenu = new HBox(0, shapesMenu.getMenu());
        HBox tab = new HBox(0, shapesMenu.getTab()); //tab to close menu
        root.getChildren().addAll(floor, sMenu, tab);
        AnchorPane.setBottomAnchor(floor, 0d); // positioning shapes in scene									
        AnchorPane.setTopAnchor(tab, 120d);
        AnchorPane.setLeftAnchor(tab, scene.getWidth()/2.0 - shapesMenu.getTab().getWidth()/2.0);
        AnchorPane.setTopAnchor(sMenu, 0d);
        stage.setTitle("GaiNNs");
        stage.setScene(scene);
        stage.show();
        stage.widthProperty().addListener((obs, oldVal, newVal) -> { // change pos of button(tab) when window changes
    		AnchorPane.setLeftAnchor(tab, ((double)newVal)/2.0 - shapesMenu.getTab().getWidth()/2.0);
    	});
        shapesMenu.getTab().setOnAction(value ->  { //button(tab) pressed
            if(shapesMenu.tabPressed()) { // if hidden
            	//AnchorPane.setTopAnchor(tab, 0d);
                TranslateTransition tt = new TranslateTransition(Duration.millis(250), tab);
                tt.setByY(-120f);
                tt.setCycleCount(1);
                //tt.setAutoReverse(true);
                tt.play();
            }
            else {
            	//AnchorPane.setTopAnchor(tab, 120d);
                TranslateTransition tt = new TranslateTransition(Duration.millis(250), tab);
                tt.setByY(120f);
                tt.setCycleCount(1);
                tt.play();
            }
         });
    }

    public static void main(String[] args) {
        launch();
    }
}
