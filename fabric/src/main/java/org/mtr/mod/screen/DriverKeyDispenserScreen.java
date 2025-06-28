package org.mtr.mod.screen;

import org.mtr.core.data.Depot;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.TextFormatting;
import org.mtr.mapping.mapper.CheckboxWidgetExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.TextFieldWidgetExtension;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockDriverKeyDispenser;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketUpdateKeyDispenserConfig;

public class DriverKeyDispenserScreen extends MTRScreenBase implements IGui {

	private final CheckboxWidgetExtension checkBoxDispenseBasicDriverKey;
	private final CheckboxWidgetExtension checkBoxDispenseAdvancedDriverKey;
	private final CheckboxWidgetExtension checkBoxDispenseGuardKey;
	private final TextFieldWidgetExtension textFieldTimeoutHours;
	private final TextFieldWidgetExtension textFieldTimeoutMinutes;
	private final BlockPos blockPos;
	private final long timeout;

	private static final int TEXT_FIELD_SIZE = 60;

	public DriverKeyDispenserScreen(BlockPos blockPos, BlockDriverKeyDispenser.BlockEntity blockEntity) {
		super();
		this.blockPos = blockPos;
		timeout = blockEntity.getTimeout();

		checkBoxDispenseBasicDriverKey = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
		});
		checkBoxDispenseBasicDriverKey.setMessage2(TranslationProvider.ITEM_MTR_BASIC_DRIVER_KEY.getText());
		checkBoxDispenseBasicDriverKey.setChecked(blockEntity.getDispenseBasicDriverKey());

		checkBoxDispenseAdvancedDriverKey = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
		});
		checkBoxDispenseAdvancedDriverKey.setMessage2(TranslationProvider.ITEM_MTR_ADVANCED_DRIVER_KEY.getText());
		checkBoxDispenseAdvancedDriverKey.setChecked(blockEntity.getDispenseAdvancedDriverKey());

		checkBoxDispenseGuardKey = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
		});
		checkBoxDispenseGuardKey.setMessage2(TranslationProvider.ITEM_MTR_GUARD_KEY.getText());
		checkBoxDispenseGuardKey.setChecked(blockEntity.getDispenseGuardKey());

		textFieldTimeoutHours = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 4, TextCase.DEFAULT, "\\D", "1");
		textFieldTimeoutMinutes = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 2, TextCase.DEFAULT, "\\D", "0");
	}

	@Override
	protected void init2() {
		super.init2();

		IDrawing.setPositionAndWidth(checkBoxDispenseBasicDriverKey, SQUARE_SIZE, SQUARE_SIZE * 2, width - SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(checkBoxDispenseAdvancedDriverKey, SQUARE_SIZE, SQUARE_SIZE * 3, width - SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(checkBoxDispenseGuardKey, SQUARE_SIZE, SQUARE_SIZE * 4, width - SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(textFieldTimeoutHours, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 6 + TEXT_FIELD_PADDING / 2, TEXT_FIELD_SIZE - TEXT_FIELD_PADDING);
		textFieldTimeoutHours.setText2(String.valueOf(timeout / Depot.MILLIS_PER_HOUR));
		IDrawing.setPositionAndWidth(textFieldTimeoutMinutes, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 3 / 2, TEXT_FIELD_SIZE - TEXT_FIELD_PADDING);
		textFieldTimeoutMinutes.setText2(String.valueOf((timeout % Depot.MILLIS_PER_HOUR) / 60 / Depot.MILLIS_PER_SECOND));

		addChild(new ClickableWidget(checkBoxDispenseBasicDriverKey));
		addChild(new ClickableWidget(checkBoxDispenseAdvancedDriverKey));
		addChild(new ClickableWidget(checkBoxDispenseGuardKey));
		addChild(new ClickableWidget(textFieldTimeoutHours));
		addChild(new ClickableWidget(textFieldTimeoutMinutes));
	}

	@Override
	public void tick2() {
		super.tick2();
		textFieldTimeoutHours.tick2();
		textFieldTimeoutMinutes.tick2();
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		renderBackground(graphicsHolder);
		super.render(graphicsHolder, mouseX, mouseY, delta);

		final Depot depot = InitClient.findDepot(blockPos);
		graphicsHolder.drawText(depot == null ? TranslationProvider.GUI_MTR_DRIVING_KEYS_DEPOT_NOT_FOUND.getMutableText().formatted(TextFormatting.RED) : TranslationProvider.GUI_MTR_DRIVING_KEYS_FOR_DEPOT.getMutableText(depot.getName()), SQUARE_SIZE, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.drawText(TranslationProvider.GUI_MTR_DRIVER_KEY_TIMEOUT.getMutableText(), SQUARE_SIZE, SQUARE_SIZE * 5 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.drawText(TranslationProvider.GUI_MTR_HOURS.getMutableText(""), SQUARE_SIZE + TEXT_FIELD_SIZE, SQUARE_SIZE * 6 + TEXT_FIELD_PADDING / 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.drawText(TranslationProvider.GUI_MTR_MINUTES.getMutableText(""), SQUARE_SIZE + TEXT_FIELD_SIZE, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 3 / 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
	}

	@Override
	public void onClose2() {
		long newTimeout = Depot.MILLIS_PER_HOUR;
		try {
			newTimeout = Long.parseLong(textFieldTimeoutHours.getText2()) * Depot.MILLIS_PER_HOUR + Long.parseLong(textFieldTimeoutMinutes.getText2()) * 60 * Depot.MILLIS_PER_SECOND;
		} catch (Exception ignored) {
		}
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateKeyDispenserConfig(
				blockPos,
				checkBoxDispenseBasicDriverKey.isChecked2(),
				checkBoxDispenseAdvancedDriverKey.isChecked2(),
				checkBoxDispenseGuardKey.isChecked2(),
				newTimeout
		));
		super.onClose2();
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}
}
