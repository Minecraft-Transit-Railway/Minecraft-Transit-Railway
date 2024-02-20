package org.mtr.mod.servlet;

import org.mtr.core.integration.Response;
import org.mtr.core.operation.DepotGenerationResponse;
import org.mtr.core.servlet.ServletBase;
import org.mtr.libraries.com.google.gson.JsonElement;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.libraries.io.netty.handler.codec.http.HttpResponseStatus;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mod.Init;
import org.mtr.mod.packet.PacketDepotGeneration;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public final class DepotGenerationServlet extends HttpServlet {

	private static MinecraftServer minecraftServer;

	public DepotGenerationServlet(MinecraftServer minecraftServer) {
		DepotGenerationServlet.minecraftServer = minecraftServer;
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
		final DepotGenerationResponse depotGenerationResponse = Response.create(jsonElement.getAsJsonObject()).getData(DepotGenerationResponse::new);

		if (minecraftServer != null) {
			depotGenerationResponse.iterateClientIds(clientId -> {
				final ServerPlayerEntity serverPlayerEntity = minecraftServer.getPlayerManager().getPlayer(UUID.fromString(clientId));
				if (serverPlayerEntity != null) {
					Init.REGISTRY.sendPacketToClient(serverPlayerEntity, new PacketDepotGeneration(depotGenerationResponse));
				}
			});
		}

		ServletBase.sendResponse(httpServletResponse, asyncContext, "{}", ServletBase.getMimeType("json"), HttpResponseStatus.OK);
	}
}
