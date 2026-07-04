package org.mtr.packet;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.mtr.MTR;

public record CustomPacketS2C(byte[] buffer) implements CustomPacketPayload {

	@Override
	public Type<CustomPacketS2C> type() {
		return MTR.PACKET_IDENTIFIER_S2C;
	}
}
