package org.mtr.tool;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;

import javax.annotation.Nullable;
import java.awt.*;

/**
 * A helper class for drawing rectangles or textures. Setup is similar to using builders; different parameters have different overloads for ease of use.
 * After creating a {@link Drawing} object, it can technically be reused, but it should really be reused for the same render type.
 * When reusing, be careful to reset vertex, colour, and other data to avoid unexpected results.
 */
public final class Drawing {

	private double x1, y1, z1;
	private double x2, y2, z2;
	private double x3, y3, z3;
	private double x4, y4, z4;
	private int color1 = IGui.ARGB_WHITE;
	private int color2 = IGui.ARGB_WHITE;
	private int color3 = IGui.ARGB_WHITE;
	private int color4 = IGui.ARGB_WHITE;
	private int light1;
	private int light2;
	private int light3;
	private int light4;
	private double u1, v1;
	private double u2, v2;

	private double boundsX1, boundsY1;
	private double boundsX2, boundsY2;

	private boolean hasLightAndNormal = false;
	private boolean hasUvAndOverlay = false;
	private boolean hasBounds = false;

	@Nullable
	private final Matrix4f matrix4f;
	@Nullable
	private final MatrixStack matrixStack;
	private final VertexConsumer vertexConsumer;

	public Drawing(VertexConsumer vertexConsumer) {
		matrix4f = null;
		matrixStack = null;
		this.vertexConsumer = vertexConsumer;
	}

	public Drawing(Matrix4f matrix4f, VertexConsumer vertexConsumer) {
		this.matrix4f = matrix4f;
		matrixStack = null;
		this.vertexConsumer = vertexConsumer;
	}

	public Drawing(MatrixStack matrixStack, VertexConsumer vertexConsumer) {
		matrix4f = null;
		this.matrixStack = matrixStack;
		this.vertexConsumer = vertexConsumer;
	}

	public Drawing(MatrixStack matrixStack, RenderLayer renderLayer) {
		this(matrixStack, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(renderLayer));
	}

	// Vertices

	public Drawing setVertices(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
		this.x3 = x3;
		this.y3 = y3;
		this.z3 = z3;
		this.x4 = x4;
		this.y4 = y4;
		this.z4 = z4;
		return this;
	}

	public Drawing setVertices(double x1, double y1, double x2, double y2, double z) {
		return setVertices(x1, y1, z, x1, y2, z, x2, y2, z, x2, y1, z);
	}

	public Drawing setVertices(double x1, double y1, double x2, double y2) {
		return setVertices(x1, y1, x2, y2, 0);
	}

	public Drawing setVerticesWH(double x, double y, double width, double height, double z) {
		return setVertices(x, y, x + width, y + height, z);
	}

	public Drawing setVerticesWH(double x, double y, double width, double height) {
		return setVerticesWH(x, y, width, height, 0);
	}

	// Colour

	public Drawing setColor(int color1, int color2, int color3, int color4) {
		this.color1 = color1;
		this.color2 = color2;
		this.color3 = color3;
		this.color4 = color4;
		return this;
	}

	public Drawing setColor(int color) {
		return setColor(color, color, color, color);
	}

	public Drawing setColor(Color color) {
		return setColor(color.getRGB());
	}

	public Drawing setColorARGB(int a, int r, int g, int b) {
		return setColor(new Color(r, g, b, a));
	}

	// Light

	public Drawing setLight(int light1, int light2, int light3, int light4) {
		this.light1 = light1;
		this.light2 = light2;
		this.light3 = light3;
		this.light4 = light4;
		hasLightAndNormal = true;
		return this;
	}

	public Drawing setLight(int light) {
		return setLight(light, light, light, light);
	}

	public Drawing setLight() {
		return setLight(IGui.DEFAULT_LIGHT);
	}

	// UV

	public Drawing setUv(double u1, double v1, double u2, double v2) {
		this.u1 = u1;
		this.v1 = v1;
		this.u2 = u2;
		this.v2 = v2;
		hasUvAndOverlay = true;
		return this;
	}

	public Drawing setUv() {
		return setUv(0, 0, 1, 1);
	}

	// Bounds (for GUI rendering, don't render anything outside the bounds)

	public Drawing setGuiBounds(double boundsX1, double boundsY1, double boundsX2, double boundsY2) {
		this.boundsX1 = boundsX1;
		this.boundsY1 = boundsY1;
		this.boundsX2 = boundsX2;
		this.boundsY2 = boundsY2;
		hasBounds = true;
		return this;
	}

	public Drawing setGuiBoundsWH(double boundsX, double boundsY, double width, double height) {
		return setGuiBounds(boundsX, boundsY, boundsX + width, boundsY + height);
	}

	/**
	 * Main method to draw the defined vertices.
	 */
	public void draw() {
		// If at least 2 dimensions are zero, don't render
		final boolean xEqual = x1 == x2 && x1 == x3 && x1 == x4;
		final boolean yEqual = y1 == y2 && y1 == y3 && y1 == y4;
		final boolean zEqual = z1 == z2 && z1 == z3 && z1 == z4;
		if (xEqual && yEqual || xEqual && zEqual || yEqual && zEqual) {
			return;
		}

		// If colours are all transparent, don't render
		if ((color1 & IGui.ARGB_BLACK) == 0 && (color2 & IGui.ARGB_BLACK) == 0 && (color3 & IGui.ARGB_BLACK) == 0 && (color4 & IGui.ARGB_BLACK) == 0) {
			return;
		}

		final float newX1, newY1, newZ1;
		final float newX2, newY2, newZ2;
		final float newX3, newY3, newZ3;
		final float newX4, newY4, newZ4;

		if (matrix4f == null && matrixStack == null) {
			newX1 = (float) x1;
			newY1 = (float) y1;
			newZ1 = (float) z1;
			newX2 = (float) x2;
			newY2 = (float) y2;
			newZ2 = (float) z2;
			newX3 = (float) x3;
			newY3 = (float) y3;
			newZ3 = (float) z3;
			newX4 = (float) x4;
			newY4 = (float) y4;
			newZ4 = (float) z4;
		} else {
			final Matrix4f newMatrix4f = matrix4f == null ? matrixStack.peek().getPositionMatrix() : matrix4f;
			final Vector3f vector3f1 = transform(newMatrix4f, x1, y1, z1);
			final Vector3f vector3f2 = transform(newMatrix4f, x2, y2, z2);
			final Vector3f vector3f3 = transform(newMatrix4f, x3, y3, z3);
			final Vector3f vector3f4 = transform(newMatrix4f, x4, y4, z4);
			newX1 = vector3f1.x;
			newY1 = vector3f1.y;
			newZ1 = vector3f1.z;
			newX2 = vector3f2.x;
			newY2 = vector3f2.y;
			newZ2 = vector3f2.z;
			newX3 = vector3f3.x;
			newY3 = vector3f3.y;
			newZ3 = vector3f3.z;
			newX4 = vector3f4.x;
			newY4 = vector3f4.y;
			newZ4 = vector3f4.z;
		}

		// If vertices are outside GuI bounds, don't render
		if (hasBounds) {
			final boolean xIntersecting = Utilities.isIntersecting(Math.min(Math.min(newX1, newX2), Math.min(newX3, newX4)), Math.max(Math.max(newX1, newX2), Math.max(newX3, newX4)), boundsX1, boundsX2);
			final boolean yIntersecting = Utilities.isIntersecting(Math.min(Math.min(newY1, newY2), Math.min(newY3, newY4)), Math.max(Math.max(newY1, newY2), Math.max(newY3, newY4)), boundsY1, boundsY2);
			if (!xIntersecting || !yIntersecting) {
				return;
			}
		}

		vertex(vertexConsumer, newX1, newY1, newZ1, color1, light1, (float) u1, (float) v1, hasLightAndNormal, hasUvAndOverlay);
		vertex(vertexConsumer, newX2, newY2, newZ2, color2, light2, (float) u1, (float) v2, hasLightAndNormal, hasUvAndOverlay);
		vertex(vertexConsumer, newX3, newY3, newZ3, color3, light3, (float) u2, (float) v2, hasLightAndNormal, hasUvAndOverlay);
		vertex(vertexConsumer, newX4, newY4, newZ4, color4, light4, (float) u2, (float) v1, hasLightAndNormal, hasUvAndOverlay);
	}

	// Utilities

	public static void rotateXRadians(MatrixStack matrixStack, float angle) {
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(angle));
	}

	public static void rotateYRadians(MatrixStack matrixStack, float angle) {
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(angle));
	}

	public static void rotateZRadians(MatrixStack matrixStack, float angle) {
		matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation(angle));
	}

	public static void rotateXDegrees(MatrixStack matrixStack, float angle) {
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(angle));
	}

	public static void rotateYDegrees(MatrixStack matrixStack, float angle) {
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle));
	}

	public static void rotateZDegrees(MatrixStack matrixStack, float angle) {
		matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle));
	}

	private static void vertex(VertexConsumer vertexConsumer, float x, float y, float z, int color, int light, float u, float v, boolean hasLightAndNormal, boolean hasUvAndOverlay) {
		VertexConsumer vertexConsumerNew = vertexConsumer.vertex(x, y, z).color(color);
		if (hasLightAndNormal) {
			vertexConsumerNew = vertexConsumerNew.light(light).normal(0, 1, 0);
		}
		if (hasUvAndOverlay) {
			vertexConsumerNew.texture(u, v).overlay(OverlayTexture.DEFAULT_UV);
		}
	}

	private static Vector3f transform(Matrix4f matrix4f, double x, double y, double z) {
		return matrix4f.transformPosition((float) x, (float) y, (float) z, new Vector3f());
	}
}
