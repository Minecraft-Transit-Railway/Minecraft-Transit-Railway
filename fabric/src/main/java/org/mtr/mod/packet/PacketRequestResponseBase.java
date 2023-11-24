package org.mtr.mod.packet;

import org.mtr.core.integration.Response;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.registry.Registry;

import javax.annotation.Nullable;

public abstract class PacketRequestResponseBase<T extends SerializedDataBase> extends PacketHandler {

	private final T request;
	private final Response response;

	public PacketRequestResponseBase(PacketBuffer packetBuffer) {
		final boolean isResponse = packetBuffer.readBoolean();
		final JsonObject jsonObject = Utilities.parseJson(readString(packetBuffer));
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
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeBoolean(request == null);
		packetBuffer.writeString((request == null ? response.getJson() : Utilities.getJsonObjectFromData(request)).toString());
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
					Registry.sendPacketToClient(serverPlayerEntity, newInstance);
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
