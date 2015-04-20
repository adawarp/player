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
package com.adavr.player.example;

import java.io.File;
import java.io.IOException;
import com.adavr.player.globjects.Texture;
import com.adavr.player.context.QuadContext;
import org.lwjgl.opengl.GL13;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class PictureContext extends QuadContext {

	private final File textureFile;
	private Texture texture;

	public PictureContext(float width, float height, File textureFile) {
		super(width, height);
		this.textureFile = textureFile;
	}

	@Override
	public void setup() {
		super.setup();
		try {
			texture = Texture.create(Texture.loadPNG(textureFile));
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
