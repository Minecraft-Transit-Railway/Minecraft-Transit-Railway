package org.mtr.model;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.mtr.data.IGui;

public final class StoredVertexConsumer implements VertexConsumer {

	private final ObjectArrayList<Vector3f> vertexEntries = new ObjectArrayList<>();
	private final ObjectArrayList<Vector2f> textureEntries = new ObjectArrayList<>();
	private final ObjectArrayList<Vector3f> normalEntries = new ObjectArrayList<>();

	@Override
	public VertexConsumer vertex(float x, float y, float z) {
		vertexEntries.add(new Vector3f(x, y, z));
		return this;
	}

	@Override
	public VertexConsumer color(int red, int green, int blue, int alpha) {
		return this;
	}

	@Override
	public VertexConsumer texture(float u, float v) {
		textureEntries.add(new Vector2f(u, v));
		return this;
	}

	@Override
	public VertexConsumer overlay(int u, int v) {
		return this;
	}

	@Override
	public VertexConsumer light(int u, int v) {
		return this;
	}

	@Override
	public VertexConsumer normal(float x, float y, float z) {
		normalEntries.add(new Vector3f(x, y, z));
		return this;
	}

	public void apply(VertexConsumer vertexConsumer) {
		for (int i = 0; i < vertexEntries.size(); i++) {
			vertexConsumer.vertex(
					vertexEntries.get(i).x,
					vertexEntries.get(i).y,
					vertexEntries.get(i).z,
					IGui.ARGB_WHITE,
					textureEntries.get(i).x,
					textureEntries.get(i).y,
					OverlayTexture.DEFAULT_UV,
					IGui.DEFAULT_LIGHT,
					normalEntries.get(i).x,
					normalEntries.get(i).y,
					normalEntries.get(i).z
			);
		}
	}

	public void add(StoredVertexConsumer storedVertexConsumer, double x, double y, double z, boolean flip) {
		final int multiplier = flip ? -1 : 1;
		storedVertexConsumer.vertexEntries.forEach(vertexEntry -> vertexEntries.add(new Vector3f(
				(float) (vertexEntry.x * multiplier + x),
				(float) (vertexEntry.y * multiplier + y),
				(float) (vertexEntry.z * multiplier + z)
		)));
		textureEntries.addAll(storedVertexConsumer.textureEntries);
		storedVertexConsumer.normalEntries.forEach(normalEntry -> normalEntries.add(new Vector3f(
				normalEntry.x * multiplier,
				normalEntry.y * multiplier,
				normalEntry.z * multiplier
		)));
	}

	public boolean isEmpty() {
		return vertexEntries.isEmpty();
	}
}
