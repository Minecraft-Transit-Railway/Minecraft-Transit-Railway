package org.mtr.screen;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTR;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.core.operation.PressLift;
import org.mtr.data.IGui;
import org.mtr.packet.PacketPressLiftButton;
import org.mtr.registry.RegistryClient;
import org.mtr.render.RenderLifts;

public class LiftSelectionScreen extends MTRScreenBase implements IGui {

	private final DashboardList selectionList;
	private final ObjectArrayList<BlockPos> floorLevels = new ObjectArrayList<>();
	private final ObjectArrayList<String> floorDescriptions = new ObjectArrayList<>();
	private final long liftId;

	public LiftSelectionScreen(long liftId) {
		super();
		this.liftId = liftId;
		final Lift lift = MinecraftClientData.getLift(liftId);
		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
		if (lift != null && clientWorld != null) {
			lift.iterateFloors(floor -> {
				final BlockPos blockPos = MTR.positionToBlockPos(floor.getPosition());
				floorLevels.add(blockPos);
				final ObjectObjectImmutablePair<LiftDirection, ObjectObjectImmutablePair<String, String>> liftDetails = RenderLifts.getLiftDetails(clientWorld, lift, blockPos);
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
	protected void init() {
		super.init();
		selectionList.x = width / 2 - PANEL_WIDTH;
		selectionList.y = SQUARE_SIZE;
		selectionList.width = PANEL_WIDTH * 2;
		selectionList.height = height - SQUARE_SIZE * 2;
		selectionList.init(this::addSelectableChild);
	}

	@Override
	public void tick() {
		final Lift lift = MinecraftClientData.getLift(liftId);
		if (lift == null) {
			close();
		} else {
			selectionList.tick();
			final ObjectArrayList<DashboardListItem> list = new ObjectArrayList<>();
			for (int i = floorLevels.size() - 1; i >= 0; i--) {
				final BlockPos blockPos = floorLevels.get(i);
				list.add(new DashboardListItem(
						blockPos.asLong(),
						floorDescriptions.get(i),
						lift.hasInstruction(lift.getFloorIndex(MTR.blockPosToPosition(blockPos))).contains(LiftDirection.NONE) ? 0xFFFF0000 : ARGB_BLACK
				));
			}
			selectionList.setData(list, true, false, false, false, false, false);
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		selectionList.render(context);
		super.render(context, mouseX, mouseY, delta);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		selectionList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		selectionList.mouseScrolled(mouseX, mouseY, verticalAmount);
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	private void onPress(DashboardListItem dashboardListItem, int index) {
		final PressLift pressLift = new PressLift();
		pressLift.add(MTR.blockPosToPosition(floorLevels.get(floorLevels.size() - index - 1)), LiftDirection.NONE);
		RegistryClient.sendPacketToServer(new PacketPressLiftButton(pressLift));
		close();
	}
}
