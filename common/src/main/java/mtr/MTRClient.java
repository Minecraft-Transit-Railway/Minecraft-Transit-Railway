package mtr;

import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import minecraftmappings.NetworkUtilities;
import minecraftmappings.RegistryUtilitiesClient;
import mtr.block.BlockPIDS1;
import mtr.block.BlockPIDS2;
import mtr.block.BlockPIDS3;
import mtr.block.BlockTactileMap;
import mtr.config.Config;
import mtr.config.CustomResources;
import mtr.data.*;
import mtr.gui.ClientData;
import mtr.gui.IDrawing;
import mtr.item.ItemNodeModifierBase;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.*;
import mtr.sound.LoopingSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class MTRClient implements IPacket {

	public static boolean isReplayMod;
	public static final LoopingSoundInstance TACTILE_MAP_SOUND_INSTANCE = new LoopingSoundInstance("tactile_map_music");

	public static void init() {
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.APG_DOOR);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.APG_GLASS);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.APG_GLASS_END);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.CLOCK);
		RegistryUtilitiesClient.registerRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_CIO);
		RegistryUtilitiesClient.registerRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_CKT);
		RegistryUtilitiesClient.registerRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_HEO);
		RegistryUtilitiesClient.registerRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_MOS);
		RegistryUtilitiesClient.registerRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_PLAIN);
		RegistryUtilitiesClient.registerRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_SHM);
		RegistryUtilitiesClient.registerRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_STAINED);
		RegistryUtilitiesClient.registerRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_STW);
		RegistryUtilitiesClient.registerRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_TSH);
		RegistryUtilitiesClient.registerRenderType(RenderType.translucent(), Blocks.GLASS_FENCE_WKS);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.LOGO);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.PLATFORM);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.PLATFORM_INDENTED);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.PLATFORM_NA_1);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.PLATFORM_NA_1_INDENTED);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.PLATFORM_NA_2);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.PLATFORM_NA_2_INDENTED);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.PLATFORM_UK_1);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.PLATFORM_UK_1_INDENTED);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.PSD_DOOR_1);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.PSD_GLASS_1);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.PSD_GLASS_END_1);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.PSD_DOOR_2);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.PSD_GLASS_2);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.PSD_GLASS_END_2);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.RAIL);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.RUBBISH_BIN_1);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.SIGNAL_LIGHT_1);
		RegistryUtilitiesClient.registerRenderType(RenderType.translucent(), Blocks.STATION_COLOR_STAINED_GLASS);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.STATION_NAME_TALL_BLOCK);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.STATION_NAME_TALL_WALL);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.TICKET_BARRIER_ENTRANCE_1);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.TICKET_BARRIER_EXIT_1);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.TICKET_MACHINE);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.TICKET_PROCESSOR);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.TICKET_PROCESSOR_ENTRANCE);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.TICKET_PROCESSOR_EXIT);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.TICKET_PROCESSOR_ENQUIRY);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.TRAIN_ANNOUNCER);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.TRAIN_CARGO_LOADER);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.TRAIN_CARGO_UNLOADER);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.TRAIN_REDSTONE_SENSOR);
		RegistryUtilitiesClient.registerRenderType(RenderType.cutout(), Blocks.TRAIN_SCHEDULE_SENSOR);

		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_1_WOODEN, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_1_WOODEN_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_2_STONE, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_2_STONE_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_3_IRON, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_3_IRON_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_4_OBSIDIAN, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_4_OBSIDIAN_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_5_BLAZE, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_5_BLAZE_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_6_DIAMOND, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_6_DIAMOND_ONE_WAY, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_PLATFORM, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_SIDING, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_CONNECTOR_TURN_BACK, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.RAIL_REMOVER, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_WHITE, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_ORANGE, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_MAGENTA, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_LIGHT_BLUE, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_YELLOW, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_LIME, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_PINK, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_GRAY, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_LIGHT_GRAY, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_CYAN, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_PURPLE, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_BLUE, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_BROWN, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_GREEN, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_RED, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_CONNECTOR_BLACK, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_WHITE, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_ORANGE, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_MAGENTA, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_LIGHT_BLUE, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_YELLOW, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_LIME, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_PINK, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_GRAY, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_LIGHT_GRAY, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_CYAN, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_PURPLE, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_BLUE, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_BROWN, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_GREEN, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_RED, ItemNodeModifierBase.TAG_POS);
		RegistryUtilitiesClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.SIGNAL_REMOVER_BLACK, ItemNodeModifierBase.TAG_POS);

		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_SMALL_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 12, 1, 15, 16, 14, 14, false, false, true, 0xFF9900, 0xFF9900));
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_MEDIUM_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 12, -15, 15, 16, 30, 46, false, false, true, 0xFF9900, 0xFF9900));
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.ARRIVAL_PROJECTOR_1_LARGE_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, 16, -15, 15, 16, 46, 46, false, false, true, 0xFF9900, 0xFF9900));
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.CLOCK_TILE_ENTITY, RenderClock::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.PSD_TOP_TILE_ENTITY, RenderPSDTop::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.APG_GLASS_TILE_ENTITY, RenderAPGGlass::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_1_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS1.TileEntityBlockPIDS1.MAX_ARRIVALS, 1, 3.25F, 6, 2.5F, 30, true, false, false, 0xFF9900, 0xFF9900));
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_2_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS2.TileEntityBlockPIDS2.MAX_ARRIVALS, 1.5F, 7.5F, 6, 6.5F, 29, true, true, false, 0xFF9900, 0xFF9900));
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.PIDS_3_TILE_ENTITY, dispatcher -> new RenderPIDS<>(dispatcher, BlockPIDS3.TileEntityBlockPIDS3.MAX_ARRIVALS, 2.5F, 7.5F, 6, 6.5F, 27, true, false, false, 0xFF9900, 0x33CC00, 1.25F, true));
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_2_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_2_ODD_TILE_ENTITY, RenderRailwaySign::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_3_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_3_ODD_TILE_ENTITY, RenderRailwaySign::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_4_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_4_ODD_TILE_ENTITY, RenderRailwaySign::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_5_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_5_ODD_TILE_ENTITY, RenderRailwaySign::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_6_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_6_ODD_TILE_ENTITY, RenderRailwaySign::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_7_EVEN_TILE_ENTITY, RenderRailwaySign::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_7_ODD_TILE_ENTITY, RenderRailwaySign::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_STANDING_LIGHT_TILE_ENTITY, RenderRouteSign::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_STANDING_METAL_TILE_ENTITY, RenderRouteSign::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_WALL_LIGHT_TILE_ENTITY, RenderRouteSign::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.ROUTE_SIGN_WALL_METAL_TILE_ENTITY, RenderRouteSign::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_1, dispatcher -> new RenderSignalLight<>(dispatcher, true, false, 0xFF0000FF));
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_2, dispatcher -> new RenderSignalLight<>(dispatcher, false, false, 0xFF0000FF));
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_3, dispatcher -> new RenderSignalLight<>(dispatcher, true, true, 0xFF00FF00));
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_LIGHT_4, dispatcher -> new RenderSignalLight<>(dispatcher, false, true, 0xFF00FF00));
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_SEMAPHORE_1, dispatcher -> new RenderSignalSemaphore<>(dispatcher, true));
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.SIGNAL_SEMAPHORE_2, dispatcher -> new RenderSignalSemaphore<>(dispatcher, false));
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_ENTRANCE_TILE_ENTITY, RenderStationNameEntrance::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_BLOCK_TILE_ENTITY, RenderStationNameTall::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_WALL_TILE_ENTITY, RenderStationNameTall::new);
		RegistryUtilitiesClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_TILE_ENTITY, RenderStationNameWall::new);

		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_ANDESITE);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_BEDROCK);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_BIRCH_WOOD);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_BONE_BLOCK);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_CHISELED_STONE_BRICKS);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_CLAY);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_COAL_ORE);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_COBBLESTONE);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_CONCRETE);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_CONCRETE_POWDER);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_CRACKED_STONE_BRICKS);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_DARK_PRISMARINE);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_DIORITE);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_GRAVEL);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_IRON_BLOCK);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_METAL);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_PLANKS);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_POLISHED_ANDESITE);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_POLISHED_DIORITE);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_PURPUR_BLOCK);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_PURPUR_PILLAR);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_QUARTZ_BLOCK);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_QUARTZ_BRICKS);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_QUARTZ_PILLAR);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_SMOOTH_QUARTZ);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_SMOOTH_STONE);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_SNOW_BLOCK);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_STAINED_GLASS);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_STONE);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_STONE_BRICKS);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_WOOL);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_NAME_TALL_BLOCK);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_NAME_TALL_WALL);
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), Blocks.STATION_COLOR_POLE);

		NetworkUtilities.registerReceiverS2C(PACKET_CHUNK_S2C, (packet, context) -> PacketTrainDataGuiClient.receiveChunk(Minecraft.getInstance(), packet));
		NetworkUtilities.registerReceiverS2C(PACKET_OPEN_DASHBOARD_SCREEN, (packet, context) -> PacketTrainDataGuiClient.openDashboardScreenS2C(Minecraft.getInstance()));
		NetworkUtilities.registerReceiverS2C(PACKET_OPEN_PIDS_CONFIG_SCREEN, (packet, context) -> PacketTrainDataGuiClient.openPIDSConfigScreenS2C(Minecraft.getInstance(), packet));
		NetworkUtilities.registerReceiverS2C(PACKET_OPEN_RAILWAY_SIGN_SCREEN, (packet, context) -> PacketTrainDataGuiClient.openRailwaySignScreenS2C(Minecraft.getInstance(), packet));
		NetworkUtilities.registerReceiverS2C(PACKET_OPEN_TICKET_MACHINE_SCREEN, (packet, context) -> PacketTrainDataGuiClient.openTicketMachineScreenS2C(Minecraft.getInstance(), packet));
		NetworkUtilities.registerReceiverS2C(PACKET_OPEN_TRAIN_SENSOR_SCREEN, (packet, context) -> PacketTrainDataGuiClient.openTrainSensorScreenS2C(Minecraft.getInstance(), packet));
		NetworkUtilities.registerReceiverS2C(PACKET_OPEN_RESOURCE_PACK_CREATOR_SCREEN, (packet, context) -> PacketTrainDataGuiClient.openResourcePackCreatorScreen(Minecraft.getInstance()));
		NetworkUtilities.registerReceiverS2C(PACKET_ANNOUNCE, (packet, context) -> PacketTrainDataGuiClient.announceS2C(Minecraft.getInstance(), packet));
		NetworkUtilities.registerReceiverS2C(PACKET_GENERATE_PATH, (packet, context) -> PacketTrainDataGuiClient.generatePathS2C(Minecraft.getInstance(), packet));
		NetworkUtilities.registerReceiverS2C(PACKET_CREATE_RAIL, (packet, context) -> PacketTrainDataGuiClient.createRailS2C(Minecraft.getInstance(), packet));
		NetworkUtilities.registerReceiverS2C(PACKET_CREATE_SIGNAL, (packet, context) -> PacketTrainDataGuiClient.createSignalS2C(Minecraft.getInstance(), packet));
		NetworkUtilities.registerReceiverS2C(PACKET_REMOVE_NODE, (packet, context) -> PacketTrainDataGuiClient.removeNodeS2C(Minecraft.getInstance(), packet));
		NetworkUtilities.registerReceiverS2C(PACKET_REMOVE_RAIL, (packet, context) -> PacketTrainDataGuiClient.removeRailConnectionS2C(Minecraft.getInstance(), packet));
		NetworkUtilities.registerReceiverS2C(PACKET_REMOVE_SIGNALS, (packet, context) -> PacketTrainDataGuiClient.removeSignalsS2C(Minecraft.getInstance(), packet));
		NetworkUtilities.registerReceiverS2C(PACKET_UPDATE_STATION, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.STATIONS, ClientData.DATA_CACHE.stationIdMap, Station::new, false));
		NetworkUtilities.registerReceiverS2C(PACKET_UPDATE_PLATFORM, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.PLATFORMS, ClientData.DATA_CACHE.platformIdMap, null, false));
		NetworkUtilities.registerReceiverS2C(PACKET_UPDATE_SIDING, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.SIDINGS, ClientData.DATA_CACHE.sidingIdMap, null, false));
		NetworkUtilities.registerReceiverS2C(PACKET_UPDATE_ROUTE, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.ROUTES, ClientData.DATA_CACHE.routeIdMap, Route::new, false));
		NetworkUtilities.registerReceiverS2C(PACKET_UPDATE_DEPOT, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.DEPOTS, ClientData.DATA_CACHE.depotIdMap, Depot::new, false));
		NetworkUtilities.registerReceiverS2C(PACKET_DELETE_STATION, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.STATIONS, ClientData.DATA_CACHE.stationIdMap, Station::new, true));
		NetworkUtilities.registerReceiverS2C(PACKET_DELETE_PLATFORM, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.PLATFORMS, ClientData.DATA_CACHE.platformIdMap, null, true));
		NetworkUtilities.registerReceiverS2C(PACKET_DELETE_SIDING, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.SIDINGS, ClientData.DATA_CACHE.sidingIdMap, null, true));
		NetworkUtilities.registerReceiverS2C(PACKET_DELETE_ROUTE, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.ROUTES, ClientData.DATA_CACHE.routeIdMap, Route::new, true));
		NetworkUtilities.registerReceiverS2C(PACKET_DELETE_DEPOT, (packet, context) -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.DEPOTS, ClientData.DATA_CACHE.depotIdMap, Depot::new, true));
		NetworkUtilities.registerReceiverS2C(PACKET_WRITE_RAILS, (packet, context) -> ClientData.writeRails(Minecraft.getInstance(), packet));
		NetworkUtilities.registerReceiverS2C(PACKET_UPDATE_TRAINS, (packet, context) -> ClientData.updateTrains(Minecraft.getInstance(), packet));
		NetworkUtilities.registerReceiverS2C(PACKET_DELETE_TRAINS, (packet, context) -> ClientData.deleteTrains(Minecraft.getInstance(), packet));
		NetworkUtilities.registerReceiverS2C(PACKET_UPDATE_TRAIN_RIDING_POSITION, (packet, context) -> ClientData.updateTrainRidingPosition(Minecraft.getInstance(), packet));
		NetworkUtilities.registerReceiverS2C(PACKET_UPDATE_SCHEDULE, (packet, context) -> ClientData.updateSchedule(Minecraft.getInstance(), packet));

		BlockTactileMap.TileEntityTactileMap.updateSoundSource = TACTILE_MAP_SOUND_INSTANCE::setPos;
		BlockTactileMap.TileEntityTactileMap.onUse = pos -> {
			final Station station = RailwayData.getStation(ClientData.STATIONS, pos);
			if (station != null) {
				IDrawing.narrateOrAnnounce(IGui.insertTranslation("gui.mtr.welcome_station_cjk", "gui.mtr.welcome_station", 1, IGui.textOrUntitled(station.name)));
			}
		};

		Config.getPatreonList();
		Config.refreshProperties();
		CrowdinTranslate.downloadTranslations("minecraft-transit-railway", MTR.MOD_ID);

		RegistryUtilitiesClient.registerPlayerJoinEvent(player -> {
			Config.refreshProperties();
			isReplayMod = player.getClass().toGenericString().toLowerCase().contains("replaymod");
		});
		RegistryUtilitiesClient.registerTextureStitchEvent(textureAtlas -> CustomResources.reload(Minecraft.getInstance().getResourceManager()));
	}

	private static class StationColor implements BlockColor {

		@Override
		public int getColor(BlockState blockState, BlockAndTintGetter blockAndTintGetter, BlockPos pos, int i) {
			final int defaultColor = 0x7F7F7F;
			if (pos != null) {
				return ClientData.STATIONS.stream().filter(station1 -> station1.inArea(pos.getX(), pos.getZ())).findFirst().map(station2 -> station2.color).orElse(defaultColor);
			} else {
				return defaultColor;
			}
		}
	}
}
