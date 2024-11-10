package org.mtr.mod.servlet;

import org.mtr.core.servlet.HttpResponseStatus;
import org.mtr.core.servlet.ServletBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.javax.servlet.AsyncContext;
import org.mtr.libraries.javax.servlet.http.HttpServlet;
import org.mtr.libraries.javax.servlet.http.HttpServletResponse;
import org.mtr.mod.resource.ResourceWrapper;

import javax.annotation.Nullable;

public abstract class AbstractResourcePackCreatorServlet extends HttpServlet {

	@Nullable
	protected static ResourceWrapper resourceWrapper;

	protected static void returnStandardResponse(HttpServletResponse httpServletResponse, AsyncContext asyncContext) {
		ServletBase.sendResponse(httpServletResponse, asyncContext, resourceWrapper == null ? String.valueOf((Object) null) : Utilities.getJsonObjectFromData(resourceWrapper).toString(), "", HttpResponseStatus.OK);
	}

	protected static void returnErrorResponse(HttpServletResponse httpServletResponse, AsyncContext asyncContext) {
		ServletBase.sendResponse(httpServletResponse, asyncContext, new JsonObject().toString(), "", HttpResponseStatus.BAD_REQUEST);
	}

}
