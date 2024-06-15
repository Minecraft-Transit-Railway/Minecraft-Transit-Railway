package org.mtr.mod.render;

import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.block.*;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;

import java.util.Collections;

public abstract class RenderSignalBase<T extends BlockSignalBase.BlockEntityBase> extends BlockEntityRenderer<T> implements IBlock, IGui {

	protected final boolean isSingleSided;
	protected final int aspects;
	private final float colorIndicatorHeight;

	public RenderSignalBase(Argument dispatcher, boolean isSingleSided, int colorIndicatorHeight, int aspects) {
		super(dispatcher);
		this.isSingleSided = isSingleSided;
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

		final float angle = IBlock.getStatePropertySafe(state, DirectionHelper.FACING).asRotation() + (IBlock.getStatePropertySafe(state, BlockSignalBase.IS_22_5).booleanValue ? 22.5F : 0) + (IBlock.getStatePropertySafe(state, BlockSignalBase.IS_45).booleanValue ? 45 : 0);

		final BlockPos startPos = getNodePos(world, pos, Direction.fromRotation(angle));
		if (startPos == null) {
			return;
		}

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + entity.getPos2().getX(), entity.getPos2().getY(), 0.5 + entity.getPos2().getZ());

		for (int i = 0; i < 2; i++) {
			final float newAngle = angle + i * 180;
			final boolean isBackSide = i == 1;
			final ObjectObjectImmutablePair<IntArrayList, IntAVLTreeSet> aspects = getAspects(isBackSide ? entity.signalColors2 : entity.signalColors1, startPos, newAngle + 90);
			final IntArrayList signalColors = aspects.left();
			final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
			storedMatrixTransformationsNew.add(graphicsHolderNew -> graphicsHolderNew.rotateYDegrees(-newAngle));

			if (!signalColors.isEmpty()) {
				if (RenderRails.isHoldingRailRelated(clientPlayerEntity)) {
					final float xStart = -0.015625F * signalColors.size();
					for (int j = 0; j < signalColors.size(); j++) {
						final int signalColor = signalColors.getInt(j);
						final boolean occupied = aspects.right().contains(signalColor);
						final float x = xStart + j * 0.03125F;
						final int lightNew = occupied ? RenderTrains.getFlashingLight() : GraphicsHolder.getDefaultLight();
						RenderTrains.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/white.png"), false, occupied ? RenderTrains.QueuedRenderLayer.EXTERIOR : RenderTrains.QueuedRenderLayer.LIGHT, (graphicsHolderNew, offset) -> {
							storedMatrixTransformationsNew.transform(graphicsHolderNew, offset);
							IDrawing.drawTexture(
									graphicsHolderNew,
									x, colorIndicatorHeight, -0.15625F,
									x + 0.03125F, colorIndicatorHeight, -0.15625F,
									x + 0.03125F, colorIndicatorHeight, -0.1875F,
									x, colorIndicatorHeight, -0.1875F,
									0, 0, 1, 1,
									Direction.UP, signalColor | ARGB_BLACK, lightNew
							);
							graphicsHolderNew.pop();
						});
					}
				}

				render(storedMatrixTransformationsNew, entity, tickDelta, aspects.right().isEmpty() ? 0 : 1, isBackSide);
			}

			if (isSingleSided) {
				break;
			}
		}
	}

	protected abstract void render(StoredMatrixTransformations storedMatrixTransformations, T entity, float tickDelta, int occupiedAspect, boolean isBackSide);

	private ObjectObjectImmutablePair<IntArrayList, IntAVLTreeSet> getAspects(LongArrayList filterColors, BlockPos startPos, float angle) {
		final MinecraftClientData minecraftClientData = MinecraftClientData.getInstance();
		final IntArrayList detectedColors = new IntArrayList();
		final IntAVLTreeSet occupiedColors = new IntAVLTreeSet();

		minecraftClientData.positionsToRail.get(Init.blockPosToPosition(startPos)).forEach((endPosition, rail) -> {
			final double difference = Math.abs(Math.toDegrees(Math.atan2(endPosition.getZ() - startPos.getZ(), endPosition.getX() - startPos.getX())) - angle) % 360;

			if (difference <= 90 || difference >= 270) {
				rail.getSignalColors().forEach(color -> {
					if (filterColors.isEmpty() || filterColors.contains(color)) {
						detectedColors.add(color);
					}
				});

				minecraftClientData.railIdToBlockedSignalColors.getOrDefault(rail.getHexId(), new LongArrayList()).forEach(color -> {
					if (filterColors.isEmpty() || filterColors.contains(color)) {
						occupiedColors.add((int) color);
					}
				});
			}
		});

		Collections.sort(detectedColors);
		return new ObjectObjectImmutablePair<>(detectedColors, occupiedColors);
	}

	private static BlockPos getNodePos(World world, BlockPos pos, Direction facing) {
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
