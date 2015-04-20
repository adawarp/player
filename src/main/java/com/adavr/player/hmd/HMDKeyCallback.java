/*
 * Copyright (C) 2015 Shotaro Uchida <fantom@xmaker.mx>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.adavr.player.hmd;

import com.adavr.player.DefaultKeyCallback;
import org.lwjgl.glfw.GLFW;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class HMDKeyCallback extends DefaultKeyCallback {

	public static final float CAMERA_ADJUST_STEP = 0.1f;
	
	private final HMDRenderContext hmd;
	
	public HMDKeyCallback(HMDRenderContext hmd) {
		this.hmd = hmd;
	}
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		super.invoke(window, key, scancode, action, mods);
		if (action == GLFW.GLFW_PRESS) {
			switch (key) {
			case GLFW.GLFW_KEY_W:
				hmd.updateCamera(0, 0, CAMERA_ADJUST_STEP);
				break;
			case GLFW.GLFW_KEY_S:
				hmd.updateCamera(0, 0, -CAMERA_ADJUST_STEP);
				break;
			case GLFW.GLFW_KEY_A:
				hmd.updateCamera(CAMERA_ADJUST_STEP, 0, 0);
				break;
			case GLFW.GLFW_KEY_D:
				hmd.updateCamera(-CAMERA_ADJUST_STEP, 0, 0);
				break;
			case GLFW.GLFW_KEY_X:
				hmd.setHeadTrackingEnabled(!hmd.isHeadTrackingEnabled());
				break;
			case GLFW.GLFW_KEY_UP:
				hmd.updateCamera(0, -CAMERA_ADJUST_STEP, 0);
				break;
			case GLFW.GLFW_KEY_DOWN:
				hmd.updateCamera(0, CAMERA_ADJUST_STEP, 0);
				break;
			case GLFW.GLFW_KEY_R:
				hmd.resetCamera();
				break;
			}
		}
	}

}
