package org.mtr.screen;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Rail;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateData;
import org.mtr.packet.PacketUpdateLastRailStyles;
import org.mtr.registry.RegistryClient;
import org.mtr.widget.BetterTextFieldWidget;

import java.util.stream.Collectors;

public class RailModifierScreen extends ScreenBase implements IGui {

	private Rail.Shape shape;
	private double radius;
	private double tiltAngleDegrees1;
	private double tiltAngleDegreesMiddle;
	private double tiltAngleDegrees2;
	private final int buttonsWidth;

	private final Rail rail;
	private final double maxRadius;
	private final ButtonWidget buttonShape;
	private final ButtonWidget buttonStyle;
	private final ButtonWidget buttonStyleFlip;
	private final ButtonWidget buttonMinus2;
	private final ButtonWidget buttonMinus1;
	private final ButtonWidget buttonMinus0;
	private final ButtonWidget buttonPlus0;
	private final ButtonWidget buttonPlus1;
	private final ButtonWidget buttonPlus2;
	private final BetterTextFieldWidget textFieldRadius;
	private final BetterTextFieldWidget textFieldTiltAngleDegrees1;
	private final BetterTextFieldWidget textFieldTiltAngleDegreesMiddle;
	private final BetterTextFieldWidget textFieldTiltAngleDegrees2;
	private final MutableText shapeText = TranslationProvider.GUI_MTR_RAIL_SHAPE.getMutableText();
	private final MutableText styleText = TranslationProvider.GUI_MTR_RAIL_STYLES.getMutableText();
	private final MutableText radiusText = TranslationProvider.GUI_MTR_RAIL_RADIUS.getMutableText();
	private final int xStart;

	public RailModifierScreen(String railId) {
		super();
		rail = MinecraftClientData.getInstance().railIdMap.get(railId);
		shape = rail == null ? Rail.Shape.QUADRATIC : rail.railMath.getShape();
		radius = rail == null ? 0 : rail.railMath.getVerticalRadius();
		maxRadius = rail == null ? 0 : rail.railMath.getMaxVerticalRadius();
		tiltAngleDegrees1 = rail == null ? 0 : rail.getTiltAngleDegrees1();
		tiltAngleDegreesMiddle = rail == null ? 0 : rail.getTiltAngleDegreesMiddle();
		tiltAngleDegrees2 = rail == null ? 0 : rail.getTiltAngleDegrees2();

		buttonShape = ButtonWidget.builder(Text.empty(), button -> {
			shape = shape == Rail.Shape.QUADRATIC ? Rail.Shape.TWO_RADII : Rail.Shape.QUADRATIC;
			update(radius, tiltAngleDegrees1, tiltAngleDegreesMiddle, tiltAngleDegrees2, true);
		}).build();
		buttonStyle = ButtonWidget.builder(Text.translatable("selectWorld.edit"), button -> {
			if (rail != null) {
				MinecraftClient.getInstance().setScreen(RailStyleSelectorScreen.create(rail));
			}
		}).build();
		buttonStyleFlip = ButtonWidget.builder(TranslationProvider.GUI_MTR_FLIP_STYLES.getMutableText(), button -> {
			if (rail != null) {
				final ObjectArrayList<String> styles = rail.getStyles().stream().map(style -> {
					final boolean isForwards = style.endsWith("_1");
					final boolean isBackwards = style.endsWith("_2");
					if (isForwards || isBackwards) {
						return style.substring(0, style.length() - 1) + (isForwards ? "2" : "1");
					} else {
						return style;
					}
				}).collect(Collectors.toCollection(ObjectArrayList::new));
				RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addRail(Rail.copy(rail, styles))));
				final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
				if (clientPlayerEntity != null) {
					RegistryClient.sendPacketToServer(new PacketUpdateLastRailStyles(clientPlayerEntity.getUuid(), rail.getTransportMode(), styles));
				}
				MinecraftClient.getInstance().setScreen(null);
			}
		}).build();
		buttonMinus2 = ButtonWidget.builder(Text.literal("-10"), button -> update(radius - 10, tiltAngleDegrees1, tiltAngleDegreesMiddle, tiltAngleDegrees2, true)).build();
		buttonMinus1 = ButtonWidget.builder(Text.literal("-1"), button -> update(radius - 1, tiltAngleDegrees1, tiltAngleDegreesMiddle, tiltAngleDegrees2, true)).build();
		buttonMinus0 = ButtonWidget.builder(Text.literal("-0.1"), button -> update(radius - 0.1, tiltAngleDegrees1, tiltAngleDegreesMiddle, tiltAngleDegrees2, true)).build();
		buttonPlus0 = ButtonWidget.builder(Text.literal("+0.1"), button -> update(radius + 0.1, tiltAngleDegrees1, tiltAngleDegreesMiddle, tiltAngleDegrees2, true)).build();
		buttonPlus1 = ButtonWidget.builder(Text.literal("+1"), button -> update(radius + 1, tiltAngleDegrees1, tiltAngleDegreesMiddle, tiltAngleDegrees2, true)).build();
		buttonPlus2 = ButtonWidget.builder(Text.literal("+10"), button -> update(radius + 10, tiltAngleDegrees1, tiltAngleDegreesMiddle, tiltAngleDegrees2, true)).build();

		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		xStart = Math.max(textRenderer.getWidth(shapeText), textRenderer.getWidth(radiusText)) + TEXT_PADDING * 2;
		buttonsWidth = SQUARE_SIZE * 12 + textRenderer.getWidth(styleText) + TEXT_PADDING * 2;

		textFieldRadius = new BetterTextFieldWidget(256, TextCase.DEFAULT, "[^\\d\\.]", "0", buttonsWidth - SQUARE_SIZE * 12 - TEXT_FIELD_PADDING, text -> {
			try {
				update(Double.parseDouble(text), tiltAngleDegrees1, tiltAngleDegreesMiddle, tiltAngleDegrees2, true);
			} catch (Exception ignored) {
			}
		});
		textFieldTiltAngleDegrees1 = new BetterTextFieldWidget(256, TextCase.DEFAULT, "[^\\d\\.-]", "0", buttonsWidth - SQUARE_SIZE * 12 - TEXT_FIELD_PADDING, text -> {
			try {
				update(radius, Double.parseDouble(text), tiltAngleDegreesMiddle, tiltAngleDegrees2, true);
			} catch (Exception ignored) {
			}
		});
		textFieldTiltAngleDegreesMiddle = new BetterTextFieldWidget(256, TextCase.DEFAULT, "[^\\d\\.-]", "0", buttonsWidth - SQUARE_SIZE * 12 - TEXT_FIELD_PADDING, text -> {
			try {
				update(radius, tiltAngleDegrees1, Double.parseDouble(text), tiltAngleDegrees2, true);
			} catch (Exception ignored) {
			}
		});
		textFieldTiltAngleDegrees2 = new BetterTextFieldWidget(256, TextCase.DEFAULT, "[^\\d\\.-]", "0", buttonsWidth - SQUARE_SIZE * 12 - TEXT_FIELD_PADDING, text -> {
			try {
				update(radius, tiltAngleDegrees1, tiltAngleDegreesMiddle, Double.parseDouble(text), true);
			} catch (Exception ignored) {
			}
		});
	}

	@Override
	protected void init() {
		super.init();

		IDrawing.setPositionAndWidth(buttonShape, xStart, 0, SQUARE_SIZE * 6);
		IDrawing.setPositionAndWidth(buttonStyle, xStart + buttonsWidth - SQUARE_SIZE * 6, 0, SQUARE_SIZE * 3);
		IDrawing.setPositionAndWidth(buttonStyleFlip, xStart + buttonsWidth - SQUARE_SIZE * 3, 0, SQUARE_SIZE * 3);
		IDrawing.setPositionAndWidth(buttonMinus2, xStart, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(buttonMinus1, xStart + SQUARE_SIZE * 2, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(buttonMinus0, xStart + SQUARE_SIZE * 4, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(buttonPlus0, xStart + buttonsWidth - SQUARE_SIZE * 6, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(buttonPlus1, xStart + buttonsWidth - SQUARE_SIZE * 4, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(buttonPlus2, xStart + buttonsWidth - SQUARE_SIZE * 2, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2);
		textFieldRadius.setPosition(0, SQUARE_SIZE + TEXT_FIELD_PADDING / 2);
		textFieldTiltAngleDegrees1.setPosition(0, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 3 / 2);
		textFieldTiltAngleDegreesMiddle.setPosition(0, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 5 / 2);
		textFieldTiltAngleDegrees2.setPosition(0, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING * 7 / 2);

		addDrawableChild(buttonShape);
		addDrawableChild(buttonStyle);
		addDrawableChild(buttonStyleFlip);
		addDrawableChild(buttonMinus2);
		addDrawableChild(buttonMinus1);
		addDrawableChild(buttonMinus0);
		addDrawableChild(buttonPlus0);
		addDrawableChild(buttonPlus1);
		addDrawableChild(buttonPlus2);
		addDrawableChild(textFieldRadius);
		addDrawableChild(textFieldTiltAngleDegrees1);
		addDrawableChild(textFieldTiltAngleDegreesMiddle);
		addDrawableChild(textFieldTiltAngleDegrees2);

		textFieldRadius.setText(String.valueOf(radius));
		textFieldTiltAngleDegrees1.setText(String.valueOf(tiltAngleDegrees1));
		textFieldTiltAngleDegreesMiddle.setText(String.valueOf(tiltAngleDegreesMiddle));
		textFieldTiltAngleDegrees2.setText(String.valueOf(tiltAngleDegrees2));
		update(radius, tiltAngleDegrees1, tiltAngleDegreesMiddle, tiltAngleDegrees2, false);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		context.fill(0, 0, width, shape == Rail.Shape.QUADRATIC ? SQUARE_SIZE : SQUARE_SIZE * 2 + TEXT_FIELD_PADDING, ARGB_BACKGROUND);
		super.render(context, mouseX, mouseY, delta);
		context.drawText(textRenderer, shapeText, TEXT_PADDING, TEXT_PADDING, ARGB_WHITE, false);
		context.drawText(textRenderer, styleText, xStart + SQUARE_SIZE * 6 + TEXT_PADDING, TEXT_PADDING, ARGB_WHITE, false);
		if (shape != Rail.Shape.QUADRATIC) {
			context.drawText(textRenderer, radiusText, TEXT_PADDING, SQUARE_SIZE + TEXT_PADDING + TEXT_FIELD_PADDING / 2, ARGB_WHITE, false);
		}
	}

	private void update(double newRadius, double newTiltAngleDegrees1, double newTiltAngleDegreesMiddle, double newTiltAngleDegrees2, boolean sendPacket) {
		buttonShape.setMessage((shape == Rail.Shape.QUADRATIC ? TranslationProvider.GUI_MTR_RAIL_SHAPE_QUADRATIC : TranslationProvider.GUI_MTR_RAIL_SHAPE_TWO_RADII).getText());
		radius = Math.clamp(Utilities.round(newRadius, 2), 0, maxRadius);
		tiltAngleDegrees1 = Math.clamp(Utilities.round(newTiltAngleDegrees1, 2), -180, 180);
		tiltAngleDegreesMiddle = Math.clamp(Utilities.round(newTiltAngleDegreesMiddle, 2), -180, 180);
		tiltAngleDegrees2 = Math.clamp(Utilities.round(newTiltAngleDegrees2, 2), -180, 180);
		textFieldRadius.setX((shape == Rail.Shape.QUADRATIC ? width : xStart + SQUARE_SIZE * 6) + TEXT_FIELD_PADDING / 2);
		buttonMinus2.visible = shape != Rail.Shape.QUADRATIC;
		buttonMinus1.visible = shape != Rail.Shape.QUADRATIC;
		buttonMinus0.visible = shape != Rail.Shape.QUADRATIC;
		buttonPlus0.visible = shape != Rail.Shape.QUADRATIC;
		buttonPlus1.visible = shape != Rail.Shape.QUADRATIC;
		buttonPlus2.visible = shape != Rail.Shape.QUADRATIC;
		buttonMinus2.active = radius > 0;
		buttonMinus1.active = radius > 0;
		buttonMinus0.active = radius > 0;
		buttonPlus0.active = radius < maxRadius;
		buttonPlus1.active = radius < maxRadius;
		buttonPlus2.active = radius < maxRadius;

		try {
			if (Double.parseDouble(textFieldRadius.getText()) != radius) {
				textFieldRadius.setText(String.valueOf(radius));
			}
		} catch (Exception ignored) {
		}

		try {
			if (Double.parseDouble(textFieldTiltAngleDegrees1.getText()) != tiltAngleDegrees1) {
				textFieldTiltAngleDegrees1.setText(String.valueOf(tiltAngleDegrees1));
			}
		} catch (Exception ignored) {
		}

		try {
			if (Double.parseDouble(textFieldTiltAngleDegreesMiddle.getText()) != tiltAngleDegreesMiddle) {
				textFieldTiltAngleDegreesMiddle.setText(String.valueOf(tiltAngleDegreesMiddle));
			}
		} catch (Exception ignored) {
		}

		try {
			if (Double.parseDouble(textFieldTiltAngleDegrees2.getText()) != tiltAngleDegrees2) {
				textFieldTiltAngleDegrees2.setText(String.valueOf(tiltAngleDegrees2));
			}
		} catch (Exception ignored) {
		}

		if (sendPacket) {
			RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addRail(Rail.copy(rail, shape, radius, tiltAngleDegrees1, tiltAngleDegreesMiddle, tiltAngleDegrees2))));
		}
	}
}
