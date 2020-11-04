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

		public static final Set<Station> stations = new HashSet<>();
		public static final Set<Platform> platforms = new HashSet<>();
		public static final Set<Route> routes = new HashSet<>();
		public static final Set<Train> trains = new HashSet<>();
		public static final Set<TrainSpawner> trainSpawners = new HashSet<>();

		public abstract void refreshInterface();

		public abstract void sendData();
	}
}
