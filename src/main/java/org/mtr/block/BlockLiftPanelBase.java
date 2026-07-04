package org.mtr.block;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.registry.Items;

import java.util.List;

//? if >= 1.21.4 {
import net.minecraft.world.level.ScheduledTickAccess;
//? } else {
/*import net.minecraft.world.level.LevelAccessor;
 *///? }

public abstract class BlockLiftPanelBase extends Block implements IBlock, TripleHorizontalBlock, EntityBlock {

	private final boolean isOdd;
	private final boolean isFlat;

	public BlockLiftPanelBase(BlockBehaviour.Properties settings, boolean isOdd, boolean isFlat) {
		super(settings.lightLevel(blockState -> 5));
		this.isOdd = isOdd;
		this.isFlat = isFlat;
	}

	@Override
//? if >= 1.21.4 {
	protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
//? } else {
	/*protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
//
*///? }
		if (isOdd) {
//? if >= 1.21.4 {
			return TripleHorizontalBlock.updateShape(state, direction, neighborState.is(this), super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random));
//? } else {
			/*return TripleHorizontalBlock.updateShape(state, direction, neighborState.is(this), super.updateShape(state, direction, neighborState, world, pos, neighborPos));
//
*///? }
		} else {
			if (IBlock.getSideDirection(state) == direction && !neighborState.is(this)) {
				return Blocks.AIR.defaultBlockState();
			} else {
				return state;
			}
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final Direction direction = ctx.getHorizontalDirection();
		if (isOdd) {
			return TripleHorizontalBlock.getStateForPlacement(ctx, defaultBlockState());
		} else {
			return IBlock.isReplaceable(ctx, direction.getClockWise(), 2) ? defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction).setValue(SIDE, EnumSide.LEFT) : null;
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, isFlat ? 1 : 4, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClientSide()) {
			final Direction direction = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);

			if (isOdd) {
				TripleHorizontalBlock.setPlacedBy(world, pos, state, defaultBlockState());
			} else {
				world.setBlock(pos.relative(direction.getClockWise()), defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction).setValue(SIDE, EnumSide.RIGHT), 3);
			}

			world.blockUpdated(pos, Blocks.AIR);
			state.updateNeighbourShapes(world, pos, 3);
		}
	}

	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (isOdd) {
			TripleHorizontalBlock.playerWillDestroy(world, pos, state, player);
		} else {
			if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
				IBlock.playerWillDestroyCreative(world, player, pos.relative(IBlock.getSideDirection(state)));
			}
		}
		return super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (world.isClientSide()) {
			return InteractionResult.SUCCESS;
		} else {
			return player.isHolding(Items.LIFT_BUTTONS_LINK_CONNECTOR.get()) || player.isHolding(Items.LIFT_BUTTONS_LINK_REMOVER.get()) ? InteractionResult.PASS : InteractionResult.FAIL;
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
		tooltip.add((isOdd ? TranslationProvider.TOOLTIP_MTR_RAILWAY_SIGN_ODD : TranslationProvider.TOOLTIP_MTR_RAILWAY_SIGN_EVEN).getMutableText().withStyle(ChatFormatting.GRAY));
	}

	public abstract static class BlockEntityBase extends BlockEntityExtension {

		@Getter
		@Nullable
		private BlockPos trackPosition = null;
		private static final String KEY_TRACK_FLOOR_POS = "track_floor_pos";

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state, boolean isOdd) {
			super(type, pos, state);
		}

		@Override
		protected void readNbt(CompoundTag nbtCompound) {
			final long data = nbtCompound.getLong(KEY_TRACK_FLOOR_POS);
			trackPosition = data == 0 ? null : BlockPos.of(data);
		}

		@Override
		protected void writeNbt(CompoundTag nbtCompound) {
			nbtCompound.putLong(KEY_TRACK_FLOOR_POS, trackPosition == null ? 0 : trackPosition.asLong());
		}

		public void registerFloor(Level world, BlockPos pos, boolean isAdd) {
			if (IBlock.getStatePropertySafe(world, getBlockPos(), SIDE) == EnumSide.RIGHT) {
				final BlockEntity blockEntity = world.getBlockEntity(getBlockPos().relative(IBlock.getStatePropertySafe(world, getBlockPos(), BlockStateProperties.HORIZONTAL_FACING).getCounterClockWise()));
				if (blockEntity instanceof BlockEntityBase) {
					((BlockEntityBase) blockEntity).registerFloor(world, pos, isAdd);
				}
			} else {
				if (isAdd) {
					trackPosition = pos;
				} else {
					trackPosition = null;
				}
				setChanged();
			}
		}
	}
}
