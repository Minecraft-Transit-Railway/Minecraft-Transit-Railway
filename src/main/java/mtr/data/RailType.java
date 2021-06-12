package mtr.data;

import net.minecraft.block.MaterialColor;

public enum RailType implements IGui {
	WOODEN(20, MaterialColor.WOOD, false),
	STONE(40, MaterialColor.STONE, false),
	IRON(80, MaterialColor.WHITE, false),
	OBSIDIAN(120, MaterialColor.PURPLE, false),
	BLAZE(160, MaterialColor.ORANGE, false),
	DIAMOND(300, MaterialColor.DIAMOND, false),
	PLATFORM(100, MaterialColor.RED, true),
	SIDING(40, MaterialColor.YELLOW, true);

	public final int speedLimit;
	public final float maxBlocksPerTick;
	public final int color;
	public final boolean hasSavedRail;

	RailType(int speedLimit, MaterialColor materialColor, boolean hasSavedRail) {
		this.speedLimit = speedLimit;
		maxBlocksPerTick = speedLimit / 3.6F / 20;
		color = materialColor.color + ARGB_BLACK_TRANSLUCENT;
		this.hasSavedRail = hasSavedRail;
	}
}
