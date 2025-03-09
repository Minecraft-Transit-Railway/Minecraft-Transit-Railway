package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Items;

import javax.annotation.Nonnull;

public class BlockAPGDoor extends BlockPSDAPGDoorBase implements BlockEntityProvider {

	public BlockAPGDoor(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new APGDoorBlockEntity(blockPos, blockState);
	}

	@Nonnull
	@Override
	public Item asItem() {
		return Items.APG_DOOR.createAndGet();
	}

	public static class APGDoorBlockEntity extends BlockEntityBase {

		public APGDoorBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.APG_DOOR.createAndGet(), pos, state);
		}
	}
}
