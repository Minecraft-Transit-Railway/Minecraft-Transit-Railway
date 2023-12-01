package org.mtr.mod.data;

import org.mtr.core.data.Rail;
import org.mtr.mapping.holder.MapColor;

public enum RailType implements IGui {
	WOODEN(20, MapColor.getOakTanMapped(), false, true, true, Rail.Shape.CURVE),
	STONE(40, MapColor.getStoneGrayMapped(), false, true, true, Rail.Shape.CURVE),
	EMERALD(60, MapColor.getEmeraldGreenMapped(), false, true, true, Rail.Shape.CURVE),
	IRON(80, MapColor.getIronGrayMapped(), false, true, true, Rail.Shape.CURVE),
	OBSIDIAN(120, MapColor.getPurpleMapped(), false, true, true, Rail.Shape.CURVE),
	BLAZE(160, MapColor.getOrangeMapped(), false, true, true, Rail.Shape.CURVE),
	QUARTZ(200, MapColor.getOffWhiteMapped(), false, true, true, Rail.Shape.CURVE),
	ICE(250, MapColor.getOffLightBlueMapped(), false, true, true, Rail.Shape.CURVE),
	DIAMOND(300, MapColor.getDiamondBlueMapped(), false, true, true, Rail.Shape.CURVE),
        NETHER(320, MapColor.getDarkRedMapped(), false, true, true, Rail.Shape.CURVE),
	PLATFORM(80, MapColor.getRedMapped(), true, false, true, Rail.Shape.CURVE),
	SIDING(40, MapColor.getYellowMapped(), true, false, true, Rail.Shape.CURVE),
	TURN_BACK(80, MapColor.getBlueMapped(), false, false, true, Rail.Shape.CURVE),
	CABLE_CAR(30, MapColor.getWhiteMapped(), false, true, true, Rail.Shape.STRAIGHT),
	CABLE_CAR_STATION(2, MapColor.getWhiteMapped(), false, true, true, Rail.Shape.CURVE),
	RUNWAY(300, MapColor.getPalePurpleMapped(), false, true, false, Rail.Shape.CURVE),
	AIRPLANE_DUMMY(900, MapColor.getBlackMapped(), false, true, false, Rail.Shape.CURVE);

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
