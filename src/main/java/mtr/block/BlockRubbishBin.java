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
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class BlockRubbishBin extends HorizontalFacingBlock {
	public static final IntProperty FILLED = IntProperty.of("fill", 0, 10);
	public BlockRubbishBin(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(FILLED, 0));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(FACING, ctx.getPlayerFacing());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, FILLED);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		Direction dir = state.get(FACING).getOpposite();
		switch(dir) {
			case NORTH:
				return VoxelShapes.cuboid(0.15f, 0f, 0.75f, 0.85f, 1.0f, 1.0f);
			case SOUTH:
				return VoxelShapes.cuboid(0.15f, 0f, 0.0f, 0.85f, 1.0f, 0.25f);
			case EAST:
				return VoxelShapes.cuboid(0.0f, 0f, 0.15f, 0.25f, 1.0f, 0.85f);
			case WEST:
				return VoxelShapes.cuboid(0.75f, 0f, 0.15f, 1.0f, 1.0f, 0.85f);
			default:
				return VoxelShapes.fullCube();
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		IBlock.checkHoldingBrush(world, player, () -> {
			world.setBlockState(pos, state.with(FILLED, 0));
		}, () -> {
			if(!player.inventory.getMainHandStack().isEmpty()) {
				int nextState = world.getBlockState(pos).get(FILLED) + 1;
				if (nextState <= 10) {
					world.setBlockState(pos, state.with(FILLED, nextState));
					player.inventory.getMainHandStack().decrement(1);
				}
			}
		});

		return ActionResult.SUCCESS;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int prevValue = world.getBlockState(pos).get(FILLED) - 1;
		if(prevValue >= 0) world.setBlockState(pos, state.with(FILLED, prevValue));
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}
}
