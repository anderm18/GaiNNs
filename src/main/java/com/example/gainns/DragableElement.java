package com.example.gainns;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


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
  }
//  @Override
//  public void start(Stage primaryStage) {
//	  Group root = new Group();
//	  Scene scene = new Scene(root, 500, 260);
//
//    // circles
//    Circle redCircle = createCircle(100, 50, 30, Color.RED);
//    Circle blueCircle = createCircle(20, 150, 20, Color.BLUE);
//    Circle greenCircle = createCircle(40, 100, 40, Color.GREEN);
//
//    // add the circles
//    root.getChildren().add(redCircle);
//    root.getChildren().add(blueCircle);
//    root.getChildren().add(greenCircle);
//
//
//    // bring the circles to the front of the lines
//    redCircle.toFront();
//    blueCircle.toFront();
//    greenCircle.toFront();
//    
//    primaryStage.setTitle("Drag and Drop demo v1");
//    primaryStage.setScene(scene);
//    primaryStage.show();
//  }
//  public static void main(String[] args) {
//    launch(args);
//  }
}