package org.mtr.mod.packet;

import org.mtr.core.operation.GenerateByDepotIds;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.tool.PacketBufferReceiver;

import javax.annotation.Nonnull;

public final class PacketDepotGenerate extends PacketRequestResponseBase {

	public PacketDepotGenerate(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
	}

	public PacketDepotGenerate(GenerateByDepotIds contentObject) {
		super(Utilities.getJsonObjectFromData(contentObject).toString());
	}

	private PacketDepotGenerate(String content) {
		super(content);
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketDepotGenerate(content);
	}

	@Nonnull
	@Override
	protected String getEndpoint() {
		return "operation/generate-by-depot-ids";
	}

	@Override
	protected ResponseType responseType() {
		return ResponseType.NONE;
	}
}
