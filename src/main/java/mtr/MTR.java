package mtr;

import mtr.block.*;
import mtr.data.RailwayData;
import mtr.entity.EntitySeat;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class MTR implements ModInitializer {

	public static final String MOD_ID = "mtr";

	public static final EntityType<EntitySeat> SEAT = registerEntity("seat", EntitySeat::new);

	public static final BlockEntityType<BlockClock.TileEntityClock> CLOCK_TILE_ENTITY = registerTileEntity("clock", BlockClock.TileEntityClock::new, Blocks.CLOCK);
	public static final BlockEntityType<BlockPSDTop.TileEntityPSDTop> PSD_TOP_TILE_ENTITY = registerTileEntity("psd_top", BlockPSDTop.TileEntityPSDTop::new, Blocks.PSD_TOP);
	public static final BlockEntityType<BlockAPGGlass.TileEntityAPGGlass> APG_GLASS_TILE_ENTITY = registerTileEntity("apg_glass", BlockAPGGlass.TileEntityAPGGlass::new, Blocks.APG_GLASS);
	public static final BlockEntityType<BlockRail.TileEntityRail> RAIL_TILE_ENTITY = registerTileEntity("rail", BlockRail.TileEntityRail::new, Blocks.RAIL);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_2_EVEN_TILE_ENTITY = registerTileEntity("railway_sign_2_even", () -> new BlockRailwaySign.TileEntityRailwaySign(2, false), Blocks.RAILWAY_SIGN_2_EVEN);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_2_ODD_TILE_ENTITY = registerTileEntity("railway_sign_2_odd", () -> new BlockRailwaySign.TileEntityRailwaySign(2, true), Blocks.RAILWAY_SIGN_2_ODD);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_3_EVEN_TILE_ENTITY = registerTileEntity("railway_sign_3_even", () -> new BlockRailwaySign.TileEntityRailwaySign(3, false), Blocks.RAILWAY_SIGN_3_EVEN);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_3_ODD_TILE_ENTITY = registerTileEntity("railway_sign_3_odd", () -> new BlockRailwaySign.TileEntityRailwaySign(3, true), Blocks.RAILWAY_SIGN_3_ODD);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_4_EVEN_TILE_ENTITY = registerTileEntity("railway_sign_4_even", () -> new BlockRailwaySign.TileEntityRailwaySign(4, false), Blocks.RAILWAY_SIGN_4_EVEN);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_4_ODD_TILE_ENTITY = registerTileEntity("railway_sign_4_odd", () -> new BlockRailwaySign.TileEntityRailwaySign(4, true), Blocks.RAILWAY_SIGN_4_ODD);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_5_EVEN_TILE_ENTITY = registerTileEntity("railway_sign_5_even", () -> new BlockRailwaySign.TileEntityRailwaySign(5, false), Blocks.RAILWAY_SIGN_5_EVEN);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_5_ODD_TILE_ENTITY = registerTileEntity("railway_sign_5_odd", () -> new BlockRailwaySign.TileEntityRailwaySign(5, true), Blocks.RAILWAY_SIGN_5_ODD);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_6_EVEN_TILE_ENTITY = registerTileEntity("railway_sign_6_even", () -> new BlockRailwaySign.TileEntityRailwaySign(6, false), Blocks.RAILWAY_SIGN_6_EVEN);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_6_ODD_TILE_ENTITY = registerTileEntity("railway_sign_6_odd", () -> new BlockRailwaySign.TileEntityRailwaySign(6, true), Blocks.RAILWAY_SIGN_6_ODD);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_7_EVEN_TILE_ENTITY = registerTileEntity("railway_sign_7_even", () -> new BlockRailwaySign.TileEntityRailwaySign(7, false), Blocks.RAILWAY_SIGN_7_EVEN);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_7_ODD_TILE_ENTITY = registerTileEntity("railway_sign_7_odd", () -> new BlockRailwaySign.TileEntityRailwaySign(7, true), Blocks.RAILWAY_SIGN_7_ODD);
	public static final BlockEntityType<BlockRouteSignStandingLight.TileEntityRouteSignStandingLight> ROUTE_SIGN_STANDING_LIGHT_TILE_ENTITY = registerTileEntity("route_sign_standing_light", BlockRouteSignStandingLight.TileEntityRouteSignStandingLight::new, Blocks.ROUTE_SIGN_STANDING_LIGHT);
	public static final BlockEntityType<BlockRouteSignStandingMetal.TileEntityRouteSignStandingMetal> ROUTE_SIGN_STANDING_METAL_TILE_ENTITY = registerTileEntity("route_sign_standing_metal", BlockRouteSignStandingMetal.TileEntityRouteSignStandingMetal::new, Blocks.ROUTE_SIGN_STANDING_METAL);
	public static final BlockEntityType<BlockRouteSignWallLight.TileEntityRouteSignWallLight> ROUTE_SIGN_WALL_LIGHT_TILE_ENTITY = registerTileEntity("route_sign_wall_light", BlockRouteSignWallLight.TileEntityRouteSignWallLight::new, Blocks.ROUTE_SIGN_WALL_LIGHT);
	public static final BlockEntityType<BlockRouteSignWallMetal.TileEntityRouteSignWallMetal> ROUTE_SIGN_WALL_METAL_TILE_ENTITY = registerTileEntity("route_sign_wall_metal", BlockRouteSignWallMetal.TileEntityRouteSignWallMetal::new, Blocks.ROUTE_SIGN_WALL_METAL);
	public static final BlockEntityType<BlockStationNameEntrance.TileEntityStationNameEntrance> STATION_NAME_ENTRANCE_TILE_ENTITY = registerTileEntity("station_name_entrance", BlockStationNameEntrance.TileEntityStationNameEntrance::new, Blocks.STATION_NAME_ENTRANCE);
	public static final BlockEntityType<BlockStationNameWall.TileEntityStationNameWall> STATION_NAME_WALL_TILE_ENTITY = registerTileEntity("station_name_wall", BlockStationNameWall.TileEntityStationNameWall::new, Blocks.STATION_NAME_WALL);
	public static final BlockEntityType<BlockStationNameTallBlock.TileEntityStationNameTallBlock> STATION_NAME_TALL_BLOCK_TILE_ENTITY = registerTileEntity("station_name_tall_block", BlockStationNameTallBlock.TileEntityStationNameTallBlock::new, Blocks.STATION_NAME_TALL_BLOCK);
	public static final BlockEntityType<BlockStationNameTallWall.TileEntityStationNameTallWall> STATION_NAME_TALL_WALL_TILE_ENTITY = registerTileEntity("station_name_tall_wall", BlockStationNameTallWall.TileEntityStationNameTallWall::new, Blocks.STATION_NAME_TALL_WALL);

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
		registerItem("rail_connector_1_wooden", Items.RAIL_CONNECTOR_1_WOODEN);
		registerItem("rail_connector_2_stone", Items.RAIL_CONNECTOR_2_STONE);
		registerItem("rail_connector_3_iron", Items.RAIL_CONNECTOR_3_IRON);
		registerItem("rail_connector_4_obsidian", Items.RAIL_CONNECTOR_4_OBSIDIAN);
		registerItem("rail_connector_5_blaze", Items.RAIL_CONNECTOR_5_BLAZE);
		registerItem("rail_connector_6_diamond", Items.RAIL_CONNECTOR_6_DIAMOND);
		registerItem("rail_connector_platform", Items.RAIL_CONNECTOR_PLATFORM);
		registerItem("rail_remover", Items.RAIL_REMOVER);

		registerBlock("apg_door", Blocks.APG_DOOR);
		registerBlock("apg_glass", Blocks.APG_GLASS);
		registerBlock("apg_glass_end", Blocks.APG_GLASS_END);
		registerBlock("ceiling", Blocks.CEILING, ItemGroup.DECORATIONS);
		registerBlock("clock", Blocks.CLOCK, ItemGroup.DECORATIONS);
		registerBlock("escalator_side", Blocks.ESCALATOR_SIDE);
		registerBlock("escalator_step", Blocks.ESCALATOR_STEP);
		registerBlock("glass_fence_cio", Blocks.GLASS_FENCE_CIO, ItemGroup.DECORATIONS);
		registerBlock("glass_fence_ckt", Blocks.GLASS_FENCE_CKT, ItemGroup.DECORATIONS);
		registerBlock("glass_fence_heo", Blocks.GLASS_FENCE_HEO, ItemGroup.DECORATIONS);
		registerBlock("glass_fence_mos", Blocks.GLASS_FENCE_MOS, ItemGroup.DECORATIONS);
		registerBlock("glass_fence_plain", Blocks.GLASS_FENCE_PLAIN, ItemGroup.DECORATIONS);
		registerBlock("glass_fence_shm", Blocks.GLASS_FENCE_SHM, ItemGroup.DECORATIONS);
		registerBlock("glass_fence_stained", Blocks.GLASS_FENCE_STAINED, ItemGroup.DECORATIONS);
		registerBlock("glass_fence_stw", Blocks.GLASS_FENCE_STW, ItemGroup.DECORATIONS);
		registerBlock("glass_fence_tsh", Blocks.GLASS_FENCE_TSH, ItemGroup.DECORATIONS);
		registerBlock("glass_fence_wks", Blocks.GLASS_FENCE_WKS, ItemGroup.DECORATIONS);
		registerBlock("logo", Blocks.LOGO, ItemGroup.BUILDING_BLOCKS);
		registerBlock("pids_1", Blocks.PIDS_1, ItemGroup.DECORATIONS);
		registerBlock("platform", Blocks.PLATFORM, ItemGroup.BUILDING_BLOCKS);
		registerBlock("psd_door", Blocks.PSD_DOOR);
		registerBlock("psd_glass", Blocks.PSD_GLASS);
		registerBlock("psd_glass_end", Blocks.PSD_GLASS_END);
		registerBlock("psd_top", Blocks.PSD_TOP);
		registerBlock("rail", Blocks.RAIL, ItemGroup.TRANSPORTATION);
		registerBlock("railway_sign_2_even", Blocks.RAILWAY_SIGN_2_EVEN, ItemGroup.DECORATIONS);
		registerBlock("railway_sign_2_odd", Blocks.RAILWAY_SIGN_2_ODD, ItemGroup.DECORATIONS);
		registerBlock("railway_sign_3_even", Blocks.RAILWAY_SIGN_3_EVEN, ItemGroup.DECORATIONS);
		registerBlock("railway_sign_3_odd", Blocks.RAILWAY_SIGN_3_ODD, ItemGroup.DECORATIONS);
		registerBlock("railway_sign_4_even", Blocks.RAILWAY_SIGN_4_EVEN, ItemGroup.DECORATIONS);
		registerBlock("railway_sign_4_odd", Blocks.RAILWAY_SIGN_4_ODD, ItemGroup.DECORATIONS);
		registerBlock("railway_sign_5_even", Blocks.RAILWAY_SIGN_5_EVEN, ItemGroup.DECORATIONS);
		registerBlock("railway_sign_5_odd", Blocks.RAILWAY_SIGN_5_ODD, ItemGroup.DECORATIONS);
		registerBlock("railway_sign_6_even", Blocks.RAILWAY_SIGN_6_EVEN, ItemGroup.DECORATIONS);
		registerBlock("railway_sign_6_odd", Blocks.RAILWAY_SIGN_6_ODD, ItemGroup.DECORATIONS);
		registerBlock("railway_sign_7_even", Blocks.RAILWAY_SIGN_7_EVEN, ItemGroup.DECORATIONS);
		registerBlock("railway_sign_7_odd", Blocks.RAILWAY_SIGN_7_ODD, ItemGroup.DECORATIONS);
		registerBlock("railway_sign_middle", Blocks.RAILWAY_SIGN_MIDDLE);
		registerBlock("route_sign_standing_light", Blocks.ROUTE_SIGN_STANDING_LIGHT, ItemGroup.DECORATIONS);
		registerBlock("route_sign_standing_metal", Blocks.ROUTE_SIGN_STANDING_METAL, ItemGroup.DECORATIONS);
		registerBlock("route_sign_wall_light", Blocks.ROUTE_SIGN_WALL_LIGHT, ItemGroup.DECORATIONS);
		registerBlock("route_sign_wall_metal", Blocks.ROUTE_SIGN_WALL_METAL, ItemGroup.DECORATIONS);
		registerBlock("station_color_andesite", Blocks.STATION_COLOR_ANDESITE, ItemGroup.DECORATIONS);
		registerBlock("station_color_bedrock", Blocks.STATION_COLOR_BEDROCK, ItemGroup.DECORATIONS);
		registerBlock("station_color_birch_wood", Blocks.STATION_COLOR_BIRCH_WOOD, ItemGroup.DECORATIONS);
		registerBlock("station_color_bone_block", Blocks.STATION_COLOR_BONE_BLOCK, ItemGroup.DECORATIONS);
		registerBlock("station_color_chiseled_quartz_block", Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK, ItemGroup.DECORATIONS);
		registerBlock("station_color_chiseled_stone_bricks", Blocks.STATION_COLOR_CHISELED_STONE_BRICKS, ItemGroup.DECORATIONS);
		registerBlock("station_color_clay", Blocks.STATION_COLOR_CLAY, ItemGroup.DECORATIONS);
		registerBlock("station_color_coal_ore", Blocks.STATION_COLOR_COAL_ORE, ItemGroup.DECORATIONS);
		registerBlock("station_color_cobblestone", Blocks.STATION_COLOR_COBBLESTONE, ItemGroup.DECORATIONS);
		registerBlock("station_color_concrete", Blocks.STATION_COLOR_CONCRETE, ItemGroup.DECORATIONS);
		registerBlock("station_color_concrete_powder", Blocks.STATION_COLOR_CONCRETE_POWDER, ItemGroup.DECORATIONS);
		registerBlock("station_color_cracked_stone_bricks", Blocks.STATION_COLOR_CRACKED_STONE_BRICKS, ItemGroup.DECORATIONS);
		registerBlock("station_color_dark_prismarine", Blocks.STATION_COLOR_DARK_PRISMARINE, ItemGroup.DECORATIONS);
		registerBlock("station_color_diorite", Blocks.STATION_COLOR_DIORITE, ItemGroup.DECORATIONS);
		registerBlock("station_color_gravel", Blocks.STATION_COLOR_GRAVEL, ItemGroup.DECORATIONS);
		registerBlock("station_color_iron_block", Blocks.STATION_COLOR_IRON_BLOCK, ItemGroup.DECORATIONS);
		registerBlock("station_color_metal", Blocks.STATION_COLOR_METAL, ItemGroup.DECORATIONS);
		registerBlock("station_color_planks", Blocks.STATION_COLOR_PLANKS, ItemGroup.DECORATIONS);
		registerBlock("station_color_polished_andesite", Blocks.STATION_COLOR_POLISHED_ANDESITE, ItemGroup.DECORATIONS);
		registerBlock("station_color_polished_diorite", Blocks.STATION_COLOR_POLISHED_DIORITE, ItemGroup.DECORATIONS);
		registerBlock("station_color_purpur_block", Blocks.STATION_COLOR_PURPUR_BLOCK, ItemGroup.DECORATIONS);
		registerBlock("station_color_purpur_pillar", Blocks.STATION_COLOR_PURPUR_PILLAR, ItemGroup.DECORATIONS);
		registerBlock("station_color_quartz_block", Blocks.STATION_COLOR_QUARTZ_BLOCK, ItemGroup.DECORATIONS);
		registerBlock("station_color_quartz_bricks", Blocks.STATION_COLOR_QUARTZ_BRICKS, ItemGroup.DECORATIONS);
		registerBlock("station_color_quartz_pillar", Blocks.STATION_COLOR_QUARTZ_PILLAR, ItemGroup.DECORATIONS);
		registerBlock("station_color_smooth_quartz", Blocks.STATION_COLOR_SMOOTH_QUARTZ, ItemGroup.DECORATIONS);
		registerBlock("station_color_smooth_stone", Blocks.STATION_COLOR_SMOOTH_STONE, ItemGroup.DECORATIONS);
		registerBlock("station_color_snow_block", Blocks.STATION_COLOR_SNOW_BLOCK, ItemGroup.DECORATIONS);
		registerBlock("station_color_stained_glass", Blocks.STATION_COLOR_STAINED_GLASS, ItemGroup.DECORATIONS);
		registerBlock("station_color_stone", Blocks.STATION_COLOR_STONE, ItemGroup.DECORATIONS);
		registerBlock("station_color_stone_bricks", Blocks.STATION_COLOR_STONE_BRICKS, ItemGroup.DECORATIONS);
		registerBlock("station_color_wool", Blocks.STATION_COLOR_WOOL, ItemGroup.DECORATIONS);
		registerBlock("station_name_entrance", Blocks.STATION_NAME_ENTRANCE, ItemGroup.DECORATIONS);
		registerBlock("station_name_tall_block", Blocks.STATION_NAME_TALL_BLOCK, ItemGroup.DECORATIONS);
		registerBlock("station_name_tall_wall", Blocks.STATION_NAME_TALL_WALL, ItemGroup.DECORATIONS);
		registerBlock("station_name_wall", Blocks.STATION_NAME_WALL, ItemGroup.DECORATIONS);
		registerBlock("station_pole", Blocks.STATION_POLE, ItemGroup.DECORATIONS);
		registerBlock("ticket_machine", Blocks.TICKET_MACHINE, ItemGroup.DECORATIONS);

		ServerPlayNetworking.registerGlobalReceiver(IPacket.ID_STATIONS_AND_ROUTES, (minecraftServer, player, handler, packet, sender) -> PacketTrainDataGuiServer.receiveStationsAndRoutesC2S(minecraftServer, player, packet));
		ServerPlayNetworking.registerGlobalReceiver(IPacket.ID_PLATFORM, (minecraftServer, player, handler, packet, sender) -> PacketTrainDataGuiServer.receivePlatformC2S(minecraftServer, player, packet));
		ServerPlayNetworking.registerGlobalReceiver(IPacket.ID_SIGN_TYPES, (minecraftServer, player, handler, packet, sender) -> PacketTrainDataGuiServer.receiveSignTypesC2S(minecraftServer, player, packet));

		ServerTickEvents.START_SERVER_TICK.register(minecraftServer -> minecraftServer.getWorlds().forEach(serverWorld -> {
			RailwayData railwayData = RailwayData.getInstance(serverWorld);
			if (railwayData != null) {
				railwayData.simulateTrains(serverWorld);
			}
		}));
		ServerEntityEvents.ENTITY_LOAD.register((entity, serverWorld) -> {
			if (entity instanceof ServerPlayerEntity) {
				final RailwayData railwayData = RailwayData.getInstance(serverWorld);
				if (railwayData != null) {
					PacketTrainDataGuiServer.broadcastS2C(serverWorld, railwayData);
				}
				final EntitySeat seat = new EntitySeat(serverWorld, entity.getX(), entity.getY(), entity.getZ());
				seat.setPlayerId(entity.getUuid());
				serverWorld.spawnEntity(seat);
			}
		});
	}

	private static void registerItem(String path, Item item) {
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, path), item);
	}

	private static void registerBlock(String path, Block block) {
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, path), block);
	}

	private static void registerBlock(String path, Block block, ItemGroup itemGroup) {
		registerBlock(path, block);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, path), new BlockItem(block, new Item.Settings().group(itemGroup)));
	}

	private static <T extends BlockEntity> BlockEntityType<T> registerTileEntity(String path, Supplier<T> supplier, Block block) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":" + path, BlockEntityType.Builder.create(supplier, block).build(null));
	}

	private static <T extends Entity> EntityType<T> registerEntity(String path, EntityType.EntityFactory<T> factory) {
		return Registry.register(Registry.ENTITY_TYPE, new Identifier(MOD_ID, path), FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).dimensions(EntityDimensions.fixed(0.125F, 0.125F)).build());
	}
}