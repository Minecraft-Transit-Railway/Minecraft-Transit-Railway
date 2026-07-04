package org.mtr.packet;

import gg.essential.universal.UMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.mtr.MTR;
import org.mtr.block.*;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.*;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.resource.SignResource;
import org.mtr.screen.*;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Since packets are registered serverside, they will fail if any client classes are used (e.g. screens).
 */
public final class ClientPacketHelper {

	public static void openBlockEntityScreen(BlockPos blockPos) {
		getBlockEntity(blockPos, blockEntity -> {
			if (blockEntity instanceof BlockTrainAnnouncer.TrainAnnouncerBlockEntity trainAnnouncerBlockEntity) {
				openScreen(new TrainAnnouncerScreen(blockPos, trainAnnouncerBlockEntity), screen -> screen instanceof TrainAnnouncerScreen);
			} else if (blockEntity instanceof BlockTrainScheduleSensor.TrainScheduleSensorBlockEntity trainScheduleSensorBlockEntity) {
				openScreen(new TrainScheduleSensorScreen(blockPos, trainScheduleSensorBlockEntity), screen -> screen instanceof TrainScheduleSensorScreen);
			} else if (blockEntity instanceof BlockTrainSensorBase.BlockEntityBase trainSensorBaseBlockEntity) {
				openScreen(new TrainBasicSensorScreen(blockPos, trainSensorBaseBlockEntity), screen -> screen instanceof TrainBasicSensorScreen);
			} else if (blockEntity instanceof BlockDriverKeyDispenser.DriverKeyDispenserBlockEntity driverKeyDispenserBlockEntity) {
				openScreen(new DriverKeyDispenserScreen(blockPos, driverKeyDispenserBlockEntity), screen -> screen instanceof DriverKeyDispenserScreen);
			} else if (blockEntity instanceof BlockPIDSBase.BlockEntityBase pidsBaseBlockEntity) {
				openScreen(new PIDSConfigScreen(blockPos, pidsBaseBlockEntity), screen -> screen instanceof RailwaySignScreen);
			} else if (blockEntity instanceof BlockRailwaySign.RailwaySignBlockEntity railwaySignBlockEntity) {
				openScreen(new RailwaySignScreen(blockPos, railwaySignBlockEntity), screen -> screen instanceof RailwaySignScreen);
			} else if (blockEntity instanceof BlockRouteSignBase.BlockEntityBase routeSignBlockEntity) {
				openRouteListSelectorScreen(blockPos, routeSignBlockEntity);
			} else if (blockEntity instanceof BlockLiftTrackFloor.LiftTrackFloorBlockEntity liftTrackFloorBlockEntity) {
				openScreen(new LiftTrackFloorScreen(blockPos, liftTrackFloorBlockEntity), screen -> screen instanceof LiftTrackFloorScreen);
			} else if (blockEntity instanceof BlockSignalBase.BlockEntityBase signalBaseBlockEntity) {
				openScreen(new SignalColorScreen(blockPos, signalBaseBlockEntity), screen -> screen instanceof SignalColorScreen);
			} else if (blockEntity instanceof BlockEyeCandy.EyeCandyBlockEntity eyeCandyBlockEntity) {
				openScreen(new EyeCandyScreen(blockPos, eyeCandyBlockEntity), screen -> screen instanceof EyeCandyScreen);
			}
		});
	}

	public static void openDashboardScreen(TransportMode transportMode, PacketOpenDashboardScreen.ScreenType screenType, long id) {
		switch (screenType) {
			case STATION:
				openScreen(new StationScreen(MinecraftClientData.getDashboardInstance().stationIdMap.get(id), null), screen -> screen instanceof StationScreen);
				break;
			case DEPOT:
				openScreen(new DepotScreen(MinecraftClientData.getDashboardInstance().depotIdMap.get(id), new DashboardScreen(transportMode)), screen -> screen instanceof DepotScreen);
				break;
			case PLATFORM:
				openScreen(new PlatformScreen(MinecraftClientData.getDashboardInstance().platformIdMap.get(id), new DashboardScreen(transportMode)), screen -> screen instanceof PlatformScreen);
				break;
			case SIDING:
				openScreen(new SidingScreen(MinecraftClientData.getDashboardInstance().sidingIdMap.get(id), new DashboardScreen(transportMode)), screen -> screen instanceof SidingScreen);
				break;
			default:
				openScreen(new DashboardScreen(transportMode), screen -> screen instanceof DashboardScreen);
				break;
		}
	}

	public static void openLiftCustomizationScreen(BlockPos blockPos) {
		for (final Lift lift : MinecraftClientData.getInstance().lifts) {
			if (lift.getFloorIndex(MTR.blockPosToPosition(blockPos)) >= 0) {
				UMinecraft.setCurrentScreenObj(new LiftCustomizationScreen(lift));
				break;
			}
		}
	}

	public static void openRailShapeModifierScreen(String railId) {
		final Rail rail = MinecraftClientData.getInstance().railIdMap.get(railId);
		if (rail != null) {
			openScreen(new RailModifierScreen(rail), screen -> screen instanceof RailModifierScreen);
		}
	}

	public static void openTicketMachineScreen(int balance) {
		openScreen(new TicketMachineScreen(balance), screen -> screen instanceof TicketMachineScreen);
	}

	private static void openScreen(Screen screen, Predicate<Screen> isInstance) {
		final Minecraft minecraftClient = Minecraft.getInstance();
		final Screen currentScreen = minecraftClient.screen;
		if (currentScreen == null || !isInstance.test(currentScreen)) {
			minecraftClient.setScreen(screen);
		}
	}

	private static void getBlockEntity(BlockPos blockPos, Consumer<BlockEntity> consumer) {
		final ClientLevel clientWorld = Minecraft.getInstance().level;
		if (clientWorld != null) {
			final BlockEntity blockEntity = clientWorld.getBlockEntity(blockPos);
			if (blockEntity != null) {
				consumer.accept(blockEntity);
			}
		}
	}

	private static void openRouteListSelectorScreen(BlockPos blockPos, BlockRouteSignBase.BlockEntityBase routeSignBlockEntity) {
		final PlatformListSelectorScreen platformListSelectorScreen = new PlatformListSelectorScreen(selectedRoutes -> new PacketUpdateRailwaySignConfig(
			blockPos,
			new LongAVLTreeSet[]{new LongAVLTreeSet(selectedRoutes.stream().map(NameColorDataBase::getId).toList())},
			new String[0]
		).send(Minecraft.getInstance().level));

		final ObjectArrayList<Platform> platforms = SignResource.getPlatforms(blockPos);
		platformListSelectorScreen.setAvailableList(platforms);
		platforms.forEach(platform -> {
			if (routeSignBlockEntity.getPlatformId() == platform.getId()) {
				platformListSelectorScreen.selectData(platform);
			}
		});

		UMinecraft.setCurrentScreenObj(platformListSelectorScreen);
	}
}
