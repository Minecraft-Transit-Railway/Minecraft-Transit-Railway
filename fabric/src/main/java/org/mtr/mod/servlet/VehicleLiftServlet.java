package org.mtr.mod.servlet;

import org.mtr.core.integration.Response;
import org.mtr.core.operation.PlayerPresentResponse;
import org.mtr.core.operation.VehicleLiftResponse;
import org.mtr.core.servlet.HttpResponseStatus;
import org.mtr.core.servlet.ServletBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonElement;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.libraries.javax.servlet.AsyncContext;
import org.mtr.libraries.javax.servlet.http.HttpServlet;
import org.mtr.libraries.javax.servlet.http.HttpServletRequest;
import org.mtr.libraries.javax.servlet.http.HttpServletResponse;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mod.Init;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.packet.PacketUpdateVehiclesLifts;

import java.io.IOException;
import java.util.UUID;

public final class VehicleLiftServlet extends HttpServlet {

	private static MinecraftServer minecraftServer;

	public VehicleLiftServlet(MinecraftServer minecraftServer) {
		VehicleLiftServlet.minecraftServer = minecraftServer;
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
		final boolean playerPresent;

		if (minecraftServer != null && jsonElement.isJsonObject()) {
			final Response response = Response.create(jsonElement.getAsJsonObject());
			final ServerPlayerEntity serverPlayerEntity = minecraftServer.getPlayerManager().getPlayer(UUID.fromString(response.getData(jsonReader -> new VehicleLiftResponse(jsonReader, new MinecraftClientData())).getClientId()));
			if (serverPlayerEntity == null) {
				playerPresent = false;
			} else {
				playerPresent = true;
				Init.REGISTRY.sendPacketToClient(serverPlayerEntity, new PacketUpdateVehiclesLifts(response));
			}
		} else {
			playerPresent = false;
		}

		ServletBase.sendResponse(httpServletResponse, asyncContext, Utilities.getJsonObjectFromData(new PlayerPresentResponse(playerPresent)).toString(), ServletBase.getMimeType("json"), HttpResponseStatus.OK);
	}
}
