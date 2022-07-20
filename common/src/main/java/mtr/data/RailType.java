package mtr.data;

import net.minecraft.world.level.material.MaterialColor;

public enum RailType implements IGui {
	WOODEN(20, MaterialColor.WOOD, false, true, false, RailSlopeStyle.CURVE),
	STONE(40, MaterialColor.STONE, false, true, false, RailSlopeStyle.CURVE),
	EMERALD(60, MaterialColor.EMERALD, false, true, false, RailSlopeStyle.CURVE),
	IRON(80, MaterialColor.METAL, false, true, false, RailSlopeStyle.CURVE),
	OBSIDIAN(120, MaterialColor.COLOR_PURPLE, false, true, false, RailSlopeStyle.CURVE),
	BLAZE(160, MaterialColor.COLOR_ORANGE, false, true, false, RailSlopeStyle.CURVE),
	QUARTZ(200, MaterialColor.QUARTZ, false, true, false, RailSlopeStyle.CURVE),
	DIAMOND(300, MaterialColor.DIAMOND, false, true, false, RailSlopeStyle.CURVE),
	PLATFORM(80, MaterialColor.COLOR_RED, true, false, true, RailSlopeStyle.CURVE),
	SIDING(40, MaterialColor.COLOR_YELLOW, true, false, true, RailSlopeStyle.CURVE),
	TURN_BACK(80, MaterialColor.COLOR_BLUE, false, false, false, RailSlopeStyle.CURVE),
	CABLE_CAR(30, MaterialColor.SNOW, false, true, true, RailSlopeStyle.CABLE),
	CABLE_CAR_STATION(2, MaterialColor.SNOW, false, true, true, RailSlopeStyle.CURVE),
	NONE(20, MaterialColor.COLOR_BLACK, false, false, false, RailSlopeStyle.CURVE);

	public final int speedLimit;
	public final float maxBlocksPerTick;
	public final int color;
	public final boolean hasSavedRail;
	public final boolean canAccelerate;
	public final boolean forContinuousMovement;
	public final RailSlopeStyle railSlopeStyle;

	RailType(int speedLimit, MaterialColor MaterialColor, boolean hasSavedRail, boolean canAccelerate, boolean forContinuousMovement, RailSlopeStyle railSlopeStyle) {
		this.speedLimit = speedLimit;
		maxBlocksPerTick = speedLimit / 3.6F / 20;
		color = MaterialColor.col | ARGB_BLACK;
		this.hasSavedRail = hasSavedRail;
		this.canAccelerate = canAccelerate;
		this.forContinuousMovement = forContinuousMovement;
		this.railSlopeStyle = railSlopeStyle;
	}

	public static float getDefaultMaxBlocksPerTick(TransportMode transportMode) {
		return (transportMode.continuousMovement ? CABLE_CAR_STATION : WOODEN).maxBlocksPerTick;
	}

	public enum RailSlopeStyle {CURVE, CABLE}
}
