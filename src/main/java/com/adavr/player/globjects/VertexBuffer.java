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
package com.adavr.player.globjects;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL15;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class VertexBuffer extends GLObject {

	protected final int target;

	private VertexBuffer(int id, int target) {
		super(id);
		this.target = target;
	}

	public int getTarget() {
		return target;
	}

	public static VertexBuffer create(int target) {
		int id = GL15.glGenBuffers();
		return new VertexBuffer(id, target);
	}

	public static void bind(VertexBuffer vb) {
		GL15.glBindBuffer(vb.target, vb.id);
	}

	public static void unbind(VertexBuffer vb) {
		GL15.glBindBuffer(vb.target, 0);
	}

	public void bufferData(FloatBuffer floatBuffer, int usage) {
		GL15.glBufferData(target, floatBuffer, usage);
	}

	public void bufferData(ByteBuffer byteBuffer, int usage) {
		GL15.glBufferData(target, byteBuffer, usage);
	}

	@Override
	public void destroy() {
		GL15.glDeleteBuffers(id);
	}
}
