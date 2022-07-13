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
	double orgTranslateX;
	double orgTranslateY;
	ShapesMenu shapesMenu;
	
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
		
		this.shapesMenu = shapesMenu;
		this.shape.setCursor(Cursor.HAND);

		this.shape.setOnMousePressed((t) -> {		
			Shape c = (Shape) (t.getSource());
			c.setOpacity(0.5);
			c.toFront();
			
			orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
            orgTranslateX = ((Shape)(t.getSource())).getTranslateX();
            orgTranslateY = ((Shape)(t.getSource())).getTranslateY();

	    });
		
		this.shape.setOnMouseDragged((t) -> {
			((Shape)(t.getSource())).setTranslateX(orgTranslateX + t.getSceneX() - orgSceneX);
			((Shape)(t.getSource())).setTranslateY(orgTranslateY + t.getSceneY() - orgSceneY);
		
	    });
		
		this.shape.setOnMouseReleased((t) -> {
			Shape c = (Shape) (t.getSource());
			c.setViewOrder(1);
			c.setOpacity(80);
	    });
	}
}