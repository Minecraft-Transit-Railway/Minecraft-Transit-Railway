package org.mtr.mod.packet;

import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.screen.ResourcePackCreatorScreen;

public class PacketOpenResourcePackCreatorScreen extends PacketHandler {

	public PacketOpenResourcePackCreatorScreen() {
	}

	public PacketOpenResourcePackCreatorScreen(PacketBuffer packetBuffer) {
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
	}

	@Override
	public void runClientQueued() {
		IPacket.openScreen(new ResourcePackCreatorScreen(), screenExtension -> screenExtension instanceof ResourcePackCreatorScreen);
	}
}
