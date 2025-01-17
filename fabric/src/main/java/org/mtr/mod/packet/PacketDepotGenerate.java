package org.mtr.mod.packet;

import org.mtr.core.operation.DepotOperationByIds;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.tool.PacketBufferReceiver;

import javax.annotation.Nonnull;

public final class PacketDepotGenerate extends PacketRequestResponseBase {

	public PacketDepotGenerate(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
	}

	public PacketDepotGenerate(DepotOperationByIds contentObject) {
		super(Utilities.getJsonObjectFromData(contentObject).toString());
	}

	private PacketDepotGenerate(String content) {
		super(content);
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketDepotGenerate(content);
	}

	@Override
	protected SerializedDataBase getDataInstance(JsonReader jsonReader) {
		return new DepotOperationByIds(jsonReader);
	}

	@Nonnull
	@Override
	protected String getKey() {
		return OperationProcessor.GENERATE_BY_DEPOT_IDS;
	}

	@Override
	protected ResponseType responseType() {
		return ResponseType.NONE;
	}
}
