package org.mtr.packet;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTR;
import org.mtr.block.BlockEyeCandy;

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
		if (!MTR.isChunkLoaded(serverPlayerEntity.getEntityWorld(), blockPos)) {
			return;
		}

		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity instanceof BlockEyeCandy.EyeCandyBlockEntity) {
			((BlockEyeCandy.EyeCandyBlockEntity) entity).setData(modelId, translateX, translateY, translateZ, rotateX, rotateY, rotateZ, fullLight);
		}
	}
}
