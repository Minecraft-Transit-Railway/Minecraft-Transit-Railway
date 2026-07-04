package org.mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.mtr.data.IGui;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

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

	public static void write(ModelPart modelPart, ObjectArrayList<StoredVertexData> storedVertexDataList) {
		final StoredVertexConsumer storedVertexConsumer = new StoredVertexConsumer();
		modelPart.render(new PoseStack(), storedVertexConsumer, IGui.MAX_LIGHT_INTERIOR, OverlayTexture.NO_OVERLAY);
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
			));
		}
	}

	public static void apply(ObjectArrayList<StoredVertexData> storedVertexDataList, VertexConsumer vertexConsumer) {
		storedVertexDataList.forEach(storedVertexData -> vertexConsumer.addVertex(
			storedVertexData.x,
			storedVertexData.y,
			storedVertexData.z,
			storedVertexData.color.getRGB(),
			storedVertexData.u,
			storedVertexData.v,
			OverlayTexture.NO_OVERLAY,
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
		public VertexConsumer addVertex(float x, float y, float z) {
			vertexEntries.add(new Vector3f(x, y, z));
			return this;
		}

		@Override
		public VertexConsumer setColor(int red, int green, int blue, int alpha) {
			return this;
		}

		@Override
		public VertexConsumer setUv(float u, float v) {
			textureEntries.add(new Vector2f(u, v));
			return this;
		}

		@Override
		public VertexConsumer setUv1(int u, int v) {
			return this;
		}

		@Override
		public VertexConsumer setUv2(int u, int v) {
			return this;
		}

		@Override
		public VertexConsumer setNormal(float x, float y, float z) {
			normalEntries.add(new Vector3f(x, y, z));
			return this;
		}
	}
}
