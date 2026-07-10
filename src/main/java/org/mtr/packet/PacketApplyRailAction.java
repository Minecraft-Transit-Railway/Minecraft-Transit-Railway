package org.mtr.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.mtr.MTR;
import org.mtr.core.data.ClientData;
import org.mtr.core.data.Position;
import org.mtr.core.operation.DataRequest;
import org.mtr.core.operation.DataResponse;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.tool.Utilities;
import org.mtr.item.ItemNodeModifierSelectableBlockBase;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;

/**
 * Server-bound packet that applies a bridge/tunnel/wall action across a multi-node rail path.
 * The client sends only the two clicked positions; the server fetches a fresh rail graph
 * snapshot covering them and finds the connecting path itself, since the server's data is
 * always complete while the client's locally-accumulated cache may not be (e.g. for a
 * far-away path that hasn't been fully re-synced since rejoining the world).
 */
public final class PacketApplyRailAction extends PacketHandler {

	private final BlockPos startBlockPos;
	private final BlockPos endBlockPos;

	private static final long FETCH_RADIUS_MARGIN = 64;

	public PacketApplyRailAction(PacketBufferReceiver packetBufferReceiver) {
		startBlockPos = BlockPos.of(packetBufferReceiver.readLong());
		endBlockPos = BlockPos.of(packetBufferReceiver.readLong());
	}

	public PacketApplyRailAction(BlockPos startBlockPos, BlockPos endBlockPos) {
		this.startBlockPos = startBlockPos;
		this.endBlockPos = endBlockPos;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(startBlockPos.asLong());
		packetBufferSender.writeLong(endBlockPos.asLong());
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayer serverPlayerEntity) {
		final ServerLevel serverLevel = serverPlayerEntity.serverLevel();
		final Position startPosition = MTR.blockPosToPosition(startBlockPos);
		final Position endPosition = MTR.blockPosToPosition(endBlockPos);
		final Position centerPosition = new Position((startPosition.getX() + endPosition.getX()) / 2, 0, (startPosition.getZ() + endPosition.getZ()) / 2);
		final long fetchRadius = startPosition.manhattanDistance(endPosition) / 2 + FETCH_RADIUS_MARGIN;

		final DataRequest dataRequest = new DataRequest(serverPlayerEntity.getUUID(), centerPosition, fetchRadius);
		MTR.sendMessageC2S(OperationProcessor.GET_DATA, minecraftServer, serverLevel, dataRequest, (SerializedDataBase responseData) -> {
			final JsonObject responseJson = Utilities.getJsonObjectFromData(responseData);
			final ClientData clientData = new ClientData();
			new DataResponse(new JsonReader(responseJson), clientData).write();
			clientData.sync();

			final ObjectArrayList<ObjectObjectImmutablePair<BlockPos, BlockPos>> path = ItemNodeModifierSelectableBlockBase.findRailPath(clientData.positionsToRail, startPosition, endPosition);
			if (path != null && path.size() > 1) {
				ItemNodeModifierSelectableBlockBase.processRailActions(serverPlayerEntity, path);
			}
		}, SerializedDataBase.class);
	}
}
