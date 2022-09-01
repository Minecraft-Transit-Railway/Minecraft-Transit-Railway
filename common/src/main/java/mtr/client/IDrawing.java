package mtr.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.text2speech.Narrator;
import mtr.MTR;
import mtr.data.IGui;
import mtr.mappings.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public interface IDrawing {

	static void drawStringWithFont(PoseStack matrices, Font textRenderer, MultiBufferSource.BufferSource immediate, String text, float x, float y, int light) {
		drawStringWithFont(matrices, textRenderer, immediate, text, IGui.HorizontalAlignment.CENTER, IGui.VerticalAlignment.CENTER, x, y, 1, IGui.ARGB_WHITE, true, light, null);
	}

	static void drawStringWithFont(PoseStack matrices, Font textRenderer, MultiBufferSource.BufferSource immediate, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, float x, float y, float scale, int textColor, boolean shadow, int light, DrawingCallback drawingCallback) {
		drawStringWithFont(matrices, textRenderer, immediate, text, horizontalAlignment, verticalAlignment, horizontalAlignment, x, y, -1, -1, scale, textColor, shadow, light, drawingCallback);
	}

	static void drawStringWithFont(PoseStack matrices, Font textRenderer, MultiBufferSource.BufferSource immediate, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColor, boolean shadow, int light, DrawingCallback drawingCallback) {
		drawStringWithFont(matrices, textRenderer, immediate, text, horizontalAlignment, verticalAlignment, horizontalAlignment, x, y, maxWidth, maxHeight, scale, textColor, shadow, light, drawingCallback);
	}

	static void drawStringWithFont(PoseStack matrices, Font textRenderer, MultiBufferSource.BufferSource immediate, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, IGui.HorizontalAlignment xAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColor, boolean shadow, int light, DrawingCallback drawingCallback) {
		final Style style = Config.useMTRFont() ? Style.EMPTY.withFont(new ResourceLocation(MTR.MOD_ID, "mtr")) : Style.EMPTY;

		while (text.contains("||")) {
			text = text.replace("||", "|");
		}
		final String[] stringSplit = text.split("\\|");

		final List<Boolean> isCJKList = new ArrayList<>();
		final List<FormattedCharSequence> orderedTexts = new ArrayList<>();
		int totalHeight = 0, totalWidth = 0;
		for (final String stringSplitPart : stringSplit) {
			final boolean isCJK = IGui.isCjk(stringSplitPart);
			isCJKList.add(isCJK);

			final FormattedCharSequence orderedText = Text.literal(stringSplitPart).setStyle(style).getVisualOrderText();
			orderedTexts.add(orderedText);

			totalHeight += IGui.LINE_HEIGHT * (isCJK ? 2 : 1);
			final int width = textRenderer.width(orderedText) * (isCJK ? 2 : 1);
			if (width > totalWidth) {
				totalWidth = width;
			}
		}

		if (maxHeight >= 0 && totalHeight / scale > maxHeight) {
			scale = totalHeight / maxHeight;
		}

		matrices.pushPose();

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
				matrices.pushPose();
				matrices.scale(extraScale, extraScale, 1);
			}

			final float xOffset = horizontalAlignment.getOffset(xAlignment.getOffset(x * scaleX, totalWidth), textRenderer.width(orderedTexts.get(i)) * extraScale - totalWidth);

			final float shade = light == IGui.MAX_LIGHT_GLOWING ? 1 : Math.min(LightTexture.block(light) / 16F * 0.1F + 0.7F, 1);
			final int a = (textColor >> 24) & 0xFF;
			final int r = (int) (((textColor >> 16) & 0xFF) * shade);
			final int g = (int) (((textColor >> 8) & 0xFF) * shade);
			final int b = (int) ((textColor & 0xFF) * shade);

			if (immediate != null) {
				textRenderer.drawInBatch(orderedTexts.get(i), xOffset / extraScale, offset / extraScale, (a << 24) + (r << 16) + (g << 8) + b, shadow, matrices.last().pose(), immediate, false, 0, light);
			}

			if (isCJK) {
				matrices.popPose();
			}

			offset += IGui.LINE_HEIGHT * extraScale;
		}

		matrices.popPose();

		if (drawingCallback != null) {
			final float x1 = xAlignment.getOffset(x, totalWidthScaled / scale);
			final float y1 = verticalAlignment.getOffset(y, totalHeight / scale);
			drawingCallback.drawingCallback(x1, y1, x1 + totalWidthScaled / scale, y1 + totalHeight / scale);
		}
	}

	static void drawLine(PoseStack matrices, MultiBufferSource vertexConsumers, float x1, float y1, float z1, float x2, float y2, float z2, int r, int g, int b) {
		final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderType.lines());
		final Matrix4f matrix4f = matrices.last().pose();
		final Matrix3f matrix3f = matrices.last().normal();
		vertexConsumer.vertex(matrix4f, x1, y1, z1).color(r, g, b, 0xFF).normal(matrix3f, 0, 1, 0).endVertex();
		vertexConsumer.vertex(matrix4f, x2, y2, z2).color(r, g, b, 0xFF).normal(matrix3f, 0, 1, 0).endVertex();
	}

	static void drawRectangle(VertexConsumer vertexConsumer, double x1, double y1, double x2, double y2, int color) {
		final int a = (color >> 24) & 0xFF;
		final int r = (color >> 16) & 0xFF;
		final int g = (color >> 8) & 0xFF;
		final int b = color & 0xFF;
		if (a == 0) {
			return;
		}
		vertexConsumer.vertex(x1, y1, 0).color(r, g, b, a).endVertex();
		vertexConsumer.vertex(x1, y2, 0).color(r, g, b, a).endVertex();
		vertexConsumer.vertex(x2, y2, 0).color(r, g, b, a).endVertex();
		vertexConsumer.vertex(x2, y1, 0).color(r, g, b, a).endVertex();
	}

	static void drawTexture(PoseStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, Direction facing, int color, int light) {
		drawTexture(matrices, vertexConsumer, x1, y1, z1, x2, y2, z2, 0, 0, 1, 1, facing, color, light);
	}

	static void drawTexture(PoseStack matrices, VertexConsumer vertexConsumer, float x, float y, float width, float height, Direction facing, int light) {
		drawTexture(matrices, vertexConsumer, x, y, 0, x + width, y + height, 0, 0, 0, 1, 1, facing, -1, light);
	}

	static void drawTexture(PoseStack matrices, VertexConsumer vertexConsumer, float x, float y, float width, float height, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		drawTexture(matrices, vertexConsumer, x, y, 0, x + width, y + height, 0, u1, v1, u2, v2, facing, color, light);
	}

	static void drawTexture(PoseStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		drawTexture(matrices, vertexConsumer, x1, y2, z1, x2, y2, z2, x2, y1, z2, x1, y1, z1, u1, v1, u2, v2, facing, color, light);
	}

	static void drawTexture(PoseStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		final Vec3i vec3i = facing.getNormal();
		final Matrix4f matrix4f = matrices.last().pose();
		final Matrix3f matrix3f = matrices.last().normal();
		final int a = (color >> 24) & 0xFF;
		final int r = (color >> 16) & 0xFF;
		final int g = (color >> 8) & 0xFF;
		final int b = color & 0xFF;
		if (a == 0) {
			return;
		}
		vertexConsumer.vertex(matrix4f, x1, y1, z1).color(r, g, b, a).uv(u1, v2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, vec3i.getX(), vec3i.getY(), vec3i.getZ()).endVertex();
		vertexConsumer.vertex(matrix4f, x2, y2, z2).color(r, g, b, a).uv(u2, v2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, vec3i.getX(), vec3i.getY(), vec3i.getZ()).endVertex();
		vertexConsumer.vertex(matrix4f, x3, y3, z3).color(r, g, b, a).uv(u2, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, vec3i.getX(), vec3i.getY(), vec3i.getZ()).endVertex();
		vertexConsumer.vertex(matrix4f, x4, y4, z4).color(r, g, b, a).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, vec3i.getX(), vec3i.getY(), vec3i.getZ()).endVertex();
	}

	static void setPositionAndWidth(AbstractWidget widget, int x, int y, int widgetWidth) {
		widget.x = x;
		widget.y = y;
		widget.setWidth(Mth.clamp(widgetWidth, 0, 380));
	}

	static void narrateOrAnnounce(String message) {
		String newMessage = IGui.formatStationName(message).replace("  ", " ");
		if (!newMessage.isEmpty()) {
			if (Config.useTTSAnnouncements()) {
				Narrator.getNarrator().say(newMessage, true);
			}
			if (Config.showAnnouncementMessages()) {
				final Player player = Minecraft.getInstance().player;
				if (player != null) {
					player.displayClientMessage(Text.literal(newMessage), false);
				}
			}
		}
	}

	@FunctionalInterface
	interface DrawingCallback {
		void drawingCallback(float x1, float y1, float x2, float y2);
	}
}
