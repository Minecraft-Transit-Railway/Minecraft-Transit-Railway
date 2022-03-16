package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockStationNameWall extends BlockStationNameBase implements IBlock {

	public static final BooleanProperty MERGED = BooleanProperty.create("merged");

	public BlockStationNameWall(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(MERGED, false));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			world.setBlockAndUpdate(pos, state.cycle(COLOR));
			propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getClockWise(), COLOR, 1);
			propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getCounterClockWise(), COLOR, 1);
		});
	}

	public InteractionResult toggleMerge(Level world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		world.setBlockAndUpdate(pos, state.cycle(MERGED));
		propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getClockWise(), MERGED, 1);
		propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getCounterClockWise(), MERGED, 1);
		return InteractionResult.SUCCESS;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return world.getBlockState(pos.relative(facing)).getMaterial().isSolid();
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final Direction side = ctx.getClickedFace();
		if (side != Direction.UP && side != Direction.DOWN) {
			return defaultBlockState().setValue(FACING, side.getOpposite());
		} else {
			return null;
		}
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (direction.getOpposite() == IBlock.getStatePropertySafe(state, FACING).getOpposite() && !state.canSurvive(world, pos)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return state;
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return Shapes.empty();
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityStationNameWall(pos, state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(COLOR, FACING, MERGED);
	}

	public static class TileEntityStationNameWall extends TileEntityStationNameBase {

		public TileEntityStationNameWall(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_WALL_TILE_ENTITY, pos, state, 0, 0);
		}

		@Override
		public boolean shouldRender() {
			return level != null && !(level.getBlockState(worldPosition.relative(IBlock.getStatePropertySafe(level, worldPosition, FACING).getCounterClockWise())).getBlock() instanceof BlockStationNameWall && IBlock.getStatePropertySafe(level.getBlockState(worldPosition.relative(IBlock.getStatePropertySafe(level, worldPosition, FACING).getCounterClockWise())), MERGED));
		}
	}
}
