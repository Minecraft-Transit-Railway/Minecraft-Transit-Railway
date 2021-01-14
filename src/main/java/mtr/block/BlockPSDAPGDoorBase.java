package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public abstract class BlockPSDAPGDoorBase extends BlockPSDAPGBase {

	public static final int MAX_OPEN_VALUE = 32;

	public static final BooleanProperty END = BooleanProperty.of("end");
	public static final IntProperty OPEN = IntProperty.of("open", 0, MAX_OPEN_VALUE);
	public static final EnumProperty<EnumPSDAPGDoorSide> SIDE = EnumProperty.of("side", EnumPSDAPGDoorSide.class);

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (getSideDirection(state) == direction && !newState.isOf(this)) {
			return Blocks.AIR.getDefaultState();
		} else {
			BlockState superState = super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
			if (superState.getBlock() == Blocks.AIR) {
				return superState;
			} else {
				final boolean end = world.getBlockState(pos.offset(getSideDirection(state).getOpposite())).getBlock() instanceof BlockPSDAPGGlassEndBase;
				return superState.with(END, end);
			}
		}

	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final int open = state.get(OPEN);
		if (open > 0) {
			final int height = isAPG() && state.get(TOP) ? 9 : 16;
			final EnumPSDAPGDoorSide side = state.get(SIDE);
			final double open1 = open / 2D;
			final double open2 = 16 - open / 2D;
			return IBlock.getVoxelShapeByDirection(side == EnumPSDAPGDoorSide.LEFT ? 0 : open1, 0, 0, side == EnumPSDAPGDoorSide.RIGHT ? 16 : open2, height, 4, state.get(FACING));
		} else {
			return super.getOutlineShape(state, world, pos, context);
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(END, FACING, SIDE, OPEN, TOP);
	}

	private Direction getSideDirection(BlockState state) {
		final Direction facing = state.get(FACING);
		return state.get(SIDE) == EnumPSDAPGDoorSide.LEFT ? facing.rotateYClockwise() : facing.rotateYCounterclockwise();
	}

	public enum EnumPSDAPGDoorSide implements StringIdentifiable {

		LEFT("left"), RIGHT("right");

		private final String name;

		EnumPSDAPGDoorSide(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return name;
		}
	}
}
