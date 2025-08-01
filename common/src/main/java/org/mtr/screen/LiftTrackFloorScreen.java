package org.mtr.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import org.mtr.block.BlockLiftTrackFloor;
import org.mtr.client.IDrawing;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateLiftTrackFloorConfig;
import org.mtr.registry.RegistryClient;
import org.mtr.widget.BetterTextFieldWidget;

public class LiftTrackFloorScreen extends ScreenBase implements IGui {

	private final BetterTextFieldWidget textFieldFloorNumber;
	private final BetterTextFieldWidget textFieldFloorDescription;
	private final CheckboxWidget checkboxShouldDing;

	private final BlockPos blockPos;
	private final String initialFloorNumber;
	private final String initialFloorDescription;
	private final int textWidth;
	private static final MutableText TEXT_FLOOR_NUMBER = TranslationProvider.GUI_MTR_LIFT_FLOOR_NUMBER.getMutableText();
	private static final MutableText TEXT_FLOOR_DESCRIPTION = TranslationProvider.GUI_MTR_LIFT_FLOOR_DESCRIPTION.getMutableText();
	private static final int TEXT_FIELD_WIDTH = 240;

	public LiftTrackFloorScreen(BlockPos blockPos, BlockLiftTrackFloor.LiftTrackFloorBlockEntity blockEntity) {
		super();
		this.blockPos = blockPos;

		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
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

		textFieldFloorNumber = new BetterTextFieldWidget(8, TextCase.DEFAULT, null, "1", text -> {
		});
		textFieldFloorDescription = new BetterTextFieldWidget(256, TextCase.DEFAULT, null, "Concourse", text -> {
		});
		checkboxShouldDing = CheckboxWidget.builder(TranslationProvider.GUI_MTR_LIFT_SHOULD_DING.getText(), textRenderer).checked(initialShouldDing).callback((checkBoxWidget, checked) -> {
		}).build();

		textWidth = Math.max(textRenderer.getWidth(TEXT_FLOOR_NUMBER), textRenderer.getWidth(TEXT_FLOOR_DESCRIPTION));
	}

	@Override
	protected void init() {
		super.init();

		final int startX = (width - textWidth - TEXT_PADDING - TEXT_FIELD_WIDTH) / 2;
		final int startY = (height - SQUARE_SIZE * 3 - TEXT_FIELD_PADDING * 2) / 2;
		IDrawing.setPositionAndWidth(textFieldFloorNumber, startX + textWidth + TEXT_PADDING + TEXT_FIELD_PADDING / 2, startY + TEXT_FIELD_PADDING / 2, TEXT_FIELD_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldFloorDescription, startX + textWidth + TEXT_PADDING + TEXT_FIELD_PADDING / 2, startY + SQUARE_SIZE + TEXT_FIELD_PADDING * 3 / 2, TEXT_FIELD_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(checkboxShouldDing, startX, startY + SQUARE_SIZE * 2 + TEXT_FIELD_PADDING * 2, TEXT_FIELD_WIDTH);

		textFieldFloorNumber.setText(initialFloorNumber);
		textFieldFloorDescription.setText(initialFloorDescription);

		addDrawableChild(textFieldFloorNumber);
		addDrawableChild(textFieldFloorDescription);
//		addDrawableChild(checkboxShouldDing);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		final int startX = (width - textWidth - TEXT_PADDING - TEXT_FIELD_WIDTH) / 2;
		final int startY = (height - SQUARE_SIZE * 3 - TEXT_FIELD_PADDING * 2) / 2;
		context.drawText(textRenderer, TEXT_FLOOR_NUMBER, startX, startY + TEXT_FIELD_PADDING / 2 + TEXT_PADDING, ARGB_WHITE, false);
		context.drawText(textRenderer, TEXT_FLOOR_DESCRIPTION, startX, startY + SQUARE_SIZE + TEXT_FIELD_PADDING * 3 / 2 + TEXT_PADDING, ARGB_WHITE, false);
		super.render(context, mouseX, mouseY, delta);
	}

	@Override
	public void close() {
		RegistryClient.sendPacketToServer(new PacketUpdateLiftTrackFloorConfig(blockPos, textFieldFloorNumber.getText(), textFieldFloorDescription.getText(), checkboxShouldDing.isChecked()));
		super.close();
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
