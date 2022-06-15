package com.example.gainns;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class ShapesMenu {
	
		private Rectangle menu = new Rectangle();
		private Button tab = new Button("HIDE");
		private boolean hidden = false;
	
	public void createMenu(Scene scene) { //set up shape proportions and color for menu
		menu.setWidth(1500);
		menu.setHeight(120);
		menu.widthProperty().bind(scene.widthProperty());
		menu.setFill(Color.valueOf("#4C4C4C"));
		tab.setStyle("-fx-background-color: #4C4C4C; -fx-text-fill: #FFFFFF");
		
	}
	public Rectangle getMenu() {
		return menu;
	}
	public Button getTab() {
		return tab;
	}	
	public boolean tabPressed() { // if tab pressed
		if(hidden) {
			menu.setHeight(120);
			hidden = false;
			tab.setText("HIDE");
		}
		else {
			menu.setHeight(0);
			hidden = true;
			tab.setText("SHOW");
		}
		return hidden;
	}
	
	// Add functions for adding shapes to menu or store shapes as private variables
}
