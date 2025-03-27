package org.mtr.map;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeEffects;
import org.mtr.cache.CachedFileResource;

import javax.annotation.Nullable;
import java.awt.*;
import java.nio.file.Path;

public final class MapTileResource extends CachedFileResource {

	@Nullable
	private VertexBuffer vertexBuffer;

	private final World world;
	private final MapTileProvider.MapType mapType;
	private final int chunkX;
	private final int y;
	private final int chunkZ;

	private static final int LIFESPAN = 3000;
	private static final int WATER_DEPTH_CHECK = 16;

	// See https://github.com/jpenilla/squaremap/blob/master/common/src/main/java/xyz/jpenilla/squaremap/common/data/BiomeColors.java
	private static final ObjectArrayList<Block> GRASS_COLOR_BLOCKS = ObjectArrayList.of(
			Blocks.GRASS_BLOCK,
			Blocks.SHORT_GRASS,
			Blocks.TALL_GRASS,
			Blocks.FERN,
			Blocks.LARGE_FERN,
			Blocks.POTTED_FERN,
			Blocks.SUGAR_CANE
	);
	private static final ObjectArrayList<Block> FOLIAGE_COLOR_BLOCKS = ObjectArrayList.of(
			Blocks.VINE,
			Blocks.OAK_LEAVES,
			Blocks.JUNGLE_LEAVES,
			Blocks.ACACIA_LEAVES,
			Blocks.DARK_OAK_LEAVES,
			Blocks.MANGROVE_LEAVES
	);

	public MapTileResource(World world, MapTileProvider.MapType mapType, int chunkX, int y, int chunkZ, Path path) {
		super(path, LIFESPAN);
		this.world = world;
		this.mapType = mapType;
		this.chunkX = chunkX;
		this.y = y;
		this.chunkZ = chunkZ;
	}

	@Nullable
	@Override
	protected byte[] generate(@Nullable byte[] oldData) {
		final ImageConverter imageConverter = new ImageConverter(oldData);

		for (int x = 0; x < MapTileProvider.TILE_SIZE; x++) {
			Integer previousY = null;
			for (int z = -1; z < MapTileProvider.TILE_SIZE; z++) {
				final int blockX = chunkX * MapTileProvider.TILE_SIZE + x;
				final int blockZ = chunkZ * MapTileProvider.TILE_SIZE + z;

				// Only draw for loaded chunks
				if (world.getChunkManager().isChunkLoaded(ChunkSectionPos.getSectionCoord(blockX), ChunkSectionPos.getSectionCoord(blockZ))) {
					// Find appropriate Y level
					final int topY = world.getTopY(Heightmap.Type.MOTION_BLOCKING, blockX, blockZ) - 1;
					final int blockY;
					if (mapType == MapTileProvider.MapType.DYNAMIC && y < topY) {
						int currentY = y;
						while (true) {
							if (currentY < world.getBottomY() || !world.isAir(new BlockPos(blockX, currentY, blockZ))) {
								break;
							} else {
								currentY--;
							}
						}
						blockY = currentY;
					} else {
						blockY = topY;
					}

					// Shade or highlight based on the previous row's elevation
					final float elevationShadow = previousY == null ? 0 : blockY > previousY ? 1 : blockY < previousY ? 0.8F : 0.9F;
					previousY = blockY;

					// If there is no elevation data of the row above, don't draw
					if (elevationShadow == 0 && (z < 0 || imageConverter.hasPixel(x, z))) {
						continue;
					}

					final float newElevationShadow = elevationShadow == 0 ? 0.9F : elevationShadow;
					final BlockPos finalPos = new BlockPos(blockX, blockY, blockZ);

					// Figure out light level
					final float lightLevel;
					if (mapType == MapTileProvider.MapType.DYNAMIC) {
						final BlockPos lightReferencePos = finalPos.up();
						lightLevel = (Math.max(world.getLightLevel(LightType.BLOCK, lightReferencePos), world.getLightLevel(LightType.SKY, lightReferencePos)) + 5) / 20F;
					} else {
						lightLevel = 1;
					}

					// Blend and draw pixel
					final Color color = getBlockColor(finalPos);
					final int r = Math.round(color.getRed() * newElevationShadow * lightLevel);
					final int g = Math.round(color.getGreen() * newElevationShadow * lightLevel);
					final int b = Math.round(color.getBlue() * newElevationShadow * lightLevel);
					imageConverter.setPixel(x, z, new Color(r, g, b).getRGB());
				} else {
					previousY = null;
				}
			}
		}

		return imageConverter.modified ? imageConverter.convert() : oldData;
	}

	@Override
	protected void dataUpdated(@Nullable byte[] data) {
		if (data == null) {
			this.vertexBuffer = null;
		} else {
			final boolean[] noVertices = {true};

			final VertexBuffer vertexBuffer = VertexBuffer.createAndUpload(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR, vertexConsumer -> {
				int pixelOffsetX = 0;
				int pixelOffsetY = 0;

				for (int i = 0; i < data.length; i += 5) {
					final Color color = new Color(
							data[i + 1] & 0xFF,
							data[i + 2] & 0xFF,
							data[i + 3] & 0xFF,
							data[i] & 0xFF
					);
					int count = (data[i + 4] & 0xFF) + 1;

					while (count > 0) {
						final int length = Math.min(MapTileProvider.TILE_SIZE - pixelOffsetX, count);

						if (color.getAlpha() > 0) {
							final int x2 = pixelOffsetX + length;
							final int y2 = pixelOffsetY + 1;
							vertexConsumer.vertex(pixelOffsetX, pixelOffsetY, 0).color(color.getRGB());
							vertexConsumer.vertex(pixelOffsetX, y2, 0).color(color.getRGB());
							vertexConsumer.vertex(x2, y2, 0).color(color.getRGB());
							vertexConsumer.vertex(x2, pixelOffsetY, 0).color(color.getRGB());
							noVertices[0] = false;
						}

						pixelOffsetX += length;
						count -= length;

						if (pixelOffsetX == MapTileProvider.TILE_SIZE) {
							pixelOffsetX = 0;
							pixelOffsetY++;
						}
					}
				}

				if (noVertices[0]) {
					vertexConsumer.vertex(0, 0, 0).color(0);
					vertexConsumer.vertex(0, 1, 0).color(0);
					vertexConsumer.vertex(1, 1, 0).color(0);
					vertexConsumer.vertex(1, 0, 0).color(0);
				}
			});

			if (noVertices[0]) {
				vertexBuffer.close();
				this.vertexBuffer = null;
			} else {
				this.vertexBuffer = vertexBuffer;
			}
		}
	}

	@Nullable
	public VertexBuffer getVertexBuffer() {
		return vertexBuffer;
	}

	/**
	 * Get the block colour at the specified position, taking into account grass, foliage, and water colours.
	 * For water, draw some underwater blocks as well.
	 *
	 * @param blockPos the position to get the colour
	 * @return the block colour (RGB)
	 */
	private Color getBlockColor(BlockPos blockPos) {
		final BlockState blockState = world.getBlockState(blockPos);
		final Block block = blockState.getBlock();
		final int defaultColor = blockState.getMapColor(world, blockPos).color;
		final BiomeEffects biomeEffects = world.getBiome(blockPos).value().getEffects();

		if (GRASS_COLOR_BLOCKS.contains(block)) {
			return new Color(biomeEffects.getGrassColor().orElse(defaultColor));
		} else if (FOLIAGE_COLOR_BLOCKS.contains(block)) {
			return new Color(biomeEffects.getFoliageColor().orElse(defaultColor));
		} else if (block.getDefaultMapColor() == MapColor.WATER_BLUE) {
			for (int i = 1; i < WATER_DEPTH_CHECK; i++) {
				final BlockPos checkPos = blockPos.down(i);
				if (world.getBlockState(checkPos).getMapColor(world, checkPos) != MapColor.WATER_BLUE) {
					return blendColors(getBlockColor(checkPos), WATER_DEPTH_CHECK - i, new Color(biomeEffects.getWaterColor()), i + WATER_DEPTH_CHECK);
				}
			}
			return new Color(biomeEffects.getWaterColor());
		} else {
			return new Color(defaultColor);
		}
	}

	private static Color blendColors(Color color1, int weight1, Color color2, int weight2) {
		final int r1 = color1.getRed();
		final int g1 = color1.getGreen();
		final int b1 = color1.getBlue();
		final int r2 = color2.getRed();
		final int g2 = color2.getGreen();
		final int b2 = color2.getBlue();
		final int r = (r1 * weight1 + r2 * weight2) / (weight1 + weight2);
		final int g = (g1 * weight1 + g2 * weight2) / (weight1 + weight2);
		final int b = (b1 * weight1 + b2 * weight2) / (weight1 + weight2);
		return new Color(r, g, b);
	}

	/**
	 * Converts an image to and from a {@code byte} and {@code int} array.
	 * <br/>
	 * Images are mapped to the {@code byte} array as follows:
	 * <br/>
	 * {@code byte[a1, r1, g1, b1, count1, a2, r2, g2, b2, count2, ...]}
	 * <br/>
	 * where {@code count} is how many pixels in a row the colour stretches for.
	 */
	private static class ImageConverter {

		private boolean modified = false;
		private final int[] pixels;

		private ImageConverter(@Nullable byte[] data) {
			pixels = new int[MapTileProvider.TILE_SIZE * MapTileProvider.TILE_SIZE];
			if (data != null) {
				int pixelIndex = 0;
				for (int sourceIndex = 0; sourceIndex < data.length; sourceIndex += 5) {
					final int color = getInt(data, sourceIndex);
					for (int count = 0; count <= (data[sourceIndex + 4] & 0xFF); count++) {
						pixels[pixelIndex++] = color;
					}
				}
			}
		}

		private boolean hasPixel(int x, int y) {
			final int index = x + y * MapTileProvider.TILE_SIZE;
			return pixels[index] != 0;
		}

		private void setPixel(int x, int y, int color) {
			final int index = x + y * MapTileProvider.TILE_SIZE;
			if (color != pixels[index]) {
				pixels[index] = color;
				modified = true;
			}
		}

		private byte[] convert() {
			final ByteArrayList byteArrayList = new ByteArrayList();
			writeImage(pixels, (color, count) -> {
				writeInt(byteArrayList, color);
				byteArrayList.add((byte) count);
			});
			return byteArrayList.toByteArray();
		}
	}
}
