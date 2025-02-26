package org.mtr.mod;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mod.client.CustomResourceLoader;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class ResourcePackHelper {

	private static final String PACK_MCMETA_FILE = "pack.mcmeta";
	private static final String CUSTOM_RESOURCES_FILE = String.format("assets/mtr/%s.json", CustomResourceLoader.CUSTOM_RESOURCES_ID);
	private static final String NTE_FOLDER = "assets/mtrsteamloco";
	private static final int PACK_VERSION = ResourceManagerHelper.getResourcePackVersion();

	/**
	 * If the resource pack adds extra content for this mod, update the pack version to make it always compatible
	 */
	public static void fix() {
		final long startTime = System.currentTimeMillis();

		try (final Stream<Path> stream = Files.list(MinecraftClient.getInstance().getRunDirectoryMapped().toPath().resolve("resourcepacks"))) {
			stream.forEach(path -> {
				try {
					if (Files.isDirectory(path)) {
						if (Files.exists(path.resolve(CUSTOM_RESOURCES_FILE)) || Files.isDirectory(path.resolve(NTE_FOLDER))) {
							final byte[] newPackContent = modifyPackFile(FileUtils.readFileToString(path.resolve(PACK_MCMETA_FILE).toFile(), StandardCharsets.UTF_8));
							if (newPackContent != null) {
								Files.write(
										path.resolve(PACK_MCMETA_FILE),
										newPackContent,
										StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
								);
							}
						}
					} else {
						final boolean[] isValidResourcePack = {false};
						final byte[][] newPackContent = {null};
						try (final ZipFile zipFile = new ZipFile(path.toFile())) {
							try (final Stream<? extends ZipEntry> zipStream = zipFile.stream()) {
								zipStream.forEach(entry -> {
									if (entry.getName().equals(PACK_MCMETA_FILE)) {
										try (final InputStream inputStream = zipFile.getInputStream(entry)) {
											newPackContent[0] = modifyPackFile(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
										} catch (Exception e) {
											Init.LOGGER.error("", e);
										}
									} else if (entry.getName().equals(CUSTOM_RESOURCES_FILE) || entry.getName().equals(NTE_FOLDER)) {
										isValidResourcePack[0] = true;
									}
								});
							}
						}
						if (isValidResourcePack[0] && newPackContent[0] != null) {
							try (final FileSystem fileSystem = FileSystems.newFileSystem(URI.create("jar:" + path.toUri()), Collections.singletonMap("create", "true"))) {
								Files.write(
										fileSystem.getPath(PACK_MCMETA_FILE),
										newPackContent[0],
										StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
								);
							}
						}
					}
				} catch (Exception e) {
					Init.LOGGER.error("", e);
				}
			});
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}

		Init.LOGGER.info("Resource pack version check completed in {} ms", System.currentTimeMillis() - startTime);
	}

	@Nullable
	private static byte[] modifyPackFile(String content) {
		try {
			final JsonObject jsonObject = JsonParser.parseString(content).getAsJsonObject();
			jsonObject.getAsJsonObject("pack").addProperty("pack_format", PACK_VERSION);
			return Utilities.prettyPrint(jsonObject).getBytes(StandardCharsets.UTF_8);
		} catch (Exception e) {
			Init.LOGGER.error("", e);
			return null;
		}
	}
}
