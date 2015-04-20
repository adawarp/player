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
package com.adavr.player.media;

import com.adavr.player.hmd.HMDRenderContext;
import com.adavr.player.hmd.HMDKeyCallback;
import org.lwjgl.glfw.GLFW;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class MediaPlayerKeyCallback extends HMDKeyCallback {

	public static final int AUDIO_ADJUST_STEP = 5;
	public static final long VIDEO_SEEK_LENGTH = 10000;

	private final MediaContext media;
	private boolean playing = false;
	
	public MediaPlayerKeyCallback(HMDRenderContext camera, MediaContext media) {
		super(camera);
		this.media = media;
	}

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		super.invoke(window, key, scancode, action, mods);
		if (action == GLFW.GLFW_PRESS) {
			switch (key) {
			case GLFW.GLFW_KEY_KP_SUBTRACT:
				media.adjustVolume(-AUDIO_ADJUST_STEP);
				break;
			case GLFW.GLFW_KEY_KP_ADD:
				media.adjustVolume(AUDIO_ADJUST_STEP);
				break;
			case GLFW.GLFW_KEY_SPACE:
				if (!playing) {
					media.play();
				} else {
					media.pause();
				}
				playing = !playing;
				break;
			case GLFW.GLFW_KEY_LEFT_BRACKET:
				media.seek(-VIDEO_SEEK_LENGTH);
				break;
			case GLFW.GLFW_KEY_RIGHT_BRACKET:
				media.seek(VIDEO_SEEK_LENGTH);
				break;
			case GLFW.GLFW_KEY_P:
				media.rewind();
				break;
			}
		}
	}
}
