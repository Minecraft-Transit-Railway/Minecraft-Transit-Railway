package mtr.data;

import net.minecraft.world.level.material.MaterialColor;

public enum RailType implements IGui {
	WOODEN(20, MaterialColor.WOOD, false, true),
	STONE(40, MaterialColor.STONE, false, true),
        EMERALD(60, MaterialColor.EMERALD, false, true),
	IRON(80, MaterialColor.SNOW, false, true),
        QUARTZ(100, MaterialColor.QUARTZ, false, true),
	OBSIDIAN(130, MaterialColor.COLOR_PURPLE, false, true),
	BLAZE(200, MaterialColor.COLOR_ORANGE, false, true),
	DIAMOND(300, MaterialColor.DIAMOND, false, true),
	PLATFORM(80, MaterialColor.COLOR_RED, true, false),
	SIDING(40, MaterialColor.COLOR_YELLOW, true, false),
	TURN_BACK(80, MaterialColor.COLOR_BLUE, false, false),
	NONE(20, MaterialColor.COLOR_BLACK, false, false);

	public final int speedLimit;
	public final float maxBlocksPerTick;
	public final int color;
	public final boolean hasSavedRail;
	public final boolean canAccelerate;

	RailType(int speedLimit, MaterialColor MaterialColor, boolean hasSavedRail, boolean canAccelerate) {
		this.speedLimit = speedLimit;
		maxBlocksPerTick = speedLimit / 3.6F / 20;
		color = MaterialColor.col + ARGB_BLACK_TRANSLUCENT;
		this.hasSavedRail = hasSavedRail;
		this.canAccelerate = canAccelerate;
	}
}
