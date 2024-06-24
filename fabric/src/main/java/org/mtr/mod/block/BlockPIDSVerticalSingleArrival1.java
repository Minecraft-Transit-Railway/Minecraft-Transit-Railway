package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockPIDSVerticalSingleArrival1 extends BlockPIDSVerticalBase {

	private static final int MAX_ARRIVALS = 16;

	public BlockPIDSVerticalSingleArrival1() {
		super(MAX_ARRIVALS);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityVerticalBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(MAX_ARRIVALS, BlockEntityTypes.PIDS_VERTICAL_SINGLE_ARRIVAL_1.get(), pos, state);
		}

		@Override
		public String defaultFormat(int line) {
			// TODO
			if (line == 0) {
				return "@0-100L@$#FF9900$%destination1%";
			} else if (line == 1) {
				return "@0-100R@$#FF9900$%RAH1%:%RA0m1%:%RA0s1%";
			} else {
				return "";
			}
		}
	}
}
