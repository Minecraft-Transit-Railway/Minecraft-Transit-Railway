package org.mtr.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;

public record CustomPacket(Id<CustomPacket> id, PacketByteBuf packetByteBuf) implements CustomPayload {

	@Override
	public Id<CustomPacket> getId() {
		return id;
	}

	public static void encode(CustomPacket customPacket, PacketByteBuf packetByteBuf) {
		customPacket.packetByteBuf.writeBytes(packetByteBuf);
	}
}
