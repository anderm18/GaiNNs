package com.example.gainns.framework.input;

import java.awt.*;

public class BooleanStateKeyboardInputHandler extends AbstractKeyboardInputHandler {

	private boolean active;
	
	public BooleanStateKeyboardInputHandler(Component component, int key) {
		super(component, key);
		this.active = false;
	}

	@Override
	protected void onKeyPressed() {
		super.onKeyPressed();
		this.active = true;
	}
	
	@Override
	protected void onKeyReleased() {
		super.onKeyReleased();
		this.active = false;
	}
	
	@Override
	public boolean isActive() {
		return this.active;
	}
}
