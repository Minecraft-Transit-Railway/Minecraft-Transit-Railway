package mtr.screen;

import com.google.gson.JsonObject;
import mtr.Icons;
import mtr.Patreon;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.client.TrainClientRegistry;
import mtr.client.TrainProperties;
import mtr.data.*;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SidingScreen extends SavedRailScreenBase<Siding> implements Icons {

	private boolean isSelectingTrain;
	private final float oldAcceleration;
	private final boolean oldIsManual;
	private final int oldMaxManualSpeed;
	private final int oldDwellTime;

	private final TransportMode transportMode;
	private final Button buttonSelectTrain;
	private final DashboardList availableTrainsList;
	private final WidgetBetterCheckbox buttonUnlimitedTrains;
	private final WidgetBetterTextField textFieldMaxTrains;
	private final WidgetShorterSlider sliderAccelerationConstant;
	private final WidgetBetterCheckbox buttonIsManual;
	private final WidgetShorterSlider sliderMaxManualSpeed;

	private static final Component SELECTED_TRAIN_TEXT = Text.translatable("gui.mtr.selected_vehicle");
	private static final Component MAX_TRAINS_TEXT = Text.translatable("gui.mtr.max_vehicles");
	private static final Component ACCELERATION_CONSTANT_TEXT = Text.translatable("gui.mtr.acceleration");
	private static final Component MANUAL_TO_AUTOMATIC_TIME = Text.translatable("gui.mtr.manual_to_automatic_time");
	private static final Component MAX_MANUAL_SPEED = Text.translatable("gui.mtr.max_manual_speed");
	private static final int MAX_TRAINS_TEXT_LENGTH = 3;
	private static final int MAX_TRAINS_WIDTH = 80;
	private static final int DESCRIPTION_WIDTH = 160;
	private static final int SLIDER_SCALE = 1000;
	private static final float ACCELERATION_UNIT_CONVERSION_1 = 20 * 20; // m/tick^2 to m/s^2
	private static final float ACCELERATION_UNIT_CONVERSION_2 = ACCELERATION_UNIT_CONVERSION_1 * 3.6F; // m/tick^2 to km/h/s
	private static final Map<String, String> WIKIPEDIA_ARTICLES = new HashMap<>();

	public SidingScreen(Siding siding, TransportMode transportMode, DashboardScreen dashboardScreen) {
		super(siding, transportMode, dashboardScreen, SELECTED_TRAIN_TEXT, MAX_TRAINS_TEXT, ACCELERATION_CONSTANT_TEXT, MANUAL_TO_AUTOMATIC_TIME, MAX_MANUAL_SPEED);
		this.transportMode = transportMode;
		buttonSelectTrain = UtilitiesClient.newButton(button -> onSelectingTrain());
		availableTrainsList = new DashboardList(null, null, null, null, this::onAdd, null, null, () -> ClientData.TRAINS_SEARCH, text -> ClientData.TRAINS_SEARCH = text);
		textFieldMaxTrains = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.POSITIVE_INTEGER, "", MAX_TRAINS_TEXT_LENGTH);
		sliderAccelerationConstant = new WidgetShorterSlider(0, MAX_TRAINS_WIDTH, Math.round((Train.MAX_ACCELERATION - Train.MIN_ACCELERATION) * SLIDER_SCALE), this::accelerationSliderFormatter, null);
		buttonIsManual = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.is_manual"), checked -> {
			if (checked && !textFieldMaxTrains.getValue().equals("1")) {
				textFieldMaxTrains.setValue("1");
			}
			setIsSelectingTrain(false);
		});
		sliderMaxManualSpeed = new WidgetShorterSlider(0, MAX_TRAINS_WIDTH, RailType.DIAMOND.ordinal(), this::speedSliderFormatter, null);
		buttonUnlimitedTrains = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.unlimited_vehicles"), checked -> {
			if (checked) {
				buttonIsManual.setChecked(false);
			}
			if (checked && !textFieldMaxTrains.getValue().isEmpty()) {
				textFieldMaxTrains.setValue("");
			} else if (!checked && textFieldMaxTrains.getValue().isEmpty()) {
				textFieldMaxTrains.setValue("1");
			}
			setIsSelectingTrain(false);
		});
		oldAcceleration = savedRailBase.getAccelerationConstant();
		oldIsManual = savedRailBase.getIsManual();
		oldMaxManualSpeed = savedRailBase.getMaxManualSpeed();
		oldDwellTime = savedRailBase.getDwellTime();
	}

	@Override
	protected void init() {
		super.init();

		IDrawing.setPositionAndWidth(buttonSelectTrain, SQUARE_SIZE + textWidth, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING, width - textWidth - SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(buttonUnlimitedTrains, SQUARE_SIZE + textWidth + MAX_TRAINS_WIDTH + TEXT_FIELD_PADDING + TEXT_FIELD_PADDING, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 3 / 2, width - textWidth - SQUARE_SIZE * 2);

		addDrawableChild(buttonSelectTrain);

		availableTrainsList.y = SQUARE_SIZE;
		availableTrainsList.height = height - SQUARE_SIZE * 2;
		availableTrainsList.width = width - DESCRIPTION_WIDTH - SQUARE_SIZE * 3;
		availableTrainsList.init(this::addDrawableChild);

		buttonIsManual.setChecked(savedRailBase.getIsManual());
		buttonUnlimitedTrains.setChecked(savedRailBase.getUnlimitedTrains());

		IDrawing.setPositionAndWidth(textFieldMaxTrains, SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 3 / 2, MAX_TRAINS_WIDTH - TEXT_FIELD_PADDING);
		textFieldMaxTrains.setValue(savedRailBase.getUnlimitedTrains() ? "" : String.valueOf(savedRailBase.getMaxTrains() + 1));
		textFieldMaxTrains.setResponder(text -> {
			buttonUnlimitedTrains.setChecked(text.isEmpty());
			if (!text.equals("1")) {
				buttonIsManual.setChecked(false);
			}
			setIsSelectingTrain(false);
		});

		UtilitiesClient.setWidgetX(sliderAccelerationConstant, SQUARE_SIZE + textWidth);
		UtilitiesClient.setWidgetY(sliderAccelerationConstant, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 2);
		sliderAccelerationConstant.setHeight(SQUARE_SIZE);
		sliderAccelerationConstant.setValue(Math.round((savedRailBase.getAccelerationConstant() - Train.MIN_ACCELERATION) * SLIDER_SCALE));

		IDrawing.setPositionAndWidth(buttonIsManual, SQUARE_SIZE, SQUARE_SIZE * 6 + TEXT_FIELD_PADDING * 2, width - textWidth - SQUARE_SIZE * 2);

		UtilitiesClient.setWidgetX(sliderMaxManualSpeed, SQUARE_SIZE + textWidth);
		UtilitiesClient.setWidgetY(sliderMaxManualSpeed, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 2);
		sliderMaxManualSpeed.setHeight(SQUARE_SIZE);
		sliderMaxManualSpeed.setValue(savedRailBase.getMaxManualSpeed());

		UtilitiesClient.setWidgetY(sliderDwellTimeMin, SQUARE_SIZE * 8 + TEXT_FIELD_PADDING * 2);
		UtilitiesClient.setWidgetY(sliderDwellTimeSec, SQUARE_SIZE * 17 / 2 + TEXT_FIELD_PADDING * 2);

		setIsSelectingTrain(false);

		if (showScheduleControls) {
			addDrawableChild(buttonUnlimitedTrains);
			addDrawableChild(textFieldMaxTrains);
			addDrawableChild(sliderAccelerationConstant);
			addDrawableChild(buttonIsManual);
			addDrawableChild(sliderMaxManualSpeed);
		}
	}

	@Override
	public void tick() {
		super.tick();
		availableTrainsList.tick();
		textFieldMaxTrains.tick();
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		super.render(guiGraphics, mouseX, mouseY, delta);
		if (!isSelectingTrain) {
			guiGraphics.drawString(font, SELECTED_TRAIN_TEXT, SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING + TEXT_PADDING, ARGB_WHITE);
			if (showScheduleControls) {
				guiGraphics.drawString(font, MAX_TRAINS_TEXT, SQUARE_SIZE, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 3 / 2 + TEXT_PADDING, ARGB_WHITE);
				guiGraphics.drawString(font, ACCELERATION_CONSTANT_TEXT, SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE);
				if (buttonIsManual.selected()) {
					guiGraphics.drawString(font, MAX_MANUAL_SPEED, SQUARE_SIZE, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE);
					guiGraphics.drawString(font, MANUAL_TO_AUTOMATIC_TIME, SQUARE_SIZE, SQUARE_SIZE * 8 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE);
				}
			}
		} else {
			final int index = availableTrainsList.getHoverItemIndex();
			if (index >= 0) {
				final TrainProperties properties = TrainClientRegistry.getTrainProperties(transportMode, index);
				final int spacing = TrainType.getSpacing(properties.baseTrainType);
				final int cars = (int) Math.floor(savedRailBase.railLength / spacing);
				int y = SQUARE_SIZE;
				y = drawWrappedText(guiGraphics, properties.name, y, ARGB_WHITE);
				y = drawWrappedText(guiGraphics, Text.translatable("gui.mtr.vehicle_length", spacing - 1), y, ARGB_WHITE);
				y = drawWrappedText(guiGraphics, Text.translatable("gui.mtr.cars_to_spawn", (cars == 0 ? WARNING + " " : "") + Math.min(cars, savedRailBase.transportMode.maxLength)), y, ARGB_WHITE);
				if (properties.description != null) {
					for (final String text : properties.description.split("[|\n]")) {
						y = drawWrappedText(guiGraphics, Text.literal(text), y, ARGB_LIGHT_GRAY);
					}
				}
				if (properties.wikipediaArticle != null) {
					final String fullText = fetchWikipediaArticle(properties.wikipediaArticle);
					for (final String text : fullText.split("\n")) {
						y = drawWrappedText(guiGraphics, Text.literal(text), y, ARGB_LIGHT_GRAY);
					}
				}
			}
		}
	}

	@Override
	public void onClose() {
		int maxTrains;
		try {
			maxTrains = Math.max(0, Integer.parseInt(textFieldMaxTrains.getValue()) - 1);
		} catch (Exception ignored) {
			maxTrains = 0;
		}
		float accelerationConstant;
		try {
			accelerationConstant = RailwayData.round(Mth.clamp((float) sliderAccelerationConstant.getIntValue() / SLIDER_SCALE + Train.MIN_ACCELERATION, Train.MIN_ACCELERATION, Train.MAX_ACCELERATION), 3);
		} catch (Exception ignored) {
			accelerationConstant = Train.ACCELERATION_DEFAULT;
		}
		final boolean isManual = buttonIsManual.selected();
		final int maxManualSpeed = sliderMaxManualSpeed.getIntValue();
		final int minutes = sliderDwellTimeMin.getIntValue();
		final float second = sliderDwellTimeSec.getIntValue() / 2F;
		final int dwellTime = (int) ((second + minutes * SECONDS_PER_MINUTE) * 2);
		savedRailBase.setUnlimitedTrains(buttonUnlimitedTrains.selected(), maxTrains, isManual, maxManualSpeed, accelerationConstant, dwellTime, oldAcceleration != accelerationConstant || oldIsManual != isManual || oldMaxManualSpeed != maxManualSpeed || oldDwellTime != dwellTime, packet -> PacketTrainDataGuiClient.sendUpdate(getPacketIdentifier(), packet));
		super.onClose();
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		availableTrainsList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		availableTrainsList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	protected boolean shouldRenderExtra() {
		return isSelectingTrain;
	}

	@Override
	protected void renderExtra(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		availableTrainsList.render(guiGraphics, font);
	}

	@Override
	protected String getNumberStringKey() {
		return "gui.mtr.siding_number";
	}

	@Override
	protected ResourceLocation getPacketIdentifier() {
		return PACKET_UPDATE_SIDING;
	}

	private void onSelectingTrain() {
		final List<TrainForList> trainsForListTemp = new ArrayList<>();
		final List<TrainForList> trainsForListUnavailable = new ArrayList<>();

		TrainClientRegistry.forEach(transportMode, (id, trainProperties) -> {
			final TrainForList trainForList = new TrainForList(savedRailBase, id, trainProperties);
			(trainForList.isAvailable ? trainsForListTemp : trainsForListUnavailable).add(trainForList);
		});

		trainsForListTemp.addAll(trainsForListUnavailable);
		availableTrainsList.setData(trainsForListTemp, false, false, false, false, true, false);
		setIsSelectingTrain(true);
	}

	private void setIsSelectingTrain(boolean isSelectingTrain) {
		this.isSelectingTrain = isSelectingTrain;
		buttonSelectTrain.visible = !isSelectingTrain;
		buttonUnlimitedTrains.visible = !isSelectingTrain;
		textFieldMaxTrains.visible = !isSelectingTrain;
		sliderAccelerationConstant.visible = !isSelectingTrain;
		buttonIsManual.visible = !isSelectingTrain;
		sliderMaxManualSpeed.visible = !isSelectingTrain && buttonIsManual.selected();
		sliderDwellTimeMin.visible = !isSelectingTrain && buttonIsManual.selected();
		sliderDwellTimeSec.visible = !isSelectingTrain && buttonIsManual.selected();
		buttonSelectTrain.setMessage(TrainClientRegistry.getTrainProperties(savedRailBase.getTrainId()).name);
		availableTrainsList.x = isSelectingTrain ? SQUARE_SIZE : width;
	}

	private void onAdd(NameColorDataBase data, int index) {
		if (data instanceof TrainForList) {
			final String baseTrainType = ((TrainForList) data).trainProperties.baseTrainType;
			if (savedRailBase.isValidVehicle(TrainType.getSpacing(baseTrainType))) {
				savedRailBase.setTrainIdAndBaseType(((TrainForList) data).trainId, baseTrainType, packet -> PacketTrainDataGuiClient.sendUpdate(IPacket.PACKET_UPDATE_SIDING, packet));
				setIsSelectingTrain(false);
			}
		}
	}

	private String accelerationSliderFormatter(int value) {
		final float valueMeterPerTickSquared = ((float) value / SLIDER_SCALE + Train.MIN_ACCELERATION);
		return String.format("%s m/s² (%s km/h/s)", RailwayData.round(valueMeterPerTickSquared * ACCELERATION_UNIT_CONVERSION_1, 1), RailwayData.round(valueMeterPerTickSquared * ACCELERATION_UNIT_CONVERSION_2, 1));
	}

	private String speedSliderFormatter(int value) {
		final RailType railType = Train.convertMaxManualSpeed(value);
		return railType == null ? Text.translatable("gui.mtr.unlimited").getString() : String.format("%s km/h", railType.speedLimit);
	}

	private int drawWrappedText(GuiGraphics guiGraphics, Component component, int y, int color) {
		final List<FormattedCharSequence> splitText = font.split(component, DESCRIPTION_WIDTH);
		int newY = y;
		for (final FormattedCharSequence formattedCharSequence : splitText) {
			final int nextY = newY + TEXT_HEIGHT + 2;
			if (nextY > height - SQUARE_SIZE - TEXT_HEIGHT) {
				guiGraphics.drawString(font, "...", width - DESCRIPTION_WIDTH - SQUARE_SIZE, newY, color);
				return height;
			} else {
				guiGraphics.drawString(font, formattedCharSequence, width - DESCRIPTION_WIDTH - SQUARE_SIZE, newY, color);
			}
			newY = nextY;
		}
		return newY + TEXT_PADDING;
	}

	private static String fetchWikipediaArticle(String wikipediaArticle) {
		final String result = WIKIPEDIA_ARTICLES.get(wikipediaArticle);
		if (result == null) {
			CompletableFuture.runAsync(() -> Patreon.openConnectionSafeJson("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&explaintext&exintro&titles=" + wikipediaArticle, jsonElement -> {
				final JsonObject pagesObject = jsonElement.getAsJsonObject().getAsJsonObject("query").getAsJsonObject("pages");
				pagesObject.entrySet().stream().findFirst().ifPresent(entry -> WIKIPEDIA_ARTICLES.put(wikipediaArticle, pagesObject.getAsJsonObject(entry.getKey()).get("extract").getAsString()));
			}));
			WIKIPEDIA_ARTICLES.put(wikipediaArticle, "");
			return "";
		} else {
			return result;
		}
	}

	private static class TrainForList extends NameColorDataBase {

		private final String trainId;
		private final TrainProperties trainProperties;
		private final boolean isAvailable;

		private TrainForList(Siding savedRailBase, String trainId, TrainProperties trainProperties) {
			this.trainId = trainId;
			this.trainProperties = trainProperties;
			isAvailable = savedRailBase.isValidVehicle(TrainType.getSpacing(trainProperties.baseTrainType));
			this.name = (isAvailable ? "" : WARNING + " ") + trainProperties.name.getString();
			this.color = isAvailable ? trainProperties.color : 0;
		}

		@Override
		protected boolean hasTransportMode() {
			return false;
		}
	}
}
