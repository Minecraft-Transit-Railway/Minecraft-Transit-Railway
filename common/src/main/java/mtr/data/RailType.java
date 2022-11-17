package mtr.data;

import net.minecraft.world.level.material.MaterialColor;

public enum RailType implements IGui {
	WOODEN(20, MaterialColor.WOOD, false, true, true, RailSlopeStyle.CURVE),
	STONE(40, MaterialColor.STONE, false, true, true, RailSlopeStyle.CURVE),
	EMERALD(60, MaterialColor.EMERALD, false, true, true, RailSlopeStyle.CURVE),
	IRON(80, MaterialColor.METAL, false, true, true, RailSlopeStyle.CURVE),
	OBSIDIAN(120, MaterialColor.COLOR_PURPLE, false, true, true, RailSlopeStyle.CURVE),
	BLAZE(160, MaterialColor.COLOR_ORANGE, false, true, true, RailSlopeStyle.CURVE),
	QUARTZ(200, MaterialColor.QUARTZ, false, true, true, RailSlopeStyle.CURVE),
	DIAMOND(300, MaterialColor.DIAMOND, false, true, true, RailSlopeStyle.CURVE),
	PLATFORM(80, MaterialColor.COLOR_RED, true, false, true, RailSlopeStyle.CURVE),
	SIDING(40, MaterialColor.COLOR_YELLOW, true, false, true, RailSlopeStyle.CURVE),
	TURN_BACK(80, MaterialColor.COLOR_BLUE, false, false, true, RailSlopeStyle.CURVE),
	CABLE_CAR(30, MaterialColor.SNOW, false, true, true, RailSlopeStyle.CABLE),
	CABLE_CAR_STATION(2, MaterialColor.SNOW, false, true, true, RailSlopeStyle.CURVE),
	RUNWAY(300, MaterialColor.ICE, false, true, false, RailSlopeStyle.CURVE),
	AIRPLANE_DUMMY(900, MaterialColor.COLOR_BLACK, false, true, false, RailSlopeStyle.CURVE),
	NONE(20, MaterialColor.COLOR_BLACK, false, false, true, RailSlopeStyle.CURVE);

	public final int speedLimit;
	public final float maxBlocksPerTick;
	public final int color;
	public final boolean hasSavedRail;
	public final boolean canAccelerate;
	public final boolean hasSignal;
	public final RailSlopeStyle railSlopeStyle;

	RailType(int speedLimit, MaterialColor MaterialColor, boolean hasSavedRail, boolean canAccelerate, boolean hasSignal, RailSlopeStyle railSlopeStyle) {
		this.speedLimit = speedLimit;
		maxBlocksPerTick = speedLimit / 3.6F / 20;
		color = MaterialColor.col | ARGB_BLACK;
		this.hasSavedRail = hasSavedRail;
		this.canAccelerate = canAccelerate;
		this.hasSignal = hasSignal;
		this.railSlopeStyle = railSlopeStyle;
	}

	public static float getDefaultMaxBlocksPerTick(TransportMode transportMode) {
		return (transportMode.continuousMovement ? CABLE_CAR_STATION : WOODEN).maxBlocksPerTick;
	}

	public enum RailSlopeStyle {CURVE, CABLE}
}
