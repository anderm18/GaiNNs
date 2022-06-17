package com.example.gainns;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import java.io.IOException;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Mass;
//import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;

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
    	final Label reporter = new Label("Debug Info");
        AnchorPane root = new AnchorPane(); //AnchorPane had better functions then border pane
        root.setStyle("-fx-background-color: #99F0F5");
        Scene scene = new Scene(root, 1500, 800);
        Rectangle floorRect = createFloor(scene); //floor
        HBox floor = new HBox(0, floorRect);
        World<Body> world = new World<Body>();
        Body body = new Body();
        body.addFixture(Geometry.createCircle(1.0));
        body.translate(1.0, 0.0);
        body.setMass(MassType.NORMAL);
        world.addBody(body);
        AnchorPane.setBottomAnchor(floor, 0d); // positioning shapes in scene
        // Debug info - positioning
        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
              String msg =
                "Debug Info:\n(x: "       + event.getX()      + ", y: "       + event.getY()       + ") \n" +
                "(sceneX: "  + event.getSceneX() + ", sceneY: "  + event.getSceneY()  + ") \n" +
                "(screenX: " + event.getScreenX()+ ", screenY: " + event.getScreenY() + ")";

              reporter.setText(msg);
            }
          });
        
//        ShapesMenu shapesMenu = new ShapesMenu(); //menu for shapes
//        shapesMenu.createMenu(scene);
//        HBox sMenu = new HBox(0, shapesMenu.getMenu());
//        HBox tab = new HBox(0, shapesMenu.getTab()); //tab to close menu
        root.getChildren().addAll(floor, reporter);
        
        scene.setOnMousePressed(new EventHandler<MouseEvent>(){
        	@Override public void handle(MouseEvent event) {
        		//instantiateSquare();
        		instantiateSquare(event.getSceneX(), event.getSceneY());
            }

			private void instantiateSquare(double x, double y) {
				// TODO Auto-generated method stub
				
			}
		});
        
        
//        root.getChildren().addAll(floor, sMenu, tab);							
//        AnchorPane.setTopAnchor(tab, 120d);
//        AnchorPane.setLeftAnchor(tab, scene.getWidth()/2.0 - shapesMenu.getTab().getWidth()/2.0);
//        AnchorPane.setTopAnchor(sMenu, 0d);
//        
//        
//        stage.widthProperty().addListener((obs, oldVal, newVal) -> { // change pos of button(tab) when window changes
//    		AnchorPane.setLeftAnchor(tab, ((double)newVal)/2.0 - shapesMenu.getTab().getWidth()/2.0);
//    	});
//        shapesMenu.getTab().setOnAction(value ->  { //button(tab) pressed
//            if(shapesMenu.tabPressed()) { // if hidden
//            	AnchorPane.setTopAnchor(tab, 0d);
//            }
//            else {
//            	AnchorPane.setTopAnchor(tab, 120d);
//            }
//         });
        
        stage.setTitle("GaiNNs - Scence");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
