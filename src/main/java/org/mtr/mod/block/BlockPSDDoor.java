package mtr.block;

import mtr.BlockEntityTypes;
import mtr.Items;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;

public class BlockPSDDoor extends BlockPSDAPGDoorBase {

	private final int style;

	public BlockPSDDoor(int style) {
		super();
		this.style = style;
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityPSDDoor(style, pos, state);
	}

	@Override
	public Item asItem() {
		return style == 0 ? Items.PSD_DOOR_1.get() : Items.PSD_DOOR_2.get();
	}

	public static class TileEntityPSDDoor extends TileEntityPSDAPGDoorBase {

		public TileEntityPSDDoor(int style, BlockPos pos, BlockState state) {
			super(style == 0 ? BlockEntityTypes.PSD_DOOR_1_TILE_ENTITY.get() : BlockEntityTypes.PSD_DOOR_2_TILE_ENTITY.get(), pos, state);
		}
	}
}
