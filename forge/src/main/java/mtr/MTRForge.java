package mtr;

import mtr.client.CustomResources;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.DeferredRegisterHolder;
import mtr.mappings.ForgeUtilities;
import mtr.mappings.RegistryUtilitiesClient;
import mtr.render.RenderTrains;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MTR.MOD_ID)
public class MTRForge {

	private static final DeferredRegisterHolder<Item> ITEMS = new DeferredRegisterHolder<>(MTR.MOD_ID, Registry.ITEM_REGISTRY);
	private static final DeferredRegisterHolder<Block> BLOCKS = new DeferredRegisterHolder<>(MTR.MOD_ID, Registry.BLOCK_REGISTRY);
	private static final DeferredRegisterHolder<BlockEntityType<?>> BLOCK_ENTITY_TYPES = new DeferredRegisterHolder<>(MTR.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
	private static final DeferredRegisterHolder<EntityType<?>> ENTITY_TYPES = new DeferredRegisterHolder<>(MTR.MOD_ID, Registry.ENTITY_TYPE_REGISTRY);
	private static final DeferredRegisterHolder<SoundEvent> SOUND_EVENTS = new DeferredRegisterHolder<>(MTR.MOD_ID, Registry.SOUND_EVENT_REGISTRY);

	public MTRForge() {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			ForgeUtilities.renderTickAction(MTRClient::incrementGameTick);
			ForgeUtilities.registerEntityRenderer(EntityTypes.SEAT, RenderTrains::new);
			MinecraftForge.EVENT_BUS.register(ForgeUtilities.RenderTick.class);
		});

		final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ForgeUtilities.registerModEventBus(MTR.MOD_ID, eventBus);
		eventBus.register(MTRModEventBus.class);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> eventBus.register(ForgeUtilities.RegisterEntityRenderer.class));

		MTR.init(MTRForge::registerItem, MTRForge::registerBlock, MTRForge::registerBlock, MTRForge::registerBlockEntityType, MTRForge::registerEntityType, MTRForge::registerSoundEvent);
		ITEMS.register();
		BLOCKS.register();
		BLOCK_ENTITY_TYPES.register();
		ENTITY_TYPES.register();
		SOUND_EVENTS.register();
	}

	private static void registerItem(String path, Item item) {
		ITEMS.register(path, () -> item);
	}

	private static void registerBlock(String path, Block block) {
		BLOCKS.register(path, () -> block);
	}

	private static void registerBlock(String path, Block block, CreativeModeTab itemGroup) {
		registerBlock(path, block);
		ITEMS.register(path, () -> new BlockItem(block, new Item.Properties().tab(itemGroup)));
	}

	private static <T extends BlockEntityMapper> void registerBlockEntityType(String path, BlockEntityType<T> blockEntityType) {
		BLOCK_ENTITY_TYPES.register(path, () -> blockEntityType);
	}

	private static <T extends Entity> void registerEntityType(String path, EntityType<T> entityType) {
		ENTITY_TYPES.register(path, () -> entityType);
	}

	private static void registerSoundEvent(String path, SoundEvent soundEvent) {
		SOUND_EVENTS.register(path, () -> soundEvent);
	}

	private static class MTRModEventBus {

		@SubscribeEvent
		public static void onClientSetupEvent(FMLClientSetupEvent event) {
			MTRClient.init();
			RegistryUtilitiesClient.registerTextureStitchEvent(textureAtlas -> {
				if (textureAtlas.location().getPath().equals("textures/atlas/blocks.png")) {
					CustomResources.reload(Minecraft.getInstance().getResourceManager());
				}
			});
		}
	}
}
