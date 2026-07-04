package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Items;

public class BlockAPGGlass extends BlockPSDAPGGlassBase implements EntityBlock {

	public static final IntegerProperty ARROW_DIRECTION = IntegerProperty.create("propagate_property", 0, 3);

	public BlockAPGGlass(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public Item asItem() {
		return Items.APG_GLASS.get();
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		final double y = hit.getLocation().y;
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER && y - Math.floor(y) > 0.21875) {
			return IBlock.checkHoldingBrush(world, player, () -> {
				world.setBlockAndUpdate(pos, state.cycle(ARROW_DIRECTION));
				propagate(world, pos, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING).getClockWise(), ARROW_DIRECTION, 3);
				propagate(world, pos, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING).getCounterClockWise(), ARROW_DIRECTION, 3);
			});
		} else {
			return super.useWithoutItem(state, world, pos, player, hit);
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new APGGlassBlockEntity(blockPos, blockState);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(HALF);
		builder.add(SIDE_EXTENDED);
		builder.add(ARROW_DIRECTION);
	}

	public static class APGGlassBlockEntity extends BlockPSDTop.BlockEntityBase {

		public APGGlassBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.APG_GLASS.get(), pos, state);
		}
	}
}
