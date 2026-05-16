package org.mtr.screen;

import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTRClient;
import org.mtr.block.BlockDriverKeyDispenser;
import org.mtr.core.data.Depot;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateKeyDispenserConfig;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.CheckboxComponent;
import org.mtr.widget.NumberInputComponent;

import java.awt.*;

public final class DriverKeyDispenserScreen extends SingleTabBackgroundScreenBase implements IGui {

	private final CheckboxComponent dispenseBasicDriverKeyCheckbox;
	private final CheckboxComponent dispenseAdvancedDriverKeyCheckbox;
	private final CheckboxComponent dispenseGuardKeyCheckbox;
	private final NumberInputComponent timeoutHoursInputComponent;
	private final NumberInputComponent timeoutMinutesInputComponent;
	private final BlockPos blockPos;

	private static final int LEFT_WIDTH = 96;

	public DriverKeyDispenserScreen(BlockPos blockPos, BlockDriverKeyDispenser.DriverKeyDispenserBlockEntity blockEntity) {
		super(TranslationProvider.BLOCK_MTR_DRIVER_KEY_DISPENSER.getString());
		this.blockPos = blockPos;

		final long timeout = blockEntity.getTimeout();
		final Depot depot = MTRClient.findDepot(blockPos);
		final UIWrappedText topLabel = GuiHelper.createLabel(contentContainer, depot == null ? TranslationProvider.GUI_MTR_DRIVING_KEYS_DEPOT_NOT_FOUND.getString() : TranslationProvider.GUI_MTR_DRIVING_KEYS_FOR_DEPOT.getString(depot.getName()));

		if (depot == null) {
			topLabel.setColor(Color.RED);
		}

		GuiHelper.createSpacing(contentContainer);

		dispenseBasicDriverKeyCheckbox = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		dispenseBasicDriverKeyCheckbox.setText(TranslationProvider.ITEM_MTR_BASIC_DRIVER_KEY.getString());
		dispenseBasicDriverKeyCheckbox.setChecked(blockEntity.getDispenseBasicDriverKey());

		dispenseAdvancedDriverKeyCheckbox = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		dispenseAdvancedDriverKeyCheckbox.setText(TranslationProvider.ITEM_MTR_ADVANCED_DRIVER_KEY.getString());
		dispenseAdvancedDriverKeyCheckbox.setChecked(blockEntity.getDispenseAdvancedDriverKey());

		dispenseGuardKeyCheckbox = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		dispenseGuardKeyCheckbox.setText(TranslationProvider.ITEM_MTR_GUARD_KEY.getString());
		dispenseGuardKeyCheckbox.setChecked(blockEntity.getDispenseGuardKey());

		GuiHelper.createSpacing(contentContainer);
		GuiHelper.createLabel(contentContainer, TranslationProvider.GUI_MTR_DRIVER_KEY_TIMEOUT.getString());

		timeoutHoursInputComponent = (NumberInputComponent) new NumberInputComponent(0, 24 * 7, 1, false, null)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new PixelConstraint(LEFT_WIDTH));

		timeoutHoursInputComponent.setSuffix(TranslationProvider.GUI_MTR_HOURS.getString(""));
		timeoutHoursInputComponent.setValue((double) (timeout / Depot.MILLIS_PER_HOUR));

		timeoutMinutesInputComponent = (NumberInputComponent) new NumberInputComponent(0, 59, 1, false, null)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new PixelConstraint(LEFT_WIDTH));

		timeoutMinutesInputComponent.setSuffix(TranslationProvider.GUI_MTR_MINUTES.getString(""));
		timeoutMinutesInputComponent.setValue((double) ((timeout % Depot.MILLIS_PER_HOUR) / 60 / Depot.MILLIS_PER_SECOND));
	}

	@Override
	public void onScreenClose() {
		new PacketUpdateKeyDispenserConfig(
			blockPos,
			dispenseBasicDriverKeyCheckbox.isChecked(),
			dispenseAdvancedDriverKeyCheckbox.isChecked(),
			dispenseGuardKeyCheckbox.isChecked(),
			Math.round(timeoutHoursInputComponent.getValue() * Depot.MILLIS_PER_HOUR + timeoutMinutesInputComponent.getValue() * 60 * Depot.MILLIS_PER_SECOND)
		).send(MinecraftClient.getInstance().world);
		super.onScreenClose();
	}
}
