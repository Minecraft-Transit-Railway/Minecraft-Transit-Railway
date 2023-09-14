package org.mtr.mod.packet;

import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.registry.PacketHandler;

public class PacketOpenLiftCustomizationScreen extends PacketHandler {

	private final long id;

	public PacketOpenLiftCustomizationScreen(PacketBuffer packetBuffer) {
		id = packetBuffer.readLong();
	}

	public PacketOpenLiftCustomizationScreen(long id) {
		this.id = id;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeLong(id);
	}

	@Override
	public void runClientQueued() {
		// TODO
	}
}
