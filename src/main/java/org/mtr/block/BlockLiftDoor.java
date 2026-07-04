package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Items;

public class BlockLiftDoor extends BlockPSDAPGDoorBase {

	public BlockLiftDoor(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new LiftDoorBlockEntity(blockPos, blockState);
	}

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
