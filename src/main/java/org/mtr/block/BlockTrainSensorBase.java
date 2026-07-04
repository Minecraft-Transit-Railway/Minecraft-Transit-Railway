package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.packet.PacketOpenBlockEntityScreen;

import java.util.ArrayList;

public abstract class BlockTrainSensorBase extends Block implements EntityBlock {

	public BlockTrainSensorBase(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> PacketOpenBlockEntityScreen.sendDirectlyToServer((ServerLevel) world, (ServerPlayer) player, pos));
	}

	public static boolean matchesFilter(Level world, BlockPos pos, long routeId, double speed) {
		final BlockEntity entity = world.getBlockEntity(pos);
		return entity instanceof BlockEntityBase && ((BlockEntityBase) entity).matchesFilter(routeId, speed);
	}

	public abstract static class BlockEntityBase extends BlockEntityExtension {

		private boolean stoppedOnly;
		private boolean movingOnly;
		private final LongAVLTreeSet filterRouteIds = new LongAVLTreeSet();
		private static final String KEY_ROUTE_IDS = "route_ids";
		private static final String KEY_STOPPED_ONLY = "stopped_only";
		private static final String KEY_MOVING_ONLY = "moving_only";

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		protected void readNbt(CompoundTag nbtCompound) {
			final long[] routeIdsArray = nbtCompound.getLongArray(KEY_ROUTE_IDS);
			for (final long routeId : routeIdsArray) {
				filterRouteIds.add(routeId);
			}
			stoppedOnly = nbtCompound.getBoolean(KEY_STOPPED_ONLY);
			movingOnly = nbtCompound.getBoolean(KEY_MOVING_ONLY);
		}

		@Override
		protected void writeNbt(CompoundTag nbtCompound) {
			nbtCompound.putLongArray(KEY_ROUTE_IDS, new ArrayList<>(filterRouteIds));
			nbtCompound.putBoolean(KEY_STOPPED_ONLY, stoppedOnly);
			nbtCompound.putBoolean(KEY_MOVING_ONLY, movingOnly);
		}

		public boolean matchesFilter(long routeId, double speed) {
			if (!filterRouteIds.isEmpty() && !filterRouteIds.contains(routeId)) {
				return false;
			} else {
				return speed < 0 || !stoppedOnly && !movingOnly || stoppedOnly && speed == 0 || movingOnly && speed > 0;
			}
		}

		public LongAVLTreeSet getRouteIds() {
			return filterRouteIds;
		}

		public boolean getStoppedOnly() {
			return stoppedOnly;
		}

		public boolean getMovingOnly() {
			return movingOnly;
		}

		public void setData(LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly) {
			final LongAVLTreeSet filterRouteIdsCopy = new LongAVLTreeSet(filterRouteIds);
			this.filterRouteIds.clear();
			this.filterRouteIds.addAll(filterRouteIdsCopy);
			this.stoppedOnly = stoppedOnly;
			this.movingOnly = movingOnly;
			setChanged();
		}
	}
}
