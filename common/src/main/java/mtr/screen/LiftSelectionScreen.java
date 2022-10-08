package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.ClientData;
import mtr.data.DataConverter;
import mtr.data.IGui;
import mtr.data.LiftClient;
import mtr.data.NameColorDataBase;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.RenderTrains;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class LiftSelectionScreen extends ScreenMapper implements IGui {

	private final DashboardList selectionList;
	private final List<BlockPos> floorLevels = new ArrayList<>();
	private final List<String> floorDescriptions = new ArrayList<>();
	private final LiftClient lift;

	public LiftSelectionScreen(LiftClient lift) {
		super(Text.literal(""));
		this.lift = lift;
		lift.iterateFloors(floor -> {
			floorLevels.add(floor);
			floorDescriptions.add(IGui.formatStationName(String.join("|", ClientData.DATA_CACHE.requestLiftFloorText(floor))));
		});
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
		for (int i = floorLevels.size() - 1; i >= 0; i--) {
			list.add(new DataConverter(floorDescriptions.get(i), lift.liftInstructions.containsInstruction(floorLevels.get(i).getY()) ? RenderTrains.LIFT_LIGHT_COLOR : ARGB_BLACK));
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
		if (lift != null) {
			PacketTrainDataGuiClient.sendPressLiftButtonC2S(lift.id, floorLevels.get(floorLevels.size() - index - 1).getY());
		}
		onClose();
	}
}
