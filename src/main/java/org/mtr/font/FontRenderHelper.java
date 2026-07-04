package org.mtr.font;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.data.IGui;
import org.mtr.libraries.it.unimi.dsi.fastutil.floats.FloatFloatImmutablePair;
import org.mtr.tool.GuiHelper;

import java.awt.*;

public final class FontRenderHelper {

	public static final ResourceLocation MTR_FONT = ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "mtr");
	private static final float LINE_SPACING = 0.25F;

	public static FloatFloatImmutablePair render(@Nullable PoseStack matrixStack, String text, FontRenderOptions fontRenderOptions) {
		return render(matrixStack, null, text, fontRenderOptions);
	}

	public static FloatFloatImmutablePair render(@Nullable PoseStack matrixStack, @Nullable MultiBufferSource vertexConsumerProvider, String text, FontRenderOptions fontRenderOptions) {
		// Split lines
		final String[] lines;
		switch (fontRenderOptions.getLineBreak()) {
			case SPLIT, ALTERNATE -> lines = text.split("\\|");
			case FORCE_ONE_LINE -> lines = new String[]{IGui.formatStationName(text)};
			default -> lines = new String[0];
		}
		final FormattedCharSequence[] orderedTextArray = getOrderedTextArray(lines, fontRenderOptions.getFont());

		// Calculate dimensions
		final float[] rawLineWidths = new float[orderedTextArray.length];
		final float[] rawLineHeights = new float[orderedTextArray.length];
		final float[] fontSize = new float[orderedTextArray.length];
		final Color[] colors = new Color[orderedTextArray.length];
		float maxLineWidth = 0;
		float totalLineHeight = 0;
		final boolean hasCjkColor = fontRenderOptions.getCjkColor() != null && fontRenderOptions.getCjkColor() != fontRenderOptions.getColor();
		final boolean shouldCheckCjk = matrixStack != null && hasCjkColor || fontRenderOptions.getCjkScaling() != 1;
		for (int i = 0; i < orderedTextArray.length; i++) {
			final boolean isCjk = shouldCheckCjk && IGui.isCjk(lines[i]);
			fontSize[i] = (isCjk ? fontRenderOptions.getCjkScaling() : 1) * fontRenderOptions.getMaxFontSize();
			colors[i] = hasCjkColor && isCjk ? fontRenderOptions.getCjkColor() : fontRenderOptions.getColor();
			final float[] dimensions = renderRaw(null, null, orderedTextArray[i], 0, 0, 0, fontSize[i], fontSize[i], true, false, null, 0);
			rawLineWidths[i] = dimensions[0];
			rawLineHeights[i] = dimensions[1];
			maxLineWidth = Math.max(maxLineWidth, rawLineWidths[i]);
			totalLineHeight += rawLineHeights[i];
		}

		// Calculate scale
		final float yScale;
		if (fontRenderOptions.getTextOverflow() == FontRenderOptions.TextOverflow.SCALE) {
			yScale = Math.min(1, Math.min(fontRenderOptions.getVerticalSpace() / totalLineHeight, fontRenderOptions.getHorizontalSpace() / maxLineWidth));
		} else {
			yScale = Math.min(1, fontRenderOptions.getVerticalSpace() / totalLineHeight);
		}

		// Calculate stating position
		final float x = fontRenderOptions.getHorizontalPositioning().getOffset(fontRenderOptions.getHorizontalSpace());
		float y = fontRenderOptions.getVerticalPositioning().getOffset(fontRenderOptions.getVerticalSpace()) + fontRenderOptions.getVerticalTextAlignment().getOffset(totalLineHeight * yScale - fontRenderOptions.getVerticalSpace());
		float totalWidth = 0;

		// Render text
		for (int i = 0; i < orderedTextArray.length; i++) {
			final float xScale = fontRenderOptions.getTextOverflow() == FontRenderOptions.TextOverflow.COMPRESS ? Math.min(yScale, Math.min(1, fontRenderOptions.getHorizontalSpace() / rawLineWidths[i])) : yScale;
			final float horizontalOffset = fontRenderOptions.getHorizontalTextAlignment().getOffset(rawLineWidths[i] * xScale - fontRenderOptions.getHorizontalSpace());
			totalWidth = Math.max(totalWidth, renderRaw(
				matrixStack, vertexConsumerProvider, orderedTextArray[i],
				x + fontRenderOptions.getOffsetX() + horizontalOffset,
				y + fontRenderOptions.getOffsetY(),
				fontRenderOptions.getOffsetZ(),
				xScale * fontSize[i],
				yScale * fontSize[i],
				fontRenderOptions.getFont() == null, fontRenderOptions.isDrawShadow(), colors[i], fontRenderOptions.getLight()
			)[0]);
			y += rawLineHeights[i] * yScale;
		}

		return new FloatFloatImmutablePair(totalWidth, y);
	}

	private static float[] renderRaw(@Nullable PoseStack matrixStack, @Nullable MultiBufferSource vertexConsumerProvider, FormattedCharSequence orderedText, float x, float y, float z, float xScale, float yScale, boolean isDefaultFont, boolean drawShadow, @Nullable Color color, int light) {
		if (matrixStack != null && color != null) {
			final MultiBufferSource newVertexConsumerProvider = vertexConsumerProvider == null ? Minecraft.getInstance().renderBuffers().bufferSource() : vertexConsumerProvider;
			matrixStack.pushPose();
			matrixStack.translate(x, y, z);
			matrixStack.scale(xScale / GuiHelper.MINECRAFT_FONT_SIZE, yScale / GuiHelper.MINECRAFT_FONT_SIZE, 1);
			Minecraft.getInstance().font.drawInBatch(orderedText, isDefaultFont ? 0.5F : 0, 1, color.getRGB(), drawShadow, matrixStack.last().pose(), newVertexConsumerProvider, Font.DisplayMode.NORMAL, 0, light);
			matrixStack.popPose();
		}

		return new float[]{Minecraft.getInstance().font.width(orderedText) * xScale / GuiHelper.MINECRAFT_FONT_SIZE, yScale * (1 + LINE_SPACING)};
	}

	private static FormattedCharSequence getOrderedText(String text, @Nullable ResourceLocation font) {
		return (font == null ? Component.literal(text) : Component.literal(text).setStyle(Style.EMPTY.withFont(font))).getVisualOrderText();
	}

	private static FormattedCharSequence[] getOrderedTextArray(String[] lines, @Nullable ResourceLocation font) {
		final FormattedCharSequence[] orderedText = new FormattedCharSequence[lines.length];
		for (int i = 0; i < lines.length; i++) {
			orderedText[i] = getOrderedText(lines[i], font);
		}
		return orderedText;
	}
}
