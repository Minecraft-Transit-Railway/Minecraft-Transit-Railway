package org.mtr.screen;

import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import gg.essential.elementa.constraints.SubtractiveConstraint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import org.mtr.block.BlockSignalBase;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.item.ItemSignalModifier;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import org.mtr.packet.PacketUpdateSignalConfig;
import org.mtr.render.RenderSignalBase;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.CheckboxComponent;

import java.awt.*;

/**
 * Elementa screen for selecting enabled signal colors and redstone behavior.
 */
public class SignalColorScreen extends SingleTabBackgroundScreenBase {

	private final CheckboxComponent checkBoxAcceptRedstone;
	private final CheckboxComponent checkBoxOutputRedstone;
	private final CheckboxComponent checkBoxSelectAll;
	private final CheckboxComponent[] basicCheckBoxes = new CheckboxComponent[ItemSignalModifier.COLORS.length];
	private final CheckboxComponent[] advancedCheckBoxes = new CheckboxComponent[ItemSignalModifier.COLORS.length];
	private final BlockPos blockPos;
	private final IntAVLTreeSet signalColors;
	private final boolean isBackSide;

	public SignalColorScreen(BlockPos blockPos, BlockSignalBase.BlockEntityBase blockEntity) {
		super(TranslationProvider.GUI_MTR_SIGNAL_OPTIONS.getString());
		this.blockPos = blockPos;
		final IntAVLTreeSet detectedColors = new IntAVLTreeSet();

		final Minecraft minecraftClient = Minecraft.getInstance();
		final ClientLevel clientWorld = minecraftClient.level;
		if (clientWorld == null) {
			signalColors = new IntAVLTreeSet();
			isBackSide = false;
		} else {
			final float angle = BlockSignalBase.getAngle(clientWorld.getBlockState(blockPos));
			final LocalPlayer clientPlayerEntity = minecraftClient.player;
			if (clientPlayerEntity == null) {
				isBackSide = false;
			} else {
				isBackSide = blockEntity.isDoubleSided && Math.abs(Utilities.circularDifference(Math.round(clientPlayerEntity.getYRot()), Math.round(angle), 360)) > 90;
			}

			signalColors = new IntAVLTreeSet(blockEntity.getSignalColors(isBackSide));
			final RenderSignalBase.AspectState aspectState = RenderSignalBase.getAspectState(blockPos, angle + (isBackSide ? 180 : 0) + 90);
			detectedColors.addAll(aspectState == null ? new IntAVLTreeSet() : aspectState.detectedColors);
		}

		final CheckboxComponent newCheckBoxAcceptRedstone = createMainCheckbox(TranslationProvider.GUI_MTR_ACCEPT_REDSTONE.getString() + " (BETA)", blockEntity.getAcceptRedstone());
		newCheckBoxAcceptRedstone.onClick(() -> {
			newCheckBoxAcceptRedstone.setChecked(!newCheckBoxAcceptRedstone.isChecked());
			toggleRedstoneCheckboxes1();
		});
		checkBoxAcceptRedstone = newCheckBoxAcceptRedstone;

		final CheckboxComponent newCheckBoxOutputRedstone = createMainCheckbox(TranslationProvider.GUI_MTR_OUTPUT_REDSTONE.getString() + " (BETA)", blockEntity.getOutputRedstone());
		newCheckBoxOutputRedstone.onClick(() -> {
			newCheckBoxOutputRedstone.setChecked(!newCheckBoxOutputRedstone.isChecked());
			toggleRedstoneCheckboxes2();
		});
		checkBoxOutputRedstone = newCheckBoxOutputRedstone;

		checkBoxSelectAll = createMainCheckbox(TranslationProvider.GUI_MTR_SELECT_ALL.getString(), signalColors.isEmpty());
		checkBoxSelectAll.onClick(() -> {
			if (signalColors.isEmpty()) {
				signalColors.add(ItemSignalModifier.COLORS[0]);
			} else {
				signalColors.clear();
			}
			setButtons();
		});

		final int checkBoxesHeight = GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT + GuiHelper.DEFAULT_PADDING + 20 * ItemSignalModifier.COLORS.length;
		GuiHelper.createSpacing(contentContainer);

		final UIContainer row = (UIContainer) new UIContainer()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(checkBoxesHeight));

		createCheckboxes(
			TranslationProvider.GUI_MTR_BASIC_SIGNAL_COLORS,
			(UIContainer) new UIContainer().setChildOf(row).setWidth(new SubtractiveConstraint(new RelativeConstraint(0.5F), new PixelConstraint(GuiHelper.DEFAULT_PADDING))).setHeight(new PixelConstraint(checkBoxesHeight)),
			basicCheckBoxes,
			false,
			detectedColors
		);

		createCheckboxes(
			TranslationProvider.GUI_MTR_ADVANCED_SIGNAL_COLORS,
			(UIContainer) new UIContainer().setChildOf(row).setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING)).setWidth(new SubtractiveConstraint(new RelativeConstraint(0.5F), new PixelConstraint(GuiHelper.DEFAULT_PADDING))).setHeight(new PixelConstraint(checkBoxesHeight)),
			advancedCheckBoxes,
			true,
			detectedColors
		);

		setButtons();
	}

	@Override
	public void onScreenClose() {
		new PacketUpdateSignalConfig(blockPos, checkBoxAcceptRedstone.isChecked(), checkBoxOutputRedstone.isChecked(), signalColors, isBackSide).send(Minecraft.getInstance().level);
		super.onScreenClose();
	}

	private CheckboxComponent createMainCheckbox(String text, boolean checked) {
		final CheckboxComponent checkboxComponent = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());
		checkboxComponent.setText(text);
		checkboxComponent.setChecked(checked);
		return checkboxComponent;
	}

	private void createCheckboxes(TranslationProvider.TranslationHolder title, UIContainer container, CheckboxComponent[] checkboxes, boolean isAdvanced, IntAVLTreeSet detectedColors) {
		new UIText(title.getString(), false)
			.setChildOf(container)
			.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		GuiHelper.createSpacing(container);

		for (int i = 0; i < ItemSignalModifier.COLORS.length; i++) {
			final int color = isAdvanced ? (ItemSignalModifier.COLORS[i] | 0xFF000000) : (ItemSignalModifier.COLORS[i] & 0x00FFFFFF);

			final UIContainer row = (UIContainer) new UIContainer()
				.setChildOf(container)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(20));

			new UIBlock(new Color(color))
				.setChildOf(row)
				.setWidth(detectedColors.contains(color) ? new RelativeConstraint() : new PixelConstraint(24))
				.setHeight(new PixelConstraint(20));

			checkboxes[i] = (CheckboxComponent) new CheckboxComponent()
				.setChildOf(row)
				.setWidth(new RelativeConstraint());
			checkboxes[i].onClick(() -> {
				if (signalColors.contains(color)) {
					signalColors.remove(color);
				} else {
					signalColors.add(color);
				}
				setButtons();
			});
		}
	}

	private void setButtons() {
		for (int i = 0; i < ItemSignalModifier.COLORS.length; i++) {
			basicCheckBoxes[i].setChecked(signalColors.contains(ItemSignalModifier.COLORS[i] & 0x00FFFFFF));
			advancedCheckBoxes[i].setChecked(signalColors.contains(ItemSignalModifier.COLORS[i] | 0xFF000000));
		}
		checkBoxSelectAll.setChecked(signalColors.isEmpty());
	}

	private void toggleRedstoneCheckboxes1() {
		if (checkBoxAcceptRedstone.isChecked()) {
			checkBoxOutputRedstone.setChecked(false);
		}
	}

	private void toggleRedstoneCheckboxes2() {
		if (checkBoxOutputRedstone.isChecked()) {
			checkBoxAcceptRedstone.setChecked(false);
		}
	}
}
