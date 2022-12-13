package mtr.sound.bve;

import mtr.mappings.RegistryUtilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class ConfigFile {

	public final SoundEvent[] run = new SoundEvent[1];
	public final SoundEvent[] flange = new SoundEvent[1];
	public final SoundEvent[] motor = new SoundEvent[40];
	public final SoundEvent[] joint = new SoundEvent[1];

	public final SoundEvent air;
	public final SoundEvent airZero;
	public final SoundEvent airHigh;
	public final SoundEvent brakeEmergency;

	public final SoundEvent doorOpen;
	public final SoundEvent doorClose;

	public final SoundEvent brakeHandleApply;
	public final SoundEvent brakeHandleRelease;

	public final SoundEvent compressorAttack;
	public final SoundEvent compressorLoop;
	public final SoundEvent compressorRelease;

	public final SoundEvent noise;
	public final SoundEvent shoe;

	public final int motorNoiseDataType;

	public final float motorVolumeMultiply;

	public final float breakerDelay;
	public final float regenerationLimit;

	public final float motorOutputAtCoast;

	public final int mrPressMin = 700; // kPa
	public final int mrPressMax = 800; // kPa
	public final float mrCompressorSpeed = 5; // kPa/s
	public final float mrServiceBrakeReduce = 5; // kPa each time

	public final float doorCloseSoundLength;

	public ConfigFile(String textContent, BveTrainSoundConfig config) {
		final String[] lines = textContent.split("[\\r\\n]+");
		String section = "";

		SoundEvent air = null;
		SoundEvent airZero = null;
		SoundEvent airHigh = null;
		SoundEvent brakeEmergency = null;

		SoundEvent doorOpen = null;
		SoundEvent doorClose = null;

		SoundEvent brakeHandleApply = null;
		SoundEvent brakeHandleRelease = null;

		SoundEvent compressorAttack = null;
		SoundEvent compressorLoop = null;
		SoundEvent compressorRelease = null;

		SoundEvent noise = null;
		SoundEvent shoe = null;

		int motorNoiseDataType = 5; // 4 or 5
		float motorVolumeMultiply = 1;
		float breakerDelay = 0;
		float regenerationLimit = 0; // m/s
		float motorOutputAtCoast = 0.4F;
		float doorCloseSoundLength = 1;

		for (final String line : lines) {
			final String trimLine = line.trim().replaceAll("\\s*(;|#|//).+", "");
			if (StringUtils.isEmpty(trimLine)) {
				continue;
			}

			if (trimLine.contains("=")) {
				final String[] tokens = trimLine.split("=");
				if (tokens.length != 2) {
					continue;
				}

				final String key = tokens[0].trim().toLowerCase(Locale.ENGLISH).replaceAll("\\s", "");
				final String value = tokens[1].trim().toLowerCase(Locale.ENGLISH).replace("\\", "/").replaceAll("\\.wav|\\s|.+/", "");
				if (StringUtils.isEmpty(value)) {
					continue;
				}

				final SoundEvent valueAsSoundEvent = RegistryUtilities.createSoundEvent(new ResourceLocation(config.audioBaseName + value));
				switch (section) {
					case "mtr":
						switch (key) {
							case "motornoisedatatype":
								motorNoiseDataType = Integer.parseInt(value);
								break;
							case "motorvolumemultiply":
								motorVolumeMultiply = Float.parseFloat(value);
								break;
							case "doorclosesoundlength":
								doorCloseSoundLength = Float.parseFloat(value);
								break;
							case "breakerdelay":
								breakerDelay = Float.parseFloat(value);
								break;
							case "regenerationlimit":
								regenerationLimit = Float.parseFloat(value) / 3.6F;
								break;
							case "motoroutputatcoast":
								motorOutputAtCoast = Float.parseFloat(value);
								break;
						}
						break;
					case "run":
					case "rolling":
						if (Integer.parseInt(key) >= run.length) {
							break;
						}
						run[Integer.parseInt(key)] = valueAsSoundEvent;
						break;
					case "flange":
						if (Integer.parseInt(key) >= flange.length) {
							break;
						}
						flange[Integer.parseInt(key)] = valueAsSoundEvent;
						break;
					case "motor":
						if (Integer.parseInt(key) >= motor.length) {
							break;
						}
						motor[Integer.parseInt(key)] = valueAsSoundEvent;
						break;
					case "joint":
					case "switch":
						if (Integer.parseInt(key) >= joint.length) {
							break;
						}
						joint[Integer.parseInt(key)] = valueAsSoundEvent;
						break;
					case "brake":
						switch (key) {
							case "bcrelease":
								air = valueAsSoundEvent;
								break;
							case "bcreleasefull":
								airZero = valueAsSoundEvent;
								break;
							case "bcreleasehigh":
								airHigh = valueAsSoundEvent;
								break;
							case "emergency":
								brakeEmergency = valueAsSoundEvent;
								break;
						}
						break;
					case "door":
						switch (key) {
							case "open":
							case "openleft":
							case "openright":
								doorOpen = valueAsSoundEvent;
								break;
							case "close":
							case "closeleft":
							case "closeright":
								doorClose = valueAsSoundEvent;
								break;
						}
					case "brakehandle":
						switch (key) {
							case "apply":
								brakeHandleApply = valueAsSoundEvent;
								break;
							case "release":
								brakeHandleRelease = valueAsSoundEvent;
								break;
						}
						break;
					case "compressor":
						switch (key) {
							case "attack":
								compressorAttack = valueAsSoundEvent;
								break;
							case "loop":
								compressorLoop = valueAsSoundEvent;
								break;
							case "release":
								compressorRelease = valueAsSoundEvent;
								break;
						}
					case "others":
						switch (key) {
							case "noise":
								noise = valueAsSoundEvent;
								break;
							case "shoe":
								shoe = valueAsSoundEvent;
								break;
						}
				}
			} else if (trimLine.startsWith("[") && trimLine.endsWith("]")) {
				section = trimLine.substring(1, trimLine.length() - 1).trim().replace(" ", "").toLowerCase(Locale.ENGLISH);
			}
		}

		if (airZero == null) {
			airZero = air;
		}

		if (airHigh == null) {
			airHigh = air;
		}

		this.air = air;
		this.airZero = airZero;
		this.airHigh = airHigh;
		this.brakeEmergency = brakeEmergency;
		this.doorOpen = doorOpen;
		this.doorClose = doorClose;
		this.brakeHandleApply = brakeHandleApply;
		this.brakeHandleRelease = brakeHandleRelease;
		this.compressorAttack = compressorAttack;
		this.compressorLoop = compressorLoop;
		this.compressorRelease = compressorRelease;
		this.noise = noise;
		this.shoe = shoe;
		this.motorNoiseDataType = motorNoiseDataType;
		this.motorVolumeMultiply = motorVolumeMultiply;
		this.breakerDelay = breakerDelay;
		this.regenerationLimit = regenerationLimit;
		this.motorOutputAtCoast = motorOutputAtCoast;
		this.doorCloseSoundLength = doorCloseSoundLength;
	}
}
