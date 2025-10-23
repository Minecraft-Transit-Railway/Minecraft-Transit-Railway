package org.mtr.servlet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.mtr.core.operation.DynamicDataResponse;
import org.mtr.core.operation.PlayerPresentResponse;
import org.mtr.core.operation.UpdateDataResponse;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.servlet.QueueObject;
import org.mtr.packet.PacketUpdateData;
import org.mtr.packet.PacketUpdateDynamicData;
import org.mtr.registry.Registry;

public final class MinecraftOperationProcessor {

	public static void process(QueueObject queueObject, ServerWorld serverWorld, String dimension) {
		switch (queueObject.key) {
			case OperationProcessor.VEHICLES_LIFTS:
				if (queueObject.data instanceof DynamicDataResponse) {
					final PlayerEntity playerEntity = serverWorld.getPlayerByUuid(((DynamicDataResponse) queueObject.data).uuid);
					if (playerEntity == null) {
						queueObject.runCallback(new PlayerPresentResponse(""));
					} else {
						Registry.sendPacketToClient((ServerPlayerEntity) playerEntity, new PacketUpdateDynamicData((DynamicDataResponse) queueObject.data));
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
