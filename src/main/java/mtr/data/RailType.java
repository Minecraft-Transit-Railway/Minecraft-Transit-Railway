package mtr.data;

import mtr.gui.IGui;
import net.minecraft.block.MaterialColor;

public enum RailType implements IGui {
	WOODEN(20, MaterialColor.WOOD),
	STONE(40, MaterialColor.STONE),
	IRON(80, MaterialColor.WHITE),
	OBSIDIAN(120, MaterialColor.PURPLE),
	BLAZE(160, MaterialColor.ORANGE),
	DIAMOND(300, MaterialColor.DIAMOND),
	DEPOT(40, MaterialColor.YELLOW),
	PLATFORM(100, MaterialColor.RED);

	public final int speedLimit;
	public final float maxBlocksPerTick;
	public final int color;

	RailType(int speedLimit, MaterialColor materialColor) {
		this.speedLimit = speedLimit;
		maxBlocksPerTick = speedLimit / 3.6F / 20;
		color = materialColor.color + ARGB_BLACK_TRANSLUCENT;
	}
}
