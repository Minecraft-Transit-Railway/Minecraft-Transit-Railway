package org.mtr.render;

import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.mtr.MTR;
import org.mtr.block.*;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;

import java.util.Collections;

public abstract class RenderSignalBase<T extends BlockSignalBase.BlockEntityBase> extends BlockEntityRendererExtension<T> implements IBlock, IGui {

	protected final int aspects;
	private final float colorIndicatorHeight;

	public RenderSignalBase(int colorIndicatorHeight, int aspects) {
		this.aspects = aspects;
		this.colorIndicatorHeight = colorIndicatorHeight / 16F + SMALL_OFFSET;
	}

	@Override
	public void render(T entity, ClientWorld world, ClientPlayerEntity player, float tickDelta, int light, int overlay) {
		final BlockPos pos = entity.getPos();
		final BlockState state = world.getBlockState(pos);
		if (!(state.getBlock() instanceof BlockSignalLightBase || state.getBlock() instanceof BlockSignalSemaphoreBase)) {
			return;
		}

		final float angle = BlockSignalBase.getAngle(state);
		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + entity.getPos().getX(), entity.getPos().getY(), 0.5 + entity.getPos().getZ());

		for (int i = 0; i < (entity.isDoubleSided ? 2 : 1); i++) {
			final float newAngle = angle + i * 180;
			final boolean isBackSide = i == 1;
			final ObjectObjectImmutablePair<IntArrayList, IntAVLTreeSet> aspects = getAspects(pos, newAngle + 90);
			final IntArrayList detectedColors = aspects.left();

			if (!detectedColors.isEmpty()) {
				final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
				storedMatrixTransformationsNew.add(matrixStack -> IDrawing.rotateYDegrees(matrixStack, -newAngle));
				final IntAVLTreeSet filterColors = entity.getSignalColors(isBackSide);

				if (RenderRails.isHoldingRailRelated(player)) {
					final float xStart = -0.015625F * detectedColors.size();
					for (int j = 0; j < detectedColors.size(); j++) {
						final int signalColor = detectedColors.getInt(j);
						final boolean occupied = aspects.right().contains(signalColor);
						final float x = xStart + j * 0.03125F;
						final float width = 0.03125F / (filterColors.isEmpty() || filterColors.contains(signalColor) ? 1 : 8);
						MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, "textures/block/white.png"), false, occupied ? QueuedRenderLayer.EXTERIOR : QueuedRenderLayer.LIGHT, (matrixStack, vertexConsumer, offset) -> {
							storedMatrixTransformationsNew.transform(matrixStack, offset);
							IDrawing.drawTexture(
									matrixStack, vertexConsumer,
									x, colorIndicatorHeight, -0.15625F,
									x + 0.03125F, colorIndicatorHeight, -0.15625F,
									x + 0.03125F, colorIndicatorHeight, -0.15625F - width,
									x, colorIndicatorHeight, -0.15625F - width,
									0, 0, 1, 1,
									Direction.UP, MainRenderer.getFlashingColor(signalColor | ARGB_BLACK), DEFAULT_LIGHT
							);
							matrixStack.pop();
						});
					}
				}

				render(storedMatrixTransformationsNew, entity, world, tickDelta, light, aspects.right().intStream().anyMatch(color -> filterColors.isEmpty() || filterColors.contains(color)) ? 1 : 0, isBackSide);
			}
		}
	}

	protected abstract void render(StoredMatrixTransformations storedMatrixTransformations, T entity, ClientWorld world, float tickDelta, int light, int occupiedAspect, boolean isBackSide);

	public static ObjectObjectImmutablePair<IntArrayList, IntAVLTreeSet> getAspects(BlockPos blockPos, float angle) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
		if (clientWorld == null) {
			return new ObjectObjectImmutablePair<>(new IntArrayList(), new IntAVLTreeSet());
		}

		final BlockPos startPos = getNodePos(clientWorld, blockPos, Direction.fromHorizontalDegrees(angle));
		if (startPos == null) {
			return new ObjectObjectImmutablePair<>(new IntArrayList(), new IntAVLTreeSet());
		}

		final MinecraftClientData minecraftClientData = MinecraftClientData.getInstance();
		final IntArrayList detectedColors = new IntArrayList();
		final IntAVLTreeSet occupiedColors = new IntAVLTreeSet();

		minecraftClientData.positionsToRail.getOrDefault(MTR.blockPosToPosition(startPos), new Object2ObjectOpenHashMap<>()).forEach((endPosition, rail) -> {
			if (Math.abs(Utilities.circularDifference(Math.round(Math.toDegrees(Math.atan2(endPosition.getZ() - startPos.getZ(), endPosition.getX() - startPos.getX()))), Math.round(angle), 360)) < 90) {
				rail.getSignalColors().forEach(detectedColors::add);
				minecraftClientData.railIdToBlockedSignalColors.getOrDefault(rail.getHexId(), new LongArrayList()).forEach(color -> occupiedColors.add((int) color));
			}
		});

		Collections.sort(detectedColors);
		return new ObjectObjectImmutablePair<>(detectedColors, occupiedColors);
	}

	private static BlockPos getNodePos(ClientWorld world, BlockPos pos, Direction facing) {
		final int[] checkDistance = {0, 1, -1, 2, -2, 3, -3, 4, -4};
		for (final int z : checkDistance) {
			for (final int x : checkDistance) {
				for (int y = -5; y <= 0; y++) {
					final BlockPos checkPos = pos.up(y).offset(facing.rotateYClockwise(), x).offset(facing, z);
					final BlockState checkState = world.getBlockState(checkPos);
					if (checkState.getBlock() instanceof BlockNode) {
						return checkPos;
					}
				}
			}
		}
		return null;
	}
}
