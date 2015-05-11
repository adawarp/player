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
package com.adavr.player.example;

import com.adavr.player.Utils;
import com.adavr.player.context.RenderContext;
import com.adavr.player.context.SceneRenderContext;
import com.adavr.player.hmd.HMDRenderContext;
import com.adavr.player.hmd.HMDStatusListener;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import org.saintandreas.math.Matrix4f;
import org.saintandreas.math.Vector3f;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class DummyHMDRenderContext implements RenderContext, HMDRenderContext {

	private SceneRenderContext ctx;
	private Matrix4f projectionMatrix;
	private Vector3f modelPos;
	private Vector3f modelAngle;
	private Vector3f modelScale;
	private Vector3f cameraPos;

	public DummyHMDRenderContext(SceneRenderContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public void setup() {
		ctx.setup();
		projectionMatrix = Utils.setupProjectionMatrix(60, 1.77, 100, 0.1);
		modelPos = new Vector3f(0, 0, 0);
		modelAngle = new Vector3f(0, 0, 0);
		modelScale = new Vector3f(1, 1, 1);
		resetCamera();
	}

	@Override
	public void resetCamera() {
		cameraPos = new Vector3f(0, 0, -1);
	}

	@Override
	public void updateCamera(float x, float y, float z) {
		cameraPos = cameraPos.add(new Vector3f(x, y, z));
	}

	@Override
	public boolean isHeadTrackingEnabled() {
		return false;
	}

	@Override
	public void setHeadTrackingEnabled(boolean enabled) { }

	@Override
	public void addStatusListener(HMDStatusListener listener) { }
	
	@Override
	public void removeStatusListener(HMDStatusListener listener) { }
	
	@Override
	public void loop() {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix = viewMatrix.translate(cameraPos);
		Matrix4f modelMatrix = new Matrix4f();
		modelMatrix = modelMatrix.scale(modelScale).translate(modelPos); // TODO: Rotate
		Matrix4f modelViewMatrix = viewMatrix.mult(modelMatrix);

		ctx.updateMatrix(projectionMatrix, modelViewMatrix);

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		ctx.loop();
	}

	@Override
	public void destroy() {
		ctx.destroy();
	}

	@Override
	public long getPreferredMonitor() {
		return MemoryUtil.NULL;
	}

	@Override
	public int getPreferredWidth() {
		return 1920;
	}

	@Override
	public int getPreferredHeight() {
		return 1080;
	}
}
