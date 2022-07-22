package mtr.sound.bve;

import mtr.mappings.Utilities;
import mtr.mappings.UtilitiesClient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class BveTrainSoundConfig {

	public final String baseName;
	public final String audioBaseName;
	public final ConfigFile soundCfg;
	public final MotorDataBase motorData;

	public BveTrainSoundConfig(ResourceManager manager, String baseName) {
		ResourceLocation baseLocation = new ResourceLocation(baseName.contains(":") ? baseName : "mtr:" + baseName);

		this.baseName = baseLocation.toString();
		final String configBaseName = baseLocation.getNamespace() + ":sounds/" + baseLocation.getPath();
		audioBaseName = baseLocation.getNamespace() + ":" + baseLocation.getPath() + "_";
		soundCfg = new ConfigFile(readResource(manager, new ResourceLocation(configBaseName + "/sound.cfg")), this);
		if (soundCfg.motorNoiseDataType == 4) {
			motorData = new MotorData4(manager, configBaseName);
		} else {
			motorData = new MotorData5(manager, configBaseName);
		}
	}

	public static String readResource(ResourceManager manager, ResourceLocation location) {
		try {
			final List<Resource> resources = UtilitiesClient.getResources(manager, location);
			if (resources.size() < 1) {
				return "";
			}
			return IOUtils.toString(Utilities.getInputStream(resources.get(0)), StandardCharsets.UTF_8);
		} catch (Exception e) {
			return "";
		}
	}
}
