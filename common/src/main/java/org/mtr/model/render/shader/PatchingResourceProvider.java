package org.mtr.model.render.shader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.mtr.MTR;
import org.mtr.model.render.tool.GlStateTracker;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public final class PatchingResourceProvider implements ResourceFactory {

	private final ResourceFactory resourceFactory;

	public PatchingResourceProvider(ResourceManager resourceManager) {
		resourceFactory = resourceManager;
	}

	@Override
	public Optional<Resource> getResource(Identifier identifier) {
		final Identifier newIdentifier = identifier.getPath().contains("_modelmat") ? Identifier.of(identifier.getNamespace(), identifier.getPath().replace("_modelmat", "")) : identifier;
		final Optional<Resource> resource = resourceFactory.getResource(newIdentifier);

		if (resource.isEmpty()) {
			return Optional.empty();
		} else {
			try {
				final InputStream inputStream = resource.get().getInputStream();
				final String returningContent;

				if (newIdentifier.getPath().endsWith(".json")) {
					final JsonObject dataObject = JsonParser.parseString(IOUtils.toString(inputStream, StandardCharsets.UTF_8)).getAsJsonObject();
					inputStream.close();
					dataObject.addProperty("vertex", dataObject.get("vertex").getAsString() + "_modelmat");
					final JsonArray attributeArray = dataObject.get("attributes").getAsJsonArray();
					for (int i = 0; i < 6 - attributeArray.size(); i++) {
						attributeArray.add("Dummy" + i);
					}
					attributeArray.add("ModelMat");
					returningContent = dataObject.toString();
				} else if (newIdentifier.getPath().endsWith(".vsh")) {
					returningContent = patchVertexShaderSource(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
					inputStream.close();
				} else {
					return resource;
				}

				return Optional.of(new Resource(resource.get().getPack(), () -> new ByteArrayInputStream(returningContent.getBytes(StandardCharsets.UTF_8))));
			} catch (Exception e) {
				MTR.LOGGER.error("", e);
				return Optional.empty();
			}
		}
	}

	private static String patchVertexShaderSource(String sourceContent) {
		final String[] contentParts = sourceContent.split("void main");
		contentParts[0] = contentParts[0].replace("uniform mat4 ModelViewMat;", "uniform mat4 ModelViewMat;\nin mat4 ModelMat;");
		if (GlStateTracker.isGl4ES()) {
			contentParts[0] = contentParts[0].replace("ivec2", "vec2");
		}
		contentParts[1] = contentParts[1]
				.replaceAll("\\bPosition\\b", "(MODELVIEWMAT * ModelMat * vec4(Position, 1.0)).xyz")
				.replaceAll("\\bNormal\\b", "normalize(mat3(MODELVIEWMAT * ModelMat) * Normal)")
				.replace("ModelViewMat", "mat4(1.0)")
				.replace("MODELVIEWMAT", "ModelViewMat");
		return contentParts[0] + "void main" + contentParts[1];
	}
}
