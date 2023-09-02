package mtr.block;

import mtr.mappings.BlockDirectionalMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BlockSignalLightBase extends BlockDirectionalMapper implements EntityBlockMapper {

	private final int shapeX;
	private final int shapeHeight;

	public BlockSignalLightBase(Properties settings, int shapeX, int shapeHeight) {
		super(settings);
		this.shapeX = shapeX;
		this.shapeHeight = shapeHeight;
	}

	// TODO backwards compatibility
	@Deprecated
	public BlockSignalLightBase(Properties settings) {
		this(settings, 2, 14);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		return IBlock.getVoxelShapeByDirection(shapeX, 0, 5, 16 - shapeX, shapeHeight, 11, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState blockState) {
		return PushReaction.BLOCK;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
