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

import java.io.IOException;
import java.nio.IntBuffer;
import com.adavr.player.globjects.Texture;
import java.io.InputStream;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class RGBVideoContext extends QuadContext {

	private static final String LOGO = "logo.png";

	private int videoWidth;
	private int videoHeight;
	private Texture texture;

	public RGBVideoContext(float width, float height) {
		super(width, height);
	}
	
	public void setVideoSize(int width, int height) {
		this.videoWidth = width;
		this.videoHeight = height;
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
		try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(LOGO)) {
			Texture.Image image = Texture.loadPNG(is);
//			texture = Texture.create(GL11.GL_RGBA, width, height, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, null);
			texture = Texture.create(image);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
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
