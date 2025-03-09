package org.mtr.packet;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public final class PacketDriveTrain extends PacketHandler {

	private final boolean pressingAccelerate;
	private final boolean pressingBrake;
	private final boolean pressingDoors;

	public PacketDriveTrain(PacketBufferReceiver packetBufferReceiver) {
		pressingAccelerate = packetBufferReceiver.readBoolean();
		pressingBrake = packetBufferReceiver.readBoolean();
		pressingDoors = packetBufferReceiver.readBoolean();
	}

	public PacketDriveTrain(boolean pressingAccelerate, boolean pressingBrake, boolean pressingDoors) {
		this.pressingAccelerate = pressingAccelerate;
		this.pressingBrake = pressingBrake;
		this.pressingDoors = pressingDoors;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeBoolean(pressingAccelerate);
		packetBufferSender.writeBoolean(pressingBrake);
		packetBufferSender.writeBoolean(pressingDoors);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		// TODO
	}
}
