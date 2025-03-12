package org.mtr.screen;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
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

import java.util.stream.Collectors;

public class RailModifierScreen extends MTRScreenBase implements IGui {

	private Rail.Shape shape;
	private double radius;
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
	private final WidgetBetterTextField textFieldRadius;
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

		buttonShape = ButtonWidget.builder(Text.empty(), button -> {
			shape = shape == Rail.Shape.QUADRATIC ? Rail.Shape.TWO_RADII : Rail.Shape.QUADRATIC;
			update(radius, true);
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
		buttonMinus2 = ButtonWidget.builder(Text.literal("-10"), button -> update(radius - 10, true)).build();
		buttonMinus1 = ButtonWidget.builder(Text.literal("-1"), button -> update(radius - 1, true)).build();
		buttonMinus0 = ButtonWidget.builder(Text.literal("-0.1"), button -> update(radius - 0.1, true)).build();
		buttonPlus0 = ButtonWidget.builder(Text.literal("+0.1"), button -> update(radius + 0.1, true)).build();
		buttonPlus1 = ButtonWidget.builder(Text.literal("+1"), button -> update(radius + 1, true)).build();
		buttonPlus2 = ButtonWidget.builder(Text.literal("+10"), button -> update(radius + 10, true)).build();
		textFieldRadius = new WidgetBetterTextField(256, TextCase.DEFAULT, "[^\\d\\.]", "0");
		xStart = Math.max(textRenderer.getWidth(shapeText), textRenderer.getWidth(radiusText)) + TEXT_PADDING * 2;
		buttonsWidth = SQUARE_SIZE * 12 + textRenderer.getWidth(styleText) + TEXT_PADDING * 2;
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
		IDrawing.setPositionAndWidth(textFieldRadius, 0, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, buttonsWidth - SQUARE_SIZE * 12 - TEXT_FIELD_PADDING);

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

		textFieldRadius.setText(String.valueOf(radius));
		textFieldRadius.setChangedListener(text -> {
			try {
				update(Double.parseDouble(text), true);
			} catch (Exception ignored) {
			}
		});
		update(radius, false);
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

	@Override
	public boolean shouldPause() {
		return false;
	}

	private void update(double newRadius, boolean sendPacket) {
		buttonShape.setMessage((shape == Rail.Shape.QUADRATIC ? TranslationProvider.GUI_MTR_RAIL_SHAPE_QUADRATIC : TranslationProvider.GUI_MTR_RAIL_SHAPE_TWO_RADII).getText());
		radius = Utilities.clamp(Utilities.round(newRadius, 2), 0, maxRadius);
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
		if (sendPacket) {
			RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addRail(Rail.copy(rail, shape, radius))));
		}
	}
}
