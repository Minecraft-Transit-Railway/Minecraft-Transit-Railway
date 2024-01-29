package org.mtr.mod.client;

import com.mojang.text2speech.Narrator;
import org.mtr.libraries.it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mod.Init;
import org.mtr.mod.data.IGui;

import javax.annotation.Nullable;

public interface IDrawing {

	static void drawStringWithFont(GraphicsHolder graphicsHolder, String text, float x, float y, int light) {
		drawStringWithFont(graphicsHolder, text, IGui.HorizontalAlignment.CENTER, IGui.VerticalAlignment.CENTER, x, y, -1, -1, 1, IGui.ARGB_WHITE, true, light, null);
	}

	static void drawStringWithFont(GraphicsHolder graphicsHolder, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColor, boolean shadow, int light, @Nullable DrawingCallback drawingCallback) {
		drawStringWithFont(graphicsHolder, text, horizontalAlignment, verticalAlignment, horizontalAlignment, x, y, maxWidth, maxHeight, scale, textColor, shadow, light, drawingCallback);
	}

	static void drawStringWithFont(GraphicsHolder graphicsHolder, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, IGui.HorizontalAlignment xAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColor, boolean shadow, int light, @Nullable DrawingCallback drawingCallback) {
		drawStringWithFont(graphicsHolder, text, horizontalAlignment, verticalAlignment, xAlignment, x, y, maxWidth, maxHeight, scale, textColor, textColor, 2, shadow, light, drawingCallback);
	}

	static void drawStringWithFont(GraphicsHolder graphicsHolder, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, IGui.HorizontalAlignment xAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColorCjk, int textColor, float fontSizeRatio, boolean shadow, int light, @Nullable DrawingCallback drawingCallback) {
		final Style style = Config.USE_MTR_FONT.get() ? Style.getEmptyMapped().withFont(new Identifier(Init.MOD_ID, "mtr")) : Style.getEmptyMapped();

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

			final OrderedText orderedText = TextHelper.mutableTextToOrderedText(TextHelper.setStyle(TextHelper.literal(stringSplitPart), style));
			orderedTexts.add(orderedText);

			totalHeight += Math.round(IGui.LINE_HEIGHT * (isCJK ? fontSizeRatio : 1));
			final int width = (int) Math.ceil(GraphicsHolder.getTextWidth(orderedText) * (isCJK ? fontSizeRatio : 1));
			if (width > totalWidth) {
				totalWidth = width;
			}
		}

		if (maxHeight >= 0 && totalHeight / scale > maxHeight) {
			scale = totalHeight / maxHeight;
		}

		graphicsHolder.push();

		final float totalWidthScaled;
		final float scaleX;
		if (maxWidth >= 0 && totalWidth > maxWidth * scale) {
			totalWidthScaled = maxWidth * scale;
			scaleX = totalWidth / maxWidth;
		} else {
			totalWidthScaled = totalWidth;
			scaleX = scale;
		}
		graphicsHolder.scale(1 / scaleX, 1 / scale, 1 / scale);

		float offset = verticalAlignment.getOffset(y * scale, totalHeight);
		for (int i = 0; i < orderedTexts.size(); i++) {
			final boolean isCJK = isCJKList.getBoolean(i);
			final float extraScale = isCJK ? fontSizeRatio : 1;
			if (isCJK) {
				graphicsHolder.push();
				graphicsHolder.scale(extraScale, extraScale, 1);
			}

			final float xOffset = horizontalAlignment.getOffset(xAlignment.getOffset(x * scaleX, totalWidth), GraphicsHolder.getTextWidth(orderedTexts.get(i)) * extraScale - totalWidth);

			final float shade = light == IGui.MAX_LIGHT_GLOWING ? 1 : Math.min(LightmapTextureManager.getBlockLightCoordinates(light) / 16F * 0.1F + 0.7F, 1);
			final int a = ((isCJK ? textColorCjk : textColor) >> 24) & 0xFF;
			final int r = (int) ((((isCJK ? textColorCjk : textColor) >> 16) & 0xFF) * shade);
			final int g = (int) ((((isCJK ? textColorCjk : textColor) >> 8) & 0xFF) * shade);
			final int b = (int) (((isCJK ? textColorCjk : textColor) & 0xFF) * shade);

			graphicsHolder.drawText(orderedTexts.get(i), Math.round(xOffset / extraScale), Math.round(offset / extraScale), (a << 24) + (r << 16) + (g << 8) + b, shadow, light);

			if (isCJK) {
				graphicsHolder.pop();
			}

			offset += IGui.LINE_HEIGHT * extraScale;
		}

		graphicsHolder.pop();

		if (drawingCallback != null) {
			final float x1 = xAlignment.getOffset(x, totalWidthScaled / scale);
			final float y1 = verticalAlignment.getOffset(y, totalHeight / scale);
			drawingCallback.drawingCallback(x1, y1, x1 + totalWidthScaled / scale, y1 + totalHeight / scale);
		}
	}

	static void drawTexture(GraphicsHolder graphicsHolder, float x1, float y1, float z1, float x2, float y2, float z2, Direction facing, int color, int light) {
		drawTexture(graphicsHolder, x1, y1, z1, x2, y2, z2, 0, 0, 1, 1, facing, color, light);
	}

	static void drawTexture(GraphicsHolder graphicsHolder, float x, float y, float width, float height, Direction facing, int light) {
		drawTexture(graphicsHolder, x, y, 0, x + width, y + height, 0, 0, 0, 1, 1, facing, -1, light);
	}

	static void drawTexture(GraphicsHolder graphicsHolder, float x, float y, float width, float height, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		drawTexture(graphicsHolder, x, y, 0, x + width, y + height, 0, u1, v1, u2, v2, facing, color, light);
	}

	static void drawTexture(GraphicsHolder graphicsHolder, float x1, float y1, float z1, float x2, float y2, float z2, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		drawTexture(graphicsHolder, x1, y2, z1, x2, y2, z2, x2, y1, z2, x1, y1, z1, u1, v1, u2, v2, facing, color, light);
	}

	static void drawTexture(GraphicsHolder graphicsHolder, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, Vector3d playerOffset, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		drawTexture(
				graphicsHolder,
				(float) (x1 - playerOffset.getXMapped()), (float) (y1 - playerOffset.getYMapped()), (float) (z1 - playerOffset.getZMapped()),
				(float) (x2 - playerOffset.getXMapped()), (float) (y2 - playerOffset.getYMapped()), (float) (z2 - playerOffset.getZMapped()),
				(float) (x3 - playerOffset.getXMapped()), (float) (y3 - playerOffset.getYMapped()), (float) (z3 - playerOffset.getZMapped()),
				(float) (x4 - playerOffset.getXMapped()), (float) (y4 - playerOffset.getYMapped()), (float) (z4 - playerOffset.getZMapped()),
				u1, v1, u2, v2, facing, color, light
		);
	}

	static void drawTexture(GraphicsHolder graphicsHolder, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		graphicsHolder.drawTextureInWorld(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, u1, v1, u2, v2, facing, color, light);
	}

	static void setPositionAndWidth(ButtonWidgetExtension widget, int x, int y, int widgetWidth) {
		widget.setX2(x);
		widget.setY2(y);
		widget.setWidth2(MathHelper.clamp(widgetWidth, 0, 380));
	}

	static void setPositionAndWidth(CheckboxWidgetExtension widget, int x, int y, int widgetWidth) {
		widget.setX2(x);
		widget.setY2(y);
		widget.setWidth2(MathHelper.clamp(widgetWidth, 0, 380));
	}

	static void setPositionAndWidth(TextFieldWidgetExtension widget, int x, int y, int widgetWidth) {
		widget.setX2(x);
		widget.setY2(y);
		widget.setWidth2(MathHelper.clamp(widgetWidth, 0, 380 - IGui.TEXT_FIELD_PADDING));
	}

	static void setPositionAndWidth(TexturedButtonWidgetExtension widget, int x, int y, int widgetWidth) {
		widget.setX2(x);
		widget.setY2(y);
		widget.setWidth2(MathHelper.clamp(widgetWidth, 0, 380));
	}

	static void setPositionAndWidth(SliderWidgetExtension widget, int x, int y, int widgetWidth) {
		widget.setX2(x);
		widget.setY2(y);
		widget.setWidth2(MathHelper.clamp(widgetWidth, 0, 380));
	}

	static void narrateOrAnnounce(String message) {
		String newMessage = IGui.formatStationName(message).replace("  ", " ");
		if (!newMessage.isEmpty()) {
			if (Config.USE_TTS_ANNOUNCEMENTS.get()) {
				Narrator.getNarrator().say(newMessage, true);
			}
			if (Config.SHOW_ANNOUNCEMENT_MESSAGES.get()) {
				final ClientPlayerEntity player = MinecraftClient.getInstance().getPlayerMapped();
				if (player != null) {
					player.sendMessage(new Text(TextHelper.literal(newMessage).data), false);
				}
			}
		}
	}

	@FunctionalInterface
	interface DrawingCallback {
		void drawingCallback(float x1, float y1, float x2, float y2);
	}
}
