package mtr.path;

import mtr.data.Rail;
import net.minecraft.util.math.BlockPos;

public class PathData {

	public final Rail rail;
	public final int dwellTime;
	public final int stopIndex;

	private final BlockPos startingPos;
	private final BlockPos endingPos;

	public PathData(Rail rail, int dwellTime, BlockPos startingPos, BlockPos endingPos, int stopIndex) {
		this.rail = rail;
		this.dwellTime = dwellTime;
		this.startingPos = startingPos;
		this.endingPos = endingPos;
		this.stopIndex = stopIndex;
	}

	public boolean isSameRail(PathData pathData) {
		return startingPos.equals(pathData.startingPos) && endingPos.equals(pathData.endingPos);
	}

	public boolean isOppositeRail(PathData pathData) {
		return startingPos.equals(pathData.endingPos) && endingPos.equals(pathData.startingPos);
	}
}
