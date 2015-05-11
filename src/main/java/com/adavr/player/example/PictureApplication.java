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

import com.adavr.player.ApplicationContext;
import com.adavr.player.context.SceneRenderContext;
import com.adavr.player.context.ShaderContext;
import com.adavr.player.media.MediaContext;
import java.io.File;
import org.saintandreas.math.Vector3f;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class PictureApplication implements ApplicationContext {
	
	private final ShaderContext shaderCtx;
	
	public PictureApplication(String picture) {
		shaderCtx = new ShaderContext();
		PictureContext context = new PictureContext(6.0f, 6.0f, new File(picture));
		context.setOffset(new Vector3f(0.0f, 0.0f, -3.0f));
		shaderCtx.addContext(context);
	}
	
	@Override
	public SceneRenderContext getSceneRenderContext() {
		return shaderCtx;
	}

	@Override
	public MediaContext getMediaContext() {
		return null;
	}
}
