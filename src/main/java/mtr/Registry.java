package mtr;

import mtr.block.BlockLogo;
import mtr.item.ItemBrush;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class Registry {
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		final IForgeRegistry<Block> registry = event.getRegistry();
		registry.register(setBlockName(new BlockLogo(), "logo"));
	}

	@SubscribeEvent
	public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		registry.register(setItemBlock(Blocks.logo));
	}

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		registry.register(setItemName(new ItemBrush(), "brush"));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerBlockModels(final ModelRegistryEvent event) {
		// Blocks
		registerBlockModel(Blocks.logo, 0);
		// Items
		registerItemModel(Items.brush, 0);
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

	private static void registerItemModel(Item item, int metadata) {
		ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
