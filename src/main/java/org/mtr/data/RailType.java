package org.mtr.data;

import net.minecraft.world.level.material.MapColor;
import org.mtr.core.data.Rail;

public enum RailType implements IGui {
	WOODEN(20, MapColor.WOOD, false, true, true, Rail.Shape.QUADRATIC),
	STONE(40, MapColor.STONE, false, true, true, Rail.Shape.QUADRATIC),
	EMERALD(60, MapColor.EMERALD, false, true, true, Rail.Shape.QUADRATIC),
	IRON(80, MapColor.METAL, false, true, true, Rail.Shape.QUADRATIC),
	BRICKS(100, MapColor.COLOR_BROWN, false, true, true, Rail.Shape.QUADRATIC),
	OBSIDIAN(120, MapColor.COLOR_PURPLE, false, true, true, Rail.Shape.QUADRATIC),
	PRISMARINE(140, MapColor.COLOR_CYAN, false, true, true, Rail.Shape.QUADRATIC),
	BLAZE(160, MapColor.COLOR_ORANGE, false, true, true, Rail.Shape.QUADRATIC),
	QUARTZ(200, MapColor.QUARTZ, false, true, true, Rail.Shape.QUADRATIC),
	DIAMOND(300, MapColor.DIAMOND, false, true, true, Rail.Shape.QUADRATIC),
	PLATFORM(80, MapColor.COLOR_RED, true, false, true, Rail.Shape.QUADRATIC),
	SIDING(40, MapColor.COLOR_YELLOW, true, false, true, Rail.Shape.QUADRATIC),
	TURN_BACK(80, MapColor.COLOR_BLUE, false, false, true, Rail.Shape.QUADRATIC),
	CABLE_CAR(30, MapColor.SNOW, false, true, true, Rail.Shape.CABLE),
	CABLE_CAR_STATION(2, MapColor.SNOW, false, true, true, Rail.Shape.QUADRATIC),
	RUNWAY(300, MapColor.ICE, false, true, false, Rail.Shape.QUADRATIC),
	AIRPLANE_DUMMY(900, MapColor.COLOR_BLACK, false, true, false, Rail.Shape.QUADRATIC);

	public final int speedLimit;
	public final int color;
	public final boolean isSavedRail;
	public final boolean canAccelerate;
	public final boolean hasSignal;
	public final Rail.Shape railShape;

	RailType(int speedLimit, MapColor mapColor, boolean isSavedRail, boolean canAccelerate, boolean hasSignal, Rail.Shape railShape) {
		this.speedLimit = speedLimit;
		color = mapColor.col | ARGB_BLACK;
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
