package mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BlockPSDAPGDoorBase extends BlockPSDAPGBase {

	public static final int MAX_OPEN_VALUE = 32;

	public static final BooleanProperty END = BooleanProperty.create("end");
	public static final BooleanProperty ODD = BooleanProperty.create("odd");
	public static final IntegerProperty OPEN = IntegerProperty.create("open", 0, MAX_OPEN_VALUE);

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (IBlock.getSideDirection(state) == direction && !newState.is(this)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			final BlockState superState = super.updateShape(state, direction, newState, world, pos, posFrom);
			if (superState.getBlock() == Blocks.AIR) {
				return superState;
			} else {
				final boolean end = world.getBlockState(pos.relative(IBlock.getSideDirection(state).getOpposite())).getBlock() instanceof BlockPSDAPGGlassEndBase;
				return superState.setValue(END, end);
			}
		}
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		BlockPos offsetPos = pos;
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			offsetPos = offsetPos.below();
		}
		if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
			offsetPos = offsetPos.relative(IBlock.getSideDirection(state));
		}
		IBlock.onBreakCreative(world, player, offsetPos);
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos) {
		world.setBlockAndUpdate(pos, state.setValue(OPEN, 0));
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
		return IBlock.getStatePropertySafe(state, OPEN) > 0 ? Shapes.empty() : super.getCollisionShape(state, world, pos, collisionContext);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(END, FACING, HALF, OPEN, SIDE);
	}
}
