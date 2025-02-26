package org.mtr.mod.screen;

import org.mtr.core.data.Rail;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketUpdateData;
import org.mtr.mod.packet.PacketUpdateLastRailStyles;

import java.util.stream.Collectors;

public class RailModifierScreen extends MTRScreenBase implements IGui {

	private Rail.Shape shape;
	private double radius;
	private final int buttonsWidth;

	private final Rail rail;
	private final double maxRadius;
	private final ButtonWidgetExtension buttonShape;
	private final ButtonWidgetExtension buttonStyle;
	private final ButtonWidgetExtension buttonStyleFlip;
	private final ButtonWidgetExtension buttonMinus2;
	private final ButtonWidgetExtension buttonMinus1;
	private final ButtonWidgetExtension buttonMinus0;
	private final ButtonWidgetExtension buttonPlus0;
	private final ButtonWidgetExtension buttonPlus1;
	private final ButtonWidgetExtension buttonPlus2;
	private final TextFieldWidgetExtension textFieldRadius;
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

		buttonShape = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal(""), button -> {
			shape = shape == Rail.Shape.QUADRATIC ? Rail.Shape.TWO_RADII : Rail.Shape.QUADRATIC;
			update(radius, true);
		});
		buttonStyle = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("selectWorld.edit"), button -> {
			if (rail != null) {
				MinecraftClient.getInstance().openScreen(new Screen(RailStyleSelectorScreen.create(rail)));
			}
		});
		buttonStyleFlip = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_FLIP_STYLES.getMutableText(), button -> {
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
				InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addRail(Rail.copy(rail, styles))));
				final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
				if (clientPlayerEntity != null) {
					InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateLastRailStyles(clientPlayerEntity.getUuid(), rail.getTransportMode(), styles));
				}
				MinecraftClient.getInstance().openScreen(null);
			}
		});
		buttonMinus2 = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("-10"), button -> update(radius - 10, true));
		buttonMinus1 = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("-1"), button -> update(radius - 1, true));
		buttonMinus0 = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("-0.1"), button -> update(radius - 0.1, true));
		buttonPlus0 = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("+0.1"), button -> update(radius + 0.1, true));
		buttonPlus1 = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("+1"), button -> update(radius + 1, true));
		buttonPlus2 = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("+10"), button -> update(radius + 10, true));
		textFieldRadius = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 256, TextCase.DEFAULT, "[^\\d\\.]", "0");
		xStart = Math.max(GraphicsHolder.getTextWidth(shapeText), GraphicsHolder.getTextWidth(radiusText)) + TEXT_PADDING * 2;
		buttonsWidth = SQUARE_SIZE * 12 + GraphicsHolder.getTextWidth(styleText) + TEXT_PADDING * 2;
	}

	@Override
	protected void init2() {
		super.init2();

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

		addChild(new ClickableWidget(buttonShape));
		addChild(new ClickableWidget(buttonStyle));
		addChild(new ClickableWidget(buttonStyleFlip));
		addChild(new ClickableWidget(buttonMinus2));
		addChild(new ClickableWidget(buttonMinus1));
		addChild(new ClickableWidget(buttonMinus0));
		addChild(new ClickableWidget(buttonPlus0));
		addChild(new ClickableWidget(buttonPlus1));
		addChild(new ClickableWidget(buttonPlus2));
		addChild(new ClickableWidget(textFieldRadius));

		textFieldRadius.setText2(String.valueOf(radius));
		textFieldRadius.setChangedListener2(text -> {
			try {
				update(Double.parseDouble(text), true);
			} catch (Exception ignored) {
			}
		});
		update(radius, false);
	}

	@Override
	public void tick2() {
		super.tick2();
		textFieldRadius.tick2();
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
		guiDrawing.beginDrawingRectangle();
		guiDrawing.drawRectangle(0, 0, width, shape == Rail.Shape.QUADRATIC ? SQUARE_SIZE : SQUARE_SIZE * 2 + TEXT_FIELD_PADDING, ARGB_BACKGROUND);
		guiDrawing.finishDrawingRectangle();
		super.render(graphicsHolder, mouseX, mouseY, delta);
		graphicsHolder.drawText(shapeText, TEXT_PADDING, TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.drawText(styleText, xStart + SQUARE_SIZE * 6 + TEXT_PADDING, TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		if (shape != Rail.Shape.QUADRATIC) {
			graphicsHolder.drawText(radiusText, TEXT_PADDING, SQUARE_SIZE + TEXT_PADDING + TEXT_FIELD_PADDING / 2, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		}
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}

	private void update(double newRadius, boolean sendPacket) {
		buttonShape.setMessage2((shape == Rail.Shape.QUADRATIC ? TranslationProvider.GUI_MTR_RAIL_SHAPE_QUADRATIC : TranslationProvider.GUI_MTR_RAIL_SHAPE_TWO_RADII).getText());
		radius = Utilities.clamp(Utilities.round(newRadius, 2), 0, maxRadius);
		textFieldRadius.setX2((shape == Rail.Shape.QUADRATIC ? width : xStart + SQUARE_SIZE * 6) + TEXT_FIELD_PADDING / 2);
		buttonMinus2.setVisibleMapped(shape != Rail.Shape.QUADRATIC);
		buttonMinus1.setVisibleMapped(shape != Rail.Shape.QUADRATIC);
		buttonMinus0.setVisibleMapped(shape != Rail.Shape.QUADRATIC);
		buttonPlus0.setVisibleMapped(shape != Rail.Shape.QUADRATIC);
		buttonPlus1.setVisibleMapped(shape != Rail.Shape.QUADRATIC);
		buttonPlus2.setVisibleMapped(shape != Rail.Shape.QUADRATIC);
		buttonMinus2.setActiveMapped(radius > 0);
		buttonMinus1.setActiveMapped(radius > 0);
		buttonMinus0.setActiveMapped(radius > 0);
		buttonPlus0.setActiveMapped(radius < maxRadius);
		buttonPlus1.setActiveMapped(radius < maxRadius);
		buttonPlus2.setActiveMapped(radius < maxRadius);

		try {
			if (Double.parseDouble(textFieldRadius.getText2()) != radius) {
				textFieldRadius.setText2(String.valueOf(radius));
			}
		} catch (Exception ignored) {
		}
		if (sendPacket) {
			InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getInstance()).addRail(Rail.copy(rail, shape, radius))));
		}
	}
}
