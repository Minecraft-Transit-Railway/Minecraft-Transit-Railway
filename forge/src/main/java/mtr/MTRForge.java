package mtr;

import mtr.client.CustomResources;
import mtr.item.ItemBlockEnchanted;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.DeferredRegisterHolder;
import mtr.mappings.ForgeUtilities;
import mtr.mappings.RegistryUtilitiesClient;
import mtr.render.RenderDrivingOverlay;
import mtr.render.RenderLift;
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

	static {
		if (Keys.LIFTS_ONLY) {
			MTRLifts.init(MTRForge::registerItem, MTRForge::registerBlock, MTRForge::registerBlock, MTRForge::registerBlockEntityType, MTRForge::registerEntityType);
		} else {
			MTR.init(MTRForge::registerItem, MTRForge::registerBlock, MTRForge::registerBlock, MTRForge::registerEnchantedBlock, MTRForge::registerBlockEntityType, MTRForge::registerEntityType, MTRForge::registerSoundEvent);
		}
	}

	public MTRForge() {
		final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ForgeUtilities.registerModEventBus(MTR.MOD_ID, eventBus);

		ITEMS.register();
		BLOCKS.register();
		BLOCK_ENTITY_TYPES.register();
		ENTITY_TYPES.register();
		SOUND_EVENTS.register();

		eventBus.register(MTRModEventBus.class);
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			ForgeUtilities.renderTickAction(MTRClient::incrementGameTick);
			if (!Keys.LIFTS_ONLY) {
				ForgeUtilities.registerEntityRenderer(EntityTypes.SEAT::get, RenderTrains::new);
			}
			ForgeUtilities.registerEntityRenderer(EntityTypes.LiftType.SIZE_2_2.registryObject::get, RenderLift::new);
			ForgeUtilities.registerEntityRenderer(EntityTypes.LiftType.SIZE_2_2_DOUBLE_SIDED.registryObject::get, RenderLift::new);
			ForgeUtilities.registerEntityRenderer(EntityTypes.LiftType.SIZE_3_2.registryObject::get, RenderLift::new);
			ForgeUtilities.registerEntityRenderer(EntityTypes.LiftType.SIZE_3_2_DOUBLE_SIDED.registryObject::get, RenderLift::new);
			ForgeUtilities.registerEntityRenderer(EntityTypes.LiftType.SIZE_3_3.registryObject::get, RenderLift::new);
			ForgeUtilities.registerEntityRenderer(EntityTypes.LiftType.SIZE_3_3_DOUBLE_SIDED.registryObject::get, RenderLift::new);
			ForgeUtilities.registerEntityRenderer(EntityTypes.LiftType.SIZE_4_3.registryObject::get, RenderLift::new);
			ForgeUtilities.registerEntityRenderer(EntityTypes.LiftType.SIZE_4_3_DOUBLE_SIDED.registryObject::get, RenderLift::new);
			ForgeUtilities.registerEntityRenderer(EntityTypes.LiftType.SIZE_4_4.registryObject::get, RenderLift::new);
			ForgeUtilities.registerEntityRenderer(EntityTypes.LiftType.SIZE_4_4_DOUBLE_SIDED.registryObject::get, RenderLift::new);
			ForgeUtilities.renderGameOverlayAction(RenderDrivingOverlay::render);
			MinecraftForge.EVENT_BUS.register(ForgeUtilities.Events.class);
			eventBus.register(ForgeUtilities.RegisterEntityRenderer.class);
		});
	}

	private static void registerItem(String path, RegistryObject<Item> item) {
		ITEMS.register(path, item::get);
	}

	private static void registerBlock(String path, RegistryObject<Block> block) {
		BLOCKS.register(path, block::get);
	}

	private static void registerBlock(String path, RegistryObject<Block> block, CreativeModeTab itemGroup) {
		registerBlock(path, block);
		ITEMS.register(path, () -> new BlockItem(block.get(), new Item.Properties().tab(itemGroup)));
	}

	private static void registerEnchantedBlock(String path, RegistryObject<Block> block, CreativeModeTab itemGroup) {
		registerBlock(path, block);
		ITEMS.register(path, () -> new ItemBlockEnchanted(block.get(), new Item.Properties().tab(itemGroup)));
	}

	private static void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
		BLOCK_ENTITY_TYPES.register(path, blockEntityType::get);
	}

	private static void registerEntityType(String path, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
		ENTITY_TYPES.register(path, entityType::get);
	}

	private static void registerSoundEvent(String path, SoundEvent soundEvent) {
		SOUND_EVENTS.register(path, () -> soundEvent);
	}

	private static class MTRModEventBus {

		@SubscribeEvent
		public static void onClientSetupEvent(FMLClientSetupEvent event) {
			if (Keys.LIFTS_ONLY) {
				MTRClientLifts.init();
			} else {
				MTRClient.init();
			}
			RegistryUtilitiesClient.registerTextureStitchEvent(textureAtlas -> {
				if (textureAtlas.location().getPath().equals("textures/atlas/blocks.png")) {
					CustomResources.reload(Minecraft.getInstance().getResourceManager());
				}
			});
		}
	}
}
