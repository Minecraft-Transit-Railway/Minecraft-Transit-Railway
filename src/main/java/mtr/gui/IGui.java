package mtr.gui;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import mtr.MTR;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;

import java.util.stream.IntStream;

public interface IGui {

	int SQUARE_SIZE = 20;
	int TEXT_PADDING = 6;
	int TEXT_FIELD_PADDING = 4;
	int LINE_HEIGHT = 10;

	int RGB_WHITE = 0xFFFFFF;
	int ARGB_WHITE = 0xFFFFFFFF;
	int ARGB_BLACK = 0xFF000000;
	int ARGB_BLUE = 0xFF4285F4;
	int ARGB_WHITE_TRANSLUCENT = 0x7FFFFFFF;
	int ARGB_BLACK_TRANSLUCENT = 0x7F000000;

	static String formatStationName(String name) {
		return name.replace('|', ' ');
	}

	static void drawStringWithFont(MatrixStack matrices, String text, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, int x, int y, boolean verticalChinese) {
		if (verticalChinese) {
			StringBuilder textBuilder = new StringBuilder();
			for (int i = 0; i < text.length(); i++) {
				boolean isChinese = Character.UnicodeScript.of(text.codePointAt(i)) == Character.UnicodeScript.HAN;
				if (isChinese) {
					textBuilder.append('|');
				}
				textBuilder.append(text, i, i + 1);
				if (isChinese) {
					textBuilder.append('|');
				}
			}
			text = textBuilder.toString();
		}
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
		int offset = y - IntStream.of(lineHeights).sum() * verticalAlignment.ordinal() / 2;
		for (int i = 0; i < textSplit.length; i++) {
			OrderedText orderedText = new LiteralText(textSplit[i]).fillStyle(style).asOrderedText();
			ScreenDrawing.drawStringWithShadow(matrices, orderedText, horizontalAlignment, x, offset, 0, ARGB_WHITE);
			offset += lineHeights[i];
		}
	}
}
