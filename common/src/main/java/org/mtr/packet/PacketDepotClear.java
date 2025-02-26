package org.mtr.mod.packet;

import org.mtr.core.operation.DepotOperationByIds;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.tool.PacketBufferReceiver;

import javax.annotation.Nonnull;

public final class PacketDepotClear extends PacketRequestResponseBase {

	public PacketDepotClear(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
	}

	public PacketDepotClear(DepotOperationByIds contentObject) {
		super(Utilities.getJsonObjectFromData(contentObject).toString());
	}

	private PacketDepotClear(String content) {
		super(content);
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketDepotClear(content);
	}

	@Override
	protected SerializedDataBase getDataInstance(JsonReader jsonReader) {
		return new DepotOperationByIds(jsonReader);
	}

	@Nonnull
	@Override
	protected String getKey() {
		return OperationProcessor.CLEAR_BY_DEPOT_IDS;
	}

	@Override
	protected ResponseType responseType() {
		return ResponseType.NONE;
	}
}
