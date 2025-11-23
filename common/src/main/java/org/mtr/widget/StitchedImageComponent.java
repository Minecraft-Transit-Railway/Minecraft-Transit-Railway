package org.mtr.widget;

import gg.essential.universal.UMatrixStack;
import gg.essential.universal.utils.ReleasedDynamicTexture;
import gg.essential.universal.vertex.UVertexConsumer;

import java.util.Random;

public class StitchedImageComponent extends ImageComponentBase {

	private final int textureWidth;
	private final int textureHeight;
	private final int croppedTextureWidth;
	private final int croppedTextureHeight;
	private final int textureBorder;
	private final int texturePadding;
	private final int textureMiddleX1;
	private final int textureMiddleY1;
	private final int textureMiddleX2;
	private final int textureMiddleY2;

	private static final long RANDOM_SEED = new Random().nextLong();

	public StitchedImageComponent(int textureWidth, int textureHeight, int textureBorder, int texturePadding, ReleasedDynamicTexture... releasedDynamicTextures) {
		this(
				textureWidth, textureHeight, textureWidth, textureHeight, textureBorder, texturePadding,
				textureBorder, textureBorder, textureWidth - textureBorder, textureHeight - textureBorder,
				releasedDynamicTextures
		);
	}

	public StitchedImageComponent(
			int textureWidth, int textureHeight, int croppedTextureWidth, int croppedTextureHeight, int textureBorder, int texturePadding,
			int textureMiddleX1, int textureMiddleY1, int textureMiddleX2, int textureMiddleY2,
			ReleasedDynamicTexture... releasedDynamicTextures
	) {
		super(releasedDynamicTextures);
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.croppedTextureWidth = croppedTextureWidth;
		this.croppedTextureHeight = croppedTextureHeight;
		this.textureBorder = textureBorder;
		this.texturePadding = texturePadding;
		this.textureMiddleX1 = textureMiddleX1;
		this.textureMiddleY1 = textureMiddleY1;
		this.textureMiddleX2 = textureMiddleX2;
		this.textureMiddleY2 = textureMiddleY2;
	}

	@Override
	public final void renderTexture(UMatrixStack matrixStack, UVertexConsumer vertexConsumer) {
		drawImage(
				matrixStack, vertexConsumer,
				getLeft() + texturePadding, getTop() + texturePadding, getWidth() - texturePadding * 2, getHeight() - texturePadding * 2,
				textureWidth, textureHeight, croppedTextureWidth, croppedTextureHeight, textureBorder,
				textureMiddleX1, textureMiddleY1, textureMiddleX2, textureMiddleY2,
				backgroundColor == null
		);
	}

	public static void drawImage(
			UMatrixStack matrixStack, UVertexConsumer vertexConsumer,
			float x, float y, float width, float height,
			int textureWidth, int textureHeight, int textureBorder,
			boolean drawBackground
	) {
		drawImage(
				matrixStack, vertexConsumer,
				x, y, width, height,
				textureWidth, textureHeight, textureWidth, textureHeight, textureBorder,
				textureBorder, textureBorder, textureWidth - textureBorder, textureHeight - textureBorder,
				drawBackground
		);
	}

	public static void drawImage(
			UMatrixStack matrixStack, UVertexConsumer vertexConsumer,
			float x, float y, float width, float height,
			int textureWidth, int textureHeight, int croppedTextureWidth, int croppedTextureHeight, int textureBorder,
			int textureMiddleX1, int textureMiddleY1, int textureMiddleX2, int textureMiddleY2,
			boolean drawBackground
	) {
		if (width > 0 && height > 0) {
			final int halfMiddleWidth = (textureMiddleX2 - textureMiddleX1) / 2;
			final int halfMiddleHeight = (textureMiddleY2 - textureMiddleY1) / 2;
			final int countX = (halfMiddleWidth == 0 ? 0 : (int) Math.ceil((width - textureBorder * 2) / halfMiddleWidth)) + 2;
			final int countY = (halfMiddleHeight == 0 ? 0 : (int) Math.ceil((height - textureBorder * 2) / halfMiddleHeight)) + 2;
			final Random random = new Random(RANDOM_SEED);

			for (int drawX = 0; drawX < countX; drawX++) {
				for (int drawY = 0; drawY < countY; drawY++) {
					final boolean firstX = drawX == 0;
					final boolean lastX = drawX == countX - 1;
					final boolean firstY = drawY == 0;
					final boolean lastY = drawY == countY - 1;

					if (firstX || lastX || firstY || lastY || drawBackground) {
						final float x1 = firstX ? 0 : (lastX ? width - textureBorder : textureBorder + (drawX - 1) * halfMiddleWidth);
						final float y1 = firstY ? 0 : (lastY ? height - textureBorder : textureBorder + (drawY - 1) * halfMiddleHeight);
						final float x2 = Math.min(x1 + (firstX || lastX ? textureBorder : halfMiddleWidth), width - (lastX ? 0 : textureBorder));
						final float y2 = Math.min(y1 + (firstY || lastY ? textureBorder : halfMiddleHeight), height - (lastY ? 0 : textureBorder));
						final float u1 = (firstX ? 0F : (lastX ? croppedTextureWidth - textureBorder : random.nextInt(textureMiddleX1, textureMiddleX1 + Math.max(1, halfMiddleWidth)))) / textureWidth;
						final float v1 = (firstY ? 0F : (lastY ? croppedTextureHeight - textureBorder : random.nextInt(textureMiddleY1, textureMiddleY1 + Math.max(1, halfMiddleHeight)))) / textureHeight;
						final float u2 = u1 + (x2 - x1) / textureWidth;
						final float v2 = v1 + (y2 - y1) / textureHeight;

						drawTexturedQuad(matrixStack, vertexConsumer, x1 + x, y1 + y, x2 + x, y2 + y, u1, v1, u2, v2);
					}
				}
			}
		}
	}
}
