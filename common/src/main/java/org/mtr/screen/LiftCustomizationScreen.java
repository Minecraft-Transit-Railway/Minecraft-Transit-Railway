package org.mtr.screen;

import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.UMinecraft;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Lift;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Angle;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.tool.GuiHelper;
import org.mtr.tool.ReleasedDynamicTextureRegistry;
import org.mtr.widget.*;

import java.awt.*;

/**
 * Elementa lift customization panel for dimensions, offsets, style and orientation.
 */
public final class LiftCustomizationScreen extends WindowBase {

	private Direction liftDirection;
	private final Lift lift;

	private final NumberInputComponent dimensionXInputComponent;
	private final NumberInputComponent dimensionYInputComponent;
	private final NumberInputComponent dimensionZInputComponent;
	private final NumberInputComponent offsetXInputComponent;
	private final NumberInputComponent offsetYInputComponent;
	private final NumberInputComponent offsetZInputComponent;
	private final CheckboxComponent isDoubleSidedCheckboxComponent;

	private static final int MIN_DIMENSION = 2;
	private static final int MAX_DIMENSION = 16;
	private static final int MAX_OFFSET = 16;
	private static final int LEFT_WIDTH = 96;

	public LiftCustomizationScreen(Lift lift) {
		this.lift = lift;
		liftDirection = Direction.fromHorizontalDegrees(lift.getAngle().angleDegrees);

		final BackgroundComponent backgroundComponent = new BackgroundComponent(getWindow(), ObjectImmutableList.of(
			new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.BRUSH_TEXTURE.get(), TranslationProvider.GUI_MTR_LIFT_CUSTOMIZATION.getString())
		));
		final UIContainer leftContainer = (UIContainer) new UIContainer()
			.setChildOf(backgroundComponent.containers[0])
			.setWidth(new CoerceAtMostConstraint(new RelativeConstraint(0.5F), new PixelConstraint(LEFT_WIDTH)))
			.setHeight(new RelativeConstraint());

		new UIWrappedText(TranslationProvider.GUI_MTR_LIFT_CUSTOMIZATION.getString(), false)
			.setChildOf(leftContainer)
			.setWidth(new RelativeConstraint())
			.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		final ScrollComponent scrollComponent = ((ScrollPanelComponent) new ScrollPanelComponent(true)
			.setChildOf(leftContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)))).contentContainer;

		new PreviewBoxComponent(true, true, true, this::onDrawPreview)
			.setChildOf(backgroundComponent.containers[0])
			.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)))
			.setHeight(new RelativeConstraint());

		GuiHelper.createLabel(scrollComponent, TranslationProvider.GUI_MTR_DIMENSIONS.getString());
		dimensionXInputComponent = createNumberInput(scrollComponent, "X", lift.getHeight(), MIN_DIMENSION, MAX_DIMENSION);
		dimensionYInputComponent = createNumberInput(scrollComponent, "Y", lift.getWidth(), MIN_DIMENSION, MAX_DIMENSION);
		dimensionZInputComponent = createNumberInput(scrollComponent, "Z", lift.getDepth(), MIN_DIMENSION, MAX_DIMENSION);

		GuiHelper.createSpacing(scrollComponent);
		GuiHelper.createLabel(scrollComponent, TranslationProvider.GUI_MTR_OFFSET.getString());
		offsetXInputComponent = createNumberInput(scrollComponent, "X", lift.getOffsetX(), -MAX_OFFSET, MAX_OFFSET);
		offsetYInputComponent = createNumberInput(scrollComponent, "Y", lift.getOffsetY(), -MAX_OFFSET, MAX_OFFSET);
		offsetZInputComponent = createNumberInput(scrollComponent, "Z", lift.getOffsetZ(), -MAX_OFFSET, MAX_OFFSET);

		GuiHelper.createSpacing(scrollComponent);
		isDoubleSidedCheckboxComponent = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(scrollComponent)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint());

		isDoubleSidedCheckboxComponent.setText(TranslationProvider.GUI_MTR_LIFT_IS_DOUBLE_SIDED.getString());
		isDoubleSidedCheckboxComponent.onClick(() -> lift.setIsDoubleSided(isDoubleSidedCheckboxComponent.isChecked()));

		GuiHelper.createSpacing(scrollComponent);
		GuiHelper.createLabel(scrollComponent, TranslationProvider.GUI_MTR_STYLES.getString());
		final ButtonComponent editStylesButtonComponent = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(scrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		editStylesButtonComponent.setText(Text.translatable("selectWorld.edit").getString());
		editStylesButtonComponent.onClick(() -> UMinecraft.setCurrentScreenObj(LiftStyleSelectorScreen.create(lift, this)));

		final ButtonComponent rotateAnticlockwiseButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(scrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		rotateAnticlockwiseButton.setText(TranslationProvider.GUI_MTR_ROTATE_ANTICLOCKWISE.getString());
		rotateAnticlockwiseButton.onClick(() -> {
			liftDirection = liftDirection.rotateYCounterclockwise();
			lift.setAngle(Angle.fromAngle(liftDirection.getPositiveHorizontalDegrees()));
		});

		final ButtonComponent rotateClockwiseButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(scrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		rotateClockwiseButton.setText(TranslationProvider.GUI_MTR_ROTATE_CLOCKWISE.getString());
		rotateClockwiseButton.onClick(() -> {
			liftDirection = liftDirection.rotateYClockwise();
			lift.setAngle(Angle.fromAngle(liftDirection.getPositiveHorizontalDegrees()));
		});
	}

	@Override
	public void onScreenClose() {
		lift.setDimensions(
			Utilities.clampSafe(dimensionXInputComponent.getValue(), MIN_DIMENSION, MAX_DIMENSION),
			Utilities.clampSafe(dimensionYInputComponent.getValue(), MIN_DIMENSION, MAX_DIMENSION),
			Utilities.clampSafe(dimensionZInputComponent.getValue(), MIN_DIMENSION, MAX_DIMENSION),
			Utilities.clampSafe(offsetXInputComponent.getValue(), -MAX_OFFSET, MAX_OFFSET),
			Utilities.clampSafe(offsetYInputComponent.getValue(), -MAX_OFFSET, MAX_OFFSET),
			Utilities.clampSafe(offsetZInputComponent.getValue(), -MAX_OFFSET, MAX_OFFSET)
		);
		lift.setIsDoubleSided(isDoubleSidedCheckboxComponent.isChecked());

		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addLift(lift)));
		super.onScreenClose();
	}

	private void onDrawPreview(MatrixStack matrixStack) {
		// TODO lift preview
	}

	private static NumberInputComponent createNumberInput(UIContainer container, String axis, double value, int min, int max) {
		final NumberInputComponent numberInputComponent = (NumberInputComponent) new NumberInputComponent(min, max, 0.5, true, null)
			.setChildOf(container)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		numberInputComponent.setPrefix(axis + " ");
		numberInputComponent.setValue(value);
		return numberInputComponent;
	}
}
