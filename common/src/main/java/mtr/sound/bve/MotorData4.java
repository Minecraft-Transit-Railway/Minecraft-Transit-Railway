package mtr.sound.bve;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class MotorData4 extends MotorDataBase { // 4 for BVE4 and OpenBVE

	private final Channel[] channels = new Channel[4];
	private final int soundCount;

	public MotorData4(ResourceManager manager, String baseName) {
		for (int i = 0; i < channels.length; ++i) {
			channels[i] = new Channel();
		}

		String textContent = BveTrainSoundConfig.readResource(manager, new ResourceLocation(baseName + "/train.dat"));
		String[] lines = textContent.split("[\\r\\n]+");
		String section = "";
		for (final String line : lines) {
			final String lineTrim = line.trim().toLowerCase();
			if (StringUtils.isEmpty(lineTrim)) {
				continue;
			}
			if (lineTrim.startsWith("#")) {
				section = lineTrim.substring(1).trim().toLowerCase();
				continue;
			}
			switch (section) {
				case "motor_p1":
				case "motor_p2":
				case "motor_b1":
				case "motor_b2":
					int listIndex = (section.charAt(6) == 'p' ? 0 : 2) + (section.charAt(7) == '1' ? 0 : 1);
					String[] tokens = lineTrim.split(",");
					channels[listIndex].soundIds.add(Integer.parseInt(tokens[0]));
					channels[listIndex].pitches.add(Float.parseFloat(tokens[1]) / 100F);
					channels[listIndex].volumes.add(Float.parseFloat(tokens[2]) / 128F);
					channels[listIndex].maxSoundId = Math.max(channels[listIndex].maxSoundId, Integer.parseInt(tokens[0]));
					channels[listIndex].maxEntryId++;
					break;
			}
		}

		int maxSoundId = -1;
		for (final Channel channel : channels) {
			maxSoundId = Math.max(maxSoundId, channel.maxSoundId);
		}
		soundCount = maxSoundId + 1;
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
		int offset = power > 0 ? 0 : 2;
		int entryIndex = (int) (speed / 0.2F);
		if (index == getSafe(channels[offset].soundIds, Math.min(channels[offset].maxEntryId, entryIndex))) {
			return getSafe(channels[offset].pitches, entryIndex);
		}
		if (index == getSafe(channels[offset + 1].soundIds, Math.min(channels[offset + 1].maxEntryId, entryIndex))) {
			return getSafe(channels[offset + 1].pitches, entryIndex);
		}
		return 0;
	}

	@Override
	public float getVolume(int index, float speed, float power) {
		if (power == 0) {
			return 0;
		}
		int offset = power > 0 ? 0 : 2;
		int entryIndex = (int) (speed / 0.2F);
		if (index == getSafe(channels[offset].soundIds, Math.min(channels[offset].maxEntryId, entryIndex))) {
			return getSafe(channels[offset].volumes, entryIndex) * Math.abs(power);
		}
		if (index == getSafe(channels[offset + 1].soundIds, Math.min(channels[offset + 1].maxEntryId, entryIndex))) {
			return getSafe(channels[offset + 1].volumes, entryIndex) * Math.abs(power);
		}
		return 0;
	}

	private static <T> T getSafe(ArrayList<T> list, int index) {
		return list.get(Mth.clamp(index, 0, list.size() - 1));
	}

	public static class Channel {
		public ArrayList<Integer> soundIds = new ArrayList<>();
		public ArrayList<Float> pitches = new ArrayList<>();
		public ArrayList<Float> volumes = new ArrayList<>();
		public int maxEntryId = -1;
		public int maxSoundId = -1;
	}
}
