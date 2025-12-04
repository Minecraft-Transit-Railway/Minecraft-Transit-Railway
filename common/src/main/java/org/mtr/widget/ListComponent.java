package org.mtr.widget;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.universal.UMatrixStack;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import kotlin.Pair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.mtr.client.CustomResourceLoader;
import org.mtr.core.data.*;
import org.mtr.core.tool.Utilities;
import org.mtr.font.FontGroupRegistry;
import org.mtr.font.FontRenderOptions;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.registry.UConverters;
import org.mtr.render.SpecialSignStationExitRenderer;
import org.mtr.resource.SignResource;
import org.mtr.tool.DataHelper;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;

public final class ListComponent<T> extends UIComponent {

	@Nullable
	private Runnable clickAction;
	@Nullable
	private String filter;
	@Nullable
	private ListItem<T> hoverItem;

	private final ObjectArrayList<ListItem<T>> dataList = new ObjectArrayList<>();

	private static final int DELETE_COLOR = 0xFFCC0000;

	public ListComponent() {
		onMouseClickConsumer(clickEvent -> {
			if (clickAction != null) {
				clickAction.run();
			}
		});
	}

	@Override
	public void draw(UMatrixStack uMatrixStack) {
		beforeDrawCompat(uMatrixStack);
		clickAction = null;
		hoverItem = null;

		final FontRenderOptions.FontRenderOptionsBuilder fontRenderOptionsBuilder = FontRenderOptions.builder()
				.maxFontSize(GuiHelper.MINECRAFT_FONT_SIZE)
				.verticalSpace(GuiHelper.DEFAULT_LINE_SIZE)
				.verticalTextAlignment(FontRenderOptions.Alignment.CENTER);

		final MatrixStack matrixStack = UConverters.convert(uMatrixStack);
		final Drawing drawing = new Drawing(matrixStack, RenderLayer.getGui());
		final ObjectArrayList<Runnable> deferredRenders = new ObjectArrayList<>();
		final float left = getLeft();
		final float right = getRight();
		final float top = getTop();
		final Pair<Float, Float> mousePosition = getMousePosition();
		final float mouseX = mousePosition.getFirst();
		final float mouseY = mousePosition.getSecond();
		final float[] totalHeight = {0};

		ListItem.iterateData(dataList, filter, (dataIndex, level, listItem) -> {
			final float startY = top + GuiHelper.DEFAULT_LINE_SIZE * dataIndex;
			final double startYBottomLine = startY + GuiHelper.DEFAULT_LINE_SIZE - 1;
			final boolean isMouseOver = Utilities.isBetween(mouseY, startY, startYBottomLine) && Utilities.isBetween(mouseX, left, right - 1);
			totalHeight[0] = startY - top + GuiHelper.DEFAULT_LINE_SIZE;

			// Detect hovering
			if (isMouseOver) {
				if (listItem.isParent()) {
					// Draw hover highlight
					final float leftBound = right - GuiHelper.DEFAULT_LINE_SIZE;
					drawing.setVerticesWH(leftBound, startY, GuiHelper.DEFAULT_LINE_SIZE, GuiHelper.DEFAULT_LINE_SIZE).setColor(GuiHelper.HOVER_COLOR).draw();
					drawing.setVertices(Math.max(left, leftBound - GuiHelper.DEFAULT_LINE_SIZE), startY, leftBound, startY + GuiHelper.DEFAULT_LINE_SIZE).setColor(GuiHelper.BACKGROUND_COLOR, GuiHelper.BACKGROUND_COLOR, GuiHelper.HOVER_COLOR, GuiHelper.HOVER_COLOR).draw();
					clickAction = listItem::toggle;

					// Draw the action button
					deferredRenders.add(() -> new Drawing(matrixStack, RenderLayer.getGuiTextured(listItem.isExpanded() ? GuiHelper.CHEVRON_UP_TEXTURE_ID : GuiHelper.CHEVRON_DOWN_TEXTURE_ID))
							.setVerticesWH(leftBound + GuiHelper.DEFAULT_PADDING / 2F, startY + GuiHelper.DEFAULT_PADDING / 2F, GuiHelper.DEFAULT_ICON_SIZE, GuiHelper.DEFAULT_ICON_SIZE)
							.setUv()
							.draw()
					);
				} else {
					listItem.iterateActions(dataIndex, (actionIndex, identifier, callback) -> {
						final float leftBound = right - GuiHelper.DEFAULT_LINE_SIZE * (listItem.actionCount() - actionIndex);
						final float rightBound = leftBound + GuiHelper.DEFAULT_LINE_SIZE;

						// Draw hover highlight
						if (Utilities.isBetween(mouseX, actionIndex == 0 ? left : leftBound, rightBound - 1)) {
							drawing.setVerticesWH(leftBound, startY, GuiHelper.DEFAULT_LINE_SIZE, GuiHelper.DEFAULT_LINE_SIZE).setColor(identifier.getPath().endsWith("icon_delete.png") ? DELETE_COLOR : GuiHelper.HOVER_COLOR).draw();
							if (actionIndex == 0) {
								drawing.setVertices(Math.max(left, leftBound - GuiHelper.DEFAULT_LINE_SIZE), startY, leftBound, startY + GuiHelper.DEFAULT_LINE_SIZE).setColor(GuiHelper.BACKGROUND_COLOR, GuiHelper.BACKGROUND_COLOR, GuiHelper.HOVER_COLOR, GuiHelper.HOVER_COLOR).draw();
							}
							clickAction = callback;
						}

						// Draw the action button
						deferredRenders.add(() -> new Drawing(matrixStack, RenderLayer.getGuiTextured(identifier))
								.setVerticesWH(leftBound + GuiHelper.DEFAULT_PADDING / 2F, startY + GuiHelper.DEFAULT_PADDING / 2F, GuiHelper.DEFAULT_ICON_SIZE, GuiHelper.DEFAULT_ICON_SIZE)
								.setUv()
								.draw()
						);
					});
				}

				hoverItem = listItem;
			}

			// Draw icon
			listItem.drawIcon.draw(drawing, left, startY);

			// Draw text
			FontGroupRegistry.MTR.get().render(drawing, listItem.text, fontRenderOptionsBuilder
					.horizontalSpace(right - left - listItem.iconWidth - GuiHelper.DEFAULT_PADDING * 2 - (isMouseOver ? GuiHelper.DEFAULT_LINE_SIZE * listItem.actionCount() : 0))
					.offsetX(left + listItem.iconWidth + GuiHelper.DEFAULT_PADDING)
					.offsetY(startY)
					.textOverflow(FontRenderOptions.TextOverflow.COMPRESS)
					.build()
			);

			return false;
		});

		setHeight(new PixelConstraint(Math.max(1, totalHeight[0])));
		deferredRenders.forEach(Runnable::run);
		MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().draw();
		super.draw(uMatrixStack);
	}

	public void setData(ObjectArrayList<ListItem<T>> dataList) {
		ListItem.overwriteList(this.dataList, dataList);
	}

	public void setFilter(@Nullable String filter) {
		ListItem.expandByFilter(dataList, filter);
		this.filter = filter;
	}

	public void toggleExpansion() {
		ListItem.expandAll(dataList, !canCollapse());
	}

	public boolean canCollapse() {
		return dataList.stream().anyMatch(ListItem::isExpanded);
	}

	public T getHoverData() {
		return hoverItem == null ? null : hoverItem.data;
	}

	/**
	 * If there is only one item with one action, trigger it.
	 */
	public void tryTrigger() {
		if (dataList.size() == 1) {
			final ListItem<T> listItem = dataList.getFirst();
			if (listItem.actionCount() == 1) {
				listItem.iterateActions(0, (actionIndex, identifier, callback) -> callback.run());
			}
		}
	}

	public static <T extends AreaBase<T, U>, U extends SavedRailBase<U, T>> void setAreas(ListComponent<T> listComponent, ObjectCollection<T> areas, @Nullable TransportMode transportMode, ObjectArrayList<ObjectObjectImmutablePair<Identifier, ListItem.ActionConsumer<T>>> actions) {
		final ObjectArrayList<T> sortedAreas = new ObjectArrayList<>();
		areas.forEach(route -> {
			if (transportMode == null || route.isTransportMode(transportMode)) {
				sortedAreas.add(route);
			}
		});
		Collections.sort(sortedAreas);
		final ObjectArrayList<ListItem<T>> dataList = new ObjectArrayList<>();

		sortedAreas.forEach(area -> dataList.add(ListItem.createChild(
				(drawing, x, y) -> drawing.setVerticesWH(x + GuiHelper.DEFAULT_PADDING, y + GuiHelper.DEFAULT_PADDING, GuiHelper.MINECRAFT_FONT_SIZE, GuiHelper.MINECRAFT_FONT_SIZE).setColor(ColorHelper.fullAlpha(area.getColor())).draw(),
				GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE,
				area,
				Utilities.formatName(area.getName()),
				actions
		)));

		listComponent.setData(dataList);
	}

	public static <T extends AreaBase<T, U>, U extends SavedRailBase<U, T>> void setSavedRails(ListComponent<U> listComponent, ObjectCollection<U> savedRails, ObjectArrayList<ObjectObjectImmutablePair<Identifier, ListItem.ActionConsumer<U>>> actions) {
		final ObjectArrayList<U> sortedSavedRails = new ObjectArrayList<>(savedRails);
		Collections.sort(sortedSavedRails);
		final ObjectArrayList<ListItem<U>> dataList = new ObjectArrayList<>();

		sortedSavedRails.forEach(savedRail -> {
			final IntArrayList colors;
			final String text;
			if (savedRail instanceof Platform) {
				final ObjectObjectImmutablePair<IntArrayList, String> platformColorsAndDestinations = SignResource.getPlatformColorsAndDestinations(savedRail.getId());
				colors = platformColorsAndDestinations.left();
				text = platformColorsAndDestinations.right().isEmpty() ? TranslationProvider.GUI_MTR_EMPTY_PLATFORM.getString() : Utilities.formatName(platformColorsAndDestinations.right());
			} else if (savedRail instanceof Siding siding) {
				colors = savedRail.area == null ? new IntArrayList() : IntArrayList.of(savedRail.area.getColor());
				final ObjectArrayList<String> vehicleNames = new ObjectArrayList<>();
				for (final VehicleCar vehicleCar : siding.getVehicleCars()) {
					final String[] vehicleName = {null};
					// TODO use vehicle family instead
					CustomResourceLoader.getVehicleById(siding.getTransportMode(), vehicleCar.getVehicleId(), vehicleResourceDetails -> vehicleName[0] = vehicleResourceDetails.left().getName().getString());
					if (vehicleName[0] != null && !vehicleNames.contains(vehicleName[0])) {
						vehicleNames.add(vehicleName[0]);
					}
					if (vehicleNames.size() == 2) {
						break;
					}
				}
				text = String.format("%s%s (%s)", String.join(", ", vehicleNames), vehicleNames.size() == siding.getVehicleCars().size() ? "" : "...", TranslationProvider.GUI_MTR_VEHICLE_CAR_COUNT.getString(siding.getVehicleCars().size()));
			} else {
				colors = new IntArrayList();
				text = "";
			}

			dataList.add(ListItem.createChild(
					(drawing, x, y) -> {
						GuiHelper.drawCircle(drawing, x + GuiHelper.DEFAULT_PADDING / 2F, y + GuiHelper.DEFAULT_PADDING / 2F, GuiHelper.DEFAULT_LINE_SIZE - GuiHelper.DEFAULT_PADDING, 32, colors);
						drawPlatformNumber(drawing, x, y, savedRail.getName());
					},
					GuiHelper.DEFAULT_LINE_SIZE - GuiHelper.DEFAULT_PADDING / 2,
					savedRail,
					text,
					actions
			));
		});

		listComponent.setData(dataList);
	}

	public static void setRoutes(ListComponent<Route> listComponent, ObjectCollection<Route> routes, @Nullable TransportMode transportMode, boolean flattenRoutesByColor, ObjectArrayList<ObjectObjectImmutablePair<Identifier, ListItem.ActionConsumer<Route>>> actions) {
		final ObjectArrayList<Route> sortedRoutes = new ObjectArrayList<>();
		routes.forEach(route -> {
			if (transportMode == null || route.isTransportMode(transportMode)) {
				sortedRoutes.add(route);
			}
		});
		final ObjectArrayList<ListItem<Route>> groupedRoutes = new ObjectArrayList<>();

		if (flattenRoutesByColor) {
			sortedRoutes.sort(Comparator.comparingInt(NameColorDataBase::getColor));
			final ObjectArrayList<String> combinedRouteNames = new ObjectArrayList<>();
			Route lastRoute = null;

			for (int i = 0; i <= sortedRoutes.size(); i++) {
				final Route route = Utilities.getElement(sortedRoutes, i);

				if (lastRoute != null && (route == null || route.getColor() != lastRoute.getColor())) {
					final int color = lastRoute.getColor();
					Collections.sort(combinedRouteNames);
					groupedRoutes.add(ListItem.createChild(
							(drawing, x, y) -> drawing.setVerticesWH(x + GuiHelper.DEFAULT_PADDING, y + GuiHelper.DEFAULT_PADDING, GuiHelper.MINECRAFT_FONT_SIZE, GuiHelper.MINECRAFT_FONT_SIZE).setColor(ColorHelper.fullAlpha(color)).draw(),
							GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE,
							lastRoute,
							String.join("|", combinedRouteNames),
							actions
					));
					combinedRouteNames.clear();
				}

				if (route != null) {
					final String routeName = Utilities.formatName(route.getName().split("\\|\\|")[0]);
					if (!combinedRouteNames.contains(routeName)) {
						combinedRouteNames.add(routeName);
					}
				}

				lastRoute = route;
			}
		} else {
			Collections.sort(sortedRoutes);
			String lastKey = null;

			for (final Route route : sortedRoutes) {
				final String[] routeNameSplit = route.getName().split("\\|\\|");
				final String routeKey = route.getColor() + "_" + routeNameSplit[0];
				final ListItem<Route> lastListItem = Utilities.getElement(groupedRoutes, -1);
				final ListItem<Route> currentListItem;

				if (lastListItem == null || !routeKey.equals(lastKey)) {
					currentListItem = ListItem.createParent(
							(drawing, x, y) -> drawing.setVerticesWH(x + GuiHelper.DEFAULT_PADDING, y + GuiHelper.DEFAULT_PADDING, GuiHelper.MINECRAFT_FONT_SIZE, GuiHelper.MINECRAFT_FONT_SIZE).setColor(ColorHelper.fullAlpha(route.getColor())).draw(),
							GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE,
							Utilities.formatName(routeNameSplit[0]),
							routeKey,
							new ObjectArrayList<>()
					);
					groupedRoutes.add(currentListItem);
				} else {
					currentListItem = lastListItem;
				}

				currentListItem.addChild(ListItem.createChild((drawing, x, y) -> {
				}, GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE, route, DataHelper.getNameOrUntitled(routeNameSplit.length > 1 ? routeNameSplit[1] : ""), actions));
				lastKey = routeKey;
			}
		}

		listComponent.setData(groupedRoutes);
	}

	public static void setRoutePlatforms(ScrollableListWidget<RoutePlatformData> scrollableListWidget, ObjectArrayList<RoutePlatformData> routePlatforms, ObjectArrayList<ObjectObjectImmutablePair<Identifier, ListItem.ActionConsumer<RoutePlatformData>>> actions) {
		final ObjectArrayList<ListItem<RoutePlatformData>> dataList = new ObjectArrayList<>();

		routePlatforms.forEach(routePlatformData -> {
			final Platform platform = routePlatformData.getPlatform();
			final int stationColor = platform.area == null ? GuiHelper.BLACK_COLOR : ColorHelper.fullAlpha(platform.area.getColor());
			final String customDestinationPrefix = routePlatformData.getCustomDestination().isEmpty() ? "" : (Route.destinationIsReset(routePlatformData.getCustomDestination()) ? "\"" : "*");
			final String stationName = platform.area == null ? "" : Utilities.formatName(platform.area.getName());

			dataList.add(ListItem.createChild(
					(drawing, x, y) -> {
						drawing.setVerticesWH(x + GuiHelper.DEFAULT_PADDING / 2F, y + GuiHelper.DEFAULT_PADDING / 2F, GuiHelper.DEFAULT_LINE_SIZE - GuiHelper.DEFAULT_PADDING, GuiHelper.DEFAULT_LINE_SIZE - GuiHelper.DEFAULT_PADDING).setColor(stationColor).draw();
						drawPlatformNumber(drawing, x, y, platform.getName());
					},
					GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE,
					routePlatformData,
					customDestinationPrefix + stationName,
					actions
			));
		});

		scrollableListWidget.setData(dataList);
	}

	public static void setStationExits(ListComponent<StationExit> listComponent, ObjectCollection<StationExit> stationExits, ObjectArrayList<ObjectObjectImmutablePair<Identifier, ListItem.ActionConsumer<StationExit>>> actions) {
		final ObjectArrayList<ListItem<StationExit>> dataList = new ObjectArrayList<>();

		stationExits.forEach(stationExit -> {
			final ObjectArrayList<String> destinations = stationExit.getDestinations();
			final String additional = destinations.size() > 1 ? String.format("|(+%s)", destinations.size() - 1) : "";
			dataList.add(ListItem.createChild(
					(drawing, x, y) -> {
						drawing.setVerticesWH(x + GuiHelper.DEFAULT_PADDING / 2F, y + GuiHelper.DEFAULT_PADDING / 2F, GuiHelper.DEFAULT_LINE_SIZE - GuiHelper.DEFAULT_PADDING, GuiHelper.DEFAULT_LINE_SIZE - GuiHelper.DEFAULT_PADDING).setColor(GuiHelper.DARK_GRAY_COLOR).draw();
						SpecialSignStationExitRenderer.renderText(drawing, stationExit.getName(), x + GuiHelper.DEFAULT_LINE_SIZE / 2F, y + GuiHelper.DEFAULT_LINE_SIZE / 2F, 0, GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT, GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT);
					},
					GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE,
					stationExit,
					destinations.isEmpty() ? "" : Utilities.formatName(String.format("%s%s", destinations.getFirst(), additional)),
					actions
			));
		});

		listComponent.setData(dataList);
	}

	public static <T> ObjectObjectImmutablePair<Identifier, ListItem.ActionConsumer<T>> createUpButton(ObjectArrayList<T> dataList, @Nullable Runnable onSort) {
		return new ObjectObjectImmutablePair<>(GuiHelper.UP_TEXTURE_ID, (index, data) -> moveListItem(dataList, index, -1, onSort));
	}

	public static <T> ObjectObjectImmutablePair<Identifier, ListItem.ActionConsumer<T>> createDownButton(ObjectArrayList<T> dataList, @Nullable Runnable onSort) {
		return new ObjectObjectImmutablePair<>(GuiHelper.DOWN_TEXTURE_ID, (index, data) -> moveListItem(dataList, index, 1, onSort));
	}

	private static <T> void moveListItem(ObjectArrayList<T> dataList, int index, int direction, @Nullable Runnable onSort) {
		if (direction > 0 && index < dataList.size() - 1 || direction < 0 && index > 0) {
			final T data = dataList.remove(index);
			if (Screen.hasShiftDown()) {
				if (direction > 0) {
					dataList.add(data);
				} else {
					dataList.addFirst(data);
				}
			} else {
				dataList.add(index + direction, data);
			}
			if (onSort != null) {
				onSort.run();
			}
		}
	}

	private static void drawPlatformNumber(Drawing drawing, double x, double y, String name) {
		FontGroupRegistry.MTR.get().render(drawing, Utilities.formatName(name), FontRenderOptions.builder()
				.horizontalPositioning(FontRenderOptions.Alignment.CENTER)
				.verticalPositioning(FontRenderOptions.Alignment.CENTER)
				.horizontalSpace(GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT)
				.verticalSpace(GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT)
				.horizontalTextAlignment(FontRenderOptions.Alignment.CENTER)
				.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
				.offsetX((float) x + GuiHelper.DEFAULT_LINE_SIZE / 2F)
				.offsetY((float) y + GuiHelper.DEFAULT_LINE_SIZE / 2F)
				.textOverflow(FontRenderOptions.TextOverflow.SCALE)
				.build()
		);
	}
}
