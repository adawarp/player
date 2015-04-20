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

import com.adavr.player.hmd.HMDRenderContextFactory;
import com.adavr.player.hmd.HMDKeyCallback;
import com.adavr.player.hmd.HMDRenderContext;
import com.adavr.player.example.DummyHMDRenderContext;
import com.adavr.player.media.MediaContext;
import com.adavr.player.media.MediaPlayerKeyCallback;
import org.lwjgl.Sys;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.MemoryUtil;

public class Application implements Runnable {

	private final ApplicationContext appCtx;

	private HMDRenderContextFactory hmdCtxFactory;
	private HMDRenderContext hmdCtx;
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private long window;
	private final FPSCounter counter = new FPSCounter();
	private boolean vSync = true;

	public Application(ApplicationContext appCtx) {
		this.appCtx = appCtx;
	}
	
	public void setVSync(boolean vsync) {
		this.vSync = vsync;
	}
	
	public void setHMDRenderContextFactory(HMDRenderContextFactory hmdCtxFactory) {
		this.hmdCtxFactory = hmdCtxFactory;
	}

	@Override
	public void run() {
		System.out.println("LWJGL: " + Sys.getVersion());
		try {
			init(); 
			loop();
			GLFW.glfwDestroyWindow(window);
			keyCallback.release();
		} finally {
			GLFW.glfwTerminate();
			errorCallback.release();
		}
	}

	private void init() {
		GLFW.glfwSetErrorCallback(errorCallback = Callbacks.errorCallbackPrint(System.err));

		if (GLFW.glfwInit() != GL11.GL_TRUE) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		System.out.println("GLFW:" + GLFW.glfwGetVersionString());
		
		// We need OpenGL 3.2+
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

		if (hmdCtxFactory != null) {
			hmdCtx = hmdCtxFactory.create(appCtx.getSceneRenderContext());
		} else {
			hmdCtx = new DummyHMDRenderContext(appCtx.getSceneRenderContext());
		}
		
		final long monitor = hmdCtx.getPreferredMonitor();
		final int width = hmdCtx.getPreferredWidth();
		final int height = hmdCtx.getPreferredHeight();
		
		window = GLFW.glfwCreateWindow(width, height, "Welcome to the Real World", monitor, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		MediaContext mediaCtx = appCtx.getMediaContext();
		if (mediaCtx != null) {
			keyCallback = new MediaPlayerKeyCallback(hmdCtx, mediaCtx);
		} else {
			keyCallback = new HMDKeyCallback(hmdCtx);
		}
		GLFW.glfwSetKeyCallback(window, keyCallback);
		GLFW.glfwMakeContextCurrent(window);
		if (vSync) {
			GLFW.glfwSwapInterval(1);
		}
		GLFW.glfwShowWindow(window);
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}

	private void loop() {
		GLContext.createFromCurrent();

		System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));

		hmdCtx.setup();
		while (GLFW.glfwWindowShouldClose(window) == GL11.GL_FALSE) {
			counter.tick();
			hmdCtx.loop();
			if (vSync) {
				GLFW.glfwSwapBuffers(window);
			}
			GLFW.glfwPollEvents();
		}
		hmdCtx.destroy();
	}
}
