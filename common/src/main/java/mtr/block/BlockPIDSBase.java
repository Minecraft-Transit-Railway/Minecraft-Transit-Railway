package mtr.block;

import mtr.mappings.BlockDirectionalMapper;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class BlockPIDSBase extends BlockDirectionalMapper implements EntityBlockMapper {

	public BlockPIDSBase() {
		super(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2).lightLevel(state -> 5));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockPos otherPos = pos.relative(IBlock.getStatePropertySafe(state, FACING));
			final BlockEntity entity1 = world.getBlockEntity(pos);
			final BlockEntity entity2 = world.getBlockEntity(otherPos);

			if (entity1 instanceof TileEntityBlockPIDSBase && entity2 instanceof TileEntityBlockPIDSBase) {
				((TileEntityBlockPIDSBase) entity1).syncData();
				((TileEntityBlockPIDSBase) entity2).syncData();
				PacketTrainDataGuiServer.openPIDSConfigScreenS2C((ServerPlayer) player, pos, otherPos, ((TileEntityBlockPIDSBase) entity1).getMaxArrivals());
			}
		});
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (IBlock.getStatePropertySafe(state, FACING) == direction && !newState.is(this)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return state;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final Direction direction = ctx.getHorizontalDirection().getOpposite();
		return IBlock.isReplaceable(ctx, direction, 2) ? defaultBlockState().setValue(FACING, direction) : null;
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (facing == Direction.SOUTH || facing == Direction.WEST) {
			IBlock.onBreakCreative(world, player, pos.relative(facing));
		}
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (!world.isClientSide) {
			final Direction direction = IBlock.getStatePropertySafe(state, FACING);
			world.setBlock(pos.relative(direction), defaultBlockState().setValue(FACING, direction.getOpposite()), 3);
			world.updateNeighborsAt(pos, Blocks.AIR);
			state.updateNeighbourShapes(world, pos, 3);
			final BlockEntity entity1 = world.getBlockEntity(pos);
			final BlockEntity entity2 = world.getBlockEntity(pos.relative(direction));
			if (entity1 instanceof TileEntityBlockPIDSBase && entity2 instanceof TileEntityBlockPIDSBase) {
				System.arraycopy(((TileEntityBlockPIDSBase) entity1).messages, 0, ((TileEntityBlockPIDSBase) entity2).messages, 0, Math.min(((TileEntityBlockPIDSBase) entity1).messages.length, ((TileEntityBlockPIDSBase) entity2).messages.length));
			}
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState blockState) {
		return PushReaction.BLOCK;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public abstract static class TileEntityBlockPIDSBase extends BlockEntityClientSerializableMapper {

		private final String[] messages = new String[getMaxArrivals()];
		private final boolean[] hideArrival = new boolean[getMaxArrivals()];
		private final Set<Long> platformIds = new HashSet<>();
		private static final String KEY_MESSAGE = "message";
		private static final String KEY_HIDE_ARRIVAL = "hide_arrival";
		private static final String KEY_PLATFORM_IDS = "platform_ids";

		public TileEntityBlockPIDSBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			for (int i = 0; i < getMaxArrivals(); i++) {
				messages[i] = compoundTag.getString(KEY_MESSAGE + i);
				hideArrival[i] = compoundTag.getBoolean(KEY_HIDE_ARRIVAL + i);
			}
			platformIds.clear();
			final long[] platformIdsArray = compoundTag.getLongArray(KEY_PLATFORM_IDS);
			for (final long platformId : platformIdsArray) {
				platformIds.add(platformId);
			}
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			for (int i = 0; i < getMaxArrivals(); i++) {
				compoundTag.putString(KEY_MESSAGE + i, messages[i] == null ? "" : messages[i]);
				compoundTag.putBoolean(KEY_HIDE_ARRIVAL + i, hideArrival[i]);
			}
			compoundTag.putLongArray(KEY_PLATFORM_IDS, new ArrayList<>(platformIds));
		}

		public AABB getRenderBoundingBox() {
			return new AABB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		}

		public void setData(String[] messages, boolean[] hideArrival, Set<Long> platformIds) {
			System.arraycopy(messages, 0, this.messages, 0, Math.min(messages.length, this.messages.length));
			System.arraycopy(hideArrival, 0, this.hideArrival, 0, Math.min(hideArrival.length, this.hideArrival.length));
			this.platformIds.clear();
			this.platformIds.addAll(platformIds);
			setChanged();
			syncData();
		}

		public Set<Long> getPlatformIds() {
			return platformIds;
		}

		public String getMessage(int index) {
			if (index >= 0 && index < getMaxArrivals()) {
				if (messages[index] == null) {
					messages[index] = "";
				}
				return messages[index];
			} else {
				return "";
			}
		}

		public boolean getHideArrival(int index) {
			if (index >= 0 && index < getMaxArrivals()) {
				return hideArrival[index];
			} else {
				return false;
			}
		}

		protected abstract int getMaxArrivals();
	}
}