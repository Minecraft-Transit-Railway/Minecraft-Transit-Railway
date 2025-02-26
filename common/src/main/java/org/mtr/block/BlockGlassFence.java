package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockGlassFence extends BlockDirectionalDoubleBlockBase {

	public static final IntegerProperty NUMBER = IntegerProperty.of("number", 1, 7);

	public BlockGlassFence() {
		super(Blocks.createDefaultBlockSettings(true).nonOpaque());
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 3, 3, facing);
		} else {
			return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 3, facing);
		}
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return VoxelShapes.union(getOutlineShape2(state, world, pos, context), IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 8, 3, facing));
	}

	@Nonnull
	@Override
	public VoxelShape getCameraCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
		tooltip.add(TextHelper.translatable("tooltip." + stack.getItem().getTranslationKey()).formatted(TextFormatting.GRAY));
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(HALF);
		properties.add(NUMBER);
	}

	@Override
	protected BlockState getAdditionalState(BlockPos pos, Direction facing) {
		return getDefaultState2().with(new Property<>(NUMBER.data), getNumber(pos, facing));
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
