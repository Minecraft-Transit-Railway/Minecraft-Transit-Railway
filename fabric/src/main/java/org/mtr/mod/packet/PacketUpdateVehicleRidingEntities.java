package org.mtr.mod.packet;

import org.mtr.core.data.VehicleRidingEntity;
import org.mtr.core.integration.Response;
import org.mtr.core.operation.UpdateVehicleRidingEntities;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.ClientPlayerEntity;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.PacketBuffer;

import javax.annotation.Nullable;

public class PacketUpdateVehicleRidingEntities extends PacketRequestResponseBase<UpdateVehicleRidingEntities> implements Utilities {

	public static PacketUpdateVehicleRidingEntities create(long sidingId, long vehicleId, int ridingCar, double x, double y, double z, boolean isOnGangway) {
		final UpdateVehicleRidingEntities updateVehicleRidingEntities = new UpdateVehicleRidingEntities(sidingId, vehicleId);
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity != null) {
			updateVehicleRidingEntities.add(new VehicleRidingEntity(clientPlayerEntity.getUuid(), ridingCar, x, y, z, isOnGangway));
		}
		return new PacketUpdateVehicleRidingEntities(updateVehicleRidingEntities);
	}

	public PacketUpdateVehicleRidingEntities(PacketBuffer packetBuffer) {
		super(packetBuffer);
	}

	private PacketUpdateVehicleRidingEntities(UpdateVehicleRidingEntities updateVehicleRidingEntities) {
		super(updateVehicleRidingEntities);
	}

	@Nullable
	@Override
	protected PacketRequestResponseBase<UpdateVehicleRidingEntities> createInstance(Response response) {
		return null;
	}

	@Override
	protected UpdateVehicleRidingEntities createRequest(JsonReader jsonReader) {
		return new UpdateVehicleRidingEntities(jsonReader);
	}

	@Override
	protected String getEndpoint() {
		return "operation/update-riding-entities";
	}

	@Override
	protected void runClient(Response response) {
	}
}
