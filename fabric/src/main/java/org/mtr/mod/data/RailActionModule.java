package org.mtr.mod.data;

import org.mtr.core.data.Rail;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.packet.PacketBroadcastRailActions;

public class RailActionModule {

	private final ServerWorld serverWorld;
	private final ObjectArrayList<RailAction> railActions = new ObjectArrayList<>();

	public RailActionModule(ServerWorld serverWorld) {
		this.serverWorld = serverWorld;
	}

	public void tick() {
		if (!railActions.isEmpty() && railActions.get(0).build()) {
			railActions.remove(0);
			broadcastUpdate();
		}
	}

	public void markRailForBridge(Rail rail, ServerPlayerEntity serverPlayerEntity, int radius, BlockState blockState) {
		railActions.add(new RailAction(serverWorld, serverPlayerEntity, RailActionType.BRIDGE, rail, radius, 0, blockState));
		broadcastUpdate();
	}

	public void markRailForTunnel(Rail rail, ServerPlayerEntity serverPlayerEntity, int radius, int height) {
		railActions.add(new RailAction(serverWorld, serverPlayerEntity, RailActionType.TUNNEL, rail, radius, height, null));
		broadcastUpdate();
	}

	public void markRailForTunnelWall(Rail rail, ServerPlayerEntity serverPlayerEntity, int radius, int height, BlockState blockState) {
		railActions.add(new RailAction(serverWorld, serverPlayerEntity, RailActionType.TUNNEL_WALL, rail, radius + 1, height + 1, blockState));
		broadcastUpdate();
	}

	public void removeRailAction(long id) {
		railActions.removeIf(railAction -> railAction.id == id);
		broadcastUpdate();
	}

	private void broadcastUpdate() {
		MinecraftServerHelper.iteratePlayers(serverWorld, serverPlayerEntity -> Init.REGISTRY.sendPacketToClient(serverPlayerEntity, new PacketBroadcastRailActions(railActions)));
	}
}
