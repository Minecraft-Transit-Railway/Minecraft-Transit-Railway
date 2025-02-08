package org.mtr.mod.data;

import org.mtr.core.data.Rail;
import org.mtr.mapping.holder.MapColor;

public enum RailType implements IGui {
	WOODEN(20, MapColor.getOakTanMapped(), false, true, true, Rail.Shape.QUADRATIC),
	STONE(40, MapColor.getStoneGrayMapped(), false, true, true, Rail.Shape.QUADRATIC),
	EMERALD(60, MapColor.getEmeraldGreenMapped(), false, true, true, Rail.Shape.QUADRATIC),
	IRON(80, MapColor.getIronGrayMapped(), false, true, true, Rail.Shape.QUADRATIC),
	SMELTEDIRON(90, MapColor.getDiamondBlueMapped(), false, true, true, Rail.Shape.QUADRATIC),
	SOLIDIRON(100, MapColor.getDiamondBlueMapped(), false, true, true, Rail.Shape.QUADRATIC),
	SLOWOBSIDIAN(110, MapColor.getDiamondBlueMapped(), false, true, true, Rail.Shape.QUADRATIC),
	OBSIDIAN(120, MapColor.getPurpleMapped(), false, true, true, Rail.Shape.QUADRATIC),
	BLAZE(160, MapColor.getOrangeMapped(), false, true, true, Rail.Shape.QUADRATIC),
	QUARTZ(200, MapColor.getOffWhiteMapped(), false, true, true, Rail.Shape.QUADRATIC),
	DIAMOND(300, MapColor.getDiamondBlueMapped(), false, true, true, Rail.Shape.QUADRATIC),
	PLATFORM(80, MapColor.getRedMapped(), true, false, true, Rail.Shape.QUADRATIC),
	SIDING(40, MapColor.getYellowMapped(), true, false, true, Rail.Shape.QUADRATIC),
	TURN_BACK(80, MapColor.getBlueMapped(), false, false, true, Rail.Shape.QUADRATIC),
	CABLE_CAR(30, MapColor.getWhiteMapped(), false, true, true, Rail.Shape.CABLE),
	CABLE_CAR_STATION(2, MapColor.getWhiteMapped(), false, true, true, Rail.Shape.QUADRATIC),
	RUNWAY(300, MapColor.getPalePurpleMapped(), false, true, false, Rail.Shape.QUADRATIC),
	AIRPLANE_DUMMY(900, MapColor.getBlackMapped(), false, true, false, Rail.Shape.QUADRATIC);

	public final int speedLimit;
	public final int color;
	public final boolean isSavedRail;
	public final boolean canAccelerate;
	public final boolean hasSignal;
	public final Rail.Shape railShape;

	RailType(int speedLimit, MapColor mapColor, boolean isSavedRail, boolean canAccelerate, boolean hasSignal, Rail.Shape railShape) {
		this.speedLimit = speedLimit;
		color = mapColor.getColorMapped() | ARGB_BLACK;
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
