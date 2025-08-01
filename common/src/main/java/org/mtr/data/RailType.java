package org.mtr.data;

import net.minecraft.block.MapColor;
import org.mtr.core.data.Rail;

public enum RailType implements IGui {
	WOODEN(20, MapColor.OAK_TAN, false, true, true, Rail.Shape.QUADRATIC),
	STONE(40, MapColor.STONE_GRAY, false, true, true, Rail.Shape.QUADRATIC),
	EMERALD(60, MapColor.EMERALD_GREEN, false, true, true, Rail.Shape.QUADRATIC),
	IRON(80, MapColor.IRON_GRAY, false, true, true, Rail.Shape.QUADRATIC),
	BRICKS(100, MapColor.BROWN, false, true, true, Rail.Shape.QUADRATIC),
	OBSIDIAN(120, MapColor.PURPLE, false, true, true, Rail.Shape.QUADRATIC),
	PRISMARINE(140, MapColor.CYAN, false, true, true, Rail.Shape.QUADRATIC),
	BLAZE(160, MapColor.ORANGE, false, true, true, Rail.Shape.QUADRATIC),
	QUARTZ(200, MapColor.OFF_WHITE, false, true, true, Rail.Shape.QUADRATIC),
	DIAMOND(300, MapColor.DIAMOND_BLUE, false, true, true, Rail.Shape.QUADRATIC),
	PLATFORM(80, MapColor.RED, true, false, true, Rail.Shape.QUADRATIC),
	SIDING(40, MapColor.YELLOW, true, false, true, Rail.Shape.QUADRATIC),
	TURN_BACK(80, MapColor.BLUE, false, false, true, Rail.Shape.QUADRATIC),
	CABLE_CAR(30, MapColor.WHITE, false, true, true, Rail.Shape.CABLE),
	CABLE_CAR_STATION(2, MapColor.WHITE, false, true, true, Rail.Shape.QUADRATIC),
	RUNWAY(300, MapColor.PALE_PURPLE, false, true, false, Rail.Shape.QUADRATIC),
	AIRPLANE_DUMMY(900, MapColor.BLACK, false, true, false, Rail.Shape.QUADRATIC);

	public final int speedLimit;
	public final int color;
	public final boolean isSavedRail;
	public final boolean canAccelerate;
	public final boolean hasSignal;
	public final Rail.Shape railShape;

	RailType(int speedLimit, MapColor mapColor, boolean isSavedRail, boolean canAccelerate, boolean hasSignal, Rail.Shape railShape) {
		this.speedLimit = speedLimit;
		color = mapColor.color | ARGB_BLACK;
		this.isSavedRail = isSavedRail;
		this.canAccelerate = canAccelerate;
		this.hasSignal = hasSignal;
		this.railShape = railShape;
	}

	public static int getRailColor(Rail rail) {
		if (rail.isPlatform()) {
			return PLATFORM.color;
		} else if (rail.isSiding()) {
			return SIDING.color;
		} else if (rail.canTurnBack()) {
			return TURN_BACK.color;
		} else if (rail.canConnectRemotely()) {
			return RUNWAY.color;
		} else {
			for (final RailType railType : values()) {
				if (railType.speedLimit == Math.max(rail.getSpeedLimitKilometersPerHour(false), rail.getSpeedLimitKilometersPerHour(true))) {
					return railType.color;
				}
			}
			return ARGB_BLACK;
		}
	}
}
