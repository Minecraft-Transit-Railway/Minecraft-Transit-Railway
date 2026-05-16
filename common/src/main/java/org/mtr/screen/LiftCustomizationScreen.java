package org.mtr.screen;

import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import gg.essential.universal.UMinecraft;
import net.minecraft.util.math.Direction;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Lift;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Angle;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.render.RenderLifts;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.ButtonComponent;
import org.mtr.widget.CheckboxComponent;

import java.awt.*;

/**
 * Elementa lift customization panel for dimensions, offsets, style and orientation.
 */
public class LiftCustomizationScreen extends SingleTabBackgroundScreenBase implements IGui {

	private Direction liftDirection;
	private final Lift lift;

	private final ButtonComponent buttonHeightMinus;
	private final ButtonComponent buttonHeightAdd;
	private final ButtonComponent buttonWidthMinus;
	private final ButtonComponent buttonWidthAdd;
	private final ButtonComponent buttonDepthMinus;
	private final ButtonComponent buttonDepthAdd;
	private final ButtonComponent buttonOffsetXMinus;
	private final ButtonComponent buttonOffsetXAdd;
	private final ButtonComponent buttonOffsetYMinus;
	private final ButtonComponent buttonOffsetYAdd;
	private final ButtonComponent buttonOffsetZMinus;
	private final ButtonComponent buttonOffsetZAdd;
	private final CheckboxComponent buttonIsDoubleSided;
	private final ButtonComponent buttonLiftStyle;
	private final ButtonComponent buttonRotateAnticlockwise;
	private final ButtonComponent buttonRotateClockwise;

	private final UIWrappedText heightText;
	private final UIWrappedText widthText;
	private final UIWrappedText depthText;
	private final UIWrappedText offsetXText;
	private final UIWrappedText offsetYText;
	private final UIWrappedText offsetZText;

	private static final int MIN_DIMENSION = 2;
	private static final int MAX_DIMENSION = 16;
	private static final int MAX_OFFSET = 16;

	public LiftCustomizationScreen(Lift lift) {
		super(TranslationProvider.GUI_MTR_LIFT_CUSTOMIZATION.getString());
		this.lift = lift;
		liftDirection = Direction.fromHorizontalDegrees(lift.getAngle().angleDegrees);

		heightText = createValueText();
		final org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair<ButtonComponent, ButtonComponent> heightButtons = createAdjustButtonRow(heightText, () -> {
			lift.setDimensions(Math.max(MIN_DIMENSION, lift.getHeight() - 0.5), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		}, () -> {
			lift.setDimensions(Math.min(MAX_DIMENSION, lift.getHeight() + 0.5), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		});
		buttonHeightMinus = heightButtons.left();
		buttonHeightAdd = heightButtons.right();

		widthText = createValueText();
		final org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair<ButtonComponent, ButtonComponent> widthButtons = createAdjustButtonRow(widthText, () -> {
			lift.setDimensions(lift.getHeight(), Math.max(MIN_DIMENSION, lift.getWidth() - 1), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		}, () -> {
			lift.setDimensions(lift.getHeight(), Math.min(MAX_DIMENSION, lift.getWidth() + 1), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		});
		buttonWidthMinus = widthButtons.left();
		buttonWidthAdd = widthButtons.right();

		depthText = createValueText();
		final org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair<ButtonComponent, ButtonComponent> depthButtons = createAdjustButtonRow(depthText, () -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), Math.max(MIN_DIMENSION, lift.getDepth() - 1), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		}, () -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), Math.min(MAX_DIMENSION, lift.getDepth() + 1), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		});
		buttonDepthMinus = depthButtons.left();
		buttonDepthAdd = depthButtons.right();

		offsetXText = createValueText();
		final org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair<ButtonComponent, ButtonComponent> offsetXButtons = createAdjustButtonRow(offsetXText, () -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), Math.max(-MAX_OFFSET, lift.getOffsetX() - 0.5), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		}, () -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), Math.min(MAX_OFFSET, lift.getOffsetX() + 0.5), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		});
		buttonOffsetXMinus = offsetXButtons.left();
		buttonOffsetXAdd = offsetXButtons.right();

		offsetYText = createValueText();
		final org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair<ButtonComponent, ButtonComponent> offsetYButtons = createAdjustButtonRow(offsetYText, () -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), Math.max(-MAX_OFFSET, lift.getOffsetY() - 1), lift.getOffsetZ());
			updateControls(true);
		}, () -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), Math.min(MAX_OFFSET, lift.getOffsetY() + 1), lift.getOffsetZ());
			updateControls(true);
		});
		buttonOffsetYMinus = offsetYButtons.left();
		buttonOffsetYAdd = offsetYButtons.right();

		offsetZText = createValueText();
		final org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair<ButtonComponent, ButtonComponent> offsetZButtons = createAdjustButtonRow(offsetZText, () -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), Math.max(-MAX_OFFSET, lift.getOffsetZ() - 0.5));
			updateControls(true);
		}, () -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), Math.min(MAX_OFFSET, lift.getOffsetZ() + 0.5));
			updateControls(true);
		});
		buttonOffsetZMinus = offsetZButtons.left();
		buttonOffsetZAdd = offsetZButtons.right();

		buttonIsDoubleSided = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint());
		buttonIsDoubleSided.setText(TranslationProvider.GUI_MTR_LIFT_IS_DOUBLE_SIDED.getString());
		buttonIsDoubleSided.onClick(() -> {
			buttonIsDoubleSided.setChecked(!buttonIsDoubleSided.isChecked());
			lift.setIsDoubleSided(buttonIsDoubleSided.isChecked());
			updateControls(true);
		});

		buttonLiftStyle = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());
		buttonLiftStyle.onClick(() -> UMinecraft.setCurrentScreenObj(LiftStyleSelectorScreen.create(lift, this)));

		buttonRotateAnticlockwise = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());
		buttonRotateAnticlockwise.setText(TranslationProvider.GUI_MTR_ROTATE_ANTICLOCKWISE.getString());
		buttonRotateAnticlockwise.onClick(() -> {
			liftDirection = liftDirection.rotateYCounterclockwise();
			lift.setAngle(Angle.fromAngle(liftDirection.getPositiveHorizontalDegrees()));
			updateControls(true);
		});

		buttonRotateClockwise = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());
		buttonRotateClockwise.setText(TranslationProvider.GUI_MTR_ROTATE_CLOCKWISE.getString());
		buttonRotateClockwise.onClick(() -> {
			liftDirection = liftDirection.rotateYClockwise();
			lift.setAngle(Angle.fromAngle(liftDirection.getPositiveHorizontalDegrees()));
			updateControls(true);
		});

		updateControls(false);
	}

	private org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair<ButtonComponent, ButtonComponent> createAdjustButtonRow(UIWrappedText textComponent, Runnable onMinus, Runnable onPlus) {
		final UIContainer row = (UIContainer) new UIContainer()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING / 2F))
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		textComponent.setChildOf(row)
			.setWidth(new RelativeConstraint(0.6F));

		final ButtonComponent minusButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(row)
			.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new PixelConstraint(20));
		minusButton.setText("-");
		if (onMinus != null) {
			minusButton.onClick(onMinus);
		}

		final ButtonComponent plusButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(row)
			.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING / 2F))
			.setWidth(new PixelConstraint(20));
		plusButton.setText("+");
		if (onPlus != null) {
			plusButton.onClick(onPlus);
		}

		return new org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair<>(minusButton, plusButton);
	}

	private UIWrappedText createValueText() {
		return (UIWrappedText) new UIWrappedText("", false)
			.setWidth(new RelativeConstraint())
			.setColor(new Color(GuiHelper.WHITE_COLOR));
	}

	private void updateControls(boolean sendUpdate) {
		buttonHeightMinus.setDisabled(!(lift.getHeight() > MIN_DIMENSION));
		buttonHeightAdd.setDisabled(!(lift.getHeight() < MAX_DIMENSION));
		buttonWidthMinus.setDisabled(!(lift.getWidth() > MIN_DIMENSION));
		buttonWidthAdd.setDisabled(!(lift.getWidth() < MAX_DIMENSION));
		buttonDepthMinus.setDisabled(!(lift.getDepth() > MIN_DIMENSION));
		buttonDepthAdd.setDisabled(!(lift.getDepth() < MAX_DIMENSION));
		buttonOffsetXMinus.setDisabled(!(lift.getOffsetX() > -MAX_OFFSET));
		buttonOffsetXAdd.setDisabled(!(lift.getOffsetX() < MAX_OFFSET));
		buttonOffsetYMinus.setDisabled(!(lift.getOffsetY() > -MAX_OFFSET));
		buttonOffsetYAdd.setDisabled(!(lift.getOffsetY() < MAX_OFFSET));
		buttonOffsetZMinus.setDisabled(!(lift.getOffsetZ() > -MAX_OFFSET));
		buttonOffsetZAdd.setDisabled(!(lift.getOffsetZ() < MAX_OFFSET));
		buttonIsDoubleSided.setChecked(lift.getIsDoubleSided());
		buttonLiftStyle.setText(TranslationProvider.GUI_MTR_LIFT_STYLE.getString(RenderLifts.getLiftResource(lift.getStyle()).getName()));

		heightText.setText(TranslationProvider.TOOLTIP_MTR_RAIL_ACTION_HEIGHT.getString(lift.getHeight()));
		widthText.setText(TranslationProvider.TOOLTIP_MTR_RAIL_ACTION_WIDTH.getString(lift.getWidth()));
		depthText.setText(TranslationProvider.TOOLTIP_MTR_RAIL_ACTION_DEPTH.getString(lift.getDepth()));
		offsetXText.setText(TranslationProvider.GUI_MTR_OFFSET_X.getString(lift.getOffsetX()));
		offsetYText.setText(TranslationProvider.GUI_MTR_OFFSET_Y.getString(lift.getOffsetY()));
		offsetZText.setText(TranslationProvider.GUI_MTR_OFFSET_Z.getString(lift.getOffsetZ()));

		if (sendUpdate) {
			RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addLift(lift)));
		}
	}
}
