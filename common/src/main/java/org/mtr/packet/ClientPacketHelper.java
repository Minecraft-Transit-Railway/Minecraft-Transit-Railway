package org.mtr.packet;

import gg.essential.universal.UMinecraft;
import gg.essential.universal.UScreen;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTR;
import org.mtr.block.*;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Lift;
import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
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
			} else if (blockEntity instanceof BlockRailwaySign.RailwaySignBlockEntity || blockEntity instanceof BlockRouteSignBase.BlockEntityBase) {
				openScreen(new RailwaySignScreen(blockPos), screen -> screen instanceof RailwaySignScreen);
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
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final Screen currentScreen = minecraftClient.currentScreen;
		if (currentScreen == null || !isInstance.test(currentScreen)) {
			minecraftClient.setScreen(screen);
		}
	}

	private static void openScreen(UScreen screen, Predicate<UScreen> isInstance) {
		final Object currentScreen = UMinecraft.getCurrentScreenObj();
		if (!(currentScreen instanceof UScreen uScreen) || !isInstance.test(uScreen)) {
			UMinecraft.setCurrentScreenObj(screen);
		}
	}

	private static void getBlockEntity(BlockPos blockPos, Consumer<BlockEntity> consumer) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
		if (clientWorld != null) {
			final BlockEntity blockEntity = clientWorld.getBlockEntity(blockPos);
			if (blockEntity != null) {
				consumer.accept(blockEntity);
			}
		}
	}
}
