package org.mtr.registry;

import net.minecraft.item.ItemStack;
import org.mtr.MTR;

public final class ItemGroups {

	static {
		CORE = Registry.registerItemGroup("core", () -> new ItemStack(Items.RAILWAY_DASHBOARD.get()));
		RAILWAY_FACILITIES = Registry.registerItemGroup("railway_facilities", () -> new ItemStack(Blocks.TICKET_PROCESSOR.get()));
		STATION_BUILDING_BLOCKS = Registry.registerItemGroup("station_building_blocks", () -> new ItemStack(Blocks.LOGO.get()));
		ESCALATORS_LIFTS = Registry.registerItemGroup("escalators_lifts", () -> new ItemStack(Items.ESCALATOR.get()));
	}

	public static final String CORE;
	public static final String RAILWAY_FACILITIES;
	public static final String STATION_BUILDING_BLOCKS;
	public static final String ESCALATORS_LIFTS;

	public static void init() {
		MTR.LOGGER.info("Registering Minecraft Transit Railway creative mode tabs");
	}
}
