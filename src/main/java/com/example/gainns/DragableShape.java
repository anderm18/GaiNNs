package com.example.gainns;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

class Dragable extends Group {
	
	private String shapeName;
	private Rectangle rectangle = new Rectangle();
	private Ellipse ellipse = new Ellipse();
	private DoubleProperty widthProperty = new SimpleDoubleProperty();
	private DoubleProperty heightProperty = new SimpleDoubleProperty();
	private double rotataionDegree, centerX, centerY;
    private double rotationLengthOffsetY = 0;
    private double rotationLengthOffsetX = 0;
    
    Dragable(double x, double y, Paint fill, String shapeName, double shapeParam0, double shapeParam1){
    	this.rotataionDegree = 0.0;
    	this.shapeName = new String (shapeName);
    	if (shapeName.equals("Rectangle")) {
    		this.widthProperty.addListener((v, o, n) -> { rectangle.setWidth(n.doubleValue()); });
			this.heightProperty.addListener((v, o, n) -> { rectangle.setHeight(n.doubleValue()); });
		    setLayoutX(x - 0.5*shapeParam0);
		    setLayoutY(y - 0.5*shapeParam1);
		    this.widthProperty.set(shapeParam0);
		    this.heightProperty.set(shapeParam1);
		    this.rectangle.setFill(fill);
		    getChildren().add(rectangle);
		    
		    // set transparency during moving
		    this.rectangle.setOnMouseDragged(me -> rectangle.setOpacity(0.7));
		    this.rectangle.setOnMouseReleased(me -> rectangle.setOpacity(1));
		}else if (shapeName.equals("Ellipse") || shapeName.equals("Circle")) {
			this.widthProperty.addListener((v, o, n) -> { this.ellipse.setRadiusX(n.doubleValue()/2); 
														  this.ellipse.setCenterX(n.doubleValue()/2);});
			this.heightProperty.addListener((v, o, n) -> { this.ellipse.setRadiusY(n.doubleValue()/2); 
														   this.ellipse.setCenterY(n.doubleValue()/2);});
		    setLayoutX(x - shapeParam0);
		    setLayoutY(y - shapeParam0);
		    this.widthProperty.set(shapeParam0*2);
		    if (shapeName.equals("Circle")) this.heightProperty.set(shapeParam0*2);
		    else this.heightProperty.set(shapeParam1*2);
		    this.ellipse.setFill(fill);
		    getChildren().add(ellipse);
		    
		    // set transparency during moving
		    this.ellipse.setOnMouseDragged(me -> ellipse.setOpacity(0.7));
		    this.ellipse.setOnMouseReleased(me ->ellipse.setOpacity(1));
		}
    	// System.out.println("Set center X: " + x + " so assigned: " + getLayoutX());
    	this.centerX = x;
	    this.centerY = y;
    }
    
    // Copy constructor
    Dragable(double x, double y, Dragable copied_element){
    	this.shapeName = new String(copied_element.shapeName);
    	
    }
    public String getShapeName() {
    	return new String( shapeName );
    }
    
    public double getRotationLengthOffsetY(){
    	return this.rotationLengthOffsetY;
    }
    
    public double getRotationLengthOffsetX() {
    	return this.rotationLengthOffsetX;
    }
    
    public double getCenterX() {
    	return this.centerX;
    }
    
    public double getCenterY() {
    	return this.centerY;
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
    
    public void setCenterX(double centerX) {
    	this.centerX = centerX;
    }
    
    public void setCenterY(double centerY) {
    	this.centerY = centerY;
    }
    
    public double getRotationDegree() {
    	return this.rotataionDegree;
    }
    
    public void autoCenterUpdate() {
    	if (shapeName.equals("Rectangle")) {
    		this.centerX = getLayoutX() + 0.5*this.rectangle.getWidth();
    		this.centerY = getLayoutY() + 0.5*this.rectangle.getHeight();
    	}else {
    		this.centerX = getLayoutX() + this.ellipse.getRadiusX();
    		this.centerY = getLayoutY() + this.ellipse.getRadiusY();
    	}
		
	}
    
    DoubleProperty widthProperty() { return widthProperty; }
    DoubleProperty heightProperty() { return heightProperty; }
    @Override public String toString() { return "[" + getLayoutX() + ", " + getLayoutY() + ", " + widthProperty.get() + ", " + heightProperty.get() + "]"; }

	
}