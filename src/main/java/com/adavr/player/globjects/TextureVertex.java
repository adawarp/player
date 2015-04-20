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

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class TextureVertex extends Vertex {

	private Color color;
	private UV uv;

	public TextureVertex() {
	}

	public TextureVertex(float x, float y, float z, float w, Color color, UV uv) {
		super(x, y, z, w);
		this.color = color;
		this.uv = uv;
	}

	@Override
	public void push(FloatBuffer buffer) {
		super.push(buffer);
		color.push(buffer);
		uv.push(buffer);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public UV getUv() {
		return uv;
	}

	public void setUv(UV uv) {
		this.uv = uv;
	}
}
