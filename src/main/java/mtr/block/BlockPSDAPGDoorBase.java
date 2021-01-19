package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public abstract class BlockPSDAPGDoorBase extends BlockPSDAPGBase {

	public static final int MAX_OPEN_VALUE = 32;

	public static final BooleanProperty END = BooleanProperty.of("end");
	public static final IntProperty OPEN = IntProperty.of("open", 0, MAX_OPEN_VALUE);

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (IBlock.getSideDirection(state) == direction && !newState.isOf(this)) {
			return Blocks.AIR.getDefaultState();
		} else {
			final BlockState superState = super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
			if (superState.getBlock() == Blocks.AIR) {
				return superState;
			} else {
				final boolean end = world.getBlockState(pos.offset(IBlock.getSideDirection(state).getOpposite())).getBlock() instanceof BlockPSDAPGGlassEndBase;
				return superState.with(END, end);
			}
		}
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockPos offsetPos = pos;
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			offsetPos = offsetPos.down();
		}
		if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
			offsetPos = offsetPos.offset(IBlock.getSideDirection(state));
		}
		IBlock.onBreakCreative(world, player, offsetPos);
		super.onBreak(world, pos, state, player);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final int open = IBlock.getStatePropertySafe(state, OPEN);
		if (open > 0) {
			final int height = isAPG() && IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 9 : 16;
			final EnumSide side = IBlock.getStatePropertySafe(state, SIDE);
			final double open1 = open / 2D;
			final double open2 = 16 - open / 2D;
			return IBlock.getVoxelShapeByDirection(side == EnumSide.LEFT ? 0 : open1, 0, 0, side == EnumSide.RIGHT ? 16 : open2, height, 4, IBlock.getStatePropertySafe(state, FACING));
		} else {
			return super.getOutlineShape(state, world, pos, context);
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(END, FACING, HALF, OPEN, SIDE);
	}
}
