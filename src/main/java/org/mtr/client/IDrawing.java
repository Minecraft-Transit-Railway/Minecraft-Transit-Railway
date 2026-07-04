package org.mtr.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.text2speech.Narrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.config.Config;
import org.mtr.data.IGui;
import org.mtr.font.FontRenderOptions;
import org.mtr.libraries.it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.awt.*;

public interface IDrawing {

	static void drawStringWithFont(PoseStack matrixStack, MultiBufferSource vertexConsumers, String text, float x, float y, int light) {
		drawStringWithFont(matrixStack, vertexConsumers, text, IGui.HorizontalAlignment.CENTER, IGui.VerticalAlignment.CENTER, x, y, -1, -1, 1, IGui.ARGB_WHITE, true, light, null);
	}

	static void drawStringWithFont(PoseStack matrixStack, MultiBufferSource vertexConsumers, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColor, boolean shadow, int light, @Nullable DrawingCallback drawingCallback) {
		drawStringWithFont(matrixStack, vertexConsumers, text, horizontalAlignment, verticalAlignment, horizontalAlignment, x, y, maxWidth, maxHeight, scale, textColor, shadow, light, drawingCallback);
	}

	static void drawStringWithFont(PoseStack matrixStack, MultiBufferSource vertexConsumers, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, IGui.HorizontalAlignment xAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColor, boolean shadow, int light, @Nullable DrawingCallback drawingCallback) {
		drawStringWithFont(matrixStack, vertexConsumers, text, horizontalAlignment, verticalAlignment, xAlignment, x, y, maxWidth, maxHeight, scale, textColor, textColor, 2, shadow, light, drawingCallback);
	}

	static void drawStringWithFont(PoseStack matrixStack, MultiBufferSource vertexConsumers, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, IGui.HorizontalAlignment xAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColorCjk, int textColor, float fontSizeRatio, boolean shadow, int light, @Nullable DrawingCallback drawingCallback) {
		final Style style = Config.getClient().getUseMTRFont() ? Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "mtr")) : Style.EMPTY;

		while (text.contains("||")) {
			text = text.replace("||", "|");
		}
		final String[] stringSplit = text.split("\\|");

		final BooleanArrayList isCJKList = new BooleanArrayList();
		final ObjectArrayList<FormattedCharSequence> orderedTexts = new ObjectArrayList<>();
		int totalHeight = 0, totalWidth = 0;
		for (final String stringSplitPart : stringSplit) {
			final boolean isCJK = IGui.isCjk(stringSplitPart);
			isCJKList.add(isCJK);

			final FormattedCharSequence orderedText = Component.literal(stringSplitPart).setStyle(style).getVisualOrderText();
			orderedTexts.add(orderedText);

			totalHeight += Math.round(IGui.LINE_HEIGHT * (isCJK ? fontSizeRatio : 1));
			final int width = (int) Math.ceil(Minecraft.getInstance().font.width(orderedText) * (isCJK ? fontSizeRatio : 1));
			if (width > totalWidth) {
				totalWidth = width;
			}
		}

		if (maxHeight >= 0 && totalHeight / scale > maxHeight) {
			scale = totalHeight / maxHeight;
		}

		matrixStack.pushPose();

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
				matrixStack.pushPose();
				matrixStack.scale(extraScale, extraScale, 1);
			}

			final Font textRenderer = Minecraft.getInstance().font;
			final float xOffset = horizontalAlignment.getOffset(xAlignment.getOffset(x * scaleX, totalWidth), textRenderer.width(orderedTexts.get(i)) * extraScale - totalWidth);

			final float shade = light == IGui.DEFAULT_LIGHT ? 1 : Math.min(LightTexture.block(light) / 16F * 0.1F + 0.7F, 1);
			final int a = ((isCJK ? textColorCjk : textColor) >> 24) & 0xFF;
			final int r = (int) ((((isCJK ? textColorCjk : textColor) >> 16) & 0xFF) * shade);
			final int g = (int) ((((isCJK ? textColorCjk : textColor) >> 8) & 0xFF) * shade);
			final int b = (int) (((isCJK ? textColorCjk : textColor) & 0xFF) * shade);

			textRenderer.drawInBatch(orderedTexts.get(i), Math.round(xOffset / extraScale), Math.round(offset / extraScale), (a << 24) + (r << 16) + (g << 8) + b, shadow, matrixStack.last().pose(), vertexConsumers, Font.DisplayMode.NORMAL, 0, IGui.DEFAULT_LIGHT);

			if (isCJK) {
				matrixStack.popPose();
			}

			offset += IGui.LINE_HEIGHT * extraScale;
		}

		matrixStack.popPose();

		if (drawingCallback != null) {
			final float x1 = xAlignment.getOffset(x, totalWidthScaled / scale);
			final float y1 = verticalAlignment.getOffset(y, totalHeight / scale);
			drawingCallback.drawingCallback(x1, y1, x1 + totalWidthScaled / scale, y1 + totalHeight / scale);
		}
	}

	static void drawTexture(PoseStack matrixStack, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, Direction facing, int color, int light) {
		drawTexture(matrixStack, vertexConsumer, x1, y1, z1, x2, y2, z2, 0, 0, 1, 1, facing, color, light);
	}

	static void drawTexture(PoseStack matrixStack, VertexConsumer vertexConsumer, float x, float y, float width, float height, Direction facing, int light) {
		drawTexture(matrixStack, vertexConsumer, x, y, 0, x + width, y + height, 0, 0, 0, 1, 1, facing, -1, light);
	}

	static void drawTexture(PoseStack matrixStack, VertexConsumer vertexConsumer, float x, float y, float width, float height, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		drawTexture(matrixStack, vertexConsumer, x, y, 0, x + width, y + height, 0, u1, v1, u2, v2, facing, color, light);
	}

	static void drawTexture(PoseStack matrixStack, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		drawTexture(matrixStack, vertexConsumer, x1, y1, z1, x1, y2, z1, x2, y2, z2, x2, y1, z2, u1, v1, u2, v2, facing, color, light);
	}

	static void drawTexture(PoseStack matrixStack, VertexConsumer vertexConsumer, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, Vec3 playerOffset, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		drawTexture(
			matrixStack, vertexConsumer,
			(float) (x1 - playerOffset.x), (float) (y1 - playerOffset.y), (float) (z1 - playerOffset.z),
			(float) (x2 - playerOffset.x), (float) (y2 - playerOffset.y), (float) (z2 - playerOffset.z),
			(float) (x3 - playerOffset.x), (float) (y3 - playerOffset.y), (float) (z3 - playerOffset.z),
			(float) (x4 - playerOffset.x), (float) (y4 - playerOffset.y), (float) (z4 - playerOffset.z),
			u1, v1, u2, v2, facing, color, light
		);
	}

	static void drawTexture(PoseStack matrixStack, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		vertexConsumer.addVertex(matrixStack.last().pose(), x1, y1, z1).setColor(color).setUv(u1, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 1, 0);
		vertexConsumer.addVertex(matrixStack.last().pose(), x2, y2, z2).setColor(color).setUv(u1, v2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 1, 0);
		vertexConsumer.addVertex(matrixStack.last().pose(), x3, y3, z3).setColor(color).setUv(u2, v2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 1, 0);
		vertexConsumer.addVertex(matrixStack.last().pose(), x4, y4, z4).setColor(color).setUv(u2, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 1, 0);
	}

	static void drawLineInWorld(PoseStack matrixStack, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, int color) {
		if (new Color(color, true).getAlpha() > 0) {
			final PoseStack.Pose entry = matrixStack.last();
			final Matrix4f matrix4f = entry.pose();

			vertexConsumer.addVertex(matrix4f, x1, y1, z1).setColor(color).setNormal(entry, 0, 1, 0);
			vertexConsumer.addVertex(matrix4f, x2, y2, z2).setColor(color).setNormal(entry, 0, 1, 0);
		}
	}

	static void drawSevenSegment(PoseStack matrixStack, VertexConsumer vertexConsumer, String numberString, float availableSpace, float x, float y, float height, FontRenderOptions.Alignment horizontalAlignment, int color, int light) {
		try {
			drawSevenSegment(matrixStack, vertexConsumer, Integer.parseInt(numberString), availableSpace, x, y, height, horizontalAlignment, color, light);
		} catch (Exception ignored) {
		}
	}

	static void drawSevenSegment(PoseStack matrixStack, VertexConsumer vertexConsumer, int number, float availableSpace, float x, float y, float height, FontRenderOptions.Alignment horizontalAlignment, int color, int light) {
		// Negatives and decimals are not supported right now
		final float u = 0.25F;
		final float v = 170F / 512;
		final float paddingMultiplier = 1.2F;
		final float digitWidth = height * u / v * paddingMultiplier;
		final int digits = (int) Math.floor(availableSpace / digitWidth);
		final float startX = x + horizontalAlignment.getOffset(digits * digitWidth - availableSpace);

		for (int i = 0; i < digits; i++) {
			final int digit = (number / (int) Math.pow(10, digits - i - 1)) % 10;
			final float digitX = startX + digitWidth * i;
			final float digitU = (digit % 4) * u;
			final float digitV = Math.floorDiv(digit, 4) * v;
			final float x1 = digitX + (paddingMultiplier - 1) * digitWidth / 2;
			final float x2 = x1 + digitWidth / paddingMultiplier;
			vertexConsumer.addVertex(matrixStack.last().pose(), x1, y, 0).setColor(color).setUv(digitU, digitV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 1, 0);
			vertexConsumer.addVertex(matrixStack.last().pose(), x1, y + height, 0).setColor(color).setUv(digitU, digitV + v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 1, 0);
			vertexConsumer.addVertex(matrixStack.last().pose(), x2, y + height, 0).setColor(color).setUv(digitU + u, digitV + v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 1, 0);
			vertexConsumer.addVertex(matrixStack.last().pose(), x2, y, 0).setColor(color).setUv(digitU + u, digitV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 1, 0);
		}
	}

	static void setPositionAndWidth(AbstractWidget widget, int x, int y, int widgetWidth) {
		widget.setX(x);
		widget.setY(y);
		widget.setWidth(Math.clamp(widgetWidth, 0, 380));
	}

	static void narrateOrAnnounce(String narrateMessage, ObjectArrayList<MutableComponent> chatMessages) {
		if (Config.getClient().getTextToSpeechAnnouncements() && !narrateMessage.isEmpty()) {
			Narrator.getNarrator().say(narrateMessage, true);
		}
		if (Config.getClient().getChatAnnouncements() && !chatMessages.isEmpty()) {
			final LocalPlayer player = Minecraft.getInstance().player;
			if (player != null) {
				chatMessages.forEach(chatMessage -> {
					if (!chatMessage.getString().isEmpty()) {
						player.displayClientMessage(chatMessage, false);
					}
				});
			}
		}
	}

	static MutableComponent withMTRFont(MutableComponent text) {
		return Config.getClient().getUseMTRFont() ? text.setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "mtr"))) : text;
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
