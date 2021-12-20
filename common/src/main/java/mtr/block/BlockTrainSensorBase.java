package mtr.block;

import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class BlockTrainSensorBase extends Block implements EntityBlockMapper {

	public BlockTrainSensorBase() {
		super(BlockBehaviour.Properties.copy(Blocks.SMOOTH_STONE));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof BlockTrainSensorBase.TileEntityTrainSensorBase) {
				((BlockTrainSensorBase.TileEntityTrainSensorBase) entity).syncData();
				PacketTrainDataGuiServer.openTrainSensorScreenS2C((ServerPlayer) player, pos);
			}
		});
	}

	public static boolean matchesFilter(Level world, BlockPos pos, long routeId) {
		final BlockEntity entity = world.getBlockEntity(pos);
		return entity instanceof TileEntityTrainSensorBase && ((TileEntityTrainSensorBase) entity).matchesFilter(routeId);
	}

	public abstract static class TileEntityTrainSensorBase extends BlockEntityClientSerializableMapper {

		private final Set<Long> filterRouteIds = new HashSet<>();
		private static final String KEY_ROUTE_IDS = "route_ids";

		public TileEntityTrainSensorBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			final long[] routeIdsArray = compoundTag.getLongArray(KEY_ROUTE_IDS);
			for (final long routeId : routeIdsArray) {
				filterRouteIds.add(routeId);
			}
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putLongArray(KEY_ROUTE_IDS, new ArrayList<>(filterRouteIds));
		}

		public Set<Long> getRouteIds() {
			return filterRouteIds;
		}

		public boolean matchesFilter(long routeId) {
			return filterRouteIds.isEmpty() || filterRouteIds.contains(routeId);
		}

		protected void setData(Set<Long> filterRouteIds) {
			this.filterRouteIds.clear();
			this.filterRouteIds.addAll(filterRouteIds);
			setChanged();
			syncData();
		}

		public abstract void setData(Set<Long> filterRouteIds, int number, String string);
	}
}
