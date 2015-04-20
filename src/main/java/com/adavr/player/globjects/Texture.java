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

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class Texture extends GLObject {

	private final int target;

	private Texture(int id, int target) {
		super(id);
		this.target = target;
	}

	public int getTarget() {
		return target;
	}

	public static void bind(Texture t) {
		GL11.glBindTexture(t.target, t.id);
	}

	@Override
	public void destroy() {
		GL11.glDeleteTextures(id);
	}

	public static Texture create(Image image) {
		return create(GL11.GL_RGBA,
				image.getWidth(), image.getHeight(),
				GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image.getFlippedBuffer());
	}

	public static Texture create(
			int internalFormat, int width, int height,
			int format, int type, ByteBuffer pixels
	) {
		int id = GL11.glGenTextures();
		int target = GL11.GL_TEXTURE_2D;

		GL11.glBindTexture(target, id);

		GL11.glTexImage2D(target, 0, internalFormat, width, height, 0, format, type, pixels);

		GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

		return new Texture(id, target);
	}

	public static Image loadPNG(File file) throws IOException {
		try (InputStream in = new FileInputStream(file)) {
			return loadPNG(in);
		} catch (IOException ex) {
			return null;
		}
	}
	
	public static Image loadPNG(InputStream in) throws IOException {
		PNGDecoder decoder = new PNGDecoder(in);
		Image image = new Image(decoder.getWidth(), decoder.getHeight());
		decoder.decode(image.buffer, decoder.getWidth() * 4, Format.RGBA);
		return image;
	}

	public static class Image {

		private final int width;
		private final int height;
		private final ByteBuffer buffer;

		public Image(int width, int height) {
			this.width = width;
			this.height = height;
			this.buffer = ByteBuffer.allocateDirect(4 * width * height);
			System.out.println("[Image] width=" + width + ", heigth=" + height);
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		public ByteBuffer getFlippedBuffer() {
			buffer.flip();
			return buffer;
		}
	}
}
