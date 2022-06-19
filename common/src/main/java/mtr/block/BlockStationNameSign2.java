package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import java.util.List;

/**
 * Second variant for <b>Station Name Sign</b>.
 * @see BlockStationNameSignBase
 */

public class BlockStationNameSign2 extends BlockStationNameSignBase
{
    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state)
    {
        return new TileEntityStationNameWall(pos, state);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(new TranslatableComponent("tooltip.mtr.station_name").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }

    public static class TileEntityStationNameWall extends BlockStationNameSignBase.TileEntityStationNameBase
    {
        public TileEntityStationNameWall(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_NAME_SIGN_ENTITY_2.get(), pos, state);
        }
    }
}
