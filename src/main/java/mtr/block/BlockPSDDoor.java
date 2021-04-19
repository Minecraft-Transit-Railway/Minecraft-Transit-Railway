package mtr.block;

import mtr.Items;
import net.minecraft.item.Item;

public class BlockPSDDoor extends BlockPSDAPGDoorBase {

	public final int style;

	public BlockPSDDoor(int style) {
		super();
		this.style = style;
	}

	@Override
	public Item asItem() {
		return style == 0 ? Items.PSD_DOOR : Items.PSD_DOOR_2;
	}
}
