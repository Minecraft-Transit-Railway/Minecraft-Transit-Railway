package org.mtr.screen;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.mtr.MTR;
import org.mtr.client.CustomResourceLoader;
import org.mtr.core.data.Siding;
import org.mtr.core.data.VehicleCar;
import org.mtr.core.tool.Utilities;
import org.mtr.font.FontGroupRegistry;
import org.mtr.font.FontRenderOptions;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.resource.VehicleResource;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.BetterTextFieldWidget;
import org.mtr.widget.ListItem;
import org.mtr.widget.ScrollableListWidget;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class VehicleSelectorScreen extends ScreenBase {

	private final Siding siding;
	private final Object2ObjectAVLTreeMap<String, ObjectOpenHashSet<String>> selectedTags = new Object2ObjectAVLTreeMap<>();
	private final ObjectArrayList<VehicleCar> selectedVehicleCars = new ObjectArrayList<>();

	private final ScrollableListWidget<Filter> filtersListWidget = new ScrollableListWidget<>();
	private final ScrollableListWidget<VehicleResource> availableVehiclesListWidget = new ScrollableListWidget<>();
	private final ScrollableListWidget<ObjectIntImmutablePair<VehicleResource>> selectedVehiclesListWidget = new ScrollableListWidget<>();

	private final int filtersTextWidth;
	private final int availableTextWidth;
	private final int selectedTextWidth;

	private static final String FILTERS_TEXT = TranslationProvider.GUI_MTR_FILTERS.getString();
	private static final String AVAILABLE_TEXT = TranslationProvider.GUI_MTR_AVAILABLE.getString();
	private static final String SELECTED_TEXT = TranslationProvider.GUI_MTR_SELECTED.getString();
	private static final Object2ObjectAVLTreeMap<String, String> WIKIPEDIA_ARTICLES = new Object2ObjectAVLTreeMap<>();
	private static final String FAMILY_TAG = "family";

	public VehicleSelectorScreen(Siding siding, Screen previousScreen) {
		super(previousScreen);
		this.siding = siding;

		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		filtersTextWidth = textRenderer.getWidth(FILTERS_TEXT);
		availableTextWidth = textRenderer.getWidth(AVAILABLE_TEXT);
		selectedTextWidth = textRenderer.getWidth(SELECTED_TEXT);
	}

	@Override
	protected void init() {
		super.init();
		final int columnWidth = width / 3;

		final BetterTextFieldWidget textFieldSearch = new BetterTextFieldWidget(1024, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_SEARCH.getString(), columnWidth - GuiHelper.DEFAULT_PADDING * 2, availableVehiclesListWidget::setFilter);
		textFieldSearch.setPosition(GuiHelper.DEFAULT_PADDING, GuiHelper.DEFAULT_LINE_SIZE);

		final int filtersY = GuiHelper.DEFAULT_PADDING + GuiHelper.DEFAULT_LINE_SIZE * 2;
		filtersListWidget.setBounds(columnWidth, 0, columnWidth, height * 2 / 3 - filtersY);
		filtersListWidget.setPosition(0, filtersY);

		availableVehiclesListWidget.setBounds(columnWidth, height - GuiHelper.DEFAULT_LINE_SIZE, columnWidth, height - GuiHelper.DEFAULT_LINE_SIZE);
		availableVehiclesListWidget.setPosition(columnWidth, GuiHelper.DEFAULT_LINE_SIZE);
		selectedVehiclesListWidget.setBounds(columnWidth, height - GuiHelper.DEFAULT_LINE_SIZE, columnWidth, height - GuiHelper.DEFAULT_LINE_SIZE);
		selectedVehiclesListWidget.setPosition(columnWidth * 2, GuiHelper.DEFAULT_LINE_SIZE);

		selectedVehicleCars.clear();
		selectedVehicleCars.addAll(siding.getVehicleCars());
		updateListWidgets();

		addDrawableChild(textFieldSearch);
		addDrawableChild(filtersListWidget);
		addDrawableChild(availableVehiclesListWidget);
		addDrawableChild(selectedVehiclesListWidget);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		final int columnWidth = width / 3;

		context.drawText(textRenderer, FILTERS_TEXT, (columnWidth - filtersTextWidth) / 2, GuiHelper.DEFAULT_PADDING, GuiHelper.WHITE_COLOR, false);
		context.drawText(textRenderer, AVAILABLE_TEXT, (columnWidth * 3 - availableTextWidth) / 2, GuiHelper.DEFAULT_PADDING, GuiHelper.WHITE_COLOR, false);
		context.drawText(textRenderer, SELECTED_TEXT, (columnWidth * 5 - selectedTextWidth) / 2, GuiHelper.DEFAULT_PADDING, GuiHelper.WHITE_COLOR, false);

		final VehicleResource hoverVehicleResource = availableVehiclesListWidget.getHoverData();
		if (hoverVehicleResource != null) {
			int y = filtersListWidget.getY() + filtersListWidget.getHeight() + GuiHelper.DEFAULT_PADDING;
			y = drawWrappedText(context, hoverVehicleResource.getName(), y, GuiHelper.WHITE_COLOR);
			y = drawWrappedText(context, TranslationProvider.GUI_MTR_VEHICLE_LENGTH.getMutableText(hoverVehicleResource.getLength()), y, GuiHelper.WHITE_COLOR);

			final String description = hoverVehicleResource.getDescription().getString();
			if (!description.isEmpty()) {
				for (final String text : description.split("[|\n]")) {
					y = drawWrappedText(context, Text.literal(text), y, GuiHelper.LIGHT_GRAY_COLOR);
				}
			}

			final String wikipediaArticle = hoverVehicleResource.getWikipediaArticle();
			if (!wikipediaArticle.isEmpty()) {
				final String fullText = fetchWikipediaArticle(wikipediaArticle);
				for (final String text : fullText.split("\n")) {
					y = drawWrappedText(context, Text.literal(text), y, GuiHelper.LIGHT_GRAY_COLOR);
				}
			}
		}
	}

	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
		context.fill(0, 0, width, height, GuiHelper.BACKGROUND_COLOR);
	}

	@Override
	public void close() {
		siding.setVehicleCars(selectedVehicleCars);
		super.close();
	}

	private boolean canAddCar(ObjectArrayList<VehicleCar> vehicleCars, VehicleResource vehicleResource) {
		final ObjectArrayList<VehicleCar> currentVehicleCars = new ObjectArrayList<>(siding.getVehicleCars());
		final ObjectArrayList<VehicleCar> testVehicleCars = new ObjectArrayList<>(vehicleCars);
		testVehicleCars.add(toVehicleCar(vehicleResource));
		siding.setVehicleCars(testVehicleCars);
		final boolean canAddCar = siding.getVehicleCars().size() == testVehicleCars.size();
		siding.setVehicleCars(currentVehicleCars);
		return canAddCar;
	}

	private void updateListWidgets() {
		// Filters list widget
		final ObjectArrayList<ListItem<Filter>> filterListItems = new ObjectArrayList<>();
		final Object2ObjectOpenHashMap<String, ObjectOpenHashSet<String>> filteredVehicleIds = new Object2ObjectOpenHashMap<>();
		final Object2ObjectOpenHashMap<String, String> familyForVehicleId = new Object2ObjectOpenHashMap<>();

		CustomResourceLoader.getVehicleTags(siding.getTransportMode()).forEach((tagName, vehicleIdsForTag) -> {
			// Find the currently selected tags
			final ObjectOpenHashSet<String> selectedChildren = selectedTags.getOrDefault(tagName, new ObjectOpenHashSet<>());
			final ObjectArrayList<ListItem<Filter>> childFilterListItems = new ObjectArrayList<>();

			// Build the child list items
			vehicleIdsForTag.forEach((tag, vehicleIds) -> {
				if (tagName.equals(FAMILY_TAG)) {
					vehicleIds.forEach(vehicleId -> familyForVehicleId.put(vehicleId, tag));
				}

				final boolean childSelected = selectedChildren.contains(tag);
				if (childSelected) {
					// Store filtered vehicle IDs for building the available list widget later
					filteredVehicleIds.computeIfAbsent(tagName, key -> new ObjectOpenHashSet<>()).addAll(vehicleIds);
				}

				childFilterListItems.add(ListItem.createChild(
						(drawing, x, y) -> drawing.setVerticesWH(x + GuiHelper.DEFAULT_PADDING * 3 / 2F, y + GuiHelper.DEFAULT_PADDING * 3 / 2F, GuiHelper.DEFAULT_PADDING, GuiHelper.DEFAULT_PADDING).setColor(childSelected ? GuiHelper.RED_COLOR : GuiHelper.BLACK_COLOR).draw(),
						GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE,
						new Filter(tagName, tag, vehicleIds, childSelected),
						tag,
						ObjectArrayList.of(
								new ObjectObjectImmutablePair<>(childSelected ? GuiHelper.DELETE_TEXTURE_ID : GuiHelper.ADD_TEXTURE_ID, filter -> {
									// Click action to modify the selected tags
									if (childSelected) {
										selectedChildren.remove(tag);
										if (selectedChildren.isEmpty()) {
											selectedTags.remove(tagName);
										}
									} else {
										selectedTags.computeIfAbsent(tagName, key -> new ObjectOpenHashSet<>()).add(tag);
									}

									// Update the list widgets after filters have changed
									updateListWidgets();
								})
						)
				));
			});

			// item.data should never be null for children
			childFilterListItems.sort(Comparator.comparing(item -> item.data == null ? "" : item.data.tag));

			// Build the parent list item
			final boolean parentSelected = !selectedChildren.isEmpty();
			filterListItems.add(ListItem.createParent(
					(drawing, x, y) -> drawing.setVerticesWH(x + GuiHelper.DEFAULT_PADDING, y + GuiHelper.DEFAULT_PADDING, GuiHelper.MINECRAFT_FONT_SIZE, GuiHelper.MINECRAFT_FONT_SIZE).setColor(parentSelected ? GuiHelper.RED_COLOR : GuiHelper.DARK_GRAY_COLOR).draw(),
					GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE,
					tagName + (parentSelected ? String.format(" (%s)", selectedChildren.size()) : ""),
					tagName,
					childFilterListItems
			));
		});

		// Sort and add to the filters list widget
		filterListItems.sort(Comparator.comparing(item -> item.text));
		filtersListWidget.setData(filterListItems);

		// Available vehicles list widget
		final ObjectArrayList<AvailableVehicleFamily> availableVehicleFamilyList = new ObjectArrayList<>();

		CustomResourceLoader.iterateVehicles(siding.getTransportMode(), vehicleResource -> {
			if (filteredVehicleIds.isEmpty() || filteredVehicleIds.values().stream().allMatch(filteredVehicleIdsPerTag -> filteredVehicleIdsPerTag.contains(vehicleResource.getId()))) {
				final String family = familyForVehicleId.get(vehicleResource.getId());
				final boolean canAddCar = canAddCar(selectedVehicleCars, vehicleResource);

				// Build the child list item
				final ListItem<VehicleResource> childListItem = ListItem.createChild(
						(drawing, x, y) -> drawVehicleIcon(drawing, x, y, canAddCar, true, vehicleResource.getColor()),
						GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE,
						vehicleResource,
						vehicleResource.getName().getString(),
						ObjectArrayList.of(
								new ObjectObjectImmutablePair<>(GuiHelper.ADD_TEXTURE_ID, vehicleResource1 -> {
									selectedVehicleCars.add(toVehicleCar(vehicleResource1));
									updateListWidgets();
								})
						)
				);

				// Check if the vehicle has a family
				if (family == null) {
					availableVehicleFamilyList.add(new AvailableVehicleFamily(null, vehicleResource.getColor(), ObjectArrayList.of(new ObjectBooleanImmutablePair<>(childListItem, canAddCar))));
				} else {
					boolean addFamily = true;
					for (final AvailableVehicleFamily availableVehicleFamily : availableVehicleFamilyList) {
						if (family.equals(availableVehicleFamily.family)) {
							addFamily = false;
							availableVehicleFamily.children.add(new ObjectBooleanImmutablePair<>(childListItem, canAddCar));
							break;
						}
					}
					if (addFamily) {
						availableVehicleFamilyList.add(new AvailableVehicleFamily(family, vehicleResource.getColor(), ObjectArrayList.of(new ObjectBooleanImmutablePair<>(childListItem, canAddCar))));
					}
				}
			}
		});

		// Add to the available list widget
		availableVehiclesListWidget.setData(availableVehicleFamilyList.stream().map(availableVehicleFamily -> {
			if (availableVehicleFamily.family == null || availableVehicleFamily.children.size() == 1) {
				return availableVehicleFamily.children.getFirst().left();
			} else {
				return ListItem.createParent(
						(drawing, x, y) -> drawVehicleIcon(drawing, x, y, availableVehicleFamily.children.stream().anyMatch(ObjectBooleanImmutablePair::rightBoolean), false, availableVehicleFamily.color),
						GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE,
						availableVehicleFamily.family,
						availableVehicleFamily.family,
						availableVehicleFamily.children.stream().map(ObjectBooleanImmutablePair::left).collect(Collectors.toCollection(ObjectArrayList::new))
				);
			}
		}).collect(Collectors.toCollection(ObjectArrayList::new)));

		// Selected vehicles list widget
		final ObjectArrayList<ListItem<ObjectIntImmutablePair<VehicleResource>>> selectedVehicleListItems = new ObjectArrayList<>();
		final ObjectArrayList<VehicleCar> currentVehicleCars = new ObjectArrayList<>();

		for (int i = 0; i < selectedVehicleCars.size(); i++) {
			final int index = i;
			final VehicleCar vehicleCar = selectedVehicleCars.get(i);

			CustomResourceLoader.getVehicleById(siding.getTransportMode(), vehicleCar.getVehicleId(), vehicleResourceDetails -> {
				final boolean canAddCar = canAddCar(currentVehicleCars, vehicleResourceDetails.left());
				selectedVehicleListItems.add(ListItem.createChild(
						(drawing, x, y) -> drawVehicleIcon(drawing, x, y, canAddCar, false, vehicleResourceDetails.left().getColor()),
						GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE,
						new ObjectIntImmutablePair<>(vehicleResourceDetails.left(), index),
						vehicleResourceDetails.left().getName().getString(),
						ObjectArrayList.of(
								ScrollableListWidget.createUpButton(selectedVehicleCars, this::updateListWidgets),
								ScrollableListWidget.createDownButton(selectedVehicleCars, this::updateListWidgets),
								new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, vehicleResourceWithIndex -> {
									Utilities.removeElement(selectedVehicleCars, vehicleResourceWithIndex.rightInt());
									updateListWidgets();
								})
						)
				));
				currentVehicleCars.add(vehicleCar);
			});
		}

		// Add to the selected list widget
		selectedVehiclesListWidget.setData(selectedVehicleListItems);
	}

	private int drawWrappedText(DrawContext context, MutableText component, int y, int color) {
		int newY = y;
		for (final OrderedText orderedText : textRenderer.wrapLines(component, Math.max(0, width / 3 - GuiHelper.DEFAULT_PADDING))) {
			final int nextY = newY + GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT;
			if (nextY > height - GuiHelper.DEFAULT_PADDING - GuiHelper.MINECRAFT_FONT_SIZE) {
				context.drawText(textRenderer, "...", GuiHelper.DEFAULT_PADDING, newY, color, false);
				return height;
			} else {
				context.drawText(textRenderer, orderedText, GuiHelper.DEFAULT_PADDING, newY, color, false);
			}
			newY = nextY;
		}
		return newY - GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT + GuiHelper.DEFAULT_LINE_SIZE;
	}

	private static VehicleCar toVehicleCar(VehicleResource vehicleResource) {
		return new VehicleCar(vehicleResource.getId(), vehicleResource.getLength(), vehicleResource.getWidth(), vehicleResource.getBogie1Position(), vehicleResource.getBogie2Position(), vehicleResource.getCouplingPadding1(), vehicleResource.getCouplingPadding2());
	}

	private static void drawVehicleIcon(Drawing drawing, int x, double y, boolean canAddCar, boolean smallIcon, int color) {
		if (smallIcon) {
			drawing.setVerticesWH(x + GuiHelper.DEFAULT_PADDING * 3 / 2F, y + GuiHelper.DEFAULT_PADDING * 3 / 2F, GuiHelper.DEFAULT_PADDING, GuiHelper.DEFAULT_PADDING).setColor(ColorHelper.fullAlpha(color)).draw();
		} else {
			drawing.setVerticesWH(x + GuiHelper.DEFAULT_PADDING, y + GuiHelper.DEFAULT_PADDING, GuiHelper.MINECRAFT_FONT_SIZE, GuiHelper.MINECRAFT_FONT_SIZE).setColor(ColorHelper.fullAlpha(color)).draw();
		}
		if (!canAddCar) {
			drawing.setVerticesWH(x + GuiHelper.DEFAULT_PADDING + 1, y + GuiHelper.DEFAULT_PADDING + 1, GuiHelper.MINECRAFT_FONT_SIZE - 2, GuiHelper.MINECRAFT_FONT_SIZE - 2).setColor(GuiHelper.BACKGROUND_COLOR).draw();
			FontGroupRegistry.MTR.get().render(drawing, "!", FontRenderOptions.builder()
					.color(GuiHelper.WHITE_COLOR)
					.offsetX(x)
					.offsetY((float) y)
					.horizontalSpace(GuiHelper.DEFAULT_LINE_SIZE)
					.horizontalTextAlignment(FontRenderOptions.Alignment.CENTER)
					.verticalSpace(GuiHelper.DEFAULT_LINE_SIZE)
					.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
					.maxFontSize(GuiHelper.MINECRAFT_FONT_SIZE - 2)
					.build());
		}
	}

	private static String fetchWikipediaArticle(String wikipediaArticle) {
		final String result = WIKIPEDIA_ARTICLES.get(wikipediaArticle);
		if (result == null) {
			CompletableFuture.runAsync(() -> MTR.openConnectionSafeJson("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&explaintext&exintro&titles=" + wikipediaArticle, jsonElement -> {
				final JsonObject pagesObject = jsonElement.getAsJsonObject().getAsJsonObject("query").getAsJsonObject("pages");
				pagesObject.entrySet().stream().findFirst().ifPresent(entry -> WIKIPEDIA_ARTICLES.put(wikipediaArticle, pagesObject.getAsJsonObject(entry.getKey()).get("extract").getAsString()));
			}));
			WIKIPEDIA_ARTICLES.put(wikipediaArticle, "");
			return "";
		} else {
			return result;
		}
	}

	private record Filter(String tagName, String tag, ObjectArrayList<String> vehicleIds, boolean selected) {
	}

	private record AvailableVehicleFamily(@Nullable String family, int color, ObjectArrayList<ObjectBooleanImmutablePair<ListItem<VehicleResource>>> children) {
	}
}
