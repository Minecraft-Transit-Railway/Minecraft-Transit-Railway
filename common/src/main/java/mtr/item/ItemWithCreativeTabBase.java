package mtr.item;

import mtr.CreativeModeTabs;
import mtr.mappings.PlaceOnWaterBlockItem;
import mtr.mappings.RegistryUtilities;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class ItemWithCreativeTabBase extends Item {

	public final CreativeModeTabs.Wrapper creativeModeTab;

	public ItemWithCreativeTabBase(CreativeModeTabs.Wrapper creativeModeTab) {
		super(RegistryUtilities.createItemProperties(creativeModeTab::get));
		this.creativeModeTab = creativeModeTab;
	}

	public ItemWithCreativeTabBase(CreativeModeTabs.Wrapper creativeModeTab, Function<Properties, Properties> propertiesConsumer) {
		super(propertiesConsumer.apply(RegistryUtilities.createItemProperties(creativeModeTab::get)));
		this.creativeModeTab = creativeModeTab;
	}

	public static class ItemPlaceOnWater extends PlaceOnWaterBlockItem {

		public final CreativeModeTabs.Wrapper creativeModeTab;

		public ItemPlaceOnWater(CreativeModeTabs.Wrapper creativeModeTab, Block block) {
			super(block, RegistryUtilities.createItemProperties(creativeModeTab::get));
			this.creativeModeTab = creativeModeTab;
		}
	}
}
