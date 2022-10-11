package mtr;

import mtr.mappings.BlockEntityMapper;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;

public class MTRLifts implements IPacket {

	public static void init(
			BiConsumer<String, RegistryObject<Item>> registerItem,
			BiConsumer<String, RegistryObject<Block>> registerBlock,
			MTR.RegisterBlockItem registerBlockItem,
			BiConsumer<String, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>>> registerBlockEntityType,
			BiConsumer<String, RegistryObject<? extends EntityType<? extends Entity>>> registerEntityType
	) {
		registerItem.accept("brush", Items.BRUSH);
		registerItem.accept("escalator", Items.ESCALATOR);
		registerItem.accept("lift_buttons_link_connector", Items.LIFT_BUTTONS_LINK_CONNECTOR);
		registerItem.accept("lift_buttons_link_remover", Items.LIFT_BUTTONS_LINK_REMOVER);
		registerItem.accept("lift_door_1", Items.LIFT_DOOR_1);
		registerItem.accept("lift_door_odd_1", Items.LIFT_DOOR_ODD_1);
		registerItem.accept("lift_refresher", Items.LIFT_REFRESHER);

		registerBlock.accept("escalator_side", Blocks.ESCALATOR_SIDE);
		registerBlock.accept("escalator_step", Blocks.ESCALATOR_STEP);
		registerBlockItem.accept("lift_buttons_1", Blocks.LIFT_BUTTONS_1, ItemGroups.ESCALATORS_LIFTS);
		registerBlockItem.accept("lift_panel_even_1", Blocks.LIFT_PANEL_EVEN_1, ItemGroups.ESCALATORS_LIFTS);
		registerBlock.accept("lift_door_1", Blocks.LIFT_DOOR_EVEN_1);
		registerBlock.accept("lift_door_odd_1", Blocks.LIFT_DOOR_ODD_1);
		registerBlockItem.accept("lift_track_1", Blocks.LIFT_TRACK_1, ItemGroups.ESCALATORS_LIFTS);
		registerBlockItem.accept("lift_track_floor_1", Blocks.LIFT_TRACK_FLOOR_1, ItemGroups.ESCALATORS_LIFTS);

		registerBlockEntityType.accept("lift_buttons_1", BlockEntityTypes.LIFT_BUTTONS_1_TILE_ENTITY);
		registerBlockEntityType.accept("lift_panel_even_1", BlockEntityTypes.LIFT_PANEL_EVEN_1_TILE_ENTITY);
		registerBlockEntityType.accept("lift_track_floor_1", BlockEntityTypes.LIFT_TRACK_FLOOR_1_TILE_ENTITY);
		registerBlockEntityType.accept("lift_door_1", BlockEntityTypes.LIFT_DOOR_EVEN_1_TILE_ENTITY);
		registerBlockEntityType.accept("lift_door_odd_1", BlockEntityTypes.LIFT_DOOR_ODD_1_TILE_ENTITY);

		registerEntityType.accept(EntityTypes.LiftType.SIZE_2_2.key, EntityTypes.LiftType.SIZE_2_2.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_2_2_DOUBLE_SIDED.key, EntityTypes.LiftType.SIZE_2_2_DOUBLE_SIDED.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_3_2.key, EntityTypes.LiftType.SIZE_3_2.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_3_2_DOUBLE_SIDED.key, EntityTypes.LiftType.SIZE_3_2_DOUBLE_SIDED.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_3_3.key, EntityTypes.LiftType.SIZE_3_3.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_3_3_DOUBLE_SIDED.key, EntityTypes.LiftType.SIZE_3_3_DOUBLE_SIDED.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_4_3.key, EntityTypes.LiftType.SIZE_4_3.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_4_3_DOUBLE_SIDED.key, EntityTypes.LiftType.SIZE_4_3_DOUBLE_SIDED.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_4_4.key, EntityTypes.LiftType.SIZE_4_4.registryObject);
		registerEntityType.accept(EntityTypes.LiftType.SIZE_4_4_DOUBLE_SIDED.key, EntityTypes.LiftType.SIZE_4_4_DOUBLE_SIDED.registryObject);

		Registry.registerNetworkReceiver(PACKET_PRESS_LIFT_BUTTON, PacketTrainDataGuiServer::receivePressLiftButtonC2S);
		Registry.registerNetworkReceiver(PACKET_UPDATE_LIFT, (minecraftServer, player, packet) -> PacketTrainDataGuiServer.receiveUpdateOrDeleteC2S(minecraftServer, player, packet, PACKET_UPDATE_LIFT, railwayData -> railwayData.lifts, railwayData -> railwayData.dataCache.liftsServerIdMap, null, false));
		Registry.registerNetworkReceiver(PACKET_UPDATE_LIFT_TRACK_FLOOR, PacketTrainDataGuiServer::receiveLiftTrackFloorC2S);
		Registry.registerNetworkReceiver(PACKET_UPDATE_LIFT_PASSENGER_POSITION, PacketTrainDataGuiServer::receiveUpdateLiftPassengerPosition);
	}
}
