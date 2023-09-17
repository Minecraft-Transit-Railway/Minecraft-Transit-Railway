package org.mtr.mod.screen;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.jetbrains.annotations.Nullable;
import org.mtr.core.data.Siding;
import org.mtr.core.data.TransportMode;
import org.mtr.core.data.VehicleCar;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.holder.OrderedText;
import org.mtr.mapping.mapper.ButtonWidgetExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Icons;
import org.mtr.mod.Patreon;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.TrainClientRegistry;
import org.mtr.mod.client.TrainProperties;
import org.mtr.mod.data.TrainType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VehicleSelectorScreen extends DashboardListSelectorScreen implements Icons {

	private final ButtonWidgetExtension buttonDuplicateVehicleCars;
	private final Siding siding;

	private static final Object2ObjectAVLTreeMap<String, String> WIKIPEDIA_ARTICLES = new Object2ObjectAVLTreeMap<>();

	public VehicleSelectorScreen(@Nullable ScreenExtension previousScreen, Siding siding) {
		super(previousScreen, getVehicleList(siding.getTransportMode()), getSelectedIds(siding), false, true);
		buttonDuplicateVehicleCars = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.mtr.duplicate_vehicle_cars"), button -> {
			final ObjectArrayList<VehicleCar> existingCars = new ObjectArrayList<>(siding.getVehicleCars());
			while (true) {
				final ObjectArrayList<VehicleCar> currentCars = new ObjectArrayList<>(siding.getVehicleCars());
				final int currentCarCount = currentCars.size();
				currentCars.addAll(existingCars);
				siding.setVehicleCars(currentCars);
				if (siding.getVehicleCars().size() == currentCarCount) {
					break;
				}
			}
			selectedIds.clear();
			selectedIds.addAll(getSelectedIds(siding));
			updateList();
		});
		this.siding = siding;
	}

	@Override
	protected void init2() {
		super.init2();
		final int spareSpace = Math.max(0, width - SQUARE_SIZE * 4 - PANEL_WIDTH * 2);
		availableList.x = SQUARE_SIZE * 2 + spareSpace;
		selectedList.x = SQUARE_SIZE * 3 + spareSpace + PANEL_WIDTH;
		IDrawing.setPositionAndWidth(buttonDone, SQUARE_SIZE * 2 + spareSpace, height - SQUARE_SIZE * 2, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonDuplicateVehicleCars, SQUARE_SIZE * 3 + spareSpace + PANEL_WIDTH, height - SQUARE_SIZE * 2, PANEL_WIDTH);
		addChild(new ClickableWidget(buttonDuplicateVehicleCars));
	}

	@Override
	public void renderAdditional(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		final int spareSpace = Math.max(0, width - SQUARE_SIZE * 4 - PANEL_WIDTH * 2);
		graphicsHolder.drawCenteredText(TextHelper.translatable("gui.mtr.available"), SQUARE_SIZE * 2 + spareSpace + PANEL_WIDTH / 2, SQUARE_SIZE, ARGB_WHITE);
		graphicsHolder.drawCenteredText(TextHelper.translatable("gui.mtr.selected"), SQUARE_SIZE * 3 + spareSpace + PANEL_WIDTH * 3 / 2, SQUARE_SIZE, ARGB_WHITE);

		final int index = availableList.getHoverItemIndex();
		if (index >= 0) {
			final TrainProperties properties = TrainClientRegistry.getTrainProperties(siding.getTransportMode(), index);
			final int spacing = TrainType.getSpacing(properties.baseTrainType);
			int y = SQUARE_SIZE;
			y = drawWrappedText(graphicsHolder, properties.name, y, ARGB_WHITE);
			y = drawWrappedText(graphicsHolder, TextHelper.translatable("gui.mtr.vehicle_length", spacing - 1), y, ARGB_WHITE);
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

	@Override
	protected void updateList() {
		final ObjectArrayList<VehicleCar> tempList = new ObjectArrayList<>();
		selectedIds.forEach(selectedId -> allData.stream().filter(data -> data.id == selectedId).findFirst().ifPresent(data -> {
			if (data instanceof VehicleForList) {
				final TrainProperties trainProperties = ((VehicleForList) data).trainProperties;
				tempList.add(new VehicleCar(((VehicleForList) data).vehicleId, TrainType.getSpacing(trainProperties.baseTrainType), TrainType.getWidth(trainProperties.baseTrainType), -trainProperties.bogiePosition, trainProperties.bogiePosition));
			}
		}));
		siding.setVehicleCars(tempList);
		selectedIds.clear();
		selectedIds.addAll(getSelectedIds(siding));
		super.updateList();

		final ObjectArrayList<VehicleCar> currentList = siding.getVehicleCars();
		final double remainingLength = siding.getRailLength() - currentList.stream().mapToDouble(VehicleCar::getLength).sum();
		allData.forEach(data -> {
			if (data instanceof VehicleForList) {
				((VehicleForList) data).disabled = TrainType.getSpacing(((VehicleForList) data).trainProperties.baseTrainType) > remainingLength;
			}
		});
	}

	private int drawWrappedText(GraphicsHolder graphicsHolder, MutableText component, int y, int color) {
		final List<OrderedText> splitText = GraphicsHolder.wrapLines(component, Math.max(0, width - SQUARE_SIZE * 4 - PANEL_WIDTH * 2));
		int newY = y;
		for (final OrderedText formattedCharSequence : splitText) {
			final int nextY = newY + TEXT_HEIGHT + 2;
			if (nextY > height - SQUARE_SIZE - TEXT_HEIGHT) {
				graphicsHolder.drawText("...", SQUARE_SIZE, newY, color, false, MAX_LIGHT_GLOWING);
				return height;
			} else {
				graphicsHolder.drawText(formattedCharSequence, SQUARE_SIZE, newY, color, false, MAX_LIGHT_GLOWING);
			}
			newY = nextY;
		}
		return newY + TEXT_PADDING;
	}

	private static ObjectImmutableList<DashboardListItem> getVehicleList(TransportMode transportMode) {
		final ObjectArrayList<DashboardListItem> trainsForList = new ObjectArrayList<>();
		TrainClientRegistry.forEach(transportMode, (id, trainId, trainProperties) -> trainsForList.add(new VehicleForList(id, trainId, trainProperties)));
		return new ObjectImmutableList<>(trainsForList);
	}

	private static LongArrayList getSelectedIds(Siding siding) {
		final ObjectImmutableList<DashboardListItem> trainsForList = getVehicleList(siding.getTransportMode());
		final LongArrayList selectedIds = new LongArrayList();
		siding.getVehicleCars().forEach(vehicleCar -> trainsForList.stream()
				.filter(data -> data instanceof VehicleForList && vehicleCar.getVehicleId().equals(((VehicleForList) data).vehicleId))
				.mapToLong(data -> data.id)
				.findFirst()
				.ifPresent(selectedIds::add)
		);
		return selectedIds;
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

	private static class VehicleForList extends DashboardListItem {

		private boolean disabled;
		private final String vehicleId;
		private final TrainProperties trainProperties;

		public VehicleForList(long id, String vehicleId, TrainProperties trainProperties) {
			super(id, trainProperties.name.getString(), trainProperties.color);
			this.vehicleId = vehicleId;
			this.trainProperties = trainProperties;
		}

		@Override
		public String getName(boolean formatted) {
			return String.format("%s%s", disabled && formatted ? WARNING + " " : "", super.getName(formatted));
		}

		@Override
		public int getColor(boolean formatted) {
			return disabled && formatted ? ARGB_BLACK : super.getColor(formatted);
		}
	}
}
