package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.BlockEntityTypes;
import org.mtr.mod.Items;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockAPGGlass extends BlockPSDAPGGlassBase implements BlockWithEntity {

	public static final IntegerProperty ARROW_DIRECTION = IntegerProperty.of("propagate_property", 0, 3);

	@Nonnull
	@Override
	public Item asItem2() {
		return Items.APG_GLASS.get();
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		final double y = hit.getPos().getYMapped();
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER && y - Math.floor(y) > 0.21875) {
			return IBlock.checkHoldingBrush(world, player, () -> {
				world.setBlockState(pos, state.cycle(new Property<>(ARROW_DIRECTION.data)));
				propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYClockwise(), new Property<>(ARROW_DIRECTION.data), 3);
				propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYCounterclockwise(), new Property<>(ARROW_DIRECTION.data), 3);
			});
		} else {
			return super.onUse2(state, world, pos, player, hand, hit);
		}
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(HALF);
		properties.add(SIDE_EXTENDED);
		properties.add(ARROW_DIRECTION);
	}

	public static class BlockEntity extends BlockPSDTop.BlockEntityBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.APG_GLASS.get(), pos, state);
		}
	}
}
