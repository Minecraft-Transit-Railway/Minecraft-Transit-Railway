package org.mtr.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.core.data.VehicleRidingEntity;
import org.mtr.core.operation.UpdateVehicleRidingEntities;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.tool.Utilities;

public final class PacketUpdateVehicleRidingEntities extends PacketRequestResponseBase implements Utilities {

	private final boolean dismount;

	public static PacketUpdateVehicleRidingEntities create(long sidingId, long vehicleId, int ridingCar, double x, double y, double z, boolean isOnGangway, boolean isDriver, boolean manualAccelerate, boolean manualBrake, boolean manualToggleDoors, boolean manualToggleAto, boolean doorOverride) {
		final UpdateVehicleRidingEntities updateVehicleRidingEntities = new UpdateVehicleRidingEntities(sidingId, vehicleId);
		final LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;
		if (clientPlayerEntity != null) {
			updateVehicleRidingEntities.add(new VehicleRidingEntity(clientPlayerEntity.getUUID(), ridingCar, x, y, z, isOnGangway, isDriver, manualAccelerate, manualBrake, manualToggleDoors, manualToggleAto, doorOverride));
		}
		return new PacketUpdateVehicleRidingEntities(Utilities.getJsonObjectFromData(updateVehicleRidingEntities).toString(), ridingCar < 0);
	}

	public PacketUpdateVehicleRidingEntities(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
		dismount = packetBufferReceiver.readBoolean();
	}

	private PacketUpdateVehicleRidingEntities(String content, boolean dismount) {
		super(content);
		this.dismount = dismount;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		super.write(packetBufferSender);
		packetBufferSender.writeBoolean(dismount);
	}

	@Override
	protected void runServerOutbound(ServerLevel serverWorld, @Nullable ServerPlayer serverPlayerEntity) {
		super.runServerOutbound(serverWorld, serverPlayerEntity);
		if (serverPlayerEntity != null) {
			MTR.updateRidingEntity(serverPlayerEntity, dismount);
		}
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketUpdateVehicleRidingEntities(content, dismount);
	}

	@Override
	protected SerializedDataBase getDataInstance(JsonReader jsonReader) {
		return new UpdateVehicleRidingEntities(jsonReader);
	}

	@Override
	protected String getKey() {
		return OperationProcessor.UPDATE_RIDING_ENTITIES;
	}

	@Override
	protected ResponseType responseType() {
		return ResponseType.NONE;
	}
}
