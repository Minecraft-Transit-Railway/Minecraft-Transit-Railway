package org.mtr.registry;

import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import org.apache.commons.io.FileUtils;
import org.mtr.Keys;
import org.mtr.MTR;
import org.mtr.block.*;
import org.mtr.core.data.TransportMode;
import org.mtr.item.ItemBlockEnchanted;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;


public final class Blocks {

	private static ObjectAVLTreeSet<String> REGISTERED_IDENTIFIERS = new ObjectAVLTreeSet<>();

	// Nodes
	public static final ObjectHolder<Block> RAIL_NODE = registerBlockWithBlockItem("rail", settings -> new BlockNode(settings, TransportMode.TRAIN), true, ItemGroups.CORE);
	public static final ObjectHolder<Block> BOAT_NODE = registerBlock("boat_node", settings -> new BlockNode(settings, TransportMode.BOAT));
	public static final ObjectHolder<Block> CABLE_CAR_NODE_LOWER = registerBlockWithBlockItem("cable_car_node_lower", settings -> new BlockNode.BlockContinuousMovementNode(settings, false, false), true, ItemGroups.CORE);
	public static final ObjectHolder<Block> CABLE_CAR_NODE_UPPER = registerBlockWithBlockItem("cable_car_node_upper", settings -> new BlockNode.BlockContinuousMovementNode(settings, true, false), true, ItemGroups.CORE);
	public static final ObjectHolder<Block> CABLE_CAR_NODE_STATION = registerBlockWithBlockItem("cable_car_node_station", settings -> new BlockNode.BlockContinuousMovementNode(settings, false, true), true, ItemGroups.CORE);
	public static final ObjectHolder<Block> AIRPLANE_NODE = registerBlockWithBlockItem("airplane_node", settings -> new BlockNode(settings, TransportMode.AIRPLANE), true, ItemGroups.CORE);

	// Doors
	public static final ObjectHolder<Block> APG_DOOR = registerBlock("apg_door", BlockAPGDoor::new);
	public static final ObjectHolder<Block> APG_GLASS = registerBlock("apg_glass", BlockAPGGlass::new);
	public static final ObjectHolder<Block> APG_GLASS_END = registerBlock("apg_glass_end", BlockAPGGlassEnd::new);
	public static final ObjectHolder<Block> PSD_DOOR_1 = registerBlock("psd_door", settings -> new BlockPSDDoor(settings, 0));
	public static final ObjectHolder<Block> PSD_GLASS_1 = registerBlock("psd_glass", settings -> new BlockPSDGlass(settings, 0));
	public static final ObjectHolder<Block> PSD_GLASS_END_1 = registerBlock("psd_glass_end", settings -> new BlockPSDGlassEnd(settings, 0));
	public static final ObjectHolder<Block> PSD_DOOR_2 = registerBlock("psd_door_2", settings -> new BlockPSDDoor(settings, 1));
	public static final ObjectHolder<Block> PSD_GLASS_2 = registerBlock("psd_glass_2", settings -> new BlockPSDGlass(settings, 1));
	public static final ObjectHolder<Block> PSD_GLASS_END_2 = registerBlock("psd_glass_end_2", settings -> new BlockPSDGlassEnd(settings, 1));
	public static final ObjectHolder<Block> PSD_TOP = registerBlock("psd_top", BlockPSDTop::new);

	// Escalators lifts
	public static final ObjectHolder<Block> ESCALATOR_SIDE = registerBlock("escalator_side", BlockEscalatorSide::new);
	public static final ObjectHolder<Block> ESCALATOR_STEP = registerBlock("escalator_step", BlockEscalatorStep::new);
	public static final ObjectHolder<Block> LIFT_BUTTONS_1 = registerBlockWithBlockItem("lift_buttons_1", BlockLiftButtons::new, true, ItemGroups.ESCALATORS_LIFTS);
	public static final ObjectHolder<Block> LIFT_PANEL_EVEN_1 = registerBlockWithBlockItem("lift_panel_even_1", BlockLiftPanelEven1::new, true, ItemGroups.ESCALATORS_LIFTS);
	public static final ObjectHolder<Block> LIFT_PANEL_ODD_1 = registerBlockWithBlockItem("lift_panel_odd_1", BlockLiftPanelOdd1::new, true, ItemGroups.ESCALATORS_LIFTS);
	public static final ObjectHolder<Block> LIFT_PANEL_EVEN_2 = registerBlockWithBlockItem("lift_panel_even_2", BlockLiftPanelEven2::new, true, ItemGroups.ESCALATORS_LIFTS);
	public static final ObjectHolder<Block> LIFT_PANEL_ODD_2 = registerBlockWithBlockItem("lift_panel_odd_2", BlockLiftPanelOdd2::new, true, ItemGroups.ESCALATORS_LIFTS);
	public static final ObjectHolder<Block> LIFT_TRACK_HORIZONTAL_1 = registerBlockWithBlockItem("lift_track_horizontal_1", BlockLiftTrackHorizontal::new, true, ItemGroups.ESCALATORS_LIFTS);
	public static final ObjectHolder<Block> LIFT_TRACK_VERTICAL_1 = registerBlockWithBlockItem("lift_track_1", BlockLiftTrackVertical::new, true, ItemGroups.ESCALATORS_LIFTS);
	public static final ObjectHolder<Block> LIFT_TRACK_DIAGONAL_1 = registerBlockWithBlockItem("lift_track_diagonal_1", BlockLiftTrackDiagonal::new, true, ItemGroups.ESCALATORS_LIFTS);
	public static final ObjectHolder<Block> LIFT_TRACK_FLOOR_1 = registerBlockWithBlockItem("lift_track_floor_1", BlockLiftTrackFloor::new, true, ItemGroups.ESCALATORS_LIFTS);
	public static final ObjectHolder<Block> LIFT_DOOR_EVEN_1 = registerBlock("lift_door_1", BlockLiftDoor::new);
	public static final ObjectHolder<Block> LIFT_DOOR_ODD_1 = registerBlock("lift_door_odd_1", BlockLiftDoorOdd::new);

	// PIDS projectors
	public static final ObjectHolder<Block> PIDS_1 = registerBlockWithBlockItem("pids_1", BlockPIDSHorizontal1::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> PIDS_2 = registerBlockWithBlockItem("pids_2", BlockPIDSHorizontal2::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> PIDS_3 = registerBlockWithBlockItem("pids_3", BlockPIDSHorizontal3::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> PIDS_4 = registerBlockWithBlockItem("pids_4", BlockPIDSVertical1::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> PIDS_POLE = registerBlockWithBlockItem("pids_pole", BlockPIDSPole::new, false, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> PIDS_SINGLE_ARRIVAL_1 = registerBlockWithBlockItem("pids_single_arrival_1", BlockPIDSVerticalSingleArrival1::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> ARRIVAL_PROJECTOR_1_SMALL = registerBlockWithBlockItem("arrival_projector_1_small", BlockArrivalProjector1Small::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> ARRIVAL_PROJECTOR_1_MEDIUM = registerBlockWithBlockItem("arrival_projector_1_medium", BlockArrivalProjector1Medium::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> ARRIVAL_PROJECTOR_1_LARGE = registerBlockWithBlockItem("arrival_projector_1_large", BlockArrivalProjector1Large::new, true, ItemGroups.RAILWAY_FACILITIES);

	// blocks
	public static final ObjectHolder<Block> PLATFORM = registerBlockWithBlockItem("platform", settings -> new BlockPlatform(settings, false), false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> PLATFORM_INDENTED = registerBlockWithBlockItem("platform_indented", settings -> new BlockPlatform(settings, true), false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> PLATFORM_SLAB = registerBlockWithBlockItem("platform_slab", BlockPlatformSlab::new, false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> PLATFORM_NA_1 = registerBlockWithBlockItem("platform_na_1", settings -> new BlockPlatform(settings, false), false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> PLATFORM_NA_1_INDENTED = registerBlockWithBlockItem("platform_na_1_indented", settings -> new BlockPlatform(settings, true), false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> PLATFORM_NA_1_SLAB = registerBlockWithBlockItem("platform_na_1_slab", BlockPlatformSlab::new, false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> PLATFORM_NA_2 = registerBlockWithBlockItem("platform_na_2", settings -> new BlockPlatform(settings, false), false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> PLATFORM_NA_2_INDENTED = registerBlockWithBlockItem("platform_na_2_indented", settings -> new BlockPlatform(settings, true), false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> PLATFORM_NA_2_SLAB = registerBlockWithBlockItem("platform_na_2_slab", BlockPlatformSlab::new, false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> PLATFORM_UK_1 = registerBlockWithBlockItem("platform_uk_1", settings -> new BlockPlatform(settings, false), false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> PLATFORM_UK_1_INDENTED = registerBlockWithBlockItem("platform_uk_1_indented", settings -> new BlockPlatform(settings, true), false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> PLATFORM_UK_1_SLAB = registerBlockWithBlockItem("platform_uk_1_slab", BlockPlatformSlab::new, false, ItemGroups.STATION_BUILDING_BLOCKS);

	// Signs
	public static final ObjectHolder<Block> RAILWAY_SIGN_2_EVEN = registerBlockWithBlockItem("railway_sign_2_even", settings -> new BlockRailwaySign(settings, 2, false), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> RAILWAY_SIGN_2_ODD = registerBlockWithBlockItem("railway_sign_2_odd", settings -> new BlockRailwaySign(settings, 2, true), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> RAILWAY_SIGN_3_EVEN = registerBlockWithBlockItem("railway_sign_3_even", settings -> new BlockRailwaySign(settings, 3, false), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> RAILWAY_SIGN_3_ODD = registerBlockWithBlockItem("railway_sign_3_odd", settings -> new BlockRailwaySign(settings, 3, true), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> RAILWAY_SIGN_4_EVEN = registerBlockWithBlockItem("railway_sign_4_even", settings -> new BlockRailwaySign(settings, 4, false), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> RAILWAY_SIGN_4_ODD = registerBlockWithBlockItem("railway_sign_4_odd", settings -> new BlockRailwaySign(settings, 4, true), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> RAILWAY_SIGN_5_EVEN = registerBlockWithBlockItem("railway_sign_5_even", settings -> new BlockRailwaySign(settings, 5, false), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> RAILWAY_SIGN_5_ODD = registerBlockWithBlockItem("railway_sign_5_odd", settings -> new BlockRailwaySign(settings, 5, true), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> RAILWAY_SIGN_6_EVEN = registerBlockWithBlockItem("railway_sign_6_even", settings -> new BlockRailwaySign(settings, 6, false), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> RAILWAY_SIGN_6_ODD = registerBlockWithBlockItem("railway_sign_6_odd", settings -> new BlockRailwaySign(settings, 6, true), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> RAILWAY_SIGN_7_EVEN = registerBlockWithBlockItem("railway_sign_7_even", settings -> new BlockRailwaySign(settings, 7, false), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> RAILWAY_SIGN_7_ODD = registerBlockWithBlockItem("railway_sign_7_odd", settings -> new BlockRailwaySign(settings, 7, true), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> RAILWAY_SIGN_MIDDLE = registerBlock("railway_sign_middle", settings -> new BlockRailwaySign(settings, 0, false));
	public static final ObjectHolder<Block> RAILWAY_SIGN_POLE = registerBlockWithBlockItem("railway_sign_pole", BlockRailwaySignPole::new, false, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> ROUTE_SIGN_STANDING_LIGHT = registerBlockWithBlockItem("route_sign_standing_light", BlockRouteSignStandingLight::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> ROUTE_SIGN_STANDING_METAL = registerBlockWithBlockItem("route_sign_standing_metal", BlockRouteSignStandingMetal::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> ROUTE_SIGN_WALL_LIGHT = registerBlockWithBlockItem("route_sign_wall_light", BlockRouteSignWallLight::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> ROUTE_SIGN_WALL_METAL = registerBlockWithBlockItem("route_sign_wall_metal", BlockRouteSignWallMetal::new, true, ItemGroups.RAILWAY_FACILITIES);

	// Signals
	public static final ObjectHolder<Block> SIGNAL_LIGHT_2_ASPECT_1 = registerBlockWithBlockItem("signal_light_1", BlockSignalLight2Aspect1::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> SIGNAL_LIGHT_2_ASPECT_2 = registerBlockWithBlockItem("signal_light_2", BlockSignalLight2Aspect2::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> SIGNAL_LIGHT_2_ASPECT_3 = registerBlockWithBlockItem("signal_light_3", BlockSignalLight2Aspect3::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> SIGNAL_LIGHT_2_ASPECT_4 = registerBlockWithBlockItem("signal_light_4", BlockSignalLight2Aspect4::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> SIGNAL_LIGHT_3_ASPECT_1 = registerBlockWithBlockItem("signal_light_3_aspect_1", BlockSignalLight3Aspect1::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> SIGNAL_LIGHT_3_ASPECT_2 = registerBlockWithBlockItem("signal_light_3_aspect_2", BlockSignalLight3Aspect2::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> SIGNAL_LIGHT_4_ASPECT_1 = registerBlockWithBlockItem("signal_light_4_aspect_1", BlockSignalLight4Aspect1::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> SIGNAL_LIGHT_4_ASPECT_2 = registerBlockWithBlockItem("signal_light_4_aspect_2", BlockSignalLight4Aspect2::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> SIGNAL_SEMAPHORE_1 = registerBlockWithBlockItem("signal_semaphore_1", BlockSignalSemaphore1::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> SIGNAL_SEMAPHORE_2 = registerBlockWithBlockItem("signal_semaphore_2", BlockSignalSemaphore2::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> SIGNAL_POLE = registerBlockWithBlockItem("signal_pole", settings -> new BlockStationColorPole(settings, false), false, ItemGroups.RAILWAY_FACILITIES);

	// name
	public static final ObjectHolder<Block> STATION_NAME_ENTRANCE = registerBlockWithBlockItem("station_name_entrance", BlockStationNameEntrance::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> STATION_NAME_TALL_BLOCK = registerBlockWithBlockItem("station_name_tall_block", BlockStationNameTallBlock::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> STATION_NAME_TALL_BLOCK_DOUBLE_SIDED = registerBlockWithBlockItem("station_name_tall_block_double_sided", BlockStationNameTallBlockDoubleSided::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> STATION_NAME_TALL_WALL = registerBlockWithBlockItem("station_name_tall_wall", BlockStationNameTallWall::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> STATION_NAME_WALL_WHITE = registerBlockWithBlockItem("station_name_wall", BlockStationNameWallWhite::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> STATION_NAME_WALL_GRAY = registerBlockWithBlockItem("station_name_wall_gray", BlockStationNameWallGray::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> STATION_NAME_WALL_BLACK = registerBlockWithBlockItem("station_name_wall_black", BlockStationNameWallBlack::new, true, ItemGroups.RAILWAY_FACILITIES);

	// Station blocks
	public static final ObjectHolder<Block> STATION_COLOR_ANDESITE = registerBlockWithBlockItem("station_color_andesite", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_BEDROCK = registerBlockWithBlockItem("station_color_bedrock", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_BIRCH_WOOD = registerBlockWithBlockItem("station_color_birch_wood", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_BONE_BLOCK = registerBlockWithBlockItem("station_color_bone_block", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_CHISELED_QUARTZ_BLOCK = registerBlockWithBlockItem("station_color_chiseled_quartz_block", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_CHISELED_STONE_BRICKS = registerBlockWithBlockItem("station_color_chiseled_stone_bricks", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_CLAY = registerBlockWithBlockItem("station_color_clay", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_COAL_ORE = registerBlockWithBlockItem("station_color_coal_ore", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_COBBLESTONE = registerBlockWithBlockItem("station_color_cobblestone", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_CONCRETE = registerBlockWithBlockItem("station_color_concrete", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_CONCRETE_POWDER = registerBlockWithBlockItem("station_color_concrete_powder", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_CRACKED_STONE_BRICKS = registerBlockWithBlockItem("station_color_cracked_stone_bricks", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_DARK_PRISMARINE = registerBlockWithBlockItem("station_color_dark_prismarine", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_DIORITE = registerBlockWithBlockItem("station_color_diorite", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_GRAVEL = registerBlockWithBlockItem("station_color_gravel", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_IRON_BLOCK = registerBlockWithBlockItem("station_color_iron_block", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_METAL = registerBlockWithBlockItem("station_color_metal", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_MOSAIC_TILE = registerBlockWithBlockItem("station_color_mosaic_tile", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_PLANKS = registerBlockWithBlockItem("station_color_planks", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_POLISHED_ANDESITE = registerBlockWithBlockItem("station_color_polished_andesite", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_POLISHED_DIORITE = registerBlockWithBlockItem("station_color_polished_diorite", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_PURPUR_BLOCK = registerBlockWithBlockItem("station_color_purpur_block", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_PURPUR_PILLAR = registerBlockWithBlockItem("station_color_purpur_pillar", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_QUARTZ_BLOCK = registerBlockWithBlockItem("station_color_quartz_block", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_QUARTZ_BRICKS = registerBlockWithBlockItem("station_color_quartz_bricks", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_QUARTZ_PILLAR = registerBlockWithBlockItem("station_color_quartz_pillar", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_SMOOTH_QUARTZ = registerBlockWithBlockItem("station_color_smooth_quartz", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_SMOOTH_STONE = registerBlockWithBlockItem("station_color_smooth_stone", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_SNOW_BLOCK = registerBlockWithBlockItem("station_color_snow_block", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_STAINED_GLASS = registerBlockWithBlockItem("station_color_stained_glass", BlockStationColorGlass::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_STONE = registerBlockWithBlockItem("station_color_stone", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_STONE_BRICKS = registerBlockWithBlockItem("station_color_stone_bricks", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_WOOL = registerBlockWithBlockItem("station_color_wool", BlockStationColor::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);

	// Station slabs
	public static final ObjectHolder<Block> STATION_COLOR_ANDESITE_SLAB = registerBlockWithBlockItem("station_color_andesite_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_BEDROCK_SLAB = registerBlockWithBlockItem("station_color_bedrock_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_BIRCH_WOOD_SLAB = registerBlockWithBlockItem("station_color_birch_wood_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_BONE_BLOCK_SLAB = registerBlockWithBlockItem("station_color_bone_block_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_CHISELED_QUARTZ_BLOCK_SLAB = registerBlockWithBlockItem("station_color_chiseled_quartz_block_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_CHISELED_STONE_BRICKS_SLAB = registerBlockWithBlockItem("station_color_chiseled_stone_bricks_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_CLAY_SLAB = registerBlockWithBlockItem("station_color_clay_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_COAL_ORE_SLAB = registerBlockWithBlockItem("station_color_coal_ore_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_COBBLESTONE_SLAB = registerBlockWithBlockItem("station_color_cobblestone_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_CONCRETE_SLAB = registerBlockWithBlockItem("station_color_concrete_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_CONCRETE_POWDER_SLAB = registerBlockWithBlockItem("station_color_concrete_powder_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_CRACKED_STONE_BRICKS_SLAB = registerBlockWithBlockItem("station_color_cracked_stone_bricks_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_DARK_PRISMARINE_SLAB = registerBlockWithBlockItem("station_color_dark_prismarine_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_DIORITE_SLAB = registerBlockWithBlockItem("station_color_diorite_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_GRAVEL_SLAB = registerBlockWithBlockItem("station_color_gravel_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_IRON_BLOCK_SLAB = registerBlockWithBlockItem("station_color_iron_block_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_METAL_SLAB = registerBlockWithBlockItem("station_color_metal_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_MOSAIC_TILE_SLAB = registerBlockWithBlockItem("station_color_mosaic_tile_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_PLANKS_SLAB = registerBlockWithBlockItem("station_color_planks_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_POLISHED_ANDESITE_SLAB = registerBlockWithBlockItem("station_color_polished_andesite_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_POLISHED_DIORITE_SLAB = registerBlockWithBlockItem("station_color_polished_diorite_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_PURPUR_BLOCK_SLAB = registerBlockWithBlockItem("station_color_purpur_block_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_PURPUR_PILLAR_SLAB = registerBlockWithBlockItem("station_color_purpur_pillar_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_QUARTZ_BLOCK_SLAB = registerBlockWithBlockItem("station_color_quartz_block_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_QUARTZ_BRICKS_SLAB = registerBlockWithBlockItem("station_color_quartz_bricks_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_QUARTZ_PILLAR_SLAB = registerBlockWithBlockItem("station_color_quartz_pillar_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_SMOOTH_QUARTZ_SLAB = registerBlockWithBlockItem("station_color_smooth_quartz_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_SMOOTH_STONE_SLAB = registerBlockWithBlockItem("station_color_smooth_stone_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_SNOW_BLOCK_SLAB = registerBlockWithBlockItem("station_color_snow_block_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_STAINED_GLASS_SLAB = registerBlockWithBlockItem("station_color_stained_glass_slab", BlockStationColorGlassSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_STONE_SLAB = registerBlockWithBlockItem("station_color_stone_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_STONE_BRICKS_SLAB = registerBlockWithBlockItem("station_color_stone_bricks_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> STATION_COLOR_WOOL_SLAB = registerBlockWithBlockItem("station_color_wool_slab", BlockStationColorSlab::new, false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);

	// Station misc
	public static final ObjectHolder<Block> STATION_COLOR_POLE = registerBlockWithBlockItem("station_pole", settings -> new BlockStationColorPole(settings, true), false, ItemBlockEnchanted::new, ItemGroups.STATION_BUILDING_BLOCKS);

	// machines
	public static final ObjectHolder<Block> TICKET_BARRIER_ENTRANCE_1 = registerBlockWithBlockItem("ticket_barrier_entrance_1", settings -> new BlockTicketBarrier(settings, true), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> TICKET_BARRIER_EXIT_1 = registerBlockWithBlockItem("ticket_barrier_exit_1", settings -> new BlockTicketBarrier(settings, false), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> TICKET_MACHINE = registerBlockWithBlockItem("ticket_machine", BlockTicketMachine::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> TICKET_PROCESSOR = registerBlockWithBlockItem("ticket_processor", settings -> new BlockTicketProcessor(settings, true, true, true), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> TICKET_PROCESSOR_ENTRANCE = registerBlockWithBlockItem("ticket_processor_entrance", settings -> new BlockTicketProcessor(settings, true, true, false), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> TICKET_PROCESSOR_EXIT = registerBlockWithBlockItem("ticket_processor_exit", settings -> new BlockTicketProcessor(settings, true, false, true), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> TICKET_PROCESSOR_ENQUIRY = registerBlockWithBlockItem("ticket_processor_enquiry", BlockTicketProcessorEnquiry::new, true, ItemGroups.RAILWAY_FACILITIES);

	// Sensors
	public static final ObjectHolder<Block> TRAIN_ANNOUNCER = registerBlockWithBlockItem("train_announcer", BlockTrainAnnouncer::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> TRAIN_CARGO_LOADER = registerBlockWithBlockItem("train_cargo_loader", BlockTrainCargoLoader::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> TRAIN_CARGO_UNLOADER = registerBlockWithBlockItem("train_cargo_unloader", BlockTrainCargoUnloader::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> TRAIN_REDSTONE_SENSOR = registerBlockWithBlockItem("train_sensor", BlockTrainRedstoneSensor::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> TRAIN_SCHEDULE_SENSOR = registerBlockWithBlockItem("train_schedule_sensor", BlockTrainScheduleSensor::new, true, ItemGroups.RAILWAY_FACILITIES);

	// Misc
	public static final ObjectHolder<Block> CEILING = registerBlockWithBlockItem("ceiling", settings -> new BlockCeilingAuto(settings.luminance(blockState -> 15)), false, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> CEILING_LIGHT = registerBlockWithBlockItem("ceiling_light", settings -> new BlockCeiling(settings.luminance(blockState -> 15)), false, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> CEILING_NO_LIGHT = registerBlockWithBlockItem("ceiling_no_light", BlockCeiling::new, false, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> CLOCK = registerBlockWithBlockItem("clock", settings -> new BlockClock(settings.luminance(blockState -> 5)), true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> CLOCK_POLE = registerBlockWithBlockItem("clock_pole", BlockClockPole::new, false, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> GLASS_FENCE_CIO = registerBlockWithBlockItem("glass_fence_cio", BlockGlassFence::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> GLASS_FENCE_CKT = registerBlockWithBlockItem("glass_fence_ckt", BlockGlassFence::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> GLASS_FENCE_HEO = registerBlockWithBlockItem("glass_fence_heo", BlockGlassFence::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> GLASS_FENCE_MOS = registerBlockWithBlockItem("glass_fence_mos", BlockGlassFence::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> GLASS_FENCE_PLAIN = registerBlockWithBlockItem("glass_fence_plain", BlockGlassFence::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> GLASS_FENCE_SHM = registerBlockWithBlockItem("glass_fence_shm", BlockGlassFence::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> GLASS_FENCE_STAINED = registerBlockWithBlockItem("glass_fence_stained", BlockGlassFence::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> GLASS_FENCE_STW = registerBlockWithBlockItem("glass_fence_stw", BlockGlassFence::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> GLASS_FENCE_TSH = registerBlockWithBlockItem("glass_fence_tsh", BlockGlassFence::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> GLASS_FENCE_WKS = registerBlockWithBlockItem("glass_fence_wks", BlockGlassFence::new, true, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> LOGO = registerBlockWithBlockItem("logo", settings -> new Block(settings.luminance(blockState -> 10)), false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> MARBLE_BLUE = registerBlockWithBlockItem("marble_blue", Block::new, false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> MARBLE_BLUE_SLAB = registerBlockWithBlockItem("marble_blue_slab", SlabBlock::new, false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> MARBLE_SANDY = registerBlockWithBlockItem("marble_sandy", Block::new, false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> MARBLE_SANDY_SLAB = registerBlockWithBlockItem("marble_sandy_slab", SlabBlock::new, false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> METAL = registerBlockWithBlockItem("metal", Block::new, false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> METAL_SLAB = registerBlockWithBlockItem("metal_slab", SlabBlock::new, false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> MOSAIC_TILE = registerBlockWithBlockItem("mosaic_tile", Block::new, false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> MOSAIC_TILE_SLAB = registerBlockWithBlockItem("mosaic_tile_slab", SlabBlock::new, false, ItemGroups.STATION_BUILDING_BLOCKS);
	public static final ObjectHolder<Block> RUBBISH_BIN_1 = registerBlockWithBlockItem("rubbish_bin_1", BlockRubbishBin::new, false, ItemGroups.RAILWAY_FACILITIES);
	public static final ObjectHolder<Block> TACTILE_MAP = registerBlockWithBlockItem("tactile_map", BlockTactileMap::new, false, ItemGroups.RAILWAY_FACILITIES);

	// NTE
	public static final ObjectHolder<Block> EYE_CANDY = registerBlockWithBlockItem("eye_candy", BlockEyeCandy::new, true, ItemGroups.STATION_BUILDING_BLOCKS);

	public static void init() {
		MTR.LOGGER.info("Registering Minecraft Transit Railway blocks");
		if (Keys.DEBUG) {
			try {
				// Validate all registered blocks are in pickaxe.json
				final ObjectAVLTreeSet<String> expectedIdentifiers = new ObjectAVLTreeSet<>();
				JsonParser.parseString(FileUtils.readFileToString(
						MinecraftClient.getInstance().runDirectory.toPath().resolve("../src/main/resources/data/minecraft/tags/block/mineable/pickaxe.json").toFile(),
						StandardCharsets.UTF_8
				)).getAsJsonObject().getAsJsonArray("values").forEach(identifier -> expectedIdentifiers.add(identifier.getAsString()));
				REGISTERED_IDENTIFIERS.forEach(identifier -> {
					if (!expectedIdentifiers.contains(identifier)) {
						MTR.LOGGER.warn("Identifier missing in pickaxe.json! [{}]", identifier);
					}
				});
				expectedIdentifiers.forEach(identifier -> {
					if (!REGISTERED_IDENTIFIERS.contains(identifier)) {
						MTR.LOGGER.warn("Extra identifier in pickaxe.json! [{}]", identifier);
					}
				});

				// Validate json file exists in loot_table/block
				final ObjectAVLTreeSet<String> expectedFiles = new ObjectAVLTreeSet<>();
				try (final Stream<Path> stream = Files.list(MinecraftClient.getInstance().runDirectory.toPath().resolve("../src/main/resources/data/mtr/loot_table/block"))) {
					stream.forEach(path -> expectedFiles.add("mtr:" + path.getFileName().toString().split(".json")[0]));
				}
				REGISTERED_IDENTIFIERS.forEach(identifier -> {
					if (!expectedFiles.contains(identifier)) {
						MTR.LOGGER.warn("Loot table file missing! [{}]", identifier);
					}
				});
				expectedFiles.forEach(identifier -> {
					if (!REGISTERED_IDENTIFIERS.contains(identifier)) {
						MTR.LOGGER.warn("Extra loot table file! [{}]", identifier);
					}
				});
			} catch (Exception e) {
				MTR.LOGGER.error("", e);
			}
		}
	}

	private static AbstractBlock.Settings createDefaultBlockSettings(AbstractBlock.Settings settings, boolean blockPiston) {
		return settings.pistonBehavior(blockPiston ? PistonBehavior.BLOCK : PistonBehavior.NORMAL).strength(3);
	}

	private static ObjectHolder<Block> registerBlock(String registryName, Function<AbstractBlock.Settings, Block> factory) {
		REGISTERED_IDENTIFIERS.add(registryName);
		return Registry.registerBlock(registryName, settings -> factory.apply(createDefaultBlockSettings(settings, true)));
	}

	private static ObjectHolder<Block> registerBlockWithBlockItem(String registryName, Function<AbstractBlock.Settings, Block> factory, boolean blockPiston, String itemGroupRegistryName) {
		return registerBlockWithBlockItem(registryName, factory, blockPiston, BlockItem::new, itemGroupRegistryName);
	}

	private static ObjectHolder<Block> registerBlockWithBlockItem(String registryName, Function<AbstractBlock.Settings, Block> blockFactory, boolean blockPiston, BiFunction<Block, Item.Settings, BlockItem> blockItemFactory, String itemGroupRegistryName) {
		REGISTERED_IDENTIFIERS.add(registryName);
		final ObjectHolder<Block> objectHolder = Registry.registerBlock(registryName, settings -> blockFactory.apply(createDefaultBlockSettings(settings, blockPiston)));
		Registry.registerItem(registryName, settings -> blockItemFactory.apply(objectHolder.createAndGet(), settings), itemGroupRegistryName);
		return objectHolder;
	}
}
