package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Items;

public class BlockAPGDoor extends BlockPSDAPGDoorBase implements EntityBlock {

	public BlockAPGDoor(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new APGDoorBlockEntity(blockPos, blockState);
	}

	@Override
	public Item asItem() {
		return Items.APG_DOOR.get();
	}

	public static class APGDoorBlockEntity extends BlockEntityBase {

		public APGDoorBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.APG_DOOR.get(), pos, state);
		}
	}
}
