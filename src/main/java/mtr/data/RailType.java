package mtr.data;

import net.minecraft.block.MapColor;

public enum RailType implements IGui {
	WOODEN(20, MapColor.OAK_TAN, false),
	STONE(40, MapColor.STONE_GRAY, false),
	IRON(80, MapColor.WHITE, false),
	OBSIDIAN(120, MapColor.PURPLE, false),
	BLAZE(160, MapColor.ORANGE, false),
	DIAMOND(300, MapColor.DIAMOND_BLUE, false),
	PLATFORM(80, MapColor.RED, true),
	SIDING(40, MapColor.YELLOW, true),
	TURN_BACK(80, MapColor.BLUE, false),
	NONE(20, MapColor.BLACK, false);

	public final int speedLimit;
	public final float maxBlocksPerTick;
	public final int color;
	public final boolean hasSavedRail;

	RailType(int speedLimit, MapColor mapColor, boolean hasSavedRail) {
		this.speedLimit = speedLimit;
		maxBlocksPerTick = speedLimit / 3.6F / 20;
		color = mapColor.color + ARGB_BLACK_TRANSLUCENT;
		this.hasSavedRail = hasSavedRail;
	}
}
