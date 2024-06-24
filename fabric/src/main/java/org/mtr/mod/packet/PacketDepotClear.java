package org.mtr.mod.packet;

import org.mtr.core.operation.GenerateOrClearByDepotIds;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.tool.PacketBufferReceiver;

import javax.annotation.Nonnull;

public final class PacketDepotClear extends PacketRequestResponseBase {

	public PacketDepotClear(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
	}

	public PacketDepotClear(GenerateOrClearByDepotIds contentObject) {
		super(Utilities.getJsonObjectFromData(contentObject).toString());
	}

	private PacketDepotClear(String content) {
		super(content);
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketDepotClear(content);
	}

	@Nonnull
	@Override
	protected String getEndpoint() {
		return "operation/clear-by-depot-ids";
	}

	@Override
	protected ResponseType responseType() {
		return ResponseType.NONE;
	}
}
