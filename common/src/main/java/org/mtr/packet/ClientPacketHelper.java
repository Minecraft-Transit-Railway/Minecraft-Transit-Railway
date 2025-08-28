package org.mtr.packet;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTR;
import org.mtr.block.*;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Lift;
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
			if (blockEntity instanceof BlockTrainAnnouncer.TrainAnnouncerBlockEntity) {
				openScreen(new TrainAnnouncerScreen(blockPos, (BlockTrainAnnouncer.TrainAnnouncerBlockEntity) blockEntity), screenExtension -> screenExtension instanceof TrainAnnouncerScreen);
			} else if (blockEntity instanceof BlockTrainScheduleSensor.TrainScheduleSensorBlockEntity) {
				openScreen(new TrainScheduleSensorScreen(blockPos, (BlockTrainScheduleSensor.TrainScheduleSensorBlockEntity) blockEntity), screenExtension -> screenExtension instanceof TrainScheduleSensorScreen);
			} else if (blockEntity instanceof BlockTrainSensorBase.BlockEntityBase) {
				openScreen(new TrainBasicSensorScreen(blockPos), screenExtension -> screenExtension instanceof TrainBasicSensorScreen);
			} else if (blockEntity instanceof BlockRailwaySign.RailwaySignBlockEntity || blockEntity instanceof BlockRouteSignBase.BlockEntityBase) {
				openScreen(new RailwaySignScreen(blockPos), screenExtension -> screenExtension instanceof RailwaySignScreen);
			} else if (blockEntity instanceof BlockLiftTrackFloor.LiftTrackFloorBlockEntity) {
				openScreen(new LiftTrackFloorScreen(blockPos, (BlockLiftTrackFloor.LiftTrackFloorBlockEntity) blockEntity), screenExtension -> screenExtension instanceof LiftTrackFloorScreen);
			} else if (blockEntity instanceof BlockSignalBase.BlockEntityBase) {
				openScreen(new SignalColorScreen(blockPos, (BlockSignalBase.BlockEntityBase) blockEntity), screenExtension -> screenExtension instanceof SignalColorScreen);
			} else if (blockEntity instanceof BlockEyeCandy.EyeCandyBlockEntity) {
				openScreen(new EyeCandyScreen(blockPos, (BlockEyeCandy.EyeCandyBlockEntity) blockEntity), screenExtension -> screenExtension instanceof EyeCandyScreen);
			}
		});
	}

	public static void openDashboardScreen(TransportMode transportMode, PacketOpenDashboardScreen.ScreenType screenType, long id) {
		switch (screenType) {
			case STATION:
				openScreen(new StationScreen(MinecraftClientData.getDashboardInstance().stationIdMap.get(id), new DashboardScreen(transportMode)), screenExtension -> screenExtension instanceof StationScreen);
				break;
			case DEPOT:
				openScreen(new DepotScreen(MinecraftClientData.getDashboardInstance().depotIdMap.get(id), new DashboardScreen(transportMode)), screenExtension -> screenExtension instanceof DepotScreen);
				break;
			case PLATFORM:
				openScreen(new PlatformScreen(MinecraftClientData.getDashboardInstance().platformIdMap.get(id), new DashboardScreen(transportMode)), screenExtension -> screenExtension instanceof PlatformScreen);
				break;
			case SIDING:
				openScreen(new SidingScreen(MinecraftClientData.getDashboardInstance().sidingIdMap.get(id), new DashboardScreen(transportMode)), screenExtension -> screenExtension instanceof SidingScreen);
				break;
			default:
				openScreen(new DashboardScreen(transportMode), screenExtension -> screenExtension instanceof DashboardScreen);
				break;
		}
	}

	public static void openLiftCustomizationScreen(BlockPos blockPos) {
		for (final Lift lift : MinecraftClientData.getInstance().lifts) {
			if (lift.getFloorIndex(MTR.blockPosToPosition(blockPos)) >= 0) {
				MinecraftClient.getInstance().setScreen(new LiftCustomizationScreen(lift));
				break;
			}
		}
	}

	public static void openPIDSConfigScreen(BlockPos blockPos, int maxArrivals) {
		getBlockEntity(blockPos, blockEntity -> {
			if (blockEntity instanceof BlockPIDSBase.BlockEntityBase) {
				openScreen(new PIDSConfigScreen(blockPos, maxArrivals), screenExtension -> screenExtension instanceof PIDSConfigScreen);
			}
		});
	}

	public static void openRailShapeModifierScreen(String railId) {
		openScreen(new RailModifierScreen(railId), screenExtension -> screenExtension instanceof RailModifierScreen);
	}

	public static void openTicketMachineScreen(int balance) {
		openScreen(new TicketMachineScreen(balance), screenExtension -> screenExtension instanceof TicketMachineScreen);
	}

	private static void openScreen(Screen screenExtension, Predicate<Screen> isInstance) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final Screen screen = minecraftClient.currentScreen;
		if (screen == null || !isInstance.test(screen)) {
			minecraftClient.setScreen(screenExtension);
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
