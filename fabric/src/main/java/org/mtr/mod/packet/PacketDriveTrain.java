package org.mtr.mod.packet;

import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;

public final class PacketDriveTrain extends PacketHandler {

	private final boolean pressingAccelerate;
	private final boolean pressingBrake;
	private final boolean pressingDoors;

	public PacketDriveTrain(PacketBuffer packetBuffer) {
		pressingAccelerate = packetBuffer.readBoolean();
		pressingBrake = packetBuffer.readBoolean();
		pressingDoors = packetBuffer.readBoolean();
	}

	public PacketDriveTrain(boolean pressingAccelerate, boolean pressingBrake, boolean pressingDoors) {
		this.pressingAccelerate = pressingAccelerate;
		this.pressingBrake = pressingBrake;
		this.pressingDoors = pressingDoors;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeBoolean(pressingAccelerate);
		packetBuffer.writeBoolean(pressingBrake);
		packetBuffer.writeBoolean(pressingDoors);
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		// TODO
	}
}
