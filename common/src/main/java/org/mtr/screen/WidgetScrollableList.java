package org.mtr.screen;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.Identifier;
import org.mtr.font.FontGroups;
import org.mtr.font.FontRenderOptions;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public final class WidgetScrollableList<T> extends WidgetScrollablePanel {

	@Nullable
	private Runnable clickAction;

	public final int x1;
	public final int y1;
	public final int x2;
	public final int y2;

	private final ObjectArrayList<T> dataList;
	private final ObjectArrayList<ObjectObjectImmutablePair<Identifier, Consumer<T>>> actions;
	private final Function<T, String> nameProvider;
	private final BiConsumer<DrawContext, T> drawIcon;
	private final int iconWidth;

	public static final int FONT_SIZE = 8;
	public static final int PADDING = 4;
	public static final int LINE_SIZE = FONT_SIZE + PADDING * 2;
	public static final int HOVER_COLOR = 0xFF444444;

	/**
	 * Creates a scrollable list with bounds based on the desired centre point, the render bounds, number of data elements, and the width of the longest text.
	 */
	public static <T> WidgetScrollableList<T> createFlexible(
			ObjectArrayList<T> dataList,
			ObjectArrayList<ObjectObjectImmutablePair<Identifier, Consumer<T>>> actions,
			Function<T, String> nameProvider,
			BiConsumer<DrawContext, T> drawIcon, int iconWidth,
			int centerX, int centerY, int minX, int minY, int maxX, int maxY
	) {
		int maxTextWidth = 0;
		for (final T data : dataList) {
			maxTextWidth = Math.max(maxTextWidth, (int) Math.ceil(FontGroups.renderMTR(null, null, nameProvider.apply(data), FontRenderOptions.builder().maxFontSize(FONT_SIZE).verticalSpace(LINE_SIZE).build()).leftFloat()));
		}

		final int rawWidth = iconWidth + PADDING * 2 + maxTextWidth + LINE_SIZE * actions.size() + SCROLLBAR_WIDTH;
		final int rawHeight = LINE_SIZE * dataList.size();
		return new WidgetScrollableList<>(
				dataList, actions, nameProvider, drawIcon, iconWidth,
				Math.max(minX, Math.min(centerX - Math.floorDiv(rawWidth, 2), maxX - rawWidth)),
				Math.max(minY, Math.min(centerY - Math.floorDiv(rawHeight, 2), maxY - rawHeight)),
				Math.min(maxX, Math.max(centerX + Math.ceilDiv(rawWidth, 2), minX + rawWidth)),
				Math.min(maxY, Math.max(centerY + Math.ceilDiv(rawHeight, 2), minY + rawHeight))
		);
	}

	/**
	 * Creates a scrollable list with fixed dimensions.
	 */
	public static <T> WidgetScrollableList<T> createFixed(
			ObjectArrayList<T> dataList,
			ObjectArrayList<ObjectObjectImmutablePair<Identifier, Consumer<T>>> actions,
			Function<T, String> nameProvider,
			BiConsumer<DrawContext, T> drawIcon, int iconWidth,
			int x, int y, int width, int height
	) {
		return new WidgetScrollableList<>(dataList, actions, nameProvider, drawIcon, iconWidth, x, y, x + width, y + height);
	}

	private WidgetScrollableList(
			ObjectArrayList<T> dataList,
			ObjectArrayList<ObjectObjectImmutablePair<Identifier, Consumer<T>>> actions,
			Function<T, String> nameProvider,
			BiConsumer<DrawContext, T> drawIcon, int iconWidth,
			int x1, int y1, int x2, int y2
	) {
		super(x1, y1, x2 - x1, y2 - y1);
		this.dataList = dataList;
		this.actions = actions;
		this.nameProvider = nameProvider;
		this.drawIcon = drawIcon;
		this.iconWidth = iconWidth;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	protected void render(DrawContext context, int mouseX, int mouseY) {
		clickAction = null;
		final double startY = y1 - getScrollY();
		context.getMatrices().push();
		context.getMatrices().translate(x1, startY, 0);

		for (int i = 0; i < dataList.size(); i++) {
			final T data = dataList.get(i);

			// Detect hovering
			if (mouseY >= startY + LINE_SIZE * i && mouseY < startY + LINE_SIZE * (i + 1) && mouseX >= x1 && mouseX < x2 - getScrollbarWidth()) {
				int leftBound = x1;
				for (int j = 0; j < actions.size(); j++) {
					final int rightBound = x2 - LINE_SIZE * (actions.size() - j - 1) - getScrollbarWidth();
					final ObjectObjectImmutablePair<Identifier, Consumer<T>> action = actions.get(j);
					final Identifier identifier = action.left();

					// Draw hover highlight
					if (mouseX >= leftBound && mouseX < rightBound) {
						context.fill(leftBound - x1, 0, rightBound - x1, LINE_SIZE, identifier.getPath().endsWith("icon_delete") ? 0xFFCC0000 : HOVER_COLOR);
						clickAction = () -> action.right().accept(data);
					}

					// Draw action button
					context.drawGuiTexture(RenderLayer::getGuiTextured, identifier, rightBound - x1 - LINE_SIZE, 0, LINE_SIZE, LINE_SIZE);
					leftBound = rightBound;
				}
			}

			// Draw icon
			drawIcon.accept(context, data);

			// Draw text
			context.getMatrices().push();
			context.getMatrices().translate(iconWidth + PADDING, 0, 0);
			final VertexConsumer vertexConsumer = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui());
			FontGroups.renderMTR(
					context.getMatrices().peek().getPositionMatrix(), vertexConsumer,
					nameProvider.apply(data),
					FontRenderOptions.builder()
							.maxFontSize(FONT_SIZE)
							.horizontalSpace(x2 - x1 - iconWidth - PADDING * 2 - LINE_SIZE * actions.size() - getScrollbarWidth())
							.verticalSpace(LINE_SIZE)
							.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
							.textOverflow(FontRenderOptions.TextOverflow.COMPRESS)
							.build()
			);
			context.getMatrices().pop();
			context.getMatrices().translate(0, LINE_SIZE, 0);
		}

		context.getMatrices().pop();
	}

	@Override
	protected boolean mouseClickedNew(double mouseX, double mouseY, int button) {
		if (clickAction == null) {
			return false;
		} else {
			clickAction.run();
			return true;
		}
	}

	@Override
	protected double getDeltaYPerScroll() {
		return LINE_SIZE;
	}

	@Override
	protected int getContentsHeightWithPadding() {
		return LINE_SIZE * dataList.size();
	}
}
