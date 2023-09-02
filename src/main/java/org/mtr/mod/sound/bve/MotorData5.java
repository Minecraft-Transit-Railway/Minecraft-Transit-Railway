package mtr.sound.bve;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class MotorData5 extends MotorDataBase { // 5 for BVE5 and BVE6

	private final FloatSplines powerVolume;
	private final FloatSplines powerFrequency;
	private final FloatSplines brakeVolume;
	private final FloatSplines brakeFrequency;
	private final int soundCount;

	public MotorData5(ResourceManager manager, String baseName) {
		powerVolume = new FloatSplines(BveTrainSoundConfig.readResource(manager, new ResourceLocation(baseName + "/powervol.csv")));
		powerFrequency = new FloatSplines(BveTrainSoundConfig.readResource(manager, new ResourceLocation(baseName + "/powerfreq.csv")));
		brakeVolume = new FloatSplines(BveTrainSoundConfig.readResource(manager, new ResourceLocation(baseName + "/brakevol.csv")));
		brakeFrequency = new FloatSplines(BveTrainSoundConfig.readResource(manager, new ResourceLocation(baseName + "/brakefreq.csv")));
		soundCount = Math.max(
				Math.max(powerVolume.data.size(), powerFrequency.data.size()),
				Math.max(brakeVolume.data.size(), brakeFrequency.data.size())
		);
	}

	@Override
	public int getSoundCount() {
		return soundCount;
	}

	@Override
	public float getPitch(int index, float speed, float power) {
		if (power == 0) {
			return 0;
		}
		return power > 0 ? powerFrequency.getValue(index, speed) : brakeFrequency.getValue(index, speed);
	}

	@Override
	public float getVolume(int index, float speed, float power) {
		if (power == 0) {
			return 0;
		}
		return (power > 0 ? powerVolume.getValue(index, speed) : brakeVolume.getValue(index, speed)) * Math.abs(power);
	}

	public static class FloatSplines {

		public List<TreeMap<Float, Float>> data = new ArrayList<>();

		public FloatSplines(String textContent) {
			String[] lines = textContent.split("[\\r\\n]+");

			for (final String line : lines) {
				final String lineTrim = line.trim().toLowerCase(Locale.ENGLISH);
				if (StringUtils.isEmpty(lineTrim)) {
					continue;
				}
				if (lineTrim.startsWith("#") || lineTrim.startsWith("//") || lineTrim.startsWith("bvets")) {
					continue;
				}
				String[] tokens = lineTrim.split(","); // Trailing entries automatically removed

				while (data.size() < tokens.length - 1) {
					data.add(new TreeMap<>());
				}
				float key = Float.parseFloat(tokens[0].trim());
				for (int i = 1; i < tokens.length; ++i) {
					final String tokenTrim = tokens[i].trim();
					if (tokenTrim.isEmpty()) {
						continue;
					}
					data.get(i - 1).put(key, Float.parseFloat(tokenTrim));
				}
			}
		}

		public float getValue(int index, float key) {
			TreeMap<Float, Float> spline = data.get(index);
			if (spline.size() < 1) {
				return 0F;
			}
			Map.Entry<Float, Float> floorEntry = spline.floorEntry(key);
			Map.Entry<Float, Float> ceilingEntry = spline.ceilingEntry(key);
			if (floorEntry == null) {
				return ceilingEntry.getValue();
			} else if (ceilingEntry == null) {
				return floorEntry.getValue();
			} else if (Objects.equals(floorEntry.getKey(), ceilingEntry.getKey())) {
				return floorEntry.getValue();
			} else {
				return floorEntry.getValue() + (ceilingEntry.getValue() - floorEntry.getValue()) *
						((key - floorEntry.getKey()) / (ceilingEntry.getKey() - floorEntry.getKey()));
			}
		}
	}
}
