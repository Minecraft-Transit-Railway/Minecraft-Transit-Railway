package mtr.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public abstract class BlockStationNameTallBase extends BlockStationNameBase implements IBlock {

	public static final BooleanProperty METAL = BooleanProperty.of("metal");

	public BlockStationNameTallBase() {
		super(FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().hardness(2).nonOpaque());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final boolean isWhite = IBlock.getStatePropertySafe(state, COLOR) == 0;
			final int newColorProperty = isWhite ? 2 : 0;
			final boolean newMetalProperty = isWhite == IBlock.getStatePropertySafe(state, METAL);

			updateProperties(world, pos, newMetalProperty, newColorProperty);
			switch (IBlock.getStatePropertySafe(state, THIRD)) {
				case LOWER:
					updateProperties(world, pos.up(), newMetalProperty, newColorProperty);
					updateProperties(world, pos.up(2), newMetalProperty, newColorProperty);
					break;
				case MIDDLE:
					updateProperties(world, pos.down(), newMetalProperty, newColorProperty);
					updateProperties(world, pos.up(), newMetalProperty, newColorProperty);
					break;
				case UPPER:
					updateProperties(world, pos.down(), newMetalProperty, newColorProperty);
					updateProperties(world, pos.down(2), newMetalProperty, newColorProperty);
					break;
			}
		});
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if ((direction == Direction.UP && IBlock.getStatePropertySafe(state, THIRD) != EnumThird.UPPER || direction == Direction.DOWN && IBlock.getStatePropertySafe(state, THIRD) != EnumThird.LOWER) && !newState.isOf(this)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return state;
		}
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		switch (IBlock.getStatePropertySafe(state, THIRD)) {
			case MIDDLE:
				IBlock.onBreakCreative(world, player, pos.down());
				break;
			case UPPER:
				IBlock.onBreakCreative(world, player, pos.down(2));
				break;
		}
		super.onBreak(world, pos, state, player);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient) {
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			world.setBlockState(pos.up(), getDefaultState().with(FACING, facing).with(METAL, true).with(THIRD, EnumThird.MIDDLE), 3);
			world.setBlockState(pos.up(2), getDefaultState().with(FACING, facing).with(METAL, true).with(THIRD, EnumThird.UPPER), 3);
			world.updateNeighbors(pos, Blocks.AIR);
			state.updateNeighbors(world, pos, 3);
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(COLOR, FACING, METAL, THIRD);
	}

	protected static Pair<Integer, Integer> getBounds(BlockState state) {
		final EnumThird third = IBlock.getStatePropertySafe(state, THIRD);
		final int start, end;
		switch (third) {
			case LOWER:
				start = 10;
				end = 16;
				break;
			case UPPER:
				start = 0;
				end = 8;
				break;
			default:
				start = 0;
				end = 16;
				break;
		}
		return new Pair<>(start, end);
	}

	private static void updateProperties(World world, BlockPos pos, boolean metalProperty, int colorProperty) {
		world.setBlockState(pos, world.getBlockState(pos).with(COLOR, colorProperty).with(METAL, metalProperty));
	}

	public static class TileEntityStationNameTallBase extends TileEntityStationNameBase {

		public TileEntityStationNameTallBase(BlockEntityType<?> type, BlockPos pos, BlockState state, float zOffset) {
			super(type, pos, state, 0.25F, zOffset);
		}

		@Override
		public boolean shouldRender() {
			if (world == null) {
				return false;
			}
			return IBlock.getStatePropertySafe(world, pos, THIRD) == EnumThird.MIDDLE;
		}
	}
}
