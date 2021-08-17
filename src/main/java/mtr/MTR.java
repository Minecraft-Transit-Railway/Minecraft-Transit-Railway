package mtr;

import mtr.block.*;
import mtr.data.Depot;
import mtr.data.RailwayData;
import mtr.data.Route;
import mtr.data.Station;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.stream.IntStream;

public class MTR implements ModInitializer, IPacket {

	public static final String MOD_ID = "mtr";

	public static final BlockEntityType<BlockArrivalProjector1Small.TileEntityArrivalProjector1Small> ARRIVAL_PROJECTOR_1_SMALL_TILE_ENTITY = registerTileEntity("arrival_projector_1_small", BlockArrivalProjector1Small.TileEntityArrivalProjector1Small::new, Blocks.ARRIVAL_PROJECTOR_1_SMALL);
	public static final BlockEntityType<BlockArrivalProjector1Medium.TileEntityArrivalProjector1Medium> ARRIVAL_PROJECTOR_1_MEDIUM_TILE_ENTITY = registerTileEntity("arrival_projector_1_medium", BlockArrivalProjector1Medium.TileEntityArrivalProjector1Medium::new, Blocks.ARRIVAL_PROJECTOR_1_MEDIUM);
	public static final BlockEntityType<BlockArrivalProjector1Large.TileEntityArrivalProjector1Large> ARRIVAL_PROJECTOR_1_LARGE_TILE_ENTITY = registerTileEntity("arrival_projector_1_large", BlockArrivalProjector1Large.TileEntityArrivalProjector1Large::new, Blocks.ARRIVAL_PROJECTOR_1_LARGE);
	public static final BlockEntityType<BlockClock.TileEntityClock> CLOCK_TILE_ENTITY = registerTileEntity("clock", BlockClock.TileEntityClock::new, Blocks.CLOCK);
	public static final BlockEntityType<BlockPSDTop.TileEntityPSDTop> PSD_TOP_TILE_ENTITY = registerTileEntity("psd_top", BlockPSDTop.TileEntityPSDTop::new, Blocks.PSD_TOP);
	public static final BlockEntityType<BlockAPGGlass.TileEntityAPGGlass> APG_GLASS_TILE_ENTITY = registerTileEntity("apg_glass", BlockAPGGlass.TileEntityAPGGlass::new, Blocks.APG_GLASS);
	public static final BlockEntityType<BlockPIDS1.TileEntityBlockPIDS1> PIDS_1_TILE_ENTITY = registerTileEntity("pids_1", BlockPIDS1.TileEntityBlockPIDS1::new, Blocks.PIDS_1);
	public static final BlockEntityType<BlockPIDS2.TileEntityBlockPIDS2> PIDS_2_TILE_ENTITY = registerTileEntity("pids_2", BlockPIDS2.TileEntityBlockPIDS2::new, Blocks.PIDS_2);
	public static final BlockEntityType<BlockPIDS3.TileEntityBlockPIDS3> PIDS_3_TILE_ENTITY = registerTileEntity("pids_3", BlockPIDS3.TileEntityBlockPIDS3::new, Blocks.PIDS_3);
	public static final BlockEntityType<BlockRail.TileEntityRail> RAIL_TILE_ENTITY = registerTileEntity("rail", BlockRail.TileEntityRail::new, Blocks.RAIL);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_2_EVEN_TILE_ENTITY = registerTileEntity("railway_sign_2_even", (pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(2, false, pos, state), Blocks.RAILWAY_SIGN_2_EVEN);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_2_ODD_TILE_ENTITY = registerTileEntity("railway_sign_2_odd", (pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(2, true, pos, state), Blocks.RAILWAY_SIGN_2_ODD);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_3_EVEN_TILE_ENTITY = registerTileEntity("railway_sign_3_even", (pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(3, false, pos, state), Blocks.RAILWAY_SIGN_3_EVEN);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_3_ODD_TILE_ENTITY = registerTileEntity("railway_sign_3_odd", (pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(3, true, pos, state), Blocks.RAILWAY_SIGN_3_ODD);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_4_EVEN_TILE_ENTITY = registerTileEntity("railway_sign_4_even", (pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(4, false, pos, state), Blocks.RAILWAY_SIGN_4_EVEN);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_4_ODD_TILE_ENTITY = registerTileEntity("railway_sign_4_odd", (pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(4, true, pos, state), Blocks.RAILWAY_SIGN_4_ODD);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_5_EVEN_TILE_ENTITY = registerTileEntity("railway_sign_5_even", (pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(5, false, pos, state), Blocks.RAILWAY_SIGN_5_EVEN);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_5_ODD_TILE_ENTITY = registerTileEntity("railway_sign_5_odd", (pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(5, true, pos, state), Blocks.RAILWAY_SIGN_5_ODD);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_6_EVEN_TILE_ENTITY = registerTileEntity("railway_sign_6_even", (pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(6, false, pos, state), Blocks.RAILWAY_SIGN_6_EVEN);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_6_ODD_TILE_ENTITY = registerTileEntity("railway_sign_6_odd", (pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(6, true, pos, state), Blocks.RAILWAY_SIGN_6_ODD);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_7_EVEN_TILE_ENTITY = registerTileEntity("railway_sign_7_even", (pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(7, false, pos, state), Blocks.RAILWAY_SIGN_7_EVEN);
	public static final BlockEntityType<BlockRailwaySign.TileEntityRailwaySign> RAILWAY_SIGN_7_ODD_TILE_ENTITY = registerTileEntity("railway_sign_7_odd", (pos, state) -> new BlockRailwaySign.TileEntityRailwaySign(7, true, pos, state), Blocks.RAILWAY_SIGN_7_ODD);
	public static final BlockEntityType<BlockRouteSignStandingLight.TileEntityRouteSignStandingLight> ROUTE_SIGN_STANDING_LIGHT_TILE_ENTITY = registerTileEntity("route_sign_standing_light", BlockRouteSignStandingLight.TileEntityRouteSignStandingLight::new, Blocks.ROUTE_SIGN_STANDING_LIGHT);
	public static final BlockEntityType<BlockRouteSignStandingMetal.TileEntityRouteSignStandingMetal> ROUTE_SIGN_STANDING_METAL_TILE_ENTITY = registerTileEntity("route_sign_standing_metal", BlockRouteSignStandingMetal.TileEntityRouteSignStandingMetal::new, Blocks.ROUTE_SIGN_STANDING_METAL);
	public static final BlockEntityType<BlockRouteSignWallLight.TileEntityRouteSignWallLight> ROUTE_SIGN_WALL_LIGHT_TILE_ENTITY = registerTileEntity("route_sign_wall_light", BlockRouteSignWallLight.TileEntityRouteSignWallLight::new, Blocks.ROUTE_SIGN_WALL_LIGHT);
	public static final BlockEntityType<BlockRouteSignWallMetal.TileEntityRouteSignWallMetal> ROUTE_SIGN_WALL_METAL_TILE_ENTITY = registerTileEntity("route_sign_wall_metal", BlockRouteSignWallMetal.TileEntityRouteSignWallMetal::new, Blocks.ROUTE_SIGN_WALL_METAL);
	public static final BlockEntityType<BlockStationNameEntrance.TileEntityStationNameEntrance> STATION_NAME_ENTRANCE_TILE_ENTITY = registerTileEntity("station_name_entrance", BlockStationNameEntrance.TileEntityStationNameEntrance::new, Blocks.STATION_NAME_ENTRANCE);
	public static final BlockEntityType<BlockStationNameWall.TileEntityStationNameWall> STATION_NAME_WALL_TILE_ENTITY = registerTileEntity("station_name_wall", BlockStationNameWall.TileEntityStationNameWall::new, Blocks.STATION_NAME_WALL);
	public static final BlockEntityType<BlockStationNameTallBlock.TileEntityStationNameTallBlock> STATION_NAME_TALL_BLOCK_TILE_ENTITY = registerTileEntity("station_name_tall_block", BlockStationNameTallBlock.TileEntityStationNameTallBlock::new, Blocks.STATION_NAME_TALL_BLOCK);
	public static final BlockEntityType<BlockStationNameTallWall.TileEntityStationNameTallWall> STATION_NAME_TALL_WALL_TILE_ENTITY = registerTileEntity("station_name_tall_wall", BlockStationNameTallWall.TileEntityStationNameTallWall::new, Blocks.STATION_NAME_TALL_WALL);

	public static final SoundEvent TICKET_BARRIER = registerSoundEvent("ticket_barrier");
	public static final SoundEvent TICKET_BARRIER_CONCESSIONARY = registerSoundEvent("ticket_barrier_concessionary");
	public static final SoundEvent TICKET_PROCESSOR_ENTRY = registerSoundEvent("ticket_processor_entry");
	public static final SoundEvent TICKET_PROCESSOR_ENTRY_CONCESSIONARY = registerSoundEvent("ticket_processor_entry_concessionary");
	public static final SoundEvent TICKET_PROCESSOR_EXIT = registerSoundEvent("ticket_processor_exit");
	public static final SoundEvent TICKET_PROCESSOR_EXIT_CONCESSIONARY = registerSoundEvent("ticket_processor_exit_concessionary");
	public static final SoundEvent TICKET_PROCESSOR_FAIL = registerSoundEvent("ticket_processor_fail");

	private static final int SP1900_SPEED_COUNT = 120;
	private static final int C1141A_SPEED_COUNT = 96;
	public static final SoundEvent[] SP1900_ACCELERATION = registerSoundEvents(SP1900_SPEED_COUNT, 3, "sp1900_acceleration_");
	public static final SoundEvent[] SP1900_DECELERATION = registerSoundEvents(SP1900_SPEED_COUNT, 3, "sp1900_deceleration_");
	public static final SoundEvent[] C1141A_ACCELERATION = registerSoundEvents(C1141A_SPEED_COUNT, 3, "c1141a_acceleration_");
	public static final SoundEvent[] C1141A_DECELERATION = registerSoundEvents(C1141A_SPEED_COUNT, 3, "c1141a_deceleration_");
	public static final SoundEvent SP1900_DOOR_OPEN = registerSoundEvent("sp1900_door_open");
	public static final SoundEvent SP1900_DOOR_CLOSE = registerSoundEvent("sp1900_door_close");
	private static final int MLR_SPEED_COUNT = 93;
	public static final SoundEvent[] MLR_ACCELERATION = registerSoundEvents(MLR_SPEED_COUNT, 3, "mlr_acceleration_");
	public static final SoundEvent[] MLR_DECELERATION = registerSoundEvents(MLR_SPEED_COUNT, 3, "mlr_deceleration_");
	public static final SoundEvent MLR_DOOR_OPEN = registerSoundEvent("mlr_door_open");
	public static final SoundEvent MLR_DOOR_CLOSE = registerSoundEvent("mlr_door_close");
	private static final int M_TRAIN_SPEED_COUNT = 90;
	public static final SoundEvent[] M_TRAIN_ACCELERATION = registerSoundEvents(M_TRAIN_SPEED_COUNT, 3, "m_train_acceleration_");
	public static final SoundEvent[] M_TRAIN_DECELERATION = registerSoundEvents(M_TRAIN_SPEED_COUNT, 3, "m_train_deceleration_");
	public static final SoundEvent M_TRAIN_DOOR_OPEN = registerSoundEvent("m_train_door_open");
	public static final SoundEvent M_TRAIN_DOOR_CLOSE = registerSoundEvent("m_train_door_close");
	private static final int K_TRAIN_SPEED_COUNT = 66;
	public static final SoundEvent[] K_TRAIN_ACCELERATION = registerSoundEvents(K_TRAIN_SPEED_COUNT, 3, "k_train_acceleration_");
	public static final SoundEvent[] K_TRAIN_DECELERATION = registerSoundEvents(K_TRAIN_SPEED_COUNT, 3, "k_train_deceleration_");
	public static final SoundEvent K_TRAIN_DOOR_OPEN = registerSoundEvent("k_train_door_open");
	public static final SoundEvent K_TRAIN_DOOR_CLOSE = registerSoundEvent("k_train_door_close");
	private static final int A_TRAIN_SPEED_COUNT = 78;
	public static final SoundEvent[] A_TRAIN_ACCELERATION = registerSoundEvents(A_TRAIN_SPEED_COUNT, 3, "a_train_acceleration_");
	public static final SoundEvent[] A_TRAIN_DECELERATION = registerSoundEvents(A_TRAIN_SPEED_COUNT, 3, "a_train_deceleration_");
	public static final SoundEvent A_TRAIN_DOOR_OPEN = registerSoundEvent("a_train_door_open");
	public static final SoundEvent A_TRAIN_DOOR_CLOSE = registerSoundEvent("a_train_door_close");
	private static final int LIGHT_RAIL_1_SPEED_COUNT = 48;
	public static final SoundEvent[] LIGHT_RAIL_ACCELERATION = registerSoundEvents(LIGHT_RAIL_1_SPEED_COUNT, 3, "light_rail_acceleration_");
	public static final SoundEvent[] LIGHT_RAIL_DECELERATION = registerSoundEvents(LIGHT_RAIL_1_SPEED_COUNT, 3, "light_rail_deceleration_");
	public static final SoundEvent LIGHT_RAIL_1_DOOR_OPEN = registerSoundEvent("light_rail_1_door_open");
	public static final SoundEvent LIGHT_RAIL_1_DOOR_CLOSE = registerSoundEvent("light_rail_1_door_close");
	public static final SoundEvent LIGHT_RAIL_3_DOOR_OPEN = registerSoundEvent("light_rail_3_door_open");
	public static final SoundEvent LIGHT_RAIL_3_DOOR_CLOSE = registerSoundEvent("light_rail_3_door_close");
	public static final SoundEvent LIGHT_RAIL_4_DOOR_OPEN = registerSoundEvent("light_rail_4_door_open");
	public static final SoundEvent LIGHT_RAIL_4_DOOR_CLOSE = registerSoundEvent("light_rail_4_door_close");
	// TODO phase 4 door sounds

	@Override
	public void onInitialize() {
		registerItem("apg_door", Items.APG_DOOR);
		registerItem("apg_glass", Items.APG_GLASS);
		registerItem("apg_glass_end", Items.APG_GLASS_END);
		registerItem("brush", Items.BRUSH);
		registerItem("dashboard", Items.DASHBOARD);
		registerItem("escalator", Items.ESCALATOR);
		registerItem("psd_door", Items.PSD_DOOR_1);
		registerItem("psd_glass", Items.PSD_GLASS_1);
		registerItem("psd_glass_end", Items.PSD_GLASS_END_1);
		registerItem("psd_door_2", Items.PSD_DOOR_2);
		registerItem("psd_glass_2", Items.PSD_GLASS_2);
		registerItem("psd_glass_end_2", Items.PSD_GLASS_END_2);
		registerItem("rail_connector_1_wooden", Items.RAIL_CONNECTOR_1_WOODEN);
		registerItem("rail_connector_1_wooden_one_way", Items.RAIL_CONNECTOR_1_WOODEN_ONE_WAY);
		registerItem("rail_connector_2_stone", Items.RAIL_CONNECTOR_2_STONE);
		registerItem("rail_connector_2_stone_one_way", Items.RAIL_CONNECTOR_2_STONE_ONE_WAY);
		registerItem("rail_connector_3_iron", Items.RAIL_CONNECTOR_3_IRON);
		registerItem("rail_connector_3_iron_one_way", Items.RAIL_CONNECTOR_3_IRON_ONE_WAY);
		registerItem("rail_connector_4_obsidian", Items.RAIL_CONNECTOR_4_OBSIDIAN);
		registerItem("rail_connector_4_obsidian_one_way", Items.RAIL_CONNECTOR_4_OBSIDIAN_ONE_WAY);
		registerItem("rail_connector_5_blaze", Items.RAIL_CONNECTOR_5_BLAZE);
		registerItem("rail_connector_5_blaze_one_way", Items.RAIL_CONNECTOR_5_BLAZE_ONE_WAY);
		registerItem("rail_connector_6_diamond", Items.RAIL_CONNECTOR_6_DIAMOND);
		registerItem("rail_connector_6_diamond_one_way", Items.RAIL_CONNECTOR_6_DIAMOND_ONE_WAY);
		registerItem("rail_connector_platform", Items.RAIL_CONNECTOR_PLATFORM);
		registerItem("rail_connector_siding", Items.RAIL_CONNECTOR_SIDING);
		registerItem("rail_connector_turn_back", Items.RAIL_CONNECTOR_TURN_BACK);
		registerItem("rail_remover", Items.RAIL_REMOVER);

		registerBlock("apg_door", Blocks.APG_DOOR);
		registerBlock("apg_glass", Blocks.APG_GLASS);
		registerBlock("apg_glass_end", Blocks.APG_GLASS_END);
		registerBlock("arrival_projector_1_small", Blocks.ARRIVAL_PROJECTOR_1_SMALL, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("arrival_projector_1_medium", Blocks.ARRIVAL_PROJECTOR_1_MEDIUM, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("arrival_projector_1_large", Blocks.ARRIVAL_PROJECTOR_1_LARGE, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ceiling", Blocks.CEILING, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("clock", Blocks.CLOCK, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("escalator_side", Blocks.ESCALATOR_SIDE);
		registerBlock("escalator_step", Blocks.ESCALATOR_STEP);
		registerBlock("glass_fence_cio", Blocks.GLASS_FENCE_CIO, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_ckt", Blocks.GLASS_FENCE_CKT, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_heo", Blocks.GLASS_FENCE_HEO, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_mos", Blocks.GLASS_FENCE_MOS, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_plain", Blocks.GLASS_FENCE_PLAIN, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_shm", Blocks.GLASS_FENCE_SHM, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_stained", Blocks.GLASS_FENCE_STAINED, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_stw", Blocks.GLASS_FENCE_STW, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_tsh", Blocks.GLASS_FENCE_TSH, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("glass_fence_wks", Blocks.GLASS_FENCE_WKS, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("logo", Blocks.LOGO, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("pids_1", Blocks.PIDS_1, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("pids_2", Blocks.PIDS_2, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("pids_3", Blocks.PIDS_3, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("platform", Blocks.PLATFORM, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("platform_na_1", Blocks.PLATFORM_NA_1, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("platform_na_2", Blocks.PLATFORM_NA_2, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("psd_door", Blocks.PSD_DOOR_1);
		registerBlock("psd_glass", Blocks.PSD_GLASS_1);
		registerBlock("psd_glass_end", Blocks.PSD_GLASS_END_1);
		registerBlock("psd_door_2", Blocks.PSD_DOOR_2);
		registerBlock("psd_glass_2", Blocks.PSD_GLASS_2);
		registerBlock("psd_glass_end_2", Blocks.PSD_GLASS_END_2);
		registerBlock("psd_top", Blocks.PSD_TOP);
		registerBlock("rail", Blocks.RAIL, ItemGroups.CORE);
		registerBlock("railway_sign_2_even", Blocks.RAILWAY_SIGN_2_EVEN, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_2_odd", Blocks.RAILWAY_SIGN_2_ODD, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_3_even", Blocks.RAILWAY_SIGN_3_EVEN, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_3_odd", Blocks.RAILWAY_SIGN_3_ODD, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_4_even", Blocks.RAILWAY_SIGN_4_EVEN, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_4_odd", Blocks.RAILWAY_SIGN_4_ODD, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_5_even", Blocks.RAILWAY_SIGN_5_EVEN, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_5_odd", Blocks.RAILWAY_SIGN_5_ODD, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_6_even", Blocks.RAILWAY_SIGN_6_EVEN, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_6_odd", Blocks.RAILWAY_SIGN_6_ODD, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_7_even", Blocks.RAILWAY_SIGN_7_EVEN, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_7_odd", Blocks.RAILWAY_SIGN_7_ODD, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("railway_sign_middle", Blocks.RAILWAY_SIGN_MIDDLE);
		registerBlock("route_sign_standing_light", Blocks.ROUTE_SIGN_STANDING_LIGHT, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("route_sign_standing_metal", Blocks.ROUTE_SIGN_STANDING_METAL, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("route_sign_wall_light", Blocks.ROUTE_SIGN_WALL_LIGHT, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("route_sign_wall_metal", Blocks.ROUTE_SIGN_WALL_METAL, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("rubbish_bin_1", Blocks.RUBBISH_BIN_1, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("station_color_andesite", Blocks.STATION_COLOR_ANDESITE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_bedrock", Blocks.STATION_COLOR_BEDROCK, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_birch_wood", Blocks.STATION_COLOR_BIRCH_WOOD, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_bone_block", Blocks.STATION_COLOR_BONE_BLOCK, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_chiseled_quartz_block", Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_chiseled_stone_bricks", Blocks.STATION_COLOR_CHISELED_STONE_BRICKS, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_clay", Blocks.STATION_COLOR_CLAY, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_coal_ore", Blocks.STATION_COLOR_COAL_ORE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_cobblestone", Blocks.STATION_COLOR_COBBLESTONE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_concrete", Blocks.STATION_COLOR_CONCRETE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_concrete_powder", Blocks.STATION_COLOR_CONCRETE_POWDER, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_cracked_stone_bricks", Blocks.STATION_COLOR_CRACKED_STONE_BRICKS, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_dark_prismarine", Blocks.STATION_COLOR_DARK_PRISMARINE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_diorite", Blocks.STATION_COLOR_DIORITE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_gravel", Blocks.STATION_COLOR_GRAVEL, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_iron_block", Blocks.STATION_COLOR_IRON_BLOCK, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_metal", Blocks.STATION_COLOR_METAL, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_planks", Blocks.STATION_COLOR_PLANKS, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_polished_andesite", Blocks.STATION_COLOR_POLISHED_ANDESITE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_polished_diorite", Blocks.STATION_COLOR_POLISHED_DIORITE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_purpur_block", Blocks.STATION_COLOR_PURPUR_BLOCK, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_purpur_pillar", Blocks.STATION_COLOR_PURPUR_PILLAR, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_quartz_block", Blocks.STATION_COLOR_QUARTZ_BLOCK, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_quartz_bricks", Blocks.STATION_COLOR_QUARTZ_BRICKS, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_quartz_pillar", Blocks.STATION_COLOR_QUARTZ_PILLAR, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_smooth_quartz", Blocks.STATION_COLOR_SMOOTH_QUARTZ, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_smooth_stone", Blocks.STATION_COLOR_SMOOTH_STONE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_snow_block", Blocks.STATION_COLOR_SNOW_BLOCK, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_stained_glass", Blocks.STATION_COLOR_STAINED_GLASS, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_stone", Blocks.STATION_COLOR_STONE, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_stone_bricks", Blocks.STATION_COLOR_STONE_BRICKS, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_color_wool", Blocks.STATION_COLOR_WOOL, ItemGroups.STATION_BUILDING_BLOCKS);
		registerBlock("station_name_entrance", Blocks.STATION_NAME_ENTRANCE, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("station_name_tall_block", Blocks.STATION_NAME_TALL_BLOCK, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("station_name_tall_wall", Blocks.STATION_NAME_TALL_WALL, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("station_name_wall", Blocks.STATION_NAME_WALL, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("station_pole", Blocks.STATION_POLE, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ticket_barrier_entrance_1", Blocks.TICKET_BARRIER_ENTRANCE_1, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ticket_barrier_exit_1", Blocks.TICKET_BARRIER_EXIT_1, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ticket_machine", Blocks.TICKET_MACHINE, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ticket_processor", Blocks.TICKET_PROCESSOR, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ticket_processor_entrance", Blocks.TICKET_PROCESSOR_ENTRANCE, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ticket_processor_exit", Blocks.TICKET_PROCESSOR_EXIT, ItemGroups.RAILWAY_FACILITIES);
		registerBlock("ticket_processor_enquiry", Blocks.TICKET_PROCESSOR_ENQUIRY, ItemGroups.RAILWAY_FACILITIES);

		ServerPlayNetworking.registerGlobalReceiver(PACKET_GENERATE_PATH, (minecraftServer, player, handler, packet, sender) -> PacketTrainDataGuiServer.generatePathC2S(minecraftServer, player, packet));
		ServerPlayNetworking.registerGlobalReceiver(PACKET_SIGN_TYPES, (minecraftServer, player, handler, packet, sender) -> PacketTrainDataGuiServer.receiveSignIdsC2S(minecraftServer, player, packet));
		ServerPlayNetworking.registerGlobalReceiver(PACKET_ADD_BALANCE, (minecraftServer, player, handler, packet, sender) -> PacketTrainDataGuiServer.receiveAddBalanceC2S(minecraftServer, player, packet));
		ServerPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_STATION, (minecraftServer, player, handler, packet, sender) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_STATION, railwayData -> railwayData.stations, Station::new, false));
		ServerPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_PLATFORM, (minecraftServer, player, handler, packet, sender) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_PLATFORM, railwayData -> railwayData.platforms, null, false));
		ServerPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_SIDING, (minecraftServer, player, handler, packet, sender) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_SIDING, railwayData -> railwayData.sidings, null, false));
		ServerPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_ROUTE, (minecraftServer, player, handler, packet, sender) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_ROUTE, railwayData -> railwayData.routes, Route::new, false));
		ServerPlayNetworking.registerGlobalReceiver(PACKET_UPDATE_DEPOT, (minecraftServer, player, handler, packet, sender) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_DEPOT, railwayData -> railwayData.depots, Depot::new, false));
		ServerPlayNetworking.registerGlobalReceiver(PACKET_DELETE_STATION, (minecraftServer, player, handler, packet, sender) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_STATION, railwayData -> railwayData.stations, null, true));
		ServerPlayNetworking.registerGlobalReceiver(PACKET_DELETE_PLATFORM, (minecraftServer, player, handler, packet, sender) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_PLATFORM, railwayData -> railwayData.platforms, null, true));
		ServerPlayNetworking.registerGlobalReceiver(PACKET_DELETE_SIDING, (minecraftServer, player, handler, packet, sender) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_SIDING, railwayData -> railwayData.sidings, null, true));
		ServerPlayNetworking.registerGlobalReceiver(PACKET_DELETE_ROUTE, (minecraftServer, player, handler, packet, sender) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_ROUTE, railwayData -> railwayData.routes, null, true));
		ServerPlayNetworking.registerGlobalReceiver(PACKET_DELETE_DEPOT, (minecraftServer, player, handler, packet, sender) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_DELETE_DEPOT, railwayData -> railwayData.depots, null, true));

		ServerTickEvents.START_SERVER_TICK.register(minecraftServer -> minecraftServer.getWorlds().forEach(serverWorld -> {
			final RailwayData railwayData = RailwayData.getInstance(serverWorld);
			if (railwayData != null) {
				railwayData.simulateTrains();
			}
		}));
		ServerEntityEvents.ENTITY_LOAD.register((entity, serverWorld) -> {
			if (entity instanceof ServerPlayerEntity) {
				final RailwayData railwayData = RailwayData.getInstance(serverWorld);
				if (railwayData != null) {
					railwayData.broadcastToPlayer((ServerPlayerEntity) entity);
				}
			}
		});
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			final RailwayData railwayData = RailwayData.getInstance(handler.player.world);
			if (railwayData != null) {
				railwayData.disconnectPlayer(handler.player);
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

	private static <T extends BlockEntity> BlockEntityType<T> registerTileEntity(String path, FabricBlockEntityTypeBuilder.Factory<T> factory, Block block) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":" + path, FabricBlockEntityTypeBuilder.create(factory, block).build());
	}

	private static SoundEvent registerSoundEvent(String path) {
		final Identifier id = new Identifier(MOD_ID, path);
		return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
	}

	private static SoundEvent[] registerSoundEvents(int size, int groupSize, String path) {
		return IntStream.range(0, size).mapToObj(i -> {
			String group;
			switch (i % groupSize) {
				case 0:
					group = "a";
					break;
				case 1:
					group = "b";
					break;
				case 2:
					group = "c";
					break;
				default:
					group = "";
					break;
			}
			return registerSoundEvent(path + (i / 3) + group);
		}).toArray(SoundEvent[]::new);
	}
}