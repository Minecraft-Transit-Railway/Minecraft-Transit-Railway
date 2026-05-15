package org.mtr.data;

import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.mtr.core.data.Rail;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.packet.PacketBroadcastRailActions;
import org.mtr.registry.Registry;

/**
 * Per-dimension rail action broadcaster and state manager.
 * Tracks and synchronises player rail interactions (signal changes, route interactions) across the server.
 */
public class RailActionModule {

	private final ServerWorld serverWorld;
	private final ObjectArrayList<RailAction> railActions = new ObjectArrayList<>();

	public RailActionModule(ServerWorld serverWorld) {
		this.serverWorld = serverWorld;
	}

	public void tick() {
		if (!railActions.isEmpty() && railActions.getFirst().build()) {
			railActions.removeFirst();
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
		serverWorld.getPlayers().forEach(serverPlayerEntity -> Registry.sendPacketToClient(serverPlayerEntity, new PacketBroadcastRailActions(railActions)));
	}
}
