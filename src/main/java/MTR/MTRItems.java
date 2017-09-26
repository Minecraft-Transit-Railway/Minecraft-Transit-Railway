package MTR;

import MTR.items.ItemAPG;
import MTR.items.ItemBrush;
import MTR.items.ItemEscalator;
import MTR.items.ItemKillTrain;
import MTR.items.ItemLightRail1;
import MTR.items.ItemMTRPane;
import MTR.items.ItemMinecartSpecial;
import MTR.items.ItemPSD;
import MTR.items.ItemPlatformMarker;
import MTR.items.ItemRail;
import MTR.items.ItemSP1900;
import MTR.items.ItemSpawnPlatform;
import MTR.items.ItemStationName;
import MTR.items.ItemStationNamePole;
import MTR.items.ItemTrain;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MTRItems {

	public static ItemBase itemlightrail1;
	public static ItemBase itemsp1900;
	public static ItemBase itemminecartspecial;
	public static ItemBase itemapg;
	public static ItemBase itempsd;
	public static ItemBase itembrush;
	public static ItemBase itemescalator;
	public static ItemBase itemkilltrain;
	public static ItemBase itemmtrpane;
	public static ItemBase itemplatformmarker;
	public static ItemBase itemrail;
	public static ItemBase itemspawnplatform;
	public static ItemBase itemstationname;
	public static ItemBase itemstationnamepole;
	public static ItemBase itemtrain;

	public static void init() {
		itemlightrail1 = register(new ItemLightRail1());
		itemsp1900 = register(new ItemSP1900());
		itemminecartspecial = register(new ItemMinecartSpecial());
		itemtrain = register(new ItemTrain());
		itemapg = register(new ItemAPG());
		itempsd = register(new ItemPSD());
		itembrush = register(new ItemBrush());
		itemescalator = register(new ItemEscalator());
		itemkilltrain = register(new ItemKillTrain());
		itemmtrpane = register(new ItemMTRPane());
		itemplatformmarker = register(new ItemPlatformMarker());
		itemspawnplatform = register(new ItemSpawnPlatform());
		itemstationname = register(new ItemStationName());
		itemstationnamepole = register(new ItemStationNamePole());
		itemrail = register(new ItemRail());
	}

	private static <T extends Item> T register(T item) {
		GameRegistry.register(item);
		if (item instanceof ItemBase)
			((ItemBase) item).registerItemModel();
		return item;
	}
}
