package mtr;

import mtr.mappings.Utilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public interface CreativeModeTabs {

	CreativeModeTab CORE = Keys.LIFTS_ONLY ? Utilities.getDefaultTab() : Registry.getCreativeModeTab(new ResourceLocation(MTR.MOD_ID, "core"), () -> new ItemStack(Items.RAILWAY_DASHBOARD.get()));
	CreativeModeTab RAILWAY_FACILITIES = Keys.LIFTS_ONLY ? Utilities.getDefaultTab() : Registry.getCreativeModeTab(new ResourceLocation(MTR.MOD_ID, "railway_facilities"), () -> new ItemStack(Blocks.TICKET_PROCESSOR.get()));
	CreativeModeTab STATION_BUILDING_BLOCKS = Keys.LIFTS_ONLY ? Utilities.getDefaultTab() : Registry.getCreativeModeTab(new ResourceLocation(MTR.MOD_ID, "station_building_blocks"), () -> new ItemStack(Blocks.LOGO.get()));
	CreativeModeTab ESCALATORS_LIFTS = Registry.getCreativeModeTab(new ResourceLocation(MTR.MOD_ID, "escalators_lifts"), () -> new ItemStack(Items.ESCALATOR.get()));
}
