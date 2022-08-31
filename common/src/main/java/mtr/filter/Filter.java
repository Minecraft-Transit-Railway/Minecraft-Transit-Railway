package mtr.filter;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import mtr.Blocks;
import mtr.ItemGroups;
import mtr.Items;
import mtr.mappings.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class Filter extends Button
{
    /* CORE */
    public static final Filter CORE_DASHBOARDS = new Filter(Text.translatable("filter.mtr.core_dashboards"), Items.RAILWAY_DASHBOARD.get().getDefaultInstance(),
            Arrays.asList(
                    Items.RAILWAY_DASHBOARD.get().asItem().getDefaultInstance(),
                    Items.CABLE_CAR_DASHBOARD.get().asItem().getDefaultInstance(),
                    Items.BOAT_DASHBOARD.get().asItem().getDefaultInstance()
            ));
    public static final Filter CORE_RAILS = new Filter(Text.translatable("filter.mtr.core_rails"), Items.RAIL_CONNECTOR_PLATFORM.get().getDefaultInstance(),
            Arrays.asList(
                    Items.RAIL_CONNECTOR_20.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_20_ONE_WAY.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_40.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_40_ONE_WAY.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_60.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_60_ONE_WAY.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_80.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_80_ONE_WAY.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_120.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_120_ONE_WAY.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_160.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_160_ONE_WAY.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_200.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_200_ONE_WAY.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_300.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_300_ONE_WAY.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_PLATFORM.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_SIDING.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_TURN_BACK.get().getDefaultInstance(),
                    Items.RAIL_CONNECTOR_CABLE_CAR.get().getDefaultInstance(),
                    Items.RAIL_REMOVER.get().getDefaultInstance()
            ));
    public static final Filter CORE_SIGNALS = new Filter(Text.translatable("filter.mtr.core_signals"), Items.SIGNAL_CONNECTOR_WHITE.get().getDefaultInstance(),
            Arrays.asList(
                    Items.SIGNAL_CONNECTOR_WHITE.get().getDefaultInstance(),
                    Items.SIGNAL_CONNECTOR_ORANGE.get().getDefaultInstance(),
                    Items.SIGNAL_CONNECTOR_MAGENTA.get().getDefaultInstance(),
                    Items.SIGNAL_CONNECTOR_LIGHT_BLUE.get().getDefaultInstance(),
                    Items.SIGNAL_CONNECTOR_YELLOW.get().getDefaultInstance(),
                    Items.SIGNAL_CONNECTOR_GREEN.get().getDefaultInstance(),
                    Items.SIGNAL_CONNECTOR_PINK.get().getDefaultInstance(),
                    Items.SIGNAL_CONNECTOR_GRAY.get().getDefaultInstance(),
                    Items.SIGNAL_CONNECTOR_LIGHT_GRAY.get().getDefaultInstance(),
                    Items.SIGNAL_CONNECTOR_CYAN.get().getDefaultInstance(),
                    Items.SIGNAL_CONNECTOR_PURPLE.get().getDefaultInstance(),
                    Items.SIGNAL_CONNECTOR_BLUE.get().getDefaultInstance(),
                    Items.SIGNAL_CONNECTOR_BROWN.get().getDefaultInstance(),
                    Items.SIGNAL_CONNECTOR_LIME.get().getDefaultInstance(),
                    Items.SIGNAL_CONNECTOR_RED.get().getDefaultInstance(),
                    Items.SIGNAL_CONNECTOR_BLACK.get().getDefaultInstance(),
                    Items.SIGNAL_REMOVER_WHITE.get().getDefaultInstance(),
                    Items.SIGNAL_REMOVER_ORANGE.get().getDefaultInstance(),
                    Items.SIGNAL_REMOVER_MAGENTA.get().getDefaultInstance(),
                    Items.SIGNAL_REMOVER_LIGHT_BLUE.get().getDefaultInstance(),
                    Items.SIGNAL_REMOVER_YELLOW.get().getDefaultInstance(),
                    Items.SIGNAL_REMOVER_GREEN.get().getDefaultInstance(),
                    Items.SIGNAL_REMOVER_PINK.get().getDefaultInstance(),
                    Items.SIGNAL_REMOVER_GRAY.get().getDefaultInstance(),
                    Items.SIGNAL_REMOVER_LIGHT_GRAY.get().getDefaultInstance(),
                    Items.SIGNAL_REMOVER_CYAN.get().getDefaultInstance(),
                    Items.SIGNAL_REMOVER_PURPLE.get().getDefaultInstance(),
                    Items.SIGNAL_REMOVER_BLUE.get().getDefaultInstance(),
                    Items.SIGNAL_REMOVER_BROWN.get().getDefaultInstance(),
                    Items.SIGNAL_REMOVER_LIME.get().getDefaultInstance(),
                    Items.SIGNAL_REMOVER_RED.get().getDefaultInstance(),
                    Items.SIGNAL_REMOVER_BLACK.get().getDefaultInstance()
            ));
    public static final Filter CORE_CREATORS = new Filter(Text.translatable("filter.mtr.core_creators"), Items.TUNNEL_CREATOR_4_3.get().getDefaultInstance(),
            Arrays.asList(
                    Items.BRIDGE_CREATOR_3.get().getDefaultInstance(),
                    Items.BRIDGE_CREATOR_5.get().getDefaultInstance(),
                    Items.BRIDGE_CREATOR_7.get().getDefaultInstance(),
                    Items.BRIDGE_CREATOR_9.get().getDefaultInstance(),
                    Items.TUNNEL_CREATOR_4_3.get().getDefaultInstance(),
                    Items.TUNNEL_CREATOR_4_5.get().getDefaultInstance(),
                    Items.TUNNEL_CREATOR_4_7.get().getDefaultInstance(),
                    Items.TUNNEL_CREATOR_4_9.get().getDefaultInstance(),
                    Items.TUNNEL_CREATOR_5_3.get().getDefaultInstance(),
                    Items.TUNNEL_CREATOR_5_5.get().getDefaultInstance(),
                    Items.TUNNEL_CREATOR_5_7.get().getDefaultInstance(),
                    Items.TUNNEL_CREATOR_5_9.get().getDefaultInstance(),
                    Items.TUNNEL_CREATOR_6_3.get().getDefaultInstance(),
                    Items.TUNNEL_CREATOR_6_5.get().getDefaultInstance(),
                    Items.TUNNEL_CREATOR_6_7.get().getDefaultInstance(),
                    Items.TUNNEL_CREATOR_6_9.get().getDefaultInstance(),
                    Items.TUNNEL_WALL_CREATOR_4_3.get().getDefaultInstance(),
                    Items.TUNNEL_WALL_CREATOR_4_5.get().getDefaultInstance(),
                    Items.TUNNEL_WALL_CREATOR_4_7.get().getDefaultInstance(),
                    Items.TUNNEL_WALL_CREATOR_4_9.get().getDefaultInstance(),
                    Items.TUNNEL_WALL_CREATOR_5_3.get().getDefaultInstance(),
                    Items.TUNNEL_WALL_CREATOR_5_5.get().getDefaultInstance(),
                    Items.TUNNEL_WALL_CREATOR_5_7.get().getDefaultInstance(),
                    Items.TUNNEL_WALL_CREATOR_5_9.get().getDefaultInstance(),
                    Items.TUNNEL_WALL_CREATOR_6_3.get().getDefaultInstance(),
                    Items.TUNNEL_WALL_CREATOR_6_5.get().getDefaultInstance(),
                    Items.TUNNEL_WALL_CREATOR_6_7.get().getDefaultInstance(),
                    Items.TUNNEL_WALL_CREATOR_6_9.get().getDefaultInstance()
            ));
    public static final Filter CORE_NODES = new Filter(Text.translatable("filter.mtr.core_nodes"), Blocks.RAIL_NODE.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    Blocks.RAIL_NODE.get().asItem().getDefaultInstance(),
                    Blocks.BOAT_NODE.get().asItem().getDefaultInstance(),
                    Blocks.CABLE_CAR_NODE_LOWER.get().asItem().getDefaultInstance(),
                    Blocks.CABLE_CAR_NODE_STATION.get().asItem().getDefaultInstance(),
                    Blocks.CABLE_CAR_NODE_UPPER.get().asItem().getDefaultInstance()
            ));
    public static final Filter CORE_MISC = new Filter(Text.translatable("filter.mtr.core_misc"), Items.BRUSH.get().getDefaultInstance(),
            Arrays.asList(
                    Items.BRUSH.get().getDefaultInstance(),
                    Items.DRIVER_KEY.get().getDefaultInstance(),
                    Items.RESOURCE_PACK_CREATOR.get().getDefaultInstance()
            ));

    /* FACILITIES */
    public static final Filter FACILITIES_GATES = new Filter(Text.translatable("filter.mtr.facilities_gates"), Blocks.APG_DOOR.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    Blocks.APG_DOOR.get().asItem().getDefaultInstance(),
                    Blocks.APG_GLASS.get().asItem().getDefaultInstance(),
                    Blocks.APG_GLASS_END.get().asItem().getDefaultInstance(),
                    Blocks.PSD_DOOR_1.get().asItem().getDefaultInstance(),
                    Blocks.PSD_DOOR_2.get().asItem().getDefaultInstance(),
                    Blocks.PSD_GLASS_1.get().asItem().getDefaultInstance(),
                    Blocks.PSD_GLASS_2.get().asItem().getDefaultInstance(),
                    Blocks.PSD_GLASS_END_1.get().asItem().getDefaultInstance(),
                    Blocks.PSD_GLASS_END_2.get().asItem().getDefaultInstance()
            ));
    public static final Filter FACILITIES_PIDS = new Filter(Text.translatable("filter.mtr.facilities_pids"), Blocks.PIDS_1.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    Blocks.ARRIVAL_PROJECTOR_1_SMALL.get().asItem().getDefaultInstance(),
                    Blocks.ARRIVAL_PROJECTOR_1_MEDIUM.get().asItem().getDefaultInstance(),
                    Blocks.ARRIVAL_PROJECTOR_1_LARGE.get().asItem().getDefaultInstance(),
                    Blocks.PIDS_1.get().asItem().getDefaultInstance(),
                    Blocks.PIDS_2.get().asItem().getDefaultInstance(),
                    Blocks.PIDS_3.get().asItem().getDefaultInstance(),
                    Blocks.PIDS_POLE.get().asItem().getDefaultInstance()
            ));
    public static final Filter FACILITIES_CEILINGS = new Filter(Text.translatable("filter.mtr.facilities_ceilings"), Blocks.CEILING.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    Blocks.CEILING.get().asItem().getDefaultInstance(),
                    Blocks.CEILING_LIGHT.get().asItem().getDefaultInstance(),
                    Blocks.CEILING_NO_LIGHT.get().asItem().getDefaultInstance()
            ));
    public static final Filter FACILITIES_MISC = new Filter(Text.translatable("filter.mtr.facilities_misc"), Blocks.CLOCK.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    Blocks.CLOCK.get().asItem().getDefaultInstance(),
                    Blocks.CLOCK_POLE.get().asItem().getDefaultInstance(),
                    Blocks.RUBBISH_BIN_1.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_POLE.get().asItem().getDefaultInstance(),
                    Blocks.TACTILE_MAP.get().asItem().getDefaultInstance(),
                    Blocks.TRAIN_ANNOUNCER.get().asItem().getDefaultInstance(),
                    Blocks.TRAIN_CARGO_LOADER.get().asItem().getDefaultInstance(),
                    Blocks.TRAIN_CARGO_UNLOADER.get().asItem().getDefaultInstance(),
                    Blocks.TRAIN_REDSTONE_SENSOR.get().asItem().getDefaultInstance(),
                    Blocks.TRAIN_SCHEDULE_SENSOR.get().asItem().getDefaultInstance()
            ));
    public static final Filter FACILITIES_FENCES = new Filter(Text.translatable("filter.mtr.facilities_fences"), Blocks.GLASS_FENCE_CIO.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    Blocks.GLASS_FENCE_CIO.get().asItem().getDefaultInstance(),
                    Blocks.GLASS_FENCE_CKT.get().asItem().getDefaultInstance(),
                    Blocks.GLASS_FENCE_HEO.get().asItem().getDefaultInstance(),
                    Blocks.GLASS_FENCE_MOS.get().asItem().getDefaultInstance(),
                    Blocks.GLASS_FENCE_PLAIN.get().asItem().getDefaultInstance(),
                    Blocks.GLASS_FENCE_SHM.get().asItem().getDefaultInstance(),
                    Blocks.GLASS_FENCE_STAINED.get().asItem().getDefaultInstance(),
                    Blocks.GLASS_FENCE_STW.get().asItem().getDefaultInstance(),
                    Blocks.GLASS_FENCE_TSH.get().asItem().getDefaultInstance(),
                    Blocks.GLASS_FENCE_WKS.get().asItem().getDefaultInstance()
            ));
    public static final Filter FACILITIES_RAILWAY_SIGNS = new Filter(Text.translatable("filter.mtr.facilities_railway_signs"), Blocks.RAILWAY_SIGN_2_EVEN.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    Blocks.RAILWAY_SIGN_2_EVEN.get().asItem().getDefaultInstance(),
                    Blocks.RAILWAY_SIGN_2_ODD.get().asItem().getDefaultInstance(),
                    Blocks.RAILWAY_SIGN_3_EVEN.get().asItem().getDefaultInstance(),
                    Blocks.RAILWAY_SIGN_3_ODD.get().asItem().getDefaultInstance(),
                    Blocks.RAILWAY_SIGN_4_EVEN.get().asItem().getDefaultInstance(),
                    Blocks.RAILWAY_SIGN_4_ODD.get().asItem().getDefaultInstance(),
                    Blocks.RAILWAY_SIGN_5_EVEN.get().asItem().getDefaultInstance(),
                    Blocks.RAILWAY_SIGN_5_ODD.get().asItem().getDefaultInstance(),
                    Blocks.RAILWAY_SIGN_6_EVEN.get().asItem().getDefaultInstance(),
                    Blocks.RAILWAY_SIGN_6_ODD.get().asItem().getDefaultInstance(),
                    Blocks.RAILWAY_SIGN_7_EVEN.get().asItem().getDefaultInstance(),
                    Blocks.RAILWAY_SIGN_7_ODD.get().asItem().getDefaultInstance(),
                    Blocks.RAILWAY_SIGN_POLE.get().asItem().getDefaultInstance()
            ));
    public static final Filter FACILITIES_ROUTE_SIGNS = new Filter(Text.translatable("filter.mtr.facilities_route_signs"), Blocks.ROUTE_SIGN_STANDING_LIGHT.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    Blocks.ROUTE_SIGN_STANDING_LIGHT.get().asItem().getDefaultInstance(),
                    Blocks.ROUTE_SIGN_STANDING_METAL.get().asItem().getDefaultInstance(),
                    Blocks.ROUTE_SIGN_WALL_LIGHT.get().asItem().getDefaultInstance(),
                    Blocks.ROUTE_SIGN_WALL_METAL.get().asItem().getDefaultInstance()
            ));
    public static final Filter FACILITIES_SIGNAL_LIGHTS = new Filter(Text.translatable("filter.mtr.facilities_signal_lights"), Blocks.SIGNAL_LIGHT_1.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    Blocks.SIGNAL_LIGHT_1.get().asItem().getDefaultInstance(),
                    Blocks.SIGNAL_LIGHT_2.get().asItem().getDefaultInstance(),
                    Blocks.SIGNAL_LIGHT_3.get().asItem().getDefaultInstance(),
                    Blocks.SIGNAL_LIGHT_4.get().asItem().getDefaultInstance(),
                    Blocks.SIGNAL_SEMAPHORE_1.get().asItem().getDefaultInstance(),
                    Blocks.SIGNAL_SEMAPHORE_2.get().asItem().getDefaultInstance(),
                    Blocks.SIGNAL_POLE.get().asItem().getDefaultInstance()
            ));
    public static final Filter FACILITIES_STATION_NAME_SIGNS = new Filter(Text.translatable("filter.mtr.facilities_station_name_signs"), Blocks.STATION_NAME_WALL_WHITE.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    Blocks.STATION_NAME_ENTRANCE.get().asItem().getDefaultInstance(),
                    Blocks.STATION_NAME_WALL_WHITE.get().asItem().getDefaultInstance(),
                    Blocks.STATION_NAME_WALL_BLACK.get().asItem().getDefaultInstance(),
                    Blocks.STATION_NAME_WALL_GRAY.get().asItem().getDefaultInstance(),
                    Blocks.STATION_NAME_TALL_BLOCK.get().asItem().getDefaultInstance(),
                    Blocks.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED.get().asItem().getDefaultInstance(),
                    Blocks.STATION_NAME_TALL_WALL.get().asItem().getDefaultInstance()
            ));
    public static final Filter FACILITIES_TICKETS = new Filter(Text.translatable("filter.mtr.facilities_tickets"), Blocks.TICKET_MACHINE.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    Blocks.TICKET_BARRIER_ENTRANCE_1.get().asItem().getDefaultInstance(),
                    Blocks.TICKET_BARRIER_EXIT_1.get().asItem().getDefaultInstance(),
                    Blocks.TICKET_MACHINE.get().asItem().getDefaultInstance(),
                    Blocks.TICKET_PROCESSOR.get().asItem().getDefaultInstance(),
                    Blocks.TICKET_PROCESSOR_ENQUIRY.get().asItem().getDefaultInstance(),
                    Blocks.TICKET_PROCESSOR_ENTRANCE.get().asItem().getDefaultInstance(),
                    Blocks.TICKET_PROCESSOR_EXIT.get().asItem().getDefaultInstance(),
                    Blocks.TICKET_PROCESSOR_ENQUIRY.get().asItem().getDefaultInstance()
            ));

    /* BUILDINGS */
    public static final Filter BUILDINGS_MISC = new Filter(Text.translatable("filter.mtr.buildings_misc"), Blocks.LOGO.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    Blocks.LOGO.get().asItem().getDefaultInstance()
            ));
    public static final Filter BUILDINGS_MARBLES = new Filter(Text.translatable("filter.mtr.buildings_marbles"), Blocks.MARBLE_BLUE.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    Blocks.MARBLE_BLUE.get().asItem().getDefaultInstance(),
                    Blocks.MARBLE_BLUE_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.MARBLE_SANDY.get().asItem().getDefaultInstance(),
                    Blocks.MARBLE_SANDY_SLAB.get().asItem().getDefaultInstance()
            ));
    public static final Filter BUILDINGS_PLATFORMS = new Filter(Text.translatable("filter.mtr.buildings_platforms"), Blocks.PLATFORM.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    Blocks.PLATFORM.get().asItem().getDefaultInstance(),
                    Blocks.PLATFORM_INDENTED.get().asItem().getDefaultInstance(),
                    Blocks.PLATFORM_NA_1.get().asItem().getDefaultInstance(),
                    Blocks.PLATFORM_NA_2.get().asItem().getDefaultInstance(),
                    Blocks.PLATFORM_NA_1_INDENTED.get().asItem().getDefaultInstance(),
                    Blocks.PLATFORM_NA_2_INDENTED.get().asItem().getDefaultInstance(),
                    Blocks.PLATFORM_UK_1.get().asItem().getDefaultInstance(),
                    Blocks.PLATFORM_UK_1_INDENTED.get().asItem().getDefaultInstance()
            ));
    public static final Filter BUILDINGS_STATION_COLOR_BLOCKS = new Filter(Text.translatable("filter.mtr.buildings_station_color_blocks"), Blocks.STATION_COLOR_QUARTZ_BLOCK.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    Blocks.STATION_COLOR_ANDESITE.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_BEDROCK.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_BIRCH_WOOD.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_BONE_BLOCK.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_CHISELED_STONE_BRICKS.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_CLAY.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_COAL_ORE.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_COBBLESTONE.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_CONCRETE.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_CONCRETE_POWDER.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_CRACKED_STONE_BRICKS.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_DARK_PRISMARINE.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_DIORITE.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_GRAVEL.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_IRON_BLOCK.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_METAL.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_PLANKS.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_POLISHED_ANDESITE.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_POLISHED_DIORITE.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_PURPUR_BLOCK.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_PURPUR_PILLAR.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_QUARTZ_BLOCK.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_QUARTZ_BRICKS.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_QUARTZ_PILLAR.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_SMOOTH_QUARTZ.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_SMOOTH_STONE.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_SNOW_BLOCK.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_STAINED_GLASS.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_STONE.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_STONE_BRICKS.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_WOOL.get().asItem().getDefaultInstance()
            ));
    public static final Filter BUILDINGS_STATION_COLOR_SLABS = new Filter(Text.translatable("filter.mtr.buildings_station_color_slabs"), Blocks.STATION_COLOR_QUARTZ_BLOCK_SLAB.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    Blocks.STATION_COLOR_ANDESITE_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_BEDROCK_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_BIRCH_WOOD_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_BONE_BLOCK_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_CHISELED_STONE_BRICKS_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_CLAY_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_COAL_ORE_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_COBBLESTONE_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_CONCRETE_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_CONCRETE_POWDER_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_CRACKED_STONE_BRICKS_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_DARK_PRISMARINE_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_DIORITE_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_GRAVEL_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_IRON_BLOCK_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_METAL_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_PLANKS_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_POLISHED_ANDESITE_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_POLISHED_DIORITE_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_PURPUR_BLOCK_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_PURPUR_PILLAR_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_QUARTZ_BLOCK_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_QUARTZ_BRICKS_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_QUARTZ_PILLAR_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_SMOOTH_QUARTZ_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_SMOOTH_STONE_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_SNOW_BLOCK_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_STAINED_GLASS_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_STONE_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_STONE_BRICKS_SLAB.get().asItem().getDefaultInstance(),
                    Blocks.STATION_COLOR_WOOL_SLAB.get().asItem().getDefaultInstance()
            ));

    /* LIFTS */
    public static final Filter LIFTS_ESCALATORS = new Filter(Text.translatable("filter.mtr.lifts_escalators"), Items.ESCALATOR.get().getDefaultInstance(),
            Arrays.asList(
                    Items.ESCALATOR.get().getDefaultInstance()
            ));
    public static final Filter LIFTS_LIFTS = new Filter(Text.translatable("filter.mtr.lifts_lifts"), Items.LIFT_2_2.get().getDefaultInstance(),
            Arrays.asList(
                    Items.LIFT_2_2.get().getDefaultInstance(),
                    Items.LIFT_2_2_DOUBLE_SIDED.get().getDefaultInstance(),
                    Items.LIFT_3_2.get().getDefaultInstance(),
                    Items.LIFT_3_2_DOUBLE_SIDED.get().getDefaultInstance(),
                    Items.LIFT_3_3.get().getDefaultInstance(),
                    Items.LIFT_3_3_DOUBLE_SIDED.get().getDefaultInstance(),
                    Items.LIFT_4_3.get().getDefaultInstance(),
                    Items.LIFT_4_3_DOUBLE_SIDED.get().getDefaultInstance(),
                    Items.LIFT_4_4.get().getDefaultInstance(),
                    Items.LIFT_4_4_DOUBLE_SIDED.get().getDefaultInstance(),
                    Items.LIFT_BUTTONS_LINK_CONNECTOR.get().getDefaultInstance(),
                    Items.LIFT_BUTTONS_LINK_REMOVER.get().getDefaultInstance(),
                    Items.LIFT_DOOR_1.get().getDefaultInstance(),
                    Items.LIFT_DOOR_ODD_1.get().getDefaultInstance(),
                    Blocks.LIFT_BUTTONS_1.get().asItem().getDefaultInstance(),
                    Blocks.LIFT_TRACK_1.get().asItem().getDefaultInstance(),
                    Blocks.LIFT_TRACK_FLOOR_1.get().asItem().getDefaultInstance()
            ));

    public static final Map<Integer, FilterLinkedList> FILTERS = new HashMap<>();

    public static void init() {
        FILTERS.put(ItemGroups.CORE.getId(), new FilterLinkedList(CORE_DASHBOARDS, CORE_RAILS, CORE_SIGNALS, CORE_CREATORS, CORE_MISC, CORE_NODES));
        FILTERS.put(ItemGroups.RAILWAY_FACILITIES.getId(), new FilterLinkedList(FACILITIES_GATES, FACILITIES_PIDS, FACILITIES_CEILINGS, FACILITIES_MISC, FACILITIES_FENCES, FACILITIES_RAILWAY_SIGNS, FACILITIES_ROUTE_SIGNS, FACILITIES_SIGNAL_LIGHTS, FACILITIES_STATION_NAME_SIGNS, FACILITIES_TICKETS));
        FILTERS.put(ItemGroups.STATION_BUILDING_BLOCKS.getId(), new FilterLinkedList(BUILDINGS_MISC, BUILDINGS_MARBLES, BUILDINGS_PLATFORMS, BUILDINGS_STATION_COLOR_BLOCKS, BUILDINGS_STATION_COLOR_SLABS));
        FILTERS.put(ItemGroups.ESCALATORS_LIFTS.getId(), new FilterLinkedList(LIFTS_ESCALATORS, LIFTS_LIFTS));
    }

    private static final ResourceLocation TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

    public final ItemStack itemStack;
    public final List<ItemStack> filter;
    public boolean enabled = true;
    public OnPress onPress;

    public Filter(Component component, ItemStack itemStack, List<ItemStack> filter) {
        super(0, 0, 32, 28, component, Button::onPress);
        this.itemStack = itemStack;
        this.filter = filter;
    }

    @Override
    public void onPress() {
        onPress.onPress(this);
    }

    @Override
    public void renderButton(PoseStack poseStack, int i, int j, float f) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bind(TABS);

        GlStateManager._blendColor(1f, 1f, 1f, this.alpha);
        GlStateManager._disableLighting();
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value, GlStateManager.SourceFactor.ONE.value, GlStateManager.DestFactor.ZERO.value);
        GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);

        int width = this.enabled ? 32 : 28;
        int textureX = 28;
        int textureY = this.enabled ? 32 : 0;
        this.drawRotatedTexture(poseStack.last().pose(), x, y, textureX, textureY, width, 28);

        RenderSystem.enableRescaleNormal();
        ItemRenderer renderer = mc.getItemRenderer();
        renderer.blitOffset = 100f;
        renderer.renderAndDecorateItem(itemStack, x + 8, y + 6);
        renderer.renderGuiItemDecorations(mc.font, itemStack, x + 8, y + 6);
        renderer.blitOffset = 0f;
    }

    private void drawRotatedTexture(Matrix4f pose, int x, int y, int textureX, int textureY, int width, int i) {
        float scaleX = 0.00390625F;
        float scaleY = 0.00390625F;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(7, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(pose, x, y + height, 0f).uv(((float) (textureX + height) * scaleX), ((float) (textureY) * scaleY)).endVertex();
        bufferBuilder.vertex(pose, x + width, y + height, 0f).uv(((float) (textureX + height) * scaleX), ((float) (textureY + width) * scaleY)).endVertex();
        bufferBuilder.vertex(pose, x + width, y, 0f).uv(((float) (textureX) * scaleX), ((float) (textureY + width) * scaleY)).endVertex();
        bufferBuilder.vertex(pose, x, y, 0f).uv(((float) (textureX) * scaleX), ((float) (textureY) * scaleY)).endVertex();
        tesselator.end();
    }

    public static class FilterLinkedList extends LinkedList<Filter>
    {
        public Button btnScrollUp, btnScrollDown, btnEnableAll, btnDisableAll;
        public int filterIndex = 0;

        public FilterLinkedList(Filter... filters) {
            super(Arrays.asList(filters));
        }
    }
}
