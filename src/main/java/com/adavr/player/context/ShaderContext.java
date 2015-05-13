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

import java.util.ArrayList;
import com.adavr.player.Utils;
import com.adavr.player.globjects.Program;
import com.adavr.player.globjects.Shader;
import java.io.InputStream;
import org.lwjgl.opengl.GL20;
import org.saintandreas.math.Matrix4f;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class ShaderContext implements SceneRenderContext {

	public static final String DEFAULT_VERTEX_SHADER = "com/adavr/shader/vertex.glsl";
	public static final String DEFAULT_FRAGMENT_SHADER = "com/adavr/shader/fragment.glsl";
	
	private final Object contextLock = new Object();
	private final ArrayList<RenderContext> newContexts = new ArrayList<>();
	private final ArrayList<RenderContext> currentContexts = new ArrayList<>();
	private final ArrayList<RenderContext> oldContexts = new ArrayList<>();
	private final String vertexShaderName;
	private final String fragmentShaderName;
	protected Program program;
	private Shader vertexShader;
	private Shader fragmentShader;

	public ShaderContext(String vertexShaderName, String fragmentShaderName) {
		this.vertexShaderName = vertexShaderName;
		this.fragmentShaderName = fragmentShaderName;
	}
	
	public ShaderContext() {
		this(DEFAULT_VERTEX_SHADER, DEFAULT_FRAGMENT_SHADER);
	}
	
	@Override
	public void addContext(RenderContext context) {
		synchronized (contextLock) {
			if (!currentContexts.contains(context)) {
				newContexts.add(context);
			}
		}
	}

	@Override
	public void removeContext(RenderContext context) {
		synchronized (contextLock) {
			if (currentContexts.contains(context)) {
				oldContexts.add(context);
			}
		}
	}
	
	private void updateContexts() {
		synchronized (contextLock) {
			if (!newContexts.isEmpty()) {
				for (RenderContext context : newContexts) {
					context.setup();
					// Move to currents
					currentContexts.add(context);
				}
				newContexts.clear();
			}
			if (!oldContexts.isEmpty()) {
				for (RenderContext context : oldContexts) {
					context.destroy();
					// Remove from currents
					currentContexts.remove(context);
				}
				oldContexts.clear();
			}
		}
	}

	@Override
	public void updateMatrix(Matrix4f projectionMatrix, Matrix4f modelViewMatrix) {
		int location;
		Program.bind(program);
		{
			location = program.getUniformLocation("projectionMatrix");
			GL20.glUniformMatrix4fv(location, false, Utils.toFloatBuffer(projectionMatrix));
			location = program.getUniformLocation("modelViewMatrix");
			GL20.glUniformMatrix4fv(location, false, Utils.toFloatBuffer(modelViewMatrix));
		}
		Program.unbind();
	}
	
	private static Shader loadShader(String shaderName, int type) {
		System.out.println("Load Shader: " + shaderName);
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(shaderName);
		if (is == null) {
			System.out.println("Couldn't find: " + shaderName);
			return null;
		}
		return Shader.compile(is, type);
	}

	@Override
	public void setup() {
		vertexShader = loadShader(vertexShaderName, GL20.GL_VERTEX_SHADER);
		fragmentShader = loadShader(fragmentShaderName, GL20.GL_FRAGMENT_SHADER);
		program = Program.create();
		program.attachShader(vertexShader);
		program.attachShader(fragmentShader);
		program.bindAttribLocation(0, "in_Position");
		program.bindAttribLocation(1, "in_Color");
		program.bindAttribLocation(2, "in_TextureCoord");
		program.link(true);
	}

	@Override
	public void loop() {
		updateContexts();
		Program.bind(program);
		{
			for (RenderContext context : currentContexts) {
				context.loop();
			}
		}
		Program.unbind();
	}

	@Override
	public void destroy() {
		Program.unbind();
		program.detachShader(vertexShader);
		program.detachShader(fragmentShader);
		vertexShader.destroy();
		fragmentShader.destroy();
		program.destroy();
		for (RenderContext context : currentContexts) {
			context.destroy();
		}
	}
}
