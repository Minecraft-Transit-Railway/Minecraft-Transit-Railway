package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockStationNameTallStanding extends BlockStationNameTallBase {

    public static final float WIDTH = 0.6875F;
    public static final float HEIGHT = 1;

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        switch (IBlock.getStatePropertySafe(state, new Property<>(THIRD.data))) {
            case LOWER:
                VoxelShape vx1 = IBlock.getVoxelShapeByDirection(1, 0, 0, 2, 16, 1, IBlock.getStatePropertySafe(state, FACING));
                VoxelShape vx2 = IBlock.getVoxelShapeByDirection(14, 0, 0, 15, 16, 1, IBlock.getStatePropertySafe(state, FACING));
                return VoxelShapes.union(vx1, vx2);
            case MIDDLE:
                return IBlock.getVoxelShapeByDirection(1, 0, 0, 15, 16, 1, IBlock.getStatePropertySafe(state, FACING));
            case UPPER:
                return IBlock.getVoxelShapeByDirection(1, 0, 0, 15, 6, 1, IBlock.getStatePropertySafe(state, FACING));
            default:
                return VoxelShapes.empty();
        }
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    public static class BlockEntity extends BlockEntityTallBase {
        public BlockEntity(BlockPos blockPos, BlockState blockState) {
            super(BlockEntityTypes.STATION_NAME_TALL_STANDING.get(), blockPos, blockState, 0.07F, false);
        }
    }
}
