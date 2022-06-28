package com.example.gainns;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Screen;
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
	private List<Dragable> shapesInEnv = new ArrayList<Dragable>();
	
	// resolution offset
	private double screenWidth = Screen.getPrimary().getBounds().getWidth() - 400;
    private double screenHeight = Screen.getPrimary().getBounds().getHeight() - 300;
	
    private Rectangle createFloor(Scene scene) {
        Rectangle rectangle = new Rectangle(screenWidth, 100);
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
        Scene scene = new Scene(root, screenWidth, screenHeight);
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
        
        // track mouse info all the time
        root.setOnMousePressed(e -> {
            updateMousePosition(e);
        });
        root.setOnMouseDragged(e -> {
            updateMousePosition(e);
        });
        root.setOnMouseMoved(e -> {
            updateMousePosition(e);
        });
        shapesMenu.getMenu().shapeVisualizedList.setOnMousePressed(e -> {
            updateMousePosition(e);
        });
        shapesMenu.getMenu().shapeVisualizedList.setOnMouseDragged(e -> {
            updateMousePosition(e);
        });
        shapesMenu.getMenu().shapeVisualizedList.setOnMouseMoved(e -> {
            updateMousePosition(e);
        });
        
       
        // HIDE/SHOW
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
        
        
        // Shape drag and drop
        scene.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
            	updateMousePosition(event);
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                event.consume();
            }
        });

        scene.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            String shapeInfo = db.getString().replace("[", "!!").replace("]", "!!"); // avoid parsing bug in JAVA 1.8 windows
//            System.out.println("Select: " + shapeInfo.split("!!")[0]);
            Dragable newAddedElement = new Dragable(mousePosXTraker, mousePosYTraker, Color.web(shapeInfo.split("fill=")[1].split("!!")[0]), 
            										shapeInfo.split("!!")[0], Double.parseDouble(db.getString().split(", ")[2].split("=")[1]), 
            										Double.parseDouble(shapeInfo.split(", ")[3].split("=")[0].equals("fill")? "0":shapeInfo.split(", ")[3].split("=")[1]));
            shapesInEnv.add(newAddedElement);
            elementInEnvReporter.setText("Current element count in env: " + shapesInEnv.size());
            root.getChildren().add(newAddedElement.shape);
            if (db.hasString()) {
                System.out.println("Dropped: " + db.getString());
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
        
//        shapesMenu.getMenu().shapeVisualizedList.getSelectionModel().selectedItemProperty().addListener(
//	            new ChangeListener<DragAndDropListShape>() {
//	                public void changed(ObservableValue<? extends DragAndDropListShape> ov, 
//	                		DragAndDropListShape old_val, DragAndDropListShape new_val) {
//	                	System.out.println("Current color: "+ new_val.getColor());
//	                	Dragable newAddedElement = new Dragable(mousePosXTraker, mousePosYTraker, new_val.getColor(), new_val.getShape());
//	                	shapesInEnv.add(newAddedElement);
//	                	elementInEnvReporter.setText("Current element count in env: " + shapesInEnv.size());
//	                	root.getChildren().add(newAddedElement.shape);
//	            }
//	     });
        
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
    
    public void updateMousePosition(InputEvent event) {
    	if (event instanceof MouseEvent ) {
			MouseEvent eventInstance = (MouseEvent) event;
    		mousePosXTraker = eventInstance.getX();
            mousePosYTraker = eventInstance.getY();
            posReporter.setText("Debug Info:\n(x: " + eventInstance.getX() + ", y: " + eventInstance.getY() + ") \n" );
    	}else if (event instanceof DragEvent) {
    		DragEvent eventInstance = (DragEvent) event;
    		mousePosXTraker = eventInstance.getX();
            mousePosYTraker = eventInstance.getY();
            posReporter.setText("Debug Info:\n(x: " + eventInstance.getX() + ", y: " + eventInstance.getY() + ") \n" );
    	}
    	
    }
}
