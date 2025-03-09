package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockGlassFence extends BlockDirectionalDoubleBlockBase {

	public static final IntProperty NUMBER = IntProperty.of("number", 1, 7);

	public BlockGlassFence(AbstractBlock.Settings settings) {
		super(settings.nonOpaque());
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.FACING);
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 3, 3, facing);
		} else {
			return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 3, facing);
		}
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.FACING);
		return VoxelShapes.union(getOutlineShape(state, world, pos, context), IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 8, 3, facing));
	}

	@Nonnull
	@Override
	protected VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
		tooltip.add(Text.translatable("tooltip." + stack.getItem().getTranslationKey()).formatted(Formatting.GRAY));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.FACING);
		builder.add(HALF);
		builder.add(NUMBER);
	}

	@Override
	protected BlockState getAdditionalState(BlockPos pos, Direction facing) {
		return getDefaultState().with(NUMBER, getNumber(pos, facing));
	}

	private static int getNumber(BlockPos pos, Direction facing) {
		final int x = (pos.getX() % 7 + 7) % 7;
		final int z = (pos.getZ() % 7 + 7) % 7;
		if (facing == Direction.NORTH || facing == Direction.EAST) {
			return ((x + z) % 7) + 1;
		} else {
			return ((-x - z) % 7) + 7;
		}
	}
}
