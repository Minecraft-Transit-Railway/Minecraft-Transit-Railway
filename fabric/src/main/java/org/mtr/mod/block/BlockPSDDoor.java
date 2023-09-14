package org.mtr.mod.block;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.Item;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;
import org.mtr.mod.Items;

import javax.annotation.Nonnull;

public class BlockPSDDoor extends BlockPSDAPGDoorBase {

	private final int style;

	public BlockPSDDoor(int style) {
		super();
		this.style = style;
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(style, blockPos, blockState);
	}

	@Nonnull
	@Override
	public Item asItem2() {
		return style == 0 ? Items.PSD_DOOR_1.get() : Items.PSD_DOOR_2.get();
	}

	public static class BlockEntity extends BlockEntityBase {

		public BlockEntity(int style, BlockPos pos, BlockState state) {
			super(style == 0 ? BlockEntityTypes.PSD_DOOR_1.get() : BlockEntityTypes.PSD_DOOR_2.get(), pos, state);
		}
	}
}
