package mtr;

import mtr.data.RailwayData;
import mtr.entity.EntityLightRail1;
import mtr.entity.EntityMTrain;
import mtr.entity.EntitySP1900;
import mtr.packet.PacketTrainDataGuiServer;
import mtr.tile.TileEntityAPGDoor;
import mtr.tile.TileEntityPSDDoor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BedItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class MTR implements ModInitializer {

	public static final String MOD_ID = "mtr";

	public static final BlockEntityType<TileEntityAPGDoor> APG_DOOR = registerTileEntity("apg_door", TileEntityAPGDoor::new, Blocks.APG_DOOR);
	public static final BlockEntityType<TileEntityPSDDoor> PSD_DOOR = registerTileEntity("psd_door", TileEntityPSDDoor::new, Blocks.PSD_DOOR);

	public static final EntityType<EntitySP1900> SP1900 = registerEntity("sp1900", EntitySP1900::new, 1, 1);
	public static final EntityType<EntityMTrain> M_TRAIN = registerEntity("m_train", EntityMTrain::new, 1, 1);
	public static final EntityType<EntityLightRail1> LIGHT_RAIL_1 = registerEntity("light_rail_1", EntityLightRail1::new, 1, 1);

	@Override
	public void onInitialize() {
		registerItem("apg_door", Items.APG_DOOR);
		registerItem("apg_glass", Items.APG_GLASS);
		registerItem("apg_glass_end", Items.APG_GLASS_END);
		registerItem("brush", Items.BRUSH);
		registerItem("dashboard", Items.DASHBOARD);
		registerItem("escalator", Items.ESCALATOR);
		registerItem("psd_door", Items.PSD_DOOR);
		registerItem("psd_glass", Items.PSD_GLASS);
		registerItem("psd_glass_end", Items.PSD_GLASS_END);

		registerBlock("apg_door", Blocks.APG_DOOR);
		registerBlock("apg_glass", Blocks.APG_GLASS);
		registerBlock("apg_glass_end", Blocks.APG_GLASS_END);
		registerBlock("ceiling", Blocks.CEILING, new BlockItem(Blocks.CEILING, new Item.Settings().group(ItemGroup.DECORATIONS)));
		registerBlock("escalator_side", Blocks.ESCALATOR_SIDE);
		registerBlock("escalator_step", Blocks.ESCALATOR_STEP);
		registerBlock("logo", Blocks.LOGO, new BlockItem(Blocks.LOGO, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		registerBlock("pids_1", Blocks.PIDS_1, new BedItem(Blocks.PIDS_1, new Item.Settings().group(ItemGroup.DECORATIONS)));
		registerBlock("platform", Blocks.PLATFORM, new BlockItem(Blocks.PLATFORM, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		registerBlock("platform_rail", Blocks.PLATFORM_RAIL, new BlockItem(Blocks.PLATFORM_RAIL, new Item.Settings().group(ItemGroup.TRANSPORTATION)));
		registerBlock("psd_door", Blocks.PSD_DOOR);
		registerBlock("psd_glass", Blocks.PSD_GLASS);
		registerBlock("psd_glass_end", Blocks.PSD_GLASS_END);
		registerBlock("psd_top", Blocks.PSD_TOP);

		ServerSidePacketRegistry.INSTANCE.register(PacketTrainDataGuiServer.ID, PacketTrainDataGuiServer::receiveC2S);

		ServerTickEvents.START_SERVER_TICK.register((event) -> event.getWorlds().forEach(world -> {
			RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				railwayData.simulateTrains(world);
			}
		}));
	}

	private static void registerItem(String path, Item item) {
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, path), item);
	}

	private static void registerBlock(String path, Block block) {
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, path), block);
	}

	private static void registerBlock(String path, Block block, BlockItem blockItem) {
		registerBlock(path, block);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, path), blockItem);
	}

	private static <T extends BlockEntity> BlockEntityType<T> registerTileEntity(String path, Supplier<T> supplier, Block block) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":" + path, BlockEntityType.Builder.create(supplier, block).build(null));
	}

	private static <T extends Entity> EntityType<T> registerEntity(String path, EntityType.EntityFactory<T> factory, float width, float height) {
		return Registry.register(Registry.ENTITY_TYPE, new Identifier(MTR.MOD_ID, path), FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).dimensions(EntityDimensions.fixed(width, height)).build());
	}
}