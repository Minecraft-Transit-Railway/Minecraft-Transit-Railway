package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;
import org.mtr.mod.render.pids.PIDSRenderController;

import javax.annotation.Nonnull;

public class BlockPIDSHorizontal3 extends BlockPIDSHorizontalBase {

	private static final int MAX_ARRIVALS = 2;
	private static final String DEFAULT_LAYOUT = "{\"_editor_size\":\"hc\", \"version\": 1,\"id\":\"base_horizontal_c\",\"modules\":[{\"type\":\"DestinationModule\",\"pos\":{\"x\":1.75,\"y\":1.75,\"w\":20.25,\"h\":2.75},\"align\":\"left\",\"color\":16777215,\"arrival\":0},{\"type\":\"DestinationModule\",\"pos\":{\"x\":1.75,\"y\":5.5,\"w\":20.25,\"h\":2.75},\"align\":\"left\",\"color\":16777215,\"arrival\":1},{\"type\":\"ArrivalTimeModule\",\"pos\":{\"x\":22.5,\"y\":1.75,\"w\":7.75,\"h\":2.75},\"align\":\"right\",\"color\":16777215,\"arrival\":0},{\"type\":\"ArrivalTimeModule\",\"pos\":{\"x\":22.5,\"y\":5.5,\"w\":7.75,\"h\":2.75},\"align\":\"right\",\"color\":16777215,\"arrival\":1}],\"name\":\"Base Horizontal Type C\",\"author\":\"EpicPuppy613\",\"description\":\"MTR Built-in layout.\\n\\nSize: 32 x 10\\nArrivals: 2\"}";

	public BlockPIDSHorizontal3() {
		super(MAX_ARRIVALS);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape shape1 = IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 10, 16, IBlock.getStatePropertySafe(state, FACING));
		VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 10, 12.5, 8.5, 16, 13.5, IBlock.getStatePropertySafe(state, FACING));
		return VoxelShapes.union(shape1, shape2);
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityHorizontalBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(MAX_ARRIVALS, "base_horizontal_c", BlockEntityTypes.PIDS_HORIZONTAL_3.get(), pos, state);
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