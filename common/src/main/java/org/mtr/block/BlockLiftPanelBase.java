package org.mtr.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.registry.Items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockLiftPanelBase extends Block implements IBlock, TripleHorizontalBlock, BlockEntityProvider {

	private final boolean isOdd;
	private final boolean isFlat;

	public BlockLiftPanelBase(AbstractBlock.Settings settings, boolean isOdd, boolean isFlat) {
		super(settings.luminance(blockState -> 5));
		this.isOdd = isOdd;
		this.isFlat = isFlat;
	}

	@Nonnull
	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (isOdd) {
			return TripleHorizontalBlock.getStateForNeighborUpdate(state, direction, neighborState.isOf(this), super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random));
		} else {
			if (IBlock.getSideDirection(state) == direction && !neighborState.isOf(this)) {
				return Blocks.AIR.getDefaultState();
			} else {
				return state;
			}
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final Direction direction = ctx.getHorizontalPlayerFacing();
		if (isOdd) {
			return TripleHorizontalBlock.getPlacementState(ctx, getDefaultState());
		} else {
			return IBlock.isReplaceable(ctx, direction.rotateYClockwise(), 2) ? getDefaultState().with(Properties.HORIZONTAL_FACING, direction).with(SIDE, EnumSide.LEFT) : null;
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, isFlat ? 1 : 4, state.get(Properties.HORIZONTAL_FACING));
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient()) {
			final Direction direction = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);

			if (isOdd) {
				TripleHorizontalBlock.onPlaced(world, pos, state, getDefaultState());
			} else {
				world.setBlockState(pos.offset(direction.rotateYClockwise()), getDefaultState().with(Properties.HORIZONTAL_FACING, direction).with(SIDE, EnumSide.RIGHT), 3);
			}

			world.updateNeighbors(pos, Blocks.AIR);
			state.updateNeighbors(world, pos, 3);
		}
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (isOdd) {
			TripleHorizontalBlock.onBreak(world, pos, state, player);
		} else {
			if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
				IBlock.onBreakCreative(world, player, pos.offset(IBlock.getSideDirection(state)));
			}
		}
		return super.onBreak(world, pos, state, player);
	}

	@Nonnull
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (world.isClient()) {
			return ActionResult.SUCCESS;
		} else {
			return player.isHolding(Items.LIFT_BUTTONS_LINK_CONNECTOR.get()) || player.isHolding(Items.LIFT_BUTTONS_LINK_REMOVER.get()) ? ActionResult.PASS : ActionResult.FAIL;
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
		tooltip.add((isOdd ? TranslationProvider.TOOLTIP_MTR_RAILWAY_SIGN_ODD : TranslationProvider.TOOLTIP_MTR_RAILWAY_SIGN_EVEN).getMutableText().formatted(Formatting.GRAY));
	}

	public abstract static class BlockEntityBase extends BlockEntityExtension {

		private BlockPos trackPosition = null;
		private static final String KEY_TRACK_FLOOR_POS = "track_floor_pos";

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state, boolean isOdd) {
			super(type, pos, state);
		}

		@Override
		protected void readNbt(NbtCompound nbtCompound) {
			final long data = nbtCompound.getLong(KEY_TRACK_FLOOR_POS);
			trackPosition = data == 0 ? null : BlockPos.fromLong(data);
		}

		@Override
		protected void writeNbt(NbtCompound nbtCompound) {
			nbtCompound.putLong(KEY_TRACK_FLOOR_POS, trackPosition == null ? 0 : trackPosition.asLong());
		}

		public void registerFloor(World world, BlockPos pos, boolean isAdd) {
			if (IBlock.getStatePropertySafe(world, getPos(), SIDE) == EnumSide.RIGHT) {
				final BlockEntity blockEntity = world.getBlockEntity(getPos().offset(IBlock.getStatePropertySafe(world, getPos(), Properties.HORIZONTAL_FACING).rotateYCounterclockwise()));
				if (blockEntity instanceof BlockEntityBase) {
					((BlockEntityBase) blockEntity).registerFloor(world, pos, isAdd);
				}
			} else {
				if (isAdd) {
					trackPosition = pos;
				} else {
					trackPosition = null;
				}
				markDirty();
			}
		}

		@Nullable
		public BlockPos getTrackPosition() {
			return trackPosition;
		}
	}
}
