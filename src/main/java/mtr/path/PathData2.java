package mtr.path;

import mtr.data.Rail;

public class PathData2 {

	public final Rail rail;
	public final float distance;
	public final int dwellTime;

	public PathData2(Rail rail, float distance, int dwellTime) {
		this.rail = rail;
		this.distance = distance;
		this.dwellTime = dwellTime;
	}
}
