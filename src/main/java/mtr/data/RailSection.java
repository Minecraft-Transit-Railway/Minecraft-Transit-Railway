package mtr.data;

import mods.railcraft.api.tracks.TrackKit;
import mods.railcraft.api.tracks.TrackType;
import net.minecraft.block.BlockRailBase;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class RailSection {

	public final BlockPos pos;
	public final TrackKit trackKit;
	public final TrackType trackType;
	public final BlockRailBase.EnumRailDirection trackDirection;

	public RailSection(BlockPos pos, TrackKit trackKit, TrackType trackType, BlockRailBase.EnumRailDirection trackDirection) {
		this.pos = pos;
		this.trackKit = trackKit;
		this.trackType = trackType;
		this.trackDirection = trackDirection;
	}

	@Override
	public String toString() {
		return "RailSection{" +
				"pos=" + pos +
				", trackKit=" + trackKit +
				", trackType=" + trackType +
				", trackDirection=" + trackDirection +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RailSection that = (RailSection) o;
		return Objects.equals(pos, that.pos) &&
				Objects.equals(trackKit, that.trackKit) &&
				Objects.equals(trackType, that.trackType) &&
				trackDirection == that.trackDirection;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pos, trackKit, trackType, trackDirection);
	}
}
