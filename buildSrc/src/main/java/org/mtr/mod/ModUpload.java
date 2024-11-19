package org.mtr.mod;

import com.jonafanho.apitools.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ModUpload {

	private static final String[] MINECRAFT_VERSIONS = {"1.16.5", "1.17.1", "1.18.2", "1.19.2", "1.19.4", "1.20.1", "1.20.4"};

	public static void main(String[] args) throws IOException {
		if (args.length == 3) {
			for (final String minecraftVersion : MINECRAFT_VERSIONS) {
				for (final ModLoader modLoader : ModLoader.values()) {
					final String modVersion = String.format("%s-%s+%s", modLoader.name, args[0], minecraftVersion);
					final String modVersionUpperCase = String.format("%s-%s+%s", modLoader.name.toUpperCase(Locale.ENGLISH), args[0], minecraftVersion);
					final String fileName = String.format("MTR-%s.jar", modVersion);
					final Path filePath = Paths.get("build/release").resolve(fileName);
					final ReleaseStatus releaseStatus = args[0].toLowerCase(Locale.ENGLISH).contains("beta") ? ReleaseStatus.BETA : ReleaseStatus.RELEASE;

					final Map<String, DependencyType> dependenciesCurseForge = new HashMap<>();
					if (modLoader == ModLoader.FABRIC) {
						dependenciesCurseForge.put("fabric-api", DependencyType.REQUIRED);
					}
					dependenciesCurseForge.put("the-tube", DependencyType.OPTIONAL);

					do {
					} while (!new ModId("266707", ModProvider.CURSE_FORGE).uploadFile(
							"",
							modVersionUpperCase,
							"See Discord",
							dependenciesCurseForge,
							releaseStatus,
							Collections.singleton(minecraftVersion),
							Collections.singleton(modLoader),
							false,
							Files.newInputStream(filePath),
							fileName,
							args[1]
					));

					final Map<String, DependencyType> dependenciesModrinth = new HashMap<>();
					if (modLoader == ModLoader.FABRIC) {
						dependenciesModrinth.put("P7dR8mSH", DependencyType.REQUIRED);
					}
					dependenciesModrinth.put("8pXSjgW8", DependencyType.INCOMPATIBLE);

					do {
					} while (!new ModId("XKPAmI6u", ModProvider.MODRINTH).uploadFile(
							modVersionUpperCase,
							modVersionUpperCase,
							"See Discord",
							dependenciesModrinth,
							releaseStatus,
							Collections.singleton(minecraftVersion),
							Collections.singleton(modLoader),
							false,
							Files.newInputStream(filePath),
							fileName,
							args[2]
					));
				}
			}
		}
	}
}
