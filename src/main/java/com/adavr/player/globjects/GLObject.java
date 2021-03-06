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
import org.lwjgl.BufferUtils;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public abstract class GLObject {

	protected final int id;

	protected GLObject(int id) {
		this.id = id;
	}

	public int id() {
		return id;
	}

	public abstract void destroy();

	public static ByteBuffer setupByteBuffer(byte[] array) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(array.length);
		buffer.put(array);
		buffer.flip();
		return buffer;
	}

	public static FloatBuffer setupFloatBuffer(float[] array) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
		buffer.put(array);
		buffer.flip();
		return buffer;
	}
}
