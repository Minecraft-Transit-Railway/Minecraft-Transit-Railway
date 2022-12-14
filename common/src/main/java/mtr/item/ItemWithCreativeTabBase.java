package mtr.item;

import mtr.RegistryObject;
import mtr.mappings.PlaceOnWaterBlockItem;
import mtr.mappings.RegistryUtilities;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class ItemWithCreativeTabBase extends Item {

	public final RegistryObject<CreativeModeTab> creativeModeTab;

	public ItemWithCreativeTabBase(RegistryObject<CreativeModeTab> creativeModeTab) {
		super(RegistryUtilities.createItemProperties(creativeModeTab::get));
		this.creativeModeTab = creativeModeTab;
	}

	public ItemWithCreativeTabBase(RegistryObject<CreativeModeTab> creativeModeTab, Function<Properties, Properties> propertiesConsumer) {
		super(propertiesConsumer.apply(RegistryUtilities.createItemProperties(creativeModeTab::get)));
		this.creativeModeTab = creativeModeTab;
	}

	public static class ItemPlaceOnWater extends PlaceOnWaterBlockItem {

		public final RegistryObject<CreativeModeTab> creativeModeTab;

		public ItemPlaceOnWater(RegistryObject<CreativeModeTab> creativeModeTab, Block block) {
			super(block, RegistryUtilities.createItemProperties(creativeModeTab::get));
			this.creativeModeTab = creativeModeTab;
		}
	}
}
