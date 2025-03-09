package org.mtr.packet;

import it.unimi.dsi.fastutil.longs.Long2ObjectAVLTreeMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.mtr.MTR;
import org.mtr.registry.Registry;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.BiConsumer;

public final class PacketForwardClientRequest extends PacketHandler {

	private final String endpoint;
	private final String content;
	private final String path;
	private final long callbackId;

	private static final Long2ObjectAVLTreeMap<BiConsumer<String, String>> CALLBACKS = new Long2ObjectAVLTreeMap<>();

	public PacketForwardClientRequest(PacketBufferReceiver packetBufferReceiver) {
		endpoint = packetBufferReceiver.readString();
		content = packetBufferReceiver.readString();
		path = packetBufferReceiver.readString();
		callbackId = packetBufferReceiver.readLong();
	}

	public PacketForwardClientRequest(String endpoint, @Nullable String content, BiConsumer<String, String> callback) {
		this.endpoint = endpoint;
		this.content = content == null ? "" : content;
		path = "";
		callbackId = new Random().nextLong();
		CALLBACKS.put(callbackId, callback);
	}

	private PacketForwardClientRequest(@Nullable String content, String path, long callbackId) {
		endpoint = "";
		this.content = content == null ? "" : content;
		this.path = path;
		this.callbackId = callbackId;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeString(endpoint);
		packetBufferSender.writeString(content);
		packetBufferSender.writeString(path);
		packetBufferSender.writeLong(callbackId);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		MTR.REQUEST_HELPER.sendRequest(
				String.format("http://localhost:%s%s", MTR.getServerPort(), endpoint),
				content.isEmpty() ? null : content,
				(response, path) -> Registry.sendPacketToClient(serverPlayerEntity, new PacketForwardClientRequest(response, path, callbackId))
		);
	}

	@Override
	public void runClient() {
		final BiConsumer<String, String> callback = CALLBACKS.remove(callbackId);
		if (callback != null) {
			callback.accept(content, path);
		}
	}
}
