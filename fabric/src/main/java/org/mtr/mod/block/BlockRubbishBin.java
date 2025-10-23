package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockRubbishBin extends BlockExtension implements DirectionHelper {

	public static final int MAX_LEVEL = 15;
	public static final IntegerProperty FILLED = IntegerProperty.of("filled", 0, MAX_LEVEL);

	public BlockRubbishBin(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		return getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(2, 0, 0, 14, 16, 4.5, Direction.convert(state.get(new Property<>(FACING.data))));
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> world.setBlockState(pos, state.with(new Property<>(FILLED.data), 0)), () -> {
			final int currentLevel = IBlock.getStatePropertySafe(state, FILLED);
			if (!player.getMainHandStack().isEmpty() && currentLevel < MAX_LEVEL) {
				world.setBlockState(pos, state.with(new Property<>(FILLED.data), currentLevel + 1));
				if (!player.isCreative()) {
					player.getMainHandStack().decrement(1);
				}
			}
		});
	}

	@Override
	public void randomTick2(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		final int newLevel = IBlock.getStatePropertySafe(state, FILLED) - 1;
		if (newLevel >= 0) {
			world.setBlockState(pos, state.with(new Property<>(FILLED.data), newLevel));
		}
	}

	@Override
	public boolean hasRandomTicks2(BlockState state) {
		return true;
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(FILLED);
	}
}
