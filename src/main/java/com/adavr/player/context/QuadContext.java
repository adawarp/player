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

import java.nio.ByteBuffer;
import com.adavr.player.globjects.GLObject;
import com.adavr.player.globjects.VertexArray;
import com.adavr.player.globjects.VertexBuffer;
import com.adavr.player.globjects.Color;
import com.adavr.player.globjects.GLDataBuffer;
import com.adavr.player.globjects.TextureVertex;
import com.adavr.player.globjects.UV;
import com.adavr.player.globjects.Vertex;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.saintandreas.math.Vector3f;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class QuadContext extends ModelRenderContext {

	private final float width;
	private final float height;
	private Vector3f offset;
	private TextureVertex[] vertices;
	private int indicesCount;
	private VertexArray vao;
	private VertexBuffer verticesVBO;
	private VertexBuffer indicesVBO;

	public QuadContext(float width, float height) {
		this.width = width;
		this.height = height;
		offset = Vector3f.ZERO;
	}

	public void setOffset(Vector3f offset) {
		this.offset = offset;
	}

	@Override
	public void setup() {
		final float w = width / 2f;
		final float h = height / 2f;
		vertices = new TextureVertex[]{
			new TextureVertex(-w + offset.x, h + offset.y, offset.z, 1f, new Color(1, 0, 0, 1), new UV(0, 0)),
			new TextureVertex(-w + offset.x, -h + offset.y, offset.z, 1f, new Color(0, 1, 0, 1), new UV(0, 1)),
			new TextureVertex(w + offset.x, -h + offset.y, offset.z, 1f, new Color(0, 0, 1, 1), new UV(1, 1)),
			new TextureVertex(w + offset.x, h + offset.y, offset.z, 1f, new Color(1, 1, 1, 1), new UV(1, 0))
		};
		GLDataBuffer verticesBuffer = new GLDataBuffer(64);
		verticesBuffer.put(vertices);

		vao = VertexArray.create();
		VertexArray.bind(vao);
		{
			verticesVBO = VertexBuffer.create(GL15.GL_ARRAY_BUFFER);
			VertexBuffer.bind(verticesVBO);
			{
				verticesVBO.bufferData(verticesBuffer.getFlippedBuffer(), GL15.GL_STATIC_DRAW);
				int stride = Vertex.SIZE + Color.SIZE + UV.SIZE;
				vao.addAtrribute(Vertex.COUNT, stride);
				vao.addAtrribute(Color.COUNT, stride);
				vao.addAtrribute(UV.COUNT, stride);
			}
			VertexBuffer.unbind(verticesVBO);
		}
		VertexArray.unbind();

		ByteBuffer indicesBuffer = GLObject.setupByteBuffer(
				new byte[]{
					0, 1, 2,
					2, 3, 0
				}
		);
		indicesCount = indicesBuffer.limit();

		indicesVBO = VertexBuffer.create(GL15.GL_ELEMENT_ARRAY_BUFFER);
		VertexBuffer.bind(indicesVBO);
		{
			indicesVBO.bufferData(indicesBuffer, GL15.GL_STATIC_DRAW);
		}
		VertexBuffer.unbind(indicesVBO);
	}

	@Override
	public void loop() {
		VertexArray.bind(vao);
		{
			vao.enableAttributes();
			VertexBuffer.bind(indicesVBO);
			{
				GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_BYTE, 0);
			}
			VertexBuffer.unbind(indicesVBO);
			vao.disableAttributes();
		}
		VertexArray.unbind();
	}

	@Override
	public void destroy() {
		VertexArray.bind(vao);
		{
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);

			VertexBuffer.unbind(verticesVBO);
			verticesVBO.destroy();
			VertexBuffer.unbind(indicesVBO);
			indicesVBO.destroy();
		}
		VertexArray.unbind();
		vao.destroy();
	}
}
