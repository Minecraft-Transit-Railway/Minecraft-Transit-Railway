package mtr;

import mtr.mappings.Utilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public interface CreativeModeTabs {

	Wrapper CORE = Keys.LIFTS_ONLY ? new Wrapper() : new Wrapper(new ResourceLocation(MTR.MOD_ID, "core"), () -> new ItemStack(Items.RAILWAY_DASHBOARD.get()));
	Wrapper RAILWAY_FACILITIES = Keys.LIFTS_ONLY ? new Wrapper() : new Wrapper(new ResourceLocation(MTR.MOD_ID, "railway_facilities"), () -> new ItemStack(Blocks.TICKET_PROCESSOR.get()));
	Wrapper STATION_BUILDING_BLOCKS = Keys.LIFTS_ONLY ? new Wrapper() : new Wrapper(new ResourceLocation(MTR.MOD_ID, "station_building_blocks"), () -> new ItemStack(Blocks.LOGO.get()));
	Wrapper ESCALATORS_LIFTS = new Wrapper(new ResourceLocation(MTR.MOD_ID, "escalators_lifts"), () -> new ItemStack(Items.ESCALATOR.get()));

	class Wrapper {

		public final ResourceLocation resourceLocation;
		private final Supplier<CreativeModeTab> creativeModeTabSupplier;
		private CreativeModeTab creativeModeTab;

		public Wrapper(ResourceLocation resourceLocation, Supplier<ItemStack> itemSupplier) {
			this.resourceLocation = resourceLocation;
			creativeModeTabSupplier = Registry.getCreativeModeTab(resourceLocation, itemSupplier);
		}

		public CreativeModeTab get() {
			if (creativeModeTab == null) {
				creativeModeTab = creativeModeTabSupplier.get();
			}
			return creativeModeTab;
		}

		public Wrapper() {
			resourceLocation = new ResourceLocation("");
			creativeModeTabSupplier = Utilities::getDefaultTab;
		}
	}
}
