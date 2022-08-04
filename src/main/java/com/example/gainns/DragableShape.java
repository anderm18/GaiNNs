package com.example.gainns;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

class Dragable extends Group {
	
	private String myShapeName;
	private Rectangle rectangle = new Rectangle();
	private Ellipse ellipse = new Ellipse();
	private DoubleProperty widthProperty = new SimpleDoubleProperty();
	private DoubleProperty heightProperty = new SimpleDoubleProperty();
	private double rotataionDegree, centerX, centerY;
    private double rotationLengthOffsetY = 0;
    private double rotationLengthOffsetX = 0;
    
    Dragable(double x, double y, Paint fill, String shapeName, double shapeParam0, double shapeParam1){
    	this.rotataionDegree = 0.0;
    	this.myShapeName = new String (shapeName);
    	if (shapeName.equals("Rectangle")) {
			widthProperty.addListener((v, o, n) -> { rectangle.setWidth(n.doubleValue()); });
		    heightProperty.addListener((v, o, n) -> { rectangle.setHeight(n.doubleValue()); });
		    setLayoutX(x);
		    setLayoutY(y);
		    widthProperty.set(shapeParam0);
		    heightProperty.set(shapeParam1);
		    rectangle.setFill(fill);
		    getChildren().add(rectangle);
		    
		    // set transparency during moving
		    rectangle.setOnMouseDragged(me -> rectangle.setOpacity(0.7));
		    rectangle.setOnMouseReleased(me -> rectangle.setOpacity(1));
		}else if (shapeName.equals("Ellipse") || shapeName.equals("Circle")) {
			widthProperty.addListener((v, o, n) -> { ellipse.setRadiusX(n.doubleValue()/2); ellipse.setCenterX(n.doubleValue()/2);});
			heightProperty.addListener((v, o, n) -> { ellipse.setRadiusY(n.doubleValue()/2); ellipse.setCenterY(n.doubleValue()/2);});
		    setLayoutX(x);
		    setLayoutY(y);
		    widthProperty.set(shapeParam0*2);
		    if (shapeName.equals("Circle")) heightProperty.set(shapeParam0*2);
		    else heightProperty.set(shapeParam1*2);
		    ellipse.setFill(fill);
		    getChildren().add(ellipse);
		    
		    // set transparency during moving
		    ellipse.setOnMouseDragged(me -> ellipse.setOpacity(0.7));
		    ellipse.setOnMouseReleased(me ->ellipse.setOpacity(1));
		}
    }
    
    // Copy constructor
    Dragable(double x, double y, Dragable copied_element){
    	this.myShapeName = new String(copied_element.myShapeName);
    	
    }
    public String getShapeName() {
    	return new String( myShapeName );
    }
    
    public double getRotationLengthOffsetY(){
    	return this.rotationLengthOffsetY;
    }
    
    public double getRotationLengthOffsetX() {
    	return this.rotationLengthOffsetX;
    }
    
    public void setRotationLengthOffsetY(Double rotationLengthOffsetY) {
    	this.rotationLengthOffsetY = rotationLengthOffsetY;
    }
    
    public void setRotationLengthOffsetX(Double rotationLengthOffsetX) {
    	this.rotationLengthOffsetX = rotationLengthOffsetX;
    }
    
    public void setRotate(double angle, boolean needStore) {
    	this.setRotate(angle);
    	if (needStore) {
    		this.rotataionDegree = angle;
    	}
    }
    
    public double getRotationDegree() {
    	return this.rotataionDegree;
    }
    
    DoubleProperty widthProperty() { return widthProperty; }
    DoubleProperty heightProperty() { return heightProperty; }
    @Override public String toString() { return "[" + getLayoutX() + ", " + getLayoutY() + ", " + widthProperty.get() + ", " + heightProperty.get() + "]"; }
}