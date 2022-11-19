package mtr.block;

import mtr.Items;
import net.minecraft.world.item.Item;

public class BlockPSDDoor extends BlockPSDAPGDoorBase {

	private final int style;

	public BlockPSDDoor(int style) {
		super();
		this.style = style;
	}

	@Override
	public Item asItem() {
		return style == 0 ? Items.PSD_DOOR_1.get() : Items.PSD_DOOR_2.get();
	}
}
