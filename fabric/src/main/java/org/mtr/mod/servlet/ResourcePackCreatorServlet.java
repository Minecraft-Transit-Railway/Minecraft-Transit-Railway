package org.mtr.mod.servlet;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.servlet.HttpResponseStatus;
import org.mtr.core.servlet.ServletBase;
import org.mtr.core.tool.Utilities;
import org.mtr.legacy.resource.CustomResourcesConverter;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.javax.servlet.AsyncContext;
import org.mtr.libraries.javax.servlet.http.HttpServlet;
import org.mtr.libraries.javax.servlet.http.HttpServletRequest;
import org.mtr.libraries.javax.servlet.http.HttpServletResponse;
import org.mtr.libraries.javax.servlet.http.Part;
import org.mtr.mod.Init;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.resource.BlockbenchModel;
import org.mtr.mod.resource.CustomResources;
import org.mtr.mod.resource.ResourceWrapper;

import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ResourcePackCreatorServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		final AsyncContext asyncContext = httpServletRequest.startAsync();
		asyncContext.setTimeout(0);

		switch (httpServletRequest.getPathInfo()) {
			case "/upload":
				upload(httpServletRequest, httpServletResponse, asyncContext);
				break;
			default:
				ServletBase.sendResponse(httpServletResponse, asyncContext, "", "", HttpResponseStatus.BAD_REQUEST);
				break;
		}
	}

	private static void upload(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AsyncContext asyncContext) {
		try {
			for (final Part part : httpServletRequest.getParts()) {
				Init.LOGGER.info("Processing {} uploaded from the Resource Pack Creator", part.getSubmittedFileName());
				final Object2ObjectAVLTreeMap<String, String> files = getUploadedFilesInZip(part);
				final CustomResources customResources = CustomResourcesConverter.convert(Utilities.parseJson(files.get(String.format("assets/%s/%s.json", Init.MOD_ID, CustomResourceLoader.CUSTOM_RESOURCES_ID))));
				final ObjectArrayList<BlockbenchModel> blockbenchModels = new ObjectArrayList<>();
				final ObjectArrayList<String> textures = new ObjectArrayList<>();

				files.forEach((name, content) -> {
					final String nameLowerCase = name.toLowerCase(Locale.ENGLISH);
					if (nameLowerCase.endsWith(".png")) {
						textures.add(name);
					} else if (nameLowerCase.endsWith(".bbmodel")) {
						blockbenchModels.add(new BlockbenchModel(new JsonReader(Utilities.parseJson(content))));
					}
				});

				final ResourceWrapper resourceWrapper = new ResourceWrapper(customResources, blockbenchModels, textures);
				ServletBase.sendResponse(httpServletResponse, asyncContext, Utilities.getJsonObjectFromData(resourceWrapper).toString(), "", HttpResponseStatus.OK);
				return;
			}
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}

		ServletBase.sendResponse(httpServletResponse, asyncContext, "", "", HttpResponseStatus.BAD_REQUEST);
	}

	private static Object2ObjectAVLTreeMap<String, String> getUploadedFilesInZip(Part part) {
		final Object2ObjectAVLTreeMap<String, String> files = new Object2ObjectAVLTreeMap<>();

		try (final ZipInputStream zipInputStream = new ZipInputStream(part.getInputStream())) {
			ZipEntry zipEntry;

			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				final String name = zipEntry.getName();
				Init.LOGGER.debug("Reading {}", name);
				final StringBuilder stringBuilder = new StringBuilder();

				final byte[] bytes = new byte[4096];
				int length;
				while ((length = zipInputStream.read(bytes)) != -1) {
					stringBuilder.append(new String(bytes, 0, length));
				}

				files.put(name, stringBuilder.toString());
				zipInputStream.closeEntry();
			}
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}

		return files;
	}
}
