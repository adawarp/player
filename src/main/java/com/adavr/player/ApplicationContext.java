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
package com.adavr.player;

import com.adavr.player.context.SceneRenderContext;
import com.adavr.player.media.MediaContext;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public interface ApplicationContext {
	
	public void setup();
	
	public void destroy();
	
	public SceneRenderContext getSceneRenderContext();
	
	public MediaContext getMediaContext();
}
