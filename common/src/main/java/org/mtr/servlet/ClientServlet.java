package org.mtr.mod.servlet;

import org.apache.commons.io.IOUtils;
import org.mtr.core.servlet.HttpResponseStatus;
import org.mtr.core.servlet.ServletBase;
import org.mtr.libraries.javax.servlet.AsyncContext;
import org.mtr.libraries.javax.servlet.http.HttpServlet;
import org.mtr.libraries.javax.servlet.http.HttpServletRequest;
import org.mtr.libraries.javax.servlet.http.HttpServletResponse;
import org.mtr.libraries.org.eclipse.jetty.server.Request;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mod.InitClient;
import org.mtr.mod.packet.PacketForwardClientRequest;

import javax.annotation.Nullable;
import java.io.IOException;

public final class ClientServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		sendRequest(httpServletRequest, httpServletResponse, null);
	}

	@Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		sendRequest(httpServletRequest, httpServletResponse, IOUtils.toString(httpServletRequest.getReader()));
	}

	private static void sendRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @Nullable String content) {
		final AsyncContext asyncContext = httpServletRequest.startAsync();
		asyncContext.setTimeout(0);
		final String endpoint = httpServletRequest instanceof Request ? ((Request) httpServletRequest).getOriginalURI() : httpServletRequest.getRequestURI();
		MinecraftClient.getInstance().execute(() -> InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketForwardClientRequest(
				endpoint,
				content,
				(response, path) -> {
					if (path.equals(endpoint)) {
						ServletBase.sendResponse(httpServletResponse, asyncContext, response, ServletBase.getMimeType(endpoint.equals("/") ? "html" : endpoint), HttpResponseStatus.OK);
					} else {
						ServletBase.sendResponse(httpServletResponse, asyncContext, path, "", HttpResponseStatus.REDIRECT);
					}
				}
		)));
	}
}
