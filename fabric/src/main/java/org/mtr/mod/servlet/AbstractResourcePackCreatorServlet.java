package org.mtr.mod.servlet;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.servlet.HttpResponseStatus;
import org.mtr.core.servlet.ServletBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.javax.servlet.AsyncContext;
import org.mtr.libraries.javax.servlet.http.HttpServlet;
import org.mtr.libraries.javax.servlet.http.HttpServletRequest;
import org.mtr.libraries.javax.servlet.http.HttpServletResponse;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.resource.ResourceWrapper;

import javax.annotation.Nullable;

public abstract class AbstractResourcePackCreatorServlet extends HttpServlet {

	@Nullable
	protected static ResourceWrapper resourceWrapper;

	protected static void returnStandardResponse(HttpServletResponse httpServletResponse, AsyncContext asyncContext, boolean refreshVehicles) {
		if (resourceWrapper == null) {
			ServletBase.sendResponse(httpServletResponse, asyncContext, String.valueOf((Object) null), "", HttpResponseStatus.OK);
		} else {
			resourceWrapper.updateMinecraftInfo();
			ServletBase.sendResponse(httpServletResponse, asyncContext, Utilities.getJsonObjectFromData(resourceWrapper).toString(), "", HttpResponseStatus.OK);
			if (refreshVehicles) {
				final JsonObject vehiclesFlattened = resourceWrapper.flatten();
				MinecraftClient.getInstance().execute(() -> {
					CustomResourceLoader.clearCustomVehicles();
					new ResourceWrapper(new JsonReader(vehiclesFlattened), new ObjectArrayList<>(), new ObjectArrayList<>()).iterateVehicles(vehicleResourceWrapper -> CustomResourceLoader.registerVehicle(vehicleResourceWrapper.toVehicleResource(identifier -> {
						final String modelString = ResourcePackCreatorUploadServlet.getModel(identifier.data.toString());
						return modelString == null ? ResourceManagerHelper.readResource(identifier) : modelString;
					}, null, null)));
				});
			}
		}
	}

	protected static void returnErrorResponse(HttpServletResponse httpServletResponse, AsyncContext asyncContext) {
		ServletBase.sendResponse(httpServletResponse, asyncContext, new JsonObject().toString(), "", HttpResponseStatus.BAD_REQUEST);
	}

	protected static void setEncoding(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		try {
			httpServletRequest.setCharacterEncoding("UTF-8");
			httpServletResponse.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}
	}
}
