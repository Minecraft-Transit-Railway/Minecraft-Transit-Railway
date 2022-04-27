package mtr.data;

import net.minecraft.world.level.material.MaterialColor;

public enum RailType implements IGui {
	WOODEN(20, MaterialColor.WOOD, false, true, false),
	STONE(40, MaterialColor.STONE, false, true, false),
	EMERALD(60, MaterialColor.EMERALD, false, true, false),
	IRON(80, MaterialColor.METAL, false, true, false),
	OBSIDIAN(120, MaterialColor.COLOR_PURPLE, false, true, false),
	BLAZE(160, MaterialColor.COLOR_ORANGE, false, true, false),
	QUARTZ(200, MaterialColor.QUARTZ, false, true, false),
	DIAMOND(300, MaterialColor.DIAMOND, false, true, false),
	PLATFORM(80, MaterialColor.COLOR_RED, true, false, true),
	SIDING(40, MaterialColor.COLOR_YELLOW, true, false, true),
	TURN_BACK(80, MaterialColor.COLOR_BLUE, false, false, false),
	CABLE_CAR(2, MaterialColor.COLOR_BLACK, false, true, true),
	NONE(20, MaterialColor.COLOR_BLACK, false, false, false);

	public final int speedLimit;
	public final float maxBlocksPerTick;
	public final int color;
	public final boolean hasSavedRail;
	public final boolean canAccelerate;
	public final boolean forContinuousMovement;

	RailType(int speedLimit, MaterialColor MaterialColor, boolean hasSavedRail, boolean canAccelerate, boolean forContinuousMovement) {
		this.speedLimit = speedLimit;
		maxBlocksPerTick = speedLimit / 3.6F / 20;
		color = MaterialColor.col | ARGB_BLACK_TRANSLUCENT;
		this.hasSavedRail = hasSavedRail;
		this.canAccelerate = canAccelerate;
		this.forContinuousMovement = forContinuousMovement;
	}

	public static float getDefaultMaxBlocksPerTick(TransportMode transportMode) {
		return (transportMode.continuousMovement ? CABLE_CAR : WOODEN).maxBlocksPerTick;
	}
}
