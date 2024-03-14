package org.mtr.mod.screen;

import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.data.IGui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public abstract class GenericScreenBase<T extends SerializedDataBase> extends ScreenExtension implements IGui {

	protected final T data;
	protected final List<DataElementBase<?>> dataElements = new ArrayList<>();
	private final GenericScreenBase<?> previousScreen;

	public GenericScreenBase(T data, GenericScreenBase<?> previousScreen) {
		super();
		this.data = data;
		this.previousScreen = previousScreen;
	}

	@Override
	protected void init2() {
		super.init2();
		int offsetY = 0;
		for (final DataElementBase<?> dataElement : dataElements) {
			dataElement.init(this, SQUARE_SIZE + TEXT_HEIGHT + TEXT_PADDING + offsetY);
			offsetY += dataElement.getWidgetHeight();
		}
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		renderBackground(graphicsHolder);
		super.render(graphicsHolder, mouseX, mouseY, delta);
		int offsetY = 0;
		for (final DataElementBase<?> dataElement : dataElements) {
			dataElement.render(graphicsHolder, SQUARE_SIZE + TEXT_HEIGHT + TEXT_PADDING + offsetY);
			offsetY += dataElement.getWidgetHeight();
		}
	}

	@Override
	public void onClose2() {
		MinecraftClient.getInstance().openScreen(new Screen(previousScreen));
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}

	protected static abstract class DataElementBase<U> {

		final String title;
		final Supplier<U> getData;
		final Consumer<U> setData;

		static final int OFFSET_X = SQUARE_SIZE + PANEL_WIDTH + TEXT_PADDING;

		DataElementBase(String title, Supplier<U> getData, Consumer<U> setData) {
			this.title = title;
			this.getData = getData;
			this.setData = setData;
		}

		abstract void init(ScreenExtension screen, int offsetY);

		abstract int getWidgetHeight();

		void render(GraphicsHolder graphicsHolder, int offsetY) {
			graphicsHolder.drawText(title, SQUARE_SIZE, offsetY + getWidgetHeight() / 2, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		}
	}

	protected static final class BooleanDataElementBase extends DataElementBase<Boolean> {

		private final CheckboxWidgetExtension checkboxWidgetExtension;

		BooleanDataElementBase(String title, BooleanSupplier getData, Consumer<Boolean> setData) {
			super(title, getData::getAsBoolean, setData);
			checkboxWidgetExtension = new CheckboxWidgetExtension(SQUARE_SIZE, 0, PANEL_WIDTH * 2, SQUARE_SIZE, title, true, setData);
		}

		@Override
		void init(ScreenExtension screen, int offsetY) {
			checkboxWidgetExtension.setChecked(getData.get());
			checkboxWidgetExtension.setY2(offsetY);
			screen.addChild(new ClickableWidget(checkboxWidgetExtension));
		}

		@Override
		int getWidgetHeight() {
			return SQUARE_SIZE;
		}

		@Override
		void render(GraphicsHolder graphicsHolder, int offsetY) {
		}
	}

	protected static final class StringDataElementBase extends DataElementBase<String> {

		private final TextFieldWidgetExtension textFieldWidgetExtension;

		StringDataElementBase(String title, Supplier<String> getData, Consumer<String> setData) {
			super(title, getData, setData);
			textFieldWidgetExtension = new TextFieldWidgetExtension(OFFSET_X + TEXT_FIELD_PADDING / 2, 0, PANEL_WIDTH - TEXT_FIELD_PADDING, SQUARE_SIZE, title, 256, TextCase.DEFAULT, null, title);
		}

		@Override
		void init(ScreenExtension screen, int offsetY) {
			textFieldWidgetExtension.setText2(getData.get());
			textFieldWidgetExtension.setY2(TEXT_FIELD_PADDING / 2 + offsetY);
			screen.addChild(new ClickableWidget(textFieldWidgetExtension));
		}

		@Override
		int getWidgetHeight() {
			return SQUARE_SIZE + TEXT_FIELD_PADDING;
		}
	}

	protected static final class IntegerDataElementBase extends DataElementBase<Integer> {

		private final TextFieldWidgetExtension textFieldWidgetExtension;

		IntegerDataElementBase(String title, IntSupplier getData, IntConsumer setData) {
			super(title, getData::getAsInt, setData::accept);
			textFieldWidgetExtension = new TextFieldWidgetExtension(OFFSET_X + TEXT_FIELD_PADDING / 2, 0, PANEL_WIDTH - TEXT_FIELD_PADDING, SQUARE_SIZE, title, 16, TextCase.DEFAULT, "[^\\d-]", title);
		}

		@Override
		void init(ScreenExtension screen, int offsetY) {
			textFieldWidgetExtension.setText2(getData.get().toString());
			textFieldWidgetExtension.setY2(TEXT_FIELD_PADDING / 2 + offsetY);
			screen.addChild(new ClickableWidget(textFieldWidgetExtension));
		}

		@Override
		int getWidgetHeight() {
			return SQUARE_SIZE + TEXT_FIELD_PADDING;
		}
	}

	protected static final class DoubleDataElementBase extends DataElementBase<Double> {

		private final TextFieldWidgetExtension textFieldWidgetExtension;

		DoubleDataElementBase(String title, DoubleSupplier getData, DoubleConsumer setData) {
			super(title, getData::getAsDouble, setData::accept);
			textFieldWidgetExtension = new TextFieldWidgetExtension(OFFSET_X + TEXT_FIELD_PADDING / 2, 0, PANEL_WIDTH - TEXT_FIELD_PADDING, SQUARE_SIZE, title, 16, TextCase.DEFAULT, "[^\\d.-]", title);
		}

		@Override
		void init(ScreenExtension screen, int offsetY) {
			textFieldWidgetExtension.setText2(getData.get().toString());
			textFieldWidgetExtension.setY2(TEXT_FIELD_PADDING / 2 + offsetY);
			screen.addChild(new ClickableWidget(textFieldWidgetExtension));
		}

		@Override
		int getWidgetHeight() {
			return SQUARE_SIZE + TEXT_FIELD_PADDING;
		}
	}

	protected static final class ChildDataElementBase<T extends SerializedDataBase, U extends SerializedDataBase> extends DataElementBase<U> {

		private final ButtonWidgetExtension buttonWidgetExtension;

		ChildDataElementBase(String title, GenericScreenBase<?> newScreen, Supplier<U> getData, Consumer<U> setData) {
			super(title, getData, setData);
			buttonWidgetExtension = new ButtonWidgetExtension(OFFSET_X, 0, PANEL_WIDTH, SQUARE_SIZE, title, button -> MinecraftClient.getInstance().openScreen(new Screen(newScreen)));
		}

		@Override
		void init(ScreenExtension screen, int offsetY) {
			buttonWidgetExtension.setY2(offsetY);
			screen.addChild(new ClickableWidget(buttonWidgetExtension));
		}

		@Override
		int getWidgetHeight() {
			return SQUARE_SIZE;
		}
	}
}
