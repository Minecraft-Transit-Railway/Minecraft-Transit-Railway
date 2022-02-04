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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockStationNameEntrance extends BlockStationNameBase implements IPropagateBlock {
	public static final BooleanProperty SHOW_STATION = BooleanProperty.of("name");

	public BlockStationNameEntrance(Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		boolean showStation = state.get(SHOW_STATION);
		if(player.isHolding(Items.STICK)) {
			world.setBlockState(pos, (BlockState) state.with(SHOW_STATION, !showStation));
			this.propagate2(world, pos, ((Direction) IBlock.getStatePropertySafe(state, FACING)).rotateYClockwise(), 1);
			this.propagate2(world, pos, ((Direction) IBlock.getStatePropertySafe(state, FACING)).rotateYCounterclockwise(), 1);
			return ActionResult.SUCCESS;
		}else{
			return IBlock.checkHoldingBrush(world, player, () -> {
				world.setBlockState(pos, (BlockState) state.cycle(PROPAGATE_PROPERTY));
				this.propagate(world, pos, ((Direction) IBlock.getStatePropertySafe(state, FACING)).rotateYClockwise(), 1);
				this.propagate(world, pos, ((Direction) IBlock.getStatePropertySafe(state, FACING)).rotateYCounterclockwise(), 1);
			});
		}
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
			return defaultBlockState().setValue(FACING, side.getOpposite().with(SHOW_STATION, true));
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
		final boolean tall = IBlock.getStatePropertySafe(state, PROPAGATE_PROPERTY) % 2 == 1;
		return IBlock.getVoxelShapeByDirection(0, tall ? 0 : 4, 0, 16, tall ? 16 : 12, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return Shapes.empty();
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityStationNameEntrance(pos, state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, PROPAGATE_PROPERTY, SHOW_STATION);
	}

	public static class TileEntityStationNameEntrance extends TileEntityStationNameBase {

		public TileEntityStationNameEntrance(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_ENTRANCE_TILE_ENTITY, pos, state, 0, 0.00625F);
		}

		@Override
		public boolean shouldRender() {
			return level != null && !(level.getBlockState(worldPosition.relative(IBlock.getStatePropertySafe(level, worldPosition, FACING).getCounterClockWise())).getBlock() instanceof BlockStationNameEntrance);
		}
	}

	void propagate2(World world, BlockPos pos, Direction direction, int maxBlocksAway) {
		for (int i = 1; i <= maxBlocksAway; i++) {
			final BlockPos offsetPos = pos.offset(direction, i);
			final BlockState offsetState = world.getBlockState(offsetPos);
			if (this == offsetState.getBlock()) {
				world.setBlockState(offsetPos, offsetState.with(SHOW_STATION, IBlock.getStatePropertySafe(world, pos, SHOW_STATION)));
				propagate2(world, offsetPos, direction, maxBlocksAway);
				return;
			}
		}
	}
}
