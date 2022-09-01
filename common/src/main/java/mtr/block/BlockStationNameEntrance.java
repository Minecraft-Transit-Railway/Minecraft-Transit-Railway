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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockStationNameEntrance extends BlockStationNameBase implements IBlock {

	public static final IntegerProperty STYLE = IntegerProperty.create("propagate_property", 0, 5);

	public BlockStationNameEntrance(Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			world.setBlockAndUpdate(pos, state.cycle(STYLE));
			propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getClockWise(), STYLE, 1);
			propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getCounterClockWise(), STYLE, 1);
		});
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
		final boolean tall = IBlock.getStatePropertySafe(state, STYLE) % 2 == 1;
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
		builder.add(FACING, STYLE);
	}

	public static class TileEntityStationNameEntrance extends TileEntityStationNameBase {

		public TileEntityStationNameEntrance(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_ENTRANCE_TILE_ENTITY.get(), pos, state, 0, 0.00625F, false);
		}

		@Override
		public int getColor(BlockState state) {
			switch (IBlock.getStatePropertySafe(state, BlockStationNameBase.COLOR)) {
				case 1:
					return ARGB_LIGHT_GRAY;
				case 2:
					return ARGB_BLACK;
				default:
					return ARGB_WHITE;
			}
		}

		public AABB getRenderBoundingBox() {
			return new AABB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		}
	}
}
