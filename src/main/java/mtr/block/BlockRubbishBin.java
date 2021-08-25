package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class BlockRubbishBin extends HorizontalFacingBlock {

	public static final int MAX_LEVEL = 15;
	public static final IntProperty FILLED = IntProperty.of("filled", 0, MAX_LEVEL);

	public BlockRubbishBin(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(FILLED, 0));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(FACING, ctx.getPlayerFacing());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(2, 0, 0, 14, 16, 4.5, state.get(FACING));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		IBlock.checkHoldingBrush(world, player, () -> world.setBlockState(pos, state.with(FILLED, 0)), () -> {
			final int currentLevel = IBlock.getStatePropertySafe(state, FILLED);
			if (!player.getMainHandStack().isEmpty() && currentLevel < MAX_LEVEL) {
				world.setBlockState(pos, state.with(FILLED, currentLevel + 1));
				if (!player.isCreative()) {
					player.getMainHandStack().decrement(1);
				}
			}
		});
		return ActionResult.SUCCESS;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		final int newLevel = IBlock.getStatePropertySafe(state, FILLED) - 1;
		if (newLevel >= 0) {
			world.setBlockState(pos, state.with(FILLED, newLevel));
		}
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, FILLED);
	}
}
