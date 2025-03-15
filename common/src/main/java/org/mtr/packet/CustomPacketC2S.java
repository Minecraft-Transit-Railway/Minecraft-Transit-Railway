package org.mtr.packet;

import net.minecraft.network.packet.CustomPayload;
import org.mtr.MTR;

public record CustomPacketC2S(byte[] buffer) implements CustomPayload {

	@Override
	public Id<CustomPacketC2S> getId() {
		return MTR.PACKET_IDENTIFIER_C2S;
	}
}
