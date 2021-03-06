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
package com.adavr.player.context;

import java.nio.IntBuffer;
import com.adavr.player.globjects.Texture;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class RGBVideoContext extends QuadContext {

	private final int videoWidth;
	private final int videoHeight;
	private Texture texture;

	public RGBVideoContext(float width, float height, int videoWidth, int videoHeight) {
		super(width, height);
		this.videoWidth = videoWidth;
		this.videoHeight = videoHeight;
	}
	
	public int getVideoWidth() {
		return videoWidth;
	}
	
	public int getVideoHeight() {
		return videoHeight;
	}

	public void setPixels(IntBuffer pixels) {
		Texture.bind(texture);
//		GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, stride);
		GL11.glTexSubImage2D(texture.getTarget(), 0, 0, 0, videoWidth, videoHeight,
				GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, pixels);
	}

	@Override
	public void setup() {
		super.setup();
		ByteBuffer buffer = BufferUtils.createByteBuffer(4 * videoWidth * videoHeight);
		for (int i = 0; i < buffer.capacity(); i++) {
			buffer.put((byte) 0);
		}
		buffer.flip();
		texture = Texture.create(GL11.GL_RGBA, videoWidth, videoHeight,
				GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, buffer);
	}

	@Override
	public void loop() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		Texture.bind(texture);
		super.loop();
	}

	@Override
	public void destroy() {
		texture.destroy();
		super.destroy();
	}
}
