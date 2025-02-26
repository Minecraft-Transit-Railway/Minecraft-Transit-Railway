package org.mtr.mod.block;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mod.Blocks;
import org.mtr.mod.Init;
import org.mtr.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public abstract class BlockTrainSensorBase extends BlockExtension implements BlockWithEntity {

	public BlockTrainSensorBase() {
		super(Blocks.createDefaultBlockSettings(true));
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockEntity entity = world.getBlockEntity(pos);
			if (entity != null && entity.data instanceof BlockEntityBase) {
				((BlockEntityBase) entity.data).markDirty2();
				Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(pos));
			}
		});
	}

	public static boolean matchesFilter(World world, BlockPos pos, long routeId, double speed) {
		final BlockEntity entity = world.getBlockEntity(pos);
		return entity != null && entity.data instanceof BlockEntityBase && ((BlockEntityBase) entity.data).matchesFilter(routeId, speed);
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
		public void readCompoundTag(CompoundTag compoundTag) {
			final long[] routeIdsArray = compoundTag.getLongArray(KEY_ROUTE_IDS);
			for (final long routeId : routeIdsArray) {
				filterRouteIds.add(routeId);
			}
			stoppedOnly = compoundTag.getBoolean(KEY_STOPPED_ONLY);
			movingOnly = compoundTag.getBoolean(KEY_MOVING_ONLY);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putLongArray(KEY_ROUTE_IDS, new ArrayList<>(filterRouteIds));
			compoundTag.putBoolean(KEY_STOPPED_ONLY, stoppedOnly);
			compoundTag.putBoolean(KEY_MOVING_ONLY, movingOnly);
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
			this.filterRouteIds.clear();
			this.filterRouteIds.addAll(filterRouteIds);
			this.stoppedOnly = stoppedOnly;
			this.movingOnly = movingOnly;
			markDirty2();
		}
	}
}
