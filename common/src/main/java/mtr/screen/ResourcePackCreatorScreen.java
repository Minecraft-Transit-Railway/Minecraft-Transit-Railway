package mtr.screen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import mtr.Icons;
import mtr.client.CustomResources;
import mtr.client.DoorAnimationType;
import mtr.client.IDrawing;
import mtr.client.IResourcePackCreatorProperties;
import mtr.data.*;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.model.ModelTrainBase;
import mtr.render.RenderTrains;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class ResourcePackCreatorScreen extends ScreenMapper implements IResourcePackCreatorProperties, IGui, Icons {

	private int editingPartIndex = -1;

	private final Button buttonOptions;
	private final DashboardList availableModelPartsList;
	private final DashboardList usedModelPartsList;
	private final WidgetShorterSlider sliderCars;
	private final WidgetShorterSlider sliderBrightness;
	private final Button buttonToggleTrainDirection;
	private final Button buttonDoorLeft;
	private final Button buttonDoorRight;

	private final Button buttonTransportMode;
	private final WidgetShorterSlider sliderLength;
	private final WidgetShorterSlider sliderWidth;
	private final WidgetShorterSlider sliderDoorMax;
	private final Button buttonDoorAnimationType;

	private final WidgetBetterCheckbox checkboxPartMirror;
	private final WidgetBetterCheckbox checkboxPartSkipRenderingIfTooFar;
	private final WidgetBetterCheckbox checkboxIsDisplay;
	private final WidgetShorterSlider sliderDisplayXPadding;
	private final WidgetShorterSlider sliderDisplayYPadding;
	private final WidgetShorterSlider sliderDisplayCjkSizeRatio;
	private final Button buttonDisplayType;
	private final WidgetColorSelector colorSelectorDisplayCjk;
	private final WidgetColorSelector colorSelectorDisplay;
	private final WidgetBetterCheckbox checkboxShouldScroll;
	private final WidgetBetterCheckbox checkboxForceUpperCase;
	private final WidgetBetterCheckbox checkboxForceSingleLine;
	private final WidgetBetterTextField textFieldDisplayTest;

	private final Button buttonPartStage;
	private final Button buttonPartDoorOffset;
	private final Button buttonPartRenderCondition;
	private final WidgetBetterTextField textFieldPositions;
	private final WidgetBetterTextField textFieldWhitelistedCars;
	private final WidgetBetterTextField textFieldBlacklistedCars;
	private final Button buttonDone;

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
		super(Text.literal(""));

		buttonOptions = UtilitiesClient.newButton(Text.translatable("menu.options"), button -> {
			if (minecraft != null) {
				UtilitiesClient.setScreen(minecraft, new ResourcePackCreatorOptionsScreen(this));
			}
		});
		availableModelPartsList = new DashboardList(null, null, null, null, this::onAdd, null, null, () -> "", text -> {
		});
		usedModelPartsList = new DashboardList(null, null, this::onEdit, null, null, this::onDelete, null, () -> "", text -> {
		});

		sliderCars = new WidgetShorterSlider(0, 0, 31, value -> {
			cars = value + 1;
			updateControls(true);
			return Text.translatable("gui.mtr.vehicle_cars", cars).getString();
		}, null);
		sliderBrightness = new WidgetShorterSlider(0, 0, 100, value -> {
			brightness = value;
			updateControls(true);
			return Text.translatable("gui.mtr.vehicle_brightness", brightness).getString();
		}, null);
		buttonToggleTrainDirection = UtilitiesClient.newButton(button -> {
			head1IsFront = !head1IsFront;
			updateControls(true);
		});
		buttonDoorLeft = UtilitiesClient.newButton(button -> {
			openingLeft = !openingLeft;
			updateControls(true);
		});
		buttonDoorRight = UtilitiesClient.newButton(button -> {
			openingRight = !openingRight;
			updateControls(true);
		});

		buttonTransportMode = UtilitiesClient.newButton(button -> {
			RenderTrains.creatorProperties.editTransportMode();
			updateControls(true);
		});
		sliderLength = new WidgetShorterSlider(0, 0, 31, value -> {
			RenderTrains.creatorProperties.editLength(value + 1);
			updateControls(true);
			return Text.translatable("gui.mtr.vehicle_length", value + 1).getString();
		}, null);
		sliderWidth = new WidgetShorterSlider(0, 0, 7, value -> {
			RenderTrains.creatorProperties.editWidth(value + 1);
			updateControls(true);
			return Text.translatable("gui.mtr.vehicle_width", value + 1).getString();
		}, null);
		sliderDoorMax = new WidgetShorterSlider(0, 0, 31, value -> {
			RenderTrains.creatorProperties.editDoorMax(value + 1);
			updateControls(true);
			return Text.translatable("gui.mtr.vehicle_door_max", value + 1).getString();
		}, null);
		buttonDoorAnimationType = UtilitiesClient.newButton(button -> {
			RenderTrains.creatorProperties.editDoorAnimationType();
			updateControls(true);
		});

		checkboxPartMirror = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.part_mirror"), checked -> {
			RenderTrains.creatorProperties.editPartMirror(editingPartIndex, checked);
			updateControls(true);
		});
		checkboxPartSkipRenderingIfTooFar = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.part_skip_rendering_if_too_far"), checked -> {
			RenderTrains.creatorProperties.editPartSkipRenderingIfTooFar(editingPartIndex, checked);
			updateControls(true);
		});
		checkboxIsDisplay = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.part_is_display"), checked -> {
			RenderTrains.creatorProperties.editPartDisplay(editingPartIndex, checked);
			updateControls(true);
		});
		sliderDisplayXPadding = new WidgetShorterSlider(0, 0, 40, value -> {
			final float newValue = (value - 20) / 20F;
			RenderTrains.creatorProperties.editPartDisplayPadding(editingPartIndex, newValue, false);
			updateControls(true);
			return Text.translatable("gui.mtr.part_display_x_padding", newValue).getString();
		}, null);
		sliderDisplayYPadding = new WidgetShorterSlider(0, 0, 40, value -> {
			final float newValue = (value - 20) / 20F;
			RenderTrains.creatorProperties.editPartDisplayPadding(editingPartIndex, newValue, true);
			updateControls(true);
			return Text.translatable("gui.mtr.part_display_y_padding", newValue).getString();
		}, null);
		sliderDisplayCjkSizeRatio = new WidgetShorterSlider(0, 0, 40, value -> {
			final float newValue = (value - 20) / 10F;
			RenderTrains.creatorProperties.editPartDisplayCjkSizeRatio(editingPartIndex, newValue);
			updateControls(true);
			return Text.translatable("gui.mtr.part_display_cjk_size_ratio", newValue).getString();
		}, null);
		buttonDisplayType = UtilitiesClient.newButton(button -> {
			RenderTrains.creatorProperties.editPartDisplayType(editingPartIndex);
			updateControls(true);
		});
		colorSelectorDisplayCjk = new WidgetColorSelector(this, false, () -> onUpdateColor(true));
		colorSelectorDisplay = new WidgetColorSelector(this, false, () -> onUpdateColor(false));
		checkboxShouldScroll = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.part_display_should_scroll"), checked -> {
			RenderTrains.creatorProperties.editPartDisplayShouldScroll(editingPartIndex, checked);
			updateControls(true);
		});
		checkboxForceUpperCase = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.part_display_force_upper_case"), checked -> {
			RenderTrains.creatorProperties.editPartDisplayForceUpperCase(editingPartIndex, checked);
			updateControls(true);
		});
		checkboxForceSingleLine = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.part_display_force_single_line"), checked -> {
			RenderTrains.creatorProperties.editPartDisplayForceSingleLine(editingPartIndex, checked);
			updateControls(true);
		});
		textFieldDisplayTest = new WidgetBetterTextField(DEFAULT_TEST_STRING, Integer.MAX_VALUE);

		buttonPartStage = UtilitiesClient.newButton(button -> {
			RenderTrains.creatorProperties.editPartRenderStage(editingPartIndex);
			updateControls(true);
		});
		buttonPartDoorOffset = UtilitiesClient.newButton(button -> {
			RenderTrains.creatorProperties.editPartDoorOffset(editingPartIndex);
			updateControls(true);
		});
		buttonPartRenderCondition = UtilitiesClient.newButton(button -> {
			RenderTrains.creatorProperties.editPartRenderCondition(editingPartIndex);
			updateControls(true);
		});
		textFieldPositions = new WidgetBetterTextField("[^\\d.,\\[\\]\\- ]", "[0,0]", Integer.MAX_VALUE);
		textFieldWhitelistedCars = new WidgetBetterTextField("[^%\\d,+\\- ]", "5,-4,%3,%2+1", Integer.MAX_VALUE);
		textFieldBlacklistedCars = new WidgetBetterTextField("[^%\\d,+\\- ]", "5,-4,%3,%2+1", Integer.MAX_VALUE);
		buttonDone = UtilitiesClient.newButton(Text.translatable("gui.done"), button -> {
			editingPartIndex = -1;
			updateControls(true);
		});
	}

	@Override
	protected void init() {
		super.init();

		IDrawing.setPositionAndWidth(buttonOptions, 0, height - SQUARE_SIZE, PANEL_WIDTH);

		availableModelPartsList.x = 0;
		availableModelPartsList.y = SQUARE_SIZE;
		availableModelPartsList.width = PANEL_WIDTH;
		availableModelPartsList.height = height - SQUARE_SIZE * 2;
		usedModelPartsList.y = SQUARE_SIZE * 9 / 2;
		usedModelPartsList.width = PANEL_WIDTH;
		usedModelPartsList.height = height - SQUARE_SIZE * 9 / 2;

		final int textWidth1 = Math.max(font.width(Text.translatable("gui.mtr.vehicle_cars", 88)), font.width(Text.translatable("gui.mtr.vehicle_brightness", 888)));
		final int remainingWidth = width - PANEL_WIDTH * 2 - TEXT_HEIGHT * 2;
		UtilitiesClient.setWidgetX(sliderCars, PANEL_WIDTH + TEXT_HEIGHT);
		UtilitiesClient.setWidgetY(sliderCars, height - TEXT_HEIGHT - SQUARE_SIZE * 3);
		sliderCars.setWidth(remainingWidth - TEXT_PADDING - textWidth1);
		sliderCars.setHeight(SQUARE_SIZE / 2);
		sliderCars.setValue(-1);
		UtilitiesClient.setWidgetX(sliderBrightness, PANEL_WIDTH + TEXT_HEIGHT);
		UtilitiesClient.setWidgetY(sliderBrightness, height - TEXT_HEIGHT - SQUARE_SIZE * 5 / 2);
		sliderBrightness.setWidth(remainingWidth - TEXT_PADDING - textWidth1);
		sliderBrightness.setHeight(SQUARE_SIZE / 2);

		IDrawing.setPositionAndWidth(buttonToggleTrainDirection, PANEL_WIDTH + TEXT_HEIGHT, height - TEXT_HEIGHT - SQUARE_SIZE * 2, remainingWidth);
		IDrawing.setPositionAndWidth(buttonDoorLeft, PANEL_WIDTH + TEXT_HEIGHT, height - TEXT_HEIGHT - SQUARE_SIZE, remainingWidth / 2);
		IDrawing.setPositionAndWidth(buttonDoorRight, PANEL_WIDTH + TEXT_HEIGHT + remainingWidth / 2, height - TEXT_HEIGHT - SQUARE_SIZE, remainingWidth / 2);

		final int xStart = width - PANEL_WIDTH;
		IDrawing.setPositionAndWidth(buttonTransportMode, xStart, 0, PANEL_WIDTH);

		final Component lengthText = Text.translatable("gui.mtr.vehicle_length", 88);
		final Component widthText = Text.translatable("gui.mtr.vehicle_width", 88);
		final Component doorMaxText = Text.translatable("gui.mtr.vehicle_door_max", 88);
		final int textWidth2 = Math.max(font.width(lengthText), Math.max(font.width(widthText), font.width(doorMaxText))) + TEXT_PADDING * 2;
		UtilitiesClient.setWidgetX(sliderLength, xStart);
		UtilitiesClient.setWidgetY(sliderLength, SQUARE_SIZE);
		sliderLength.setWidth(PANEL_WIDTH - textWidth2);
		sliderLength.setHeight(SQUARE_SIZE / 2);
		sliderLength.setValue(RenderTrains.creatorProperties.getLength() - 1);
		UtilitiesClient.setWidgetX(sliderWidth, xStart);
		UtilitiesClient.setWidgetY(sliderWidth, SQUARE_SIZE * 3 / 2);
		sliderWidth.setWidth(PANEL_WIDTH - textWidth2);
		sliderWidth.setHeight(SQUARE_SIZE / 2);
		sliderWidth.setValue(RenderTrains.creatorProperties.getWidth() - 1);
		UtilitiesClient.setWidgetX(sliderDoorMax, xStart);
		UtilitiesClient.setWidgetY(sliderDoorMax, SQUARE_SIZE * 2);
		sliderDoorMax.setWidth(PANEL_WIDTH - textWidth2);
		sliderDoorMax.setHeight(SQUARE_SIZE / 2);
		sliderDoorMax.setValue(RenderTrains.creatorProperties.getDoorMax() - 1);

		IDrawing.setPositionAndWidth(buttonDoorAnimationType, xStart, SQUARE_SIZE * 5 / 2, PANEL_WIDTH);

		IDrawing.setPositionAndWidth(checkboxPartMirror, 0, SQUARE_SIZE, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(checkboxPartSkipRenderingIfTooFar, 0, SQUARE_SIZE * 2, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(checkboxIsDisplay, 0, SQUARE_SIZE * 3, PANEL_WIDTH);

		final Component displayXPaddingText = Text.translatable("gui.mtr.part_display_x_padding", -8.88);
		final Component displayYPaddingText = Text.translatable("gui.mtr.part_display_y_padding", -8.88);
		final Component displayCjkSizeRatioText = Text.translatable("gui.mtr.part_display_cjk_size_ratio", -8.88);
		final int textWidth3 = Math.max(Math.max(font.width(displayXPaddingText), font.width(displayYPaddingText)), font.width(displayCjkSizeRatioText)) + TEXT_PADDING * 2;
		UtilitiesClient.setWidgetX(sliderDisplayXPadding, 0);
		UtilitiesClient.setWidgetY(sliderDisplayXPadding, SQUARE_SIZE * 4);
		sliderDisplayXPadding.setWidth(PANEL_WIDTH - textWidth3);
		sliderDisplayXPadding.setHeight(SQUARE_SIZE / 2);
		UtilitiesClient.setWidgetX(sliderDisplayYPadding, 0);
		UtilitiesClient.setWidgetY(sliderDisplayYPadding, SQUARE_SIZE * 9 / 2);
		sliderDisplayYPadding.setWidth(PANEL_WIDTH - textWidth3);
		sliderDisplayYPadding.setHeight(SQUARE_SIZE / 2);
		UtilitiesClient.setWidgetX(sliderDisplayCjkSizeRatio, 0);
		UtilitiesClient.setWidgetY(sliderDisplayCjkSizeRatio, SQUARE_SIZE * 5);
		sliderDisplayCjkSizeRatio.setWidth(PANEL_WIDTH - textWidth3);
		sliderDisplayCjkSizeRatio.setHeight(SQUARE_SIZE / 2);

		IDrawing.setPositionAndWidth(buttonDisplayType, 0, SQUARE_SIZE * 11 / 2, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(colorSelectorDisplayCjk, PANEL_WIDTH - SQUARE_SIZE, SQUARE_SIZE * 13 / 2, SQUARE_SIZE / 2);
		IDrawing.setPositionAndWidth(colorSelectorDisplay, PANEL_WIDTH - SQUARE_SIZE / 2, SQUARE_SIZE * 13 / 2, SQUARE_SIZE / 2);
		IDrawing.setPositionAndWidth(checkboxShouldScroll, 0, SQUARE_SIZE * 15 / 2, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(checkboxForceUpperCase, 0, SQUARE_SIZE * 17 / 2, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(checkboxForceSingleLine, 0, SQUARE_SIZE * 19 / 2, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(textFieldDisplayTest, TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 21 / 2 + TEXT_FIELD_PADDING / 2, PANEL_WIDTH - TEXT_FIELD_PADDING);
		textFieldDisplayTest.setValue(DEFAULT_TEST_STRING);

		IDrawing.setPositionAndWidth(buttonPartStage, xStart, 0, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonPartDoorOffset, xStart, SQUARE_SIZE, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonPartRenderCondition, xStart, SQUARE_SIZE * 2, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(textFieldPositions, width, SQUARE_SIZE * 3 + TEXT_PADDING + TEXT_HEIGHT + TEXT_FIELD_PADDING / 2, PANEL_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldWhitelistedCars, width, SQUARE_SIZE * 4 + TEXT_PADDING * 2 + TEXT_HEIGHT * 2 + TEXT_FIELD_PADDING * 3 / 2, PANEL_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldBlacklistedCars, width, SQUARE_SIZE * 5 + TEXT_PADDING * 3 + TEXT_HEIGHT * 3 + TEXT_FIELD_PADDING * 5 / 2, PANEL_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(buttonDone, xStart, height - SQUARE_SIZE, PANEL_WIDTH);

		textFieldPositions.setResponder(text -> {
			RenderTrains.creatorProperties.editPartPositions(editingPartIndex, textFieldPositions.getValue());
			updateControls(false);
		});
		textFieldWhitelistedCars.setResponder(text -> {
			RenderTrains.creatorProperties.editPartWhitelistedCars(editingPartIndex, textFieldWhitelistedCars.getValue());
			updateControls(false);
		});
		textFieldBlacklistedCars.setResponder(text -> {
			RenderTrains.creatorProperties.editPartBlacklistedCars(editingPartIndex, textFieldBlacklistedCars.getValue());
			updateControls(false);
		});

		updateControls(true);

		addDrawableChild(buttonOptions);
		availableModelPartsList.init(this::addDrawableChild);
		usedModelPartsList.init(this::addDrawableChild);

		addDrawableChild(sliderCars);
		addDrawableChild(sliderBrightness);
		addDrawableChild(buttonToggleTrainDirection);
		addDrawableChild(buttonDoorLeft);
		addDrawableChild(buttonDoorRight);

		addDrawableChild(buttonTransportMode);
		addDrawableChild(sliderLength);
		addDrawableChild(sliderWidth);
		addDrawableChild(sliderDoorMax);
		addDrawableChild(buttonDoorAnimationType);

		addDrawableChild(checkboxPartMirror);
		addDrawableChild(checkboxPartSkipRenderingIfTooFar);
		addDrawableChild(checkboxIsDisplay);
		addDrawableChild(sliderDisplayXPadding);
		addDrawableChild(sliderDisplayYPadding);
		addDrawableChild(sliderDisplayCjkSizeRatio);
		addDrawableChild(buttonDisplayType);
		addDrawableChild(colorSelectorDisplayCjk);
		addDrawableChild(colorSelectorDisplay);
		addDrawableChild(checkboxShouldScroll);
		addDrawableChild(checkboxForceUpperCase);
		addDrawableChild(checkboxForceSingleLine);
		addDrawableChild(textFieldDisplayTest);

		addDrawableChild(buttonPartStage);
		addDrawableChild(buttonPartDoorOffset);
		addDrawableChild(buttonPartRenderCondition);
		addDrawableChild(textFieldPositions);
		addDrawableChild(textFieldWhitelistedCars);
		addDrawableChild(textFieldBlacklistedCars);
		addDrawableChild(buttonDone);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		try {
			if (guiCounter == 0 && minecraft != null) {
				hideGui = minecraft.options.hideGui;
				guiGraphics.fill(0, 0, width, height, ARGB_BLACK);
			}

			guiGraphics.fill(0, 0, PANEL_WIDTH, height, ARGB_BACKGROUND);
			guiGraphics.fill(width - PANEL_WIDTH, 0, width, height, ARGB_BACKGROUND);
			availableModelPartsList.render(guiGraphics, font);
			usedModelPartsList.render(guiGraphics, font);

			super.render(guiGraphics, mouseX, mouseY, delta);

			if (isEditing()) {
				guiGraphics.drawCenteredString(font, Text.translatable("gui.mtr.editing_part", RenderTrains.creatorProperties.getPropertiesPartsArray().get(editingPartIndex).getAsJsonObject().get(KEY_PROPERTIES_NAME).getAsString()), PANEL_WIDTH / 2, TEXT_PADDING, ARGB_WHITE);
				if (colorSelectorDisplay.visible) {
					guiGraphics.drawString(font, Text.translatable(colorSelectorDisplayCjk.visible ? "gui.mtr.part_display_text_color_cjk" : "gui.mtr.part_display_text_color"), TEXT_PADDING, SQUARE_SIZE * 13 / 2 + TEXT_PADDING, ARGB_WHITE);
				}
				guiGraphics.drawCenteredString(font, Text.translatable("gui.mtr.part_positions"), width - PANEL_WIDTH / 2, SQUARE_SIZE * 3 + TEXT_PADDING, ARGB_WHITE);
				guiGraphics.drawCenteredString(font, Text.translatable("gui.mtr.part_whitelisted_cars"), width - PANEL_WIDTH / 2, SQUARE_SIZE * 4 + TEXT_PADDING * 2 + TEXT_HEIGHT + TEXT_FIELD_PADDING, ARGB_WHITE);
				guiGraphics.drawCenteredString(font, Text.translatable("gui.mtr.part_blacklisted_cars"), width - PANEL_WIDTH / 2, SQUARE_SIZE * 5 + TEXT_PADDING * 3 + TEXT_HEIGHT * 2 + TEXT_FIELD_PADDING * 2, ARGB_WHITE);
			} else {
				guiGraphics.drawCenteredString(font, Text.translatable("gui.mtr.available_model_parts"), PANEL_WIDTH / 2, TEXT_PADDING, ARGB_WHITE);
				guiGraphics.drawCenteredString(font, Text.translatable("gui.mtr.used_model_parts"), width - PANEL_WIDTH / 2, SQUARE_SIZE * 7 / 2 + TEXT_PADDING, ARGB_WHITE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		guiCounter = 2;
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		availableModelPartsList.mouseMoved(mouseX, mouseY);
		usedModelPartsList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		if (mouseX >= PANEL_WIDTH && mouseX < width - PANEL_WIDTH) {
			scale = Math.max(scale - (float) amount, MIN_SCALE);
			final float bound = cars * RenderTrains.creatorProperties.getLength() / 2F;
			translation = Mth.clamp(translation, -bound, bound);
		}
		availableModelPartsList.mouseScrolled(mouseX, mouseY, amount);
		usedModelPartsList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (mouseX >= PANEL_WIDTH && mouseX < width - PANEL_WIDTH && mouseY < height - TEXT_HEIGHT - SQUARE_SIZE * 3) {
			if (button == 0) {
				final Vec3 movement = new Vec3(0, deltaY * MOUSE_SCALE * scale, deltaX * MOUSE_SCALE * scale).yRot(yaw).zRot(roll);
				final float bound = cars * RenderTrains.creatorProperties.getLength() / 2F;
				translation = Mth.clamp(translation - (float) movement.z, -bound, bound);
			} else {
				yaw -= (float) deltaX * MOUSE_SCALE * scale;
				roll -= (float) deltaY * MOUSE_SCALE * scale * Math.cos(yaw);
			}
		}
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public void tick() {
		availableModelPartsList.tick();
		usedModelPartsList.tick();
		textFieldPositions.tick();
		textFieldWhitelistedCars.tick();
		textFieldBlacklistedCars.tick();

		final float maxTime = EnumHelper.valueOf(DoorAnimationType.STANDARD, RenderTrains.creatorProperties.getDoorAnimationType()).maxTime;
		final float increment = 1F / Train.DOOR_MOVE_TIME;
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
		return IGui.textOrUntitled(textFieldDisplayTest.getValue());
	}

	private List<DataConverter> updatePartsList(JsonArray jsonArray, Function<JsonObject, Integer> color, Function<JsonObject, String> extraText) {
		final List<DataConverter> listData = new ArrayList<>();
		if (jsonArray.size() > 0) {
			try {
				jsonArray.forEach(jsonElement -> {
					final JsonObject jsonObject = jsonElement.getAsJsonObject();
					final String name = jsonObject.get("name").getAsString();
					listData.add(new DataConverter(extraText == null ? name : extraText.apply(jsonObject).replace("%s", name), ARGB_BLACK | (color == null ? 0 : color.apply(jsonObject))));
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
		buttonTransportMode.setMessage(Text.translatable("gui.mtr.transport_mode_" + transportModeString.toLowerCase(Locale.ENGLISH)));
		cars = Mth.clamp(cars, 1, Math.min(32, EnumHelper.valueOf(TransportMode.TRAIN, transportModeString).maxLength));
		buttonDoorAnimationType.setMessage(Text.translatable("gui.mtr.door_animation_type_" + RenderTrains.creatorProperties.getDoorAnimationType().toLowerCase(Locale.ENGLISH)));

		final int sliderCarsValue = cars - 1;
		if (sliderCarsValue != sliderCars.getIntValue()) {
			sliderCars.setValue(sliderCarsValue);
		}
		if (brightness != sliderBrightness.getIntValue()) {
			sliderBrightness.setValue(brightness);
		}

		buttonToggleTrainDirection.setMessage(Text.translatable("gui.mtr.vehicle_direction_" + (head1IsFront ? "forwards" : "backwards")));
		buttonDoorLeft.setMessage(Text.literal(openingLeft ? DOOR_CLOSE : DOOR_OPEN).append(" ").append(Text.translatable("gui.mtr.left")));
		buttonDoorRight.setMessage(Text.literal(openingRight ? DOOR_CLOSE : DOOR_OPEN).append(" ").append(Text.translatable("gui.mtr.right")));

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

			UtilitiesClient.setWidgetX(textFieldPositions, width - PANEL_WIDTH + TEXT_FIELD_PADDING / 2);
			UtilitiesClient.setWidgetX(textFieldWhitelistedCars, width - PANEL_WIDTH + TEXT_FIELD_PADDING / 2);
			UtilitiesClient.setWidgetX(textFieldBlacklistedCars, width - PANEL_WIDTH + TEXT_FIELD_PADDING / 2);
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
			UtilitiesClient.setWidgetX(textFieldDisplayTest, (hasDisplay ? 0 : width) + TEXT_FIELD_PADDING / 2);

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

				buttonDisplayType.setMessage(Text.translatable("gui.mtr.part_display_type_" + partObject.getAsJsonObject(KEY_PROPERTIES_DISPLAY).get(KEY_PROPERTIES_DISPLAY_TYPE).getAsString().toLowerCase(Locale.ENGLISH)));
				colorSelectorDisplayCjk.setColor(CustomResources.colorStringToInt(partObject.getAsJsonObject(KEY_PROPERTIES_DISPLAY).get(KEY_PROPERTIES_DISPLAY_COLOR_CJK).getAsString()));
				colorSelectorDisplay.setColor(CustomResources.colorStringToInt(partObject.getAsJsonObject(KEY_PROPERTIES_DISPLAY).get(KEY_PROPERTIES_DISPLAY_COLOR).getAsString()));
				checkboxShouldScroll.setChecked(shouldScroll);
				checkboxForceUpperCase.setChecked(partObject.getAsJsonObject(KEY_PROPERTIES_DISPLAY).get(KEY_PROPERTIES_DISPLAY_FORCE_UPPER_CASE).getAsBoolean());
				checkboxForceSingleLine.setChecked(forceSingleLine);
			}

			buttonPartStage.setMessage(Text.translatable("gui.mtr.part_stage_" + partObject.get(KEY_PROPERTIES_STAGE).getAsString().toLowerCase(Locale.ENGLISH)));
			buttonPartDoorOffset.setMessage(Text.translatable("gui.mtr.part_door_offset_" + partObject.get(KEY_PROPERTIES_DOOR_OFFSET).getAsString().toLowerCase(Locale.ENGLISH)));
			buttonPartRenderCondition.setMessage(Text.translatable("gui.mtr.part_render_condition_" + partObject.get(KEY_PROPERTIES_RENDER_CONDITION).getAsString().toLowerCase(Locale.ENGLISH)));

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
				textFieldPositions.setValue(positionsString.toString().replace(".0,", ",").replace(".0]", "]"));
				textFieldWhitelistedCars.setValue(partObject.get(KEY_PROPERTIES_WHITELISTED_CARS).toString());
				textFieldBlacklistedCars.setValue(partObject.get(KEY_PROPERTIES_BLACKLISTED_CARS).toString());
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
			UtilitiesClient.setWidgetX(textFieldDisplayTest, width + TEXT_FIELD_PADDING / 2);

			buttonPartStage.visible = false;
			buttonPartDoorOffset.visible = false;
			buttonPartRenderCondition.visible = false;

			UtilitiesClient.setWidgetX(textFieldPositions, width + TEXT_FIELD_PADDING / 2);
			UtilitiesClient.setWidgetX(textFieldWhitelistedCars, width + TEXT_FIELD_PADDING / 2);
			UtilitiesClient.setWidgetX(textFieldBlacklistedCars, width + TEXT_FIELD_PADDING / 2);
			buttonDone.visible = false;
			availableModelPartsList.x = 0;
			usedModelPartsList.x = width - PANEL_WIDTH;
		}
	}

	private void onAdd(NameColorDataBase data, int index) {
		RenderTrains.creatorProperties.addPart(data.name);
		updateControls(true);
	}

	private void onEdit(NameColorDataBase data, int index) {
		editingPartIndex = index;
		updateControls(true);
	}

	private void onDelete(NameColorDataBase data, int index) {
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

	public static void render(PoseStack matrices) {
		if (guiCounter > 0) {
			guiCounter--;
			final Minecraft minecraft = Minecraft.getInstance();
			minecraft.options.hideGui = guiCounter != 0 || hideGui;

			matrices.pushPose();
			final MultiBufferSource.BufferSource immediate = minecraft.renderBuffers().bufferSource();
			IDrawing.drawTexture(matrices, immediate.getBuffer(RenderType.solid()), Integer.MIN_VALUE, Integer.MAX_VALUE, -256, Integer.MAX_VALUE, Integer.MIN_VALUE, -256, Direction.UP, ARGB_BLACK, 0);
			immediate.endBatch();
			matrices.translate(0, 0, -scale);
			UtilitiesClient.rotateYDegrees(matrices, 90);
			UtilitiesClient.rotateXDegrees(matrices, 180);
			UtilitiesClient.rotateY(matrices, yaw);
			UtilitiesClient.rotateZ(matrices, roll);
			final int light = LightTexture.pack(0, (int) Math.round(brightness / 100D * 0x0F));
			for (int i = 0; i < cars; i++) {
				matrices.pushPose();
				matrices.translate(0, 0, (i - (cars - 1) / 2F) * (RenderTrains.creatorProperties.getLength() + 1) + translation);
				RenderTrains.creatorProperties.render(matrices, i, cars, head1IsFront, doorLeftValue, doorRightValue, openingRight || openingLeft, light);
				matrices.popPose();
			}
			matrices.popPose();
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
			case LIGHTS:
				return 0xF2D600;
			case ALWAYS_ON_LIGHTS:
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
