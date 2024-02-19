package org.mtr.mod.packet;

import org.mtr.core.data.VehicleRidingEntity;
import org.mtr.core.operation.UpdateVehicleRidingEntities;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.ClientPlayerEntity;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.tool.PacketBufferReceiver;

import javax.annotation.Nonnull;

public final class PacketUpdateVehicleRidingEntities extends PacketRequestResponseBase implements Utilities {

	public static PacketUpdateVehicleRidingEntities create(long sidingId, long vehicleId, int ridingCar, double x, double y, double z, boolean isOnGangway) {
		final UpdateVehicleRidingEntities updateVehicleRidingEntities = new UpdateVehicleRidingEntities(sidingId, vehicleId);
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity != null) {
			updateVehicleRidingEntities.add(new VehicleRidingEntity(clientPlayerEntity.getUuid(), ridingCar, x, y, z, isOnGangway));
		}
		return new PacketUpdateVehicleRidingEntities(updateVehicleRidingEntities);
	}

	public PacketUpdateVehicleRidingEntities(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
	}

	private PacketUpdateVehicleRidingEntities(String content) {
		super(content);
	}

	private PacketUpdateVehicleRidingEntities(UpdateVehicleRidingEntities updateVehicleRidingEntities) {
		super(Utilities.getJsonObjectFromData(updateVehicleRidingEntities).toString());
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketUpdateVehicleRidingEntities(content);
	}

	@Nonnull
	@Override
	protected String getEndpoint() {
		return "operation/update-riding-entities";
	}

	@Override
	protected PacketRequestResponseBase.ResponseType responseType() {
		return PacketRequestResponseBase.ResponseType.NONE;
	}
}
