package org.mtr.mod.block;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockStationNameTallBase extends BlockStationNameBase implements IBlock {

	public static final BooleanProperty METAL = BooleanProperty.of("metal");

	public BlockStationNameTallBase() {
		super(org.mtr.mod.Blocks.createDefaultBlockSettings(true));
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
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

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if ((direction == Direction.UP && IBlock.getStatePropertySafe(state, THIRD) != EnumThird.UPPER || direction == Direction.DOWN && IBlock.getStatePropertySafe(state, THIRD) != EnumThird.LOWER) && !neighborState.isOf(new Block(this))) {
			return Blocks.getAirMapped().getDefaultState();
		} else {
			return state;
		}
	}

	@Override
	public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		switch (IBlock.getStatePropertySafe(state, THIRD)) {
			case MIDDLE:
				IBlock.onBreakCreative(world, player, pos.down());
				break;
			case UPPER:
				IBlock.onBreakCreative(world, player, pos.down(2));
				break;
		}
		super.onBreak2(world, pos, state, player);
	}

	@Override
	public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient()) {
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			world.setBlockState(pos.up(), getDefaultState2().with(new Property<>(FACING.data), facing.data).with(new Property<>(METAL.data), true).with(new Property<>(THIRD.data), EnumThird.MIDDLE), 3);
			world.setBlockState(pos.up(2), getDefaultState2().with(new Property<>(FACING.data), facing.data).with(new Property<>(METAL.data), true).with(new Property<>(THIRD.data), EnumThird.UPPER), 3);
			world.updateNeighbors(pos, Blocks.getAirMapped());
			state.updateNeighbors(new WorldAccess(world.data), pos, 3);
		}
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(COLOR);
		properties.add(FACING);
		properties.add(METAL);
		properties.add(THIRD);
	}

	protected static ImmutablePair<Integer, Integer> getBounds(BlockState state) {
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
		return new ImmutablePair<>(start, end);
	}

	private static void updateProperties(World world, BlockPos pos, boolean metalProperty, int colorProperty) {
		world.setBlockState(pos, world.getBlockState(pos).with(new Property<>(COLOR.data), colorProperty).with(new Property<>(METAL.data), metalProperty));
	}

	public static class BlockEntityTallBase extends BlockEntityBase {

		public BlockEntityTallBase(BlockEntityType<?> type, BlockPos pos, BlockState state, float zOffset, boolean isDoubleSided) {
			super(type, pos, state, 0.21875F, zOffset, isDoubleSided);
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
	}
}
