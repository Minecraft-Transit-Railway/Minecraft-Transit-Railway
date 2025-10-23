package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockPIDSVertical1 extends BlockPIDSVerticalBase {

	private static final int MAX_ARRIVALS = 16;

	public BlockPIDSVertical1(AbstractBlock.Settings settings) {
		super(settings, MAX_ARRIVALS);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING));
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new PIDSVertical1BlockEntity(blockPos, blockState);
	}

	public static class PIDSVertical1BlockEntity extends BlockEntityVerticalBase {

		public PIDSVertical1BlockEntity(BlockPos pos, BlockState state) {
			super(MAX_ARRIVALS, BlockEntityTypes.PIDS_VERTICAL_1.get(), pos, state);
		}
	}
}