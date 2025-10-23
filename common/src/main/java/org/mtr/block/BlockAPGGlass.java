package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Items;

import javax.annotation.Nonnull;

public class BlockAPGGlass extends BlockPSDAPGGlassBase implements BlockEntityProvider {

	public static final IntProperty ARROW_DIRECTION = IntProperty.of("propagate_property", 0, 3);

	public BlockAPGGlass(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public Item asItem() {
		return Items.APG_GLASS.get();
	}

	@Nonnull
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		final double y = hit.getPos().y;
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER && y - Math.floor(y) > 0.21875) {
			return IBlock.checkHoldingBrush(world, player, () -> {
				world.setBlockState(pos, state.cycle(ARROW_DIRECTION));
				propagate(world, pos, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING).rotateYClockwise(), ARROW_DIRECTION, 3);
				propagate(world, pos, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING).rotateYCounterclockwise(), ARROW_DIRECTION, 3);
			});
		} else {
			return super.onUse(state, world, pos, player, hit);
		}
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new APGGlassBlockEntity(blockPos, blockState);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
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
