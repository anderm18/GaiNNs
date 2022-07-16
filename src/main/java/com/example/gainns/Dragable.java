package com.example.gainns;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

class Dragable extends Group {

    Rectangle rectangle = new Rectangle();
    Ellipse ellipse = new Ellipse();
    Circle circle = new Circle();
    DoubleProperty widthProperty = new SimpleDoubleProperty();
    DoubleProperty heightProperty = new SimpleDoubleProperty();
    Dragable(double x, double y, Paint fill, String shapeName, double shapeParam0, double shapeParam1){
    	if (shapeName.equals("Rectangle")) {
			widthProperty.addListener((v, o, n) -> { rectangle.setWidth(n.doubleValue()); });
		    heightProperty.addListener((v, o, n) -> { rectangle.setHeight(n.doubleValue()); });
		    setLayoutX(x);
		    setLayoutY(y);
		    widthProperty.set(shapeParam0);
		    heightProperty.set(shapeParam1);
		    rectangle.setFill(fill);
		    getChildren().add(rectangle);
		}else if (shapeName.equals("Ellipse")) {
			widthProperty.addListener((v, o, n) -> { ellipse.setRadiusX(n.doubleValue()/2); ellipse.setCenterX(n.doubleValue()/2);});
			heightProperty.addListener((v, o, n) -> { ellipse.setRadiusY(n.doubleValue()/2); ellipse.setCenterY(n.doubleValue()/2);});
		    setLayoutX(x);
		    setLayoutY(y);
		    widthProperty.set(shapeParam0*2);
		    heightProperty.set(shapeParam1*2);
		    ellipse.setFill(fill);
		    getChildren().add(ellipse);
		}else if (shapeName.equals("Circle")) {
			widthProperty.addListener((v, o, n) -> { ellipse.setRadiusX(n.doubleValue()/2); ellipse.setCenterX(n.doubleValue()/2);});
			heightProperty.addListener((v, o, n) -> { ellipse.setRadiusY(n.doubleValue()/2); ellipse.setCenterY(n.doubleValue()/2);});
		    setLayoutX(x);
		    setLayoutY(y);
		    widthProperty.set(shapeParam0*2);
		    heightProperty.set(shapeParam0*2);
		    ellipse.setFill(fill);
		    getChildren().add(ellipse);
		}
    }
    
    DoubleProperty widthProperty() { return widthProperty; }
    DoubleProperty heightProperty() { return heightProperty; }
    @Override public String toString() { return "[" + getLayoutX() + ", " + getLayoutY() + ", " + widthProperty.get() + ", " + heightProperty.get() + "]"; }
}