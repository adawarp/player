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
package com.adavr.player.vlc;

import com.adavr.player.ApplicationContext;
import com.adavr.player.context.SceneRenderContext;
import com.adavr.player.context.ShaderContext;
import com.adavr.player.media.MediaContext;
import org.saintandreas.math.Vector3f;
import uk.co.caprica.vlcj.version.LibVlcVersion;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class VLCApplication implements ApplicationContext {
	
	private final ShaderContext shaderCtx;
	private final VLCContext vlcCtx;
	
	public VLCApplication() {
		shaderCtx = new ShaderContext();
		vlcCtx = new VLCContext(8.0f, 4.5f, 1920, 1080);
		vlcCtx.setOffset(new Vector3f(0.0f, 0.0f, -3.0f));
		shaderCtx.addContext(vlcCtx);
	}
	
	@Override
	public void setup() {
		System.out.println("LibVLC: " + LibVlcVersion.getVersion().version());
	}

	@Override
	public SceneRenderContext getSceneRenderContext() {
		return shaderCtx;
	}

	@Override
	public MediaContext getMediaContext() {
		return vlcCtx;
	}
	
	@Override
	public void destroy () { }
}
