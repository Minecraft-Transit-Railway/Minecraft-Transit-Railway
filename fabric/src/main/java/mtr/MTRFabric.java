package mtr;

import mtr.item.ItemBlockEnchanted;
import mtr.mappings.BlockEntityMapper;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MTRFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		MTR.init(MTRFabric::registerItem, MTRFabric::registerBlock, MTRFabric::registerBlock, MTRFabric::registerEnchantedBlock, MTRFabric::registerBlockEntityType, MTRFabric::registerEntityType, MTRFabric::registerSoundEvent);
	}

	private static void registerItem(String path, Item item) {
		Registry.register(Registry.ITEM, new ResourceLocation(MTR.MOD_ID, path), item);
	}

	private static void registerBlock(String path, Block block) {
		Registry.register(Registry.BLOCK, new ResourceLocation(MTR.MOD_ID, path), block);
	}

	private static void registerBlock(String path, Block block, CreativeModeTab itemGroup) {
		registerBlock(path, block);
		Registry.register(Registry.ITEM, new ResourceLocation(MTR.MOD_ID, path), new BlockItem(block, new Item.Properties().tab(itemGroup)));
	}

	private static void registerEnchantedBlock(String path, Block block, CreativeModeTab itemGroup) {
		registerBlock(path, block);
		Registry.register(Registry.ITEM, new ResourceLocation(MTR.MOD_ID, path), new ItemBlockEnchanted(block, new Item.Properties().tab(itemGroup)));
	}

	private static <T extends BlockEntityMapper> void registerBlockEntityType(String path, BlockEntityType<T> blockEntityType) {
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(MTR.MOD_ID, path), blockEntityType);
	}

	private static <T extends Entity> void registerEntityType(String path, EntityType<T> entityType) {
		Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(MTR.MOD_ID, path), entityType);
	}

	private static void registerSoundEvent(String path, SoundEvent soundEvent) {
		Registry.register(Registry.SOUND_EVENT, new ResourceLocation(MTR.MOD_ID, path), soundEvent);
	}
}
