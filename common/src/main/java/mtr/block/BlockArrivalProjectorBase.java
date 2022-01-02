package mtr.block;

import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BlockArrivalProjectorBase extends HorizontalDirectionalBlock implements EntityBlockMapper {

	public BlockArrivalProjectorBase() {
		super(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2).lightLevel(state -> 5).noOcclusion());
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final Direction side = ctx.getClickedFace();
		if (side != Direction.UP && side != Direction.DOWN) {
			return defaultBlockState().setValue(FACING, side.getOpposite());
		} else {
			return null;
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, facing);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
