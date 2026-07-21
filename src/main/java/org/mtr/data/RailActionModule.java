package org.mtr.data;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.core.data.Rail;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.packet.PacketBroadcastRailActions;
import org.mtr.registry.RegistryServer;

/**
 * Per-dimension rail action broadcaster and state manager.
 * Tracks and synchronises player rail interactions (signal changes, route interactions) across the server.
 */
public class RailActionModule {

	private final ServerLevel serverWorld;
	private final ObjectArrayList<RailAction> railActions = new ObjectArrayList<>();

	public RailActionModule(ServerLevel serverWorld) {
		this.serverWorld = serverWorld;
	}

	public void tick() {
		if (!railActions.isEmpty() && railActions.getFirst().build()) {
			railActions.removeFirst();
			broadcastUpdate();
		}
	}

	public void markRailForBridge(Rail rail, ServerPlayer serverPlayerEntity, int radius, BlockState blockState, int batchIndex, int batchTotal) {
		railActions.add(new RailAction(serverWorld, serverPlayerEntity, RailActionType.BRIDGE, rail, radius, 0, blockState, 0, batchIndex, batchTotal));
		broadcastUpdate();
	}

	public void markRailForTunnel(Rail rail, ServerPlayer serverPlayerEntity, int radius, int height, int batchIndex, int batchTotal) {
		railActions.add(new RailAction(serverWorld, serverPlayerEntity, RailActionType.TUNNEL, rail, radius, height, null, 0, batchIndex, batchTotal));
		broadcastUpdate();
	}

	public void markRailForTunnelWall(Rail rail, ServerPlayer serverPlayerEntity, int radius, int height, BlockState blockState, int batchIndex, int batchTotal, int wallSide) {
		railActions.add(new RailAction(serverWorld, serverPlayerEntity, RailActionType.TUNNEL_WALL, rail, radius + 1, height + 1, blockState, batchIndex , batchTotal, wallSide));
		broadcastUpdate();
	}

	public void markRailForBridgeWall(Rail rail, ServerPlayer serverPlayerEntity, int radius, int height, BlockState blockState, int batchIndex, int batchTotal, int wallSide) {
		railActions.add(new RailAction(serverWorld, serverPlayerEntity, RailActionType.BRIDGE_WALL, rail, radius + 1, height + 1, blockState, batchIndex, batchTotal, wallSide));
		broadcastUpdate();
	}

	public void removeRailAction(long id) {
		railActions.removeIf(railAction -> railAction.id == id);
		broadcastUpdate();
	}

	private void broadcastUpdate() {
		serverWorld.players().forEach(serverPlayerEntity -> RegistryServer.sendPacketToClient(serverPlayerEntity, new PacketBroadcastRailActions(railActions)));
	}
}
