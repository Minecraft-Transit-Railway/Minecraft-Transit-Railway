package mtr.data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Map;

public abstract class RailwayDataModuleBase {

	protected final RailwayData railwayData;
	protected final Level world;
	protected final Map<BlockPos, Map<BlockPos, Rail>> rails;

	public RailwayDataModuleBase(RailwayData railwayData, Level world, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		this.railwayData = railwayData;
		this.world = world;
		this.rails = rails;
	}
}
