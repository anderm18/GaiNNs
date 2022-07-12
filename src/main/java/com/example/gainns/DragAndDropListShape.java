package com.example.gainns;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class DragAndDropListShape{
	Shape shape;
//	double x = 12;
//	double y = 12;
	Color color;
	
//	public static void main(String[] args) {
//        launch(args);
//    }
//	
//	@Override
//    public void start(Stage primaryStage) {
	 public DragAndDropListShape(Shape shape, Color color){
		this.color = color;
		if (shape instanceof Circle) {
			Circle instanceShape = (Circle) shape;
			this.shape = new Circle(instanceShape.getRadius(), color);
		}else if (shape instanceof Rectangle) {
			Rectangle instanceShape = (Rectangle) shape;
			this.shape = new Rectangle(instanceShape.getWidth(), instanceShape.getHeight());
			this.shape.setFill(color);
		}else if (shape instanceof Ellipse) {
			Ellipse instanceShape = (Ellipse) shape;
			this.shape = new Ellipse(instanceShape.getRadiusX(), instanceShape.getRadiusY());
			this.shape.setFill(color);
		}
	}
	 
	public Shape getShape() {
		return this.shape;
	}
	
	public Color getColor() {
		return this.color;
	}
	
}
