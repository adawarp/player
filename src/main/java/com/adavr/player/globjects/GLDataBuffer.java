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

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class GLDataBuffer {

	private final FloatBuffer buffer;

	public GLDataBuffer(int capacity) {
		this.buffer = BufferUtils.createFloatBuffer(capacity);
	}

	public GLDataBuffer put(GLData... dataArray) {
		for (GLData data : dataArray) {
			data.push(buffer);
		}
		return this;
	}

	public FloatBuffer getFlippedBuffer() {
		buffer.flip();
		return buffer;
	}
}
