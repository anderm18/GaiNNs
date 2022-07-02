package com.example.gainns;

import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.paint.Color;
//import javafx.animation.Transition.*;
import javafx.util.Duration;
import javafx.util.Pair;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListCell;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Screen;
import javafx.util.Callback;

public class ShapesMenu {
		
		// private Rectangle menu = new Rectangle();
		private shapeContainer items = new shapeContainer();
		private Button tab = new Button("HIDE");
		private boolean hidden = false;
	
	public void createMenu(Scene scene) { //set up shape proportions and color for menu
//		menu.setWidth(1500);
//		menu.setHeight(120);
//		menu.widthProperty().bind(scene.widthProperty());
//		menu.setFill(Color.valueOf("#4C4C4C"));
//		tab.setStyle("-fx-background-color: #4C4C4C; -fx-text-fill: #FFFFFF");
		
	}
	public shapeContainer getMenu() {
		return items;
	}
	public Button getTab() {
		return tab;
	}
	public boolean isHidden() {
		return hidden;
	}
	
	public boolean tabPressed() { // if tab pressed
		TranslateTransition tt = new TranslateTransition(Duration.millis(250), this.items.shapeVisualizedList);
		if(hidden) {
			//menu.setHeight(120);
			tt.setByY(120f);
			tt.setCycleCount(1);
			//tt.setAutoReverse(true);
			tt.play();
			hidden = false;
			tab.setText("HIDE");
			tab.setStyle("-fx-color: white; ");
			
		}
		else {
			//menu.setHeight(0);
			tt.setByY(-120.0f);
			tt.setCycleCount(1);
			//tt.setAutoReverse(true);
			tt.play();
			hidden = true;
			tab.setText("SHOW");
			tab.setStyle("-fx-color: black; ");
		}

		return hidden;
	}
	
	// Add functions for adding shapes to menu or store shapes as private variables
}

class shapeContainer {
	public ListView<Pair<Shape, Color>> shapeVisualizedList;
	@SuppressWarnings("unchecked")
	ObservableList<Pair<Shape, Color>> data = FXCollections.observableArrayList(new Pair<>(new Circle(20), Color.BLUE), 
																				new Pair<>(new Rectangle(50, 50), Color.RED), 
																				new Pair<>(new Rectangle(100, 50), Color.PINK), 
																				new Pair<>(new Ellipse(50, 20), Color.GREEN),
																				new Pair<>(new Circle(20), Color.BLUE), 
																				new Pair<>(new Rectangle(50, 50), Color.RED), 
																				new Pair<>(new Rectangle(100, 50), Color.PINK), 
																				new Pair<>(new Ellipse(50, 20), Color.GREEN),
																				new Pair<>(new Circle(20), Color.BLUE), 
																				new Pair<>(new Rectangle(50, 50), Color.RED), 
																				new Pair<>(new Rectangle(100, 50), Color.PINK), 
																				new Pair<>(new Ellipse(50, 20), Color.GREEN),
																				new Pair<>(new Circle(20), Color.BLUE), 
																				new Pair<>(new Rectangle(50, 50), Color.RED), 
																				new Pair<>(new Rectangle(100, 50), Color.PINK), 
																				new Pair<>(new Ellipse(50, 20), Color.GREEN),
																				new Pair<>(new Circle(20), Color.BLUE), 
																				new Pair<>(new Rectangle(50, 50), Color.RED), 
																				new Pair<>(new Rectangle(100, 50), Color.PINK), 	
																				new Pair<>(new Ellipse(50, 20), Color.GREEN));
	
	//temp debug
	final Label label = new Label();
	public shapeContainer() {
		
		this.shapeVisualizedList = new ListView<>();
		// scroll-bar
		shapeVisualizedList.setId("listview");
		shapeVisualizedList.getStylesheets().addAll(this.getClass().getResource("scrollbar.css").toExternalForm());
		
		shapeVisualizedList.setItems(data);
		// TO-DO: write an event listener to detect when window is resized. Use setPrefWidth
		shapeVisualizedList.setPrefWidth(Screen.getPrimary().getBounds().getWidth() - 400);  
		shapeVisualizedList.setPrefHeight(120);
		shapeVisualizedList.setOrientation(Orientation.HORIZONTAL);
		shapeVisualizedList.setStyle(/**/
									"-fx-control-inner-background: #4C4C4C; "
				                   + "-fx-control-inner-background-alt: derive(-fx-control-inner-background, 20%); "   // change the color!!
								   + "-fx-opacity: 0.5;"
				                   );
		label.setText("[The shape content does not represent the final choice]\n");
				
		shapeVisualizedList.setCellFactory(new Callback<ListView<Pair<Shape, Color>>, 
	            ListCell<Pair<Shape, Color>>>() {
	                @Override 
	                public ListCell<Pair<Shape, Color>> call(ListView<Pair<Shape, Color>> list) {
	                    return new ColorCell();
	                }
	            }
	        );
	 
		shapeVisualizedList.getSelectionModel().selectedItemProperty().addListener(
	            new ChangeListener<Pair<Shape, Color>>() {
	                public void changed(ObservableValue<? extends Pair<Shape,Color>> ov, 
	                		Pair<Shape,Color> old_val, Pair<Shape,Color> new_val) {
	                        label.setText("[The shape content does not represent the final choice]"
	                        		+ "\nDebug Info: Select " + new_val.getKey().toString());
	                        label.setTextFill(Color.web(new_val.getValue().toString()));
	            }
	        });
	        
	        // box.getChildren().addAll(list, label);
//	        stage.show();
	    }
		    
	    static class ColorCell extends ListCell<Pair<Shape, Color>> {
	        @Override
	        public void updateItem(Pair<Shape, Color> item, boolean empty) {
	            super.updateItem(item, empty);
	            if (item != null) {
	            	item.getKey().setFill(Color.web(item.getValue().toString()));
	                setGraphic(item.getKey());
	            }
	        }
	    }
}
	
	
//	@Override
//	public void start(Stage primaryStage) {
//		ListView<String> listView = new ListView<>();
//		ObservableList<String> items = FXCollections.observableArrayList("Cricket", "Chess", "Kabaddy", "Badminton",
//		"Football", "Golf", "CoCo", "car racing");
//		listView.setItems(items);
//		  
//		listView.setPrefWidth(400);
//		listView.setPrefHeight(50);
//		listView.setOrientation(Orientation.HORIZONTAL);
//		
//		HBox hBox = new HBox(listView);
//		
//		Scene scene = new Scene(hBox, 500, 200);
//		
//		/* Set the scene to primaryStage, and call the show method */
//		primaryStage.setTitle("JavaFX ListView app Example");
//		primaryStage.setScene(scene);
//		primaryStage.show();
//	}

