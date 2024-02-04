package org.mtr.mod.packet;

import org.mtr.core.integration.Response;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;

import javax.annotation.Nullable;

public abstract class PacketRequestResponseBase<T extends SerializedDataBase> extends PacketHandler {

	private final T request;
	private final Response response;

	public PacketRequestResponseBase(PacketBufferReceiver packetBufferReceiver) {
		final boolean isResponse = packetBufferReceiver.readBoolean();
		final JsonObject jsonObject = Utilities.parseJson(packetBufferReceiver.readString());
		request = isResponse ? null : createRequest(new JsonReader(jsonObject));
		response = isResponse ? Response.create(jsonObject) : null;
	}

	public PacketRequestResponseBase(T request) {
		this.request = request;
		response = null;
	}

	protected PacketRequestResponseBase(Response response) {
		request = null;
		this.response = response;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeBoolean(request == null);
		packetBufferSender.writeString((request == null ? response.getJson() : Utilities.getJsonObjectFromData(request)).toString());
	}

	@Override
	public final void runServer() {
	}

	@Override
	public final void runClient() {
	}

	@Override
	public final void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		if (request != null) {
			PacketData.sendHttpRequest(getEndpoint(), Utilities.getJsonObjectFromData(request), data -> {
				final PacketRequestResponseBase<T> newInstance = createInstance(Response.create(data));
				if (newInstance != null) {
					Init.REGISTRY.sendPacketToClient(serverPlayerEntity, newInstance);
				}
			});
		}
	}

	@Override
	public final void runClientQueued() {
		if (response != null) {
			runClient(response);
		}
	}

	@Nullable
	protected abstract PacketRequestResponseBase<T> createInstance(Response response);

	protected abstract T createRequest(JsonReader jsonReader);

	protected abstract String getEndpoint();

	protected abstract void runClient(Response responseData);
}
