package mtr.block;

import mtr.Items;
import net.minecraft.world.item.Item;

public class BlockLiftDoor extends BlockPSDAPGDoorBase {

	@Override
	public Item asItem() {
		return Items.LIFT_DOOR_1.get();
	}
}
