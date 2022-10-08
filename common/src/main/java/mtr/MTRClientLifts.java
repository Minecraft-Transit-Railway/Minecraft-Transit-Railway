package mtr;

import mtr.client.ClientData;
import mtr.client.Config;
import mtr.item.ItemBlockClickingBase;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.RenderLift;
import mtr.render.RenderLiftButtons;
import mtr.render.RenderPSDAPGDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;

public class MTRClientLifts implements IPacket {

	public static void init() {
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.LIFT_DOOR_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.LIFT_DOOR_ODD_1.get());

		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.LIFT_BUTTONS_LINK_CONNECTOR.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.LIFT_BUTTONS_LINK_REMOVER.get(), ItemBlockClickingBase.TAG_POS);

		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_BUTTONS_1_TILE_ENTITY.get(), RenderLiftButtons::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_DOOR_1_TILE_ENTITY.get(), dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 3));
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

		RegistryClient.registerNetworkReceiver(PACKET_OPEN_LIFT_TRACK_FLOOR_SCREEN, packet -> PacketTrainDataGuiClient.openLiftTrackFloorS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_REMOVE_LIFT_FLOOR_TRACK, packet -> PacketTrainDataGuiClient.removeLiftFloorTrackS2C(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_LIFTS, packet -> ClientData.updateLifts(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_DELETE_LIFTS, packet -> ClientData.deleteLifts(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_LIFT_PASSENGERS, packet -> ClientData.updateLiftPassengers(Minecraft.getInstance(), packet));
		RegistryClient.registerNetworkReceiver(PACKET_UPDATE_LIFT_PASSENGER_POSITION, packet -> ClientData.updateLiftPassengerPosition(Minecraft.getInstance(), packet));

		RegistryClient.registerKeyBinding(KeyMappings.LIFT_MENU);

		Config.getPatreonList();
		Config.refreshProperties();
	}
}
