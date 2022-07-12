package com.example.gainns;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
import javafx.scene.text.Text;
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
		}
		else {
			//menu.setHeight(0);
			tt.setByY(-120.0f);
			tt.setCycleCount(1);
			//tt.setAutoReverse(true);
			tt.play();
			hidden = true;
			tab.setText("SHOW");
		}

		return hidden;
	}
	
	// Add functions for adding shapes to menu or store shapes as private variables
}

class shapeContainer {
	public ListView<DragAndDropListShape> shapeVisualizedList;
	@SuppressWarnings("unchecked")
	ObservableList<DragAndDropListShape> data = FXCollections.observableArrayList(new DragAndDropListShape(new Circle(20), Color.BLUE), 
																				new DragAndDropListShape(new Rectangle(50, 50), Color.RED), 
																				new DragAndDropListShape(new Rectangle(100, 50), Color.PINK),
																				new DragAndDropListShape(new Ellipse(50, 20), Color.GREEN),
																				new DragAndDropListShape(new Circle(20), Color.YELLOW), 
																				new DragAndDropListShape(new Rectangle(50, 50), Color.AQUA), 
																				new DragAndDropListShape(new Rectangle(60, 50), Color.BURLYWOOD),
																				new DragAndDropListShape(new Ellipse(59, 20), Color.CADETBLUE),
																				new DragAndDropListShape(new Circle(20), Color.DARKCYAN), 
																				new DragAndDropListShape(new Rectangle(10, 40), Color.DARKKHAKI), 
																				new DragAndDropListShape(new Rectangle(50, 80), Color.DARKSLATEBLUE),
																				new DragAndDropListShape(new Ellipse(50, 20), Color.KHAKI),
																				new DragAndDropListShape(new Circle(20), Color.RED), 
																				new DragAndDropListShape(new Rectangle(50, 50), Color.AQUA), 
																				new DragAndDropListShape(new Rectangle(60, 50), Color.BURLYWOOD),
																				new DragAndDropListShape(new Ellipse(59, 20), Color.RED),
																				new DragAndDropListShape(new Circle(20), Color.DARKCYAN),
																				new DragAndDropListShape(new Circle(20), Color.YELLOW), 
																				new DragAndDropListShape(new Rectangle(50, 50), Color.SKYBLUE), 
																				new DragAndDropListShape(new Rectangle(60, 50), Color.BURLYWOOD),
																				new DragAndDropListShape(new Ellipse(59, 20), Color.CADETBLUE),
																				new DragAndDropListShape(new Circle(20), Color.BLUE));
	
	//temp debug
	final Label label = new Label();
	
	public shapeContainer() {
		this.shapeVisualizedList = new ListView<>();	
		shapeVisualizedList.setItems(data);
		shapeVisualizedList.setPrefWidth(Screen.getPrimary().getBounds().getWidth() - 400);  // TO-DO: make change?
		shapeVisualizedList.setPrefHeight(120);
		shapeVisualizedList.setOrientation(Orientation.HORIZONTAL);
		shapeVisualizedList.setStyle("-fx-control-inner-background: #4C4C4C; "
				                   + "-fx-control-inner-background-alt: derive(-fx-control-inner-background, 20%)");   // change the color!!
		
		label.setText("[The shape content does not represent the final choice]\n");
		
		
		shapeVisualizedList.setCellFactory(new Callback<ListView<DragAndDropListShape>, 
				                                        ListCell<DragAndDropListShape>>() {
	                @Override 
	                public ListCell<DragAndDropListShape> call(ListView<DragAndDropListShape> list) {
	                    return new ColorCell();
	                }
	            }
	        );
	 
		shapeVisualizedList.getSelectionModel().selectedItemProperty().addListener(
	            new ChangeListener<DragAndDropListShape>() {
	                public void changed(ObservableValue<? extends DragAndDropListShape> ov, 
	                		DragAndDropListShape old_val, DragAndDropListShape new_val) {
	                        label.setText("[The shape content does not represent the final choice]"
	                        		+ "\nDebug Info: Select " + new_val.getShape().toString());
	                        label.setTextFill(Color.web(new_val.getColor().toString()));
	            }
	        });
	        
	        // box.getChildren().addAll(list, label);
//	        stage.show();
	    }
	    
	    static class ColorCell extends ListCell<DragAndDropListShape> {
	        @Override
	        public void updateItem(DragAndDropListShape item, boolean empty) {
	            super.updateItem(item, empty);
	            if (item != null) {
	            	item.getShape().setFill(Color.web(item.getColor().toString()));
	                setGraphic(item.getShape());
	                item.getShape().setOnDragDetected((MouseEvent event) -> {              
	                    Dragboard db = item.getShape().startDragAndDrop(TransferMode.ANY);
	                    SnapshotParameters sp = new SnapshotParameters();
	                    sp.setFill(Color.TRANSPARENT);
	                    db.setDragView(item.getShape().snapshot(sp, null), event.getX(), event.getY());
	                    ClipboardContent content = new ClipboardContent();
	                    content.putString(item.getShape().toString());
	                    db.setContent(content);
	                });
	        		
	                item.getShape().setOnMouseDragged((MouseEvent event) -> {
	                    event.setDragDetect(true);
	                });
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

