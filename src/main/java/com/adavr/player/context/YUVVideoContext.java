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

import java.nio.ByteBuffer;
import com.adavr.player.globjects.Program;
import com.adavr.player.globjects.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class YUVVideoContext extends ShaderContext {

	public static final int Y_INDEX = 0;
	public static final int U_INDEX = 1;
	public static final int V_INDEX = 2;
	
	private static final String VERTEX_SHADER = "com/adavr/shader/vertex.glsl";
	private static final String FRAGMENT_SHADER = "com/adavr/shader/fragment2.glsl";

	private final int width;
	private final int height;
	private Texture[] textures;	// YUV

	public YUVVideoContext(int width, int height) {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
		this.width = width;
		this.height = height;
	}

	public void setPixels(int index, ByteBuffer pixels, int stride) {
		int w;
		int h;
		if (index == Y_INDEX) {
			w = width;
			h = height;
		} else {
			w = width / 2;
			h = height / 2;
		}
		Texture.bind(textures[index]);
		GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, stride);
		GL11.glTexSubImage2D(
				textures[index].getTarget(), 0, 0, 0, w, h,
				GL11.GL_RED, GL11.GL_UNSIGNED_BYTE, pixels);
	}

	@Override
	public void setup() {
		super.setup();
		// Setup YUV
		textures[Y_INDEX] = Texture.create(GL30.GL_R8, width, height, GL11.GL_RED, GL11.GL_UNSIGNED_BYTE, null);
		textures[U_INDEX] = Texture.create(GL30.GL_R8, width / 2, height / 2, GL11.GL_RED, GL11.GL_UNSIGNED_BYTE, null);
		textures[V_INDEX] = Texture.create(GL30.GL_R8, width / 2, height / 2, GL11.GL_RED, GL11.GL_UNSIGNED_BYTE, null);

		int location;
		Program.bind(program);
		{
			location = program.getUniformLocation("y_tex");
			GL20.glUniform1i(location, 0);
			location = program.getUniformLocation("u_tex");
			GL20.glUniform1i(location, 1);
			location = program.getUniformLocation("v_tex");
			GL20.glUniform1i(location, 2);
		}
		Program.unbind();
	}

	@Override
	public void loop() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		Texture.bind(textures[Y_INDEX]);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		Texture.bind(textures[U_INDEX]);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		Texture.bind(textures[V_INDEX]);
		super.loop();
	}

	@Override
	public void destroy() {
		for (Texture texture : textures) {
			texture.destroy();
		}
		super.destroy();
	}
}
