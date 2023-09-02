package org.mtr.mod.screen;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.registry.RegistryClient;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.block.BlockLiftTrackFloor;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.packet.IPacket;
import org.mtr.mod.packet.PacketUpdateLiftTrackFloorConfig;

public class LiftTrackFloorScreen extends ScreenExtension implements IGui, IPacket {

	private final TextFieldWidgetExtension textFieldFloorNumber;
	private final TextFieldWidgetExtension textFieldFloorDescription;
	private final CheckboxWidgetExtension checkboxShouldDing;

	private final BlockPos blockPos;
	private final String initialFloorNumber;
	private final String initialFloorDescription;
	private final boolean initialShouldDing;
	private final int textWidth;
	private static final MutableText TEXT_FLOOR_NUMBER = TextHelper.translatable("gui.mtr.lift_floor_number");
	private static final MutableText TEXT_FLOOR_DESCRIPTION = TextHelper.translatable("gui.mtr.lift_floor_description");
	private static final int TEXT_FIELD_WIDTH = 240;

	public LiftTrackFloorScreen(BlockPos blockPos) {
		super();
		this.blockPos = blockPos;

		textFieldFloorNumber = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 8, TextCase.DEFAULT, null, "1");
		textFieldFloorDescription = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 256, TextCase.DEFAULT, null, "Concourse");
		checkboxShouldDing = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
		});
		checkboxShouldDing.setMessage2(new Text(TextHelper.translatable("gui.mtr.lift_should_ding").data));

		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			initialFloorNumber = "1";
			initialFloorDescription = "";
			initialShouldDing = false;
		} else {
			final BlockEntity blockEntity = clientWorld.getBlockEntity(blockPos);
			if (blockEntity != null && blockEntity.data instanceof BlockLiftTrackFloor.BlockEntity) {
				initialFloorNumber = ((BlockLiftTrackFloor.BlockEntity) blockEntity.data).getFloorNumber();
				initialFloorDescription = ((BlockLiftTrackFloor.BlockEntity) blockEntity.data).getFloorDescription();
				initialShouldDing = ((BlockLiftTrackFloor.BlockEntity) blockEntity.data).getShouldDing();
			} else {
				initialFloorNumber = "1";
				initialFloorDescription = "";
				initialShouldDing = false;
			}
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
		addChild(new ClickableWidget(checkboxShouldDing));
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
		graphicsHolder.drawText(TEXT_FLOOR_NUMBER, startX, startY + TEXT_FIELD_PADDING / 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		graphicsHolder.drawText(TEXT_FLOOR_DESCRIPTION, startX, startY + SQUARE_SIZE + TEXT_FIELD_PADDING * 3 / 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		super.render(graphicsHolder, mouseX, mouseY, delta);
	}

	@Override
	public void onClose2() {
		RegistryClient.sendPacketToServer(new PacketUpdateLiftTrackFloorConfig(blockPos, textFieldFloorNumber.getText2(), textFieldFloorDescription.getText2(), checkboxShouldDing.isChecked2()));
		super.onClose2();
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}
}
