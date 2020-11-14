package mtr.packet;

import io.netty.buffer.Unpooled;
import mtr.data.*;
import mtr.gui.DashboardScreen;
import mtr.gui.ScreenBase;
import mtr.gui.TestScreen;
import mtr.gui.TestScreen2;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketByteBuf;

import java.util.Set;

public class PacketTrainDataGuiClient implements IPacket {

	public static void openDashboardScreenS2C(PacketByteBuf packet) {
		if (receiveAll(packet)) {
			MinecraftClient.getInstance().openScreen(new TestScreen());
		}
	}

	public static void openTrainSpawnerScreenS2C(PacketByteBuf packet) {
		if (receiveAll(packet)) {
			MinecraftClient.getInstance().openScreen(new TestScreen2(packet.readBlockPos()));
		}
	}

	public static void receiveTrainsS2C(PacketByteBuf packet) {
		ScreenBase.GuiBase.trains = IPacket.receiveData(packet, Train::new);

		final Screen screen = MinecraftClient.getInstance().currentScreen;
		if (screen instanceof DashboardScreen) {
			((DashboardScreen.GuiBase) ((DashboardScreen) screen).getDescription()).refreshInterface();
		}
	}

	public static void sendStationsAndRoutesC2S(Set<Station> stations, Set<Route> routes) {
		final PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		IPacket.sendData(packet, stations);
		IPacket.sendData(packet, routes);
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID_STATIONS_AND_ROUTES, packet);
	}

	public static void sendTrainSpawnerC2S(TrainSpawner trainSpawner) {
		final PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
		trainSpawner.writePacket(packet);
		ClientSidePacketRegistry.INSTANCE.sendToServer(ID_TRAIN_SPAWNER, packet);
	}

	public static boolean receiveAll(PacketByteBuf packet) {
		ScreenBase.GuiBase.stations = IPacket.receiveData(packet, Station::new);
		ScreenBase.GuiBase.platforms = IPacket.receiveData(packet, Platform::new);
		ScreenBase.GuiBase.routes = IPacket.receiveData(packet, Route::new);
		ScreenBase.GuiBase.trainSpawners = IPacket.receiveData(packet, TrainSpawner::new);

		final Screen screen = MinecraftClient.getInstance().currentScreen;
		if (screen instanceof ScreenBase) {
			((ScreenBase.GuiBase) ((ScreenBase) screen).getDescription()).refreshInterface();
			return false;
		} else {
			return true;
		}
	}
}
