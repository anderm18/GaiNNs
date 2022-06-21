package com.example.gainns;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Environment extends Application {
	
	//temp debug
	final Label posReporter = new Label();
	final Label elementInEnvReporter = new Label();
	// mouse tracker
	private double mousePosXTraker = 0;
	private double mousePosYTraker = 0;
	private List<DragableElement> shapesInEnv = new ArrayList<DragableElement>();
	
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
        Scene scene = new Scene(root, 800, 600);
        Rectangle floorRect = createFloor(scene); //floor
        HBox floor = new HBox(0, floorRect);
        ShapesMenu shapesMenu = new ShapesMenu(); //menu for shapes
        shapesMenu.createMenu(scene);
        HBox sMenu = new HBox(0, shapesMenu.getMenu().shapeVisualizedList);
        HBox tab = new HBox(0, shapesMenu.getTab()); //tab to close menu
        root.getChildren().addAll(floor, sMenu, tab, posReporter, elementInEnvReporter);
        AnchorPane.setBottomAnchor(floor, 0d); // positioning shapes in scene									
        AnchorPane.setTopAnchor(tab, 120d);
        AnchorPane.setTopAnchor(posReporter, 620d);
        AnchorPane.setTopAnchor(elementInEnvReporter, 420d);
        AnchorPane.setLeftAnchor(tab, scene.getWidth()/2.0 - shapesMenu.getTab().getWidth()/2.0);
        AnchorPane.setTopAnchor(sMenu, 0d);
        
        root.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	mousePosXTraker = event.getX();
                mousePosYTraker = event.getY();
                posReporter.setText("Debug Info:\n(x: " + event.getX() + ", y: " + event.getY() + ") \n" +
              		  			  "(sceneX: "  + event.getSceneX() + ", sceneY: "  + event.getSceneY()  + ") \n" +
              		  			  "(screenX: " + event.getScreenX()+ ", screenY: " + event.getScreenY() + ")");
            }
        });
        shapesMenu.getMenu().shapeVisualizedList.setOnMouseMoved(new EventHandler<MouseEvent>() {
        	@Override
            public void handle(MouseEvent event) {
            	mousePosXTraker = event.getX();
                mousePosYTraker = event.getY();
                posReporter.setText("Debug Info:\n(x: " + event.getX() + ", y: " + event.getY() + ") \n" +
              		  			  "(sceneX: "  + event.getSceneX() + ", sceneY: "  + event.getSceneY()  + ") \n" +
              		  			  "(screenX: " + event.getScreenX()+ ", screenY: " + event.getScreenY() + ")");
            }
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
        
        shapesMenu.getMenu().shapeVisualizedList.getSelectionModel().selectedItemProperty().addListener(
	            new ChangeListener<Pair<Shape, Color>>() {
	                public void changed(ObservableValue<? extends Pair<Shape,Color>> ov, 
	                		Pair<Shape,Color> old_val, Pair<Shape,Color> new_val) {
	                	DragableElement newAddedElement = new DragableElement(mousePosXTraker, mousePosYTraker, 40, new_val.getValue());
	                	shapesInEnv.add(newAddedElement);
	                	elementInEnvReporter.setText("Current element count in env: " + shapesInEnv.size());
	                	root.getChildren().add(newAddedElement.circle);
	            }
	     });
        
        stage.setTitle("GaiNNs");
        stage.setScene(scene);
        stage.show();
        stage.widthProperty().addListener((obs, oldVal, newVal) -> { // change pos of button(tab) when window changes
    		AnchorPane.setLeftAnchor(tab, ((double)newVal)/2.0 - shapesMenu.getTab().getWidth()/2.0);
    	});
        
        
    }
    
    
    public static void main(String[] args) {
        launch();
    }
}
