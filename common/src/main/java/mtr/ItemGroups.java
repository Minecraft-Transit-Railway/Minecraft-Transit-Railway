package mtr;

import me.shedaniel.architectury.registry.CreativeTabs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public interface ItemGroups {

	CreativeModeTab CORE = CreativeTabs.create(new ResourceLocation(MTR.MOD_ID, "core"), () -> new ItemStack(Items.DASHBOARD));
	CreativeModeTab RAILWAY_FACILITIES = CreativeTabs.create(new ResourceLocation(MTR.MOD_ID, "railway_facilities"), () -> new ItemStack(Blocks.TICKET_PROCESSOR));
	CreativeModeTab STATION_BUILDING_BLOCKS = CreativeTabs.create(new ResourceLocation(MTR.MOD_ID, "station_building_blocks"), () -> new ItemStack(Blocks.STATION_COLOR_DARK_PRISMARINE));
}
