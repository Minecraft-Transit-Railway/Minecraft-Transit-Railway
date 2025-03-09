package org.mtr.packet;

import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTR;
import org.mtr.block.BlockSignalBase;

public final class PacketUpdateSignalConfig extends PacketHandler {

	private final BlockPos blockPos;
	private final IntAVLTreeSet signalColors;
	private final boolean isBackSide;

	public PacketUpdateSignalConfig(PacketBufferReceiver packetBufferReceiver) {
		blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
		final int signalColorCount = packetBufferReceiver.readInt();
		signalColors = new IntAVLTreeSet();
		for (int i = 0; i < signalColorCount; i++) {
			signalColors.add(packetBufferReceiver.readInt());
		}
		isBackSide = packetBufferReceiver.readBoolean();
	}

	public PacketUpdateSignalConfig(BlockPos blockPos, IntAVLTreeSet signalColors, boolean isBackSide) {
		this.blockPos = blockPos;
		this.signalColors = signalColors;
		this.isBackSide = isBackSide;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(blockPos.asLong());
		packetBufferSender.writeInt(signalColors.size());
		signalColors.forEach(packetBufferSender::writeInt);
		packetBufferSender.writeBoolean(isBackSide);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		if (!MTR.isChunkLoaded(serverPlayerEntity.getEntityWorld(), blockPos)) {
			return;
		}

		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity instanceof BlockSignalBase.BlockEntityBase) {
			((BlockSignalBase.BlockEntityBase) entity).setData(signalColors, isBackSide);
		}
	}
}
