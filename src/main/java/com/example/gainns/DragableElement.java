package com.example.gainns;

import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;


public class DragableElement {

  Circle circle;
  double orgSceneX;
  double orgSceneY;
  SimulationBody object;

  public DragableElement(double x, double y, double r, Color color) {
    this.circle = new Circle(x, y, r, color);
    object = new SimulationBody();
    object.addFixture(circle);


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