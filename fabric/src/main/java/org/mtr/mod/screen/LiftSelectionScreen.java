package org.mtr.mod.screen;

import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.core.operation.PressLift;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.ClientWorld;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.packet.PacketPressLiftButton;
import org.mtr.mod.render.RenderLifts;

public class LiftSelectionScreen extends MTRScreenBase implements IGui {

	private final DashboardList selectionList;
	private final ObjectArrayList<BlockPos> floorLevels = new ObjectArrayList<>();
	private final ObjectArrayList<String> floorDescriptions = new ObjectArrayList<>();
	private final long liftId;

	public LiftSelectionScreen(long liftId) {
		super();
		this.liftId = liftId;
		final Lift lift = MinecraftClientData.getLift(liftId);
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (lift != null && clientWorld != null) {
			lift.iterateFloors(floor -> {
				final BlockPos blockPos = Init.positionToBlockPos(floor.getPosition());
				floorLevels.add(blockPos);
				final ObjectObjectImmutablePair<LiftDirection, ObjectObjectImmutablePair<String, String>> liftDetails = RenderLifts.getLiftDetails(new World(clientWorld.data), lift, blockPos);
				floorDescriptions.add(String.format(
						"%s %s",
						liftDetails.right().left(),
						IGui.formatStationName(String.join("|", liftDetails.right().right()))
				));
			});
		}
		selectionList = new DashboardList(this::onPress, null, null, null, null, null, null, () -> "", text -> {
		});
	}

	@Override
	protected void init2() {
		super.init2();
		selectionList.x = width / 2 - PANEL_WIDTH;
		selectionList.y = SQUARE_SIZE;
		selectionList.width = PANEL_WIDTH * 2;
		selectionList.height = height - SQUARE_SIZE * 2;
		selectionList.init(this::addChild);
	}

	@Override
	public void tick2() {
		final Lift lift = MinecraftClientData.getLift(liftId);
		if (lift == null) {
			onClose2();
		} else {
			selectionList.tick();
			final ObjectArrayList<DashboardListItem> list = new ObjectArrayList<>();
			for (int i = floorLevels.size() - 1; i >= 0; i--) {
				final BlockPos blockPos = floorLevels.get(i);
				list.add(new DashboardListItem(
						blockPos.asLong(),
						floorDescriptions.get(i),
						lift.hasInstruction(lift.getFloorIndex(Init.blockPosToPosition(blockPos))).contains(LiftDirection.NONE) ? 0xFFFF0000 : ARGB_BLACK
				));
			}
			selectionList.setData(list, true, false, false, false, false, false);
		}
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		renderBackground(graphicsHolder);
		selectionList.render(graphicsHolder);
		super.render(graphicsHolder, mouseX, mouseY, delta);
	}

	@Override
	public void mouseMoved2(double mouseX, double mouseY) {
		selectionList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled2(double mouseX, double mouseY, double amount) {
		selectionList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled2(mouseX, mouseY, amount);
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}

	private void onPress(DashboardListItem dashboardListItem, int index) {
		final PressLift pressLift = new PressLift();
		pressLift.add(Init.blockPosToPosition(floorLevels.get(floorLevels.size() - index - 1)), LiftDirection.NONE);
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketPressLiftButton(pressLift));
		onClose2();
	}
}
