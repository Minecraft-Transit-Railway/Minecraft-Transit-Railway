package mtr.block;

import mtr.Items;
import mtr.tile.TileEntityPSDDoor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.world.BlockView;

public class BlockPSDDoor extends BlockPSDAPGDoorBase {

	@Override
	public Item asItem() {
		return Items.PSD_DOOR;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityPSDDoor();
	}
}
