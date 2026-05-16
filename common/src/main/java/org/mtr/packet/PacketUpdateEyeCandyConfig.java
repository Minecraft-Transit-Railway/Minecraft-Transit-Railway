package org.mtr.packet;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.block.BlockEyeCandy;

public final class PacketUpdateEyeCandyConfig extends BlockEntityPacketHandler {

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

	protected void setData(@Nullable World world) {
		if (world == null || !MTR.isChunkLoaded(world, blockPos)) {
			return;
		}

		final BlockEntity entity = world.getBlockEntity(blockPos);
		if (entity instanceof BlockEyeCandy.EyeCandyBlockEntity) {
			((BlockEyeCandy.EyeCandyBlockEntity) entity).setData(modelId, translateX, translateY, translateZ, rotateX, rotateY, rotateZ, fullLight);
		}
	}
}
