package com.example.gainns;

import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class DragableElement {

  Circle circle;
  double orgSceneX;
  double orgSceneY;

  public DragableElement(double x, double y, double r, Color color) {
    this.circle = new Circle(x, y, r, color);

    circle.setCursor(Cursor.HAND);

    circle.setOnMousePressed((t) -> {
      orgSceneX = t.getSceneX();
      orgSceneY = t.getSceneY();

      Circle c = (Circle) (t.getSource());
      c.toFront();
    });
    circle.setOnMouseDragged((t) -> {
    	
      double offsetX = t.getSceneX() - orgSceneX;
      double offsetY = t.getSceneY() - orgSceneY;

      Circle c = (Circle) (t.getSource());

      c.setCenterX(c.getCenterX() + offsetX);
      c.setCenterY(c.getCenterY() + offsetY);

      orgSceneX = t.getSceneX();
      orgSceneY = t.getSceneY();
    });
    circle.setOnMouseReleased((t) -> {
    	Circle c = (Circle) (t.getSource());
    	c.setFill(color.brighter());
    });
  }
}