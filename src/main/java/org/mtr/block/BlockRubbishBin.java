package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockRubbishBin extends Block {

	public static final int MAX_LEVEL = 15;
	public static final IntegerProperty FILLED = IntegerProperty.create("filled", 0, MAX_LEVEL);

	public BlockRubbishBin(BlockBehaviour.Properties blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return IBlock.getVoxelShapeByDirection(2, 0, 0, 14, 16, 4.5, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> world.setBlockAndUpdate(pos, state.setValue(FILLED, 0)), () -> {
			final int currentLevel = IBlock.getStatePropertySafe(state, FILLED);
			if (!player.getMainHandItem().isEmpty() && currentLevel < MAX_LEVEL) {
				world.setBlockAndUpdate(pos, state.setValue(FILLED, currentLevel + 1));
				if (!player.isCreative()) {
					player.getMainHandItem().shrink(1);
				}
			}
		});
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		final int newLevel = IBlock.getStatePropertySafe(state, FILLED) - 1;
		if (newLevel >= 0) {
			world.setBlockAndUpdate(pos, state.setValue(FILLED, newLevel));
		}
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return true;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(FILLED);
	}
}
