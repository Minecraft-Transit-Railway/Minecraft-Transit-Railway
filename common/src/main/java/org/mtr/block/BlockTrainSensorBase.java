package org.mtr.block;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mtr.packet.PacketOpenBlockEntityScreen;
import org.mtr.registry.Registry;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public abstract class BlockTrainSensorBase extends Block implements BlockEntityProvider {

	public BlockTrainSensorBase(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof BlockEntityBase) {
				entity.markDirty();
				Registry.sendPacketToClient((ServerPlayerEntity) player, new PacketOpenBlockEntityScreen(pos));
			}
		});
	}

	public static boolean matchesFilter(World world, BlockPos pos, long routeId, double speed) {
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
		protected void readNbt(NbtCompound nbtCompound) {
			final long[] routeIdsArray = nbtCompound.getLongArray(KEY_ROUTE_IDS);
			for (final long routeId : routeIdsArray) {
				filterRouteIds.add(routeId);
			}
			stoppedOnly = nbtCompound.getBoolean(KEY_STOPPED_ONLY);
			movingOnly = nbtCompound.getBoolean(KEY_MOVING_ONLY);
		}

		@Override
		protected void writeNbt(NbtCompound nbtCompound) {
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
			this.filterRouteIds.clear();
			this.filterRouteIds.addAll(filterRouteIds);
			this.stoppedOnly = stoppedOnly;
			this.movingOnly = movingOnly;
			markDirty();
		}
	}
}
