package mtr.gui;

import mtr.MTR;
import mtr.config.Config;
import mtr.data.IGui;
import mtr.render.MoreRenderLayers;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public interface IDrawing {

	static void drawStringWithFont(MatrixStack matrices, TextRenderer textRenderer, VertexConsumerProvider.Immediate immediate, String text, float x, float y, int light) {
		drawStringWithFont(matrices, textRenderer, immediate, text, IGui.HorizontalAlignment.CENTER, IGui.VerticalAlignment.CENTER, x, y, 1, IGui.ARGB_WHITE, true, light, null);
	}

	static void drawStringWithFont(MatrixStack matrices, TextRenderer textRenderer, VertexConsumerProvider.Immediate immediate, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, float x, float y, float scale, int textColor, boolean shadow, int light, IGui.DrawingCallback drawingCallback) {
		drawStringWithFont(matrices, textRenderer, immediate, text, horizontalAlignment, verticalAlignment, horizontalAlignment, x, y, -1, -1, scale, textColor, shadow, light, drawingCallback);
	}

	static void drawStringWithFont(MatrixStack matrices, TextRenderer textRenderer, VertexConsumerProvider.Immediate immediate, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColor, boolean shadow, int light, IGui.DrawingCallback drawingCallback) {
		drawStringWithFont(matrices, textRenderer, immediate, text, horizontalAlignment, verticalAlignment, horizontalAlignment, x, y, maxWidth, maxHeight, scale, textColor, shadow, light, drawingCallback);
	}

	static void drawStringWithFont(MatrixStack matrices, TextRenderer textRenderer, VertexConsumerProvider.Immediate immediate, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, IGui.HorizontalAlignment xAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColor, boolean shadow, int light, IGui.DrawingCallback drawingCallback) {
		final Style style = Config.useMTRFont() ? Style.EMPTY.withFont(new Identifier(MTR.MOD_ID, "mtr")) : Style.EMPTY;

		while (text.contains("||")) {
			text = text.replace("||", "|");
		}
		final String[] stringSplit = text.split("\\|");

		final List<Boolean> isCJKList = new ArrayList<>();
		final List<OrderedText> orderedTexts = new ArrayList<>();
		int totalHeight = 0, totalWidth = 0;
		for (final String stringSplitPart : stringSplit) {
			final boolean isCJK = stringSplitPart.codePoints().anyMatch(Character::isIdeographic);
			isCJKList.add(isCJK);

			final OrderedText orderedText = new LiteralText(stringSplitPart).fillStyle(style).asOrderedText();
			orderedTexts.add(orderedText);

			totalHeight += IGui.LINE_HEIGHT * (isCJK ? 2 : 1);
			final int width = textRenderer.getWidth(orderedText) * (isCJK ? 2 : 1);
			if (width > totalWidth) {
				totalWidth = width;
			}
		}

		if (maxHeight >= 0 && totalHeight / scale > maxHeight) {
			scale = totalHeight / maxHeight;
		}

		matrices.push();

		final float totalWidthScaled;
		final float scaleX;
		if (maxWidth >= 0 && totalWidth > maxWidth * scale) {
			totalWidthScaled = maxWidth * scale;
			scaleX = totalWidth / maxWidth;
		} else {
			totalWidthScaled = totalWidth;
			scaleX = scale;
		}
		matrices.scale(1 / scaleX, 1 / scale, 1 / scale);

		float offset = verticalAlignment.getOffset(y * scale, totalHeight);
		for (int i = 0; i < orderedTexts.size(); i++) {
			final boolean isCJK = isCJKList.get(i);
			final int extraScale = isCJK ? 2 : 1;
			if (isCJK) {
				matrices.push();
				matrices.scale(extraScale, extraScale, 1);
			}

			final float xOffset = horizontalAlignment.getOffset(xAlignment.getOffset(x * scaleX, totalWidth), textRenderer.getWidth(orderedTexts.get(i)) * extraScale - totalWidth);

			final float shade = light == IGui.MAX_LIGHT_GLOWING ? 1 : Math.min(LightmapTextureManager.getBlockLightCoordinates(light) / 16F * 0.1F + 0.7F, 1);
			final int a = (textColor >> 24) & 0xFF;
			final int r = (int) (((textColor >> 16) & 0xFF) * shade);
			final int g = (int) (((textColor >> 8) & 0xFF) * shade);
			final int b = (int) ((textColor & 0xFF) * shade);

			textRenderer.draw(orderedTexts.get(i), xOffset / extraScale, offset / extraScale, (a << 24) + (r << 16) + (g << 8) + b, shadow, matrices.peek().getModel(), immediate, false, 0, light);

			if (isCJK) {
				matrices.pop();
			}

			offset += IGui.LINE_HEIGHT * extraScale;
		}

		matrices.pop();

		if (drawingCallback != null) {
			final float x1 = xAlignment.getOffset(x, totalWidthScaled / scale);
			final float y1 = verticalAlignment.getOffset(y, totalHeight / scale);
			drawingCallback.drawingCallback(x1, y1, x1 + totalWidthScaled / scale, y1 + totalHeight / scale);
		}
	}

	static void drawRectangle(VertexConsumer vertexConsumer, double x1, double y1, double x2, double y2, int color) {
		final int a = (color >> 24) & 0xFF;
		final int r = (color >> 16) & 0xFF;
		final int g = (color >> 8) & 0xFF;
		final int b = color & 0xFF;
		if (a == 0) {
			return;
		}
		vertexConsumer.vertex(x1, y1, 0).color(r, g, b, a).next();
		vertexConsumer.vertex(x1, y2, 0).color(r, g, b, a).next();
		vertexConsumer.vertex(x2, y2, 0).color(r, g, b, a).next();
		vertexConsumer.vertex(x2, y1, 0).color(r, g, b, a).next();
	}

	static void drawTexture(MatrixStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, Direction facing, int color, int light) {
		drawTexture(matrices, vertexConsumer, x1, y1, z1, x2, y2, z2, 0, 0, 1, 1, facing, color, light);
	}

	static void drawTexture(MatrixStack matrices, VertexConsumer vertexConsumer, float x, float y, float width, float height, Direction facing, int light) {
		drawTexture(matrices, vertexConsumer, x, y, 0, x + width, y + height, 0, 0, 0, 1, 1, facing, -1, light);
	}

	static void drawTexture(MatrixStack matrices, VertexConsumer vertexConsumer, float x, float y, float width, float height, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		drawTexture(matrices, vertexConsumer, x, y, 0, x + width, y + height, 0, u1, v1, u2, v2, facing, color, light);
	}

	static void drawTexture(MatrixStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		drawTexture(matrices, vertexConsumer, x1, y2, z1, x2, y2, z2, x2, y1, z2, x1, y1, z1, u1, v1, u2, v2, facing, color, light);
	}

	static void drawTexture(MatrixStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		final Vec3i vec3i = facing.getVector();
		final Matrix4f matrix4f = matrices.peek().getModel();
		final Matrix3f matrix3f = matrices.peek().getNormal();
		final int a = (color >> 24) & 0xFF;
		final int r = (color >> 16) & 0xFF;
		final int g = (color >> 8) & 0xFF;
		final int b = color & 0xFF;
		if (a == 0) {
			return;
		}
		vertexConsumer.vertex(matrix4f, x1, y1, z1).color(r, g, b, a).texture(u1, v2).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, vec3i.getX(), vec3i.getY(), vec3i.getZ()).next();
		vertexConsumer.vertex(matrix4f, x2, y2, z2).color(r, g, b, a).texture(u2, v2).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, vec3i.getX(), vec3i.getY(), vec3i.getZ()).next();
		vertexConsumer.vertex(matrix4f, x3, y3, z3).color(r, g, b, a).texture(u2, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, vec3i.getX(), vec3i.getY(), vec3i.getZ()).next();
		vertexConsumer.vertex(matrix4f, x4, y4, z4).color(r, g, b, a).texture(u1, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, vec3i.getX(), vec3i.getY(), vec3i.getZ()).next();
	}

	BlockModelRenderer blockModelRenderer = new BlockModelRenderer(null);
	BlockModelRenderer.AmbientOcclusionCalculator aoCalculator = blockModelRenderer.new AmbientOcclusionCalculator();

	static void drawBlockFace(MatrixStack matrices, VertexConsumer vertexConsumer,
		  float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4,
		  float u1, float v1, float u2, float v2, float u3, float v3, float u4, float v4,
		  Direction facing, BlockPos pos, World world, int color) {

		final BlockState bs = world.getBlockState(pos);
		final float yMax = Math.max(Math.max(y1, y2), Math.max(y3, y4));
		BlockPos lightRefPos;
		if (facing.getAxis() != Direction.Axis.Y) {
			lightRefPos = pos.offset(facing);
			// Sometimes on very steep slopes, a segment can cover multiple blocks horizontally.
			// This is to check an upper part when lower part is blocked, to prevent black faces from being shown.
			// Because such a steep slope is rarely used, it's not that necessary to divide it into multiple faces.
			while (!world.getBlockState(lightRefPos).isAir()) {
				if (lightRefPos.getY() <= pos.getY() + yMax) {
					lightRefPos = lightRefPos.up();
				} else {
					break;
				}
			}

			// Update the positions to reject the invisible parts, making the lighting area correct.
			final float yRefOffset = lightRefPos.getY() - pos.getY();
			if (yRefOffset > 0) {
				// This assumes y2 and y3 is at the bottom.
				y2 = Math.min(y2, yRefOffset);
				y3 = Math.min(y3, yRefOffset);
			}
		} else {
			lightRefPos = pos;
		}

		int[] vertexData = new int[] {
				Float.floatToIntBits(x1), Float.floatToIntBits(y1), Float.floatToIntBits(z1), 0, Float.floatToIntBits(u1), Float.floatToIntBits(v1), 0, 0,
				Float.floatToIntBits(x2), Float.floatToIntBits(y2), Float.floatToIntBits(z2), 0, Float.floatToIntBits(u2), Float.floatToIntBits(v2), 0, 0,
				Float.floatToIntBits(x3), Float.floatToIntBits(y3), Float.floatToIntBits(z3), 0, Float.floatToIntBits(u3), Float.floatToIntBits(v3), 0, 0,
				Float.floatToIntBits(x4), Float.floatToIntBits(y4), Float.floatToIntBits(z4), 0, Float.floatToIntBits(u4), Float.floatToIntBits(v4), 0, 0,
		};
		BakedQuad quad = new BakedQuad(vertexData, 0, facing, null, true);

		final float r = ((color >> 16) & 0xFF) / 255F;
		final float g = ((color >> 8) & 0xFF) / 255F;
		final float b = (color & 0xFF) / 255F;

		if (MinecraftClient.isAmbientOcclusionEnabled() && (facing.getAxis() != Direction.Axis.Y)) {
			BitSet flags = new BitSet(3);
			float[] box = new float[Direction.values().length * 2];
			blockModelRenderer.getQuadDimensions(world, bs, lightRefPos.offset(facing.getOpposite()), vertexData, facing, box, flags);
			aoCalculator.apply(world, bs, lightRefPos.offset(facing.getOpposite()), facing, box, flags, true);
			vertexConsumer.quad(matrices.peek(), quad,
					new float[]{aoCalculator.brightness[0], aoCalculator.brightness[1], aoCalculator.brightness[2], aoCalculator.brightness[3]},
					r, g, b,
					new int[]{aoCalculator.light[0], aoCalculator.light[1], aoCalculator.light[2], aoCalculator.light[3]},
					0, false
			);
		} else {
			final int light = WorldRenderer.getLightmapCoordinates(world, lightRefPos);
			final float brightness = world.getBrightness(facing, true);
			vertexConsumer.quad(matrices.peek(), quad,
					new float[]{brightness, brightness, brightness, brightness},
					r, g, b,
					new int[]{light, light, light, light},
					0, false
			);
		}
	}

	static void setPositionAndWidth(ClickableWidget widget, int x, int y, int widgetWidth) {
		widget.x = x;
		widget.y = y;
		widget.setWidth(widgetWidth);
	}
}
