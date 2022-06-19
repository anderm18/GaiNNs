package com.example.gainns;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    	
	@Override
    public void start(Stage stage) throws IOException {
        GridPane root = new GridPane();
        root.setId("root-pane");
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(this.getClass().getResource("hello-root.css").toExternalForm());
        
		stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}