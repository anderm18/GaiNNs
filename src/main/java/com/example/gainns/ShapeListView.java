package com.example.gainns;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
 
public class ShapeListView extends ListView<Pair<Shape, Color>>{
 
    ListView<Pair<Shape, Color>> list = new ListView<Pair<Shape, Color>>();
    @SuppressWarnings("unchecked")
	// Pair<Integer, String> pair = new Pair<>(1, "One");
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
    final Label label = new Label();
 
    public ShapeListView() {
        VBox box = new VBox();
//        Scene scene = new Scene(box, 200, 200);
//        stage.setScene(scene);
//        stage.setTitle("ListViewSample");
//        
    	// VBox box = new VBox();
        VBox.setVgrow(list, Priority.ALWAYS);
        label.setLayoutX(10);
        label.setLayoutY(115);
        label.setText("[The shape content does not represent the final choice]\nDebug Info: ");
        list.setItems(data);
        
        list.setCellFactory(new Callback<ListView<Pair<Shape, Color>>, 
            ListCell<Pair<Shape, Color>>>() {
                @Override 
                public ListCell<Pair<Shape, Color>> call(ListView<Pair<Shape, Color>> list) {
                    return new ColorCell();
                }
            }
        );
 
        list.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Pair<Shape, Color>>() {
                public void changed(ObservableValue<? extends Pair<Shape,Color>> ov, 
                		Pair<Shape,Color> old_val, Pair<Shape,Color> new_val) {
                        label.setText("[The shape content does not represent the final choice]"
                        		+ "\nDebug Info: Select " + new_val.getKey().toString());
                        label.setTextFill(Color.web(new_val.getValue().toString()));
            }
        });
        
        box.getChildren().addAll(list, label);
//        stage.show();
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