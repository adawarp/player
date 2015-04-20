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

import org.lwjgl.opengl.GL20;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class Program extends GLObject {

	private Program(int id) {
		super(id);
	}

	public static Program create() {
		int id = GL20.glCreateProgram();
		return new Program(id);
	}

	public static void bind(Program p) {
		GL20.glUseProgram(p.id);
	}

	public static void unbind() {
		GL20.glUseProgram(0);
	}

	public void link(boolean validate) {
		GL20.glLinkProgram(id);
		if (validate) {
			GL20.glValidateProgram(id);
		}
	}

	public void attachShader(Shader shader) {
		GL20.glAttachShader(id, shader.id);
	}

	public void detachShader(Shader shader) {
		GL20.glDetachShader(id, shader.id);
	}

	public void bindAttribLocation(int index, CharSequence name) {
		GL20.glBindAttribLocation(id, index, name);
	}

	public int getUniformLocation(CharSequence name) {
		return GL20.glGetUniformLocation(id, name);
	}

	@Override
	public void destroy() {
		GL20.glDeleteProgram(id);
	}
}
