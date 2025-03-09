package org.mtr.data;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.mtr.core.data.Rail;
import org.mtr.packet.PacketBroadcastRailActions;
import org.mtr.registry.Registry;

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
		serverWorld.getPlayers().forEach(serverPlayerEntity -> Registry.sendPacketToClient(serverPlayerEntity, new PacketBroadcastRailActions(railActions)));
	}
}
