package com.example.gainns;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
	
	private Label welcomeText = new Label();
	protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }	
	
	private VBox RootSetUp() {
		VBox root = new VBox();
        root.setId("root-pane");
//        root.setGridLinesVisible(true);
        
        welcomeText.setAlignment(Pos.CENTER);
        root.getChildren().add(welcomeText);
        
        Button hello_button = new Button("Hello!");
        hello_button.setOnAction(value -> { onHelloButtonClick(); } );
        hello_button.setAlignment(Pos.CENTER);
        
        root.getChildren().add(hello_button);
        
        return root;
	}
	
	@Override
    public void start(Stage stage) throws IOException {
        Pane root = RootSetUp(); 
                
        Scene scene = new Scene(root, 500, 500);
        scene.getStylesheets().addAll(this.getClass().getResource("hello-root.css").toExternalForm());
        
		stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}