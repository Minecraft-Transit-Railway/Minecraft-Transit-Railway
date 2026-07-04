package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntIntImmutablePair;

//? if >= 1.21.4 {
import net.minecraft.world.level.ScheduledTickAccess;
//? } else {
/*import net.minecraft.world.level.LevelAccessor;
 *///? }

public abstract class BlockStationNameTallBase extends BlockStationNameBase implements IBlock {

	public static final BooleanProperty METAL = BooleanProperty.create("metal");
	public static final float WIDTH = 0.6875F;
	public static final float HEIGHT = 1.5F;
	public static final float OFFSET_Y = 0.125F;

	public BlockStationNameTallBase(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final boolean isWhite = IBlock.getStatePropertySafe(state, COLOR) == 0;
			final int newColorProperty = isWhite ? 2 : 0;
			final boolean newMetalProperty = isWhite == IBlock.getStatePropertySafe(state, METAL);

			updateProperties(world, pos, newMetalProperty, newColorProperty);
			switch (IBlock.getStatePropertySafe(state, THIRD)) {
				case LOWER:
					updateProperties(world, pos.above(), newMetalProperty, newColorProperty);
					updateProperties(world, pos.above(2), newMetalProperty, newColorProperty);
					break;
				case MIDDLE:
					updateProperties(world, pos.below(), newMetalProperty, newColorProperty);
					updateProperties(world, pos.above(), newMetalProperty, newColorProperty);
					break;
				case UPPER:
					updateProperties(world, pos.below(), newMetalProperty, newColorProperty);
					updateProperties(world, pos.below(2), newMetalProperty, newColorProperty);
					break;
			}
		});
	}

	@Override
//? if >= 1.21.4 {
	protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
//? } else {
	/*protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
//
*///? }
		if ((direction == Direction.UP && IBlock.getStatePropertySafe(state, THIRD) != EnumThird.UPPER || direction == Direction.DOWN && IBlock.getStatePropertySafe(state, THIRD) != EnumThird.LOWER) && !neighborState.is(this)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return state;
		}
	}

	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		switch (IBlock.getStatePropertySafe(state, THIRD)) {
			case MIDDLE:
				IBlock.playerWillDestroyCreative(world, player, pos.below());
				break;
			case UPPER:
				IBlock.playerWillDestroyCreative(world, player, pos.below(2));
				break;
		}
		return super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClientSide()) {
			final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
			world.setBlock(pos.above(), defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing).setValue(METAL, true).setValue(THIRD, EnumThird.MIDDLE), 3);
			world.setBlock(pos.above(2), defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing).setValue(METAL, true).setValue(THIRD, EnumThird.UPPER), 3);
			world.blockUpdated(pos, Blocks.AIR);
			state.updateNeighbourShapes(world, pos, 3);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(COLOR);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(METAL);
		builder.add(THIRD);
	}

	protected static IntIntImmutablePair getBounds(BlockState state) {
		final EnumThird third = IBlock.getStatePropertySafe(state, THIRD);
		final int start, end;
		end = switch (third) {
			case LOWER -> {
				start = 10;
				yield 16;
			}
			case UPPER -> {
				start = 0;
				yield 8;
			}
			default -> {
				start = 0;
				yield 16;
			}
		};
		return new IntIntImmutablePair(start, end);
	}

	private static void updateProperties(Level world, BlockPos pos, boolean metalProperty, int colorProperty) {
		world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(COLOR, colorProperty).setValue(METAL, metalProperty));
	}

	public static class BlockEntityTallBase extends BlockEntityBase {

		public BlockEntityTallBase(BlockEntityType<?> type, BlockPos pos, BlockState state, float zOffset, boolean isDoubleSided) {
			super(type, pos, state, 0.21875F, zOffset, isDoubleSided);
		}

		@Override
		public int getColor(BlockState state) {
			return switch (IBlock.getStatePropertySafe(state, BlockStationNameBase.COLOR)) {
				case 1 -> ARGB_LIGHT_GRAY;
				case 2 -> ARGB_BLACK;
				default -> ARGB_WHITE;
			};
		}
	}
}
