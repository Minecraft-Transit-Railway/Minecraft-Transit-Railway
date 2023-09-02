package org.mtr.mod.screen;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.mtr.core.data.Depot;
import org.mtr.core.data.Siding;
import org.mtr.core.data.TransportMode;
import org.mtr.core.data.VehicleCar;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.core.tools.Utilities;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.registry.RegistryClient;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.Icons;
import org.mtr.mod.Patreon;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.TrainClientRegistry;
import org.mtr.mod.client.TrainProperties;
import org.mtr.mod.data.RailType;
import org.mtr.mod.data.TrainType;
import org.mtr.mod.packet.PacketData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SidingScreen extends SavedRailScreenBase<Siding, Depot> implements Icons {

	private boolean isSelectingTrain;

	private final TransportMode transportMode;
	private final ButtonWidgetExtension buttonSelectTrain;
	private final DashboardList availableTrainsList;
	private final CheckboxWidgetExtension buttonUnlimitedTrains;
	private final TextFieldWidgetExtension textFieldMaxTrains;
	private final WidgetShorterSlider sliderAccelerationConstant;
	private final CheckboxWidgetExtension buttonIsManual;
	private final WidgetShorterSlider sliderMaxManualSpeed;

	private static final MutableText SELECTED_TRAIN_TEXT = TextHelper.translatable("gui.mtr.selected_vehicle");
	private static final MutableText MAX_TRAINS_TEXT = TextHelper.translatable("gui.mtr.max_vehicles");
	private static final MutableText ACCELERATION_CONSTANT_TEXT = TextHelper.translatable("gui.mtr.acceleration");
	private static final MutableText MANUAL_TO_AUTOMATIC_TIME = TextHelper.translatable("gui.mtr.manual_to_automatic_time");
	private static final MutableText MAX_MANUAL_SPEED = TextHelper.translatable("gui.mtr.max_manual_speed");
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
		buttonSelectTrain = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> onSelectingTrain());
		availableTrainsList = new DashboardList(null, null, null, null, this::onAdd, null, null, () -> ClientData.TRAINS_SEARCH, text -> ClientData.TRAINS_SEARCH = text);
		textFieldMaxTrains = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_TRAINS_TEXT_LENGTH, TextCase.DEFAULT, "\\D", null);
		sliderAccelerationConstant = new WidgetShorterSlider(0, MAX_TRAINS_WIDTH, (int) Math.round((Siding.MAX_ACCELERATION - Siding.MIN_ACCELERATION) * SLIDER_SCALE), this::accelerationSliderFormatter, null);
		buttonIsManual = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
			if (checked && !textFieldMaxTrains.getText2().equals("1")) {
				textFieldMaxTrains.setText2("1");
			}
			setIsSelectingTrain(false);
		});
		buttonIsManual.setMessage2(new Text(TextHelper.translatable("gui.mtr.is_manual").data));
		sliderMaxManualSpeed = new WidgetShorterSlider(0, MAX_TRAINS_WIDTH, RailType.DIAMOND.ordinal(), this::speedSliderFormatter, null);
		buttonUnlimitedTrains = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
			if (checked) {
				buttonIsManual.setChecked(false);
			}
			if (checked && !textFieldMaxTrains.getText2().isEmpty()) {
				textFieldMaxTrains.setText2("");
			} else if (!checked && textFieldMaxTrains.getText2().isEmpty()) {
				textFieldMaxTrains.setText2("1");
			}
			setIsSelectingTrain(false);
		});
		buttonUnlimitedTrains.setMessage2(new Text(TextHelper.translatable("gui.mtr.unlimited_vehicles").data));
	}

	@Override
	protected void init2() {
		super.init2();

		IDrawing.setPositionAndWidth(buttonSelectTrain, SQUARE_SIZE + textWidth, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING, width - textWidth - SQUARE_SIZE * 2);
		IDrawing.setPositionAndWidth(buttonUnlimitedTrains, SQUARE_SIZE + textWidth + MAX_TRAINS_WIDTH + TEXT_FIELD_PADDING + TEXT_FIELD_PADDING, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 3 / 2, width - textWidth - SQUARE_SIZE * 2);

		addChild(new ClickableWidget(buttonSelectTrain));

		availableTrainsList.y = SQUARE_SIZE;
		availableTrainsList.height = height - SQUARE_SIZE * 2;
		availableTrainsList.width = width - DESCRIPTION_WIDTH - SQUARE_SIZE * 3;
		availableTrainsList.init(this::addChild);

		buttonIsManual.setChecked(savedRailBase.getIsManual());
		buttonUnlimitedTrains.setChecked(savedRailBase.getIsUnlimited());

		IDrawing.setPositionAndWidth(textFieldMaxTrains, SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 3 / 2, MAX_TRAINS_WIDTH - TEXT_FIELD_PADDING);
		textFieldMaxTrains.setText2(savedRailBase.getIsUnlimited() ? "" : String.valueOf(savedRailBase.getMaxVehicles() + 1));
		textFieldMaxTrains.setChangedListener2(text -> {
			buttonUnlimitedTrains.setChecked(text.isEmpty());
			if (!text.equals("1")) {
				buttonIsManual.setChecked(false);
			}
			setIsSelectingTrain(false);
		});

		sliderAccelerationConstant.setX2(SQUARE_SIZE + textWidth);
		sliderAccelerationConstant.setY2(SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 2);
		sliderAccelerationConstant.setHeight(SQUARE_SIZE);
		sliderAccelerationConstant.setValue((int) Math.round((savedRailBase.getAcceleration() - Siding.MIN_ACCELERATION) * SLIDER_SCALE));

		IDrawing.setPositionAndWidth(buttonIsManual, SQUARE_SIZE, SQUARE_SIZE * 6 + TEXT_FIELD_PADDING * 2, width - textWidth - SQUARE_SIZE * 2);

		sliderMaxManualSpeed.setX2(SQUARE_SIZE + textWidth);
		sliderMaxManualSpeed.setY2(SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 2);
		sliderMaxManualSpeed.setHeight(SQUARE_SIZE);
		sliderMaxManualSpeed.setValue(0); // TODO

		sliderDwellTimeMin.setY2(SQUARE_SIZE * 8 + TEXT_FIELD_PADDING * 2);
		sliderDwellTimeSec.setY2(SQUARE_SIZE * 17 / 2 + TEXT_FIELD_PADDING * 2);

		setIsSelectingTrain(false);

		if (showScheduleControls) {
			addChild(new ClickableWidget(buttonUnlimitedTrains));
			addChild(new ClickableWidget(textFieldMaxTrains));
			addChild(new ClickableWidget(sliderAccelerationConstant));
			addChild(new ClickableWidget(buttonIsManual));
			addChild(new ClickableWidget(sliderMaxManualSpeed));
		}
	}

	@Override
	public void tick2() {
		super.tick2();
		availableTrainsList.tick();
		textFieldMaxTrains.tick2();
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		super.render(graphicsHolder, mouseX, mouseY, delta);
		if (!isSelectingTrain) {
			graphicsHolder.drawText(SELECTED_TRAIN_TEXT, SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
			if (showScheduleControls) {
				graphicsHolder.drawText(MAX_TRAINS_TEXT, SQUARE_SIZE, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 3 / 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
				graphicsHolder.drawText(ACCELERATION_CONSTANT_TEXT, SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
				if (buttonIsManual.isChecked2()) {
					graphicsHolder.drawText(MAX_MANUAL_SPEED, SQUARE_SIZE, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
					graphicsHolder.drawText(MANUAL_TO_AUTOMATIC_TIME, SQUARE_SIZE, SQUARE_SIZE * 8 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
				}
			}
		} else {
			final int index = availableTrainsList.getHoverItemIndex();
			if (index >= 0) {
				final TrainProperties properties = TrainClientRegistry.getTrainProperties(transportMode, index);
				final int spacing = TrainType.getSpacing(properties.baseTrainType);
				final int cars = (int) Math.floor(savedRailBase.getRailLength() / spacing);
				int y = SQUARE_SIZE;
				y = drawWrappedText(graphicsHolder, properties.name, y, ARGB_WHITE);
				y = drawWrappedText(graphicsHolder, TextHelper.translatable("gui.mtr.vehicle_length", spacing - 1), y, ARGB_WHITE);
				y = drawWrappedText(graphicsHolder, TextHelper.translatable("gui.mtr.cars_to_spawn", (cars == 0 ? WARNING + " " : "") + Math.min(cars, savedRailBase.getTransportMode().maxLength)), y, ARGB_WHITE);
				if (properties.description != null) {
					for (final String text : properties.description.split("[|\n]")) {
						y = drawWrappedText(graphicsHolder, TextHelper.literal(text), y, ARGB_LIGHT_GRAY);
					}
				}
				if (properties.wikipediaArticle != null) {
					final String fullText = fetchWikipediaArticle(properties.wikipediaArticle);
					for (final String text : fullText.split("\n")) {
						y = drawWrappedText(graphicsHolder, TextHelper.literal(text), y, ARGB_LIGHT_GRAY);
					}
				}
			}
		}
	}

	@Override
	public void onClose2() {
		int maxTrains;
		try {
			maxTrains = Math.max(0, Integer.parseInt(textFieldMaxTrains.getText2()) - 1);
		} catch (Exception ignored) {
			maxTrains = 0;
		}

		double accelerationConstant;
		try {
			accelerationConstant = Utilities.round(MathHelper.clamp((float) sliderAccelerationConstant.getIntValue() / SLIDER_SCALE + Siding.MIN_ACCELERATION, Siding.MIN_ACCELERATION, Siding.MAX_ACCELERATION), 3);
		} catch (Exception ignored) {
			accelerationConstant = Siding.ACCELERATION_DEFAULT;
		}

		savedRailBase.setIsManual(buttonIsManual.isChecked2());
		savedRailBase.setUnlimitedVehicles(buttonUnlimitedTrains.isChecked2());
		savedRailBase.setMaxVehicles(maxTrains);
		savedRailBase.setAcceleration(accelerationConstant);

		RegistryClient.sendPacketToServer(PacketData.fromSidings(IntegrationServlet.Operation.UPDATE, ObjectSet.of(savedRailBase), sidings -> {

		}));

		super.onClose2();
	}

	@Override
	public void mouseMoved2(double mouseX, double mouseY) {
		availableTrainsList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled2(double mouseX, double mouseY, double amount) {
		availableTrainsList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled2(mouseX, mouseY, amount);
	}

	@Override
	protected boolean shouldRenderExtra() {
		return isSelectingTrain;
	}

	@Override
	protected void renderExtra(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		availableTrainsList.render(graphicsHolder);
	}

	@Override
	protected String getNumberStringKey() {
		return "gui.mtr.siding_number";
	}

	private void onSelectingTrain() {
		final ObjectArrayList<DashboardListItem> trainsForList = new ObjectArrayList<>();
		TrainClientRegistry.forEach(transportMode, (id, trainProperties) -> trainsForList.add(new TrainForList(trainProperties)));
		availableTrainsList.setData(trainsForList, false, false, false, false, true, false);
		setIsSelectingTrain(true);
	}

	private void setIsSelectingTrain(boolean isSelectingTrain) {
		this.isSelectingTrain = isSelectingTrain;
		buttonSelectTrain.visible = !isSelectingTrain;
		buttonUnlimitedTrains.visible = !isSelectingTrain;
		textFieldMaxTrains.visible = !isSelectingTrain;
		sliderAccelerationConstant.visible = !isSelectingTrain;
		buttonIsManual.visible = !isSelectingTrain;
		sliderMaxManualSpeed.visible = !isSelectingTrain && buttonIsManual.isChecked2();
		sliderDwellTimeMin.visible = !isSelectingTrain && buttonIsManual.isChecked2();
		sliderDwellTimeSec.visible = !isSelectingTrain && buttonIsManual.isChecked2();
		final ObjectArrayList<VehicleCar> vehicleCars = savedRailBase.getVehicleCars();
		buttonSelectTrain.setMessage2(new Text((vehicleCars.isEmpty() ? TextHelper.literal("") : TrainClientRegistry.getTrainProperties(vehicleCars.get(0).getVehicleId()).name).data));
		availableTrainsList.x = isSelectingTrain ? SQUARE_SIZE : width;
	}

	private void onAdd(DashboardListItem dashboardListItem, int index) {
		if (dashboardListItem instanceof TrainForList) {
			final TrainProperties trainProperties = ((TrainForList) dashboardListItem).trainProperties;
			final ObjectArrayList<VehicleCar> vehicleCars = new ObjectArrayList<>();
			for (int i = 0; i < 256; i++) {
				vehicleCars.add(new VehicleCar(trainProperties.baseTrainType, TrainType.getSpacing(trainProperties.baseTrainType), TrainType.getWidth(trainProperties.baseTrainType), -trainProperties.bogiePosition, trainProperties.bogiePosition));
			}
			savedRailBase.setVehicleCars(vehicleCars);
			setIsSelectingTrain(false);
		}
	}

	private String accelerationSliderFormatter(int value) {
		final double valueMeterPerTickSquared = ((double) value / SLIDER_SCALE + Siding.MIN_ACCELERATION);
		return String.format("%s m/s² (%s km/h/s)", Utilities.round(valueMeterPerTickSquared * ACCELERATION_UNIT_CONVERSION_1, 1), Utilities.round(valueMeterPerTickSquared * ACCELERATION_UNIT_CONVERSION_2, 1));
	}

	private String speedSliderFormatter(int value) {
		final RailType railType = convertMaxManualSpeed(value);
		return railType == null ? TextHelper.translatable("gui.mtr.unlimited").getString() : String.format("%s km/h", railType.speedLimit);
	}

	private int drawWrappedText(GraphicsHolder graphicsHolder, MutableText component, int y, int color) {
		final List<OrderedText> splitText = GraphicsHolder.wrapLines(component, DESCRIPTION_WIDTH);
		int newY = y;
		for (final OrderedText formattedCharSequence : splitText) {
			final int nextY = newY + TEXT_HEIGHT + 2;
			if (nextY > height - SQUARE_SIZE - TEXT_HEIGHT) {
				graphicsHolder.drawText("...", width - DESCRIPTION_WIDTH - SQUARE_SIZE, newY, color, false, MAX_LIGHT_GLOWING);
				return height;
			} else {
				graphicsHolder.drawText(formattedCharSequence, width - DESCRIPTION_WIDTH - SQUARE_SIZE, newY, color, false, MAX_LIGHT_GLOWING);
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

	private static RailType convertMaxManualSpeed(int maxManualSpeed) {
		if (maxManualSpeed >= 0 && maxManualSpeed <= RailType.DIAMOND.ordinal()) {
			return RailType.values()[maxManualSpeed];
		} else {
			return null;
		}
	}

	private static class TrainForList extends DashboardListItem {

		private final TrainProperties trainProperties;

		private TrainForList(TrainProperties trainProperties) {
			super(0, trainProperties.name.getString(), trainProperties.color);
			this.trainProperties = trainProperties;
		}
	}
}
