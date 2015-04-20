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
package com.adavr.player;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.saintandreas.math.Matrix4f;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class Utils {

	public static Matrix4f setupProjectionMatrix(double fov, double aspectRatio, double farPlane, double nearPlane) {
		float yScale = (float) (1.0 / Math.tan(Math.toRadians(fov / 2.0)));
		float xScale = (float) (yScale / aspectRatio);
		float frustumLength = (float) (farPlane - nearPlane);
		float tmp1 = (float) -((farPlane + nearPlane) / frustumLength);
		float tmp2 = (float) -((2 * farPlane * nearPlane) / frustumLength);
		return new Matrix4f(
				xScale, 0.0f, 0.0f, 0.0f,
				0.0f, yScale, 0.0f, 0.0f,
				0.0f, 0.0f, tmp1, tmp2,
				0.0f, 0.0f, -1.0f, 0.0f
		);
	}

	public static FloatBuffer toFloatBuffer(Matrix4f matrix) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		for (int col = 0; col < 4; col++) {
			for (int row = 0; row < 4; row++) {
				buffer.put((float) matrix.get(row, col));
			}
		}
		buffer.flip();
		return buffer;
	}

	public static void bailIfError(String errorMessage) {
		int errorValue = GL11.glGetError();
		if (errorValue != GL11.GL_NO_ERROR) {
			System.err.println("ERROR: " + errorValue + ", " + errorMessage);
			throw new IllegalStateException(errorMessage);
		}
	}
}
