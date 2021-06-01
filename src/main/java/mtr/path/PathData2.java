package mtr.path;

import mtr.data.Rail;
import net.minecraft.util.math.BlockPos;

public class PathData2 {

	public final Rail rail;
	public final int dwellTime;

	private final BlockPos startingPos;
	private final BlockPos endingPos;

	public PathData2(Rail rail, int dwellTime, BlockPos startingPos, BlockPos endingPos) {
		this.rail = rail;
		this.dwellTime = dwellTime;
		this.startingPos = startingPos;
		this.endingPos = endingPos;
	}

	public boolean isEqual(PathData2 pathData) {
		return startingPos.equals(pathData.startingPos) && endingPos.equals(pathData.endingPos);
	}

	public boolean isOpposite(PathData2 pathData) {
		return startingPos.equals(pathData.endingPos) && endingPos.equals(pathData.startingPos);
	}
}
