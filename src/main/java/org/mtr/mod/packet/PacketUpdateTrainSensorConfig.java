package org.mtr.mod.packet;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.block.BlockTrainSensorBase;

public class PacketUpdateTrainSensorConfig extends PacketHandler {

	private final BlockPos blockPos;
	private final LongAVLTreeSet filterRouteIds;
	private final boolean stoppedOnly;
	private final boolean movingOnly;
	private final int number;
	private final String[] strings;

	public PacketUpdateTrainSensorConfig(PacketBuffer packetBuffer) {
		blockPos = packetBuffer.readBlockPos();
		final int routeIdCount = packetBuffer.readInt();
		filterRouteIds = new LongAVLTreeSet();
		for (int i = 0; i < routeIdCount; i++) {
			filterRouteIds.add(packetBuffer.readLong());
		}
		stoppedOnly = packetBuffer.readBoolean();
		movingOnly = packetBuffer.readBoolean();
		number = packetBuffer.readInt();
		final int stringCount = packetBuffer.readInt();
		strings = new String[stringCount];
		for (int i = 0; i < stringCount; i++) {
			strings[i] = readString(packetBuffer);
		}
	}

	public PacketUpdateTrainSensorConfig(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly, int number, String[] strings) {
		this.blockPos = blockPos;
		this.filterRouteIds = filterRouteIds;
		this.stoppedOnly = stoppedOnly;
		this.movingOnly = movingOnly;
		this.number = number;
		this.strings = strings;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeBlockPos(blockPos);
		packetBuffer.writeInt(filterRouteIds.size());
		filterRouteIds.forEach(packetBuffer::writeLong);
		packetBuffer.writeBoolean(stoppedOnly);
		packetBuffer.writeBoolean(movingOnly);
		packetBuffer.writeInt(number);
		packetBuffer.writeInt(strings.length);
		for (final String string : strings) {
			packetBuffer.writeString(string);
		}
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity != null && entity.data instanceof BlockTrainSensorBase.BlockEntityBase) {
			((BlockTrainSensorBase.BlockEntityBase) entity.data).setData(filterRouteIds, stoppedOnly, movingOnly, number, strings);
		}
	}
}
