package org.mtr.mod;

import org.mtr.core.data.TransportMode;
import org.mtr.mapping.holder.Block;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.mapper.SlabBlockExtension;
import org.mtr.mapping.registry.BlockRegistryObject;
import org.mtr.mod.block.*;


public final class Blocks {

	static {
		// Nodes
		RAIL_NODE = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "rail"), () -> new Block(new BlockNode(TransportMode.TRAIN)), CreativeModeTabs.CORE);
		BOAT_NODE = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "boat_node"), () -> new Block(new BlockNode.BlockBoatNode()));
		CABLE_CAR_NODE_LOWER = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "cable_car_node_lower"), () -> new Block(new BlockNode.BlockContinuousMovementNode(false, false)), CreativeModeTabs.CORE);
		CABLE_CAR_NODE_UPPER = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "cable_car_node_upper"), () -> new Block(new BlockNode.BlockContinuousMovementNode(true, false)), CreativeModeTabs.CORE);
		CABLE_CAR_NODE_STATION = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "cable_car_node_station"), () -> new Block(new BlockNode.BlockContinuousMovementNode(false, true)), CreativeModeTabs.CORE);
		AIRPLANE_NODE = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "airplane_node"), () -> new Block(new BlockNode(TransportMode.AIRPLANE)), CreativeModeTabs.CORE);

		// Doors
		APG_DOOR = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "apg_door"), () -> new Block(new BlockAPGDoor()));
		APG_GLASS = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "apg_glass"), () -> new Block(new BlockAPGGlass()));
		APG_GLASS_END = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "apg_glass_end"), () -> new Block(new BlockAPGGlassEnd()));
		PSD_DOOR_1 = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "psd_door"), () -> new Block(new BlockPSDDoor(0)));
		PSD_GLASS_1 = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "psd_glass"), () -> new Block(new BlockPSDGlass(0)));
		PSD_GLASS_END_1 = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "psd_glass_end"), () -> new Block(new BlockPSDGlassEnd(0)));
		PSD_DOOR_2 = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "psd_door_2"), () -> new Block(new BlockPSDDoor(1)));
		PSD_GLASS_2 = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "psd_glass_2"), () -> new Block(new BlockPSDGlass(1)));
		PSD_GLASS_END_2 = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "psd_glass_end_2"), () -> new Block(new BlockPSDGlassEnd(1)));
		PSD_TOP = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "psd_top"), () -> new Block(new BlockPSDTop()));

		// Escalators lifts
		ESCALATOR_SIDE = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "escalator_side"), () -> new Block(new BlockEscalatorSide()));
		ESCALATOR_STEP = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "escalator_step"), () -> new Block(new BlockEscalatorStep()));
		LIFT_BUTTONS_1 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_buttons_1"), () -> new Block(new BlockLiftButtons()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_PANEL_EVEN_1 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_panel_even_1"), () -> new Block(new BlockLiftPanelEven1()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_PANEL_ODD_1 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_panel_odd_1"), () -> new Block(new BlockLiftPanelOdd1()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_PANEL_EVEN_2 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_panel_even_2"), () -> new Block(new BlockLiftPanelEven2()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_PANEL_ODD_2 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_panel_odd_2"), () -> new Block(new BlockLiftPanelOdd2()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_TRACK_HORIZONTAL_1 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_track_horizontal_1"), () -> new Block(new BlockLiftTrackHorizontal()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_TRACK_VERTICAL_1 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_track_1"), () -> new Block(new BlockLiftTrackVertical()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_TRACK_DIAGONAL_1 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_track_diagonal_1"), () -> new Block(new BlockLiftTrackDiagonal()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_TRACK_FLOOR_1 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_track_floor_1"), () -> new Block(new BlockLiftTrackFloor()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_DOOR_EVEN_1 = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "lift_door_1"), () -> new Block(new BlockLiftDoor()));
		LIFT_DOOR_ODD_1 = Init.REGISTRY.registerBlock(new Identifier(Init.MOD_ID, "lift_door_odd_1"), () -> new Block(new BlockLiftDoorOdd()));

		// PIDS projectors
		PIDS_1 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "pids_1"), () -> new Block(new BlockPIDSHorizontal1()), CreativeModeTabs.RAILWAY_FACILITIES);
		PIDS_2 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "pids_2"), () -> new Block(new BlockPIDSHorizontal2()), CreativeModeTabs.RAILWAY_FACILITIES);
		PIDS_3 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "pids_3"), () -> new Block(new BlockPIDSHorizontal3()), CreativeModeTabs.RAILWAY_FACILITIES);
		PIDS_4 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "pids_4"), () -> new Block(new BlockPIDSVertical1()), CreativeModeTabs.RAILWAY_FACILITIES);
		PIDS_POLE = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "pids_pole"), () -> new Block(new BlockPIDSPole(BlockHelper.createBlockSettings(false))), CreativeModeTabs.RAILWAY_FACILITIES);
		PIDS_SINGLE_ARRIVAL_1 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "pids_single_arrival_1"), () -> new Block(new BlockPIDSVerticalSingleArrival1()), CreativeModeTabs.RAILWAY_FACILITIES);
		ARRIVAL_PROJECTOR_1_SMALL = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "arrival_projector_1_small"), () -> new Block(new BlockArrivalProjector1Small()), CreativeModeTabs.RAILWAY_FACILITIES);
		ARRIVAL_PROJECTOR_1_MEDIUM = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "arrival_projector_1_medium"), () -> new Block(new BlockArrivalProjector1Medium()), CreativeModeTabs.RAILWAY_FACILITIES);
		ARRIVAL_PROJECTOR_1_LARGE = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "arrival_projector_1_large"), () -> new Block(new BlockArrivalProjector1Large()), CreativeModeTabs.RAILWAY_FACILITIES);

		// blocks
		PLATFORM = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform"), () -> new Block(new BlockPlatform(BlockHelper.createBlockSettings(false), false)), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_INDENTED = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_indented"), () -> new Block(new BlockPlatform(BlockHelper.createBlockSettings(false), true)), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_NA_1 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_na_1"), () -> new Block(new BlockPlatform(BlockHelper.createBlockSettings(false), false)), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_NA_1_INDENTED = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_na_1_indented"), () -> new Block(new BlockPlatform(BlockHelper.createBlockSettings(false), true)), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_NA_2 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_na_2"), () -> new Block(new BlockPlatform(BlockHelper.createBlockSettings(false), false)), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_NA_2_INDENTED = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_na_2_indented"), () -> new Block(new BlockPlatform(BlockHelper.createBlockSettings(false), true)), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_UK_1 = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_uk_1"), () -> new Block(new BlockPlatform(BlockHelper.createBlockSettings(false), false)), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_UK_1_INDENTED = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_uk_1_indented"), () -> new Block(new BlockPlatform(BlockHelper.createBlockSettings(false), true)), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		
		// Signs
		RAILWAY_SIGN_2_EVEN = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "railway_sign_2_even"), () -> new Block(new BlockRailwaySign(2, false)), CreativeModeTabs.RAILWAY_FACILITIES);
		RAILWAY_SIGN_2_ODD = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "railway_sign_2_odd"), () -> new Block(new BlockRailwaySign(2, true)), CreativeModeTabs.RAILWAY_FACILITIES);
		RAILWAY_SIGN_3_EVEN = Init.REGISTRY.registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "railway_sign_3_even"), () -> new Block(new BlockRailwaySign(3, false)), CreativeModeTabs.RAILWAY_FACILITIES);
		
	public static final BlockRegistryObject PLATFORM_INDENTED;
	public static final BlockRegistryObject PLATFORM_NA_1;
	public static final BlockRegistryObject PLATFORM_NA_1_INDENTED;
	public static final BlockRegistryObject PLATFORM_NA_2;
	public static final BlockRegistryObject PLATFORM_NA_2_INDENTED;
	public static final BlockRegistryObject PLATFORM_UK_1;
	public static final BlockRegistryObject PLATFORM_UK_1_INDENTED;
	public static final BlockRegistryObject PSD_DOOR_1;
	public static final BlockRegistryObject PSD_DOOR_2;
	public static final BlockRegistryObject PSD_GLASS_1;
	public static final BlockRegistryObject PSD_GLASS_2;
	public static final BlockRegistryObject PSD_GLASS_END_1;
	public static final BlockRegistryObject PSD_GLASS_END_2;
	public static final BlockRegistryObject PSD_TOP;
	public static final BlockRegistryObject RAIL_NODE;
	public static final BlockRegistryObject RAILWAY_SIGN_2_EVEN;
	public static final BlockRegistryObject RAILWAY_SIGN_2_ODD;
	public static final BlockRegistryObject RAILWAY_SIGN_3_EVEN;
	public static final BlockRegistryObject RAILWAY_SIGN_3_ODD;
	public static final BlockRegistryObject RAILWAY_SIGN_4_EVEN;
	public static final BlockRegistryObject RAILWAY_SIGN_4_ODD;
	public static final BlockRegistryObject RAILWAY_SIGN_5_EVEN;
	public static final BlockRegistryObject RAILWAY_SIGN_5_ODD;
	public static final BlockRegistryObject RAILWAY_SIGN_6_EVEN;
	public static final BlockRegistryObject RAILWAY_SIGN_6_ODD;
	public static final BlockRegistryObject RAILWAY_SIGN_7_EVEN;
	public static final BlockRegistryObject RAILWAY_SIGN_7_ODD;
	public static final BlockRegistryObject RAILWAY_SIGN_MIDDLE;
	public static final BlockRegistryObject RAILWAY_SIGN_POLE;
	public static final BlockRegistryObject ROUTE_SIGN_STANDING_LIGHT;
	public static final BlockRegistryObject ROUTE_SIGN_STANDING_METAL;
	public static final BlockRegistryObject ROUTE_SIGN_WALL_LIGHT;
	public static final BlockRegistryObject ROUTE_SIGN_WALL_METAL;
	public static final BlockRegistryObject RUBBISH_BIN_1;
	public static final BlockRegistryObject SIGNAL_LIGHT_2_ASPECT_1;
	public static final BlockRegistryObject SIGNAL_LIGHT_2_ASPECT_2;
	public static final BlockRegistryObject SIGNAL_LIGHT_2_ASPECT_3;
	public static final BlockRegistryObject SIGNAL_LIGHT_2_ASPECT_4;
	public static final BlockRegistryObject SIGNAL_LIGHT_3_ASPECT_1;
	public static final BlockRegistryObject SIGNAL_LIGHT_3_ASPECT_2;
	public static final BlockRegistryObject SIGNAL_LIGHT_4_ASPECT_1;
	public static final BlockRegistryObject SIGNAL_LIGHT_4_ASPECT_2;
	public static final BlockRegistryObject SIGNAL_POLE;
	public static final BlockRegistryObject SIGNAL_SEMAPHORE_1;
	public static final BlockRegistryObject SIGNAL_SEMAPHORE_2;
	public static final BlockRegistryObject STATION_COLOR_ANDESITE;
	public static final BlockRegistryObject STATION_COLOR_ANDESITE_SLAB;
	public static final BlockRegistryObject STATION_COLOR_BEDROCK;
	public static final BlockRegistryObject STATION_COLOR_BEDROCK_SLAB;
	public static final BlockRegistryObject STATION_COLOR_BIRCH_WOOD;
	public static final BlockRegistryObject STATION_COLOR_BIRCH_WOOD_SLAB;
	public static final BlockRegistryObject STATION_COLOR_BONE_BLOCK;
	public static final BlockRegistryObject STATION_COLOR_BONE_BLOCK_SLAB;
	public static final BlockRegistryObject STATION_COLOR_CHISELED_QUARTZ_BLOCK;
	public static final BlockRegistryObject STATION_COLOR_CHISELED_QUARTZ_BLOCK_SLAB;
	public static final BlockRegistryObject STATION_COLOR_CHISELED_STONE_BRICKS;
	public static final BlockRegistryObject STATION_COLOR_CHISELED_STONE_BRICKS_SLAB;
	public static final BlockRegistryObject STATION_COLOR_CLAY;
	public static final BlockRegistryObject STATION_COLOR_CLAY_SLAB;
	public static final BlockRegistryObject STATION_COLOR_COAL_ORE;
	public static final BlockRegistryObject STATION_COLOR_COAL_ORE_SLAB;
	public static final BlockRegistryObject STATION_COLOR_COBBLESTONE;
	public static final BlockRegistryObject STATION_COLOR_COBBLESTONE_SLAB;
	public static final BlockRegistryObject STATION_COLOR_CONCRETE;
	public static final BlockRegistryObject STATION_COLOR_CONCRETE_POWDER;
	public static final BlockRegistryObject STATION_COLOR_CONCRETE_POWDER_SLAB;
	public static final BlockRegistryObject STATION_COLOR_CONCRETE_SLAB;
	public static final BlockRegistryObject STATION_COLOR_CRACKED_STONE_BRICKS;
	public static final BlockRegistryObject STATION_COLOR_CRACKED_STONE_BRICKS_SLAB;
	public static final BlockRegistryObject STATION_COLOR_DARK_PRISMARINE;
	public static final BlockRegistryObject STATION_COLOR_DARK_PRISMARINE_SLAB;
	public static final BlockRegistryObject STATION_COLOR_DIORITE;
	public static final BlockRegistryObject STATION_COLOR_DIORITE_SLAB;
	public static final BlockRegistryObject STATION_COLOR_GRAVEL;
	public static final BlockRegistryObject STATION_COLOR_GRAVEL_SLAB;
	public static final BlockRegistryObject STATION_COLOR_IRON_BLOCK;
	public static final BlockRegistryObject STATION_COLOR_IRON_BLOCK_SLAB;
	public static final BlockRegistryObject STATION_COLOR_METAL;
	public static final BlockRegistryObject STATION_COLOR_METAL_SLAB;
	public static final BlockRegistryObject STATION_COLOR_PLANKS;
	public static final BlockRegistryObject STATION_COLOR_PLANKS_SLAB;
	public static final BlockRegistryObject STATION_COLOR_POLE;
	public static final BlockRegistryObject STATION_COLOR_POLISHED_ANDESITE;
	public static final BlockRegistryObject STATION_COLOR_POLISHED_ANDESITE_SLAB;
	public static final BlockRegistryObject STATION_COLOR_POLISHED_DIORITE;
	public static final BlockRegistryObject STATION_COLOR_POLISHED_DIORITE_SLAB;
	public static final BlockRegistryObject STATION_COLOR_PURPUR_BLOCK;
	public static final BlockRegistryObject STATION_COLOR_PURPUR_BLOCK_SLAB;
	public static final BlockRegistryObject STATION_COLOR_PURPUR_PILLAR;
	public static final BlockRegistryObject STATION_COLOR_PURPUR_PILLAR_SLAB;
	public static final BlockRegistryObject STATION_COLOR_QUARTZ_BLOCK;
	public static final BlockRegistryObject STATION_COLOR_QUARTZ_BLOCK_SLAB;
	public static final BlockRegistryObject STATION_COLOR_QUARTZ_BRICKS;
	public static final BlockRegistryObject STATION_COLOR_QUARTZ_BRICKS_SLAB;
	public static final BlockRegistryObject STATION_COLOR_QUARTZ_PILLAR;
	public static final BlockRegistryObject STATION_COLOR_QUARTZ_PILLAR_SLAB;
	public static final BlockRegistryObject STATION_COLOR_SMOOTH_QUARTZ;
	public static final BlockRegistryObject STATION_COLOR_SMOOTH_QUARTZ_SLAB;
	public static final BlockRegistryObject STATION_COLOR_SMOOTH_STONE;
	public static final BlockRegistryObject STATION_COLOR_SMOOTH_STONE_SLAB;
	public static final BlockRegistryObject STATION_COLOR_SNOW_BLOCK;
	public static final BlockRegistryObject STATION_COLOR_SNOW_BLOCK_SLAB;
	public static final BlockRegistryObject STATION_COLOR_STAINED_GLASS;
	public static final BlockRegistryObject STATION_COLOR_STAINED_GLASS_SLAB;
	public static final BlockRegistryObject STATION_COLOR_STONE;
	public static final BlockRegistryObject STATION_COLOR_STONE_BRICKS;
	public static final BlockRegistryObject STATION_COLOR_STONE_BRICKS_SLAB;
	public static final BlockRegistryObject STATION_COLOR_STONE_SLAB;
	public static final BlockRegistryObject STATION_COLOR_WOOL;
	public static final BlockRegistryObject STATION_COLOR_WOOL_SLAB;
	public static final BlockRegistryObject STATION_NAME_ENTRANCE;
	public static final BlockRegistryObject STATION_NAME_TALL_BLOCK;
	public static final BlockRegistryObject STATION_NAME_TALL_BLOCK_DOUBLE_SIDED;
	public static final BlockRegistryObject STATION_NAME_TALL_WALL;
	public static final BlockRegistryObject STATION_NAME_WALL_BLACK;
	public static final BlockRegistryObject STATION_NAME_WALL_GRAY;
	public static final BlockRegistryObject STATION_NAME_WALL_WHITE;
	public static final BlockRegistryObject TACTILE_MAP;
	public static final BlockRegistryObject TICKET_BARRIER_ENTRANCE_1;
	public static final BlockRegistryObject TICKET_BARRIER_EXIT_1;
	public static final BlockRegistryObject TICKET_MACHINE;
	public static final BlockRegistryObject TICKET_PROCESSOR;
	public static final BlockRegistryObject TICKET_PROCESSOR_ENQUIRY;
	public static final BlockRegistryObject TICKET_PROCESSOR_ENTRANCE;
	public static final BlockRegistryObject TICKET_PROCESSOR_EXIT;
	public static final BlockRegistryObject TRAIN_ANNOUNCER;
	public static final BlockRegistryObject TRAIN_CARGO_LOADER;
	public static final BlockRegistryObject TRAIN_CARGO_UNLOADER;
	public static final BlockRegistryObject TRAIN_REDSTONE_SENSOR;
	public static final BlockRegistryObject TRAIN_SCHEDULE_SENSOR;

	public static void init() {
		Init.LOGGER.info("Registering Minecraft Transit Railway blocks");
	}
}
