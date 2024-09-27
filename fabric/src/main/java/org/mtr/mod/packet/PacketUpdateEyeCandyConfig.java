package org.mtr.mod.packet;

import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockEyeCandy;

import javax.annotation.Nullable;

public final class PacketUpdateEyeCandyConfig extends PacketHandler {

	private final BlockPos blockPos;
	private final String modelId;
	private final float translateX, translateY, translateZ;
	private final float rotateX, rotateY, rotateZ;
	private final boolean fullLight;

	public PacketUpdateEyeCandyConfig(PacketBufferReceiver packetBufferReceiver) {
		blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
		modelId = packetBufferReceiver.readString();
		translateX = packetBufferReceiver.readFloat();
		translateY = packetBufferReceiver.readFloat();
		translateZ = packetBufferReceiver.readFloat();
		rotateX = packetBufferReceiver.readFloat();
		rotateY = packetBufferReceiver.readFloat();
		rotateZ = packetBufferReceiver.readFloat();
		fullLight = packetBufferReceiver.readBoolean();
	}

	public PacketUpdateEyeCandyConfig(BlockPos blockPos, @Nullable String modelId, float translateX, float translateY, float translateZ, float rotateX, float rotateY, float rotateZ, boolean fullLight) {
		this.blockPos = blockPos;
		this.modelId = modelId == null ? "" : modelId;
		this.translateX = translateX;
		this.translateY = translateY;
		this.translateZ = translateZ;
		this.rotateX = rotateX;
		this.rotateY = rotateY;
		this.rotateZ = rotateZ;
		this.fullLight = fullLight;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(blockPos.asLong());
		packetBufferSender.writeString(modelId);
		packetBufferSender.writeFloat(translateX);
		packetBufferSender.writeFloat(translateY);
		packetBufferSender.writeFloat(translateZ);
		packetBufferSender.writeFloat(rotateX);
		packetBufferSender.writeFloat(rotateY);
		packetBufferSender.writeFloat(rotateZ);
		packetBufferSender.writeBoolean(fullLight);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		if (!Init.isChunkLoaded(serverPlayerEntity.getEntityWorld(), blockPos)) {
			return;
		}

		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity != null && entity.data instanceof BlockEyeCandy.BlockEntity) {
			((BlockEyeCandy.BlockEntity) entity.data).setData(modelId, translateX, translateY, translateZ, rotateX, rotateY, rotateZ, fullLight);
		}
	}
}
