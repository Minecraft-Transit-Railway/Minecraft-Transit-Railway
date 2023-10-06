package org.mtr.mod.render;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockNode;
import org.mtr.mod.block.BlockSignalLightBase;
import org.mtr.mod.block.BlockSignalSemaphoreBase;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.data.IGui;

public abstract class RenderSignalBase<T extends BlockEntityExtension> extends BlockEntityRenderer<T> implements IBlock, IGui {

	protected final boolean isSingleSided;
	protected final int aspects;

	public RenderSignalBase(Argument dispatcher, boolean isSingleSided, int aspects) {
		super(dispatcher);
		this.isSingleSided = isSingleSided;
		this.aspects = aspects;
	}

	@Override
	public final void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
		final World world = entity.getWorld2();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos2();
		final BlockState state = world.getBlockState(pos);
		if (!(state.getBlock().data instanceof BlockSignalLightBase || state.getBlock().data instanceof BlockSignalSemaphoreBase)) {
			return;
		}
		final Direction facing = IBlock.getStatePropertySafe(state, DirectionHelper.FACING);
		if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, null)) {
			return;
		}

		final BlockPos startPos = getNodePos(world, pos, facing);
		if (startPos == null) {
			return;
		}

		graphicsHolder.push();
		graphicsHolder.translate(0.5, 0, 0.5);

		for (int i = 0; i < 2; i++) {
			final Direction newFacing = (i == 1 ? facing.getOpposite() : facing);
			final int occupiedAspect = getOccupiedAspect(startPos, newFacing.asRotation() + 90);

			if (occupiedAspect >= 0) {
				graphicsHolder.push();
				graphicsHolder.rotateYDegrees(-newFacing.asRotation());
				graphicsHolder.createVertexConsumer(MoreRenderLayers.getLight(new Identifier(Init.MOD_ID, "textures/block/white.png"), false));
				render(graphicsHolder, entity, tickDelta, newFacing, occupiedAspect, i == 1);
				graphicsHolder.pop();
			}

			if (isSingleSided) {
				break;
			}
		}

		graphicsHolder.pop();
	}

	protected abstract void render(GraphicsHolder graphicsHolder, T entity, float tickDelta, Direction facing, int occupiedAspect, boolean isBackSide);

	private int getOccupiedAspect(BlockPos startPos, float facing) {
		return 0; // TODO
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
