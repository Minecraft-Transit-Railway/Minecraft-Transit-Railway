package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.mtr.registry.BlockEntityTypes;

public class BlockPIDSHorizontal3 extends BlockPIDSHorizontalBase {

	private static final int MAX_ARRIVALS = 2;

	public BlockPIDSHorizontal3(BlockBehaviour.Properties settings) {
		super(settings, MAX_ARRIVALS);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		VoxelShape shape1 = IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 10, 16, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING));
		VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 10, 12.5, 8.5, 16, 13.5, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING));
		return Shapes.or(shape1, shape2);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new PIDSHorizontal3BlockEntity(blockPos, blockState);
	}

	public static class PIDSHorizontal3BlockEntity extends BlockEntityHorizontalBase {

		public PIDSHorizontal3BlockEntity(BlockPos pos, BlockState state) {
			super(MAX_ARRIVALS, BlockEntityTypes.PIDS_HORIZONTAL_3.get(), pos, state);
		}

		@Override
		public boolean showArrivalNumber() {
			return false;
		}

		@Override
		public int textColorArrived() {
			return 0x00FF00;
		}
	}
}
