package org.mtr.packet;

import com.google.gson.JsonObject;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.mtr.MTR;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.tool.Utilities;
import org.mtr.registry.Registry;

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
		runServerOutbound(serverPlayerEntity.getServerWorld(), serverPlayerEntity);
	}

	@Override
	public final void runClient() {
		runClientInbound(new JsonReader(Utilities.parseJson(content)));
	}

	protected void runServerOutbound(ServerWorld serverWorld, @Nullable ServerPlayerEntity serverPlayerEntity) {
		MTR.sendMessageC2S(getKey(), serverWorld.getServer(), serverWorld, getDataInstance(new JsonReader(Utilities.parseJson(content))), responseType() == ResponseType.NONE ? null : responseData -> {
			final JsonObject responseJson = Utilities.getJsonObjectFromData(responseData);
			if (responseType() == ResponseType.PLAYER) {
				if (serverPlayerEntity != null) {
					Registry.sendPacketToClient(serverPlayerEntity, getInstance(responseJson.toString()));
				}
			} else {
				serverWorld.getPlayers().forEach(serverPlayerEntityNew -> Registry.sendPacketToClient(serverPlayerEntityNew, getInstance(responseJson.toString())));
			}
			runServerInbound(serverWorld, responseJson);
		}, SerializedDataBase.class);
	}

	protected void runServerInbound(ServerWorld serverWorld, JsonObject jsonObject) {
	}

	protected void runClientInbound(JsonReader jsonReader) {
	}

	/**
	 * @param content the content being sent from the Minecraft server to the Minecraft client
	 * @return an instance of the packet (should be constructed using {@link #PacketRequestResponseBase(String)})
	 */
	protected abstract PacketRequestResponseBase getInstance(String content);

	protected abstract SerializedDataBase getDataInstance(JsonReader jsonReader);

	@Nonnull
	protected abstract String getKey();

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
