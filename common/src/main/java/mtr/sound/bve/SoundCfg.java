package mtr.sound.bve;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class SoundCfg {

    public String[] run = new String[1];
    public String[] flange = new String[1];
    public String[] motor = new String[40];
    public String[] joint = new String[1];

    public String air;
    public String airZero;
    public String airHigh;
    public String brakeEmergency;

    public String doorOpen;
    public String doorClose;

    public String brakeHandleApply;
    public String brakeHandleRelease;

    public String noise;
    public String shoe;

    public float doorCloseSoundLength = 2F;
    public float breakerDelay = 0.5F;
    public float regenerationLimit = 8F / 3.6F;

    public SoundCfg(String textContent) {
        final String[] lines = textContent.split("[\\r\\n]+");
        String section = "";
        for (final String line : lines) {
            final String trimLine = line.trim();
            if (StringUtils.isEmpty(trimLine)) continue;
            if (trimLine.startsWith("#") || trimLine.startsWith("//") || trimLine.startsWith("bvets")) continue;

            if (trimLine.contains("=")) {
                final String[] tokens = trimLine.split("=");
                final String key = tokens[0].trim().replace(" ", "").toLowerCase(Locale.ROOT);
                final String value = tokens[1].trim().toLowerCase(Locale.ROOT).replace(".wav", "");
                switch (section) {
                    case "mtr":
                        switch (key) {
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
                        this.run[Integer.parseInt(key)] = value;
                        break;
                    case "flange":
                        if (Integer.parseInt(key) >= this.flange.length) break;
                        this.flange[Integer.parseInt(key)] = value;
                        break;
                    case "motor":
                        if (Integer.parseInt(key) >= this.motor.length) break;
                        this.motor[Integer.parseInt(key)] = value;
                        break;
                    case "joint":
                    case "switch":
                        if (Integer.parseInt(key) >= this.joint.length) break;
                        this.joint[Integer.parseInt(key)] = value;
                        break;
                    case "brake":
                        switch (key) {
                            case "bcrelease":
                                this.air = value;
                                break;
                            case "bcreleasefull":
                                this.airZero = value;
                                break;
                            case "bcreleasehigh":
                                this.airHigh = value;
                                break;
                            case "emergency":
                                this.brakeEmergency = value;
                                break;
                        }
                        break;
                    case "door":
                        switch (key) {
                            case "open":
                            case "openleft":
                            case "openright":
                                this.doorOpen = value;
                                break;
                            case "close":
                            case "closeleft":
                            case "closeright":
                                this.doorClose = value;
                                break;
                        }
                    case "brakehandle":
                        switch (key) {
                            case "apply":
                                this.brakeHandleApply = value;
                                break;
                            case "release":
                                this.brakeHandleRelease = value;
                                break;
                        }
                        break;
                    case "others":
                        switch (key) {
                            case "noise":
                                this.noise = value;
                                break;
                            case "shoe":
                                this.shoe = value;
                                break;
                        }
                }
            } else if (trimLine.startsWith("[") && trimLine.endsWith("]")) {
                section = trimLine.substring(1, trimLine.length() - 1).trim().toLowerCase(Locale.ROOT);
            }
        }
        if (StringUtils.isEmpty(airZero)) airZero = air;
        if (StringUtils.isEmpty(airHigh)) airHigh = air;
    }
}
