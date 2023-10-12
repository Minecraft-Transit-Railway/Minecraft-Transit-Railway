package org.mtr.mod.screen;

import org.mtr.core.data.EnumHelper;
import org.mtr.core.data.TransportMode;
import org.mtr.core.data.Vehicle;
import org.mtr.libraries.com.google.gson.JsonArray;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.Icons;
import org.mtr.mod.client.CustomResources;
import org.mtr.mod.client.DoorAnimationType;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.IResourcePackCreatorProperties;
import org.mtr.mod.data.IGui;
import org.mtr.mod.model.ModelTrainBase;
import org.mtr.mod.packet.IPacket;
import org.mtr.mod.render.RenderTrains;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class ResourcePackCreatorScreen extends ScreenExtension implements IResourcePackCreatorProperties, IGui, Icons {

	private int editingPartIndex = -1;

	private final ButtonWidgetExtension buttonOptions;
	private final DashboardList availableModelPartsList;
	private final DashboardList usedModelPartsList;
	private final WidgetShorterSlider sliderCars;
	private final WidgetShorterSlider sliderBrightness;
	private final ButtonWidgetExtension buttonToggleTrainDirection;
	private final ButtonWidgetExtension buttonDoorLeft;
	private final ButtonWidgetExtension buttonDoorRight;

	private final ButtonWidgetExtension buttonTransportMode;
	private final WidgetShorterSlider sliderLength;
	private final WidgetShorterSlider sliderWidth;
	private final WidgetShorterSlider sliderDoorMax;
	private final ButtonWidgetExtension buttonDoorAnimationType;

	private final CheckboxWidgetExtension checkboxPartMirror;
	private final CheckboxWidgetExtension checkboxPartSkipRenderingIfTooFar;
	private final CheckboxWidgetExtension checkboxIsDisplay;
	private final WidgetShorterSlider sliderDisplayXPadding;
	private final WidgetShorterSlider sliderDisplayYPadding;
	private final WidgetShorterSlider sliderDisplayCjkSizeRatio;
	private final ButtonWidgetExtension buttonDisplayType;
	private final WidgetColorSelector colorSelectorDisplayCjk;
	private final WidgetColorSelector colorSelectorDisplay;
	private final CheckboxWidgetExtension checkboxShouldScroll;
	private final CheckboxWidgetExtension checkboxForceUpperCase;
	private final CheckboxWidgetExtension checkboxForceSingleLine;
	private final TextFieldWidgetExtension textFieldDisplayTest;

	private final ButtonWidgetExtension buttonPartStage;
	private final ButtonWidgetExtension buttonPartDoorOffset;
	private final ButtonWidgetExtension buttonPartRenderCondition;
	private final TextFieldWidgetExtension textFieldPositions;
	private final TextFieldWidgetExtension textFieldWhitelistedCars;
	private final TextFieldWidgetExtension textFieldBlacklistedCars;
	private final ButtonWidgetExtension buttonDone;

	private static int guiCounter;
	private static boolean hideGui;
	private static float translation;
	private static float yaw;
	private static float roll;
	private static float scale = 10;
	private static int cars;
	private static int brightness = 50;
	private static boolean head1IsFront = true;
	private static float doorLeftValue = 0;
	private static float doorRightValue = 0;
	private static boolean openingLeft = false;
	private static boolean openingRight = false;

	private static final int MIN_SCALE = 1;
	private static final float MOUSE_SCALE = 0.005F;
	private static final String DEFAULT_TEST_STRING = "測試|Test";

	public ResourcePackCreatorScreen() {
		super();

		buttonOptions = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("menu.options"), button -> IPacket.openScreen(new ResourcePackCreatorOptionsScreen(this), screenExtension -> screenExtension instanceof ResourcePackCreatorOptionsScreen));
		availableModelPartsList = new DashboardList(null, null, null, null, this::onAdd, null, null, () -> "", text -> {
		});
		usedModelPartsList = new DashboardList(null, null, this::onEdit, null, null, this::onDelete, null, () -> "", text -> {
		});

		sliderCars = new WidgetShorterSlider(0, 0, 31, value -> {
			cars = value + 1;
			updateControls(true);
			return TextHelper.translatable("gui.mtr.vehicle_cars", cars).getString();
		}, null);
		sliderBrightness = new WidgetShorterSlider(0, 0, 100, value -> {
			brightness = value;
			updateControls(true);
			return TextHelper.translatable("gui.mtr.vehicle_brightness", brightness).getString();
		}, null);
		buttonToggleTrainDirection = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> {
			head1IsFront = !head1IsFront;
			updateControls(true);
		});
		buttonDoorLeft = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> {
			openingLeft = !openingLeft;
			updateControls(true);
		});
		buttonDoorRight = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> {
			openingRight = !openingRight;
			updateControls(true);
		});

		buttonTransportMode = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> {
			RenderTrains.creatorProperties.editTransportMode();
			updateControls(true);
		});
		sliderLength = new WidgetShorterSlider(0, 0, 31, value -> {
			RenderTrains.creatorProperties.editLength(value + 1);
			updateControls(true);
			return TextHelper.translatable("gui.mtr.vehicle_length", value + 1).getString();
		}, null);
		sliderWidth = new WidgetShorterSlider(0, 0, 7, value -> {
			RenderTrains.creatorProperties.editWidth(value + 1);
			updateControls(true);
			return TextHelper.translatable("gui.mtr.vehicle_width", value + 1).getString();
		}, null);
		sliderDoorMax = new WidgetShorterSlider(0, 0, 31, value -> {
			RenderTrains.creatorProperties.editDoorMax(value + 1);
			updateControls(true);
			return TextHelper.translatable("gui.mtr.vehicle_door_max", value + 1).getString();
		}, null);
		buttonDoorAnimationType = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> {
			RenderTrains.creatorProperties.editDoorAnimationType();
			updateControls(true);
		});

		checkboxPartMirror = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
			RenderTrains.creatorProperties.editPartMirror(editingPartIndex, checked);
			updateControls(true);
		});
		checkboxPartMirror.setMessage2(new Text(TextHelper.translatable("gui.mtr.part_mirror").data));
		checkboxPartSkipRenderingIfTooFar = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
			RenderTrains.creatorProperties.editPartSkipRenderingIfTooFar(editingPartIndex, checked);
			updateControls(true);
		});
		checkboxPartSkipRenderingIfTooFar.setMessage2(new Text(TextHelper.translatable("gui.mtr.part_skip_rendering_if_too_far").data));
		checkboxIsDisplay = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
			RenderTrains.creatorProperties.editPartDisplay(editingPartIndex, checked);
			updateControls(true);
		});
		checkboxIsDisplay.setMessage2(new Text(TextHelper.translatable("gui.mtr.part_is_display").data));
		sliderDisplayXPadding = new WidgetShorterSlider(0, 0, 40, value -> {
			final float newValue = (value - 20) / 20F;
			RenderTrains.creatorProperties.editPartDisplayPadding(editingPartIndex, newValue, false);
			updateControls(true);
			return TextHelper.translatable("gui.mtr.part_display_x_padding", newValue).getString();
		}, null);
		sliderDisplayYPadding = new WidgetShorterSlider(0, 0, 40, value -> {
			final float newValue = (value - 20) / 20F;
			RenderTrains.creatorProperties.editPartDisplayPadding(editingPartIndex, newValue, true);
			updateControls(true);
			return TextHelper.translatable("gui.mtr.part_display_y_padding", newValue).getString();
		}, null);
		sliderDisplayCjkSizeRatio = new WidgetShorterSlider(0, 0, 40, value -> {
			final float newValue = (value - 20) / 10F;
			RenderTrains.creatorProperties.editPartDisplayCjkSizeRatio(editingPartIndex, newValue);
			updateControls(true);
			return TextHelper.translatable("gui.mtr.part_display_cjk_size_ratio", newValue).getString();
		}, null);
		buttonDisplayType = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> {
			RenderTrains.creatorProperties.editPartDisplayType(editingPartIndex);
			updateControls(true);
		});
		colorSelectorDisplayCjk = new WidgetColorSelector(this, false, () -> onUpdateColor(true));
		colorSelectorDisplay = new WidgetColorSelector(this, false, () -> onUpdateColor(false));
		checkboxShouldScroll = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
			RenderTrains.creatorProperties.editPartDisplayShouldScroll(editingPartIndex, checked);
			updateControls(true);
		});
		checkboxShouldScroll.setMessage2(new Text(TextHelper.translatable("gui.mtr.part_display_should_scroll").data));
		checkboxForceUpperCase = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
			RenderTrains.creatorProperties.editPartDisplayForceUpperCase(editingPartIndex, checked);
			updateControls(true);
		});
		checkboxForceUpperCase.setMessage2(new Text(TextHelper.translatable("gui.mtr.part_display_force_upper_case").data));
		checkboxForceSingleLine = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
			RenderTrains.creatorProperties.editPartDisplayForceSingleLine(editingPartIndex, checked);
			updateControls(true);
		});
		checkboxForceSingleLine.setMessage2(new Text(TextHelper.translatable("gui.mtr.part_display_force_single_line").data));
		textFieldDisplayTest = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, Integer.MAX_VALUE, TextCase.DEFAULT, null, DEFAULT_TEST_STRING);

		buttonPartStage = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> {
			RenderTrains.creatorProperties.editPartRenderStage(editingPartIndex);
			updateControls(true);
		});
		buttonPartDoorOffset = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> {
			RenderTrains.creatorProperties.editPartDoorOffset(editingPartIndex);
			updateControls(true);
		});
		buttonPartRenderCondition = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> {
			RenderTrains.creatorProperties.editPartRenderCondition(editingPartIndex);
			updateControls(true);
		});
		textFieldPositions = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, Integer.MAX_VALUE, TextCase.DEFAULT, "[^\\d.,\\[\\]\\- ]", "[0,0]");
		textFieldWhitelistedCars = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, Integer.MAX_VALUE, TextCase.DEFAULT, "[^%\\d,+\\- ]", "5,-4,%3,%2+1");
		textFieldBlacklistedCars = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, Integer.MAX_VALUE, TextCase.DEFAULT, "[^%\\d,+\\- ]", "5,-4,%3,%2+1");
		buttonDone = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.done"), button -> {
			editingPartIndex = -1;
			updateControls(true);
		});
	}

	@Override
	protected void init2() {
		super.init2();

		IDrawing.setPositionAndWidth(buttonOptions, 0, height - SQUARE_SIZE, PANEL_WIDTH);

		availableModelPartsList.x = 0;
		availableModelPartsList.y = SQUARE_SIZE;
		availableModelPartsList.width = PANEL_WIDTH;
		availableModelPartsList.height = height - SQUARE_SIZE * 2;
		usedModelPartsList.y = SQUARE_SIZE * 9 / 2;
		usedModelPartsList.width = PANEL_WIDTH;
		usedModelPartsList.height = height - SQUARE_SIZE * 9 / 2;

		final int textWidth1 = Math.max(GraphicsHolder.getTextWidth(TextHelper.translatable("gui.mtr.vehicle_cars", 88)), GraphicsHolder.getTextWidth(TextHelper.translatable("gui.mtr.vehicle_brightness", 888)));
		final int remainingWidth = width - PANEL_WIDTH * 2 - TEXT_HEIGHT * 2;
		sliderCars.setX2(PANEL_WIDTH + TEXT_HEIGHT);
		sliderCars.setY2(height - TEXT_HEIGHT - SQUARE_SIZE * 3);
		sliderCars.setWidth2(remainingWidth - TEXT_PADDING - textWidth1);
		sliderCars.setHeight2(SQUARE_SIZE / 2);
		sliderCars.setValue(-1);
		sliderBrightness.setX2(PANEL_WIDTH + TEXT_HEIGHT);
		sliderBrightness.setY2(height - TEXT_HEIGHT - SQUARE_SIZE * 5 / 2);
		sliderBrightness.setWidth2(remainingWidth - TEXT_PADDING - textWidth1);
		sliderBrightness.setHeight2(SQUARE_SIZE / 2);

		IDrawing.setPositionAndWidth(buttonToggleTrainDirection, PANEL_WIDTH + TEXT_HEIGHT, height - TEXT_HEIGHT - SQUARE_SIZE * 2, remainingWidth);
		IDrawing.setPositionAndWidth(buttonDoorLeft, PANEL_WIDTH + TEXT_HEIGHT, height - TEXT_HEIGHT - SQUARE_SIZE, remainingWidth / 2);
		IDrawing.setPositionAndWidth(buttonDoorRight, PANEL_WIDTH + TEXT_HEIGHT + remainingWidth / 2, height - TEXT_HEIGHT - SQUARE_SIZE, remainingWidth / 2);

		final int xStart = width - PANEL_WIDTH;
		IDrawing.setPositionAndWidth(buttonTransportMode, xStart, 0, PANEL_WIDTH);

		final MutableText lengthText = TextHelper.translatable("gui.mtr.vehicle_length", 88);
		final MutableText widthText = TextHelper.translatable("gui.mtr.vehicle_width", 88);
		final MutableText doorMaxText = TextHelper.translatable("gui.mtr.vehicle_door_max", 88);
		final int textWidth2 = Math.max(GraphicsHolder.getTextWidth(lengthText), Math.max(GraphicsHolder.getTextWidth(widthText), GraphicsHolder.getTextWidth(doorMaxText))) + TEXT_PADDING * 2;
		sliderLength.setX2(xStart);
		sliderLength.setY2(SQUARE_SIZE);
		sliderLength.setWidth2(PANEL_WIDTH - textWidth2);
		sliderLength.setHeight2(SQUARE_SIZE / 2);
		sliderLength.setValue(RenderTrains.creatorProperties.getLength() - 1);
		sliderWidth.setX2(xStart);
		sliderWidth.setY2(SQUARE_SIZE * 3 / 2);
		sliderWidth.setWidth2(PANEL_WIDTH - textWidth2);
		sliderWidth.setHeight2(SQUARE_SIZE / 2);
		sliderWidth.setValue(RenderTrains.creatorProperties.getWidth() - 1);
		sliderDoorMax.setX2(xStart);
		sliderDoorMax.setY2(SQUARE_SIZE * 2);
		sliderDoorMax.setWidth2(PANEL_WIDTH - textWidth2);
		sliderDoorMax.setHeight2(SQUARE_SIZE / 2);
		sliderDoorMax.setValue(RenderTrains.creatorProperties.getDoorMax() - 1);

		IDrawing.setPositionAndWidth(buttonDoorAnimationType, xStart, SQUARE_SIZE * 5 / 2, PANEL_WIDTH);

		IDrawing.setPositionAndWidth(checkboxPartMirror, 0, SQUARE_SIZE, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(checkboxPartSkipRenderingIfTooFar, 0, SQUARE_SIZE * 2, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(checkboxIsDisplay, 0, SQUARE_SIZE * 3, PANEL_WIDTH);

		final MutableText displayXPaddingText = TextHelper.translatable("gui.mtr.part_display_x_padding", -8.88);
		final MutableText displayYPaddingText = TextHelper.translatable("gui.mtr.part_display_y_padding", -8.88);
		final MutableText displayCjkSizeRatioText = TextHelper.translatable("gui.mtr.part_display_cjk_size_ratio", -8.88);
		final int textWidth3 = Math.max(Math.max(GraphicsHolder.getTextWidth(displayXPaddingText), GraphicsHolder.getTextWidth(displayYPaddingText)), GraphicsHolder.getTextWidth(displayCjkSizeRatioText)) + TEXT_PADDING * 2;
		sliderDisplayXPadding.setX2(0);
		sliderDisplayXPadding.setY2(SQUARE_SIZE * 4);
		sliderDisplayXPadding.setWidth2(PANEL_WIDTH - textWidth3);
		sliderDisplayXPadding.setHeight2(SQUARE_SIZE / 2);
		sliderDisplayYPadding.setX2(0);
		sliderDisplayYPadding.setY2(SQUARE_SIZE * 9 / 2);
		sliderDisplayYPadding.setWidth2(PANEL_WIDTH - textWidth3);
		sliderDisplayYPadding.setHeight2(SQUARE_SIZE / 2);
		sliderDisplayCjkSizeRatio.setX2(0);
		sliderDisplayCjkSizeRatio.setY2(SQUARE_SIZE * 5);
		sliderDisplayCjkSizeRatio.setWidth2(PANEL_WIDTH - textWidth3);
		sliderDisplayCjkSizeRatio.setHeight2(SQUARE_SIZE / 2);

		IDrawing.setPositionAndWidth(buttonDisplayType, 0, SQUARE_SIZE * 11 / 2, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(colorSelectorDisplayCjk, PANEL_WIDTH - SQUARE_SIZE, SQUARE_SIZE * 13 / 2, SQUARE_SIZE / 2);
		IDrawing.setPositionAndWidth(colorSelectorDisplay, PANEL_WIDTH - SQUARE_SIZE / 2, SQUARE_SIZE * 13 / 2, SQUARE_SIZE / 2);
		IDrawing.setPositionAndWidth(checkboxShouldScroll, 0, SQUARE_SIZE * 15 / 2, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(checkboxForceUpperCase, 0, SQUARE_SIZE * 17 / 2, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(checkboxForceSingleLine, 0, SQUARE_SIZE * 19 / 2, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(textFieldDisplayTest, TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 21 / 2 + TEXT_FIELD_PADDING / 2, PANEL_WIDTH - TEXT_FIELD_PADDING);
		textFieldDisplayTest.setText2(DEFAULT_TEST_STRING);

		IDrawing.setPositionAndWidth(buttonPartStage, xStart, 0, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonPartDoorOffset, xStart, SQUARE_SIZE, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonPartRenderCondition, xStart, SQUARE_SIZE * 2, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(textFieldPositions, width, SQUARE_SIZE * 3 + TEXT_PADDING + TEXT_HEIGHT + TEXT_FIELD_PADDING / 2, PANEL_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldWhitelistedCars, width, SQUARE_SIZE * 4 + TEXT_PADDING * 2 + TEXT_HEIGHT * 2 + TEXT_FIELD_PADDING * 3 / 2, PANEL_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldBlacklistedCars, width, SQUARE_SIZE * 5 + TEXT_PADDING * 3 + TEXT_HEIGHT * 3 + TEXT_FIELD_PADDING * 5 / 2, PANEL_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(buttonDone, xStart, height - SQUARE_SIZE, PANEL_WIDTH);

		textFieldPositions.setChangedListener2(text -> {
			RenderTrains.creatorProperties.editPartPositions(editingPartIndex, textFieldPositions.getText2());
			updateControls(false);
		});
		textFieldWhitelistedCars.setChangedListener2(text -> {
			RenderTrains.creatorProperties.editPartWhitelistedCars(editingPartIndex, textFieldWhitelistedCars.getText2());
			updateControls(false);
		});
		textFieldBlacklistedCars.setChangedListener2(text -> {
			RenderTrains.creatorProperties.editPartBlacklistedCars(editingPartIndex, textFieldBlacklistedCars.getText2());
			updateControls(false);
		});

		updateControls(true);

		addChild(new ClickableWidget(buttonOptions));
		availableModelPartsList.init(this::addChild);
		usedModelPartsList.init(this::addChild);

		addChild(new ClickableWidget(sliderCars));
		addChild(new ClickableWidget(sliderBrightness));
		addChild(new ClickableWidget(buttonToggleTrainDirection));
		addChild(new ClickableWidget(buttonDoorLeft));
		addChild(new ClickableWidget(buttonDoorRight));

		addChild(new ClickableWidget(buttonTransportMode));
		addChild(new ClickableWidget(sliderLength));
		addChild(new ClickableWidget(sliderWidth));
		addChild(new ClickableWidget(sliderDoorMax));
		addChild(new ClickableWidget(buttonDoorAnimationType));

		addChild(new ClickableWidget(checkboxPartMirror));
		addChild(new ClickableWidget(checkboxPartSkipRenderingIfTooFar));
		addChild(new ClickableWidget(checkboxIsDisplay));
		addChild(new ClickableWidget(sliderDisplayXPadding));
		addChild(new ClickableWidget(sliderDisplayYPadding));
		addChild(new ClickableWidget(sliderDisplayCjkSizeRatio));
		addChild(new ClickableWidget(buttonDisplayType));
		addChild(new ClickableWidget(colorSelectorDisplayCjk));
		addChild(new ClickableWidget(colorSelectorDisplay));
		addChild(new ClickableWidget(checkboxShouldScroll));
		addChild(new ClickableWidget(checkboxForceUpperCase));
		addChild(new ClickableWidget(checkboxForceSingleLine));
		addChild(new ClickableWidget(textFieldDisplayTest));

		addChild(new ClickableWidget(buttonPartStage));
		addChild(new ClickableWidget(buttonPartDoorOffset));
		addChild(new ClickableWidget(buttonPartRenderCondition));
		addChild(new ClickableWidget(textFieldPositions));
		addChild(new ClickableWidget(textFieldWhitelistedCars));
		addChild(new ClickableWidget(textFieldBlacklistedCars));
		addChild(new ClickableWidget(buttonDone));
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
		guiDrawing.beginDrawingRectangle();

		if (guiCounter == 0) {
			hideGui = MinecraftClient.getInstance().getOptionsMapped().getHudHiddenMapped();
			guiDrawing.drawRectangle(0, 0, width, height, ARGB_BLACK);
		}

		guiDrawing.drawRectangle(0, 0, PANEL_WIDTH, height, ARGB_BACKGROUND);
		guiDrawing.drawRectangle(width - PANEL_WIDTH, 0, width, height, ARGB_BACKGROUND);
		guiDrawing.finishDrawingRectangle();
		availableModelPartsList.render(graphicsHolder);
		usedModelPartsList.render(graphicsHolder);

		super.render(graphicsHolder, mouseX, mouseY, delta);

		if (isEditing()) {
			graphicsHolder.drawCenteredText(TextHelper.translatable("gui.mtr.editing_part", RenderTrains.creatorProperties.getPropertiesPartsArray().get(editingPartIndex).getAsJsonObject().get(KEY_PROPERTIES_NAME).getAsString()), PANEL_WIDTH / 2, TEXT_PADDING, ARGB_WHITE);
			if (colorSelectorDisplay.visible) {
				graphicsHolder.drawText(TextHelper.translatable(colorSelectorDisplayCjk.visible ? "gui.mtr.part_display_text_color_cjk" : "gui.mtr.part_display_text_color"), TEXT_PADDING, SQUARE_SIZE * 13 / 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			}
			graphicsHolder.drawCenteredText(TextHelper.translatable("gui.mtr.part_positions"), width - PANEL_WIDTH / 2, SQUARE_SIZE * 3 + TEXT_PADDING, ARGB_WHITE);
			graphicsHolder.drawCenteredText(TextHelper.translatable("gui.mtr.part_whitelisted_cars"), width - PANEL_WIDTH / 2, SQUARE_SIZE * 4 + TEXT_PADDING * 2 + TEXT_HEIGHT + TEXT_FIELD_PADDING, ARGB_WHITE);
			graphicsHolder.drawCenteredText(TextHelper.translatable("gui.mtr.part_blacklisted_cars"), width - PANEL_WIDTH / 2, SQUARE_SIZE * 5 + TEXT_PADDING * 3 + TEXT_HEIGHT * 2 + TEXT_FIELD_PADDING * 2, ARGB_WHITE);
		} else {
			graphicsHolder.drawCenteredText(TextHelper.translatable("gui.mtr.available_model_parts"), PANEL_WIDTH / 2, TEXT_PADDING, ARGB_WHITE);
			graphicsHolder.drawCenteredText(TextHelper.translatable("gui.mtr.used_model_parts"), width - PANEL_WIDTH / 2, SQUARE_SIZE * 7 / 2 + TEXT_PADDING, ARGB_WHITE);
		}
		guiCounter = 2;
	}

	@Override
	public void mouseMoved2(double mouseX, double mouseY) {
		availableModelPartsList.mouseMoved(mouseX, mouseY);
		usedModelPartsList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled3(double mouseX, double mouseY, double amount) {
		if (mouseX >= PANEL_WIDTH && mouseX < width - PANEL_WIDTH) {
			scale = Math.max(scale - (float) amount, MIN_SCALE);
			final float bound = cars * RenderTrains.creatorProperties.getLength() / 2F;
			translation = MathHelper.clamp(translation, -bound, bound);
		}
		availableModelPartsList.mouseScrolled(mouseX, mouseY, amount);
		usedModelPartsList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled3(mouseX, mouseY, amount);
	}

	@Override
	public boolean mouseDragged2(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (mouseX >= PANEL_WIDTH && mouseX < width - PANEL_WIDTH && mouseY < height - TEXT_HEIGHT - SQUARE_SIZE * 3) {
			if (button == 0) {
				final Vector3d movement = new Vector3d(0, deltaY * MOUSE_SCALE * scale, deltaX * MOUSE_SCALE * scale).rotateY(yaw).rotateZ(roll);
				final float bound = cars * RenderTrains.creatorProperties.getLength() / 2F;
				translation = MathHelper.clamp(translation - (float) movement.getZMapped(), -bound, bound);
			} else {
				yaw -= (float) deltaX * MOUSE_SCALE * scale;
				roll -= (float) (deltaY * MOUSE_SCALE * scale * Math.cos(yaw));
			}
		}
		return super.mouseDragged2(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public void tick2() {
		availableModelPartsList.tick();
		usedModelPartsList.tick();
		textFieldPositions.tick3();
		textFieldWhitelistedCars.tick3();
		textFieldBlacklistedCars.tick3();

		final float maxTime = EnumHelper.valueOf(DoorAnimationType.STANDARD, RenderTrains.creatorProperties.getDoorAnimationType()).maxTime;
		final float increment = 1F / Vehicle.DOOR_MOVE_TIME;
		if (openingLeft) {
			if (doorLeftValue < maxTime) {
				doorLeftValue = Math.min(maxTime, doorLeftValue + increment);
			}
		} else {
			if (doorLeftValue > 0) {
				doorLeftValue = Math.max(0, doorLeftValue - increment);
			}
		}
		if (openingRight) {
			if (doorRightValue < maxTime) {
				doorRightValue = Math.min(maxTime, doorRightValue + increment);
			}
		} else {
			if (doorRightValue > 0) {
				doorRightValue = Math.max(0, doorRightValue - increment);
			}
		}
	}

	public String getTestText() {
		return IGui.textOrUntitled(textFieldDisplayTest.getText2());
	}

	private ObjectArrayList<DashboardListItem> updatePartsList(JsonArray jsonArray, @Nullable ToIntFunction<JsonObject> color, @Nullable Function<JsonObject, String> extraText) {
		final ObjectArrayList<DashboardListItem> listData = new ObjectArrayList<>();
		if (!jsonArray.isEmpty()) {
			try {
				jsonArray.forEach(jsonElement -> {
					final JsonObject jsonObject = jsonElement.getAsJsonObject();
					final String name = jsonObject.get("name").getAsString();
					listData.add(new DashboardListItem(0, extraText == null ? name : extraText.apply(jsonObject).replace("%s", name), ARGB_BLACK | (color == null ? 0 : color.applyAsInt(jsonObject))));
				});
			} catch (Exception ignored) {
			}
		}
		return listData;
	}

	private void updateControls(boolean formatTextFields) {
		availableModelPartsList.setData(updatePartsList(RenderTrains.creatorProperties.getModelPartsArray(), null, null), false, false, false, false, true, false);
		final JsonArray partsArray = RenderTrains.creatorProperties.getPropertiesPartsArray();
		usedModelPartsList.setData(updatePartsList(partsArray, ResourcePackCreatorScreen::getColor, ResourcePackCreatorScreen::getName), false, false, true, false, false, true);

		final String transportModeString = RenderTrains.creatorProperties.getTransportMode();
		buttonTransportMode.setMessage2(new Text(TextHelper.translatable("gui.mtr.transport_mode_" + transportModeString.toLowerCase(Locale.ENGLISH)).data));
		cars = MathHelper.clamp(cars, 1, Math.min(32, EnumHelper.valueOf(TransportMode.TRAIN, transportModeString).maxLength));
		buttonDoorAnimationType.setMessage2(new Text(TextHelper.translatable("gui.mtr.door_animation_type_" + RenderTrains.creatorProperties.getDoorAnimationType().toLowerCase(Locale.ENGLISH)).data));

		final int sliderCarsValue = cars - 1;
		if (sliderCarsValue != sliderCars.getIntValue()) {
			sliderCars.setValue(sliderCarsValue);
		}
		if (brightness != sliderBrightness.getIntValue()) {
			sliderBrightness.setValue(brightness);
		}

		buttonToggleTrainDirection.setMessage2(new Text(TextHelper.translatable("gui.mtr.vehicle_direction_" + (head1IsFront ? "forwards" : "backwards")).data));
		buttonDoorLeft.setMessage2(new Text(TextHelper.literal(openingLeft ? DOOR_CLOSE : DOOR_OPEN).append(" ").append(TextHelper.translatable("gui.mtr.left").getString()).data));
		buttonDoorRight.setMessage2(new Text(TextHelper.literal(openingRight ? DOOR_CLOSE : DOOR_OPEN).append(" ").append(TextHelper.translatable("gui.mtr.right").getString()).data));

		final int sliderLengthValue = RenderTrains.creatorProperties.getLength() - 1;
		if (sliderLengthValue != sliderLength.getIntValue()) {
			sliderLength.setValue(sliderLengthValue);
		}
		final int sliderWidthValue = RenderTrains.creatorProperties.getWidth() - 1;
		if (sliderWidthValue != sliderWidth.getIntValue()) {
			sliderWidth.setValue(sliderWidthValue);
		}
		final int sliderDoorMaxValue = RenderTrains.creatorProperties.getDoorMax() - 1;
		if (sliderDoorMaxValue != sliderDoorMax.getIntValue()) {
			sliderDoorMax.setValue(sliderDoorMaxValue);
		}

		if (isEditing()) {
			buttonOptions.visible = false;
			buttonTransportMode.visible = false;
			sliderLength.visible = false;
			sliderWidth.visible = false;
			sliderDoorMax.visible = false;
			buttonDoorAnimationType.visible = false;

			checkboxPartMirror.visible = true;
			checkboxPartSkipRenderingIfTooFar.visible = true;
			checkboxIsDisplay.visible = true;

			buttonPartStage.visible = true;
			buttonPartDoorOffset.visible = true;
			buttonPartRenderCondition.visible = true;

			textFieldPositions.setX2(width - PANEL_WIDTH + TEXT_FIELD_PADDING / 2);
			textFieldWhitelistedCars.setX2(width - PANEL_WIDTH + TEXT_FIELD_PADDING / 2);
			textFieldBlacklistedCars.setX2(width - PANEL_WIDTH + TEXT_FIELD_PADDING / 2);
			buttonDone.visible = true;
			availableModelPartsList.x = width;
			usedModelPartsList.x = width;

			final JsonObject partObject = partsArray.get(editingPartIndex).getAsJsonObject();

			checkboxPartMirror.setChecked(partObject.get(KEY_PROPERTIES_MIRROR).getAsBoolean());
			checkboxPartSkipRenderingIfTooFar.setChecked(partObject.get(KEY_PROPERTIES_SKIP_RENDERING_IF_TOO_FAR).getAsBoolean());
			final boolean hasDisplay = partObject.has(KEY_PROPERTIES_DISPLAY);
			checkboxIsDisplay.setChecked(hasDisplay);

			sliderDisplayXPadding.visible = hasDisplay;
			sliderDisplayYPadding.visible = hasDisplay;
			sliderDisplayCjkSizeRatio.visible = hasDisplay;
			buttonDisplayType.visible = hasDisplay;
			colorSelectorDisplayCjk.visible = hasDisplay;
			colorSelectorDisplay.visible = hasDisplay;
			checkboxShouldScroll.visible = hasDisplay;
			checkboxForceUpperCase.visible = hasDisplay;
			checkboxForceSingleLine.visible = hasDisplay;
			textFieldDisplayTest.setX2((hasDisplay ? 0 : width) + TEXT_FIELD_PADDING / 2);

			if (hasDisplay) {
				final boolean shouldScroll = partObject.getAsJsonObject(KEY_PROPERTIES_DISPLAY).get(KEY_PROPERTIES_DISPLAY_SHOULD_SCROLL).getAsBoolean();
				final boolean forceSingleLine = partObject.getAsJsonObject(KEY_PROPERTIES_DISPLAY).get(KEY_PROPERTIES_DISPLAY_FORCE_SINGLE_LINE).getAsBoolean();

				final int sliderDisplayXPaddingValue = Math.round(partObject.getAsJsonObject(KEY_PROPERTIES_DISPLAY).get(KEY_PROPERTIES_DISPLAY_X_PADDING).getAsFloat() * 20 + 20);
				if (sliderDisplayXPaddingValue != sliderDisplayXPadding.getIntValue()) {
					sliderDisplayXPadding.setValue(sliderDisplayXPaddingValue);
				}
				final int sliderDisplayYPaddingValue = Math.round(partObject.getAsJsonObject(KEY_PROPERTIES_DISPLAY).get(KEY_PROPERTIES_DISPLAY_Y_PADDING).getAsFloat() * 20 + 20);
				if (sliderDisplayYPaddingValue != sliderDisplayYPadding.getIntValue()) {
					sliderDisplayYPadding.setValue(sliderDisplayYPaddingValue);
				}
				final int sliderDisplayCjkSizeRatio = (forceSingleLine ? 0 : Math.round(partObject.getAsJsonObject(KEY_PROPERTIES_DISPLAY).get(KEY_PROPERTIES_DISPLAY_CJK_SIZE_RATIO).getAsFloat() * 10)) + 20;
				if (sliderDisplayCjkSizeRatio != this.sliderDisplayCjkSizeRatio.getIntValue()) {
					this.sliderDisplayCjkSizeRatio.setValue(sliderDisplayCjkSizeRatio);
				}

				colorSelectorDisplayCjk.visible = !shouldScroll;
				this.sliderDisplayCjkSizeRatio.visible = !forceSingleLine;

				buttonDisplayType.setMessage2(new Text(TextHelper.translatable("gui.mtr.part_display_type_" + partObject.getAsJsonObject(KEY_PROPERTIES_DISPLAY).get(KEY_PROPERTIES_DISPLAY_TYPE).getAsString().toLowerCase(Locale.ENGLISH)).data));
				colorSelectorDisplayCjk.setColor(CustomResources.colorStringToInt(partObject.getAsJsonObject(KEY_PROPERTIES_DISPLAY).get(KEY_PROPERTIES_DISPLAY_COLOR_CJK).getAsString()));
				colorSelectorDisplay.setColor(CustomResources.colorStringToInt(partObject.getAsJsonObject(KEY_PROPERTIES_DISPLAY).get(KEY_PROPERTIES_DISPLAY_COLOR).getAsString()));
				checkboxShouldScroll.setChecked(shouldScroll);
				checkboxForceUpperCase.setChecked(partObject.getAsJsonObject(KEY_PROPERTIES_DISPLAY).get(KEY_PROPERTIES_DISPLAY_FORCE_UPPER_CASE).getAsBoolean());
				checkboxForceSingleLine.setChecked(forceSingleLine);
			}

			buttonPartStage.setMessage2(new Text(TextHelper.translatable("gui.mtr.part_stage_" + partObject.get(KEY_PROPERTIES_STAGE).getAsString().toLowerCase(Locale.ENGLISH)).data));
			buttonPartDoorOffset.setMessage2(new Text(TextHelper.translatable("gui.mtr.part_door_offset_" + partObject.get(KEY_PROPERTIES_DOOR_OFFSET).getAsString().toLowerCase(Locale.ENGLISH)).data));
			buttonPartRenderCondition.setMessage2(new Text(TextHelper.translatable("gui.mtr.part_render_condition_" + partObject.get(KEY_PROPERTIES_RENDER_CONDITION).getAsString().toLowerCase(Locale.ENGLISH)).data));

			if (formatTextFields) {
				final StringBuilder positionsString = new StringBuilder();
				final JsonArray positionsArray = partObject.getAsJsonArray(KEY_PROPERTIES_POSITIONS);
				for (int i = 0; i < positionsArray.size(); i++) {
					try {
						final JsonArray positionArray = positionsArray.get(i).getAsJsonArray();
						positionsString.append("[").append(positionArray.get(0).getAsFloat()).append(",").append(positionArray.get(1).getAsFloat()).append("],");
					} catch (Exception ignored) {
					}
				}
				textFieldPositions.setText2(positionsString.toString().replace(".0,", ",").replace(".0]", "]"));
				textFieldWhitelistedCars.setText2(partObject.get(KEY_PROPERTIES_WHITELISTED_CARS).toString());
				textFieldBlacklistedCars.setText2(partObject.get(KEY_PROPERTIES_BLACKLISTED_CARS).toString());
			}
		} else {
			buttonOptions.visible = true;
			buttonTransportMode.visible = true;
			sliderLength.visible = true;
			sliderWidth.visible = true;
			sliderDoorMax.visible = true;
			buttonDoorAnimationType.visible = true;

			checkboxPartMirror.visible = false;
			checkboxPartSkipRenderingIfTooFar.visible = false;
			checkboxIsDisplay.visible = false;
			sliderDisplayXPadding.visible = false;
			sliderDisplayYPadding.visible = false;
			sliderDisplayCjkSizeRatio.visible = false;
			buttonDisplayType.visible = false;
			colorSelectorDisplayCjk.visible = false;
			colorSelectorDisplay.visible = false;
			checkboxShouldScroll.visible = false;
			checkboxForceUpperCase.visible = false;
			checkboxForceSingleLine.visible = false;
			textFieldDisplayTest.setX2(width + TEXT_FIELD_PADDING / 2);

			buttonPartStage.visible = false;
			buttonPartDoorOffset.visible = false;
			buttonPartRenderCondition.visible = false;

			textFieldPositions.setX2(width + TEXT_FIELD_PADDING / 2);
			textFieldWhitelistedCars.setX2(width + TEXT_FIELD_PADDING / 2);
			textFieldBlacklistedCars.setX2(width + TEXT_FIELD_PADDING / 2);
			buttonDone.visible = false;
			availableModelPartsList.x = 0;
			usedModelPartsList.x = width - PANEL_WIDTH;
		}
	}

	private void onAdd(DashboardListItem dashboardListItem, int index) {
		RenderTrains.creatorProperties.addPart(dashboardListItem.getName(true));
		updateControls(true);
	}

	private void onEdit(DashboardListItem dashboardListItem, int index) {
		editingPartIndex = index;
		updateControls(true);
	}

	private void onDelete(DashboardListItem dashboardListItem, int index) {
		RenderTrains.creatorProperties.removePart(index);
		editingPartIndex = -1;
		updateControls(true);
	}

	private void onUpdateColor(boolean isCjk) {
		RenderTrains.creatorProperties.editPartDisplayColor(editingPartIndex, (isCjk ? colorSelectorDisplayCjk : colorSelectorDisplay).getColor(), isCjk);
		updateControls(false);
	}

	private boolean isEditing() {
		return editingPartIndex >= 0 && editingPartIndex < RenderTrains.creatorProperties.getPropertiesPartsArray().size();
	}

	public static void render(GraphicsHolder graphicsHolder) {
		if (guiCounter > 0) {
			guiCounter--;
			MinecraftClient.getInstance().getOptionsMapped().setHudHiddenMapped(guiCounter != 0 || hideGui);

			graphicsHolder.push();

			final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
			guiDrawing.beginDrawingRectangle();
			guiDrawing.drawRectangle(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, ARGB_BLACK);
			guiDrawing.finishDrawingRectangle();

			graphicsHolder.translate(0, 0, -scale);
			graphicsHolder.rotateYDegrees(90);
			graphicsHolder.rotateXDegrees(180);
			graphicsHolder.rotateYDegrees(yaw);
			graphicsHolder.rotateZDegrees(roll);
			final int light = LightmapTextureManager.pack(0, (int) Math.round(brightness / 100D * 0x0F));
			for (int i = 0; i < cars; i++) {
				graphicsHolder.push();
				graphicsHolder.translate(0, 0, (i - (cars - 1) / 2F) * (RenderTrains.creatorProperties.getLength() + 1) + translation);
				RenderTrains.creatorProperties.render(i, cars, head1IsFront, doorLeftValue, doorRightValue, openingRight || openingLeft, light);
				graphicsHolder.pop();
			}
			graphicsHolder.pop();
		}
	}

	private static int getColor(JsonObject jsonObject) {
		final ModelTrainBase.RenderStage renderStage = EnumHelper.valueOf(ModelTrainBase.RenderStage.EXTERIOR, jsonObject.get(KEY_PROPERTIES_STAGE).getAsString());
		switch (renderStage) {
			case EXTERIOR:
				return 0x61BD4F;
			case INTERIOR:
				return 0xE53935;
			case INTERIOR_TRANSLUCENT:
				return 0xFB8C00;
			case LIGHT:
				return 0xF2D600;
			case ALWAYS_ON_LIGHT:
				return 0x1E88E5;
			default:
				return 0;
		}
	}

	private static String getName(JsonObject jsonObject) {
		final boolean mirror = jsonObject.get(KEY_PROPERTIES_MIRROR).getAsBoolean();
		final int positionCount = jsonObject.getAsJsonArray(KEY_PROPERTIES_POSITIONS).size();
		return (mirror ? "*" : "") + "%s (" + positionCount + "x)";
	}
}
