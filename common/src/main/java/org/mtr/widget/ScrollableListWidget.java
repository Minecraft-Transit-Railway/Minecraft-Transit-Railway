package org.mtr.widget;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.mtr.client.CustomResourceLoader;
import org.mtr.core.data.*;
import org.mtr.core.tool.Utilities;
import org.mtr.font.FontGroups;
import org.mtr.font.FontRenderOptions;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;
import org.mtr.tool.RouteHelper;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.function.Consumer;

public final class ScrollableListWidget<T> extends ScrollablePanelWidget {

	@Nullable
	private Runnable clickAction;
	private int minWidth, minHeight;
	private int maxWidth, maxHeight;
	private int rawHeight;

	private final ObjectArrayList<ListItem<T>> dataList = new ObjectArrayList<>();

	private static final int DELETE_COLOR = 0xFFCC0000;

	public ScrollableListWidget() {
		super(0, 0, 0, 0);
	}

	@Override
	protected void render(DrawContext context, int mouseX, int mouseY) {
		clickAction = null;
		final FontRenderOptions.FontRenderOptionsBuilder fontRenderOptionsBuilder = initDimensions();
		final MatrixStack matrixStack = context.getMatrices();
		final Drawing drawing = new Drawing(matrixStack, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui()));
		final ObjectArrayList<Runnable> deferredRenders = new ObjectArrayList<>();

		ListItem.iterateData(dataList, (dataIndex, level, listItem) -> {
			final int startX = getX() + level * GuiHelper.DEFAULT_PADDING;
			final int endX = getX() + width - getScrollbarWidth();
			final double startY = getY() - getScrollY() + GuiHelper.DEFAULT_LINE_SIZE * dataIndex;

			if (startY + GuiHelper.DEFAULT_LINE_SIZE > getY()) {
				final boolean isMouseOver = Utilities.isBetween(mouseY, startY, startY + GuiHelper.DEFAULT_LINE_SIZE - 1) && Utilities.isBetween(mouseX, getX(), endX - 1);

				// Detect hovering
				if (isMouseOver) {
					listItem.iterateActions((actionIndex, identifier, callback) -> {
						final int leftBound = endX - GuiHelper.DEFAULT_LINE_SIZE * (listItem.actionCount() - actionIndex);
						final int rightBound = leftBound + GuiHelper.DEFAULT_LINE_SIZE;

						// Draw hover highlight
						if (Utilities.isBetween(mouseX, actionIndex == 0 ? getX() : leftBound, rightBound - 1)) {
							drawing.setVerticesWH(leftBound, startY, GuiHelper.DEFAULT_LINE_SIZE, GuiHelper.DEFAULT_LINE_SIZE).setColor(identifier.getPath().endsWith("icon_delete.png") ? DELETE_COLOR : GuiHelper.HOVER_COLOR).draw();
							if (actionIndex == 0) {
								drawing.setVertices(Math.max(getX(), leftBound - GuiHelper.DEFAULT_LINE_SIZE), startY, leftBound, startY + GuiHelper.DEFAULT_LINE_SIZE).setColor(GuiHelper.BACKGROUND_COLOR, GuiHelper.BACKGROUND_COLOR, GuiHelper.HOVER_COLOR, GuiHelper.HOVER_COLOR).draw();
							}
							clickAction = callback;
						}

						// Draw the action button
						deferredRenders.add(() -> new Drawing(matrixStack, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGuiTextured(identifier)))
								.setVerticesWH(leftBound, startY, GuiHelper.DEFAULT_LINE_SIZE, GuiHelper.DEFAULT_LINE_SIZE)
								.setUv()
								.draw()
						);
					});
				}

				// Draw icon
				listItem.drawIcon.draw(drawing, startX, startY);

				// Draw text
				FontGroups.renderMTR(drawing, listItem.text, fontRenderOptionsBuilder
						.horizontalSpace(endX - startX - listItem.iconWidth - GuiHelper.DEFAULT_PADDING * 2 - (isMouseOver ? GuiHelper.DEFAULT_LINE_SIZE * listItem.actionCount() : 0))
						.offsetX(startX + listItem.iconWidth + GuiHelper.DEFAULT_PADDING)
						.offsetY((float) startY)
						.textOverflow(FontRenderOptions.TextOverflow.COMPRESS)
						.build()
				);
			}

			return startY >= getY() + getHeight();
		});

		deferredRenders.forEach(Runnable::run);
	}

	@Override
	protected void onClickNew(double mouseX, double mouseY) {
		if (clickAction != null) {
			clickAction.run();
		}
	}

	@Override
	protected double getDeltaYPerScroll() {
		return GuiHelper.DEFAULT_LINE_SIZE;
	}

	@Override
	protected int getContentsHeightWithPadding() {
		return rawHeight;
	}

	public void setData(ObjectArrayList<ListItem<T>> dataList) {
		this.dataList.clear();
		this.dataList.addAll(dataList);
		initDimensions();
	}

	public void setBounds(int minWidth, int minHeight, int maxWidth, int maxHeight) {
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		initDimensions();
	}

	private FontRenderOptions.FontRenderOptionsBuilder initDimensions() {
		final FontRenderOptions.FontRenderOptionsBuilder fontRenderOptionsBuilder = FontRenderOptions.builder()
				.maxFontSize(GuiHelper.MINECRAFT_FONT_SIZE)
				.verticalSpace(GuiHelper.DEFAULT_LINE_SIZE)
				.verticalTextAlignment(FontRenderOptions.Alignment.CENTER);

		final boolean fixedWidth = minWidth == maxWidth;
		final boolean fixedHeight = minHeight == maxHeight;
		final int[] count = {0};
		final int[] maxLineWidth = {0};

		ListItem.iterateData(dataList, (index, level, listItem) -> {
			if (!fixedWidth && maxLineWidth[0] < maxWidth) {
				maxLineWidth[0] = Math.max(maxLineWidth[0], (level + 2) * GuiHelper.DEFAULT_PADDING + listItem.iconWidth + (int) Math.ceil(FontGroups.renderMTR(null, listItem.text, fontRenderOptionsBuilder.build()).leftFloat()) + listItem.actionCount() * GuiHelper.DEFAULT_LINE_SIZE + getScrollbarWidth());
			}
			count[0]++;
			return false;
		});

		rawHeight = GuiHelper.DEFAULT_LINE_SIZE * count[0];
		setWidth(fixedWidth ? minWidth : Utilities.clamp(maxLineWidth[0], minWidth, maxWidth));
		setHeight(fixedHeight ? minHeight : Utilities.clamp(rawHeight, minHeight, maxHeight));
		return fontRenderOptionsBuilder;
	}

	public static <T extends AreaBase<T, U>, U extends SavedRailBase<U, T>> void setAreas(ScrollableListWidget<T> scrollableListWidget, ObjectArraySet<T> areas, ObjectArrayList<ObjectObjectImmutablePair<Identifier, Consumer<T>>> actions) {
		final ObjectArrayList<T> sortedAreas = new ObjectArrayList<>(areas);
		Collections.sort(sortedAreas);
		final ObjectArrayList<ListItem<T>> dataList = new ObjectArrayList<>();
		sortedAreas.forEach(area -> dataList.add(ListItem.createChild(
				(drawing, x, y) -> drawing.setVerticesWH(x + GuiHelper.DEFAULT_PADDING, y + GuiHelper.DEFAULT_PADDING, GuiHelper.MINECRAFT_FONT_SIZE, GuiHelper.MINECRAFT_FONT_SIZE).setColor(ColorHelper.fullAlpha(area.getColor())).draw(),
				GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE,
				Utilities.formatName(area.getName()),
				area,
				actions
		)));
		scrollableListWidget.setData(dataList);
	}

	public static <T extends AreaBase<T, U>, U extends SavedRailBase<U, T>> void setSavedRails(ScrollableListWidget<U> scrollableListWidget, ObjectArraySet<U> savedRails, ObjectArrayList<ObjectObjectImmutablePair<Identifier, Consumer<U>>> actions) {
		final ObjectArrayList<U> sortedSavedRails = new ObjectArrayList<>(savedRails);
		Collections.sort(sortedSavedRails);
		final ObjectArrayList<ListItem<U>> dataList = new ObjectArrayList<>();
		sortedSavedRails.forEach(savedRail -> {
			final IntArrayList colors;
			final String text;
			if (savedRail instanceof Platform) {
				final ObjectObjectImmutablePair<IntArrayList, String> colorsAndDestinationString = RouteHelper.getRouteColorsAndDestinationString(savedRail.getId(), true, false);
				colors = colorsAndDestinationString.left();
				text = colorsAndDestinationString.right().isEmpty() ? TranslationProvider.GUI_MTR_EMPTY_PLATFORM.getString() : Utilities.formatName(colorsAndDestinationString.right());
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
						FontGroups.renderMTR(drawing, Utilities.formatName(savedRail.getName()), FontRenderOptions.builder()
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
					},
					GuiHelper.DEFAULT_LINE_SIZE - GuiHelper.DEFAULT_PADDING / 2,
					text,
					savedRail,
					actions
			));
		});
		scrollableListWidget.setData(dataList);
	}
}
