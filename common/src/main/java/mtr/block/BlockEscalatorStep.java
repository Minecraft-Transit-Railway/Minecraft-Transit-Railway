package mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockEscalatorStep extends BlockEscalatorBase {

	public static final BooleanProperty DIRECTION = BooleanProperty.create("direction");

	public static final EnumProperty<EnumEscalatorState> STATUS = EnumProperty.create("status",EnumEscalatorState.class);

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (direction == Direction.UP && !(world.getBlockState(pos.above()).getBlock() instanceof BlockEscalatorSide)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return super.updateShape(state, direction, newState, world, pos, posFrom);
		}
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
			IBlock.onBreakCreative(world, player, pos.relative(IBlock.getSideDirection(state)));
		}
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		EnumEscalatorOrientation orientation = IBlock.getStatePropertySafe(state, ORIENTATION);
		if (orientation == EnumEscalatorOrientation.FLAT || orientation == EnumEscalatorOrientation.TRANSITION_BOTTOM) {
			return Block.box(0, 0, 0, 16, 15, 16);
		} else {
			return Shapes.join(Block.box(1, 0, 1, 15, 16, 15), super.getCollisionShape(state, world, pos, context), BooleanOp.AND);
		}
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final boolean direction = IBlock.getStatePropertySafe(state,DIRECTION);
		final EnumEscalatorState running = IBlock.getStatePropertySafe(state,STATUS);
		final float speed = 0.05F;

		if(running == EnumEscalatorState.RUNNING){
			switch (facing) {
				case NORTH:
					entity.push(0, 0, direction ? -speed : speed);
					break;
				case EAST:
					entity.push(direction ? speed : -speed, 0, 0);
					break;
				case SOUTH:
					entity.push(0, 0, direction ? speed : -speed);
					break;
				case WEST:
					entity.push(direction ? -speed : speed, 0, 0);
					break;
				default:
					break;
			}
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		return IBlock.checkHoldingBrush(world, player, () -> {

			final Direction blockFacing = IBlock.getStatePropertySafe(state, FACING);
			final BlockPos sidePos = pos.relative(IBlock.getSideDirection(state));
			final boolean direction = IBlock.getStatePropertySafe(state,DIRECTION);
			final EnumEscalatorState status = IBlock.getStatePropertySafe(state,STATUS);

			if(status == EnumEscalatorState.STOP){
				// STOP to FORWARD
				updateRunning(world,pos,blockFacing,EnumEscalatorState.RUNNING);
				updateRunning(world,pos,blockFacing.getOpposite(),EnumEscalatorState.RUNNING);
				if (isStep(world, sidePos)) {
					final  BlockEscalatorStep block = (BlockEscalatorStep) world.getBlockState(sidePos).getBlock();
					block.updateRunning(world,sidePos, blockFacing, EnumEscalatorState.RUNNING);
					block.updateRunning(world,sidePos, blockFacing.getOpposite(), EnumEscalatorState.RUNNING);
				}
				updateDirections(world,pos,blockFacing,true);
				updateDirections(world,pos,blockFacing.getOpposite(),true);
				if (isStep(world, sidePos)) {
					final  BlockEscalatorStep block = (BlockEscalatorStep) world.getBlockState(sidePos).getBlock();
					block.updateDirections(world,sidePos, blockFacing, true);
					block.updateDirections(world,sidePos, blockFacing.getOpposite(), true);
				}
			} else if (status == EnumEscalatorState.RUNNING && direction) {
				// FORWARD to BACKWARD
				updateDirections(world,pos,blockFacing,false);
				updateDirections(world,pos,blockFacing.getOpposite(),false);
				if (isStep(world, sidePos)) {
					final  BlockEscalatorStep block = (BlockEscalatorStep) world.getBlockState(sidePos).getBlock();
					block.updateDirections(world,sidePos, blockFacing, false);
					block.updateDirections(world,sidePos, blockFacing.getOpposite(), false);
				}
			} else {
				// BACKWARD to STOP
				updateRunning(world,pos,blockFacing,EnumEscalatorState.STOP);
				updateRunning(world,pos,blockFacing.getOpposite(),EnumEscalatorState.STOP);
				if (isStep(world, sidePos)) {
					final  BlockEscalatorStep block = (BlockEscalatorStep) world.getBlockState(sidePos).getBlock();
					block.updateRunning(world,sidePos, blockFacing, EnumEscalatorState.STOP);
					block.updateRunning(world,sidePos, blockFacing.getOpposite(), EnumEscalatorState.STOP);
				}
			}
		});
	}


	@Override
	public boolean softenLanding() {
		return true;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, ORIENTATION, SIDE, DIRECTION, STATUS);
	}

	private void updateDirections(Level world, BlockPos pos, Direction offset, boolean direction){
		world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(DIRECTION, direction));
		final BlockPos offsetPos = pos.relative(offset);

		if (isStep(world, offsetPos)) {
			updateDirections(world, offsetPos, offset, direction);
		}
		if (isStep(world, offsetPos.above())) {
			updateDirections(world, offsetPos.above(), offset, direction);
		}
		if (isStep(world, offsetPos.below())) {
			updateDirections(world, offsetPos.below(), offset, direction);
		}
	}

	private void updateRunning(Level world, BlockPos pos, Direction offset, EnumEscalatorState status){
		world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(STATUS, status));
		final BlockPos offsetPos = pos.relative(offset);

		if (isStep(world, offsetPos)) {
			updateRunning(world, offsetPos, offset, status);
		}
		if (isStep(world, offsetPos.above())) {
			updateRunning(world, offsetPos.above(), offset, status);
		}
		if (isStep(world, offsetPos.below())) {
			updateRunning(world, offsetPos.below(), offset, status);
		}
	}

	private boolean isStep(Level world, BlockPos pos) {
		final Block block = world.getBlockState(pos).getBlock();
		return block instanceof BlockEscalatorStep;
	}

	// Will keep all state in one property instead of 2 but will cause
	// all escalators in an existing world go up.
	protected enum EnumEscalatorState implements StringRepresentable {
		RUNNING("running"),STOP("stop"),;/* FORWARD("forward"), BACKWARD("backward")*/;
		private final String name;
		EnumEscalatorState(String nameIn) {
			name = nameIn;
		}
		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
