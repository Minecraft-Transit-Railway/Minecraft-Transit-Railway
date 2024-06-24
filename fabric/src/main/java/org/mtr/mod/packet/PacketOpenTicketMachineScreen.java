package org.mtr.mod.packet;

import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;

public final class PacketOpenTicketMachineScreen extends PacketHandler {

	private final int balance;

	public PacketOpenTicketMachineScreen(PacketBufferReceiver packetBufferReceiver) {
		balance = packetBufferReceiver.readInt();
	}

	public PacketOpenTicketMachineScreen(int balance) {
		this.balance = balance;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeInt(balance);
	}

	@Override
	public void runClient() {
		ClientPacketHelper.openTicketMachineScreen(balance);
	}
}
