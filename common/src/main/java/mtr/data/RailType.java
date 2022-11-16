package mtr.data;

import net.minecraft.world.level.material.MaterialColor;

public enum RailType implements IGui {
	WOODEN(20, MaterialColor.WOOD, false, true, RailSlopeStyle.CURVE),
	STONE(40, MaterialColor.STONE, false, true, RailSlopeStyle.CURVE),
	EMERALD(60, MaterialColor.EMERALD, false, true, RailSlopeStyle.CURVE),
	IRON(80, MaterialColor.METAL, false, true, RailSlopeStyle.CURVE),
	OBSIDIAN(120, MaterialColor.COLOR_PURPLE, false, true, RailSlopeStyle.CURVE),
	BLAZE(160, MaterialColor.COLOR_ORANGE, false, true, RailSlopeStyle.CURVE),
	QUARTZ(200, MaterialColor.QUARTZ, false, true, RailSlopeStyle.CURVE),
	DIAMOND(300, MaterialColor.DIAMOND, false, true, RailSlopeStyle.CURVE),
	PLATFORM(80, MaterialColor.COLOR_RED, true, false, RailSlopeStyle.CURVE),
	SIDING(40, MaterialColor.COLOR_YELLOW, true, false, RailSlopeStyle.CURVE),
	TURN_BACK(80, MaterialColor.COLOR_BLUE, false, false, RailSlopeStyle.CURVE),
	CABLE_CAR(30, MaterialColor.SNOW, false, true, RailSlopeStyle.CABLE),
	CABLE_CAR_STATION(2, MaterialColor.SNOW, false, true, RailSlopeStyle.CURVE),
	RUNWAY(300, MaterialColor.ICE, false, true, RailSlopeStyle.CURVE),
	AIRPLANE_DUMMY(900, MaterialColor.COLOR_BLACK, false, true, RailSlopeStyle.CURVE),
	NONE(20, MaterialColor.COLOR_BLACK, false, false, RailSlopeStyle.CURVE);

	public final int speedLimit;
	public final float maxBlocksPerTick;
	public final int color;
	public final boolean hasSavedRail;
	public final boolean canAccelerate;
	public final RailSlopeStyle railSlopeStyle;

	RailType(int speedLimit, MaterialColor MaterialColor, boolean hasSavedRail, boolean canAccelerate, RailSlopeStyle railSlopeStyle) {
		this.speedLimit = speedLimit;
		maxBlocksPerTick = speedLimit / 3.6F / 20;
		color = MaterialColor.col | ARGB_BLACK;
		this.hasSavedRail = hasSavedRail;
		this.canAccelerate = canAccelerate;
		this.railSlopeStyle = railSlopeStyle;
	}

	public static float getDefaultMaxBlocksPerTick(TransportMode transportMode) {
		return (transportMode.continuousMovement ? CABLE_CAR_STATION : WOODEN).maxBlocksPerTick;
	}

	public enum RailSlopeStyle {CURVE, CABLE}
}
