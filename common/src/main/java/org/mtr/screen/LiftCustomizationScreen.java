package org.mtr.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Lift;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Angle;
import org.mtr.core.tool.EnumHelper;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;

import java.util.Locale;

public class LiftCustomizationScreen extends MTRScreenBase implements IGui {

	private LiftStyle liftStyle;
	private Direction liftDirection;

	private final Lift lift;
	private final ButtonWidget buttonHeightMinus;
	private final ButtonWidget buttonHeightAdd;
	private final ButtonWidget buttonWidthMinus;
	private final ButtonWidget buttonWidthAdd;
	private final ButtonWidget buttonDepthMinus;
	private final ButtonWidget buttonDepthAdd;
	private final ButtonWidget buttonOffsetXMinus;
	private final ButtonWidget buttonOffsetXAdd;
	private final ButtonWidget buttonOffsetYMinus;
	private final ButtonWidget buttonOffsetYAdd;
	private final ButtonWidget buttonOffsetZMinus;
	private final ButtonWidget buttonOffsetZAdd;
	private final CheckboxWidget buttonIsDoubleSided;
	private final ButtonWidget buttonLiftStyle;
	private final ButtonWidget buttonRotateAnticlockwise;
	private final ButtonWidget buttonRotateClockwise;
	private final int width1;
	private final int width2;

	private static final int MIN_DIMENSION = 2;
	private static final int MAX_DIMENSION = 16;
	private static final int MAX_OFFSET = 16;

	public LiftCustomizationScreen(Lift lift) {
		super();
		this.lift = lift;
		liftStyle = EnumHelper.valueOf(LiftStyle.TRANSPARENT, lift.getStyle());
		liftDirection = Direction.fromHorizontalDegrees(lift.getAngle().angleDegrees);

		buttonHeightMinus = ButtonWidget.builder(Text.literal("-"), button -> {
			lift.setDimensions(Math.max(MIN_DIMENSION, lift.getHeight() - 0.5), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		}).build();
		buttonHeightAdd = ButtonWidget.builder(Text.literal("+"), button -> {
			lift.setDimensions(Math.min(MAX_DIMENSION, lift.getHeight() + 0.5), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		}).build();
		buttonWidthMinus = ButtonWidget.builder(Text.literal("-"), button -> {
			lift.setDimensions(lift.getHeight(), Math.max(MIN_DIMENSION, lift.getWidth() - 1), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		}).build();
		buttonWidthAdd = ButtonWidget.builder(Text.literal("+"), button -> {
			lift.setDimensions(lift.getHeight(), Math.min(MAX_DIMENSION, lift.getWidth() + 1), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		}).build();
		buttonDepthMinus = ButtonWidget.builder(Text.literal("-"), button -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), Math.max(MIN_DIMENSION, lift.getDepth() - 1), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		}).build();
		buttonDepthAdd = ButtonWidget.builder(Text.literal("+"), button -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), Math.min(MAX_DIMENSION, lift.getDepth() + 1), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		}).build();
		buttonOffsetXMinus = ButtonWidget.builder(Text.literal("-"), button -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), Math.max(-MAX_OFFSET, lift.getOffsetX() - 0.5), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		}).build();
		buttonOffsetXAdd = ButtonWidget.builder(Text.literal("+"), button -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), Math.min(MAX_OFFSET, lift.getOffsetX() + 0.5), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		}).build();
		buttonOffsetYMinus = ButtonWidget.builder(Text.literal("-"), button -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), Math.max(-MAX_OFFSET, lift.getOffsetY() - 1), lift.getOffsetZ());
			updateControls(true);
		}).build();
		buttonOffsetYAdd = ButtonWidget.builder(Text.literal("+"), button -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), Math.min(MAX_OFFSET, lift.getOffsetY() + 1), lift.getOffsetZ());
			updateControls(true);
		}).build();
		buttonOffsetZMinus = ButtonWidget.builder(Text.literal("-"), button -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), Math.max(-MAX_OFFSET, lift.getOffsetZ() - 0.5));
			updateControls(true);
		}).build();
		buttonOffsetZAdd = ButtonWidget.builder(Text.literal("+"), button -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), Math.min(MAX_OFFSET, lift.getOffsetZ() + 0.5));
			updateControls(true);
		}).build();

		final MutableText doubleSidedText = TranslationProvider.GUI_MTR_LIFT_IS_DOUBLE_SIDED.getMutableText();
		final MutableText rotateAnticlockwiseText = TranslationProvider.GUI_MTR_ROTATE_ANTICLOCKWISE.getMutableText();
		final MutableText rotateClockwiseText = TranslationProvider.GUI_MTR_ROTATE_CLOCKWISE.getMutableText();
		buttonIsDoubleSided = CheckboxWidget.builder(doubleSidedText, textRenderer).callback((checkboxWidget, checked) -> {
			lift.setIsDoubleSided(checked);
			updateControls(true);
		}).build();
		buttonLiftStyle = ButtonWidget.builder(Text.empty(), button -> {
			liftStyle = LiftStyle.values()[(liftStyle.ordinal() + 1) % LiftStyle.values().length];
			lift.setStyle(liftStyle.toString());
			updateControls(true);
		}).build();
		buttonRotateAnticlockwise = ButtonWidget.builder(rotateAnticlockwiseText, button -> {
			liftDirection = liftDirection.rotateYCounterclockwise();
			lift.setAngle(Angle.fromAngle(liftDirection.getPositiveHorizontalDegrees()));
			updateControls(true);
		}).build();
		buttonRotateClockwise = ButtonWidget.builder(rotateClockwiseText, button -> {
			liftDirection = liftDirection.rotateYClockwise();
			lift.setAngle(Angle.fromAngle(liftDirection.getPositiveHorizontalDegrees()));
			updateControls(true);
		}).build();

		width1 = Math.max(Math.max(SQUARE_SIZE * 3, textRenderer.getWidth(doubleSidedText)), Math.max(textRenderer.getWidth(rotateAnticlockwiseText), textRenderer.getWidth(rotateClockwiseText))) + TEXT_PADDING * 2;
		width2 = width1 + SQUARE_SIZE;
	}

	@Override
	protected void init() {
		super.init();

		IDrawing.setPositionAndWidth(buttonHeightMinus, 0, 0, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonHeightAdd, width1, 0, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonWidthMinus, 0, SQUARE_SIZE, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonWidthAdd, width1, SQUARE_SIZE, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonDepthMinus, 0, SQUARE_SIZE * 2, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonDepthAdd, width1, SQUARE_SIZE * 2, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonOffsetXMinus, 0, SQUARE_SIZE * 3, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonOffsetXAdd, width1, SQUARE_SIZE * 3, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonOffsetYMinus, 0, SQUARE_SIZE * 4, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonOffsetYAdd, width1, SQUARE_SIZE * 4, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonOffsetZMinus, 0, SQUARE_SIZE * 5, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonOffsetZAdd, width1, SQUARE_SIZE * 5, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonIsDoubleSided, 0, SQUARE_SIZE * 7, width2);
		IDrawing.setPositionAndWidth(buttonLiftStyle, 0, SQUARE_SIZE * 8, width2);
		IDrawing.setPositionAndWidth(buttonRotateAnticlockwise, 0, SQUARE_SIZE * 9, width2);
		IDrawing.setPositionAndWidth(buttonRotateClockwise, 0, SQUARE_SIZE * 10, width2);

		addSelectableChild(buttonHeightMinus);
		addSelectableChild(buttonHeightAdd);
		addSelectableChild(buttonWidthMinus);
		addSelectableChild(buttonWidthAdd);
		addSelectableChild(buttonDepthMinus);
		addSelectableChild(buttonDepthAdd);
		addSelectableChild(buttonOffsetXMinus);
		addSelectableChild(buttonOffsetXAdd);
		addSelectableChild(buttonOffsetYMinus);
		addSelectableChild(buttonOffsetYAdd);
		addSelectableChild(buttonOffsetZMinus);
		addSelectableChild(buttonOffsetZAdd);
		addSelectableChild(buttonIsDoubleSided);
//		addSelectableChild(buttonLiftStyle);
		addSelectableChild(buttonRotateAnticlockwise);
		addSelectableChild(buttonRotateClockwise);
		updateControls(false);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		context.fill(0, 0, width2, height, ARGB_BACKGROUND);
		super.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, TranslationProvider.TOOLTIP_MTR_RAIL_ACTION_HEIGHT.getMutableText(lift.getHeight()), width2 / 2, TEXT_PADDING, ARGB_WHITE);
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, TranslationProvider.TOOLTIP_MTR_RAIL_ACTION_WIDTH.getMutableText(lift.getWidth()), width2 / 2, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, TranslationProvider.TOOLTIP_MTR_RAIL_ACTION_DEPTH.getMutableText(lift.getDepth()), width2 / 2, SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, TranslationProvider.GUI_MTR_OFFSET_X.getMutableText(lift.getOffsetX()), width2 / 2, SQUARE_SIZE * 3 + TEXT_PADDING, ARGB_WHITE);
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, TranslationProvider.GUI_MTR_OFFSET_Y.getMutableText(lift.getOffsetY()), width2 / 2, SQUARE_SIZE * 4 + TEXT_PADDING, ARGB_WHITE);
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, TranslationProvider.GUI_MTR_OFFSET_Z.getMutableText(lift.getOffsetZ()), width2 / 2, SQUARE_SIZE * 5 + TEXT_PADDING, ARGB_WHITE);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	private void updateControls(boolean sendUpdate) {
		buttonHeightMinus.active = lift.getHeight() > MIN_DIMENSION;
		buttonHeightAdd.active = lift.getHeight() < MAX_DIMENSION;
		buttonWidthMinus.active = lift.getWidth() > MIN_DIMENSION;
		buttonWidthAdd.active = lift.getWidth() < MAX_DIMENSION;
		buttonDepthMinus.active = lift.getDepth() > MIN_DIMENSION;
		buttonDepthAdd.active = lift.getDepth() < MAX_DIMENSION;
		buttonOffsetXMinus.active = lift.getOffsetX() > -MAX_OFFSET;
		buttonOffsetXAdd.active = lift.getOffsetX() < MAX_OFFSET;
		buttonOffsetYMinus.active = lift.getOffsetY() > -MAX_OFFSET;
		buttonOffsetYAdd.active = lift.getOffsetY() < MAX_OFFSET;
		buttonOffsetZMinus.active = lift.getOffsetZ() > -MAX_OFFSET;
		buttonOffsetZAdd.active = lift.getOffsetZ() < MAX_OFFSET;
		IGui.setChecked(buttonIsDoubleSided, lift.getIsDoubleSided());
		buttonLiftStyle.setMessage(TranslationProvider.GUI_MTR_LIFT_STYLE.getText(lift.getStyle().toUpperCase(Locale.ENGLISH)));

		if (sendUpdate) {
			RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addLift(lift)));
		}
	}

	private enum LiftStyle {TRANSPARENT, OPAQUE}
}
