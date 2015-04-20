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

import com.adavr.http.ClientException;
import java.nio.IntBuffer;
import com.adavr.player.context.RGBVideoContext;
import com.adavr.player.media.MediaContext;
import com.adavr.player.media.MediaURLClient;
import com.adavr.player.media.MediaURLClientManager;
import org.lwjgl.BufferUtils;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class VLCContext extends RGBVideoContext implements MediaContext {

	public static final int AUDIO_MIN_VOL = 0;
	public static final int AUDIO_MAX_VOL = 200;
	private final DirectMediaPlayerComponent mediaPlayerComponent;

	private IntBuffer buffer = null;

	public VLCContext(float width, float height, final int videoWidth, final int videoHeight) {
		super(width, height);

		setVideoSize(videoWidth, videoHeight);
		BufferFormatCallback bufferFormatCallback = new BufferFormatCallback() {
			@Override
			public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
				return new RV32BufferFormat(videoWidth, videoHeight);
			}
		};
		mediaPlayerComponent = new DirectMediaPlayerComponent(bufferFormatCallback) {
			@Override
			protected RenderCallback onGetRenderCallback() {
				return new GLRenderCallbackAdapter(videoWidth, videoHeight);
			}
		};
	}
	
	public MediaPlayer getMediaPlayer() {
		return mediaPlayerComponent.getMediaPlayer();
	}

	@Override
	public void setMedia(String uri) {
		String media = uri;
		try {
			MediaURLClient client = MediaURLClientManager.getInstance().findURLClient(uri);
			if (client != null) {
				media = client.getStreamURL(uri);
			}
		} catch (ClientException ignore) { }
		getMediaPlayer().prepareMedia(media);
	}

	@Override
	public void adjustVolume(int step) {
		MediaPlayer player = getMediaPlayer();
		int vol = player.getVolume() + step;
		if (AUDIO_MIN_VOL <= vol && vol <= AUDIO_MAX_VOL) {
			player.setVolume(vol);
		}
	}

	@Override
	public void play() {
		getMediaPlayer().play();
	}

	@Override
	public void pause() {
		getMediaPlayer().pause();
	}

	@Override
	public void seek(long length) {
		MediaPlayer player = getMediaPlayer();
		if (player.isSeekable()) {
			player.skip(length);
		}
	}

	@Override
	public void rewind() {
		getMediaPlayer().setPosition(0);
	}

	@Override
	public void loop() {
		IntBuffer b = getIntBuffer();
		if (b != null) {
			super.setPixels(b);
		}
		super.loop();
	}

	@Override
	public void destroy() {
		super.destroy();
		mediaPlayerComponent.release();
	}
	
	private class GLRenderCallbackAdapter extends RenderCallbackAdapter {

		private GLRenderCallbackAdapter(int width, int height) {
			super(new int[width * height]);
		}

		@Override
		protected void onDisplay(DirectMediaPlayer mediaPlayer, int[] rgbBuffer) {
			// Simply copy buffer to the image and repaint
			IntBuffer buffer = BufferUtils.createIntBuffer(rgbBuffer.length);
			buffer.put(rgbBuffer);
			buffer.flip();
			drawFrame(buffer);
		}
	}

	private synchronized void drawFrame(IntBuffer buffer) {
		this.buffer = buffer;
	}

	private synchronized IntBuffer getIntBuffer() {
		return this.buffer;
	}
}
