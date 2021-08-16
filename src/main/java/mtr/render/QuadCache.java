package mtr.render;

import mtr.data.IGui;
import mtr.gui.IDrawing;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BasicBakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;
import java.util.stream.Collectors;

public class QuadCache implements IGui {

	private int lightUpdateIndex = 0;

	private final boolean isSolid;
	private final boolean isNonBlock;
	private final Identifier textureId;
	private final Sprite sprite;
	private final List<QuadCacheItem> cacheList = new ArrayList<>();

	private static final int UPDATES_PER_FRAME = 2;
	private static final BlockModelRenderer BLOCK_MODEL_RENDERER = MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer();

	public QuadCache(boolean isSolid, String texture, Sprite sprite) {
		this.isSolid = isSolid;
		textureId = new Identifier(texture);
		this.sprite = sprite;
		isNonBlock = sprite == null;
	}

	public void addFace(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float u1, float v1, float u2, float v2, Direction facing, int color, BlockPos lightRefPos) {
		final float alpha = ((color >> 24) & 0xFF) / 255F;
		if (alpha != 0) {
			cacheList.add(new QuadCacheItem(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, u1, v2, u2, v2, u2, v1, u1, v1, color, facing, lightRefPos));
		}
	}

	public void addBlockFace(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float u1, float v1, float u2, float v2, float u3, float v3, float u4, float v4, Direction facing, BlockPos pos, int color) {
		cacheList.add(new QuadCacheItem(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, u1, v1, u2, v2, u3, v3, u4, v4, color | ARGB_BLACK, facing, pos));
	}

	public void apply(VertexConsumerProvider vertexConsumers, MatrixStack matrices, World world) {
		vertexConsumers.getBuffer(RenderLayer.getBlockLayers().get(0));
		final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(isSolid ? MoreRenderLayers.getSolid(textureId) : MoreRenderLayers.getExterior(textureId));

		// Caching light data - how much impact does it have?
		if (isNonBlock) {
			for (int i = 0; i < cacheList.size(); i++) {
				cacheList.get(i).applyNonBlock(vertexConsumer, matrices, world, i >= lightUpdateIndex && i < lightUpdateIndex + UPDATES_PER_FRAME);
			}
		} else {
			cacheList.forEach(quadCacheItem -> quadCacheItem.applyBlock(vertexConsumer, matrices, world, sprite));
		}
		lightUpdateIndex += UPDATES_PER_FRAME;
		if (lightUpdateIndex > cacheList.size()) {
			lightUpdateIndex = 0;
		}
	}

	private static class QuadCacheItem {

		private int cachedLight = -1;

		private final float x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4;
		private final float u1, v1, u2, v2, u3, v3, u4, v4;
		private final int color;
		private final Direction facing;
		private final BlockPos pos;

		public QuadCacheItem(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float u1, float v1, float u2, float v2, float u3, float v3, float u4, float v4, int color, Direction facing, BlockPos pos) {
			this.x1 = x1;
			this.y1 = y1;
			this.z1 = z1;
			this.x2 = x2;
			this.y2 = y2;
			this.z2 = z2;
			this.x3 = x3;
			this.y3 = y3;
			this.z3 = z3;
			this.x4 = x4;
			this.y4 = y4;
			this.z4 = z4;
			this.u1 = u1;
			this.v1 = v1;
			this.u2 = u2;
			this.v2 = v2;
			this.u3 = u3;
			this.v3 = v3;
			this.u4 = u4;
			this.v4 = v4;
			this.color = color;
			this.facing = facing;
			this.pos = pos;
		}

		public void applyNonBlock(VertexConsumer vertexConsumer, MatrixStack matrices, World world, boolean performLightUpdate) {
			if (cachedLight == -1 || performLightUpdate) {
				cachedLight = WorldRenderer.getLightmapCoordinates(world, pos);
			}
			IDrawing.drawTexture(matrices, vertexConsumer, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, u1, v1, u2, v2, u3, v3, u4, v4, facing, color, cachedLight);
		}

		public void applyBlock(VertexConsumer vertexConsumer, MatrixStack matrices, World world, Sprite sprite) {
			final BlockState state = world.getBlockState(pos);
			final float yMax = Math.max(Math.max(y1, y2), Math.max(y3, y4));

			matrices.push();
			final float dY = 0.0625F + IGui.SMALL_OFFSET;
			matrices.translate(pos.getX(), pos.getY() - dY, pos.getZ());

			final int[] vertexData = new int[]{
					Float.floatToIntBits(x1), Float.floatToIntBits(y1), Float.floatToIntBits(z1), 0, Float.floatToIntBits(u1), Float.floatToIntBits(v1), 0, 0,
					Float.floatToIntBits(x2), Float.floatToIntBits(y2), Float.floatToIntBits(z2), 0, Float.floatToIntBits(u2), Float.floatToIntBits(v2), 0, 0,
					Float.floatToIntBits(x3), Float.floatToIntBits(y3), Float.floatToIntBits(z3), 0, Float.floatToIntBits(u3), Float.floatToIntBits(v3), 0, 0,
					Float.floatToIntBits(x4), Float.floatToIntBits(y4), Float.floatToIntBits(z4), 0, Float.floatToIntBits(u4), Float.floatToIntBits(v4), 0, 0,
			};
			final List<BakedQuad> bakedQuads = Collections.singletonList(new BakedQuad(vertexData, 0, facing, sprite, true));
			final Map<Direction, List<BakedQuad>> faceQuads = Arrays.stream(Direction.values()).collect(Collectors.toMap(direction -> direction, direction -> new ArrayList<>()));
			BLOCK_MODEL_RENDERER.render(world, new BasicBakedModel(bakedQuads, faceQuads, true, true, true, sprite, ModelTransformation.NONE, ModelOverrideList.EMPTY), state, pos, matrices, vertexConsumer, true, new Random(), 0, OverlayTexture.DEFAULT_UV);

//			BlockPos lightRefPos;
//			if (facing.getAxis() != Direction.Axis.Y) {
//				lightRefPos = pos.offset(facing);
//				// Sometimes on very steep slopes, a segment can cover multiple blocks horizontally.
//				// This is to check an upper part when lower part is blocked, to prevent black faces from being shown.
//				// Because such a steep slope is rarely used, it's not that necessary to divide it into multiple faces.
//				while (!world.getBlockState(lightRefPos).isAir()) {
//					if (lightRefPos.getY() <= pos.getY() + yMax) {
//						lightRefPos = lightRefPos.up();
//					} else {
//						break;
//					}
//				}
//
//				// Update the positions to reject the invisible parts, making the lighting area correct.
//				final float yRefOffset = lightRefPos.getY() - pos.getY();
//				if (yRefOffset > 0) {
//					// This assumes y2 and y3 is at the bottom.
//					y2 = Math.min(y2, yRefOffset);
//					y3 = Math.min(y3, yRefOffset);
//				}
//			} else {
//				lightRefPos = pos;
//			}
//
//
//			if (brightness == null || light == null || performLightUpdate) {
//				if (MinecraftClient.isAmbientOcclusionEnabled() && (facing.getAxis() != Direction.Axis.Y)) {
//					BitSet flags = new BitSet(3);
//					float[] box = new float[Direction.values().length * 2];
//					BLOCK_MODEL_RENDERER.getQuadDimensions(world, state, lightRefPos.offset(facing.getOpposite()), vertexData, facing, box, flags);
//					aoCalculator.apply(world, state, lightRefPos.offset(facing.getOpposite()), facing, box, flags, true);
//					brightness = aoCalculator.brightness.clone();
//					light = aoCalculator.light.clone();
//				} else {
//					final int light = WorldRenderer.getLightmapCoordinates(world, lightRefPos);
//					final float brightness = world.getBrightness(facing, true);
//					if (this.brightness == null) {
//						this.brightness = new float[4];
//					}
//					if (this.light == null) {
//						this.light = new int[4];
//					}
//					this.brightness[0] = brightness;
//					this.brightness[1] = brightness;
//					this.brightness[2] = brightness;
//					this.brightness[3] = brightness;
//					this.light[0] = light;
//					this.light[1] = light;
//					this.light[2] = light;
//					this.light[3] = light;
//				}
//			}
//			final float r = applyColor ? this.r : 1.0F;
//			final float g = applyColor ? this.g : 1.0F;
//			final float b = applyColor ? this.b : 1.0F;
//			final float a = applyColor ? alpha : 1.0F;
//			vertexConsumer.quad(matrices.peek(), quad, brightness, r, g, b, light, 0, false);

			matrices.pop();
		}
	}
}