package org.mtr.screen;

import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.mtr.block.BlockSignalBase;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
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
public class SignalColorScreen extends SingleTabBackgroundScreenBase implements IGui {

	private final CheckboxComponent checkBoxAcceptRedstone;
	private final CheckboxComponent checkBoxOutputRedstone;
	private final CheckboxComponent checkBoxSelectAll;
	private final CheckboxComponent[] checkBoxes = new CheckboxComponent[ItemSignalModifier.COLORS.length];
	private final BlockPos blockPos;
	private final IntAVLTreeSet signalColors;
	private final boolean isBackSide;

	public SignalColorScreen(BlockPos blockPos, BlockSignalBase.BlockEntityBase blockEntity) {
		super(TranslationProvider.GUI_MTR_SIGNAL_OPTIONS.getString());
		this.blockPos = blockPos;
		final IntAVLTreeSet detectedColors = new IntAVLTreeSet();

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientWorld clientWorld = minecraftClient.world;
		if (clientWorld == null) {
			signalColors = new IntAVLTreeSet();
			isBackSide = false;
		} else {
			final float angle = BlockSignalBase.getAngle(clientWorld.getBlockState(blockPos));
			final ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
			if (clientPlayerEntity == null) {
				isBackSide = false;
			} else {
				isBackSide = blockEntity.isDoubleSided && Math.abs(Utilities.circularDifference(Math.round(clientPlayerEntity.getYaw()), Math.round(angle), 360)) > 90;
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

		for (int i = 0; i < ItemSignalModifier.COLORS.length; i++) {
			final int color = ItemSignalModifier.COLORS[i];
			final UIContainer row = (UIContainer) new UIContainer()
				.setChildOf(contentContainer)
				.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING / 2F))
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(20));

			checkBoxes[i] = (CheckboxComponent) new CheckboxComponent()
				.setChildOf(row)
				.setWidth(new RelativeConstraint());
			checkBoxes[i].setText(String.format("#%06X", color & 0xFFFFFF));
			checkBoxes[i].onClick(() -> {
				if (signalColors.contains(color)) {
					signalColors.remove(color);
				} else {
					signalColors.add(color);
				}
				setButtons();
			});

			new UIBlock(new Color(color | ARGB_BLACK))
				.setChildOf(row)
				.setX(new SiblingConstraint())
				.setY(new PixelConstraint(6))
				.setWidth(new PixelConstraint(detectedColors.contains(color) ? 14 : 2))
				.setHeight(new PixelConstraint(8));
		}

		setButtons();
	}

	@Override
	public void onScreenClose() {
		new PacketUpdateSignalConfig(blockPos, checkBoxAcceptRedstone.isChecked(), checkBoxOutputRedstone.isChecked(), signalColors, isBackSide).send(MinecraftClient.getInstance().world);
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

	private void setButtons() {
		for (int i = 0; i < ItemSignalModifier.COLORS.length; i++) {
			checkBoxes[i].setChecked(signalColors.contains(ItemSignalModifier.COLORS[i]));
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
