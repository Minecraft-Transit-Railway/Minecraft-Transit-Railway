package mtr.gui;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import mtr.data.*;

import java.util.HashSet;
import java.util.Set;

public abstract class ScreenBase extends CottonClientScreen implements IGui {

	public ScreenBase(GuiBase description) {
		super(description);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	public abstract static class GuiBase extends LightweightGuiDescription {

		public static Set<Station> stations = new HashSet<>();
		public static Set<Platform> platforms = new HashSet<>();
		public static Set<Route> routes = new HashSet<>();
		public static Set<Train> trains = new HashSet<>();
		public static Set<TrainSpawner> trainSpawners = new HashSet<>();
		public static boolean refreshingInterface = false;

		public abstract void refreshInterface();

		public abstract void sendData();
	}
}
