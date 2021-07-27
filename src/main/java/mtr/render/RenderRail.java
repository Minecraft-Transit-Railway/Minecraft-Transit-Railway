package mtr.render;

import mtr.block.BlockRail;
import mtr.data.Pos3f;
import mtr.data.Rail;
import mtr.gui.IGui;
import mtr.item.ItemRailModifier;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Random;

public class RenderRail extends BlockEntityRenderer<BlockRail.TileEntityRail> implements IGui {

	public RenderRail(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockRail.TileEntityRail entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final World world = entity.getWorld();
		if (world == null) {
			return;
		}

		final Collection<Rail> railList = entity.railMap.values();
		final BlockPos pos = entity.getPos();
		final ClientPlayerEntity player = MinecraftClient.getInstance().player;
		final boolean renderColors = player != null && player.isHolding(item -> item instanceof ItemRailModifier);

		matrices.push();
		matrices.translate(-pos.getX(), 0.0625 + SMALL_OFFSET - pos.getY(), -pos.getZ());

		for (final Rail rail : railList) {
			rail.render((h, k, r, t1, t2, y1, y2, isStraight, isEnd) -> {
				final Pos3f rc1 = Rail.getPositionXZ(h, k, r, t1, -1, isStraight);
				final Pos3f rc2 = Rail.getPositionXZ(h, k, r, t1, 1, isStraight);
				final Pos3f rc3 = Rail.getPositionXZ(h, k, r, t2, 1, isStraight);
				final Pos3f rc4 = Rail.getPositionXZ(h, k, r, t2, -1, isStraight);

				final float textureOffsetRail = (((int) (rc1.x + rc2.x)) % 4) * 0.25F;
				final BlockPos pos2 = new BlockPos(rc1.x, y1, rc2.x);
				final int light2 = LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, pos2), world.getLightLevel(LightType.SKY, pos2));
				final int color = renderColors || rail.railType == Rail.RailType.PLATFORM ? rail.railType.color : -1;

				IGui.drawTexture(
					matrices, vertexConsumers, "textures/block/rail.png",
					rc1.x, y1, rc1.z, rc2.x, y1 + SMALL_OFFSET, rc2.z,
					rc3.x, y2, rc3.z, rc4.x, y2 + SMALL_OFFSET, rc4.z,
					0, 0.1875F + textureOffsetRail, 1, 0.3125F + textureOffsetRail,
					Direction.UP, color, light2
				);
				IGui.drawTexture(
					matrices, vertexConsumers, "textures/block/rail.png",
					rc4.x, y2 + SMALL_OFFSET / 4, rc4.z, rc3.x, y2, rc3.z,
					rc2.x, y1 + SMALL_OFFSET / 4, rc2.z, rc1.x, y1, rc1.z,
					0, 0.1875F + textureOffsetRail, 1, 0.3125F + textureOffsetRail,
					Direction.UP, color, light2
				);

				// Render ballast
				final int yf = (int)Math.floor(Math.min(y1, y2));
				if (!isEnd && (y1 != yf || y2 != yf)) {
					final Pos3f bc1 = Rail.getPositionXZ(h, k, r, t1, -1.5F, isStraight);
					final Pos3f bc2 = Rail.getPositionXZ(h, k, r, t1, 1.5F, isStraight);
					final Pos3f bc3 = Rail.getPositionXZ(h, k, r, t2, 1.5F, isStraight);
					final Pos3f bc4 = Rail.getPositionXZ(h, k, r, t2, -1.5F, isStraight);
					int alignment = getAxisAlignment(bc1, bc2, bc3, bc4);
					if (alignment == 1) {
						final int xmin = Math.min((int) bc1.x, (int) bc2.x);
						final int zmin = Math.min((int) bc1.z, (int) bc3.z);
						final float yl = bc1.z < bc3.z ? y1 : y2;
						final float ym = bc1.z < bc3.z ? y2 : y1;
						for (int i = 0; i < 3; i++) {
							drawSlopeBlock(matrices, vertexConsumers, "textures/block/gravel.png", world,
									pos, new BlockPos(xmin + i, yf, zmin), yl, ym, ym, yl);
						}
					} else if (alignment == 2) {
						final int zmin = Math.min((int) bc1.z, (int) bc2.z);
						final int xmin = Math.min((int) bc1.x, (int) bc3.x);
						final float yl = bc1.x < bc3.x ? y1 : y2;
						final float ym = bc1.x < bc3.x ? y2 : y1;
						for (int i = 0; i < 3; i++) {
							drawSlopeBlock(matrices, vertexConsumers, "textures/block/gravel.png", world,
									pos, new BlockPos(xmin, yf, zmin + i), yl, yl, ym, ym);
						}
					}
				}
			});
		}

		matrices.pop();
	}

	private void drawSlopeBlock(MatrixStack matrices, VertexConsumerProvider vertexConsumers, String texture, World world, BlockPos entityPos, BlockPos pos, float yll, float ylm, float ymm, float yml) {
		if (!world.getBlockState(pos).isAir()) return;
		//final int light = LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, pos), world.getLightLevel(LightType.SKY, pos));
		final int light = WorldRenderer.getLightmapCoordinates(world, pos);
		float yf = pos.getY();
		final float dY = 0.0625F + SMALL_OFFSET;
		yll -= dY; ylm -= dY; ymm -= dY; yml -= dY; yf -= dY;
		IGui.drawBlockFace(
				matrices, vertexConsumers, texture,
				pos.getX(), yll, pos.getZ(), pos.getX(), ylm, pos.getZ() + 1,
				pos.getX() + 1, ymm, pos.getZ() + 1, pos.getX() + 1, yml, pos.getZ(),
				0, 0, 0, 1, 1, 1, 1, 0,
				Direction.UP, pos, world
		);
		IGui.drawBlockFace(
				matrices, vertexConsumers, texture,
				pos.getX(), yf, pos.getZ(), pos.getX() + 1, yf, pos.getZ(),
				pos.getX() + 1, yf, pos.getZ() + 1, pos.getX(), yf, pos.getZ() + 1,
				0, 0, 0, 1, 1, 1, 1, 0,
				Direction.DOWN, pos, world
		);
		IGui.drawBlockFace(
				matrices, vertexConsumers, texture,
				pos.getX(), yll, pos.getZ(), pos.getX(), yf, pos.getZ(),
				pos.getX(), yf, pos.getZ() + 1, pos.getX(), ylm, pos.getZ() + 1,
				0, 1 - (yll - yf), 0, 1, 1, 1, 1, 1 - (ylm - yf),
				Direction.WEST, pos, world
		);
		IGui.drawBlockFace(
				matrices, vertexConsumers, texture,
				pos.getX(), ylm, pos.getZ() + 1, pos.getX(), yf, pos.getZ() + 1,
				pos.getX() + 1, yf, pos.getZ() + 1, pos.getX() + 1, ymm, pos.getZ() + 1,
				0, 1 - (ylm - yf), 0, 1, 1, 1, 1, 1 - (ymm - yf),
				Direction.SOUTH, pos, world
		);
		IGui.drawBlockFace(
				matrices, vertexConsumers, texture,
				pos.getX() + 1, ymm, pos.getZ() + 1, pos.getX() + 1, yf, pos.getZ() + 1,
				pos.getX() + 1, yf, pos.getZ(), pos.getX() + 1, yml, pos.getZ(),
				0, 1 - (ymm - yf), 0, 1, 1, 1, 1, 1 - (yml - yf),
				Direction.EAST, pos, world
		);
		IGui.drawBlockFace(
				matrices, vertexConsumers, texture,
				pos.getX() + 1, yml, pos.getZ(), pos.getX() + 1, yf, pos.getZ(),
				pos.getX(), yf, pos.getZ(), pos.getX(), yll, pos.getZ(),
				0, 1 - (yml - yf), 0, 1, 1, 1, 1, 1 - (yll - yf),
				Direction.NORTH, pos, world
		);
	}

	private int getAxisAlignment(Pos3f c1, Pos3f c2, Pos3f c3, Pos3f c4) {
		if (c1.x == c2.x) {
			return c3.x == c4.x && c1.z == c4.z && c2.z == c3.z ? 2 : 0;
		} else if (c1.z == c2.z) {
			return c3.z == c4.z && c1.x == c4.x && c2.x == c3.x ? 1 : 0;
		} else {
			return 0;
		}
	}

	@Override
	public boolean rendersOutsideBoundingBox(BlockRail.TileEntityRail blockEntity) {
		return true;
	}
}
