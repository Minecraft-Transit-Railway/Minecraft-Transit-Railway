package org.mtr.model.render.vertex;

import net.minecraft.client.render.VertexConsumer;
import org.joml.Vector3f;
import org.mtr.model.render.batch.MaterialProperties;
import org.mtr.model.render.model.RawMesh;
import org.mtr.model.render.model.RawModel;

public final class CapturingVertexConsumer implements VertexConsumer {

	private RawMesh buildingMesh;
	private Vertex buildingVertex = new Vertex();

	public final RawModel rawModel = new RawModel();

	public void beginStage(MaterialProperties materialProperties) {
		buildingMesh = rawModel.getRawMesh(materialProperties);
	}

	@Override
	public VertexConsumer vertex(float x, float y, float z) {
		buildingVertex.position = new Vector3f(x, y, z);
		return this;
	}

	@Override
	public VertexConsumer color(int red, int green, int blue, int alpha) {
		// Unused
		return this;
	}

	@Override
	public VertexConsumer texture(float u, float v) {
		buildingVertex.u = u;
		buildingVertex.v = v;
		return this;
	}

	@Override
	public VertexConsumer overlay(int u, int v) {
		// Unused
		return this;
	}

	@Override
	public VertexConsumer light(int u, int v) {
		// Unused
		return this;
	}

	@Override
	public VertexConsumer normal(float x, float y, float z) {
		buildingVertex.normal = new Vector3f(x, y, z);
		return this;
	}
}
