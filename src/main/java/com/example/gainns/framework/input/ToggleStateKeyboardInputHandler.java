package com.example.gainns.framework.input;

import java.awt.*;

public class ToggleStateKeyboardInputHandler extends AbstractKeyboardInputHandler {

	private boolean active;
	
	public ToggleStateKeyboardInputHandler(Component component, int key) {
		super(component, key);
		this.active = false;
	}

	@Override
	protected void onKeyPressed() {
		super.onKeyPressed();
		this.active = !this.active;
	}
	
	public void setActive(boolean flag) {
		this.active = flag;
	}
	
	@Override
	public boolean isActive() {
		return this.active;
	}
}
