package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;
import org.mtr.mod.render.pids.PIDSRenderController;

import javax.annotation.Nonnull;

public class BlockPIDSHorizontal2 extends BlockPIDSHorizontalBase {

	private static final int MAX_ARRIVALS = 3;
	private static final String DEFAULT_LAYOUT = "{\"_editor_size\":\"hb\", \"version\": 1,\"id\":\"base_horizontal_b\",\"modules\":[{\"type\":\"DestinationModule\",\"pos\":{\"x\":1.5,\"y\":1.5,\"w\":22.5,\"h\":1.75},\"align\":\"left\",\"color\":16777215,\"arrival\":0},{\"type\":\"DestinationModule\",\"pos\":{\"x\":1.5,\"y\":3.625,\"w\":22.5,\"h\":1.75},\"align\":\"left\",\"color\":16777215,\"arrival\":1},{\"type\":\"DestinationModule\",\"pos\":{\"x\":1.5,\"y\":5.75,\"w\":22.5,\"h\":1.75},\"align\":\"left\",\"color\":16777215,\"arrival\":2},{\"type\":\"ArrivalTimeModule\",\"pos\":{\"x\":24.5,\"y\":1.5,\"w\":6,\"h\":1.75},\"align\":\"right\",\"color\":16777215,\"arrival\":0},{\"type\":\"ArrivalTimeModule\",\"pos\":{\"x\":24.5,\"y\":3.625,\"w\":6,\"h\":1.75},\"align\":\"right\",\"color\":16777215,\"arrival\":1},{\"type\":\"ArrivalTimeModule\",\"pos\":{\"x\":24.5,\"y\":5.75,\"w\":6,\"h\":1.75},\"align\":\"right\",\"color\":16777215,\"arrival\":2}],\"name\":\"Base Horizontal Type B\",\"author\":\"EpicPuppy613\",\"description\":\"MTR Built-in layout.\\n\\nSize: 32 x 9\\nArrivals: 3\"}";

	public BlockPIDSHorizontal2() {
		super(MAX_ARRIVALS);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape shape1 = IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 9, 16, IBlock.getStatePropertySafe(state, FACING));
		VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 9, 12.5, 8.5, 16, 13.5, IBlock.getStatePropertySafe(state, FACING));
		return VoxelShapes.union(shape1, shape2);
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityHorizontalBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(MAX_ARRIVALS, "base_horizontal_b", BlockEntityTypes.PIDS_HORIZONTAL_2.get(), pos, state);
		}

		@Override
		public boolean showArrivalNumber() {
			return true;
		}
	}
}