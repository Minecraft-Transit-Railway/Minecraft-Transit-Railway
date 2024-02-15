package org.mtr.mod.packet;

import org.mtr.core.operation.PressLift;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.tool.PacketBufferReceiver;

import javax.annotation.Nonnull;

public final class PacketPressLiftButton extends PacketRequestResponseBase {

	public PacketPressLiftButton(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
	}

	public PacketPressLiftButton(PressLift contentObject) {
		super(Utilities.getJsonObjectFromData(contentObject).toString());
	}

	private PacketPressLiftButton(String content) {
		super(content);
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketPressLiftButton(content);
	}

	@Nonnull
	@Override
	protected String getEndpoint() {
		return "operation/press-lift";
	}

	@Override
	protected PacketRequestResponseBase.ResponseType responseType() {
		return PacketRequestResponseBase.ResponseType.NONE;
	}
}
