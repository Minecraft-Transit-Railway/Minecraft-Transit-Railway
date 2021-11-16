package mtr.block;

import mtr.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BlockPoleSignOdd extends HorizontalFacingBlock {
    public static final IntProperty TYPE = IntProperty.of("type", 0, 3);
    public BlockPoleSignOdd(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        int type = IBlock.getStatePropertySafe(state, TYPE);
        switch (type) {
            case 0:
                return IBlock.getVoxelShapeByDirection(14, 0, 7, 15.25, 16, 9, state.get(FACING));
            case 1:
                return IBlock.getVoxelShapeByDirection(10, 0, 7, 11.25, 16, 9, state.get(FACING));
            case 2:
                return IBlock.getVoxelShapeByDirection(6, 0, 7, 7.25, 16, 9, state.get(FACING));
            case 3:
                return IBlock.getVoxelShapeByDirection(2, 0, 7, 3.25, 16, 9, state.get(FACING));
            default:
                return VoxelShapes.fullCube();
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockBelow = ctx.getWorld().getBlockState(ctx.getBlockPos().down());

        if(blockBelow.isOf(Blocks.RAILWAY_SIGN_2_ODD) || blockBelow.isOf(Blocks.RAILWAY_SIGN_6_ODD)) {
            return getDefaultState().with(FACING, IBlock.getStatePropertySafe(blockBelow, FACING)).with(TYPE, 0);
        }
        if(blockBelow.isOf(Blocks.RAILWAY_SIGN_3_ODD) || blockBelow.isOf(Blocks.RAILWAY_SIGN_7_ODD)) {
            return getDefaultState().with(FACING, IBlock.getStatePropertySafe(blockBelow, FACING)).with(TYPE, 1);
        }
        if(blockBelow.isOf(Blocks.RAILWAY_SIGN_4_ODD)) {
            return getDefaultState().with(FACING, IBlock.getStatePropertySafe(blockBelow, FACING)).with(TYPE, 2);
        }
        if(blockBelow.isOf(Blocks.RAILWAY_SIGN_5_ODD)) {
            return getDefaultState().with(FACING, IBlock.getStatePropertySafe(blockBelow, FACING)).with(TYPE, 3);
        }
        if(blockBelow.isOf(Blocks.POLE_SIGN_ODD)) {
            return getDefaultState().with(FACING, IBlock.getStatePropertySafe(blockBelow, FACING)).with(TYPE, IBlock.getStatePropertySafe(blockBelow, TYPE));
        }
        return null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE);
    }
}