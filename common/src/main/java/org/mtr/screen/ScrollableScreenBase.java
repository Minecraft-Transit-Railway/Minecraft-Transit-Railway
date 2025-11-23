package org.mtr.screen;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.BetterButtonWidget;
import org.mtr.widget.PopupWidgetBase;
import org.mtr.widget.ScrollableListWidget;
import org.mtr.widget.ScrollbarWidget;

import javax.annotation.Nullable;

public abstract class ScrollableScreenBase extends ScreenBase {

	@Nullable
	private Object2IntArrayMap<ClickableWidget> childrenYPositions;

	private final ScrollbarWidget scrollbarWidget = new ScrollbarWidget();
	private final BetterButtonWidget doneButton = new BetterButtonWidget(GuiHelper.CHECK_TEXTURE_ID, Text.translatable("gui.done").getString(), 0, this::close);
	private final BetterButtonWidget resetButton = new BetterButtonWidget(GuiHelper.RESET_TEXTURE_ID, TranslationProvider.GUI_MTR_RESET.getString(), 0, this::init);

	protected static final int FULL_WIDGET_WIDTH = GuiHelper.STANDARD_SCREEN_WIDTH - GuiHelper.DEFAULT_PADDING * 4;
	protected static final int HALF_WIDGET_WIDTH = (FULL_WIDGET_WIDTH - GuiHelper.DEFAULT_PADDING) / 2;
	protected static final int ONE_THIRD_WIDGET_WIDTH = (FULL_WIDGET_WIDTH - GuiHelper.DEFAULT_PADDING * 2) / 3;

	private static final int TITLE_SCALE = 2;
	private static final int FOOTER_HEIGHT = GuiHelper.DEFAULT_PADDING * 2 + GuiHelper.DEFAULT_LINE_SIZE * 2;

	public ScrollableScreenBase(@Nullable Screen previousScreen) {
		super(previousScreen);
	}

	@Override
	protected void init() {
		super.init();
		addDrawableChild(scrollbarWidget);
		addDrawableChild(doneButton);
		addDrawableChild(resetButton);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		// Save initial Y positions of children
		if (childrenYPositions == null) {
			childrenYPositions = new Object2IntArrayMap<>();
			children().forEach(child -> {
				if (child instanceof ClickableWidget clickableWidget && clickableWidget != scrollbarWidget && clickableWidget != doneButton && clickableWidget != resetButton && !(clickableWidget instanceof PopupWidgetBase)) {
					childrenYPositions.put(clickableWidget, clickableWidget.getY());
				}
			});
		}

		final ObjectArrayList<OrderedText> titleLines = getTextLines(getScreenTitle());
		final int titleHeight = getTextHeight(titleLines) * TITLE_SCALE;
		final ObjectArrayList<OrderedText> subtitleLines = getTextLines(getScreenSubtitle());
		final int subtitleHeight = getTextHeight(subtitleLines);
		final ObjectArrayList<OrderedText> descriptionLines = getTextLines(getScreenDescription());
		final int descriptionHeight = getTextHeight(descriptionLines);
		final int headerSpacing1 = titleHeight > 0 && (subtitleHeight > 0 || descriptionHeight > 0) ? GuiHelper.DEFAULT_PADDING : 0;
		final int headerSpacing2 = (titleHeight > 0 || subtitleHeight > 0) && descriptionHeight > 0 ? GuiHelper.DEFAULT_PADDING : 0;
		final int headerHeight = GuiHelper.DEFAULT_PADDING + GuiHelper.DEFAULT_LINE_SIZE * 2 + titleHeight + headerSpacing1 + subtitleHeight + headerSpacing2 + descriptionHeight;
		final int scrollY = (int) scrollbarWidget.getScrollY();
		final ObjectArrayList<ScrollableListWidget<?>> listWidgets = new ObjectArrayList<>();

		// Adjust children by scroll position
		final int[] contentHeight = {0};
		childrenYPositions.forEach((clickableWidget, y) -> {
			if (clickableWidget.visible) {
				contentHeight[0] = Math.max(contentHeight[0], y + clickableWidget.getHeight());
				clickableWidget.setY(y + headerHeight - scrollY);
			}

			if (clickableWidget instanceof ScrollableListWidget<?> scrollableListWidget) {
				listWidgets.add(scrollableListWidget);
			}
		});

		// Set scrollbar size and position
		scrollbarWidget.setContentHeight(headerHeight + contentHeight[0] + FOOTER_HEIGHT);
		scrollbarWidget.setHeight(height);
		scrollbarWidget.setPosition(width - ScrollableWidget.SCROLLBAR_WIDTH, 0);

		// Set footer buttons size and position
		final int backgroundX = (width - GuiHelper.STANDARD_SCREEN_WIDTH) / 2 + GuiHelper.DEFAULT_PADDING;
		final int buttonsX = width - backgroundX - GuiHelper.DEFAULT_PADDING;
		final int buttonsY = headerHeight + contentHeight[0] + GuiHelper.DEFAULT_LINE_SIZE - scrollY;
		doneButton.setPosition(buttonsX - GuiHelper.DEFAULT_PADDING - resetButton.getWidth() - doneButton.getWidth(), buttonsY);
		resetButton.setPosition(buttonsX - resetButton.getWidth(), buttonsY);

		super.renderBackground(context, mouseX, mouseY, delta);
		final MatrixStack matrixStack = context.getMatrices();
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final Drawing drawing = new Drawing(matrixStack, RenderLayer.getGui());

		// Draw background
		drawing.setVerticesWH(backgroundX, GuiHelper.DEFAULT_PADDING - scrollY, GuiHelper.STANDARD_SCREEN_WIDTH - GuiHelper.DEFAULT_PADDING * 2, headerHeight + contentHeight[0] + FOOTER_HEIGHT - GuiHelper.DEFAULT_PADDING * 2).setColor(GuiHelper.TRANSLUCENT_BACKGROUND_COLOR).draw();
		listWidgets.forEach(scrollableListWidget -> drawing.setVerticesWH(scrollableListWidget.getX(), scrollableListWidget.getY(), scrollableListWidget.getWidth(), scrollableListWidget.getHeight()).setColor(GuiHelper.BACKGROUND_COLOR).draw());

		// Draw title
		matrixStack.push();
		matrixStack.translate(0, GuiHelper.DEFAULT_PADDING + GuiHelper.DEFAULT_LINE_SIZE - scrollY, 0);
		matrixStack.push();
		matrixStack.translate(width / 2F, 0, 0);
		matrixStack.push();
		matrixStack.scale(TITLE_SCALE, TITLE_SCALE, 1);
		for (int i = 0; i < titleLines.size(); i++) {
			final OrderedText text = titleLines.get(i);
			context.drawText(minecraftClient.textRenderer, text, -minecraftClient.textRenderer.getWidth(text) / 2, i * GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT, GuiHelper.WHITE_COLOR, false);
		}
		matrixStack.pop();

		// Draw subtitle
		for (int i = 0; i < subtitleLines.size(); i++) {
			final OrderedText text = subtitleLines.get(i);
			context.drawText(minecraftClient.textRenderer, text, -minecraftClient.textRenderer.getWidth(text) / 2, titleHeight + headerSpacing1 + i * GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT, GuiHelper.WHITE_COLOR, false);
		}
		matrixStack.pop();

		// Draw description
		for (int i = 0; i < descriptionLines.size(); i++) {
			final OrderedText text = descriptionLines.get(i);
			context.drawText(minecraftClient.textRenderer, text, backgroundX + GuiHelper.DEFAULT_PADDING, titleHeight + headerSpacing1 + subtitleHeight + headerSpacing2 + i * GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT, GuiHelper.WHITE_COLOR, false);
		}
		matrixStack.pop();

		super.render(context, mouseX, mouseY, delta);
	}

	@Override
	public final boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (mouseX < width - ScrollableWidget.SCROLLBAR_WIDTH) {
			scrollbarWidget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
		}
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	public final void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
	}

	/**
	 * Get the screen title. This will be centre-aligned and rendered bigger than the other text.
	 *
	 * @return a list of text lines to render as the title
	 */
	public abstract ObjectArrayList<MutableText> getScreenTitle();

	/**
	 * Get the screen subtitle. This will be centre-aligned.
	 *
	 * @return a list of text lines to render as the subtitle
	 */
	public abstract ObjectArrayList<MutableText> getScreenSubtitle();

	/**
	 * Get the screen description. This will be left-aligned.
	 *
	 * @return a list of text lines to render as the description
	 */
	public abstract ObjectArrayList<MutableText> getScreenDescription();

	protected int getWidgetColumn1() {
		return (width - GuiHelper.STANDARD_SCREEN_WIDTH) / 2 + GuiHelper.DEFAULT_PADDING * 2;
	}

	protected int getWidgetColumn2Of2() {
		return getWidgetColumn1() + HALF_WIDGET_WIDTH + GuiHelper.DEFAULT_PADDING;
	}

	protected int getWidgetColumn2Of3() {
		return getWidgetColumn1() + ONE_THIRD_WIDGET_WIDTH + GuiHelper.DEFAULT_PADDING;
	}

	protected int getWidgetColumn3Of3() {
		return getWidgetColumn1() + ONE_THIRD_WIDGET_WIDTH * 2 + GuiHelper.DEFAULT_PADDING * 2;
	}

	private static ObjectArrayList<OrderedText> getTextLines(ObjectArrayList<MutableText> text) {
		final ObjectArrayList<OrderedText> lines = new ObjectArrayList<>();
		text.forEach(textPart -> lines.addAll(MinecraftClient.getInstance().textRenderer.wrapLines(textPart, GuiHelper.STANDARD_SCREEN_WIDTH - GuiHelper.DEFAULT_PADDING * 2 - GuiHelper.DEFAULT_LINE_SIZE * 2)));
		return lines;
	}

	private static int getTextHeight(ObjectArrayList<OrderedText> lines) {
		return lines.isEmpty() ? 0 : ((lines.size() - 1) * GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT + GuiHelper.MINECRAFT_FONT_SIZE);
	}
}
