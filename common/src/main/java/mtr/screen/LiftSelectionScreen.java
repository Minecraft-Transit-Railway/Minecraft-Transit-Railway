package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.entity.EntityLift;
import mtr.mappings.ScreenMapper;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class LiftSelectionScreen extends ScreenMapper implements IGui {

	private final Button[] buttons;
	private final List<Integer> floorLevels;
	private final List<String> floorDescriptions = new ArrayList<>();
	private final Function<Integer, Boolean> hasStoppingFloor;

	private static final int BUTTON_WIDTH = SQUARE_SIZE * 3;
	private static final int DESCRIPTION_WIDTH = SQUARE_SIZE * 5;

	public LiftSelectionScreen(EntityLift entityLift) {
		super(new TextComponent(""));

		final Map<Integer, String> floors = entityLift == null ? new HashMap<>() : entityLift.floors;
		floorLevels = new ArrayList<>(floors.keySet());
		floorLevels.sort(Integer::compareTo);
		buttons = new Button[floorLevels.size()];
		for (int i = 0; i < floorLevels.size(); i++) {
			final String[] floorStringSplit = floors.getOrDefault(floorLevels.get(i), "").split("\\|\\|");
			floorDescriptions.add(floorStringSplit.length > 1 ? IGui.formatStationName(floorStringSplit[1]) : "");
			final int index = i;
			buttons[i] = new Button(0, 0, 0, SQUARE_SIZE, new TextComponent(floorStringSplit[0]), button -> {
				if (entityLift != null) {
					PacketTrainDataGuiClient.sendPressLiftButtonC2S(entityLift.getUUID(), floorLevels.get(index));
				}
			});
		}
		hasStoppingFloor = floor -> {
			if (entityLift != null) {
				return entityLift.hasStoppingFloorsClient(floor, true) || entityLift.hasStoppingFloorsClient(floor, false);
			} else {
				return false;
			}
		};
	}

	@Override
	protected void init() {
		super.init();
		iterateButtons((index, x, y) -> {
			IDrawing.setPositionAndWidth(buttons[index], x, y, BUTTON_WIDTH);
			addDrawableChild(buttons[index]);
		});
	}

	@Override
	public void tick() {
		for (int i = 0; i < floorLevels.size(); i++) {
			buttons[i].active = !hasStoppingFloor.apply(floorLevels.get(i));
		}
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			iterateButtons((index, x, y) -> {
				font.draw(matrices, floorDescriptions.get(index), x + BUTTON_WIDTH + TEXT_PADDING, y + TEXT_PADDING, ARGB_WHITE);
			});
			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void iterateButtons(IterateButtonsCallback iterateButtonsCallback) {
		final int maxRows = (height - SQUARE_SIZE * 2) / SQUARE_SIZE;
		final int columns = (int) Math.ceil((float) floorLevels.size() / maxRows);
		final int rows = (int) Math.ceil((float) floorLevels.size() / columns);

		int row = rows - 1;
		int column = 0;
		for (int i = 0; i < floorLevels.size(); i++) {
			iterateButtonsCallback.iterateButtonsCallback(i, width / 2 + (int) ((column - columns / 2F) * (BUTTON_WIDTH + DESCRIPTION_WIDTH)), row * SQUARE_SIZE + SQUARE_SIZE);
			if (row == 0) {
				row = rows - 1;
				column++;
			} else {
				row--;
			}
		}
	}

	@FunctionalInterface
	private interface IterateButtonsCallback {
		void iterateButtonsCallback(int index, int x, int y);
	}
}
