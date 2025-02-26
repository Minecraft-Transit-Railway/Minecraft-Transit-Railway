package org.mtr.mod.screen;

import org.mtr.core.data.Siding;
import org.mtr.core.data.TransportMode;
import org.mtr.core.data.VehicleCar;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.holder.OrderedText;
import org.mtr.mapping.mapper.ButtonWidgetExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Icons;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketUpdateData;
import org.mtr.mod.resource.VehicleResource;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VehicleSelectorScreen extends DashboardListSelectorScreen implements Icons {

	private final ButtonWidgetExtension buttonDuplicateVehicleCars;
	private final Siding siding;

	private static final Object2ObjectAVLTreeMap<String, String> WIKIPEDIA_ARTICLES = new Object2ObjectAVLTreeMap<>();

	public VehicleSelectorScreen(Siding siding, ScreenExtension previousScreenExtension) {
		super(getVehicleList(siding.getTransportMode()), getSelectedIds(siding), false, true, previousScreenExtension);
		buttonDuplicateVehicleCars = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_DUPLICATE_VEHICLE_CARS.getMutableText(), button -> {
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
		graphicsHolder.drawCenteredText(TranslationProvider.GUI_MTR_AVAILABLE.getMutableText(), SQUARE_SIZE * 2 + spareSpace + PANEL_WIDTH / 2, SQUARE_SIZE, ARGB_WHITE);
		graphicsHolder.drawCenteredText(TranslationProvider.GUI_MTR_SELECTED.getMutableText(), SQUARE_SIZE * 3 + spareSpace + PANEL_WIDTH * 3 / 2, SQUARE_SIZE, ARGB_WHITE);

		final int index = availableList.getHoverItemIndex();
		CustomResourceLoader.getVehicleByIndex(siding.getTransportMode(), index, vehicleResource -> {
			int y = SQUARE_SIZE;
			y = drawWrappedText(graphicsHolder, vehicleResource.getName(), y, ARGB_WHITE);
			y = drawWrappedText(graphicsHolder, TranslationProvider.GUI_MTR_VEHICLE_LENGTH.getMutableText(vehicleResource.getLength()), y, ARGB_WHITE);
			final String description = vehicleResource.getDescription().getString();
			if (!description.isEmpty()) {
				for (final String text : description.split("[|\n]")) {
					y = drawWrappedText(graphicsHolder, TextHelper.literal(text), y, ARGB_LIGHT_GRAY);
				}
			}
			final String wikipediaArticle = vehicleResource.getWikipediaArticle();
			if (!wikipediaArticle.isEmpty()) {
				final String fullText = fetchWikipediaArticle(wikipediaArticle);
				for (final String text : fullText.split("\n")) {
					y = drawWrappedText(graphicsHolder, TextHelper.literal(text), y, ARGB_LIGHT_GRAY);
				}
			}
		});
	}

	@Override
	protected void updateList() {
		final ObjectArrayList<VehicleCar> tempList = new ObjectArrayList<>();
		selectedIds.forEach(selectedId -> allData.stream().filter(data -> data.id == selectedId).findFirst().ifPresent(data -> {
			if (data instanceof VehicleForList) {
				final VehicleResource vehicleResource = ((VehicleForList) data).vehicleResource;
				tempList.add(new VehicleCar(vehicleResource.getId(), vehicleResource.getLength(), vehicleResource.getWidth(), vehicleResource.getBogie1Position(), vehicleResource.getBogie2Position(), vehicleResource.getCouplingPadding1(), vehicleResource.getCouplingPadding2()));
			}
		}));
		siding.setVehicleCars(tempList);
		selectedIds.clear();
		selectedIds.addAll(getSelectedIds(siding));
		super.updateList();
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addSiding(siding)));

		final ObjectArrayList<VehicleCar> currentList = siding.getVehicleCars();
		double remainingLength = siding.getRailLength();
		for (int i = 0; i < currentList.size(); i++) {
			remainingLength -= currentList.get(i).getTotalLength(i == 0, false);
		}
		for (final DashboardListItem data : allData) {
			if (data instanceof VehicleForList) {
				final VehicleForList vehicleForList = (VehicleForList) data;
				vehicleForList.disabled = vehicleForList.vehicleResource.getCouplingPadding1() + vehicleForList.vehicleResource.getLength() > remainingLength;
			}
		}
	}

	private int drawWrappedText(GraphicsHolder graphicsHolder, MutableText component, int y, int color) {
		final List<OrderedText> splitText = GraphicsHolder.wrapLines(component, Math.max(0, width - SQUARE_SIZE * 4 - PANEL_WIDTH * 2));
		int newY = y;
		for (final OrderedText formattedCharSequence : splitText) {
			final int nextY = newY + TEXT_HEIGHT + 2;
			if (nextY > height - SQUARE_SIZE - TEXT_HEIGHT) {
				graphicsHolder.drawText("...", SQUARE_SIZE, newY, color, false, GraphicsHolder.getDefaultLight());
				return height;
			} else {
				graphicsHolder.drawText(formattedCharSequence, SQUARE_SIZE, newY, color, false, GraphicsHolder.getDefaultLight());
			}
			newY = nextY;
		}
		return newY + TEXT_PADDING;
	}

	private static ObjectImmutableList<DashboardListItem> getVehicleList(TransportMode transportMode) {
		final ObjectArrayList<DashboardListItem> trainsForList = new ObjectArrayList<>();
		final long[] id = {0};
		CustomResourceLoader.iterateVehicles(transportMode, vehicleResource -> {
			trainsForList.add(new VehicleForList(id[0], vehicleResource));
			id[0]++;
		});
		return new ObjectImmutableList<>(trainsForList);
	}

	private static LongArrayList getSelectedIds(Siding siding) {
		final ObjectImmutableList<DashboardListItem> trainsForList = getVehicleList(siding.getTransportMode());
		final LongArrayList selectedIds = new LongArrayList();
		siding.getVehicleCars().forEach(vehicleCar -> trainsForList.stream()
				.filter(data -> data instanceof VehicleForList && vehicleCar.getVehicleId().equals(((VehicleForList) data).vehicleResource.getId()))
				.mapToLong(data -> data.id)
				.findFirst()
				.ifPresent(selectedIds::add)
		);
		return selectedIds;
	}

	private static String fetchWikipediaArticle(String wikipediaArticle) {
		final String result = WIKIPEDIA_ARTICLES.get(wikipediaArticle);
		if (result == null) {
			CompletableFuture.runAsync(() -> Init.openConnectionSafeJson("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&explaintext&exintro&titles=" + wikipediaArticle, jsonElement -> {
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
		private final VehicleResource vehicleResource;

		public VehicleForList(long index, VehicleResource vehicleResource) {
			super(index, vehicleResource.getName().getString(), vehicleResource.getColor());
			this.vehicleResource = vehicleResource;
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
