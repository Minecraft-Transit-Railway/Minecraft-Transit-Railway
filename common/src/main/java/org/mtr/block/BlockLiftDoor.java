package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Items;

import javax.annotation.Nonnull;

public class BlockLiftDoor extends BlockPSDAPGDoorBase {

	public BlockLiftDoor(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new LiftDoorBlockEntity(blockPos, blockState);
	}

	@Nonnull
	@Override
	public Item asItem() {
		return Items.LIFT_DOOR_1.get();
	}

	public static class LiftDoorBlockEntity extends BlockEntityBase {

		public LiftDoorBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_DOOR_EVEN_1.get(), pos, state);
		}
	}
}
