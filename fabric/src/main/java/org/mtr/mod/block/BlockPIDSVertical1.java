package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;
import org.mtr.mod.render.pids.PIDSRenderController;

import javax.annotation.Nonnull;

public class BlockPIDSVertical1 extends BlockPIDSVerticalBase {

	private static final int MAX_ARRIVALS = 16;
	private static final String DEFAULT_LAYOUT = "{\"_editor_size\":\"va\", \"version\": 1,\"id\":\"base_vertical_a\",\"modules\":[{\"type\":\"DestinationModule\",\"pos\":{\"x\":1.25,\"y\":1.25,\"w\":13.5,\"h\":1.75},\"align\":\"left\",\"color\":16777215,\"arrival\":0},{\"type\":\"ArrivalTimeModule\",\"pos\":{\"x\":8.25,\"y\":3.25,\"w\":6.5,\"h\":1.75},\"align\":\"right\",\"color\":16777215,\"arrival\":0},{\"type\":\"TrainLengthModule\",\"pos\":{\"x\":1.25,\"y\":3.25,\"w\":6.5,\"h\":1.75},\"align\":\"left\",\"color\":16711680,\"arrival\":0},{\"type\":\"DestinationModule\",\"pos\":{\"x\":1.25,\"y\":5.5,\"w\":13.5,\"h\":1.75},\"align\":\"left\",\"color\":16777215,\"arrival\":1},{\"type\":\"ArrivalTimeModule\",\"pos\":{\"x\":8.25,\"y\":7.5,\"w\":6.5,\"h\":1.75},\"align\":\"right\",\"color\":16777215,\"arrival\":1},{\"type\":\"TrainLengthModule\",\"pos\":{\"x\":1.25,\"y\":7.5,\"w\":6.5,\"h\":1.75},\"align\":\"left\",\"color\":16711680,\"arrival\":1},{\"type\":\"DestinationModule\",\"pos\":{\"x\":1.25,\"y\":9.75,\"w\":13.5,\"h\":1.75},\"align\":\"left\",\"color\":16777215,\"arrival\":2},{\"type\":\"ArrivalTimeModule\",\"pos\":{\"x\":8.25,\"y\":11.75,\"w\":6.5,\"h\":1.75},\"align\":\"right\",\"color\":16777215,\"arrival\":2},{\"type\":\"TrainLengthModule\",\"pos\":{\"x\":1.25,\"y\":11.75,\"w\":6.5,\"h\":1.75},\"align\":\"left\",\"color\":16711680,\"arrival\":2},{\"type\":\"DestinationModule\",\"pos\":{\"x\":1.25,\"y\":14,\"w\":13.5,\"h\":1.75},\"align\":\"left\",\"color\":16777215,\"arrival\":3},{\"type\":\"ArrivalTimeModule\",\"pos\":{\"x\":8.25,\"y\":16,\"w\":6.5,\"h\":1.75},\"align\":\"right\",\"color\":16777215,\"arrival\":3},{\"type\":\"TrainLengthModule\",\"pos\":{\"x\":1.25,\"y\":16,\"w\":6.5,\"h\":1.75},\"align\":\"left\",\"color\":16711680,\"arrival\":3},{\"type\":\"DestinationModule\",\"pos\":{\"x\":1.25,\"y\":18.25,\"w\":13.5,\"h\":1.75},\"align\":\"left\",\"color\":16777215,\"arrival\":4},{\"type\":\"ArrivalTimeModule\",\"pos\":{\"x\":8.25,\"y\":20.25,\"w\":6.5,\"h\":1.75},\"align\":\"right\",\"color\":16777215,\"arrival\":4},{\"type\":\"TrainLengthModule\",\"pos\":{\"x\":1.25,\"y\":20.25,\"w\":6.5,\"h\":1.75},\"align\":\"left\",\"color\":16711680,\"arrival\":4},{\"type\":\"DestinationModule\",\"pos\":{\"x\":1.25,\"y\":22.5,\"w\":13.5,\"h\":1.75},\"align\":\"left\",\"color\":16777215,\"arrival\":5},{\"type\":\"ArrivalTimeModule\",\"pos\":{\"x\":8.25,\"y\":24.5,\"w\":6.5,\"h\":1.75},\"align\":\"right\",\"color\":16777215,\"arrival\":5},{\"type\":\"TrainLengthModule\",\"pos\":{\"x\":1.25,\"y\":24.5,\"w\":6.5,\"h\":1.75},\"align\":\"left\",\"color\":16711680,\"arrival\":5},{\"type\":\"DestinationModule\",\"pos\":{\"x\":1.25,\"y\":26.75,\"w\":13.5,\"h\":1.75},\"align\":\"left\",\"color\":16777215,\"arrival\":6},{\"type\":\"ArrivalTimeModule\",\"pos\":{\"x\":8.25,\"y\":28.75,\"w\":6.375,\"h\":1.75},\"align\":\"right\",\"color\":16777215,\"arrival\":6},{\"type\":\"TrainLengthModule\",\"pos\":{\"x\":1.25,\"y\":28.75,\"w\":6.5,\"h\":1.75},\"align\":\"left\",\"color\":16711680,\"arrival\":6}],\"name\":\"Base Vertical Type A\",\"author\":\"EpicPuppy613\",\"description\":\"MTR Built-in layout.\\n\\nSize: 16 x 32\\nArrivals: 7\"}";

	public BlockPIDSVertical1() {
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
			super(MAX_ARRIVALS, "base_vertical_a", BlockEntityTypes.PIDS_VERTICAL_1.get(), pos, state);
		}
	}
}