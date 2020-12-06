package mtr.gui;

import mtr.MTR;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

import java.util.stream.IntStream;

public interface IGui {

	int SQUARE_SIZE = 20;
	int PANEL_WIDTH = 144;
	int TEXT_HEIGHT = 8;
	int TEXT_PADDING = 6;
	int TEXT_FIELD_PADDING = 4;
	int LINE_HEIGHT = 10;

	float SMALL_OFFSET = 0.0001F;

	int RGB_WHITE = 0xFFFFFF;
	int ARGB_WHITE = 0xFFFFFFFF;
	int ARGB_BLACK = 0xFF000000;
	int ARGB_WHITE_TRANSLUCENT = 0x7FFFFFFF;
	int ARGB_BLACK_TRANSLUCENT = 0x7F000000;
	int ARGB_LIGHT_GRAY = 0xFFAAAAAA;
	int ARGB_BACKGROUND = 0xFF121212;

	static String formatStationName(String name) {
		return name.replace('|', ' ');
	}

	static String textOrUntitled(String text) {
		return text.isEmpty() ? new TranslatableText("gui.mtr.untitled").getString() : text;
	}

	static String formatVerticalChinese(String text) {
		final StringBuilder textBuilder = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			final boolean isChinese = Character.UnicodeScript.of(text.codePointAt(i)) == Character.UnicodeScript.HAN;
			if (isChinese) {
				textBuilder.append('|');
			}
			textBuilder.append(text, i, i + 1);
			if (isChinese) {
				textBuilder.append('|');
			}
		}

		String newText = textBuilder.toString();
		while (newText.contains("||")) {
			newText = newText.replace("||", "|");
		}

		return newText;
	}

	static void drawStringWithFont(MatrixStack matrices, TextRenderer textRenderer, String text, float x, float y) {
		drawStringWithFont(matrices, textRenderer, text, 1, 1, x, y, ARGB_WHITE, 1, null);
	}

	static void drawStringWithFont(MatrixStack matrices, TextRenderer textRenderer, String text, int horizontalAlignment, int verticalAlignment, float x, float y, int textColor, int drawStyle, DrawingCallback drawingCallback) {
		while (text.contains("||")) {
			text = text.replace("||", "|");
		}
		final String[] textSplit = text.split("\\|");

		final int[] lineHeights = new int[textSplit.length];
		for (int i = 0; i < textSplit.length; i++) {
			final boolean hasChinese = textSplit[i].codePoints().anyMatch(codePoint -> Character.UnicodeScript.of(codePoint) == Character.UnicodeScript.HAN);
			lineHeights[i] = LINE_HEIGHT * (hasChinese ? 2 : 1);
		}

		final Style style = Style.EMPTY.withFont(new Identifier(MTR.MOD_ID, "mtr"));
		final int totalHeight = IntStream.of(lineHeights).sum();
		int totalWidth = 0;
		float offset = y - verticalAlignment * totalHeight / 2F;
		for (int i = 0; i < textSplit.length; i++) {
			final OrderedText orderedText = new LiteralText(textSplit[i]).fillStyle(style).asOrderedText();
			final int textWidth = textRenderer.getWidth(orderedText);
			totalWidth = Math.max(textWidth, totalWidth);
			switch (drawStyle) {
				case 0:
					textRenderer.draw(matrices, orderedText, x - horizontalAlignment * textWidth / 2F, offset, textColor);
					break;
				case 1:
					textRenderer.drawWithShadow(matrices, orderedText, x - horizontalAlignment * textWidth / 2F, offset, textColor);
					break;
			}
			offset += lineHeights[i];
		}

		if (drawingCallback != null) {
			final float x1 = x - horizontalAlignment * totalWidth / 2F;
			final float y1 = y - verticalAlignment * totalHeight / 2F;
			drawingCallback.drawingCallback(x1, y1, x1 + totalWidth, y1 + totalHeight);
		}
	}

	static void setPositionAndWidth(AbstractButtonWidget widget, int x, int y, int widgetWidth) {
		widget.x = x;
		widget.y = y;
		widget.setWidth(widgetWidth);
	}

	static int divideColorRGB(int color, int amount) {
		final int r = ((color >> 16) & 0xFF) / amount;
		final int g = ((color >> 8) & 0xFF) / amount;
		final int b = (color & 0xFF) / amount;
		return (r << 16) + (g << 8) + b;
	}

	static void drawRectangle(VertexConsumer vertexConsumer, double x1, double y1, double x2, double y2, int color) {
		final int a = (color >> 24) & 0xFF;
		final int r = (color >> 16) & 0xFF;
		final int g = (color >> 8) & 0xFF;
		final int b = color & 0xFF;
		vertexConsumer.vertex(x1, y1, 0).color(r, g, b, a).next();
		vertexConsumer.vertex(x1, y2, 0).color(r, g, b, a).next();
		vertexConsumer.vertex(x2, y2, 0).color(r, g, b, a).next();
		vertexConsumer.vertex(x2, y1, 0).color(r, g, b, a).next();
	}

	static void drawRectangle(Matrix4f matrices, VertexConsumerProvider vertexConsumers, float x1, float y1, float x2, float y2, float z, int color, int light) {
		drawRectangle(matrices, vertexConsumers, x1, y1, z, x2, y2, z, color, light);
	}

	static void drawRectangle(Matrix4f matrices, VertexConsumerProvider vertexConsumers, float x1, float y1, float z1, float x2, float y2, float z2, int color, int light) {
		final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLeash());
		final int a = (color >> 24) & 0xFF;
		final int r = (color >> 16) & 0xFF;
		final int g = (color >> 8) & 0xFF;
		final int b = color & 0xFF;
		vertexConsumer.vertex(matrices, x1, y2, z1).color(r, g, b, a).light(light).normal(0, 0, 1).next();
		vertexConsumer.vertex(matrices, x2, y2, z2).color(r, g, b, a).light(light).normal(0, 0, 1).next();
		vertexConsumer.vertex(matrices, x2, y1, z2).color(r, g, b, a).light(light).normal(0, 0, 1).next();
		vertexConsumer.vertex(matrices, x1, y1, z1).color(r, g, b, a).light(light).normal(0, 0, 1).next();
	}

	static void drawTexture(Matrix4f matrices, VertexConsumer vertexConsumer, float x, float y, float width, float height, int light) {
		drawTexture(matrices, vertexConsumer, x, y, width, height, 0, 0, 1, 1, -1, light);
	}

	static void drawTexture(Matrix4f matrices, VertexConsumer vertexConsumer, float x, float y, float width, float height, float u1, float v1, float u2, float v2, int color, int light) {
		final int a = (color >> 24) & 0xFF;
		final int r = (color >> 16) & 0xFF;
		final int g = (color >> 8) & 0xFF;
		final int b = color & 0xFF;
		final float x1 = x + width;
		final float y1 = y + height;
		vertexConsumer.vertex(matrices, x, y1, 0).color(r, g, b, a).texture(u1, v2).overlay(OverlayTexture.DEFAULT_UV).light(light).next();
		vertexConsumer.vertex(matrices, x1, y1, 0).color(r, g, b, a).texture(u2, v2).overlay(OverlayTexture.DEFAULT_UV).light(light).next();
		vertexConsumer.vertex(matrices, x1, y, 0).color(r, g, b, a).texture(u2, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).next();
		vertexConsumer.vertex(matrices, x, y, 0).color(r, g, b, a).texture(u1, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).next();
	}

	@FunctionalInterface
	interface DrawingCallback {
		void drawingCallback(float x1, float y1, float x2, float y2);
	}
}
