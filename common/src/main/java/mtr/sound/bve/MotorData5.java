package mtr.sound.bve;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MotorData5 extends MotorDataBase { // 5 for BVE5 and BVE6

    private final FloatSplines powerVolume;
    private final FloatSplines powerFrequency;
    private final FloatSplines brakeVolume;
    private final FloatSplines brakeFrequency;
    private final int soundCount;

    public MotorData5(ResourceManager manager, String baseName) {
        powerVolume = new FloatSplines(readResource(manager, new ResourceLocation(baseName + "/PowerVol.csv")));
        powerFrequency = new FloatSplines(readResource(manager, new ResourceLocation(baseName + "/PowerFreq.csv")));
        brakeVolume = new FloatSplines(readResource(manager, new ResourceLocation(baseName + "/BrakeVol.csv")));
        brakeFrequency = new FloatSplines(readResource(manager, new ResourceLocation(baseName + "/BrakeFreq.csv")));
        soundCount = Math.max(
                Math.max(powerVolume.data.size(), powerFrequency.data.size()),
                Math.max(brakeVolume.data.size(), brakeFrequency.data.size())
        );
    }

    private String readResource(ResourceManager manager, ResourceLocation location) {
        try {
            var resources = manager.getResources(location);
            if (resources.size() < 1) return "";
            InputStream iStream = resources.get(0).getInputStream();
            return IOUtils.toString(iStream, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            return "";
        }
    }

    @Override
    public int getSoundCount() {
        return soundCount;
    }

    @Override
    public float getPitch(int index, float speed, int accel) {
        return accel > 0 ? powerFrequency.getValue(index, speed) : brakeFrequency.getValue(index, speed);
    }

    @Override
    public float getVolume(int index, float speed, int accel) {
        return accel > 0 ? powerVolume.getValue(index, speed) : brakeVolume.getValue(index, speed);
    }

    public static class FloatSplines {

        public List<TreeMap<Float, Float>> data = new ArrayList<>();

        public FloatSplines(String textContent) {
            String[] lines = textContent.lines().toArray(String[]::new);
            for (final String line : lines) {
                final String lineTrim = line.trim().toLowerCase(Locale.ROOT);
                if (lineTrim.startsWith("#") || lineTrim.startsWith("//") || lineTrim.startsWith("bvets")) continue;
                String[] tokens = lineTrim.split(","); // Trailing entries automatically removed

                while (data.size() < tokens.length - 1) data.add(new TreeMap<>());
                float key = Float.parseFloat(tokens[0].trim());
                for (int i = 1; i < tokens.length; ++i) {
                    final String tokenTrim = tokens[i].trim();
                    if (tokenTrim.isEmpty()) continue;
                    data.get(i - 1).put(key, Float.parseFloat(tokenTrim));
                }
            }
        }

        public float getValue(int index, float key) {
            TreeMap<Float, Float> spline = data.get(index);
            if (spline.size() < 1) return 0F;
            var floorEntry = spline.floorEntry(key);
            var ceilingEntry = spline.ceilingEntry(key);
            if (floorEntry == null) {
                return ceilingEntry.getValue();
            } else if (ceilingEntry == null) {
                return floorEntry.getValue();
            } else {
                return floorEntry.getValue() + (ceilingEntry.getValue() - floorEntry.getValue()) *
                        ((key - floorEntry.getKey()) / (ceilingEntry.getKey() - floorEntry.getKey()));
            }
        }
    }
}
