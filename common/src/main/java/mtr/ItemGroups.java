package mtr;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public interface ItemGroups {

	CreativeModeTab CORE = Keys.LIFTS_ONLY ? CreativeModeTab.TAB_MISC : Registry.getItemGroup(new ResourceLocation(MTR.MOD_ID, "core"), () -> new ItemStack(Items.RAILWAY_DASHBOARD.get()));
	CreativeModeTab RAILWAY_FACILITIES = Keys.LIFTS_ONLY ? CreativeModeTab.TAB_MISC : Registry.getItemGroup(new ResourceLocation(MTR.MOD_ID, "railway_facilities"), () -> new ItemStack(Blocks.TICKET_PROCESSOR.get()));
	CreativeModeTab STATION_BUILDING_BLOCKS = Keys.LIFTS_ONLY ? CreativeModeTab.TAB_MISC : Registry.getItemGroup(new ResourceLocation(MTR.MOD_ID, "station_building_blocks"), () -> new ItemStack(Blocks.LOGO.get()));
	CreativeModeTab ESCALATORS_LIFTS = Registry.getItemGroup(new ResourceLocation(MTR.MOD_ID, "escalators_lifts"), () -> new ItemStack(Items.ESCALATOR.get()));
}
