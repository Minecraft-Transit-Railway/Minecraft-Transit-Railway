package mtr.item;

import mtr.mappings.PlaceOnWaterBlockItem;
import mtr.mappings.RegistryUtilities;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class ItemWithCreativeTabBase extends Item {

	public final CreativeModeTab creativeModeTab;

	public ItemWithCreativeTabBase(CreativeModeTab creativeModeTab) {
		super(RegistryUtilities.createItemProperties(creativeModeTab));
		this.creativeModeTab = creativeModeTab;
	}

	public ItemWithCreativeTabBase(CreativeModeTab creativeModeTab, Function<Properties, Properties> propertiesConsumer) {
		super(propertiesConsumer.apply(RegistryUtilities.createItemProperties(creativeModeTab)));
		this.creativeModeTab = creativeModeTab;
	}

	public static class ItemPlaceOnWater extends PlaceOnWaterBlockItem {

		public final CreativeModeTab creativeModeTab;

		public ItemPlaceOnWater(CreativeModeTab creativeModeTab, Block block) {
			super(block, RegistryUtilities.createItemProperties(creativeModeTab));
			this.creativeModeTab = creativeModeTab;
		}
	}
}
