package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Items;

public class BlockPSDDoor extends BlockPSDAPGDoorBase {

	private final int style;

	public BlockPSDDoor(BlockBehaviour.Properties settings, int style) {
		super(settings);
		this.style = style;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new PSDDoorBlockEntity(style, blockPos, blockState);
	}

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
