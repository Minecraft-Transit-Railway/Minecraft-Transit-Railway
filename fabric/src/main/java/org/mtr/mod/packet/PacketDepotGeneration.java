package org.mtr.mod.packet;

import org.mtr.core.operation.DepotGenerationResponse;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.client.MinecraftClientData;

public final class PacketDepotGeneration extends PacketHandler {

	private final DepotGenerationResponse depotGenerationResponse;

	public PacketDepotGeneration(PacketBufferReceiver packetBufferReceiver) {
		depotGenerationResponse = new DepotGenerationResponse(new JsonReader(Utilities.parseJson(packetBufferReceiver.readString())));
	}

	public PacketDepotGeneration(DepotGenerationResponse depotGenerationResponse) {
		this.depotGenerationResponse = depotGenerationResponse;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeString(Utilities.getJsonObjectFromData(depotGenerationResponse).toString());
	}

	@Override
	public void runClient() {
		depotGenerationResponse.iterateDepotGenerationUpdates(depotGenerationUpdate -> depotGenerationUpdate.write(MinecraftClientData.getDashboardInstance()));
	}
}
