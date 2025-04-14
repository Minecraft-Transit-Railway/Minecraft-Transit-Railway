package org.mtr.model;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.mtr.data.IGui;

import java.awt.*;

public record StoredVertexData(
		float x, float y, float z,
		Color color,
		float u, float v,
		float normalX, float normalY, float normalZ
) {

	public StoredVertexData modify(double translateX, double translateY, double translateZ, boolean flip) {
		final int multiplier = flip ? -1 : 1;
		return new StoredVertexData(
				x * multiplier + (float) translateX, y + (float) translateY, z * multiplier + (float) translateZ,
				color,
				u, v,
				normalX * multiplier, normalY, normalZ * multiplier
		);
	}

	public static void write(ModelPart modelPart, double translateX, double translateY, double translateZ, boolean flip, ObjectArrayList<StoredVertexData> storedVertexDataList) {
		final StoredVertexConsumer storedVertexConsumer = new StoredVertexConsumer();
		modelPart.render(new MatrixStack(), storedVertexConsumer, IGui.MAX_LIGHT_INTERIOR, OverlayTexture.DEFAULT_UV);
		for (int i = 0; i < storedVertexConsumer.vertexEntries.size(); i++) {
			storedVertexDataList.add(new StoredVertexData(
					storedVertexConsumer.vertexEntries.get(i).x,
					-storedVertexConsumer.vertexEntries.get(i).y,
					-storedVertexConsumer.vertexEntries.get(i).z,
					new Color(0xFF, 0xFF, 0xFF, 0xFF),
					storedVertexConsumer.textureEntries.get(i).x,
					storedVertexConsumer.textureEntries.get(i).y,
					storedVertexConsumer.normalEntries.get(i).x,
					-storedVertexConsumer.normalEntries.get(i).y,
					-storedVertexConsumer.normalEntries.get(i).z
			).modify(translateX, translateY, -translateZ, flip));
		}
	}

	public static void apply(ObjectArrayList<StoredVertexData> storedVertexDataList, VertexConsumer vertexConsumer) {
		storedVertexDataList.forEach(storedVertexData -> vertexConsumer.vertex(
				storedVertexData.x,
				storedVertexData.y,
				storedVertexData.z,
				storedVertexData.color.getRGB(),
				storedVertexData.u,
				storedVertexData.v,
				OverlayTexture.DEFAULT_UV,
				IGui.DEFAULT_LIGHT,
				storedVertexData.normalX,
				storedVertexData.normalY,
				storedVertexData.normalZ
		));
	}

	private static class StoredVertexConsumer implements VertexConsumer {

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
	}
}
