package org.mtr.servlet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.mtr.core.operation.PlayerPresentResponse;
import org.mtr.core.operation.UpdateDataResponse;
import org.mtr.core.operation.VehicleLiftResponse;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.servlet.QueueObject;
import org.mtr.packet.PacketUpdateData;
import org.mtr.packet.PacketUpdateVehiclesLifts;
import org.mtr.registry.Registry;

public final class MinecraftOperationProcessor {

	public static void process(QueueObject queueObject, ServerWorld serverWorld, String dimension) {
		switch (queueObject.key) {
			case OperationProcessor.VEHICLES_LIFTS:
				if (queueObject.data instanceof VehicleLiftResponse) {
					final PlayerEntity playerEntity = serverWorld.getPlayerByUuid(((VehicleLiftResponse) queueObject.data).uuid);
					if (playerEntity == null) {
						queueObject.runCallback(new PlayerPresentResponse(""));
					} else {
						Registry.sendPacketToClient((ServerPlayerEntity) playerEntity, new PacketUpdateVehiclesLifts((VehicleLiftResponse) queueObject.data));
						queueObject.runCallback(new PlayerPresentResponse(dimension));
					}
				}
				break;
			case OperationProcessor.GENERATION_STATUS_UPDATE:
				if (queueObject.data instanceof UpdateDataResponse) {
					PacketUpdateData.sendDirectlyToClientDepotUpdate(serverWorld, (UpdateDataResponse) queueObject.data);
				}
				break;
		}

	}
}
