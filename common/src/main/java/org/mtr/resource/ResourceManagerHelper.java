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

/**
 * Utilities for reading and managing Minecraft resource pack assets.
 *
 * <p>Centralises the resource-manager-access pattern so callers don't repeat the same
 * {@code Optional.ifPresent} / try-with-resources / IOUtils dance. All methods swallow
 * {@link Exception} into a log call rather than throwing — resource reads should not be
 * able to crash the client.</p>
 *
 * <p>This class is for <b>client-side</b> resource pack reads only. Server-side data reads
 * go through Minecraft's data-pack APIs directly.</p>
 */
public final class ResourceManagerHelper {

	/**
	 * Read the resource identified by {@code identifier}, invoking {@code consumer} with the
	 * resource's input stream. If the resource is absent or the read throws, the consumer
	 * is not invoked and the failure is logged.
	 */
	public static void readResource(Identifier identifier, Consumer<InputStream> consumer) {
		try {
			final Optional<Resource> optionalResource = MinecraftClient.getInstance().getResourceManager().getResource(identifier);
			optionalResource.ifPresent(resource -> readResource(resource, consumer));
		} catch (Exception e) {
			MTR.LOGGER.error("Failed to read resource [{}] from the resource manager", identifier, e);
		}
	}

	/**
	 * Read the resource identified by {@code identifier} as a UTF-8 string. Returns the
	 * empty string if the resource is missing or decoding fails.
	 *
	 * <p><b>Performance:</b> prefer
	 * {@link #readResource(Identifier, Consumer)} when the caller already plans to feed the
	 * stream into another parser — it avoids the intermediate {@link String} allocation
	 * (see {@code docs/PERFORMANCE.md} §1.1).</p>
	 */
	public static String readResource(Identifier identifier) {
		final String[] string = {""};
		readResource(identifier, inputStream -> {
			try {
				string[0] = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			} catch (Exception e) {
				MTR.LOGGER.error("Failed to decode resource [{}] as UTF-8 text", identifier, e);
			}
		});
		return string[0];
	}

	/**
	 * Read <b>every</b> resource matching the identifier across all active resource packs,
	 * invoking the consumer once per pack. Used to merge data from packs that all ship the
	 * same well-known manifest path (e.g. {@code mtr:mtr_custom_resources.json}).
	 */
	public static void readAllResources(Identifier identifier, Consumer<InputStream> consumer) {
		try {
			MinecraftClient.getInstance().getResourceManager().getAllResources(identifier).forEach(resource -> readResource(resource, consumer));
		} catch (Exception e) {
			MTR.LOGGER.error("Failed to enumerate all resources matching [{}]", identifier, e);
		}
	}

	/**
	 * Walk every resource under the given directory path across all active resource packs.
	 * The consumer receives each file's identifier (so the caller can recover its
	 * namespace / leaf name) and the open input stream.
	 */
	public static void readDirectory(String path, BiConsumer<Identifier, InputStream> consumer) {
		try {
			MinecraftClient.getInstance().getResourceManager()
				.findAllResources(path, identifier -> true)
				.forEach((identifier, resources) -> resources.forEach(resource -> readResource(resource, inputStream -> consumer.accept(identifier, inputStream))));
		} catch (Exception e) {
			MTR.LOGGER.error("Failed to enumerate resources under directory [{}]", path, e);
		}
	}

	private static void readResource(Resource resource, Consumer<InputStream> consumer) {
		try (final InputStream inputStream = resource.getInputStream()) {
			consumer.accept(inputStream);
		} catch (Exception e) {
			MTR.LOGGER.error("Failed to open the input stream for resource [{}]", resource.getPackId(), e);
		}
	}

	/** @return the resource-pack format version Minecraft expects on the current client. */
	public static int getResourcePackVersion() {
		return MinecraftVersion.create().getResourceVersion(ResourceType.CLIENT_RESOURCES);
	}

	/** @return the data-pack format version Minecraft expects on the current server. */
	public static int getDataPackVersion() {
		return MinecraftVersion.create().getResourceVersion(ResourceType.SERVER_DATA);
	}
}
