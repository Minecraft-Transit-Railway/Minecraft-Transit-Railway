package org.mtr.tool;

import gg.essential.universal.UMatrixStack;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.mtr.widget.ImageComponentBase;

import java.awt.*;

public final class BlockRendererHelper {

	private static final Object2ObjectOpenHashMap<String, ReleasedDynamicTextureManager.Holder> BLOCK_TEXTURE_MAP = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<String, int[]> VERTEX_DATA_MAP = new Object2ObjectOpenHashMap<>();

	public static void renderBlock(UMatrixStack matrixStack, BlockState blockState, long renderKey, double x, double y, double z, double brightness) {
		for (int i = -1; i < Direction.values().length; i++) {
			final int directionIndex = i;
			MinecraftClient.getInstance().getBlockRenderManager().getModel(blockState).getQuads(blockState, i < 0 ? null : Direction.values()[i], Random.create()).forEach(bakedQuad -> {
				final Sprite sprite = bakedQuad.getSprite();
				final Identifier tempIdentifier = bakedQuad.getSprite().getContents().getId();
				final Identifier newIdentifier = Identifier.of(tempIdentifier.getNamespace(), String.format("textures/%s.png", tempIdentifier.getPath()));

				ImageComponentBase.drawTexture(BLOCK_TEXTURE_MAP.computeIfAbsent(newIdentifier.toString(), key -> ReleasedDynamicTextureManager.create(newIdentifier)).get(), vertexConsumer -> {
					final int[] vertexData = VERTEX_DATA_MAP.computeIfAbsent(tempIdentifier.toString() + directionIndex + renderKey, key -> bakedQuad.getVertexData());
					for (int j = 0; j < vertexData.length; j += 8) {
						final Color color = new Color(vertexData[j + 3]);
						final int r = (int) Math.floor(color.getRed() * brightness);
						final int g = (int) Math.floor(color.getGreen() * brightness);
						final int b = (int) Math.floor(color.getBlue() * brightness);
						vertexConsumer.pos(
								matrixStack,
								x + Float.intBitsToFloat(vertexData[j]),
								y + Float.intBitsToFloat(vertexData[j + 1]),
								z + Float.intBitsToFloat(vertexData[j + 2])
						).tex(
								(Float.intBitsToFloat(vertexData[j + 4]) - sprite.getMinU()) / (sprite.getMaxU() - sprite.getMinU()),
								(Float.intBitsToFloat(vertexData[j + 5]) - sprite.getMinV()) / (sprite.getMaxV() - sprite.getMinV())
						).color(new Color(r, g, b)).endVertex();
					}
				});
			});
		}
	}
}
