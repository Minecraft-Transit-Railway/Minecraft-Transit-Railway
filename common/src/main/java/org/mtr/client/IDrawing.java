package org.mtr.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.text2speech.Narrator;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.mtr.MTR;
import org.mtr.config.Config;
import org.mtr.data.IGui;

import javax.annotation.Nullable;
import java.awt.*;

public interface IDrawing {

	static void drawStringWithFont(MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, String text, float x, float y, int light) {
		drawStringWithFont(matrixStack, vertexConsumers, text, IGui.HorizontalAlignment.CENTER, IGui.VerticalAlignment.CENTER, x, y, -1, -1, 1, IGui.ARGB_WHITE, true, light, null);
	}

	static void drawStringWithFont(MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColor, boolean shadow, int light, @Nullable DrawingCallback drawingCallback) {
		drawStringWithFont(matrixStack, vertexConsumers, text, horizontalAlignment, verticalAlignment, horizontalAlignment, x, y, maxWidth, maxHeight, scale, textColor, shadow, light, drawingCallback);
	}

	static void drawStringWithFont(MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, IGui.HorizontalAlignment xAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColor, boolean shadow, int light, @Nullable DrawingCallback drawingCallback) {
		drawStringWithFont(matrixStack, vertexConsumers, text, horizontalAlignment, verticalAlignment, xAlignment, x, y, maxWidth, maxHeight, scale, textColor, textColor, 2, shadow, light, drawingCallback);
	}

	static void drawStringWithFont(MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, IGui.HorizontalAlignment xAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColorCjk, int textColor, float fontSizeRatio, boolean shadow, int light, @Nullable DrawingCallback drawingCallback) {
		final Style style = Config.getClient().getUseMTRFont() ? Style.EMPTY.withFont(Identifier.of(MTR.MOD_ID, "mtr")) : Style.EMPTY;

		while (text.contains("||")) {
			text = text.replace("||", "|");
		}
		final String[] stringSplit = text.split("\\|");

		final BooleanArrayList isCJKList = new BooleanArrayList();
		final ObjectArrayList<OrderedText> orderedTexts = new ObjectArrayList<>();
		int totalHeight = 0, totalWidth = 0;
		for (final String stringSplitPart : stringSplit) {
			final boolean isCJK = IGui.isCjk(stringSplitPart);
			isCJKList.add(isCJK);

			final OrderedText orderedText = Text.literal(stringSplitPart).setStyle(style).asOrderedText();
			orderedTexts.add(orderedText);

			totalHeight += Math.round(IGui.LINE_HEIGHT * (isCJK ? fontSizeRatio : 1));
			final int width = (int) Math.ceil(MinecraftClient.getInstance().textRenderer.getWidth(orderedText) * (isCJK ? fontSizeRatio : 1));
			if (width > totalWidth) {
				totalWidth = width;
			}
		}

		if (maxHeight >= 0 && totalHeight / scale > maxHeight) {
			scale = totalHeight / maxHeight;
		}

		matrixStack.push();

		final float totalWidthScaled;
		final float scaleX;
		if (maxWidth >= 0 && totalWidth > maxWidth * scale) {
			totalWidthScaled = maxWidth * scale;
			scaleX = totalWidth / maxWidth;
		} else {
			totalWidthScaled = totalWidth;
			scaleX = scale;
		}
		matrixStack.scale(1 / scaleX, 1 / scale, 1 / scale);

		float offset = verticalAlignment.getOffset(y * scale, totalHeight);
		for (int i = 0; i < orderedTexts.size(); i++) {
			final boolean isCJK = isCJKList.getBoolean(i);
			final float extraScale = isCJK ? fontSizeRatio : 1;
			if (isCJK) {
				matrixStack.push();
				matrixStack.scale(extraScale, extraScale, 1);
			}

			final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
			final float xOffset = horizontalAlignment.getOffset(xAlignment.getOffset(x * scaleX, totalWidth), textRenderer.getWidth(orderedTexts.get(i)) * extraScale - totalWidth);

			final float shade = light == IGui.DEFAULT_LIGHT ? 1 : Math.min(LightmapTextureManager.getBlockLightCoordinates(light) / 16F * 0.1F + 0.7F, 1);
			final int a = ((isCJK ? textColorCjk : textColor) >> 24) & 0xFF;
			final int r = (int) ((((isCJK ? textColorCjk : textColor) >> 16) & 0xFF) * shade);
			final int g = (int) ((((isCJK ? textColorCjk : textColor) >> 8) & 0xFF) * shade);
			final int b = (int) (((isCJK ? textColorCjk : textColor) & 0xFF) * shade);

			textRenderer.draw(orderedTexts.get(i), Math.round(xOffset / extraScale), Math.round(offset / extraScale), (a << 24) + (r << 16) + (g << 8) + b, shadow, matrixStack.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, IGui.DEFAULT_LIGHT);

			if (isCJK) {
				matrixStack.pop();
			}

			offset += IGui.LINE_HEIGHT * extraScale;
		}

		matrixStack.pop();

		if (drawingCallback != null) {
			final float x1 = xAlignment.getOffset(x, totalWidthScaled / scale);
			final float y1 = verticalAlignment.getOffset(y, totalHeight / scale);
			drawingCallback.drawingCallback(x1, y1, x1 + totalWidthScaled / scale, y1 + totalHeight / scale);
		}
	}

	static void drawTexture(MatrixStack matrixStack, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, Direction facing, int color, int light) {
		drawTexture(matrixStack, vertexConsumer, x1, y1, z1, x2, y2, z2, 0, 0, 1, 1, facing, color, light);
	}

	static void drawTexture(MatrixStack matrixStack, VertexConsumer vertexConsumer, float x, float y, float width, float height, Direction facing, int light) {
		drawTexture(matrixStack, vertexConsumer, x, y, 0, x + width, y + height, 0, 0, 0, 1, 1, facing, -1, light);
	}

	static void drawTexture(MatrixStack matrixStack, VertexConsumer vertexConsumer, float x, float y, float width, float height, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		drawTexture(matrixStack, vertexConsumer, x, y, 0, x + width, y + height, 0, u1, v1, u2, v2, facing, color, light);
	}

	static void drawTexture(MatrixStack matrixStack, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		drawTexture(matrixStack, vertexConsumer, x1, y1, z1, x1, y2, z1, x2, y2, z2, x2, y1, z2, u1, v1, u2, v2, facing, color, light);
	}

	static void drawTexture(MatrixStack matrixStack, VertexConsumer vertexConsumer, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, Vec3d playerOffset, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		drawTexture(
				matrixStack, vertexConsumer,
				(float) (x1 - playerOffset.x), (float) (y1 - playerOffset.y), (float) (z1 - playerOffset.z),
				(float) (x2 - playerOffset.x), (float) (y2 - playerOffset.y), (float) (z2 - playerOffset.z),
				(float) (x3 - playerOffset.x), (float) (y3 - playerOffset.y), (float) (z3 - playerOffset.z),
				(float) (x4 - playerOffset.x), (float) (y4 - playerOffset.y), (float) (z4 - playerOffset.z),
				u1, v1, u2, v2, facing, color, light
		);
	}

	static void drawTexture(MatrixStack matrixStack, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		vertexConsumer.vertex(x1, y1, z1, color, u1, v1, OverlayTexture.DEFAULT_UV, light, 0, 1, 0);
		vertexConsumer.vertex(x2, y2, z2, color, u1, v2, OverlayTexture.DEFAULT_UV, light, 0, 1, 0);
		vertexConsumer.vertex(x3, y3, z3, color, u2, v2, OverlayTexture.DEFAULT_UV, light, 0, 1, 0);
		vertexConsumer.vertex(x4, y4, z4, color, u2, v1, OverlayTexture.DEFAULT_UV, light, 0, 1, 0);
	}

	static void drawLineInWorld(MatrixStack matrixStack, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, int color) {
		if (new Color(color, true).getAlpha() > 0) {
			final MatrixStack.Entry entry = matrixStack.peek();
			final Matrix4f matrix4f = entry.getPositionMatrix();

			vertexConsumer.vertex(matrix4f, x1, y1, z1).color(color).normal(entry, 0, 1, 0);
			vertexConsumer.vertex(matrix4f, x2, y2, z2).color(color).normal(entry, 0, 1, 0);
		}
	}

	static void drawSevenSegment(MatrixStack matrixStack, VertexConsumer vertexConsumer, String numberString, float availableSpace, float x, float y, float height, IGui.HorizontalAlignment horizontalAlignment, int color, int light) {
		try {
			drawSevenSegment(matrixStack, vertexConsumer, Integer.parseInt(numberString), availableSpace, x, y, height, horizontalAlignment, color, light);
		} catch (Exception ignored) {
		}
	}

	static void drawSevenSegment(MatrixStack matrixStack, VertexConsumer vertexConsumer, int number, float availableSpace, float x, float y, float height, IGui.HorizontalAlignment horizontalAlignment, int color, int light) {
		// Negatives and decimals are not supported right now
		final float u = 0.25F;
		final float v = 170F / 512;
		final float paddingMultiplier = 1.2F;
		final float digitWidth = height * u / v * paddingMultiplier;
		final int digits = (int) Math.floor(availableSpace / digitWidth);
		final float startX = horizontalAlignment.getOffset(x, digits * digitWidth - availableSpace);

		for (int i = 0; i < digits; i++) {
			final int digit = (number / (int) Math.pow(10, digits - i - 1)) % 10;
			final float digitX = startX + digitWidth * i;
			final float digitU = (digit % 4) * u;
			final float digitV = Math.floorDiv(digit, 4) * v;
			drawTexture(matrixStack, vertexConsumer, digitX + (paddingMultiplier - 1) * digitWidth / 2, y, digitWidth / paddingMultiplier, height, digitU, digitV, digitU + u, digitV + v, Direction.UP, color, light);
		}
	}

	static void rotateXRadians(MatrixStack matrixStack, float angle) {
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(angle));
	}

	static void rotateYRadians(MatrixStack matrixStack, float angle) {
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(angle));
	}

	static void rotateZRadians(MatrixStack matrixStack, float angle) {
		matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation(angle));
	}

	static void rotateXDegrees(MatrixStack matrixStack, float angle) {
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(angle));
	}

	static void rotateYDegrees(MatrixStack matrixStack, float angle) {
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle));
	}

	static void rotateZDegrees(MatrixStack matrixStack, float angle) {
		matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle));
	}

	static void setPositionAndWidth(ClickableWidget widget, int x, int y, int widgetWidth) {
		widget.setX(x);
		widget.setY(y);
		widget.setWidth(MathHelper.clamp(widgetWidth, 0, 380));
	}

	static void narrateOrAnnounce(String narrateMessage, ObjectArrayList<MutableText> chatMessages) {
		if (Config.getClient().getTextToSpeechAnnouncements() && !narrateMessage.isEmpty()) {
			Narrator.getNarrator().say(narrateMessage, true);
		}
		if (Config.getClient().getChatAnnouncements() && !chatMessages.isEmpty()) {
			final ClientPlayerEntity player = MinecraftClient.getInstance().player;
			if (player != null) {
				chatMessages.forEach(chatMessage -> {
					if (!chatMessage.getString().isEmpty()) {
						player.sendMessage(chatMessage, false);
					}
				});
			}
		}
	}

	static MutableText withMTRFont(MutableText text) {
		return Config.getClient().getUseMTRFont() ? text.setStyle(Style.EMPTY.withFont(Identifier.of(MTR.MOD_ID, "mtr"))) : text;
	}

	static void changeShaderColor(Color color, Runnable callback) {
		final float[] oldColor = RenderSystem.getShaderColor();
		final float r = oldColor[0];
		final float g = oldColor[1];
		final float b = oldColor[2];
		final float a = oldColor[3];
		RenderSystem.setShaderColor((float) color.getRed() / 0xFF, (float) color.getGreen() / 0xFF, (float) color.getBlue() / 0xFF, (float) color.getAlpha() / 0xFF);
		callback.run();
		RenderSystem.setShaderColor(r, g, b, a);
	}

	@FunctionalInterface
	interface DrawingCallback {
		void drawingCallback(float x1, float y1, float x2, float y2);
	}
}
