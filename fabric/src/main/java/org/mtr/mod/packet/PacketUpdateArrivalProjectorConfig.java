package org.mtr.mod.packet;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.block.BlockArrivalProjectorBase;

public class PacketUpdateArrivalProjectorConfig extends PacketHandler {

	protected final BlockPos blockPos1;
	protected final LongAVLTreeSet filterPlatformIds;
	protected final int displayPage;

	public PacketUpdateArrivalProjectorConfig(PacketBuffer packetBuffer) {
		blockPos1 = packetBuffer.readBlockPos();
		final int platformIdCount = packetBuffer.readInt();
		filterPlatformIds = new LongAVLTreeSet();
		for (int i = 0; i < platformIdCount; i++) {
			filterPlatformIds.add(packetBuffer.readLong());
		}
		displayPage = packetBuffer.readInt();
	}

	public PacketUpdateArrivalProjectorConfig(BlockPos blockPos1, LongAVLTreeSet filterPlatformIds, int displayPage) {
		this.blockPos1 = blockPos1;
		this.filterPlatformIds = filterPlatformIds;
		this.displayPage = displayPage;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeBlockPos(blockPos1);
		packetBuffer.writeInt(filterPlatformIds.size());
		filterPlatformIds.forEach(packetBuffer::writeLong);
		packetBuffer.writeInt(displayPage);
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final BlockEntity entity1 = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos1);
		if (entity1 != null && entity1.data instanceof BlockArrivalProjectorBase.BlockEntityBase) {
			((BlockArrivalProjectorBase.BlockEntityBase) entity1.data).setData(filterPlatformIds, displayPage);
		}
	}
}
