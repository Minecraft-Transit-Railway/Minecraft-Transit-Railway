package mtr.block;

import mtr.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class BlockPolePIDS extends HorizontalFacingBlock {
    public BlockPolePIDS(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return mtr.block.IBlock.getVoxelShapeByDirection(7.5, 0, 12.5, 8.5, 16, 13.5, state.get(FACING));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockBelow = ctx.getWorld().getBlockState(ctx.getBlockPos().down());
        if(blockBelow.isOf(Blocks.PIDS_1) || blockBelow.isOf(Blocks.PIDS_2) || blockBelow.isOf(Blocks.PIDS_3) || blockBelow.isOf(Blocks.POLE_PIDS)) {
            return getDefaultState().with(FACING, IBlock.getStatePropertySafe(blockBelow, FACING));
        }

        return null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
