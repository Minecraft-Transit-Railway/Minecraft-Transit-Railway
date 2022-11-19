package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.data.DataConverter;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.entity.EntityLift;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.RenderLift;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiftSelectionScreen extends ScreenMapper implements IGui {

	private final DashboardList selectionList;
	private final List<Integer> floorLevels;
	private final List<String> floorDescriptions = new ArrayList<>();
	private final EntityLift entityLift;

	public LiftSelectionScreen(EntityLift entityLift) {
		super(Text.literal(""));
		this.entityLift = entityLift;
		final Map<Integer, String> floors = entityLift == null ? new HashMap<>() : entityLift.floors;
		floorLevels = new ArrayList<>(floors.keySet());
		floorLevels.sort((a, b) -> b - a);
		floorLevels.forEach(floorLevel -> floorDescriptions.add(IGui.formatStationName(floors.getOrDefault(floorLevel, ""))));
		selectionList = new DashboardList(this::onPress, null, null, null, null, null, null, () -> "", text -> {
		});
	}

	@Override
	protected void init() {
		super.init();
		selectionList.x = width / 2 - PANEL_WIDTH;
		selectionList.y = SQUARE_SIZE;
		selectionList.width = PANEL_WIDTH * 2;
		selectionList.height = height - SQUARE_SIZE * 2;
		selectionList.init(this::addDrawableChild);
	}

	@Override
	public void tick() {
		selectionList.tick();
		final List<NameColorDataBase> list = new ArrayList<>();
		for (int i = 0; i < floorLevels.size(); i++) {
			list.add(new DataConverter(floorDescriptions.get(i), hasStoppingFloor(floorLevels.get(i)) ? RenderLift.LIGHT_COLOR : ARGB_BLACK));
		}
		selectionList.setData(list, true, false, false, false, false, false);
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			selectionList.render(matrices, font);
			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		selectionList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		selectionList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void onPress(NameColorDataBase data, int index) {
		if (entityLift != null) {
			PacketTrainDataGuiClient.sendPressLiftButtonC2S(entityLift.getUUID(), floorLevels.get(index));
		}
		onClose();
	}

	private boolean hasStoppingFloor(int floor) {
		if (entityLift != null) {
			return entityLift.hasStoppingFloorsClient(floor, true) || entityLift.hasStoppingFloorsClient(floor, false);
		} else {
			return false;
		}
	}
}
