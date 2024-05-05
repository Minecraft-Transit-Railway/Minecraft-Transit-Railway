package org.mtr.mod.packet;

import org.mtr.core.data.Lift;
import org.mtr.core.data.TransportMode;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mod.Init;
import org.mtr.mod.block.*;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.screen.*;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Since packets are registered serverside, they will fail if any client classes are used (e.g. screens).
 */
public final class ClientPacketHelper {

	public static void openBlockEntityScreen(BlockPos blockPos) {
		getBlockEntity(blockPos, blockEntity -> {
			if (blockEntity.data instanceof BlockTrainAnnouncer.BlockEntity) {
				openScreen(new TrainAnnouncerScreen(blockPos), screenExtension -> screenExtension instanceof TrainAnnouncerScreen);
			} else if (blockEntity.data instanceof BlockTrainScheduleSensor.BlockEntity) {
				openScreen(new TrainScheduleSensorScreen(blockPos), screenExtension -> screenExtension instanceof TrainScheduleSensorScreen);
			} else if (blockEntity.data instanceof BlockTrainSensorBase.BlockEntityBase) {
				openScreen(new TrainBasicSensorScreen(blockPos), screenExtension -> screenExtension instanceof TrainBasicSensorScreen);
			} else if (blockEntity.data instanceof BlockRailwaySign.BlockEntity || blockEntity.data instanceof BlockRouteSignBase.BlockEntityBase) {
				openScreen(new RailwaySignScreen(blockPos), screenExtension -> screenExtension instanceof RailwaySignScreen);
			} else if (blockEntity.data instanceof BlockLiftTrackFloor.BlockEntity) {
				openScreen(new LiftTrackFloorScreen(blockPos), screenExtension -> screenExtension instanceof LiftTrackFloorScreen);
			}
		});
	}

	public static void openDashboardScreen(TransportMode transportMode, boolean useTimeAndWindSync) {
		openScreen(new DashboardScreen(transportMode, useTimeAndWindSync), screenExtension -> screenExtension instanceof DashboardScreen);
	}

	public static void openLiftCustomizationScreen(BlockPos blockPos) {
		for (final Lift lift : MinecraftClientData.getInstance().lifts) {
			if (lift.getFloorIndex(Init.blockPosToPosition(blockPos)) >= 0) {
				MinecraftClient.getInstance().openScreen(new Screen(new LiftCustomizationScreen(lift)));
				break;
			}
		}
	}

	public static void openPIDSConfigScreen(BlockPos blockPos, int maxArrivals) {
		getBlockEntity(blockPos, blockEntity -> {
			if (blockEntity.data instanceof BlockPIDSBase.BlockEntityBase) {
				openScreen(new PIDSConfigScreen(blockPos, maxArrivals), screenExtension -> screenExtension instanceof PIDSConfigScreen);
			}
		});
	}

	public static void openRailShapeModifierScreen(String railId) {
		openScreen(new RailShapeModifierScreen(railId), screenExtension -> screenExtension instanceof RailShapeModifierScreen);
	}

	public static void openTicketMachineScreen(int balance) {
		openScreen(new TicketMachineScreen(balance), screenExtension -> screenExtension instanceof TicketMachineScreen);
	}

	public static void openCustomRailScreen(int speed, boolean isOneWay) {
		openScreen(new CustomRailScreen(speed, isOneWay), screenExtension -> screenExtension instanceof CustomRailScreen);
	}

	private static void openScreen(ScreenExtension screenExtension, Predicate<ScreenExtension> isInstance) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final Screen screen = minecraftClient.getCurrentScreenMapped();
		if (screen == null || screen.data instanceof ScreenExtension && !isInstance.test((ScreenExtension) screen.data)) {
			minecraftClient.openScreen(new Screen(screenExtension));
		}
	}

	private static void getBlockEntity(BlockPos blockPos, Consumer<BlockEntity> consumer) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld != null) {
			final BlockEntity blockEntity = clientWorld.getBlockEntity(blockPos);
			if (blockEntity != null) {
				consumer.accept(blockEntity);
			}
		}
	}
}
