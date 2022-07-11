package com.example.gainns;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

public class Dragable {

	Shape shape;
	double orgSceneX;
	double orgSceneY;
	double selfX;
	double selfY;
	boolean onPressed;
	
	private EventHandler<MouseEvent> setPoint;
	private EventHandler<MouseEvent> updatePosition;
	    
	public Dragable(Shape shape) {
	    this.shape = shape;
	    onPressed= false;
	    shape.addEventFilter(MouseEvent.MOUSE_PRESSED, setPoint);
	    shape.addEventFilter(MouseEvent.MOUSE_DRAGGED, updatePosition);
	    
	    // set actions (maybe move to a private function instead)
	    setPoint = event -> {
	    	if (event.isPrimaryButtonDown()) {
	    		onPressed = true;
	    		orgSceneX = event.getSceneX();
	    		orgSceneY = event.getSceneY();
	    		selfX = event.getX();
	    		selfY = event.getY();
            }
	    };
	    
	    updatePosition = event -> {
	    	if(onPressed) {
				this.shape.setTranslateX(event.getSceneX() - selfX);
				this.shape.setTranslateY(event.getSceneY() - selfY);
				
	    	} else {
	    		orgSceneX = selfX;
	    		orgSceneY = selfY;
	    	}
	    };
	}
	
	/*	FOR TESTING PURPOSES DONT REMOVE
	public Dragable(double x, double y, Color color, String shapeName, double shapeParam0, double shapeParam1) {
		if (shapeName.equals("Circle")) {
			this.shape = new Circle(x, y, shapeParam0, color);
		}else if (shapeName.equals("Rectangle")) {
			this.shape = new Rectangle(x, y, shapeParam0, shapeParam1);
			this.shape.setFill(color);
		}else if (shapeName.equals("Ellipse")) {
			this.shape = new Ellipse(x, y, shapeParam0, shapeParam1);
			this.shape.setFill(color);
		}
				  
		this.shape.setCursor(Cursor.HAND);

		this.shape.setOnMousePressed((t) -> {
			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();
			
			Shape c = (Shape) (t.getSource());
			c.setOpacity(0.5);
			c.toFront();
	    });
		
		this.shape.setOnMouseDragged((t) -> {    				
			Shape movingShape = (Shape) (t.getSource());
			movingShape.relocate(t.getSceneX(), t.getSceneY());
			
			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();
	    });
		
		this.shape.setOnMouseReleased((t) -> {
			Shape c = (Shape) (t.getSource());
			c.setOpacity(80);
	    });
	}*/
}