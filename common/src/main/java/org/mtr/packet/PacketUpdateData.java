package org.mtr.packet;

import net.minecraft.server.world.ServerWorld;
import org.mtr.client.DynamicTextureCache;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.PathData;
import org.mtr.core.data.Rail;
import org.mtr.core.data.SignalModification;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.operation.UpdateDataResponse;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.tool.Utilities;
import org.mtr.registry.Registry;

import javax.annotation.Nonnull;

public final class PacketUpdateData extends PacketRequestResponseBase {

	public PacketUpdateData(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
	}

	public PacketUpdateData(UpdateDataRequest updateDataRequest) {
		super(Utilities.getJsonObjectFromData(updateDataRequest).toString());
	}

	private PacketUpdateData(String content) {
		super(content);
	}

	@Override
	protected void runClientInbound(JsonReader jsonReader) {
		update(jsonReader);
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketUpdateData(content);
	}

	@Override
	protected SerializedDataBase getDataInstance(JsonReader jsonReader) {
		return new UpdateDataRequest(jsonReader, new MinecraftClientData());
	}

	@Nonnull
	@Override
	protected String getKey() {
		return OperationProcessor.UPDATE_DATA;
	}

	@Override
	protected PacketRequestResponseBase.ResponseType responseType() {
		return PacketRequestResponseBase.ResponseType.ALL;
	}

	public static void sendDirectlyToServerRail(ServerWorld serverWorld, Rail rail) {
		new PacketUpdateData(new UpdateDataRequest(new MinecraftClientData()).addRail(rail)).runServerOutbound(serverWorld, null);
	}

	public static void sendDirectlyToServerSignalModification(ServerWorld serverWorld, SignalModification signalModification) {
		new PacketUpdateData(new UpdateDataRequest(new MinecraftClientData()).addSignalModification(signalModification)).runServerOutbound(serverWorld, null);
	}

	public static void sendDirectlyToClientDepotUpdate(ServerWorld serverWorld, UpdateDataResponse updateDataResponse) {
		serverWorld.getPlayers().forEach(serverPlayerEntityNew -> Registry.sendPacketToClient(serverPlayerEntityNew, new PacketUpdateData(Utilities.getJsonObjectFromData(updateDataResponse).toString())));
	}

	private static void update(JsonReader jsonReader) {
		final MinecraftClientData minecraftClientData = MinecraftClientData.getInstance();
		new UpdateDataResponse(jsonReader, minecraftClientData).write();
		new UpdateDataResponse(jsonReader, MinecraftClientData.getDashboardInstance()).write();
		minecraftClientData.vehicles.forEach(vehicle -> PathData.writePathCache(vehicle.vehicleExtraData.immutablePath, new MinecraftClientData(), vehicle.getTransportMode()));
		DynamicTextureCache.instance.reload();
	}
}
