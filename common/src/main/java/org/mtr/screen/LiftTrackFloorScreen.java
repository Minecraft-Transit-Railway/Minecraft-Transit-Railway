package org.mtr.screen;

import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.mtr.block.BlockLiftTrackFloor;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateLiftTrackFloorConfig;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.CheckboxComponent;
import org.mtr.widget.TextInputComponent;

/**
 * Elementa-based editor for lift floor number/description metadata.
 */
public final class LiftTrackFloorScreen extends SingleTabBackgroundScreenBase implements IGui {

	private final TextInputComponent textFieldFloorNumber;
	private final TextInputComponent textFieldFloorDescription;
	private final CheckboxComponent checkboxShouldDing;

	private final BlockPos blockPos;

	public LiftTrackFloorScreen(BlockPos blockPos, BlockLiftTrackFloor.LiftTrackFloorBlockEntity blockEntity) {
		super(TranslationProvider.BLOCK_MTR_LIFT_TRACK_FLOOR_1.getString());
		this.blockPos = blockPos;
		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
		final String initialFloorNumber;
		final String initialFloorDescription;
		final boolean initialShouldDing;

		if (clientWorld == null) {
			initialFloorNumber = "1";
			initialFloorDescription = "";
			initialShouldDing = false;
		} else {
			initialFloorNumber = blockEntity.getFloorNumber();
			initialFloorDescription = blockEntity.getFloorDescription();
			initialShouldDing = blockEntity.getShouldDing();
		}

		GuiHelper.createLabel(contentContainer, TranslationProvider.GUI_MTR_LIFT_FLOOR_NUMBER.getString());
		textFieldFloorNumber = (TextInputComponent) new TextInputComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		textFieldFloorNumber.setText(initialFloorNumber);

		GuiHelper.createSpacing(contentContainer);
		GuiHelper.createLabel(contentContainer, TranslationProvider.GUI_MTR_LIFT_FLOOR_DESCRIPTION.getString());
		textFieldFloorDescription = (TextInputComponent) new TextInputComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		textFieldFloorDescription.setText(initialFloorDescription);

		GuiHelper.createSpacing(contentContainer);
		checkboxShouldDing = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		checkboxShouldDing.setText(TranslationProvider.GUI_MTR_LIFT_SHOULD_DING.getString());
		checkboxShouldDing.setChecked(initialShouldDing);
	}

	@Override
	public void onScreenClose() {
		new PacketUpdateLiftTrackFloorConfig(blockPos, textFieldFloorNumber.getText(), textFieldFloorDescription.getText(), checkboxShouldDing.isChecked()).send(MinecraftClient.getInstance().world);
		super.onScreenClose();
	}
}
