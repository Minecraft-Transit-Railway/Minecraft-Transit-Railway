package org.mtr.widget;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIImage;
import gg.essential.universal.UGraphics;
import gg.essential.universal.UMatrixStack;
import gg.essential.universal.render.URenderPipeline;
import gg.essential.universal.shader.BlendState;
import gg.essential.universal.utils.ReleasedDynamicTexture;
import gg.essential.universal.vertex.UBufferBuilder;
import gg.essential.universal.vertex.UBuiltBuffer;
import gg.essential.universal.vertex.UVertexConsumer;
import kotlin.Unit;
import lombok.Setter;
import net.minecraft.util.math.Vec3d;
import org.mtr.MTR;
import org.mtr.core.tool.Vector;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * An alternative to {@link UIImage} to directly draw images synchronously. Multiple images can be provided and the image to be rendered can be changed on the fly.
 */
public abstract class ImageComponentBase extends UIComponent {

	@Setter
	@Nullable
	protected Color backgroundColor;
	private int activeTextureIndex;
	private final ReleasedDynamicTexture[] releasedDynamicTextures;

	private static final URenderPipeline COLOR_QUAD_PIPELINE = createRenderPipeline("mtr:color_quad", UGraphics.CommonVertexFormats.POSITION_COLOR, false);
	private static final URenderPipeline COLOR_QUAD_PIPELINE_ALPHA = createRenderPipeline("mtr:color_quad", UGraphics.CommonVertexFormats.POSITION_COLOR, true);
	private static final URenderPipeline TEXTURE_COLOR_QUAD_PIPELINE = createRenderPipeline("mtr:texture_color_quad", UGraphics.CommonVertexFormats.POSITION_TEXTURE_COLOR, false);
	private static final int CYLINDER_EDGES = 16;

	public ImageComponentBase(ReleasedDynamicTexture... releasedDynamicTextures) {
		this.releasedDynamicTextures = releasedDynamicTextures;
	}

	@Override
	public void draw(UMatrixStack matrixStack) {
		// Apply effects
		beforeDrawCompat(matrixStack);

		// Draw colors
		if (backgroundColor != null) {
			matrixStack.push();
			drawRectangle(vertexConsumer -> {
				final float x1 = getLeft();
				final float y1 = getTop();
				final float x2 = getRight();
				final float y2 = getBottom();
				vertexConsumer.pos(matrixStack, x1, y2, 0).color(backgroundColor).endVertex();
				vertexConsumer.pos(matrixStack, x2, y2, 0).color(backgroundColor).endVertex();
				vertexConsumer.pos(matrixStack, x2, y1, 0).color(backgroundColor).endVertex();
				vertexConsumer.pos(matrixStack, x1, y1, 0).color(backgroundColor).endVertex();
			}, false);
			matrixStack.pop();
		}

		// Draw image
		if (activeTextureIndex >= 0 && activeTextureIndex < releasedDynamicTextures.length) {
			matrixStack.push();
			drawTexture(releasedDynamicTextures[activeTextureIndex], vertexConsumer -> renderTexture(matrixStack, vertexConsumer));
			matrixStack.pop();
		}

		super.draw(matrixStack);
	}

	public final void setActiveTexture(int activeTextureIndex) {
		this.activeTextureIndex = activeTextureIndex;
	}

	public abstract void renderTexture(UMatrixStack matrixStack, UVertexConsumer vertexConsumer);

	public static void drawRectangle(Consumer<UVertexConsumer> consumer, boolean hasAlpha) {
		final UBufferBuilder bufferBuilder = UBufferBuilder.create(UGraphics.DrawMode.QUADS, UGraphics.CommonVertexFormats.POSITION_COLOR);
		consumer.accept(bufferBuilder);

		try (final UBuiltBuffer builtBuffer = bufferBuilder.build()) {
			if (builtBuffer != null) {
				builtBuffer.drawAndClose(hasAlpha ? COLOR_QUAD_PIPELINE_ALPHA : COLOR_QUAD_PIPELINE, drawCallBuilder -> Unit.INSTANCE);
			}
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	public static void drawTexture(ReleasedDynamicTexture releasedDynamicTexture, Consumer<UVertexConsumer> consumer) {
		drawTexture(releasedDynamicTexture::getDynamicGlId, consumer);
	}

	public static void drawTexture(IntSupplier glIdSupplier, Consumer<UVertexConsumer> consumer) {
		final UBufferBuilder bufferBuilder = UBufferBuilder.create(UGraphics.DrawMode.QUADS, UGraphics.CommonVertexFormats.POSITION_TEXTURE_COLOR);
		consumer.accept(bufferBuilder);

		try (final UBuiltBuffer builtBuffer = bufferBuilder.build()) {
			if (builtBuffer != null) {
				builtBuffer.drawAndClose(TEXTURE_COLOR_QUAD_PIPELINE, drawCallBuilder -> {
					drawCallBuilder.texture(0, glIdSupplier.getAsInt());
					return Unit.INSTANCE;
				});
			}
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	public static void drawShadedQuad(UMatrixStack matrixStack, UVertexConsumer vertexConsumer, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, Color color) {
		final Vec3d v1 = new Vec3d(x2 - x1, y2 - y1, z2 - z1);
		final Vec3d v2 = new Vec3d(x3 - x1, y3 - y1, z3 - z1);
		final Vec3d normal = v1.crossProduct(v2).normalize();
		final float brightness = (float) (3 + normal.dotProduct(new Vec3d(0, 1, 0))) / 4;
		final int r = (int) Math.floor(color.getRed() * brightness);
		final int g = (int) Math.floor(color.getGreen() * brightness);
		final int b = (int) Math.floor(color.getBlue() * brightness);

		vertexConsumer.pos(matrixStack, x1, y1, z1).color(r, g, b, 0xFF).endVertex();
		vertexConsumer.pos(matrixStack, x2, y2, z2).color(r, g, b, 0xFF).endVertex();
		vertexConsumer.pos(matrixStack, x3, y3, z3).color(r, g, b, 0xFF).endVertex();
		vertexConsumer.pos(matrixStack, x4, y4, z4).color(r, g, b, 0xFF).endVertex();
	}

	public static void drawDoubleSidedShadedQuad(UMatrixStack matrixStack, UVertexConsumer vertexConsumer, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, Color color) {
		drawShadedQuad(matrixStack, vertexConsumer, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, color);
		drawShadedQuad(matrixStack, vertexConsumer, x4, y4, z4, x3, y3, z3, x2, y2, z2, x1, y1, z1, color);
	}

	public static void drawCylinder(UMatrixStack matrixStack, UVertexConsumer vertexConsumer, double x1, double y1, double z1, double r1, double x2, double y2, double z2, double r2, Color color) {
		drawHollowCylinder(matrixStack, vertexConsumer, x1, y1, z1, r1, x2, y2, z2, r2, color);
		if (r1 != 0 || r2 != 0) {
			final Vector offsetVector = new Vector(x2 - x1, y2 - y1, z2 - z1).normalize().multiply(0.001, 0.001, 0.001);
			if (r1 != 0) {
				drawHollowCylinder(matrixStack, vertexConsumer, x1 - offsetVector.x(), y1 - offsetVector.y(), z1 - offsetVector.z(), 0, x1, y1, z1, r1, color);
			}
			if (r2 != 0) {
				drawHollowCylinder(matrixStack, vertexConsumer, x2, y2, z2, r2, x2 + offsetVector.x(), y2 + offsetVector.y(), z2 + offsetVector.z(), 0, color);
			}
		}
	}

	public static void drawCylinderWithArrows(UMatrixStack matrixStack, UVertexConsumer vertexConsumer, double x1, double y1, double z1, double x2, double y2, double z2, double r, Color color) {
		final double differenceX = x2 - x1;
		final double differenceY = y2 - y1;
		final double differenceZ = z2 - z1;
		final double length = Math.sqrt(differenceX * differenceX + differenceY * differenceY + differenceZ * differenceZ);
		final double arrowLength = r * 4;

		if (length >= arrowLength * 3) {
			final Vector offsetVector = new Vector(differenceX, differenceY, differenceZ).normalize().multiply(arrowLength, arrowLength, arrowLength);
			drawHollowCylinder(matrixStack, vertexConsumer, x1 + offsetVector.x(), y1 + offsetVector.y(), z1 + offsetVector.z(), r, x2 - offsetVector.x(), y2 - offsetVector.y(), z2 - offsetVector.z(), r, color);
			drawCylinder(matrixStack, vertexConsumer, x1, y1, z1, 0, x1 + offsetVector.x(), y1 + offsetVector.y(), z1 + offsetVector.z(), r * 2, color);
			drawCylinder(matrixStack, vertexConsumer, x2 - offsetVector.x(), y2 - offsetVector.y(), z2 - offsetVector.z(), r * 2, x2, y2, z2, 0, color);
		} else {
			drawCylinder(matrixStack, vertexConsumer, x1, y1, z1, r, x2, y2, z2, r, color);
		}
	}

	public static void drawTexturedQuad(UMatrixStack matrixStack, UVertexConsumer vertexConsumer, float x1, float y1, float x2, float y2, float u1, float v1, float u2, float v2) {
		vertexConsumer.pos(matrixStack, x1, y2, 0).tex(u1, v2).color(Color.WHITE).endVertex();
		vertexConsumer.pos(matrixStack, x2, y2, 0).tex(u2, v2).color(Color.WHITE).endVertex();
		vertexConsumer.pos(matrixStack, x2, y1, 0).tex(u2, v1).color(Color.WHITE).endVertex();
		vertexConsumer.pos(matrixStack, x1, y1, 0).tex(u1, v1).color(Color.WHITE).endVertex();
	}

	private static void drawHollowCylinder(UMatrixStack matrixStack, UVertexConsumer vertexConsumer, double x1, double y1, double z1, double r1, double x2, double y2, double z2, double r2, Color color) {
		final double differenceX = x2 - x1;
		final double differenceY = y2 - y1;
		final double differenceZ = z2 - z1;
		final double length = Math.sqrt(differenceX * differenceX + differenceY * differenceY + differenceZ * differenceZ);
		final float angleY = (float) -Math.atan2(differenceZ, differenceX);
		final float angleZ = (float) (Math.PI / 2 - Math.atan2(differenceY, Math.sqrt(differenceX * differenceX + differenceZ * differenceZ)));

		final Vector bottomVector = new Vector(r1, 0, 0);
		final Vector topVector = new Vector(r2, length, 0);
		Vector previousBottomVectorRotated = null;
		Vector previousTopVectorRotated = null;

		for (int i = 0; i <= CYLINDER_EDGES; i++) {
			final Vector bottomVectorRotated = bottomVector.rotateY((float) (2 * Math.PI * i / CYLINDER_EDGES)).rotateZ(angleZ).rotateY(angleY);
			final Vector topVectorRotated = topVector.rotateY((float) (2 * Math.PI * i / CYLINDER_EDGES)).rotateZ(angleZ).rotateY(angleY);
			if (previousBottomVectorRotated != null) {
				drawShadedQuad(
						matrixStack, vertexConsumer,
						x1 + previousBottomVectorRotated.x(), y1 + previousBottomVectorRotated.y(), z1 + previousBottomVectorRotated.z(),
						x1 + bottomVectorRotated.x(), y1 + bottomVectorRotated.y(), z1 + bottomVectorRotated.z(),
						x1 + topVectorRotated.x(), y1 + topVectorRotated.y(), z1 + topVectorRotated.z(),
						x1 + previousTopVectorRotated.x(), y1 + previousTopVectorRotated.y(), z1 + previousTopVectorRotated.z(),
						color
				);
			}
			previousBottomVectorRotated = bottomVectorRotated;
			previousTopVectorRotated = topVectorRotated;
		}
	}

	private static URenderPipeline createRenderPipeline(String id, UGraphics.CommonVertexFormats commonVertexFormats, boolean hasAlpha) {
		final URenderPipeline.Builder builder = URenderPipeline.Companion.builderWithDefaultShader(id, UGraphics.DrawMode.QUADS, commonVertexFormats);
		builder.setDepthTest(URenderPipeline.DepthTest.LessOrEqual);
		builder.setCulling(true);
		if (hasAlpha) {
			builder.setBlendState(BlendState.ALPHA);
		}
		return builder.build();
	}
}
