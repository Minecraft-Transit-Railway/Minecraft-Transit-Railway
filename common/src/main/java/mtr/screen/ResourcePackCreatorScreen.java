package mtr.screen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
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
import net.minecraft.client.gui.Gui;
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

public class ResourcePackCreatorScreen extends ScreenMapper implements IResourcePackCreatorProperties, IGui {

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

	private final Button buttonPartStage;
	private final WidgetBetterCheckbox checkboxPartMirror;
	private final WidgetBetterCheckbox checkboxPartSkipRenderingIfTooFar;
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

	public ResourcePackCreatorScreen() {
		super(Text.literal(""));

		buttonOptions = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("menu.options"), button -> {
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
		buttonToggleTrainDirection = new Button(0, 0, 0, SQUARE_SIZE, Text.literal(""), button -> {
			head1IsFront = !head1IsFront;
			updateControls(true);
		});
		buttonDoorLeft = new Button(0, 0, 0, SQUARE_SIZE, Text.literal(""), button -> {
			openingLeft = !openingLeft;
			updateControls(true);
		});
		buttonDoorRight = new Button(0, 0, 0, SQUARE_SIZE, Text.literal(""), button -> {
			openingRight = !openingRight;
			updateControls(true);
		});

		buttonTransportMode = new Button(0, 0, 0, SQUARE_SIZE, Text.literal(""), button -> {
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
		buttonDoorAnimationType = new Button(0, 0, 0, SQUARE_SIZE, Text.literal(""), button -> {
			RenderTrains.creatorProperties.editDoorAnimationType();
			updateControls(true);
		});

		buttonPartStage = new Button(0, 0, 0, SQUARE_SIZE, Text.literal(""), button -> {
			RenderTrains.creatorProperties.editPartRenderStage(editingPartIndex);
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
		buttonPartDoorOffset = new Button(0, 0, 0, SQUARE_SIZE, Text.literal(""), button -> {
			RenderTrains.creatorProperties.editPartDoorOffset(editingPartIndex);
			updateControls(true);
		});
		buttonPartRenderCondition = new Button(0, 0, 0, SQUARE_SIZE, Text.literal(""), button -> {
			RenderTrains.creatorProperties.editPartRenderCondition(editingPartIndex);
			updateControls(true);
		});
		textFieldPositions = new WidgetBetterTextField("[^\\d.,\\[\\]\\- ]", "[0,0]", Integer.MAX_VALUE);
		textFieldWhitelistedCars = new WidgetBetterTextField("[^%\\d,+\\- ]", "5,-4,%3,%2+1", Integer.MAX_VALUE);
		textFieldBlacklistedCars = new WidgetBetterTextField("[^%\\d,+\\- ]", "5,-4,%3,%2+1", Integer.MAX_VALUE);
		buttonDone = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.done"), button -> {
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
		sliderCars.x = PANEL_WIDTH + TEXT_HEIGHT;
		sliderCars.y = height - TEXT_HEIGHT - SQUARE_SIZE * 3;
		sliderCars.setWidth(remainingWidth - TEXT_PADDING - textWidth1);
		sliderCars.setHeight(SQUARE_SIZE / 2);
		sliderCars.setValue(-1);
		sliderBrightness.x = PANEL_WIDTH + TEXT_HEIGHT;
		sliderBrightness.y = height - TEXT_HEIGHT - SQUARE_SIZE * 5 / 2;
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
		sliderLength.x = xStart;
		sliderLength.y = SQUARE_SIZE;
		sliderLength.setWidth(PANEL_WIDTH - textWidth2);
		sliderLength.setHeight(SQUARE_SIZE / 2);
		sliderLength.setValue(RenderTrains.creatorProperties.getLength() - 1);
		sliderWidth.x = xStart;
		sliderWidth.y = SQUARE_SIZE * 3 / 2;
		sliderWidth.setWidth(PANEL_WIDTH - textWidth2);
		sliderWidth.setHeight(SQUARE_SIZE / 2);
		sliderWidth.setValue(RenderTrains.creatorProperties.getWidth() - 1);
		sliderDoorMax.x = xStart;
		sliderDoorMax.y = SQUARE_SIZE * 2;
		sliderDoorMax.setWidth(PANEL_WIDTH - textWidth2);
		sliderDoorMax.setHeight(SQUARE_SIZE / 2);
		sliderDoorMax.setValue(RenderTrains.creatorProperties.getDoorMax() - 1);

		IDrawing.setPositionAndWidth(buttonDoorAnimationType, xStart, SQUARE_SIZE * 5 / 2, PANEL_WIDTH);

		IDrawing.setPositionAndWidth(buttonPartStage, xStart, 0, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(checkboxPartMirror, xStart, SQUARE_SIZE, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(checkboxPartSkipRenderingIfTooFar, xStart, SQUARE_SIZE * 2, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonPartDoorOffset, xStart, SQUARE_SIZE * 3, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonPartRenderCondition, xStart, SQUARE_SIZE * 4, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(textFieldPositions, width, SQUARE_SIZE * 5 + TEXT_PADDING + TEXT_HEIGHT + TEXT_FIELD_PADDING / 2, PANEL_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldWhitelistedCars, width, SQUARE_SIZE * 6 + TEXT_PADDING * 2 + TEXT_HEIGHT * 2 + TEXT_FIELD_PADDING * 3 / 2, PANEL_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldBlacklistedCars, width, SQUARE_SIZE * 7 + TEXT_PADDING * 3 + TEXT_HEIGHT * 3 + TEXT_FIELD_PADDING * 5 / 2, PANEL_WIDTH - TEXT_FIELD_PADDING);
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

		addDrawableChild(buttonPartStage);
		addDrawableChild(checkboxPartMirror);
		addDrawableChild(checkboxPartSkipRenderingIfTooFar);
		addDrawableChild(buttonPartDoorOffset);
		addDrawableChild(buttonPartRenderCondition);
		addDrawableChild(textFieldPositions);
		addDrawableChild(textFieldWhitelistedCars);
		addDrawableChild(textFieldBlacklistedCars);
		addDrawableChild(buttonDone);
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			if (guiCounter == 0 && minecraft != null) {
				hideGui = minecraft.options.hideGui;
				Gui.fill(matrices, 0, 0, width, height, ARGB_BLACK);
			}

			Gui.fill(matrices, 0, 0, PANEL_WIDTH, height, ARGB_BACKGROUND);
			Gui.fill(matrices, width - PANEL_WIDTH, 0, width, height, ARGB_BACKGROUND);
			availableModelPartsList.render(matrices, font);
			usedModelPartsList.render(matrices, font);
			super.render(matrices, mouseX, mouseY, delta);
			drawCenteredString(matrices, font, Text.translatable("gui.mtr.available_model_parts"), PANEL_WIDTH / 2, TEXT_PADDING, ARGB_WHITE);
			drawCenteredString(matrices, font, Text.translatable("gui.mtr.used_model_parts"), PANEL_WIDTH / 2 + usedModelPartsList.x, SQUARE_SIZE * 7 / 2 + TEXT_PADDING, ARGB_WHITE);
			drawCenteredString(matrices, font, Text.translatable("gui.mtr.part_positions"), PANEL_WIDTH / 2 + textFieldPositions.x - TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 5 + TEXT_PADDING, ARGB_WHITE);
			drawCenteredString(matrices, font, Text.translatable("gui.mtr.part_whitelisted_cars"), PANEL_WIDTH / 2 + textFieldWhitelistedCars.x - TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 6 + TEXT_PADDING * 2 + TEXT_HEIGHT + TEXT_FIELD_PADDING, ARGB_WHITE);
			drawCenteredString(matrices, font, Text.translatable("gui.mtr.part_blacklisted_cars"), PANEL_WIDTH / 2 + textFieldBlacklistedCars.x - TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 7 + TEXT_PADDING * 3 + TEXT_HEIGHT * 2 + TEXT_FIELD_PADDING * 2, ARGB_WHITE);
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
		buttonDoorLeft.setMessage(Text.translatable("gui.mtr.vehicle_door_left_" + (openingLeft ? "close" : "open")));
		buttonDoorRight.setMessage(Text.translatable("gui.mtr.vehicle_door_right_" + (openingRight ? "close" : "open")));

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

		if (editingPartIndex >= 0 && editingPartIndex < partsArray.size()) {
			buttonTransportMode.visible = false;
			sliderLength.visible = false;
			sliderWidth.visible = false;
			sliderDoorMax.visible = false;
			buttonDoorAnimationType.visible = false;

			buttonPartStage.visible = true;
			checkboxPartMirror.visible = true;
			checkboxPartSkipRenderingIfTooFar.visible = true;
			buttonPartDoorOffset.visible = true;
			buttonPartRenderCondition.visible = true;

			textFieldPositions.x = width - PANEL_WIDTH + TEXT_FIELD_PADDING / 2;
			textFieldWhitelistedCars.x = width - PANEL_WIDTH + TEXT_FIELD_PADDING / 2;
			textFieldBlacklistedCars.x = width - PANEL_WIDTH + TEXT_FIELD_PADDING / 2;
			buttonDone.visible = true;
			usedModelPartsList.x = width;

			final JsonObject partObject = partsArray.get(editingPartIndex).getAsJsonObject();

			buttonPartStage.setMessage(Text.translatable("gui.mtr.part_stage_" + partObject.get(KEY_PROPERTIES_STAGE).getAsString().toLowerCase(Locale.ENGLISH)));
			checkboxPartMirror.setChecked(partObject.get(KEY_PROPERTIES_MIRROR).getAsBoolean());
			checkboxPartSkipRenderingIfTooFar.setChecked(partObject.get(KEY_PROPERTIES_SKIP_RENDERING_IF_TOO_FAR).getAsBoolean());
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
			buttonTransportMode.visible = true;
			sliderLength.visible = true;
			sliderWidth.visible = true;
			sliderDoorMax.visible = true;
			buttonDoorAnimationType.visible = true;

			buttonPartStage.visible = false;
			checkboxPartMirror.visible = false;
			checkboxPartSkipRenderingIfTooFar.visible = false;
			buttonPartDoorOffset.visible = false;
			buttonPartRenderCondition.visible = false;

			textFieldPositions.x = width + TEXT_FIELD_PADDING / 2;
			textFieldWhitelistedCars.x = width + TEXT_FIELD_PADDING / 2;
			textFieldBlacklistedCars.x = width + TEXT_FIELD_PADDING / 2;
			buttonDone.visible = false;
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
			matrices.mulPose(Vector3f.YP.rotationDegrees(90));
			matrices.mulPose(Vector3f.XP.rotationDegrees(180));
			matrices.mulPose(Vector3f.YP.rotation(yaw));
			matrices.mulPose(Vector3f.ZP.rotation(roll));
			final int light = LightTexture.pack((int) Math.round(brightness / 100D * 0xB), 0xF000);
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
