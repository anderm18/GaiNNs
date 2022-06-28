package com.example.gainns;

import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

public class Dragable {

	Shape shape;
	double orgSceneX;
	double orgSceneY;
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
			c.setFill(color.brighter());
	    });
	}
}