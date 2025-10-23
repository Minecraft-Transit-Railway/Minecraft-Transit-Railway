package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockPIDSHorizontal2 extends BlockPIDSHorizontalBase {

	private static final int MAX_ARRIVALS = 3;

	public BlockPIDSHorizontal2(AbstractBlock.Settings settings) {
		super(settings, MAX_ARRIVALS);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape shape1 = IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 9, 16, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING));
		VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 9, 12.5, 8.5, 16, 13.5, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING));
		return VoxelShapes.union(shape1, shape2);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new PIDSHorizontal2BlockEntity(blockPos, blockState);
	}

	public static class PIDSHorizontal2BlockEntity extends BlockEntityHorizontalBase {

		public PIDSHorizontal2BlockEntity(BlockPos pos, BlockState state) {
			super(MAX_ARRIVALS, BlockEntityTypes.PIDS_HORIZONTAL_2.get(), pos, state);
		}

		@Override
		public boolean showArrivalNumber() {
			return true;
		}
	}
}