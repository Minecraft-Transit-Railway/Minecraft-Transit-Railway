package org.mtr.tool;

import gg.essential.universal.UMatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
//? if >= 26.1 {
/*import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import org.joml.Vector3fc;
*///? }
import org.jspecify.annotations.Nullable;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.widget.ImageComponentBase;

import java.awt.*;
import java.util.List;

public final class BlockRendererHelper {

	private static final Object2ObjectOpenHashMap<String, ReleasedDynamicTextureRegistry.Holder> BLOCK_TEXTURE_MAP = new Object2ObjectOpenHashMap<>();
	private static final Object2ObjectOpenHashMap<String, int[]> VERTEX_DATA_MAP = new Object2ObjectOpenHashMap<>();

	public static void renderBlock(UMatrixStack matrixStack, BlockState blockState, long renderKey, double x, double y, double z, double brightness) {
		for (int i = -1; i < Direction.values().length; i++) {
			final List<BakedQuad> bakedQuads = getQuads(blockState, i < 0 ? null : Direction.values()[i]);

			for (int j = 0; j < bakedQuads.size(); j++) {
				final BakedQuad bakedQuad = bakedQuads.get(j);
//? if >= 26.1 {
				/*final TextureAtlasSprite sprite = bakedQuad.materialInfo().sprite();
*///? } else {
				final TextureAtlasSprite sprite = bakedQuad.getSprite();
//? }
				final ResourceLocation tempIdentifier = sprite.contents().name();
				final ResourceLocation newIdentifier = ResourceLocation.fromNamespaceAndPath(tempIdentifier.getNamespace(), String.format("textures/%s.png", tempIdentifier.getPath()));
				final String newRenderKey = String.format("%s_%s_%s_%s", tempIdentifier, i, j, renderKey);

				ImageComponentBase.drawTexture(BLOCK_TEXTURE_MAP.computeIfAbsent(newIdentifier.toString(), key -> ReleasedDynamicTextureRegistry.INSTANCE.create(newIdentifier)).get(), vertexConsumer -> {
//? if >= 26.1 {
					/*// A quad is a record of four corners now rather than a packed vertex array, so the
					// corners are read straight off it and the cache that array reads needed goes unused.
					//
					// Colour is no longer carried per vertex. Block model quads always supplied white here
					// and were tinted afterwards by the block colour providers, so white is what brightness
					// scales, which is the same result the packed array gave for these blocks.
					final int value = (int) Math.floor(0xFF * brightness);
					final Color quadColor = new Color(value, value, value);
					for (int k = 0; k < 4; k++) {
						final Vector3fc position = bakedQuad.position(k);
						final long packedUv = bakedQuad.packedUV(k);
						vertexConsumer.pos(
							matrixStack,
							x + position.x(),
							y + position.y(),
							z + position.z()
						).tex(
							(UVPair.unpackU(packedUv) - sprite.getU0()) / (sprite.getU1() - sprite.getU0()),
							(UVPair.unpackV(packedUv) - sprite.getV0()) / (sprite.getV1() - sprite.getV0())
						).color(quadColor).endVertex();
					}
*///? } else {
					final int[] vertexData = VERTEX_DATA_MAP.computeIfAbsent(newRenderKey, key -> bakedQuad.getVertices());
					for (int k = 0; k < vertexData.length; k += 8) {
						final Color color = new Color(vertexData[k + 3]);
						final int r = (int) Math.floor(color.getRed() * brightness);
						final int g = (int) Math.floor(color.getGreen() * brightness);
						final int b = (int) Math.floor(color.getBlue() * brightness);
						vertexConsumer.pos(
							matrixStack,
							x + Float.intBitsToFloat(vertexData[k]),
							y + Float.intBitsToFloat(vertexData[k + 1]),
							z + Float.intBitsToFloat(vertexData[k + 2])
						).tex(
							(Float.intBitsToFloat(vertexData[k + 4]) - sprite.getU0()) / (sprite.getU1() - sprite.getU0()),
							(Float.intBitsToFloat(vertexData[k + 5]) - sprite.getV0()) / (sprite.getV1() - sprite.getV0())
						).color(new Color(r, g, b)).endVertex();
					}
//? }
				});
			}
		}
	}

	/**
	 * Sits apart from the drawing loop so the version guard has a method body of its own to occupy,
	 * rather than being nested inside the two loops and the lambda that follow it.
	 *
	 * @return every quad the block state contributes to the given face, or to no face when null.
	 */
	private static List<BakedQuad> getQuads(BlockState blockState, @Nullable Direction direction) {
//? if >= 26.1 {
		/*// A block state is assembled from parts now instead of resolving to a single model, so the
		// parts are collected first and the face is asked of each in turn. The random source seeds the
		// choice between model variants, and a fresh one matches what the single call did before.
		final ObjectArrayList<BlockStateModelPart> blockStateModelParts = new ObjectArrayList<>();
		Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(blockState).collectParts(RandomSource.create(), blockStateModelParts);
		final ObjectArrayList<BakedQuad> bakedQuads = new ObjectArrayList<>();
		for (final BlockStateModelPart blockStateModelPart : blockStateModelParts) {
			bakedQuads.addAll(blockStateModelPart.getQuads(direction));
		}
		return bakedQuads;
*///? } else {
		return Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState).getQuads(blockState, direction, RandomSource.create());
//? }
	}
}
