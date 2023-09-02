package org.mtr.mod.packet;

import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.screen.TicketMachineScreen;

public class PacketOpenTicketMachineScreen extends PacketHandler {

	private final int balance;

	public PacketOpenTicketMachineScreen(PacketBuffer packetBuffer) {
		balance = packetBuffer.readInt();
	}

	public PacketOpenTicketMachineScreen(int balance) {
		this.balance = balance;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeInt(balance);
	}

	@Override
	public void runClientQueued() {
		IPacket.openScreen(new TicketMachineScreen(balance), screenExtension -> screenExtension instanceof TicketMachineScreen);
	}
}
