package org.mtr.mod.packet;

import org.mtr.core.data.VehicleRidingEntity;
import org.mtr.core.operation.UpdateVehicleRidingEntities;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.ClientPlayerEntity;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.PacketBufferReceiver;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class PacketUpdateVehicleRidingEntities extends PacketRequestResponseBase implements Utilities {

	public static PacketUpdateVehicleRidingEntities create(long sidingId, long vehicleId, int ridingCar, double x, double y, double z, boolean isOnGangway) {
		final UpdateVehicleRidingEntities updateVehicleRidingEntities = new UpdateVehicleRidingEntities(sidingId, vehicleId);
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity != null) {
			updateVehicleRidingEntities.add(new VehicleRidingEntity(clientPlayerEntity.getUuid(), ridingCar, x, y, z, isOnGangway));
		}
		return new PacketUpdateVehicleRidingEntities(Utilities.getJsonObjectFromData(updateVehicleRidingEntities).toString());
	}

	public PacketUpdateVehicleRidingEntities(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
	}

	private PacketUpdateVehicleRidingEntities(String content) {
		super(content);
	}

	@Override
	protected void runServerOutbound(ServerWorld serverWorld, @Nullable ServerPlayerEntity serverPlayerEntity) {
		super.runServerOutbound(serverWorld, serverPlayerEntity);
		if (serverPlayerEntity != null) {
			serverPlayerEntity.setFallDistanceMapped(0);
		}
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
