package mtr.data;

public enum PIDSType {
    ARRIVAL_PROJECTOR(true),
    PIDS(true);

    public final boolean showAllPlatforms;

    PIDSType(boolean showAllPlatforms) {
        this.showAllPlatforms = showAllPlatforms;
    }
}
