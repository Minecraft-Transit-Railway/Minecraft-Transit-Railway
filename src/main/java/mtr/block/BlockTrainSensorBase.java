package mtr.block;

import mapper.BlockEntityClientSerializableMapper;
import mapper.BlockEntityProviderMapper;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class BlockTrainSensorBase extends Block implements BlockEntityProviderMapper {

	public BlockTrainSensorBase() {
		super(AbstractBlock.Settings.copy(net.minecraft.block.Blocks.SMOOTH_STONE));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof BlockTrainSensorBase.TileEntityTrainSensorBase) {
				((BlockTrainSensorBase.TileEntityTrainSensorBase) entity).sync();
				PacketTrainDataGuiServer.openTrainSensorScreenS2C((ServerPlayerEntity) player, pos);
			}
		});
	}

	public abstract static class TileEntityTrainSensorBase extends BlockEntityClientSerializableMapper {

		private final Set<Long> filterRouteIds = new HashSet<>();
		private static final String KEY_ROUTE_IDS = "route_ids";

		public TileEntityTrainSensorBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		public void readNbtCompound(NbtCompound nbtCompound) {
			final long[] routeIdsArray = nbtCompound.getLongArray(KEY_ROUTE_IDS);
			for (final long routeId : routeIdsArray) {
				filterRouteIds.add(routeId);
			}
		}

		@Override
		public void writeNbtCompound(NbtCompound nbtCompound) {
			nbtCompound.putLongArray(KEY_ROUTE_IDS, new ArrayList<>(filterRouteIds));
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
			markDirty();
			sync();
		}

		public abstract void setData(Set<Long> filterRouteIds, int number, String string);
	}
}
