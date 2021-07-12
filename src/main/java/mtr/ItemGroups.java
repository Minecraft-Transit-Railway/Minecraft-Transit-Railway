package mtr;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public interface ItemGroups {

	ItemGroup CORE = FabricItemGroupBuilder.build(new Identifier(MTR.MOD_ID, "core"), () -> new ItemStack(Items.DASHBOARD));
	ItemGroup RAILWAY_FACILITIES = FabricItemGroupBuilder.build(new Identifier(MTR.MOD_ID, "railway_facilities"), () -> new ItemStack(Blocks.TICKET_PROCESSOR));
	ItemGroup STATION_BUILDING_BLOCKS = FabricItemGroupBuilder.build(new Identifier(MTR.MOD_ID, "station_building_blocks"), () -> new ItemStack(Blocks.STATION_COLOR_DARK_PRISMARINE));
}
