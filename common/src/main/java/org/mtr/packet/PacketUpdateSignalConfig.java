package org.mtr.packet;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.block.BlockSignalBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntAVLTreeSet;

public final class PacketUpdateSignalConfig extends BlockEntityPacketHandler {

	private final BlockPos blockPos;
	private final boolean acceptRedstone;
	private final boolean outputRedstone;
	private final IntAVLTreeSet signalColors;
	private final boolean isBackSide;

	public PacketUpdateSignalConfig(PacketBufferReceiver packetBufferReceiver) {
		blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
		acceptRedstone = packetBufferReceiver.readBoolean();
		outputRedstone = packetBufferReceiver.readBoolean();
		final int signalColorCount = packetBufferReceiver.readInt();
		signalColors = new IntAVLTreeSet();
		for (int i = 0; i < signalColorCount; i++) {
			signalColors.add(packetBufferReceiver.readInt());
		}
		isBackSide = packetBufferReceiver.readBoolean();
	}

	public PacketUpdateSignalConfig(BlockPos blockPos, boolean acceptRedstone, boolean outputRedstone, IntAVLTreeSet signalColors, boolean isBackSide) {
		this.blockPos = blockPos;
		this.acceptRedstone = acceptRedstone;
		this.outputRedstone = outputRedstone;
		this.signalColors = signalColors;
		this.isBackSide = isBackSide;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(blockPos.asLong());
		packetBufferSender.writeBoolean(acceptRedstone);
		packetBufferSender.writeBoolean(outputRedstone);
		packetBufferSender.writeInt(signalColors.size());
		signalColors.forEach(packetBufferSender::writeInt);
		packetBufferSender.writeBoolean(isBackSide);
	}

	@Override
	protected void setData(@Nullable World world) {
		if (world == null || !MTR.isChunkLoaded(world, blockPos)) {
			return;
		}

		final BlockEntity entity = world.getBlockEntity(blockPos);
		if (entity instanceof BlockSignalBase.BlockEntityBase) {
			((BlockSignalBase.BlockEntityBase) entity).setData(acceptRedstone, outputRedstone, signalColors, isBackSide);
		}
	}
}
