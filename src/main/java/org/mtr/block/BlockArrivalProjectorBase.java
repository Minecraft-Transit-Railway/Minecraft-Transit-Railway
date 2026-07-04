package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public abstract class BlockArrivalProjectorBase extends BlockPIDSBase {

	private static final BiPredicate<Level, BlockPos> CAN_STORE_DATA = (world, blockPos) -> true;
	private static final BiFunction<Level, BlockPos, BlockPos> GET_BLOCK_POS_WITH_DATA = (world, blockPos) -> blockPos;

	public BlockArrivalProjectorBase(BlockBehaviour.Properties settings, int maxArrivals) {
		super(settings, maxArrivals, CAN_STORE_DATA, GET_BLOCK_POS_WITH_DATA);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final Direction side = ctx.getClickedFace();
		if (side != Direction.UP && side != Direction.DOWN) {
			return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, side.getOpposite());
		} else {
			return null;
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, facing);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	public static abstract class BlockEntityArrivalProjectorBase extends BlockEntityBase {

		public BlockEntityArrivalProjectorBase(int maxArrivals, BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(maxArrivals, CAN_STORE_DATA, GET_BLOCK_POS_WITH_DATA, type, pos, state);
		}

		@Override
		public boolean showArrivalNumber() {
			return false;
		}

		@Override
		public boolean alternateLines() {
			return false;
		}

		@Override
		public int textColorArrived() {
			return 0xFF9900;
		}

		@Override
		public int textColor() {
			return 0xFF9900;
		}
	}
}
