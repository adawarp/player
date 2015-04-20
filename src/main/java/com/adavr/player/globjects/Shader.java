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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.lwjgl.opengl.GL20;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class Shader extends GLObject {

	private Shader(int id) {
		super(id);
	}

	public static Shader compile(InputStream is, int type) {
		CharSequence source = loadSource(new InputStreamReader(is));
		return compile(source, type);
	}
	
	public static Shader compile(File file, int type) throws FileNotFoundException {
		CharSequence source = loadSource(file);
		return compile(source, type);
	}

	public static Shader compile(CharSequence source, int type) {
		int id = GL20.glCreateShader(type);
		GL20.glShaderSource(id, source);
		GL20.glCompileShader(id);
		return new Shader(id);
	}

	private static CharSequence loadSource(File file) throws FileNotFoundException {
		return loadSource(new FileReader(file));
	}
	
	private static CharSequence loadSource(Reader r) {
		try (BufferedReader reader = new BufferedReader(r)) {
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line).append("\n");
			}
			return builder;
		} catch (IOException ex) {
			return null;
		}
	}

	@Override
	public void destroy() {
		GL20.glDeleteShader(id);
	}
}
