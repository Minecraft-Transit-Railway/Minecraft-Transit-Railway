package org.mtr.mod;

import org.mtr.core.data.Data;
import org.mtr.core.integration.Integration;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.core.servlet.ServletBase;
import org.mtr.libraries.com.google.gson.JsonElement;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.libraries.io.netty.handler.codec.http.HttpResponseStatus;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mod.packet.PacketData;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public final class SocketServlet extends HttpServlet {

	private final MinecraftServer minecraftServer;

	public SocketServlet(MinecraftServer minecraftServer) {
		this.minecraftServer = minecraftServer;
	}

	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		doPost(httpServletRequest, httpServletResponse);
	}

	@Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		final AsyncContext asyncContext = httpServletRequest.startAsync();
		asyncContext.setTimeout(0);
		final JsonElement jsonElement = JsonParser.parseReader(httpServletRequest.getReader());
		final JsonObject responseObject = jsonElement.isJsonNull() ? new JsonObject() : jsonElement.getAsJsonObject();

		responseObject.keySet().forEach(playerUuid -> {
			final ServerPlayerEntity serverPlayerEntity = minecraftServer.getPlayerManager().getPlayer(UUID.fromString(playerUuid));
			if (serverPlayerEntity != null) {
				Init.REGISTRY.sendPacketToClient(serverPlayerEntity, new PacketData(IntegrationServlet.Operation.LIST, new Integration(new JsonReader(responseObject.getAsJsonObject(playerUuid)), new Data()), true, false));
			}
		});
		System.out.println();

		ServletBase.sendResponse(httpServletResponse, asyncContext, "", ServletBase.getMimeType(""), HttpResponseStatus.OK);
	}
}
