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
package com.adavr.player.hmd;

import com.adavr.player.context.RenderContext;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public interface HMDRenderContext extends RenderContext {

	public int getPreferredWidth();
	
	public int getPreferredHeight();
	
	public long getPreferredMonitor();
	
	public void resetCamera();

	public void updateCamera(float x, float y, float z);
	
	public boolean isHeadTrackingEnabled();
	
	public void setHeadTrackingEnabled(boolean enabled);}