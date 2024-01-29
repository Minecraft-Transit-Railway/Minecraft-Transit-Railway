package org.mtr.mod.data;

import org.mtr.core.data.Rail;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.client.ClientData;
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

	public boolean markRailForBridge(ServerPlayerEntity serverPlayerEntity, BlockPos pos1, BlockPos pos2, int radius, BlockState state) {
		final Rail rail = ClientData.getRail(pos1, pos2);
		if (rail == null) {
			return false;
		} else {
			railActions.add(new RailAction(serverWorld, serverPlayerEntity, RailActionType.BRIDGE, rail, radius, 0, state));
			broadcastUpdate();
			return true;
		}
	}

	public boolean markRailForTunnel(ServerPlayerEntity serverPlayerEntity, BlockPos pos1, BlockPos pos2, int radius, int height) {
		final Rail rail = ClientData.getRail(pos1, pos2);
		if (rail == null) {
			return false;
		} else {
			railActions.add(new RailAction(serverWorld, serverPlayerEntity, RailActionType.TUNNEL, rail, radius, height, null));
			broadcastUpdate();
			return true;
		}
	}

	public boolean markRailForTunnelWall(ServerPlayerEntity serverPlayerEntity, BlockPos pos1, BlockPos pos2, int radius, int height, BlockState state) {
		final Rail rail = ClientData.getRail(pos1, pos2);
		if (rail == null) {
			return false;
		} else {
			railActions.add(new RailAction(serverWorld, serverPlayerEntity, RailActionType.TUNNEL_WALL, rail, radius + 1, height + 1, state));
			broadcastUpdate();
			return true;
		}
	}

	public void removeRailAction(long id) {
		railActions.removeIf(railAction -> railAction.id == id);
		broadcastUpdate();
	}

	private void broadcastUpdate() {
		MinecraftServerHelper.iteratePlayers(serverWorld, serverPlayerEntity -> Init.REGISTRY.sendPacketToClient(serverPlayerEntity, new PacketBroadcastRailActions(railActions)));
	}
}
