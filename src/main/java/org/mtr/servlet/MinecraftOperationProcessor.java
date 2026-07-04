package org.mtr.servlet;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.mtr.core.operation.DynamicDataResponse;
import org.mtr.core.operation.PlayerPresentResponse;
import org.mtr.core.operation.UpdateDataResponse;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.servlet.QueueObject;
import org.mtr.packet.PacketUpdateData;
import org.mtr.packet.PacketUpdateDynamicData;
import org.mtr.registry.RegistryServer;

public final class MinecraftOperationProcessor {

	public static void process(QueueObject queueObject, ServerLevel serverWorld, String dimension) {
		switch (queueObject.key) {
			case OperationProcessor.VEHICLES_LIFTS:
				if (queueObject.data instanceof DynamicDataResponse) {
					final Player playerEntity = serverWorld.getPlayerByUUID(((DynamicDataResponse) queueObject.data).uuid);
					if (playerEntity == null) {
						queueObject.runCallback(new PlayerPresentResponse(""));
					} else {
						RegistryServer.sendPacketToClient((ServerPlayer) playerEntity, new PacketUpdateDynamicData((DynamicDataResponse) queueObject.data));
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
