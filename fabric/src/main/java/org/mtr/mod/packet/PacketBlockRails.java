package org.mtr.mod.packet;

import org.mtr.core.operation.BlockRails;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.tool.PacketBufferReceiver;

import javax.annotation.Nonnull;

public final class PacketBlockRails extends PacketRequestResponseBase {

	public PacketBlockRails(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
	}

	public PacketBlockRails(BlockRails contentObject) {
		super(Utilities.getJsonObjectFromData(contentObject).toString());
	}

	private PacketBlockRails(String content) {
		super(content);
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketBlockRails(content);
	}

	@Override
	protected SerializedDataBase getDataInstance(JsonReader jsonReader) {
		return new BlockRails(jsonReader);
	}

	@Nonnull
	@Override
	protected String getKey() {
		return OperationProcessor.BLOCK_RAILS;
	}

	@Override
	protected ResponseType responseType() {
		return ResponseType.NONE;
	}
}
