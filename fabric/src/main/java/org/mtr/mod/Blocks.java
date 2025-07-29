package org.mtr.mod;

import org.apache.commons.io.FileUtils;
import org.mtr.core.data.TransportMode;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.mapper.BlockItemExtension;
import org.mtr.mapping.mapper.SlabBlockExtension;
import org.mtr.mapping.registry.BlockRegistryObject;
import org.mtr.mapping.registry.CreativeModeTabHolder;
import org.mtr.mod.block.*;
import org.mtr.mod.item.ItemBlockEnchanted;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;


public final class Blocks {

	static {
		REGISTERED_IDENTIFIERS = new ObjectAVLTreeSet<>();

		// Nodes
		RAIL_NODE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "rail"), () -> new Block(new BlockNode(TransportMode.TRAIN)), CreativeModeTabs.CORE);
		BOAT_NODE = registerBlock(new Identifier(Init.MOD_ID, "boat_node"), () -> new Block(new BlockNode(TransportMode.BOAT)));
		CABLE_CAR_NODE_LOWER = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "cable_car_node_lower"), () -> new Block(new BlockNode.BlockContinuousMovementNode(false, false)), CreativeModeTabs.CORE);
		CABLE_CAR_NODE_UPPER = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "cable_car_node_upper"), () -> new Block(new BlockNode.BlockContinuousMovementNode(true, false)), CreativeModeTabs.CORE);
		CABLE_CAR_NODE_STATION = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "cable_car_node_station"), () -> new Block(new BlockNode.BlockContinuousMovementNode(false, true)), CreativeModeTabs.CORE);
		AIRPLANE_NODE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "airplane_node"), () -> new Block(new BlockNode(TransportMode.AIRPLANE)), CreativeModeTabs.CORE);

		// Doors
		APG_DOOR = registerBlock(new Identifier(Init.MOD_ID, "apg_door"), () -> new Block(new BlockAPGDoor()));
		APG_GLASS = registerBlock(new Identifier(Init.MOD_ID, "apg_glass"), () -> new Block(new BlockAPGGlass()));
		APG_GLASS_END = registerBlock(new Identifier(Init.MOD_ID, "apg_glass_end"), () -> new Block(new BlockAPGGlassEnd()));
		PSD_DOOR_1 = registerBlock(new Identifier(Init.MOD_ID, "psd_door"), () -> new Block(new BlockPSDDoor(0)));
		PSD_GLASS_1 = registerBlock(new Identifier(Init.MOD_ID, "psd_glass"), () -> new Block(new BlockPSDGlass(0)));
		PSD_GLASS_END_1 = registerBlock(new Identifier(Init.MOD_ID, "psd_glass_end"), () -> new Block(new BlockPSDGlassEnd(0)));
		PSD_DOOR_2 = registerBlock(new Identifier(Init.MOD_ID, "psd_door_2"), () -> new Block(new BlockPSDDoor(1)));
		PSD_GLASS_2 = registerBlock(new Identifier(Init.MOD_ID, "psd_glass_2"), () -> new Block(new BlockPSDGlass(1)));
		PSD_GLASS_END_2 = registerBlock(new Identifier(Init.MOD_ID, "psd_glass_end_2"), () -> new Block(new BlockPSDGlassEnd(1)));
		PSD_TOP = registerBlock(new Identifier(Init.MOD_ID, "psd_top"), () -> new Block(new BlockPSDTop()));

		// Escalators lifts
		ESCALATOR_SIDE = registerBlock(new Identifier(Init.MOD_ID, "escalator_side"), () -> new Block(new BlockEscalatorSide()));
		ESCALATOR_STEP = registerBlock(new Identifier(Init.MOD_ID, "escalator_step"), () -> new Block(new BlockEscalatorStep()));
		LIFT_BUTTONS_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_buttons_1"), () -> new Block(new BlockLiftButtons()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_PANEL_EVEN_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_panel_even_1"), () -> new Block(new BlockLiftPanelEven1()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_PANEL_ODD_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_panel_odd_1"), () -> new Block(new BlockLiftPanelOdd1()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_PANEL_EVEN_2 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_panel_even_2"), () -> new Block(new BlockLiftPanelEven2()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_PANEL_ODD_2 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_panel_odd_2"), () -> new Block(new BlockLiftPanelOdd2()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_TRACK_HORIZONTAL_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_track_horizontal_1"), () -> new Block(new BlockLiftTrackHorizontal()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_TRACK_VERTICAL_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_track_1"), () -> new Block(new BlockLiftTrackVertical()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_TRACK_DIAGONAL_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_track_diagonal_1"), () -> new Block(new BlockLiftTrackDiagonal()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_TRACK_FLOOR_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "lift_track_floor_1"), () -> new Block(new BlockLiftTrackFloor()), CreativeModeTabs.ESCALATORS_LIFTS);
		LIFT_DOOR_EVEN_1 = registerBlock(new Identifier(Init.MOD_ID, "lift_door_1"), () -> new Block(new BlockLiftDoor()));
		LIFT_DOOR_ODD_1 = registerBlock(new Identifier(Init.MOD_ID, "lift_door_odd_1"), () -> new Block(new BlockLiftDoorOdd()));

		// PIDS projectors
		PIDS_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "pids_1"), () -> new Block(new BlockPIDSHorizontal1()), CreativeModeTabs.RAILWAY_FACILITIES);
		PIDS_2 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "pids_2"), () -> new Block(new BlockPIDSHorizontal2()), CreativeModeTabs.RAILWAY_FACILITIES);
		PIDS_3 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "pids_3"), () -> new Block(new BlockPIDSHorizontal3()), CreativeModeTabs.RAILWAY_FACILITIES);
		PIDS_4 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "pids_4"), () -> new Block(new BlockPIDSVertical1()), CreativeModeTabs.RAILWAY_FACILITIES);
		PIDS_POLE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "pids_pole"), () -> new Block(new BlockPIDSPole(createDefaultBlockSettings(false))), CreativeModeTabs.RAILWAY_FACILITIES);
		PIDS_SINGLE_ARRIVAL_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "pids_single_arrival_1"), () -> new Block(new BlockPIDSVerticalSingleArrival1()), CreativeModeTabs.RAILWAY_FACILITIES);
		ARRIVAL_PROJECTOR_1_SMALL = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "arrival_projector_1_small"), () -> new Block(new BlockArrivalProjector1Small()), CreativeModeTabs.RAILWAY_FACILITIES);
		ARRIVAL_PROJECTOR_1_MEDIUM = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "arrival_projector_1_medium"), () -> new Block(new BlockArrivalProjector1Medium()), CreativeModeTabs.RAILWAY_FACILITIES);
		ARRIVAL_PROJECTOR_1_LARGE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "arrival_projector_1_large"), () -> new Block(new BlockArrivalProjector1Large()), CreativeModeTabs.RAILWAY_FACILITIES);

		// blocks
		PLATFORM = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform"), () -> new Block(new BlockPlatform(createDefaultBlockSettings(false), false)), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_INDENTED = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_indented"), () -> new Block(new BlockPlatform(createDefaultBlockSettings(false), true)), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_slab"), () -> new Block(new BlockPlatformSlab(createDefaultBlockSettings(false))), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_NA_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_na_1"), () -> new Block(new BlockPlatform(createDefaultBlockSettings(false), false)), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_NA_1_INDENTED = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_na_1_indented"), () -> new Block(new BlockPlatform(createDefaultBlockSettings(false), true)), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_NA_1_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_na_1_slab"), () -> new Block(new BlockPlatformSlab(createDefaultBlockSettings(false))), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_NA_2 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_na_2"), () -> new Block(new BlockPlatform(createDefaultBlockSettings(false), false)), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_NA_2_INDENTED = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_na_2_indented"), () -> new Block(new BlockPlatform(createDefaultBlockSettings(false), true)), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_NA_2_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_na_2_slab"), () -> new Block(new BlockPlatformSlab(createDefaultBlockSettings(false))), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_UK_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_uk_1"), () -> new Block(new BlockPlatform(createDefaultBlockSettings(false), false)), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_UK_1_INDENTED = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_uk_1_indented"), () -> new Block(new BlockPlatform(createDefaultBlockSettings(false), true)), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		PLATFORM_UK_1_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "platform_uk_1_slab"), () -> new Block(new BlockPlatformSlab(createDefaultBlockSettings(false))), CreativeModeTabs.STATION_BUILDING_BLOCKS);

		// Signs
		RAILWAY_SIGN_2_EVEN = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "railway_sign_2_even"), () -> new Block(new BlockRailwaySign(2, false)), CreativeModeTabs.RAILWAY_FACILITIES);
		RAILWAY_SIGN_2_ODD = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "railway_sign_2_odd"), () -> new Block(new BlockRailwaySign(2, true)), CreativeModeTabs.RAILWAY_FACILITIES);
		RAILWAY_SIGN_3_EVEN = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "railway_sign_3_even"), () -> new Block(new BlockRailwaySign(3, false)), CreativeModeTabs.RAILWAY_FACILITIES);
		RAILWAY_SIGN_3_ODD = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "railway_sign_3_odd"), () -> new Block(new BlockRailwaySign(3, true)), CreativeModeTabs.RAILWAY_FACILITIES);
		RAILWAY_SIGN_4_EVEN = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "railway_sign_4_even"), () -> new Block(new BlockRailwaySign(4, false)), CreativeModeTabs.RAILWAY_FACILITIES);
		RAILWAY_SIGN_4_ODD = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "railway_sign_4_odd"), () -> new Block(new BlockRailwaySign(4, true)), CreativeModeTabs.RAILWAY_FACILITIES);
		RAILWAY_SIGN_5_EVEN = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "railway_sign_5_even"), () -> new Block(new BlockRailwaySign(5, false)), CreativeModeTabs.RAILWAY_FACILITIES);
		RAILWAY_SIGN_5_ODD = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "railway_sign_5_odd"), () -> new Block(new BlockRailwaySign(5, true)), CreativeModeTabs.RAILWAY_FACILITIES);
		RAILWAY_SIGN_6_EVEN = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "railway_sign_6_even"), () -> new Block(new BlockRailwaySign(6, false)), CreativeModeTabs.RAILWAY_FACILITIES);
		RAILWAY_SIGN_6_ODD = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "railway_sign_6_odd"), () -> new Block(new BlockRailwaySign(6, true)), CreativeModeTabs.RAILWAY_FACILITIES);
		RAILWAY_SIGN_7_EVEN = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "railway_sign_7_even"), () -> new Block(new BlockRailwaySign(7, false)), CreativeModeTabs.RAILWAY_FACILITIES);
		RAILWAY_SIGN_7_ODD = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "railway_sign_7_odd"), () -> new Block(new BlockRailwaySign(7, true)), CreativeModeTabs.RAILWAY_FACILITIES);
		RAILWAY_SIGN_MIDDLE = registerBlock(new Identifier(Init.MOD_ID, "railway_sign_middle"), () -> new Block(new BlockRailwaySign(0, false)));
		RAILWAY_SIGN_POLE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "railway_sign_pole"), () -> new Block(new BlockRailwaySignPole(createDefaultBlockSettings(false))), CreativeModeTabs.RAILWAY_FACILITIES);
		ROUTE_SIGN_STANDING_LIGHT = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "route_sign_standing_light"), () -> new Block(new BlockRouteSignStandingLight()), CreativeModeTabs.RAILWAY_FACILITIES);
		ROUTE_SIGN_STANDING_METAL = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "route_sign_standing_metal"), () -> new Block(new BlockRouteSignStandingMetal()), CreativeModeTabs.RAILWAY_FACILITIES);
		ROUTE_SIGN_WALL_LIGHT = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "route_sign_wall_light"), () -> new Block(new BlockRouteSignWallLight()), CreativeModeTabs.RAILWAY_FACILITIES);
		ROUTE_SIGN_WALL_METAL = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "route_sign_wall_metal"), () -> new Block(new BlockRouteSignWallMetal()), CreativeModeTabs.RAILWAY_FACILITIES);

		// Signals
		SIGNAL_LIGHT_2_ASPECT_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "signal_light_1"), () -> new Block(new BlockSignalLight2Aspect1(createDefaultBlockSettings(true))), CreativeModeTabs.RAILWAY_FACILITIES);
		SIGNAL_LIGHT_2_ASPECT_2 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "signal_light_2"), () -> new Block(new BlockSignalLight2Aspect2(createDefaultBlockSettings(true))), CreativeModeTabs.RAILWAY_FACILITIES);
		SIGNAL_LIGHT_2_ASPECT_3 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "signal_light_3"), () -> new Block(new BlockSignalLight2Aspect3(createDefaultBlockSettings(true))), CreativeModeTabs.RAILWAY_FACILITIES);
		SIGNAL_LIGHT_2_ASPECT_4 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "signal_light_4"), () -> new Block(new BlockSignalLight2Aspect4(createDefaultBlockSettings(true))), CreativeModeTabs.RAILWAY_FACILITIES);
		SIGNAL_LIGHT_3_ASPECT_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "signal_light_3_aspect_1"), () -> new Block(new BlockSignalLight3Aspect1(createDefaultBlockSettings(true))), CreativeModeTabs.RAILWAY_FACILITIES);
		SIGNAL_LIGHT_3_ASPECT_2 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "signal_light_3_aspect_2"), () -> new Block(new BlockSignalLight3Aspect2(createDefaultBlockSettings(true))), CreativeModeTabs.RAILWAY_FACILITIES);
		SIGNAL_LIGHT_4_ASPECT_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "signal_light_4_aspect_1"), () -> new Block(new BlockSignalLight4Aspect1(createDefaultBlockSettings(true))), CreativeModeTabs.RAILWAY_FACILITIES);
		SIGNAL_LIGHT_4_ASPECT_2 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "signal_light_4_aspect_2"), () -> new Block(new BlockSignalLight4Aspect2(createDefaultBlockSettings(true))), CreativeModeTabs.RAILWAY_FACILITIES);
		SIGNAL_SEMAPHORE_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "signal_semaphore_1"), () -> new Block(new BlockSignalSemaphore1(createDefaultBlockSettings(true))), CreativeModeTabs.RAILWAY_FACILITIES);
		SIGNAL_SEMAPHORE_2 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "signal_semaphore_2"), () -> new Block(new BlockSignalSemaphore2(createDefaultBlockSettings(true))), CreativeModeTabs.RAILWAY_FACILITIES);
		SIGNAL_POLE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "signal_pole"), () -> new Block(new BlockStationColorPole(false)), CreativeModeTabs.RAILWAY_FACILITIES);

		// name
		STATION_NAME_ENTRANCE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_name_entrance"), () -> new Block(new BlockStationNameEntrance(createDefaultBlockSettings(true))), CreativeModeTabs.RAILWAY_FACILITIES);
		STATION_NAME_TALL_BLOCK = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_name_tall_block"), () -> new Block(new BlockStationNameTallBlock()), CreativeModeTabs.RAILWAY_FACILITIES);
		STATION_NAME_TALL_BLOCK_DOUBLE_SIDED = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_name_tall_block_double_sided"), () -> new Block(new BlockStationNameTallBlockDoubleSided()), CreativeModeTabs.RAILWAY_FACILITIES);
		STATION_NAME_TALL_WALL = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_name_tall_wall"), () -> new Block(new BlockStationNameTallWall()), CreativeModeTabs.RAILWAY_FACILITIES);
		STATION_NAME_TALL_STANDING = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_name_tall_standing"), () -> new Block(new BlockStationNameTallStanding()), CreativeModeTabs.RAILWAY_FACILITIES);
		STATION_NAME_WALL_WHITE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_name_wall"), () -> new Block(new BlockStationNameWallWhite(createDefaultBlockSettings(true))), CreativeModeTabs.RAILWAY_FACILITIES);
		STATION_NAME_WALL_GRAY = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_name_wall_gray"), () -> new Block(new BlockStationNameWallGray(createDefaultBlockSettings(true))), CreativeModeTabs.RAILWAY_FACILITIES);
		STATION_NAME_WALL_BLACK = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_name_wall_black"), () -> new Block(new BlockStationNameWallBlack(createDefaultBlockSettings(true))), CreativeModeTabs.RAILWAY_FACILITIES);

		// Station blocks
		STATION_COLOR_ANDESITE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_andesite"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_BEDROCK = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_bedrock"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_BIRCH_WOOD = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_birch_wood"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_BONE_BLOCK = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_bone_block"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_CHISELED_QUARTZ_BLOCK = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_chiseled_quartz_block"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_CHISELED_STONE_BRICKS = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_chiseled_stone_bricks"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_CLAY = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_clay"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_COAL_ORE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_coal_ore"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_COBBLESTONE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_cobblestone"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_CONCRETE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_concrete"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_CONCRETE_POWDER = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_concrete_powder"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_CRACKED_STONE_BRICKS = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_cracked_stone_bricks"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_DARK_PRISMARINE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_dark_prismarine"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_DIORITE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_diorite"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_GRAVEL = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_gravel"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_IRON_BLOCK = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_iron_block"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_METAL = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_metal"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_MOSAIC_TILE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_mosaic_tile"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_PLANKS = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_planks"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_POLISHED_ANDESITE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_polished_andesite"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_POLISHED_DIORITE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_polished_diorite"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_PURPUR_BLOCK = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_purpur_block"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_PURPUR_PILLAR = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_purpur_pillar"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_QUARTZ_BLOCK = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_quartz_block"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_QUARTZ_BRICKS = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_quartz_bricks"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_QUARTZ_PILLAR = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_quartz_pillar"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_SMOOTH_QUARTZ = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_smooth_quartz"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_SMOOTH_STONE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_smooth_stone"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_SNOW_BLOCK = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_snow_block"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_STAINED_GLASS = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_stained_glass"), () -> new Block(new BlockStationColorGlass()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_STONE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_stone"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_STONE_BRICKS = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_stone_bricks"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_WOOL = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_wool"), () -> new Block(new BlockStationColor()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);

		// Station slabs
		STATION_COLOR_ANDESITE_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_andesite_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_BEDROCK_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_bedrock_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_BIRCH_WOOD_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_birch_wood_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_BONE_BLOCK_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_bone_block_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_CHISELED_QUARTZ_BLOCK_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_chiseled_quartz_block_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_CHISELED_STONE_BRICKS_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_chiseled_stone_bricks_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_CLAY_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_clay_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_COAL_ORE_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_coal_ore_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_COBBLESTONE_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_cobblestone_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_CONCRETE_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_concrete_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_CONCRETE_POWDER_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_concrete_powder_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_CRACKED_STONE_BRICKS_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_cracked_stone_bricks_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_DARK_PRISMARINE_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_dark_prismarine_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_DIORITE_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_diorite_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_GRAVEL_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_gravel_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_IRON_BLOCK_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_iron_block_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_METAL_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_metal_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_MOSAIC_TILE_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_mosaic_tile_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_PLANKS_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_planks_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_POLISHED_ANDESITE_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_polished_andesite_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_POLISHED_DIORITE_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_polished_diorite_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_PURPUR_BLOCK_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_purpur_block_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_PURPUR_PILLAR_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_purpur_pillar_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_QUARTZ_BLOCK_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_quartz_block_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_QUARTZ_BRICKS_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_quartz_bricks_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_QUARTZ_PILLAR_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_quartz_pillar_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_SMOOTH_QUARTZ_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_smooth_quartz_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_SMOOTH_STONE_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_smooth_stone_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_SNOW_BLOCK_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_snow_block_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_STAINED_GLASS_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_stained_glass_slab"), () -> new Block(new BlockStationColorGlassSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_STONE_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_stone_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_STONE_BRICKS_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_stone_bricks_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);
		STATION_COLOR_WOOL_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_color_wool_slab"), () -> new Block(new BlockStationColorSlab()), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);

		// Station misc
		STATION_COLOR_POLE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "station_pole"), () -> new Block(new BlockStationColorPole(true)), ItemBlockEnchanted::new, CreativeModeTabs.STATION_BUILDING_BLOCKS);

		// machines
		TICKET_BARRIER_ENTRANCE_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "ticket_barrier_entrance_1"), () -> new Block(new BlockTicketBarrier(true)), CreativeModeTabs.RAILWAY_FACILITIES);
		TICKET_BARRIER_EXIT_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "ticket_barrier_exit_1"), () -> new Block(new BlockTicketBarrier(false)), CreativeModeTabs.RAILWAY_FACILITIES);
		TICKET_MACHINE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "ticket_machine"), () -> new Block(new BlockTicketMachine(createDefaultBlockSettings(true, blockState -> 5))), CreativeModeTabs.RAILWAY_FACILITIES);
		TICKET_PROCESSOR = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "ticket_processor"), () -> new Block(new BlockTicketProcessor(true, true, true)), CreativeModeTabs.RAILWAY_FACILITIES);
		TICKET_PROCESSOR_ENTRANCE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "ticket_processor_entrance"), () -> new Block(new BlockTicketProcessor(true, true, false)), CreativeModeTabs.RAILWAY_FACILITIES);
		TICKET_PROCESSOR_EXIT = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "ticket_processor_exit"), () -> new Block(new BlockTicketProcessor(true, false, true)), CreativeModeTabs.RAILWAY_FACILITIES);
		TICKET_PROCESSOR_ENQUIRY = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "ticket_processor_enquiry"), () -> new Block(new BlockTicketProcessorEnquiry()), CreativeModeTabs.RAILWAY_FACILITIES);

		// Sensors
		TRAIN_ANNOUNCER = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "train_announcer"), () -> new Block(new BlockTrainAnnouncer()), CreativeModeTabs.RAILWAY_FACILITIES);
		TRAIN_CARGO_LOADER = registerBlock(new Identifier(Init.MOD_ID, "train_cargo_loader"), () -> new Block(new BlockTrainCargoLoader()));
		TRAIN_CARGO_UNLOADER = registerBlock(new Identifier(Init.MOD_ID, "train_cargo_unloader"), () -> new Block(new BlockTrainCargoUnloader()));
		TRAIN_REDSTONE_SENSOR = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "train_sensor"), () -> new Block(new BlockTrainRedstoneSensor()), CreativeModeTabs.RAILWAY_FACILITIES);
		TRAIN_SCHEDULE_SENSOR = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "train_schedule_sensor"), () -> new Block(new BlockTrainScheduleSensor()), CreativeModeTabs.RAILWAY_FACILITIES);

		// Misc
		CEILING = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "ceiling"), () -> new Block(new BlockCeilingAuto(createDefaultBlockSettings(false, blockState -> 15))), CreativeModeTabs.RAILWAY_FACILITIES);
		CEILING_LIGHT = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "ceiling_light"), () -> new Block(new BlockCeiling(createDefaultBlockSettings(false, blockState -> 15))), CreativeModeTabs.RAILWAY_FACILITIES);
		CEILING_NO_LIGHT = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "ceiling_no_light"), () -> new Block(new BlockCeiling(createDefaultBlockSettings(false))), CreativeModeTabs.RAILWAY_FACILITIES);
		CLOCK = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "clock"), () -> new Block(new BlockClock(createDefaultBlockSettings(true, blockState -> 5))), CreativeModeTabs.RAILWAY_FACILITIES);
		CLOCK_POLE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "clock_pole"), () -> new Block(new BlockClockPole(createDefaultBlockSettings(false))), CreativeModeTabs.RAILWAY_FACILITIES);
		GLASS_FENCE_CIO = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "glass_fence_cio"), () -> new Block(new BlockGlassFence()), CreativeModeTabs.RAILWAY_FACILITIES);
		GLASS_FENCE_CKT = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "glass_fence_ckt"), () -> new Block(new BlockGlassFence()), CreativeModeTabs.RAILWAY_FACILITIES);
		GLASS_FENCE_HEO = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "glass_fence_heo"), () -> new Block(new BlockGlassFence()), CreativeModeTabs.RAILWAY_FACILITIES);
		GLASS_FENCE_MOS = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "glass_fence_mos"), () -> new Block(new BlockGlassFence()), CreativeModeTabs.RAILWAY_FACILITIES);
		GLASS_FENCE_PLAIN = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "glass_fence_plain"), () -> new Block(new BlockGlassFence()), CreativeModeTabs.RAILWAY_FACILITIES);
		GLASS_FENCE_SHM = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "glass_fence_shm"), () -> new Block(new BlockGlassFence()), CreativeModeTabs.RAILWAY_FACILITIES);
		GLASS_FENCE_STAINED = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "glass_fence_stained"), () -> new Block(new BlockGlassFence()), CreativeModeTabs.RAILWAY_FACILITIES);
		GLASS_FENCE_STW = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "glass_fence_stw"), () -> new Block(new BlockGlassFence()), CreativeModeTabs.RAILWAY_FACILITIES);
		GLASS_FENCE_TSH = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "glass_fence_tsh"), () -> new Block(new BlockGlassFence()), CreativeModeTabs.RAILWAY_FACILITIES);
		GLASS_FENCE_WKS = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "glass_fence_wks"), () -> new Block(new BlockGlassFence()), CreativeModeTabs.RAILWAY_FACILITIES);
		DRIVER_KEY_DISPENSER = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "driver_key_dispenser"), () -> new Block(new BlockDriverKeyDispenser(createDefaultBlockSettings(true))), CreativeModeTabs.CORE);
		LOGO = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "logo"), () -> new Block(new BlockExtension(createDefaultBlockSettings(false, blockState -> 10))), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		MARBLE_BLUE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "marble_blue"), () -> new Block(new BlockExtension(createDefaultBlockSettings(false))), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		MARBLE_BLUE_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "marble_blue_slab"), () -> new Block(new SlabBlockExtension(createDefaultBlockSettings(false))), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		MARBLE_SANDY = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "marble_sandy"), () -> new Block(new BlockExtension(createDefaultBlockSettings(false))), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		MARBLE_SANDY_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "marble_sandy_slab"), () -> new Block(new SlabBlockExtension(createDefaultBlockSettings(false))), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		METAL = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "metal"), () -> new Block(new BlockExtension(createDefaultBlockSettings(false))), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		METAL_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "metal_slab"), () -> new Block(new SlabBlockExtension(createDefaultBlockSettings(false))), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		MOSAIC_TILE = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "mosaic_tile"), () -> new Block(new BlockExtension(createDefaultBlockSettings(false))), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		MOSAIC_TILE_SLAB = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "mosaic_tile_slab"), () -> new Block(new SlabBlockExtension(createDefaultBlockSettings(false))), CreativeModeTabs.STATION_BUILDING_BLOCKS);
		RUBBISH_BIN_1 = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "rubbish_bin_1"), () -> new Block(new BlockRubbishBin(createDefaultBlockSettings(false))), CreativeModeTabs.RAILWAY_FACILITIES);
		TACTILE_MAP = registerBlockWithBlockItem(new Identifier(Init.MOD_ID, "tactile_map"), () -> new Block(new BlockTactileMap(createDefaultBlockSettings(false))), CreativeModeTabs.RAILWAY_FACILITIES);

		// NTE
		EYE_CANDY = registerBlockWithBlockItem(new Identifier(Init.MOD_ID_NTE, "eye_candy"), () -> new Block(new BlockEyeCandy()), CreativeModeTabs.STATION_BUILDING_BLOCKS);
	}

	public static final BlockRegistryObject AIRPLANE_NODE;
	public static final BlockRegistryObject APG_DOOR;
	public static final BlockRegistryObject APG_GLASS;
	public static final BlockRegistryObject APG_GLASS_END;
	public static final BlockRegistryObject ARRIVAL_PROJECTOR_1_LARGE;
	public static final BlockRegistryObject ARRIVAL_PROJECTOR_1_MEDIUM;
	public static final BlockRegistryObject ARRIVAL_PROJECTOR_1_SMALL;
	public static final BlockRegistryObject BOAT_NODE;
	public static final BlockRegistryObject CABLE_CAR_NODE_LOWER;
	public static final BlockRegistryObject CABLE_CAR_NODE_STATION;
	public static final BlockRegistryObject CABLE_CAR_NODE_UPPER;
	public static final BlockRegistryObject CEILING;
	public static final BlockRegistryObject CEILING_LIGHT;
	public static final BlockRegistryObject CEILING_NO_LIGHT;
	public static final BlockRegistryObject CLOCK;
	public static final BlockRegistryObject CLOCK_POLE;
	public static final BlockRegistryObject DRIVER_KEY_DISPENSER;
	public static final BlockRegistryObject ESCALATOR_SIDE;
	public static final BlockRegistryObject ESCALATOR_STEP;
	public static final BlockRegistryObject GLASS_FENCE_CIO;
	public static final BlockRegistryObject GLASS_FENCE_CKT;
	public static final BlockRegistryObject GLASS_FENCE_HEO;
	public static final BlockRegistryObject GLASS_FENCE_MOS;
	public static final BlockRegistryObject GLASS_FENCE_PLAIN;
	public static final BlockRegistryObject GLASS_FENCE_SHM;
	public static final BlockRegistryObject GLASS_FENCE_STAINED;
	public static final BlockRegistryObject GLASS_FENCE_STW;
	public static final BlockRegistryObject GLASS_FENCE_TSH;
	public static final BlockRegistryObject GLASS_FENCE_WKS;
	public static final BlockRegistryObject LIFT_BUTTONS_1;
	public static final BlockRegistryObject LIFT_DOOR_EVEN_1;
	public static final BlockRegistryObject LIFT_DOOR_ODD_1;
	public static final BlockRegistryObject LIFT_PANEL_EVEN_1;
	public static final BlockRegistryObject LIFT_PANEL_EVEN_2;
	public static final BlockRegistryObject LIFT_PANEL_ODD_1;
	public static final BlockRegistryObject LIFT_PANEL_ODD_2;
	public static final BlockRegistryObject LIFT_TRACK_HORIZONTAL_1;
	public static final BlockRegistryObject LIFT_TRACK_VERTICAL_1;
	public static final BlockRegistryObject LIFT_TRACK_DIAGONAL_1;
	public static final BlockRegistryObject LIFT_TRACK_FLOOR_1;
	public static final BlockRegistryObject LOGO;
	public static final BlockRegistryObject MARBLE_BLUE;
	public static final BlockRegistryObject MARBLE_BLUE_SLAB;
	public static final BlockRegistryObject MARBLE_SANDY;
	public static final BlockRegistryObject MARBLE_SANDY_SLAB;
	public static final BlockRegistryObject METAL;
	public static final BlockRegistryObject METAL_SLAB;
	public static final BlockRegistryObject MOSAIC_TILE;
	public static final BlockRegistryObject MOSAIC_TILE_SLAB;
	public static final BlockRegistryObject PIDS_1;
	public static final BlockRegistryObject PIDS_2;
	public static final BlockRegistryObject PIDS_3;
	public static final BlockRegistryObject PIDS_4;
	public static final BlockRegistryObject PIDS_POLE;
	public static final BlockRegistryObject PIDS_SINGLE_ARRIVAL_1;
	public static final BlockRegistryObject PLATFORM;
	public static final BlockRegistryObject PLATFORM_INDENTED;
	public static final BlockRegistryObject PLATFORM_SLAB;
	public static final BlockRegistryObject PLATFORM_NA_1;
	public static final BlockRegistryObject PLATFORM_NA_1_INDENTED;
	public static final BlockRegistryObject PLATFORM_NA_1_SLAB;
	public static final BlockRegistryObject PLATFORM_NA_2;
	public static final BlockRegistryObject PLATFORM_NA_2_INDENTED;
	public static final BlockRegistryObject PLATFORM_NA_2_SLAB;
	public static final BlockRegistryObject PLATFORM_UK_1;
	public static final BlockRegistryObject PLATFORM_UK_1_INDENTED;
	public static final BlockRegistryObject PLATFORM_UK_1_SLAB;
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
	public static final BlockRegistryObject STATION_COLOR_MOSAIC_TILE;
	public static final BlockRegistryObject STATION_COLOR_MOSAIC_TILE_SLAB;
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
	public static final BlockRegistryObject STATION_NAME_TALL_STANDING;
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
	public static final BlockRegistryObject EYE_CANDY;

	private static final ObjectAVLTreeSet<String> REGISTERED_IDENTIFIERS;

	public static void init() {
		Init.LOGGER.info("Registering Minecraft Transit Railway blocks");
		if (Keys.DEBUG) {
			try {
				// Validate all registered blocks are in pickaxe.json
				final ObjectAVLTreeSet<String> expectedIdentifiers = new ObjectAVLTreeSet<>();
				JsonParser.parseString(FileUtils.readFileToString(
						MinecraftClient.getInstance().getRunDirectoryMapped().toPath().resolve("../src/main/resources/data/minecraft/tags/blocks/mineable/pickaxe.json").toFile(),
						StandardCharsets.UTF_8
				)).getAsJsonObject().getAsJsonArray("values").forEach(identifier -> expectedIdentifiers.add(identifier.getAsString()));
				REGISTERED_IDENTIFIERS.forEach(identifier -> {
					if (!expectedIdentifiers.contains(identifier)) {
						Init.LOGGER.warn("Identifier missing in pickaxe.json! [{}]", identifier);
					}
				});
				expectedIdentifiers.forEach(identifier -> {
					if (!REGISTERED_IDENTIFIERS.contains(identifier)) {
						Init.LOGGER.warn("Extra identifier in pickaxe.json! [{}]", identifier);
					}
				});

				// Validate json file exists in loot_tables/blocks
				final ObjectAVLTreeSet<String> expectedFiles = new ObjectAVLTreeSet<>();
				try (final Stream<Path> stream = Files.list(MinecraftClient.getInstance().getRunDirectoryMapped().toPath().resolve("../src/main/resources/data/mtr/loot_tables/blocks"))) {
					stream.forEach(path -> expectedFiles.add("mtr:" + path.getFileName().toString().split(".json")[0]));
				}
				try (final Stream<Path> stream = Files.list(MinecraftClient.getInstance().getRunDirectoryMapped().toPath().resolve("../src/main/resources/data/mtrsteamloco/loot_tables/blocks"))) {
					stream.forEach(path -> expectedFiles.add("mtrsteamloco:" + path.getFileName().toString().split(".json")[0]));
				}
				REGISTERED_IDENTIFIERS.forEach(identifier -> {
					if (!expectedFiles.contains(identifier)) {
						Init.LOGGER.warn("Loot table file missing! [{}]", identifier);
					}
				});
				expectedFiles.forEach(identifier -> {
					if (!REGISTERED_IDENTIFIERS.contains(identifier)) {
						Init.LOGGER.warn("Extra loot table file! [{}]", identifier);
					}
				});
			} catch (Exception e) {
				Init.LOGGER.error("", e);
			}
		}
	}

	public static BlockSettings createDefaultBlockSettings(boolean blockPiston) {
		return BlockHelper.createBlockSettings(blockPiston, true).strength(3);
	}

	public static BlockSettings createDefaultBlockSettings(boolean blockPiston, ToIntFunction<BlockState> luminanceFunction) {
		return BlockHelper.createBlockSettings(blockPiston, true, luminanceFunction).strength(3);
	}

	private static BlockRegistryObject registerBlock(Identifier identifier, Supplier<Block> supplier) {
		REGISTERED_IDENTIFIERS.add(identifier.data.toString());
		return Init.REGISTRY.registerBlock(identifier, supplier);
	}

	private static BlockRegistryObject registerBlockWithBlockItem(Identifier identifier, Supplier<Block> supplier, CreativeModeTabHolder... creativeModeTabHolders) {
		REGISTERED_IDENTIFIERS.add(identifier.data.toString());
		return Init.REGISTRY.registerBlockWithBlockItem(identifier, supplier, creativeModeTabHolders);
	}

	private static BlockRegistryObject registerBlockWithBlockItem(Identifier identifier, Supplier<Block> supplier, BiFunction<Block, ItemSettings, BlockItemExtension> function, CreativeModeTabHolder... creativeModeTabHolders) {
		REGISTERED_IDENTIFIERS.add(identifier.data.toString());
		return Init.REGISTRY.registerBlockWithBlockItem(identifier, supplier, function, creativeModeTabHolders);
	}
}
