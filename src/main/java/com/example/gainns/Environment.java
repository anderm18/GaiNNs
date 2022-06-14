package com.example.gainns;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class Environment extends Application {



    private Rectangle createFloor() {
        Rectangle rectangle = new Rectangle( 150000, 100);
        rectangle.setFill(Color.valueOf("#F5E799"));
        return rectangle;
    }

    private VBox createMenu() {

        VBox mu = new VBox();
        mu.setTranslateX(-190);
        TranslateTransition translate = new TranslateTransition(Duration.millis(500), mu);
        translate.setFromX(-190);
        translate.setToX(0);


        mu.setOnMouseEntered(evt -> {
            translate.setRate(1);
            translate.play();
        });
        mu.setOnMouseExited(evt -> {
            translate.setRate(-1);
            translate.play();
        });

        return mu;
    }

    @Override
    public void start(Stage stage) throws IOException {
        StackPane root = new StackPane();
        BorderPane floor = new BorderPane();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Rectangle floorRect = createFloor();
        floor.setBottom(floorRect);
        floor.setTop(createMenu());
        root.getChildren().addAll(floor);
        root.setStyle("-fx-background-color: #99F0F5");
        Scene scene = new Scene(root, 1500, 800);
        stage.setTitle("GaiNNs");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
