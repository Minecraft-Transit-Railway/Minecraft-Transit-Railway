package mtr;

import mtr.client.ClientData;
import mtr.client.Config;
import mtr.data.Depot;
import mtr.data.Route;
import mtr.data.Station;
import mtr.item.ItemBlockClickingBase;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.RenderLift;
import mtr.render.RenderLiftButtons;
import mtr.render.RenderLiftPanel;
import mtr.render.RenderPSDAPGDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;

public class MTRClientLifts implements IPacket {

	public static void init() {
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.LIFT_DOOR_EVEN_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.LIFT_DOOR_ODD_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.LIFT_PANEL_EVEN_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.LIFT_PANEL_ODD_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.LIFT_PANEL_EVEN_2.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.LIFT_PANEL_ODD_2.get());

		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.LIFT_BUTTONS_LINK_CONNECTOR.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.LIFT_BUTTONS_LINK_REMOVER.get(), ItemBlockClickingBase.TAG_POS);

		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_BUTTONS_1_TILE_ENTITY.get(), RenderLiftButtons::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_PANEL_EVEN_1_TILE_ENTITY.get(), dispatcher -> new RenderLiftPanel<>(dispatcher, false, false));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_PANEL_ODD_1_TILE_ENTITY.get(), dispatcher -> new RenderLiftPanel<>(dispatcher, true, false));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_PANEL_EVEN_2_TILE_ENTITY.get(), dispatcher -> new RenderLiftPanel<>(dispatcher, false, true));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_PANEL_ODD_2_TILE_ENTITY.get(), dispatcher -> new RenderLiftPanel<>(dispatcher, true, true));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_DOOR_EVEN_1_TILE_ENTITY.get(), dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 3));
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_DOOR_ODD_1_TILE_ENTITY.get(), dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 4));

		RegistryClient.registerEntityRenderer(EntityTypes.LiftType.SIZE_2_2.registryObject.get(), RenderLift::new);
		RegistryClient.registerEntityRenderer(EntityTypes.LiftType.SIZE_2_2_DOUBLE_SIDED.registryObject.get(), RenderLift::new);
		RegistryClient.registerEntityRenderer(EntityTypes.LiftType.SIZE_3_2.registryObject.get(), RenderLift::new);
		RegistryClient.registerEntityRenderer(EntityTypes.LiftType.SIZE_3_2_DOUBLE_SIDED.registryObject.get(), RenderLift::new);
		RegistryClient.registerEntityRenderer(EntityTypes.LiftType.SIZE_3_3.registryObject.get(), RenderLift::new);
		RegistryClient.registerEntityRenderer(EntityTypes.LiftType.SIZE_3_3_DOUBLE_SIDED.registryObject.get(), RenderLift::new);
		RegistryClient.registerEntityRenderer(EntityTypes.LiftType.SIZE_4_3.registryObject.get(), RenderLift::new);
		RegistryClient.registerEntityRenderer(EntityTypes.LiftType.SIZE_4_3_DOUBLE_SIDED.registryObject.get(), RenderLift::new);
		RegistryClient.registerEntityRenderer(EntityTypes.LiftType.SIZE_4_4.registryObject.get(), RenderLift::new);
		RegistryClient.registerEntityRenderer(EntityTypes.LiftType.SIZE_4_4_DOUBLE_SIDED.registryObject.get(), RenderLift::new);

		RegistryClient.registerNetworkReceiver(PACKET_VERSION_CHECK, packet -> PacketTrainDataGuiClient.openVersionCheckS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_CHUNK_S2C, packet -> PacketTrainDataGuiClient.receiveChunk(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_DASHBOARD_SCREEN, packet -> PacketTrainDataGuiClient.openDashboardScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_PIDS_CONFIG_SCREEN, packet -> PacketTrainDataGuiClient.openPIDSConfigScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_ARRIVAL_PROJECTOR_CONFIG_SCREEN, packet -> PacketTrainDataGuiClient.openArrivalProjectorConfigScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_RAILWAY_SIGN_SCREEN, packet -> PacketTrainDataGuiClient.openRailwaySignScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_TICKET_MACHINE_SCREEN, packet -> PacketTrainDataGuiClient.openTicketMachineScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_TRAIN_SENSOR_SCREEN, packet -> PacketTrainDataGuiClient.openTrainSensorScreenS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_RESOURCE_PACK_CREATOR_SCREEN, packet -> PacketTrainDataGuiClient.openResourcePackCreatorScreen(Minecraft.getInstance()));
		RegistryClient.registerNetworkReceiver(PACKET_ANNOUNCE, packet -> PacketTrainDataGuiClient.announceS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_GENERATE_PATH, packet -> PacketTrainDataGuiClient.generatePathS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_CREATE_RAIL, packet -> PacketTrainDataGuiClient.createRailS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_CREATE_SIGNAL, packet -> PacketTrainDataGuiClient.createSignalS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_REMOVE_NODE, packet -> PacketTrainDataGuiClient.removeNodeS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_REMOVE_RAIL, packet -> PacketTrainDataGuiClient.removeRailConnectionS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_REMOVE_SIGNALS, packet -> PacketTrainDataGuiClient.removeSignalsS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_REMOVE_LIFT_FLOOR_TRACK, packet -> PacketTrainDataGuiClient.removeLiftFloorTrackS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_STATION, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.STATIONS, ClientData.DATA_CACHE.stationIdMap, (id, transportMode) -> new Station(id), false));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_PLATFORM, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.PLATFORMS, ClientData.DATA_CACHE.platformIdMap, null, false));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_SIDING, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.SIDINGS, ClientData.DATA_CACHE.sidingIdMap, null, false));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_ROUTE, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.ROUTES, ClientData.DATA_CACHE.routeIdMap, Route::new, false));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_DEPOT, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.DEPOTS, ClientData.DATA_CACHE.depotIdMap, Depot::new, false));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_STATION, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.STATIONS, ClientData.DATA_CACHE.stationIdMap, (id, transportMode) -> new Station(id), true));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_PLATFORM, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.PLATFORMS, ClientData.DATA_CACHE.platformIdMap, null, true));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_SIDING, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.SIDINGS, ClientData.DATA_CACHE.sidingIdMap, null, true));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_ROUTE, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.ROUTES, ClientData.DATA_CACHE.routeIdMap, Route::new, true));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_DEPOT, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.DEPOTS, ClientData.DATA_CACHE.depotIdMap, Depot::new, true));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_LIFT, packet -> PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(Minecraft.getInstance(), packet, ClientData.LIFTS, ClientData.DATA_CACHE.liftsClientIdMap, null, false));
		RegistryClient.registerNetworkReceiver(PACKET_WRITE_RAILS, packet -> ClientData.writeRails(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_TRAINS, packet -> ClientData.updateTrains(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_LIFTS, packet -> ClientData.updateLifts(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_TRAINS, packet -> ClientData.deleteTrains(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_LIFTS, packet -> ClientData.deleteLifts(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_TRAIN_PASSENGERS, packet -> ClientData.updateTrainPassengers(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_LIFT_PASSENGERS, packet -> ClientData.updateLiftPassengers(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_TRAIN_PASSENGER_POSITION, packet -> ClientData.updateTrainPassengerPosition(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_LIFT_PASSENGER_POSITION, packet -> ClientData.updateLiftPassengerPosition(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_RAIL_ACTIONS, packet -> ClientData.updateRailActions(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_SCHEDULE, packet -> ClientData.updateSchedule(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_LIFT_TRACK_FLOOR_SCREEN, packet -> PacketTrainDataGuiClient.openLiftTrackFloorS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_OPEN_LIFT_CUSTOMIZATION_SCREEN, packet -> PacketTrainDataGuiClient.openLiftCustomizationS2C(Minecraft.getInstance(), packet));

		RegistryClient.registerKeyBinding(KeyMappings.LIFT_MENU);

		Config.getPatreonList();
		Config.refreshProperties();
	}
}
