package mtr.data;

public enum PIDSType {
	ARRIVAL_PROJECTOR(false, true, false),
	PIDS(true, false, true);

	public final boolean showTerminatingPlatforms;
	public final boolean showPlatformNumber;
	public final boolean showCarCount;

	PIDSType(boolean showTerminatingPlatforms, boolean showPlatformNumber, boolean showCarCount) {
		this.showTerminatingPlatforms = showTerminatingPlatforms;
		this.showPlatformNumber = showPlatformNumber;
		this.showCarCount = showCarCount;
	}
}
