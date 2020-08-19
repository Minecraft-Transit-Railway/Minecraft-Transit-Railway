package mtr.block;

import mtr.Items;
import mtr.tile.TileEntityAPGDoor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.world.BlockView;

public class BlockAPGDoor extends BlockPSDAPGDoorBase {

	@Override
	public Item asItem() {
		return Items.APG_DOOR;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityAPGDoor();
	}
}
