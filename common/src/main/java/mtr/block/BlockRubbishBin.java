package mtr.block;

import mtr.mappings.BlockDirectionalMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockRubbishBin extends BlockDirectionalMapper {

	public static final int MAX_LEVEL = 15;
	public static final IntegerProperty FILLED = IntegerProperty.create("filled", 0, MAX_LEVEL);

	public BlockRubbishBin(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(FILLED, 0));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return IBlock.getVoxelShapeByDirection(2, 0, 0, 14, 16, 4.5, state.getValue(FACING));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
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
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos) {
		final int newLevel = IBlock.getStatePropertySafe(state, FILLED) - 1;
		if (newLevel >= 0) {
			world.setBlockAndUpdate(pos, state.setValue(FILLED, newLevel));
		}
	}

	@Override
	public boolean isRandomlyTicking(BlockState blockState) {
		return true;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, FILLED);
	}
}
