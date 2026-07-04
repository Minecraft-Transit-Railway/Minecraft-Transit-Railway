package org.mtr.packet;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.mtr.MTR;

public record CustomPacketC2S(byte[] buffer) implements CustomPacketPayload {

	@Override
	public Type<CustomPacketC2S> type() {
		return MTR.PACKET_IDENTIFIER_C2S;
	}
}
