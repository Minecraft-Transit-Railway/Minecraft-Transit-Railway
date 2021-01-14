package mtr.block;

import mtr.MTR;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class BlockStationNameBlock extends BlockStationNameBase {

	public static final BooleanProperty METAL = BooleanProperty.of("metal");
	public static final EnumProperty<EnumSegment> SEGMENT = EnumProperty.of("segment", EnumSegment.class);

	public BlockStationNameBlock(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final boolean isWhite = state.get(COLOR) == 0;
			final int newColorProperty = isWhite ? 2 : 0;
			final boolean newMetalProperty = isWhite == state.get(METAL);

			updateProperties(world, pos, newMetalProperty, newColorProperty);
			switch (state.get(SEGMENT)) {
				case BOTTOM:
					updateProperties(world, pos.up(), newMetalProperty, newColorProperty);
					updateProperties(world, pos.up(2), newMetalProperty, newColorProperty);
					break;
				case MIDDLE:
					updateProperties(world, pos.down(), newMetalProperty, newColorProperty);
					updateProperties(world, pos.up(), newMetalProperty, newColorProperty);
					break;
				case TOP:
					updateProperties(world, pos.down(), newMetalProperty, newColorProperty);
					updateProperties(world, pos.down(2), newMetalProperty, newColorProperty);
					break;
			}
		});
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if ((direction == Direction.UP && state.get(SEGMENT) != EnumSegment.TOP || direction == Direction.DOWN && state.get(SEGMENT) != EnumSegment.BOTTOM) && !newState.isOf(this)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return state;
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return IBlock.isReplaceable(ctx, Direction.UP, 3) ? getDefaultState().with(FACING, ctx.getPlayerFacing()).with(METAL, true).with(SEGMENT, EnumSegment.BOTTOM) : null;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient) {
			final Direction facing = state.get(FACING);
			world.setBlockState(pos.up(), getDefaultState().with(FACING, facing).with(METAL, true).with(SEGMENT, EnumSegment.MIDDLE), 3);
			world.setBlockState(pos.up(2), getDefaultState().with(FACING, facing).with(METAL, true).with(SEGMENT, EnumSegment.TOP), 3);
			world.updateNeighbors(pos, Blocks.AIR);
			state.updateNeighbors(world, pos, 3);
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final EnumSegment segment = state.get(SEGMENT);
		final int start, end;
		switch (segment) {
			case BOTTOM:
				start = 10;
				end = 16;
				break;
			case TOP:
				start = 0;
				end = 8;
				break;
			default:
				start = 0;
				end = 16;
				break;
		}
		return VoxelShapes.union(IBlock.getVoxelShapeByDirection(2, start, 5, 14, end, 11, state.get(FACING)), BlockStationPole.getStationPoleShape());
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityStationNameBlock();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(COLOR, FACING, METAL, SEGMENT);
	}

	private void updateProperties(World world, BlockPos pos, boolean metalProperty, int colorProperty) {
		world.setBlockState(pos, world.getBlockState(pos).with(COLOR, colorProperty).with(METAL, metalProperty));
	}

	public enum EnumSegment implements StringIdentifiable {

		TOP("top"), MIDDLE("middle"), BOTTOM("bottom");

		private final String name;

		EnumSegment(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return name;
		}
	}

	public static class TileEntityStationNameBlock extends TileEntityStationNameBase {

		public TileEntityStationNameBlock() {
			super(MTR.STATION_NAME_BLOCK_TILE_ENTITY, true, false, 80, 0.25F, 0.6875F);
		}

		@Override
		public boolean shouldRender() {
			if (world == null) {
				return false;
			}
			final BlockState state = world.getBlockState(pos);
			return state.getBlock() instanceof BlockStationNameBlock && state.get(SEGMENT) == EnumSegment.MIDDLE;
		}
	}
}
