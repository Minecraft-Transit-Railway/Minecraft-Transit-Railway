package mtr.sound.bve;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class ConfigFile {

    public SoundEvent[] run = new SoundEvent[1];
    public SoundEvent[] flange = new SoundEvent[1];
    public SoundEvent[] motor = new SoundEvent[40];
    public SoundEvent[] joint = new SoundEvent[1];

    public SoundEvent air;
    public SoundEvent airZero;
    public SoundEvent airHigh;
    public SoundEvent brakeEmergency;

    public SoundEvent doorOpen;
    public SoundEvent doorClose;

    public SoundEvent brakeHandleApply;
    public SoundEvent brakeHandleRelease;

    public SoundEvent compressorAttack;
    public SoundEvent compressorLoop;
    public SoundEvent compressorRelease;

    public SoundEvent noise;
    public SoundEvent shoe;

    public int motorNoiseDataType = 5; // 4 or 5

    public float motorVolumeMultiply = 1F;
    public float breakerDelay = 0.5F; // sec
    public float regenerationLimit = 8F / 3.6F; // m/s

    public int mrPressMin = 700; // kPa
    public int mrPressMax = 800; // kPa
    public float mrCompressorSpeed = 5; // kPa/s
    public float mrServiceBrakeReduce = 5; // kPa each time

    public float doorCloseSoundLength = 2F;

    public ConfigFile(String textContent, BveTrainSoundConfig config) {
        final String[] lines = textContent.split("[\\r\\n]+");
        String section = "";
        for (final String line : lines) {
            final String trimLine = line.trim();
            if (StringUtils.isEmpty(trimLine)) continue;
            if (trimLine.startsWith("#") || trimLine.startsWith("//") || trimLine.startsWith("bvets")) continue;

            if (trimLine.contains("=")) {
                final String[] tokens = trimLine.split("=");
                if (tokens.length != 2) continue;
                final String key = tokens[0].trim().replace(" ", "").toLowerCase(Locale.ROOT);
                final String value = tokens[1].trim().toLowerCase(Locale.ROOT).replace(".wav", "");
                if (StringUtils.isEmpty(value)) continue;
                final SoundEvent valueAsSoundEvent = new SoundEvent(new ResourceLocation(config.audioBaseName + value));
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
                        }
                        break;
                    case "run":
                    case "rolling":
                        if (Integer.parseInt(key) >= this.run.length) break;
                        this.run[Integer.parseInt(key)] = valueAsSoundEvent;
                        break;
                    case "flange":
                        if (Integer.parseInt(key) >= this.flange.length) break;
                        this.flange[Integer.parseInt(key)] = valueAsSoundEvent;
                        break;
                    case "motor":
                        if (Integer.parseInt(key) >= this.motor.length) break;
                        this.motor[Integer.parseInt(key)] = valueAsSoundEvent;
                        break;
                    case "joint":
                    case "switch":
                        if (Integer.parseInt(key) >= this.joint.length) break;
                        this.joint[Integer.parseInt(key)] = valueAsSoundEvent;
                        break;
                    case "brake":
                        switch (key) {
                            case "bcrelease":
                                this.air = valueAsSoundEvent;
                                break;
                            case "bcreleasefull":
                                this.airZero = valueAsSoundEvent;
                                break;
                            case "bcreleasehigh":
                                this.airHigh = valueAsSoundEvent;
                                break;
                            case "emergency":
                                this.brakeEmergency = valueAsSoundEvent;
                                break;
                        }
                        break;
                    case "door":
                        switch (key) {
                            case "open":
                            case "openleft":
                            case "openright":
                                this.doorOpen = valueAsSoundEvent;
                                break;
                            case "close":
                            case "closeleft":
                            case "closeright":
                                this.doorClose = valueAsSoundEvent;
                                break;
                        }
                    case "brakehandle":
                        switch (key) {
                            case "apply":
                                this.brakeHandleApply = valueAsSoundEvent;
                                break;
                            case "release":
                                this.brakeHandleRelease = valueAsSoundEvent;
                                break;
                        }
                        break;
                    case "compressor":
                        switch (key) {
                            case "attack":
                                this.compressorAttack = valueAsSoundEvent;
                                break;
                            case "loop":
                                this.compressorLoop = valueAsSoundEvent;
                                break;
                            case "release":
                                this.compressorRelease = valueAsSoundEvent;
                                break;
                        }
                    case "others":
                        switch (key) {
                            case "noise":
                                this.noise = valueAsSoundEvent;
                                break;
                            case "shoe":
                                this.shoe = valueAsSoundEvent;
                                break;
                        }
                }
            } else if (trimLine.startsWith("[") && trimLine.endsWith("]")) {
                section = trimLine.substring(1, trimLine.length() - 1).trim().replace(" ", "").toLowerCase(Locale.ROOT);
            }
        }
        if (airZero == null) airZero = air;
        if (airHigh == null) airHigh = air;
    }
}
