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

import org.lwjgl.opengl.GL30;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class Framebuffer extends GLObject {

	protected final int target;

	private Framebuffer(int id, int target) {
		super(id);
		this.target = target;
	}

	public static Framebuffer create(int target) {
		int id = GL30.glGenFramebuffers();
		return new Framebuffer(id, target);
	}

	public static void bind(Framebuffer fb) {
		GL30.glBindFramebuffer(fb.target, fb.id);
	}

	public static void unbind(Framebuffer fb) {
		GL30.glBindFramebuffer(fb.target, 0);
	}

	public void attachTexture2D(Texture texture) {
		GL30.glFramebufferTexture2D(target, GL30.GL_COLOR_ATTACHMENT0, texture.getTarget(), texture.id, 0);
	}

	public void attachRenderBuffer(int renderBuffer) {
		GL30.glFramebufferRenderbuffer(target, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, renderBuffer);
	}

	@Override
	public void destroy() {
		GL30.glDeleteFramebuffers(id);
	}

}
