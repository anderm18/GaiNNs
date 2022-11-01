package com.example.gainns.framework.input;

import com.example.gainns.framework.Camera;

import java.awt.*;

public class MouseZoomInputHandler extends AbstractMouseInputHandler implements InputHandler {

	public MouseZoomInputHandler(Component component, Camera camera, int button) {
		super(component, camera, button);
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	protected void onMouseWheel(double rotation) {
		super.onMouseWheel(rotation);
		
		if (rotation > 0) {
			this.camera.scale *= 0.8;
		} else {
			this.camera.scale *= 1.2;
		}
	}
}
