package mtr.data;

public enum PIDSType {
	ARRIVAL_PROJECTOR(true, true, false),
	PIDS(true, false, true);

	public final boolean showAllPlatforms;
	public final boolean showPlatformNumber;
	public final boolean showCarNumber;

	PIDSType(boolean showAllPlatforms, boolean showPlatformNumber, boolean showCarNumber) {
		this.showAllPlatforms = showAllPlatforms;
		this.showPlatformNumber = showPlatformNumber;
		this.showCarNumber = showCarNumber;
	}
}
