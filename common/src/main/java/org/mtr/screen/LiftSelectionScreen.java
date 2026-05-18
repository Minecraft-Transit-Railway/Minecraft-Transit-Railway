package org.mtr.screen;

import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTR;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.core.operation.PressLift;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.packet.PacketPressLiftButton;
import org.mtr.registry.RegistryClient;
import org.mtr.render.RenderLifts;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.BackgroundComponent;
import org.mtr.widget.ListComponent;
import org.mtr.widget.ListItem;
import org.mtr.widget.SlotBackgroundComponent;

import java.awt.*;

/**
 * Elementa screen for selecting a lift floor while riding a lift car.
 */
public final class LiftSelectionScreen extends WindowBase {

	private final ObjectArrayList<BlockPos> floorLevels = new ObjectArrayList<>();
	private final ObjectArrayList<String> floorDescriptions = new ObjectArrayList<>();
	private final long liftId;

	private final ListComponent<DashboardListItem> listComponent;

	public LiftSelectionScreen(long liftId) {
		this.liftId = liftId;
		final Lift lift = MinecraftClientData.getLift(liftId);
		final ClientWorld clientWorld = MinecraftClient.getInstance().world;

		if (lift != null && clientWorld != null) {
			lift.iterateFloors(floor -> {
				final BlockPos blockPos = MTR.positionToBlockPos(floor.getPosition());
				floorLevels.add(blockPos);
				final ObjectObjectImmutablePair<LiftDirection, ObjectObjectImmutablePair<String, String>> liftDetails = RenderLifts.getLiftDetails(clientWorld, lift, blockPos);
				floorDescriptions.add(String.format("%s %s", liftDetails.right().left(), IGui.formatStationName(String.join("|", liftDetails.right().right()))));
			});
		}

		final BackgroundComponent backgroundComponent = new BackgroundComponent(getWindow(), ObjectImmutableList.of());
		new UIWrappedText(TranslationProvider.GUI_MTR_SELECT_FLOOR.getString(), false)
			.setChildOf(backgroundComponent)
			.setWidth(new RelativeConstraint())
			.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		final SlotBackgroundComponent slotBackgroundComponent = (SlotBackgroundComponent) new SlotBackgroundComponent()
			.setChildOf(backgroundComponent)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)));

		listComponent = GuiHelper.createListComponent(slotBackgroundComponent);
	}

	@Override
	public void onTick() {
		super.onTick();
		final Lift lift = MinecraftClientData.getLift(liftId);
		if (lift == null) {
			MinecraftClient.getInstance().setScreen(null);
			return;
		}

		final ObjectArrayList<ListItem<DashboardListItem>> listItems = new ObjectArrayList<>();
		for (int i = floorLevels.size() - 1; i >= 0; i--) {
			final BlockPos blockPos = floorLevels.get(i);
			final Color color = lift.hasInstruction(lift.getFloorIndex(MTR.blockPosToPosition(blockPos))).contains(LiftDirection.NONE) ? Color.RED : Color.WHITE;
			final int floorIndexFromTop = i;
			final DashboardListItem dashboardListItem = new DashboardListItem(blockPos.asLong(), floorDescriptions.get(i), color.getRGB());
			listItems.add(ListItem.createChild(
				(drawing, x, y) -> drawing.setVerticesWH(x + GuiHelper.DEFAULT_PADDING, y + GuiHelper.DEFAULT_PADDING, GuiHelper.MINECRAFT_FONT_SIZE, GuiHelper.MINECRAFT_FONT_SIZE).setColor(color).draw(),
				null,
				GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE,
				dashboardListItem,
				dashboardListItem.getName(true),
				ObjectArrayList.of(new ObjectObjectImmutablePair<>(GuiHelper.ADD_TEXTURE_ID, (indexList, data) -> onPress(floorIndexFromTop)))
			));
		}

		listComponent.setData(listItems);
	}

	private void onPress(int floorIndexFromTop) {
		final PressLift pressLift = new PressLift();
		pressLift.add(MTR.blockPosToPosition(floorLevels.get(floorIndexFromTop)), LiftDirection.NONE);
		RegistryClient.sendPacketToServer(new PacketPressLiftButton(pressLift));
		MinecraftClient.getInstance().setScreen(null);
	}
}
