package mtr.path;

import mtr.data.Rail;
import mtr.data.RailwayData;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class PathData {

	public final Rail rail;
	public final long savedRailBaseId;
	public final int dwellTime;
	public final int stopIndex;

	private final BlockPos startingPos;
	private final BlockPos endingPos;

	public PathData(Rail rail, long savedRailBaseId, int dwellTime, BlockPos startingPos, BlockPos endingPos, int stopIndex) {
		this.rail = rail;
		this.savedRailBaseId = savedRailBaseId;
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

	public Rail getOppositeRail(Map<BlockPos, Map<BlockPos, Rail>> rails) {
		return RailwayData.containsRail(rails, endingPos, startingPos) ? rails.get(endingPos).get(startingPos) : null;
	}
}
