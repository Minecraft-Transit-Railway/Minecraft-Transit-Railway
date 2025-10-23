package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Items;

import javax.annotation.Nonnull;

public class BlockPSDDoor extends BlockPSDAPGDoorBase {

	private final int style;

	public BlockPSDDoor(AbstractBlock.Settings settings, int style) {
		super(settings);
		this.style = style;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new PSDDoorBlockEntity(style, blockPos, blockState);
	}

	@Nonnull
	@Override
	public Item asItem() {
		return style == 0 ? Items.PSD_DOOR_1.get() : Items.PSD_DOOR_2.get();
	}

	public static class PSDDoorBlockEntity extends BlockEntityBase {

		public PSDDoorBlockEntity(int style, BlockPos pos, BlockState state) {
			super(style == 0 ? BlockEntityTypes.PSD_DOOR_1.get() : BlockEntityTypes.PSD_DOOR_2.get(), pos, state);
		}
	}
}
