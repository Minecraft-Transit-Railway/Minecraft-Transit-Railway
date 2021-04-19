package mtr.block;

import mtr.MTR;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BlockPIDS extends BlockPIDSBase {

	public final int style;

	public BlockPIDS(int style) {
		super();
		this.style = style;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape shape1 = IBlock.getVoxelShapeByDirection(6, 0, 0, 10, style == 1 ? 11 : 9, 16, IBlock.getStatePropertySafe(state, FACING));
		VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 11, style == 1 ? 11 : 9, 8.5, 16, 13.5, IBlock.getStatePropertySafe(state, FACING));
		return VoxelShapes.union(shape1, shape2);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityBlockPIDS(style);
	}

	public static class TileEntityBlockPIDS extends BlockEntity {

		public TileEntityBlockPIDS(int style) {
			super(style == 1 ? MTR.PIDS_2_TILE_ENTITY : MTR.PIDS_1_TILE_ENTITY);
		}
	}
}
