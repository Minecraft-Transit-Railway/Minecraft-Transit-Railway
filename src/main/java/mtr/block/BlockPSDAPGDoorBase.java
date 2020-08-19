package mtr.block;

import net.minecraft.block.*;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public abstract class BlockPSDAPGDoorBase extends BlockPSDAPGBase implements BlockEntityProvider {

	public static final BooleanProperty END = BooleanProperty.of("end");
	public static final BooleanProperty OPEN = BooleanProperty.of("open");
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
				return superState.with(OPEN, isOpen(world, pos)).with(END, end);
			}
		}

	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (isOpen(world, pos)) {
			return VoxelShapes.empty();
		} else {
			return super.getOutlineShape(state, world, pos, context);
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(END, FACING, SIDE, OPEN, TOP);
	}

	public final boolean isOpen(BlockView world, BlockPos pos) {
		// TODO
		return false;
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
