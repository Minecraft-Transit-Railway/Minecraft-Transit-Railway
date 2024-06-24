package org.mtr.mod.render;

import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.block.*;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;

import java.util.Collections;

public abstract class RenderSignalBase<T extends BlockSignalBase.BlockEntityBase> extends BlockEntityRenderer<T> implements IBlock, IGui {

	protected final int aspects;
	private final float colorIndicatorHeight;

	public RenderSignalBase(Argument dispatcher, int colorIndicatorHeight, int aspects) {
		super(dispatcher);
		this.aspects = aspects;
		this.colorIndicatorHeight = colorIndicatorHeight / 16F + SMALL_OFFSET;
	}

	@Override
	public final void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
		final World world = entity.getWorld2();
		if (world == null) {
			return;
		}

		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity == null) {
			return;
		}

		final BlockPos pos = entity.getPos2();
		final BlockState state = world.getBlockState(pos);
		if (!(state.getBlock().data instanceof BlockSignalLightBase || state.getBlock().data instanceof BlockSignalSemaphoreBase)) {
			return;
		}

		final float angle = BlockSignalBase.getAngle(state);
		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + entity.getPos2().getX(), entity.getPos2().getY(), 0.5 + entity.getPos2().getZ());

		for (int i = 0; i < (entity.isDoubleSided ? 2 : 1); i++) {
			final float newAngle = angle + i * 180;
			final boolean isBackSide = i == 1;
			final ObjectObjectImmutablePair<IntArrayList, IntAVLTreeSet> aspects = getAspects(pos, newAngle + 90);
			final IntArrayList detectedColors = aspects.left();

			if (!detectedColors.isEmpty()) {
				final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
				storedMatrixTransformationsNew.add(graphicsHolderNew -> graphicsHolderNew.rotateYDegrees(-newAngle));
				final IntAVLTreeSet filterColors = entity.getSignalColors(isBackSide);

				if (RenderRails.isHoldingRailRelated(clientPlayerEntity)) {
					final float xStart = -0.015625F * detectedColors.size();
					for (int j = 0; j < detectedColors.size(); j++) {
						final int signalColor = detectedColors.getInt(j);
						final boolean occupied = aspects.right().contains(signalColor);
						final float x = xStart + j * 0.03125F;
						final float width = 0.03125F / (filterColors.isEmpty() || filterColors.contains(signalColor) ? 1 : 8);
						final int lightNew = occupied ? MainRenderer.getFlashingLight() : GraphicsHolder.getDefaultLight();
						MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/white.png"), false, occupied ? QueuedRenderLayer.EXTERIOR : QueuedRenderLayer.LIGHT, (graphicsHolderNew, offset) -> {
							storedMatrixTransformationsNew.transform(graphicsHolderNew, offset);
							IDrawing.drawTexture(
									graphicsHolderNew,
									x, colorIndicatorHeight, -0.15625F,
									x + 0.03125F, colorIndicatorHeight, -0.15625F,
									x + 0.03125F, colorIndicatorHeight, -0.15625F - width,
									x, colorIndicatorHeight, -0.15625F - width,
									0, 0, 1, 1,
									Direction.UP, signalColor | ARGB_BLACK, lightNew
							);
							graphicsHolderNew.pop();
						});
					}
				}

				render(storedMatrixTransformationsNew, entity, tickDelta, aspects.right().intStream().anyMatch(color -> filterColors.isEmpty() || filterColors.contains(color)) ? 1 : 0, isBackSide);
			}
		}
	}

	protected abstract void render(StoredMatrixTransformations storedMatrixTransformations, T entity, float tickDelta, int occupiedAspect, boolean isBackSide);

	public static ObjectObjectImmutablePair<IntArrayList, IntAVLTreeSet> getAspects(BlockPos blockPos, float angle) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			return new ObjectObjectImmutablePair<>(new IntArrayList(), new IntAVLTreeSet());
		}

		final BlockPos startPos = getNodePos(clientWorld, blockPos, Direction.fromRotation(angle));
		if (startPos == null) {
			return new ObjectObjectImmutablePair<>(new IntArrayList(), new IntAVLTreeSet());
		}

		final MinecraftClientData minecraftClientData = MinecraftClientData.getInstance();
		final IntArrayList detectedColors = new IntArrayList();
		final IntAVLTreeSet occupiedColors = new IntAVLTreeSet();

		minecraftClientData.positionsToRail.getOrDefault(Init.blockPosToPosition(startPos), new Object2ObjectOpenHashMap<>()).forEach((endPosition, rail) -> {
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
					if (checkState.getBlock().data instanceof BlockNode) {
						return checkPos;
					}
				}
			}
		}
		return null;
	}
}
