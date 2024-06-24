package org.mtr.mod.packet;

import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;

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
