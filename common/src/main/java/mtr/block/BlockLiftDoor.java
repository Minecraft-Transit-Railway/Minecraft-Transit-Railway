package mtr.block;

import mtr.BlockEntityTypes;
import mtr.Items;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;

public class BlockLiftDoor extends BlockPSDAPGDoorBase {

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityLiftDoor(pos, state);
	}

	@Override
	public Item asItem() {
		return Items.LIFT_DOOR_1.get();
	}

	public static class TileEntityLiftDoor extends TileEntityPSDAPGDoorBase {

		public TileEntityLiftDoor(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_DOOR_EVEN_1_TILE_ENTITY.get(), pos, state);
		}
	}
}
