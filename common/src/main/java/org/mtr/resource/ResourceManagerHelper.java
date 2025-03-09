package org.mtr.resource;

import net.minecraft.MinecraftVersion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.mtr.MTR;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class ResourceManagerHelper {

	public static void readResource(Identifier identifier, Consumer<InputStream> consumer) {
		try {
			final Optional<Resource> optionalResource = MinecraftClient.getInstance().getResourceManager().getResource(identifier);
			optionalResource.ifPresent(resource -> readResource(resource, consumer));
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	public static String readResource(Identifier identifier) {
		final String[] string = {""};
		readResource(identifier, inputStream -> {
			try {
				string[0] = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			} catch (Exception e) {
				MTR.LOGGER.error("", e);
			}
		});
		return string[0];
	}

	public static void readAllResources(Identifier identifier, Consumer<InputStream> consumer) {
		try {
			MinecraftClient.getInstance().getResourceManager().getAllResources(identifier).forEach(resource -> readResource(resource, consumer));
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	public static void readDirectory(String path, BiConsumer<Identifier, InputStream> consumer) {
		try {
			MinecraftClient.getInstance().getResourceManager()
					.findAllResources(path, identifier -> true)
					.forEach((identifier, resources) -> resources.forEach(resource -> readResource(resource, inputStream -> consumer.accept(identifier, inputStream))));
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	private static void readResource(Resource resource, Consumer<InputStream> consumer) {
		try (final InputStream inputStream = resource.getInputStream()) {
			consumer.accept(inputStream);
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	public static int getResourcePackVersion() {
		return MinecraftVersion.create().getResourceVersion(ResourceType.CLIENT_RESOURCES);
	}

	public static int getDataPackVersion() {
		return MinecraftVersion.create().getResourceVersion(ResourceType.SERVER_DATA);
	}
}
