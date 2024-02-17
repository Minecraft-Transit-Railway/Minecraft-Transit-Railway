package org.mtr.mod.packet;

import org.mtr.core.integration.Response;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Sends a round trip request/response. This is meant to be used on the Minecraft client only.
 * <p>
 * Minecraft client -> Minecraft server -> Transport Simulation Core -> Minecraft server -> Minecraft client
 */
public abstract class PacketRequestResponseBase extends PacketHandler {

	private final String content;

	public PacketRequestResponseBase(PacketBufferReceiver packetBufferReceiver) {
		content = packetBufferReceiver.readString();
	}

	public PacketRequestResponseBase(String content) {
		this.content = content;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeString(content);
	}

	@Override
	public final void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		runServer(serverPlayerEntity.getServerWorld(), serverPlayerEntity);
	}

	@Override
	public final void runClient() {
		runClient(Response.create(Utilities.parseJson(content)));
	}

	protected final void runServer(ServerWorld serverWorld, @Nullable ServerPlayerEntity serverPlayerEntity) {
		Init.sendHttpRequest(getEndpoint(), new World(serverWorld.data), content, responseType() == ResponseType.NONE ? null : response -> {
			if (responseType() == ResponseType.PLAYER) {
				if (serverPlayerEntity != null) {
					Init.REGISTRY.sendPacketToClient(serverPlayerEntity, getInstance(response));
				}
			} else {
				MinecraftServerHelper.iteratePlayers(serverWorld, serverPlayerEntityNew -> Init.REGISTRY.sendPacketToClient(serverPlayerEntityNew, getInstance(response)));
			}
			runServer(serverWorld, response);
		});
	}

	protected void runServer(ServerWorld serverWorld, String content) {
	}

	protected void runClient(Response response) {
	}

	/**
	 * @param content the content being sent from the Minecraft server to the Minecraft client
	 * @return an instance of the packet (should be constructed using {@link #PacketRequestResponseBase(String)})
	 */
	protected abstract PacketRequestResponseBase getInstance(String content);

	@Nonnull
	protected abstract String getEndpoint();

	/**
	 * If a response is needed, override {@link #runClient()}.
	 *
	 * @return whether this request expects a response from the POST request
	 */
	protected abstract ResponseType responseType();

	protected enum ResponseType {
		NONE, PLAYER, ALL
	}
}
