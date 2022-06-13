package mtr;

import mtr.client.Config;
import mtr.item.ItemBlockClickingBase;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.RenderLift;
import mtr.render.RenderLiftButtons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;

public class MTRClientLifts implements IPacket {

	public static void init() {
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.LIFT_DOOR_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), Blocks.LIFT_DOOR_ODD_1.get());

		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.LIFT_BUTTONS_LINK_CONNECTOR.get(), ItemBlockClickingBase.TAG_POS);
		RegistryClient.registerItemModelPredicate(MTR.MOD_ID + ":selected", Items.LIFT_BUTTONS_LINK_REMOVER.get(), ItemBlockClickingBase.TAG_POS);

		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.LIFT_BUTTONS_1_TILE_ENTITY.get(), RenderLiftButtons::new);

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

		RegistryClient.registerKeyBinding(KeyMappings.LIFT_MENU);

		Config.getPatreonList();
		Config.refreshProperties();
	}
}
