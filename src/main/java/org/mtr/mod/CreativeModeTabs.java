package org.mtr.mod;

import org.mtr.init.MTR;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.ItemConvertible;
import org.mtr.mapping.holder.ItemStack;
import org.mtr.mapping.registry.CreativeModeTabHolder;
import org.mtr.mapping.registry.Registry;

public final class CreativeModeTabs {

	static {
		CORE = Registry.createCreativeModeTabHolder(new Identifier(MTR.MOD_ID, "core"), () -> new ItemStack(new ItemConvertible(Items.RAILWAY_DASHBOARD.get().data)));
		RAILWAY_FACILITIES = Registry.createCreativeModeTabHolder(new Identifier(MTR.MOD_ID, "railway_facilities"), () -> new ItemStack(new ItemConvertible(Blocks.TICKET_PROCESSOR.get().data)));
		STATION_BUILDING_BLOCKS = Registry.createCreativeModeTabHolder(new Identifier(MTR.MOD_ID, "station_building_blocks"), () -> new ItemStack(new ItemConvertible(Blocks.LOGO.get().data)));
		ESCALATORS_LIFTS = Registry.createCreativeModeTabHolder(new Identifier(MTR.MOD_ID, "escalators_lifts"), () -> new ItemStack(new ItemConvertible(Items.ESCALATOR.get().data)));
	}

	public static final CreativeModeTabHolder CORE;
	public static final CreativeModeTabHolder RAILWAY_FACILITIES;
	public static final CreativeModeTabHolder STATION_BUILDING_BLOCKS;
	public static final CreativeModeTabHolder ESCALATORS_LIFTS;

	public static void init() {
		Init.LOGGER.info("Registering Minecraft Transit Railway creative mode tabs");
	}
}
