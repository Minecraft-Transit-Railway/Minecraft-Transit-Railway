package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * First variant for <b>Station Name Sign</b>.
 * @see BlockStationNameSignBase
 */

public class BlockStationNameSign1 extends BlockStationNameSignBase
{
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(player.isHolding(Items.STICK)) return super.use(state, world, pos, player, interactionHand, blockHitResult);
        return IBlock.checkHoldingBrush(world, player, () -> {
            final boolean isWhite = IBlock.getStatePropertySafe(state, COLOR) == 0;
            final int newColorProperty = isWhite ? 2 : 0;

            updateProperties(world, pos, newColorProperty);
        });
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state)
    {
        return new TileEntityStationNameWall(pos, state);
    }

    public static class TileEntityStationNameWall extends BlockStationNameSignBase.TileEntityStationNameBase
    {
        public TileEntityStationNameWall(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_NAME_SIGN_ENTITY_1.get(), pos, state);
        }
    }

    private static void updateProperties(Level world, BlockPos pos, int colorProperty)
    {
        world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(COLOR, colorProperty));
    }
}
