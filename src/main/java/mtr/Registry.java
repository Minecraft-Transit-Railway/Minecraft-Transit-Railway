package mtr;

import mtr.block.BlockAPGDoor;
import mtr.block.BlockAPGGlass;
import mtr.block.BlockBridgeCreator;
import mtr.block.BlockDoorController;
import mtr.block.BlockEscalatorSide;
import mtr.block.BlockEscalatorStep;
import mtr.block.BlockLogo;
import mtr.block.BlockPSDDoor;
import mtr.block.BlockPSDGlass;
import mtr.block.BlockPSDGlassEnd;
import mtr.block.BlockPSDTop;
import mtr.block.BlockPlatform;
import mtr.block.BlockRailMarker;
import mtr.block.BlockRailScaffold;
import mtr.item.ItemAPG;
import mtr.item.ItemBrush;
import mtr.item.ItemEscalator;
import mtr.item.ItemLightRail1;
import mtr.item.ItemMTrain;
import mtr.item.ItemPSD;
import mtr.item.ItemRailPainter;
import mtr.item.ItemSP1900;
import mtr.item.ItemTemplate;
import mtr.tile.TileEntityAPGDoor;
import mtr.tile.TileEntityBridgeCreator;
import mtr.tile.TileEntityPSDDoor;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class Registry {
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		// Blocks
		final IForgeRegistry<Block> registry = event.getRegistry();

		registry.register(setBlockName(new BlockAPGDoor(), "apg_door"));
		registry.register(setBlockName(new BlockAPGGlass(), "apg_glass"));

		registry.register(setBlockName(new BlockBridgeCreator(), "bridge_creator"));
		registry.register(setBlockName(new BlockDoorController(), "door_controller"));

		registry.register(setBlockName(new BlockEscalatorSide(), "escalator_side"));
		registry.register(setBlockName(new BlockEscalatorStep(), "escalator_step"));

		registry.register(setBlockName(new BlockLogo(), "logo"));
		registry.register(setBlockName(new BlockPlatform(), "platform"));

		registry.register(setBlockName(new BlockPSDDoor(), "psd_door"));
		registry.register(setBlockName(new BlockPSDGlass(), "psd_glass"));
		registry.register(setBlockName(new BlockPSDGlassEnd(), "psd_glass_end"));

		registry.register(setBlockName(new BlockPSDTop(), "psd_top"));
		registry.register(setBlockName(new BlockRailMarker(), "rail_marker"));
		registry.register(setBlockName(new BlockRailScaffold(), "rail_scaffold"));

		// Tile entities
		GameRegistry.registerTileEntity(TileEntityAPGDoor.class, new ResourceLocation("apg_door"));
		GameRegistry.registerTileEntity(TileEntityBridgeCreator.class, new ResourceLocation("bridge_creator"));
		GameRegistry.registerTileEntity(TileEntityPSDDoor.class, new ResourceLocation("psd_door"));
	}

	@SubscribeEvent
	public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();

		registry.register(setItemBlock(Blocks.bridge_creator));
		registry.register(setItemBlock(Blocks.door_controller));
		registry.register(setItemBlock(Blocks.logo));
		registry.register(setItemBlock(Blocks.platform));
		registry.register(setItemBlock(Blocks.rail_scaffold));
	}

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		registry.register(setItemName(new ItemAPG(), "apg"));
		registry.register(setItemName(new ItemBrush(), "brush"));
		registry.register(setItemName(new ItemEscalator(), "escalator"));
		registry.register(setItemName(new ItemLightRail1(), "light_rail_1"));
		registry.register(setItemName(new ItemMTrain(), "m_train"));
		registry.register(setItemName(new ItemPSD(), "psd"));
		registry.register(setItemName(new ItemRailPainter(), "rail_painter"));
		registry.register(setItemName(new ItemSP1900(), "sp1900"));
		registry.register(setItemName(new ItemTemplate(), "template"));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerBlockModels(final ModelRegistryEvent event) {
		// Blocks
		registerBlockModel(Blocks.bridge_creator, 0);
		registerBlockModel(Blocks.door_controller, 0);
		registerBlockModel(Blocks.logo, 0);
		registerBlockModel(Blocks.platform, 0);
		registerBlockModel(Blocks.rail_scaffold, 0);
		// Items
		registerItemModel(Items.apg, 2);
		registerItemModel(Items.brush);
		registerItemModel(Items.escalator);
		registerItemModel(Items.light_rail_1);
		registerItemModel(Items.m_train, 2);
		registerItemModel(Items.psd, 3);
		registerItemModel(Items.rail_painter);
		registerItemModel(Items.sp1900, 3);
		registerItemModel(Items.template);
	}

	@SubscribeEvent
	public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
		final IForgeRegistry<EntityEntry> registry = event.getRegistry();
		registry.register(Entities.light_rail_1);
		registry.register(Entities.m_train);
		registry.register(Entities.sp1900);
	}

	private static Block setBlockName(Block block, String name) {
		block.setRegistryName(name);
		block.setUnlocalizedName(name);
		return block;
	}

	private static Item setItemBlock(Block block) {
		return setItemName(new ItemBlock(block), block.getRegistryName().getResourcePath());
	}

	private static Item setItemName(Item item, String name) {
		item.setRegistryName(name);
		item.setUnlocalizedName(name);
		return item;
	}

	private static void registerBlockModel(Block block, int metadata) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), metadata, new ModelResourceLocation(MTR.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}

	private static void registerItemModel(Item item) {
		registerItemModel(item, 1);
	}

	private static void registerItemModel(Item item, int metadataCount) {
		for (int i = 0; i < metadataCount; i++)
			ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(item.getRegistryName() + (metadataCount == 1 ? "" : "_" + i), "inventory"));
	}
}
