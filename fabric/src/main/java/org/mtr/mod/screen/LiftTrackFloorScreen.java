package org.mtr.mod.screen;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.CheckboxWidgetExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.TextFieldWidgetExtension;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockLiftTrackFloor;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketUpdateLiftTrackFloorConfig;

public class LiftTrackFloorScreen extends MTRScreenBase implements IGui {

	private final TextFieldWidgetExtension textFieldFloorNumber;
	private final TextFieldWidgetExtension textFieldFloorDescription;
	private final CheckboxWidgetExtension checkboxShouldDing;

	private final BlockPos blockPos;
	private final String initialFloorNumber;
	private final String initialFloorDescription;
	private final boolean initialShouldDing;
	private final int textWidth;
	private static final MutableText TEXT_FLOOR_NUMBER = TranslationProvider.GUI_MTR_LIFT_FLOOR_NUMBER.getMutableText();
	private static final MutableText TEXT_FLOOR_DESCRIPTION = TranslationProvider.GUI_MTR_LIFT_FLOOR_DESCRIPTION.getMutableText();
	private static final int TEXT_FIELD_WIDTH = 240;

	public LiftTrackFloorScreen(BlockPos blockPos, BlockLiftTrackFloor.BlockEntity blockEntity) {
		super();
		this.blockPos = blockPos;

		textFieldFloorNumber = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 8, TextCase.DEFAULT, null, "1");
		textFieldFloorDescription = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 256, TextCase.DEFAULT, null, "Concourse");
		checkboxShouldDing = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
		});
		checkboxShouldDing.setMessage2(TranslationProvider.GUI_MTR_LIFT_SHOULD_DING.getText());

		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			initialFloorNumber = "1";
			initialFloorDescription = "";
			initialShouldDing = false;
		} else {
			initialFloorNumber = blockEntity.getFloorNumber();
			initialFloorDescription = blockEntity.getFloorDescription();
			initialShouldDing = blockEntity.getShouldDing();
		}

		textWidth = Math.max(GraphicsHolder.getTextWidth(TEXT_FLOOR_NUMBER), GraphicsHolder.getTextWidth(TEXT_FLOOR_DESCRIPTION));
	}

	@Override
	protected void init2() {
		super.init2();

		final int startX = (width - textWidth - TEXT_PADDING - TEXT_FIELD_WIDTH) / 2;
		final int startY = (height - SQUARE_SIZE * 3 - TEXT_FIELD_PADDING * 2) / 2;
		IDrawing.setPositionAndWidth(textFieldFloorNumber, startX + textWidth + TEXT_PADDING + TEXT_FIELD_PADDING / 2, startY + TEXT_FIELD_PADDING / 2, TEXT_FIELD_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldFloorDescription, startX + textWidth + TEXT_PADDING + TEXT_FIELD_PADDING / 2, startY + SQUARE_SIZE + TEXT_FIELD_PADDING * 3 / 2, TEXT_FIELD_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(checkboxShouldDing, startX, startY + SQUARE_SIZE * 2 + TEXT_FIELD_PADDING * 2, TEXT_FIELD_WIDTH);

		textFieldFloorNumber.setText2(initialFloorNumber);
		textFieldFloorDescription.setText2(initialFloorDescription);
		checkboxShouldDing.setChecked(initialShouldDing);

		addChild(new ClickableWidget(textFieldFloorNumber));
		addChild(new ClickableWidget(textFieldFloorDescription));
//		addChild(new ClickableWidget(checkboxShouldDing));
	}

	@Override
	public void tick2() {
		textFieldFloorNumber.tick2();
		textFieldFloorDescription.tick2();
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		renderBackground(graphicsHolder);
		final int startX = (width - textWidth - TEXT_PADDING - TEXT_FIELD_WIDTH) / 2;
		final int startY = (height - SQUARE_SIZE * 3 - TEXT_FIELD_PADDING * 2) / 2;
		graphicsHolder.drawText(TEXT_FLOOR_NUMBER, startX, startY + TEXT_FIELD_PADDING / 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.drawText(TEXT_FLOOR_DESCRIPTION, startX, startY + SQUARE_SIZE + TEXT_FIELD_PADDING * 3 / 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		super.render(graphicsHolder, mouseX, mouseY, delta);
	}

	@Override
	public void onClose2() {
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateLiftTrackFloorConfig(blockPos, textFieldFloorNumber.getText2(), textFieldFloorDescription.getText2(), checkboxShouldDing.isChecked2()));
		super.onClose2();
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}
}
