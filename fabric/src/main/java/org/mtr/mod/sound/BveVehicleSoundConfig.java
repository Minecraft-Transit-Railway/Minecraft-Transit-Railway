package org.mtr.mod.sound;

import org.apache.commons.io.IOUtils;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mod.Init;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BveVehicleSoundConfig {

	public final String baseName;
	public final String audioBaseName;
	public final BveConfigFile config;
	public final BveMotorDataBase motorData;

	public BveVehicleSoundConfig(String baseName) {
		final Identifier baseLocation = baseName.contains(":") ? new Identifier(baseName) : new Identifier(Init.MOD_ID, baseName);

		this.baseName = baseLocation.toString();
		final String configBaseName = baseLocation.getNamespace() + ":sounds/" + baseLocation.getPath();
		audioBaseName = baseLocation.getNamespace() + ":" + baseLocation.getPath() + "_";
		config = new BveConfigFile(readResource(new Identifier(configBaseName + "/sound.cfg")), this);
		if (config.motorNoiseDataType == 4) {
			motorData = new BveMotorData4(configBaseName);
		} else {
			motorData = new BveMotorData5(configBaseName);
		}
	}

	public static String readResource(Identifier location) {
		final String[] result = {""};
		ResourceManagerHelper.readResource(location, inputStream -> {
			try {
				result[0] = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			} catch (IOException ignored) {
			}
		});
		return result[0];
	}
}
