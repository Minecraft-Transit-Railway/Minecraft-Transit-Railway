package org.mtr.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTRClient;
import org.mtr.block.BlockDriverKeyDispenser;
import org.mtr.client.IDrawing;
import org.mtr.core.data.Depot;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateKeyDispenserConfig;
import org.mtr.registry.RegistryClient;
import org.mtr.widget.BetterTextFieldWidget;

public class DriverKeyDispenserScreen extends ScreenBase implements IGui {

	private final CheckboxWidget checkBoxDispenseBasicDriverKey;
	private final CheckboxWidget checkBoxDispenseAdvancedDriverKey;
	private final CheckboxWidget checkBoxDispenseGuardKey;
	private final BetterTextFieldWidget textFieldTimeoutHours;
	private final BetterTextFieldWidget textFieldTimeoutMinutes;
	private final BlockPos blockPos;
	private final long timeout;

	private static final int TEXT_FIELD_SIZE = 60;

	public DriverKeyDispenserScreen(BlockPos blockPos, BlockDriverKeyDispenser.DriverKeyDispenserBlockEntity blockEntity) {
		super();
		this.blockPos = blockPos;
		timeout = blockEntity.getTimeout();

		checkBoxDispenseBasicDriverKey = CheckboxWidget.builder(TranslationProvider.ITEM_MTR_BASIC_DRIVER_KEY.getText(), textRenderer).checked(blockEntity.getDispenseBasicDriverKey()).build();
		checkBoxDispenseAdvancedDriverKey = CheckboxWidget.builder(TranslationProvider.ITEM_MTR_ADVANCED_DRIVER_KEY.getText(), textRenderer).checked(blockEntity.getDispenseAdvancedDriverKey()).build();
		checkBoxDispenseGuardKey = CheckboxWidget.builder(TranslationProvider.ITEM_MTR_GUARD_KEY.getText(), textRenderer).checked(blockEntity.getDispenseGuardKey()).build();
		textFieldTimeoutHours = new BetterTextFieldWidget(4, TextCase.DEFAULT, "\\D", "1", text -> {
		});
		textFieldTimeoutMinutes = new BetterTextFieldWidget(2, TextCase.DEFAULT, "\\D", "0", text -> {
		});
	}

	@Override
	protected void init() {
		super.init();

		IDrawing.setPositionAndWidth(checkBoxDispenseBasicDriverKey, SQUARE_SIZE, SQUARE_SIZE * 2, width - SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(checkBoxDispenseAdvancedDriverKey, SQUARE_SIZE, SQUARE_SIZE * 3, width - SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(checkBoxDispenseGuardKey, SQUARE_SIZE, SQUARE_SIZE * 4, width - SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(textFieldTimeoutHours, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 6 + TEXT_FIELD_PADDING / 2, TEXT_FIELD_SIZE - TEXT_FIELD_PADDING);
		textFieldTimeoutHours.setText(String.valueOf(timeout / Depot.MILLIS_PER_HOUR));
		IDrawing.setPositionAndWidth(textFieldTimeoutMinutes, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 3 / 2, TEXT_FIELD_SIZE - TEXT_FIELD_PADDING);
		textFieldTimeoutMinutes.setText(String.valueOf((timeout % Depot.MILLIS_PER_HOUR) / 60 / Depot.MILLIS_PER_SECOND));

		addDrawableChild(checkBoxDispenseBasicDriverKey);
		addDrawableChild(checkBoxDispenseAdvancedDriverKey);
		addDrawableChild(checkBoxDispenseGuardKey);
		addDrawableChild(textFieldTimeoutHours);
		addDrawableChild(textFieldTimeoutMinutes);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		super.render(context, mouseX, mouseY, delta);

		final Depot depot = MTRClient.findDepot(blockPos);
		context.drawText(textRenderer, depot == null ? TranslationProvider.GUI_MTR_DRIVING_KEYS_DEPOT_NOT_FOUND.getMutableText().formatted(Formatting.RED) : TranslationProvider.GUI_MTR_DRIVING_KEYS_FOR_DEPOT.getMutableText(depot.getName()), SQUARE_SIZE, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE, false);
		context.drawText(textRenderer, TranslationProvider.GUI_MTR_DRIVER_KEY_TIMEOUT.getMutableText(), SQUARE_SIZE, SQUARE_SIZE * 5 + TEXT_PADDING, ARGB_WHITE, false);
		context.drawText(textRenderer, TranslationProvider.GUI_MTR_HOURS.getMutableText(""), SQUARE_SIZE + TEXT_FIELD_SIZE, SQUARE_SIZE * 6 + TEXT_FIELD_PADDING / 2 + TEXT_PADDING, ARGB_WHITE, false);
		context.drawText(textRenderer, TranslationProvider.GUI_MTR_MINUTES.getMutableText(""), SQUARE_SIZE + TEXT_FIELD_SIZE, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 3 / 2 + TEXT_PADDING, ARGB_WHITE, false);
	}

	@Override
	public void close() {
		long newTimeout = Depot.MILLIS_PER_HOUR;
		try {
			newTimeout = Long.parseLong(textFieldTimeoutHours.getText()) * Depot.MILLIS_PER_HOUR + Long.parseLong(textFieldTimeoutMinutes.getText()) * 60 * Depot.MILLIS_PER_SECOND;
		} catch (Exception ignored) {
		}
		RegistryClient.sendPacketToServer(new PacketUpdateKeyDispenserConfig(
				blockPos,
				checkBoxDispenseBasicDriverKey.isChecked(),
				checkBoxDispenseAdvancedDriverKey.isChecked(),
				checkBoxDispenseGuardKey.isChecked(),
				newTimeout
		));
		super.close();
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
