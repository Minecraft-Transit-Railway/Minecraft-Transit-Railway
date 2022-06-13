package mtr.sound.bve;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class SoundCfg {

    public String[] run = new String[1];
    public String[] flange = new String[1];
    public String[] motor = new String[32];
    public String[] joint = new String[1];

    public String brakeBcRelease;
    public String brakeBcReleaseFull;
    public String brakeBcReleaseHigh;
    public String brakeEmergency;

    public String doorOpen;
    public String doorClose;

    public String brakeHandleApply;
    public String brakeHandleApplyFast;
    public String brakeHandleRelease;
    public String brakeHandleReleaseFast;
    public String brakeHandleMin;
    public String brakeHandleMax;

    public String noise;
    public String shoe;

    public SoundCfg(String textContent) {
        final String[] lines = textContent.split("[\\r\\n]+");
        String section = "";
        for (final String line : lines) {
            final String trimLine = line.trim();
            if (trimLine.contains("=")) {
                final String[] tokens = trimLine.split("=");
                final String key = tokens[0].trim().replace(" ", "").toLowerCase(Locale.ROOT);
                final String value = tokens[1].trim();
                switch (section.toLowerCase(Locale.ROOT)) {
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
                                this.brakeBcRelease = value;
                                break;
                            case "bcreleasefull":
                                this.brakeBcReleaseFull = value;
                                break;
                            case "bcreleasehigh":
                                this.brakeBcReleaseHigh = value;
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
                            case "applyfast":
                                this.brakeHandleApplyFast = value;
                                break;
                            case "release":
                                this.brakeHandleRelease = value;
                                break;
                            case "releasefast":
                                this.brakeHandleReleaseFast = value;
                                break;
                            case "min":
                                this.brakeHandleMin = value;
                                break;
                            case "max":
                                this.brakeHandleMax = value;
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
                section = trimLine.substring(1, trimLine.length() - 2).trim().toLowerCase(Locale.ROOT);
            }
        }
        if (StringUtils.isEmpty(brakeBcReleaseFull)) brakeBcReleaseFull = brakeBcRelease;
        if (StringUtils.isEmpty(brakeBcReleaseHigh)) brakeBcReleaseHigh = brakeBcRelease;
        if (StringUtils.isEmpty(brakeHandleApplyFast)) brakeHandleApplyFast = brakeHandleApply;
        if (StringUtils.isEmpty(brakeHandleReleaseFast)) brakeHandleReleaseFast = brakeHandleRelease;
    }
}
