package org.mtr.packet;

import net.minecraft.network.packet.CustomPayload;
import org.mtr.MTR;

public record CustomPacketS2C(byte[] buffer) implements CustomPayload {

	@Override
	public Id<CustomPacketS2C> getId() {
		return MTR.PACKET_IDENTIFIER_S2C;
	}
}
