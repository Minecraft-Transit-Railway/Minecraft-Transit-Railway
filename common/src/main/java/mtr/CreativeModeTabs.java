package mtr;

import mtr.mappings.Utilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public interface CreativeModeTabs {

	RegistryObject<CreativeModeTab> CORE = new RegistryObject<>(() -> Keys.LIFTS_ONLY ? Utilities.getDefaultTab() : Registry.getCreativeModeTab(new ResourceLocation(MTR.MOD_ID, "core"), () -> new ItemStack(Items.RAILWAY_DASHBOARD.get())));
	RegistryObject<CreativeModeTab> RAILWAY_FACILITIES = new RegistryObject<>(() -> Keys.LIFTS_ONLY ? Utilities.getDefaultTab() : Registry.getCreativeModeTab(new ResourceLocation(MTR.MOD_ID, "railway_facilities"), () -> new ItemStack(Blocks.TICKET_PROCESSOR.get())));
	RegistryObject<CreativeModeTab> STATION_BUILDING_BLOCKS = new RegistryObject<>(() -> Keys.LIFTS_ONLY ? Utilities.getDefaultTab() : Registry.getCreativeModeTab(new ResourceLocation(MTR.MOD_ID, "station_building_blocks"), () -> new ItemStack(Blocks.LOGO.get())));
	RegistryObject<CreativeModeTab> ESCALATORS_LIFTS = new RegistryObject<>(() -> Registry.getCreativeModeTab(new ResourceLocation(MTR.MOD_ID, "escalators_lifts"), () -> new ItemStack(Items.ESCALATOR.get())));
}
