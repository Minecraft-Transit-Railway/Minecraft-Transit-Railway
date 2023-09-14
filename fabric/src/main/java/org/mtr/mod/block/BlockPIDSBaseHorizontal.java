package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.registry.Registry;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.packet.PacketOpenPIDSConfigScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockPIDSBaseHorizontal extends BlockExtension implements DirectionHelper, BlockWithEntity {

	public BlockPIDSBaseHorizontal() {
		super(BlockHelper.createBlockSettings(true, blockState -> 5));
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockPos otherPos = pos.offset(IBlock.getStatePropertySafe(state, FACING));
			final BlockEntity entity1 = world.getBlockEntity(pos);
			final BlockEntity entity2 = world.getBlockEntity(otherPos);

			if (entity1 != null && entity2 != null && entity1.data instanceof BlockEntityHorizontalBase && entity2.data instanceof BlockEntityHorizontalBase) {
				((BlockEntityHorizontalBase) entity1.data).markDirty2();
				((BlockEntityHorizontalBase) entity2.data).markDirty2();
				Registry.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenPIDSConfigScreen(pos, otherPos, ((BlockEntityHorizontalBase) entity1.data).getMaxArrivals(), ((BlockEntityHorizontalBase) entity1.data).getLinesPerArrival()));
			}
		});
	}

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (IBlock.getStatePropertySafe(state, FACING) == direction && !neighborState.isOf(new Block(this))) {
			return Blocks.getAirMapped().getDefaultState();
		} else {
			return state;
		}
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		final Direction direction = ctx.getPlayerFacing().getOpposite();
		return IBlock.isReplaceable(ctx, direction, 2) ? getDefaultState2().with(new Property<>(FACING.data), direction.data) : null;
	}

	@Override
	public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (facing == Direction.SOUTH || facing == Direction.WEST) {
			IBlock.onBreakCreative(world, player, pos.offset(facing));
		}
		super.onBreak2(world, pos, state, player);
	}

	@Override
	public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient()) {
			final Direction direction = IBlock.getStatePropertySafe(state, FACING);
			world.setBlockState(pos.offset(direction), getDefaultState2().with(new Property<>(FACING.data), direction.getOpposite().data), 3);
			world.updateNeighbors(pos, Blocks.getAirMapped());
			state.updateNeighbors(new WorldAccess(world.data), pos, 3);
			final BlockEntity entity1 = world.getBlockEntity(pos);
			final BlockEntity entity2 = world.getBlockEntity(pos.offset(direction));
			if (entity1 != null && entity2 != null && entity1.data instanceof BlockEntityHorizontalBase && entity2.data instanceof BlockEntityHorizontalBase) {
				System.arraycopy(((BlockEntityHorizontalBase) entity1.data).messages, 0, ((BlockEntityHorizontalBase) entity2.data).messages, 0, Math.min(((BlockEntityHorizontalBase) entity1.data).messages.length, ((BlockEntityHorizontalBase) entity2.data).messages.length));
			}
		}
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
		final BlockEntityExtension blockEntity = createBlockEntity(new BlockPos(0, 0, 0), Blocks.getAirMapped().getDefaultState());
		if (blockEntity instanceof BlockEntityPIDS) {
			tooltip.add(TextHelper.translatable("tooltip.mtr.arrivals", ((BlockEntityPIDS) blockEntity).getMaxArrivals()).formatted(TextFormatting.GRAY));
		}
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
	}

	public abstract static class BlockEntityHorizontalBase extends BlockEntityPIDS {

		public BlockEntityHorizontalBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		public int getLinesPerArrival() {
			return 1;
		}
	}
}
