package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockCeilingAuto extends BlockCeiling {

	public static final BooleanProperty LIGHT = BooleanProperty.of("light");

	public BlockCeilingAuto(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public BlockState getPlacementState2(ItemPlacementContext itemPlacementContext) {
		final boolean facing = itemPlacementContext.getPlayerFacing().getAxis() == Axis.X;
		return super.getPlacementState2(itemPlacementContext).with(new Property<>(FACING.data), facing).with(new Property<>(LIGHT.data), hasLight(facing, itemPlacementContext.getBlockPos()));
	}

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return super.getStateForNeighborUpdate2(state, direction, neighborState, world, pos, neighborPos).with(new Property<>(LIGHT.data), hasLight(IBlock.getStatePropertySafe(state, FACING), pos));
	}

	@Override
	public void randomDisplayTick2(BlockState state, World world, BlockPos pos, Random random) {
		final boolean light = hasLight(IBlock.getStatePropertySafe(state, FACING), pos);
		if (IBlock.getStatePropertySafe(state, LIGHT) != light) {
			world.setBlockState(pos, state.with(new Property<>(LIGHT.data), light));
		}
	}

	@Override
	public boolean hasRandomTicks2(BlockState state) {
		return true;
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		super.addBlockProperties(properties);
		properties.add(LIGHT);
	}

	private static boolean hasLight(boolean facing, BlockPos pos) {
		if (facing) {
			return pos.getZ() % 3 == 0;
		} else {
			return pos.getX() % 3 == 0;
		}
	}
}
