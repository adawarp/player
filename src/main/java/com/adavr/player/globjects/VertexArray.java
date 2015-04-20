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

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class VertexArray extends GLObject {

	private int currentAttribIndex = 0;
	private int currentAttribOffset = 0;

	private VertexArray(int id) {
		super(id);
	}

	public static VertexArray create() {
		int id = GL30.glGenVertexArrays();
		return new VertexArray(id);
	}

	public static void bind(VertexArray va) {
		GL30.glBindVertexArray(va.id);
	}

	public static void unbind() {
		GL30.glBindVertexArray(0);
	}

	public int addAtrribute(int size, int stride) {
		int index = currentAttribIndex;
		GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, stride, currentAttribOffset);
		currentAttribIndex++;
		currentAttribOffset += size * 4;
		return index;
	}

	public void enableAttributes() {
		for (int index = 0; index < currentAttribIndex; index++) {
			GL20.glEnableVertexAttribArray(index);
		}
	}

	public void disableAttributes() {
		for (int index = 0; index < currentAttribIndex; index++) {
			GL20.glDisableVertexAttribArray(index);
		}
	}

	@Override
	public void destroy() {
		GL30.glDeleteVertexArrays(id);
	}
}
