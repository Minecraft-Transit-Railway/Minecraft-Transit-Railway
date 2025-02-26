package org.mtr.mod.screen;

import org.mtr.core.data.Lift;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Angle;
import org.mtr.core.tool.EnumHelper;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.holder.Text;
import org.mtr.mapping.mapper.*;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketUpdateData;

import java.util.Locale;

public class LiftCustomizationScreen extends MTRScreenBase implements IGui {

	private LiftStyle liftStyle;
	private Direction liftDirection;

	private final Lift lift;
	private final ButtonWidgetExtension buttonHeightMinus;
	private final ButtonWidgetExtension buttonHeightAdd;
	private final ButtonWidgetExtension buttonWidthMinus;
	private final ButtonWidgetExtension buttonWidthAdd;
	private final ButtonWidgetExtension buttonDepthMinus;
	private final ButtonWidgetExtension buttonDepthAdd;
	private final ButtonWidgetExtension buttonOffsetXMinus;
	private final ButtonWidgetExtension buttonOffsetXAdd;
	private final ButtonWidgetExtension buttonOffsetYMinus;
	private final ButtonWidgetExtension buttonOffsetYAdd;
	private final ButtonWidgetExtension buttonOffsetZMinus;
	private final ButtonWidgetExtension buttonOffsetZAdd;
	private final CheckboxWidgetExtension buttonIsDoubleSided;
	private final ButtonWidgetExtension buttonLiftStyle;
	private final ButtonWidgetExtension buttonRotateAnticlockwise;
	private final ButtonWidgetExtension buttonRotateClockwise;
	private final int width1;
	private final int width2;

	private static final int MIN_DIMENSION = 2;
	private static final int MAX_DIMENSION = 16;
	private static final int MAX_OFFSET = 16;

	public LiftCustomizationScreen(Lift lift) {
		super();
		this.lift = lift;
		liftStyle = EnumHelper.valueOf(LiftStyle.TRANSPARENT, lift.getStyle());
		liftDirection = Direction.fromRotation(lift.getAngle().angleDegrees);

		buttonHeightMinus = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("-"), button -> {
			lift.setDimensions(Math.max(MIN_DIMENSION, lift.getHeight() - 0.5), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		});
		buttonHeightAdd = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("+"), button -> {
			lift.setDimensions(Math.min(MAX_DIMENSION, lift.getHeight() + 0.5), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		});
		buttonWidthMinus = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("-"), button -> {
			lift.setDimensions(lift.getHeight(), Math.max(MIN_DIMENSION, lift.getWidth() - 1), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		});
		buttonWidthAdd = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("+"), button -> {
			lift.setDimensions(lift.getHeight(), Math.min(MAX_DIMENSION, lift.getWidth() + 1), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		});
		buttonDepthMinus = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("-"), button -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), Math.max(MIN_DIMENSION, lift.getDepth() - 1), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		});
		buttonDepthAdd = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("+"), button -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), Math.min(MAX_DIMENSION, lift.getDepth() + 1), lift.getOffsetX(), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		});
		buttonOffsetXMinus = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("-"), button -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), Math.max(-MAX_OFFSET, lift.getOffsetX() - 0.5), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		});
		buttonOffsetXAdd = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("+"), button -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), Math.min(MAX_OFFSET, lift.getOffsetX() + 0.5), lift.getOffsetY(), lift.getOffsetZ());
			updateControls(true);
		});
		buttonOffsetYMinus = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("-"), button -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), Math.max(-MAX_OFFSET, lift.getOffsetY() - 1), lift.getOffsetZ());
			updateControls(true);
		});
		buttonOffsetYAdd = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("+"), button -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), Math.min(MAX_OFFSET, lift.getOffsetY() + 1), lift.getOffsetZ());
			updateControls(true);
		});
		buttonOffsetZMinus = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("-"), button -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), Math.max(-MAX_OFFSET, lift.getOffsetZ() - 0.5));
			updateControls(true);
		});
		buttonOffsetZAdd = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("+"), button -> {
			lift.setDimensions(lift.getHeight(), lift.getWidth(), lift.getDepth(), lift.getOffsetX(), lift.getOffsetY(), Math.min(MAX_OFFSET, lift.getOffsetZ() + 0.5));
			updateControls(true);
		});

		final MutableText doubleSidedText = TranslationProvider.GUI_MTR_LIFT_IS_DOUBLE_SIDED.getMutableText();
		final MutableText rotateAnticlockwiseText = TranslationProvider.GUI_MTR_ROTATE_ANTICLOCKWISE.getMutableText();
		final MutableText rotateClockwiseText = TranslationProvider.GUI_MTR_ROTATE_CLOCKWISE.getMutableText();
		buttonIsDoubleSided = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
			lift.setIsDoubleSided(checked);
			updateControls(true);
		});
		buttonIsDoubleSided.setMessage2(new Text(doubleSidedText.data));
		buttonLiftStyle = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> {
			liftStyle = LiftStyle.values()[(liftStyle.ordinal() + 1) % LiftStyle.values().length];
			lift.setStyle(liftStyle.toString());
			updateControls(true);
		});
		buttonRotateAnticlockwise = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, rotateAnticlockwiseText, button -> {
			liftDirection = liftDirection.rotateYCounterclockwise();
			lift.setAngle(Angle.fromAngle(liftDirection.asRotation()));
			updateControls(true);
		});
		buttonRotateClockwise = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, rotateClockwiseText, button -> {
			liftDirection = liftDirection.rotateYClockwise();
			lift.setAngle(Angle.fromAngle(liftDirection.asRotation()));
			updateControls(true);
		});

		width1 = Math.max(Math.max(SQUARE_SIZE * 3, GraphicsHolder.getTextWidth(doubleSidedText)), Math.max(GraphicsHolder.getTextWidth(rotateAnticlockwiseText), GraphicsHolder.getTextWidth(rotateClockwiseText))) + TEXT_PADDING * 2;
		width2 = width1 + SQUARE_SIZE;
	}

	@Override
	protected void init2() {
		super.init2();

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

		addChild(new ClickableWidget(buttonHeightMinus));
		addChild(new ClickableWidget(buttonHeightAdd));
		addChild(new ClickableWidget(buttonWidthMinus));
		addChild(new ClickableWidget(buttonWidthAdd));
		addChild(new ClickableWidget(buttonDepthMinus));
		addChild(new ClickableWidget(buttonDepthAdd));
		addChild(new ClickableWidget(buttonOffsetXMinus));
		addChild(new ClickableWidget(buttonOffsetXAdd));
		addChild(new ClickableWidget(buttonOffsetYMinus));
		addChild(new ClickableWidget(buttonOffsetYAdd));
		addChild(new ClickableWidget(buttonOffsetZMinus));
		addChild(new ClickableWidget(buttonOffsetZAdd));
		addChild(new ClickableWidget(buttonIsDoubleSided));
//		addChild(new ClickableWidget(buttonLiftStyle));
		addChild(new ClickableWidget(buttonRotateAnticlockwise));
		addChild(new ClickableWidget(buttonRotateClockwise));
		updateControls(false);
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
		guiDrawing.beginDrawingRectangle();
		guiDrawing.drawRectangle(0, 0, width2, height, ARGB_BACKGROUND);
		guiDrawing.finishDrawingRectangle();
		super.render(graphicsHolder, mouseX, mouseY, delta);
		graphicsHolder.drawCenteredText(TranslationProvider.TOOLTIP_MTR_RAIL_ACTION_HEIGHT.getMutableText(lift.getHeight()), width2 / 2, TEXT_PADDING, ARGB_WHITE);
		graphicsHolder.drawCenteredText(TranslationProvider.TOOLTIP_MTR_RAIL_ACTION_WIDTH.getMutableText(lift.getWidth()), width2 / 2, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
		graphicsHolder.drawCenteredText(TranslationProvider.TOOLTIP_MTR_RAIL_ACTION_DEPTH.getMutableText(lift.getDepth()), width2 / 2, SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
		graphicsHolder.drawCenteredText(TranslationProvider.GUI_MTR_OFFSET_X.getMutableText(lift.getOffsetX()), width2 / 2, SQUARE_SIZE * 3 + TEXT_PADDING, ARGB_WHITE);
		graphicsHolder.drawCenteredText(TranslationProvider.GUI_MTR_OFFSET_Y.getMutableText(lift.getOffsetY()), width2 / 2, SQUARE_SIZE * 4 + TEXT_PADDING, ARGB_WHITE);
		graphicsHolder.drawCenteredText(TranslationProvider.GUI_MTR_OFFSET_Z.getMutableText(lift.getOffsetZ()), width2 / 2, SQUARE_SIZE * 5 + TEXT_PADDING, ARGB_WHITE);
	}

	@Override
	public boolean isPauseScreen2() {
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
		buttonIsDoubleSided.setChecked(lift.getIsDoubleSided());
		buttonLiftStyle.setMessage2(TranslationProvider.GUI_MTR_LIFT_STYLE.getText(lift.getStyle().toUpperCase(Locale.ENGLISH)));

		if (sendUpdate) {
			InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addLift(lift)));
		}
	}

	private enum LiftStyle {TRANSPARENT, OPAQUE}
}
