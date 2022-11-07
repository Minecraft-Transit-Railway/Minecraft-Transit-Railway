package mtr.data;

public enum PIDSType {
	ARRIVAL_PROJECTOR(true, true, false, true),
	PIDS(true, false, true, false);

	public final boolean showAllPlatforms;
	public final boolean showPlatformNumber;
	public final boolean showCarNumber;
	public final boolean hasPageNumber;

	PIDSType(boolean showAllPlatforms, boolean showPlatformNumber, boolean showCarNumber, boolean hasPageNumber) {
		this.showAllPlatforms = showAllPlatforms;
		this.showPlatformNumber = showPlatformNumber;
		this.showCarNumber = showCarNumber;
		this.hasPageNumber = hasPageNumber;
	}
}
