package mtr.data;

import net.minecraft.block.MapColor;

public enum RailType implements IGui {
	WOODEN(20, MapColor.OAK_TAN, false, true),
	STONE(40, MapColor.STONE_GRAY, false, true),
	IRON(80, MapColor.WHITE, false, true),
	OBSIDIAN(120, MapColor.PURPLE, false, true),
	BLAZE(160, MapColor.ORANGE, false, true),
	DIAMOND(300, MapColor.DIAMOND_BLUE, false, true),
	PLATFORM(80, MapColor.RED, true, false),
	SIDING(40, MapColor.YELLOW, true, false),
	TURN_BACK(80, MapColor.BLUE, false, false),
	NONE(20, MapColor.BLACK, false, false);

	public final int speedLimit;
	public final float maxBlocksPerTick;
	public final int color;
	public final boolean hasSavedRail;
	public final boolean canAccelerate;

	RailType(int speedLimit, MapColor mapColor, boolean hasSavedRail, boolean canAccelerate) {
		this.speedLimit = speedLimit;
		maxBlocksPerTick = speedLimit / 3.6F / 20;
		color = mapColor.color + ARGB_BLACK_TRANSLUCENT;
		this.hasSavedRail = hasSavedRail;
		this.canAccelerate = canAccelerate;
	}
}
