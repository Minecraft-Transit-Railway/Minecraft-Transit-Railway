package org.mtr.mod.screen;

import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.Init;
import org.mtr.mod.client.CustomResources;
import org.mtr.mod.client.ICustomResources;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.IResourcePackCreatorProperties;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.RenderTrains;

import java.nio.file.Path;
import java.util.Locale;
import java.util.function.Consumer;

public class ResourcePackCreatorOptionsScreen extends ScreenExtension implements IResourcePackCreatorProperties, ICustomResources, IGui {

	private final ResourcePackCreatorScreen resourcePackCreatorScreen;

	private final ButtonWidgetExtension buttonChooseModelFile;
	private final ButtonWidgetExtension buttonChoosePropertiesFile;
	private final ButtonWidgetExtension buttonChooseTextureFile;

	private final TextFieldWidgetExtension textFieldId;
	private final TextFieldWidgetExtension textFieldName;
	private final WidgetColorSelector colorSelector;
	private final TextFieldWidgetExtension textFieldGangwayConnectionId;
	private final TextFieldWidgetExtension textFieldTrainBarrierId;
	private final WidgetShorterSlider sliderRiderOffset;

	private final ButtonWidgetExtension buttonDone;
	private final ButtonWidgetExtension buttonExport;

	private static final MutableText FILE_MODEL_TEXT = TextHelper.translatable("gui.mtr.file_model");
	private static final MutableText FILE_PROPERTIES_TEXT = TextHelper.translatable("gui.mtr.file_properties");
	private static final MutableText FILE_TEXTURE_TEXT = TextHelper.translatable("gui.mtr.file_texture");
	private static final MutableText NAME_TEXT = TextHelper.translatable("gui.mtr.custom_resources_name");
	private static final MutableText ID_TEXT = TextHelper.translatable("gui.mtr.custom_resources_id");
	private static final MutableText GANGWAY_CONNECTION_ID_TEXT = TextHelper.translatable("gui.mtr.custom_resources_gangway_connection_id");
	private static final MutableText TRAIN_BARRIER_ID_TEXT = TextHelper.translatable("gui.mtr.custom_resources_train_barrier_id");

	public ResourcePackCreatorOptionsScreen(ResourcePackCreatorScreen resourcePackCreatorScreen) {
		super();
		this.resourcePackCreatorScreen = resourcePackCreatorScreen;

		buttonChooseModelFile = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> buttonCallback(path -> {
			RenderTrains.creatorProperties.loadModelFile(path);
			updateControls(false);
		}));
		buttonChoosePropertiesFile = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> buttonCallback(path -> {
			RenderTrains.creatorProperties.loadPropertiesFile(path);
			updateControls(false);
		}));
		buttonChooseTextureFile = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> buttonCallback(path -> {
			RenderTrains.creatorProperties.loadTextureFile(path);
			updateControls(false);
		}));

		textFieldId = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 1024, TextCase.DEFAULT, null, "my_custom_train_id");
		textFieldName = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 1024, TextCase.DEFAULT, null, "My Custom Train Name");
		colorSelector = new WidgetColorSelector(this, true, this::onUpdateColor);
		textFieldGangwayConnectionId = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 1024, TextCase.DEFAULT, null, "mtr:textures/entity/sp1900");
		textFieldTrainBarrierId = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 1024, TextCase.DEFAULT, null, "mtr:textures/entity/r211");
		sliderRiderOffset = new WidgetShorterSlider(0, PANEL_WIDTH, 18, value -> {
			RenderTrains.creatorProperties.editCustomResourcesRiderOffset((value - 2) / 4F);
			updateControls(true);
			return TextHelper.translatable("gui.mtr.custom_resources_rider_offset", (value - 2) / 4F).getString();
		}, null);

		buttonDone = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.done"), button -> onClose2());
		buttonExport = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.mtr.custom_resources_export_resource_pack"), button -> RenderTrains.creatorProperties.export());
	}

	@Override
	protected void init2() {
		super.init2();
		final int newWidth = width - SQUARE_SIZE * 2;
		final int yStart = (height - SQUARE_SIZE * 10 - TEXT_FIELD_PADDING * 4) / 2;

		final int textWidth = Math.max(Math.max(GraphicsHolder.getTextWidth(FILE_MODEL_TEXT), Math.max(GraphicsHolder.getTextWidth(FILE_PROPERTIES_TEXT), GraphicsHolder.getTextWidth(FILE_TEXTURE_TEXT))), Math.max(Math.max(GraphicsHolder.getTextWidth(NAME_TEXT), GraphicsHolder.getTextWidth(ID_TEXT)), Math.max(GraphicsHolder.getTextWidth(GANGWAY_CONNECTION_ID_TEXT), GraphicsHolder.getTextWidth(TRAIN_BARRIER_ID_TEXT)))) + TEXT_PADDING;
		IDrawing.setPositionAndWidth(buttonChooseModelFile, SQUARE_SIZE + textWidth, yStart, newWidth - textWidth);
		IDrawing.setPositionAndWidth(buttonChoosePropertiesFile, SQUARE_SIZE + textWidth, yStart + SQUARE_SIZE, newWidth - textWidth);
		IDrawing.setPositionAndWidth(buttonChooseTextureFile, SQUARE_SIZE + textWidth, yStart + SQUARE_SIZE * 2, newWidth - textWidth);

		IDrawing.setPositionAndWidth(textFieldId, SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2, yStart + SQUARE_SIZE * 7 / 2 + TEXT_FIELD_PADDING / 2, newWidth - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING - textWidth);
		IDrawing.setPositionAndWidth(colorSelector, SQUARE_SIZE + newWidth + TEXT_FIELD_PADDING / 2 - SQUARE_SIZE * 2, yStart + SQUARE_SIZE * 7 / 2 + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2 - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldName, SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2, yStart + SQUARE_SIZE * 9 / 2 + TEXT_FIELD_PADDING * 3 / 2, newWidth - TEXT_FIELD_PADDING - textWidth);
		IDrawing.setPositionAndWidth(textFieldGangwayConnectionId, SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2, yStart + SQUARE_SIZE * 11 / 2 + TEXT_FIELD_PADDING * 5 / 2, newWidth - TEXT_FIELD_PADDING - textWidth);
		IDrawing.setPositionAndWidth(textFieldTrainBarrierId, SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2, yStart + SQUARE_SIZE * 13 / 2 + TEXT_FIELD_PADDING * 7 / 2, newWidth - TEXT_FIELD_PADDING - textWidth);

		textFieldId.setChangedListener2(text -> {
			RenderTrains.creatorProperties.editCustomResourcesId(formatText(textFieldId, text, false));
			updateControls(false);
		});
		textFieldName.setChangedListener2(text -> {
			RenderTrains.creatorProperties.editCustomResourcesName(text);
			updateControls(false);
		});
		textFieldGangwayConnectionId.setChangedListener2(text -> {
			RenderTrains.creatorProperties.editCustomResourcesGangwayConnectionId(formatText(textFieldGangwayConnectionId, text, true));
			updateControls(false);
		});
		textFieldTrainBarrierId.setChangedListener2(text -> {
			RenderTrains.creatorProperties.editCustomResourcesTrainBarrierId(formatText(textFieldTrainBarrierId, text, true));
			updateControls(false);
		});

		IDrawing.setPositionAndWidth(sliderRiderOffset, SQUARE_SIZE, yStart + SQUARE_SIZE * 15 / 2 + TEXT_FIELD_PADDING * 4, textWidth);
		sliderRiderOffset.setHeight2(SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonDone, SQUARE_SIZE, yStart + SQUARE_SIZE * 9 + TEXT_FIELD_PADDING * 4, textWidth);
		IDrawing.setPositionAndWidth(buttonExport, SQUARE_SIZE + textWidth, yStart + SQUARE_SIZE * 9 + TEXT_FIELD_PADDING * 4, newWidth - textWidth);

		updateControls(true);

		addChild(new ClickableWidget(buttonChooseModelFile));
		addChild(new ClickableWidget(buttonChoosePropertiesFile));
		addChild(new ClickableWidget(buttonChooseTextureFile));

		addChild(new ClickableWidget(textFieldId));
		addChild(new ClickableWidget(colorSelector));
		addChild(new ClickableWidget(textFieldName));
		addChild(new ClickableWidget(textFieldGangwayConnectionId));
		addChild(new ClickableWidget(textFieldTrainBarrierId));
		addChild(new ClickableWidget(sliderRiderOffset));
		addChild(new ClickableWidget(buttonDone));
		addChild(new ClickableWidget(buttonExport));
	}

	@Override
	public void tick2() {
		textFieldId.tick3();
		textFieldName.tick3();
		textFieldGangwayConnectionId.tick3();
		textFieldTrainBarrierId.tick3();
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		renderBackground(graphicsHolder);
		super.render(graphicsHolder, mouseX, mouseY, delta);
		final int yStart = (height - SQUARE_SIZE * 10 - TEXT_FIELD_PADDING * 4) / 2;
		graphicsHolder.drawText(FILE_MODEL_TEXT, SQUARE_SIZE, yStart + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		graphicsHolder.drawText(FILE_PROPERTIES_TEXT, SQUARE_SIZE, yStart + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		graphicsHolder.drawText(FILE_TEXTURE_TEXT, SQUARE_SIZE, yStart + SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		graphicsHolder.drawText(ID_TEXT, SQUARE_SIZE, yStart + SQUARE_SIZE * 7 / 2 + TEXT_FIELD_PADDING / 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		graphicsHolder.drawText(NAME_TEXT, SQUARE_SIZE, yStart + SQUARE_SIZE * 9 / 2 + TEXT_FIELD_PADDING * 3 / 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		graphicsHolder.drawText(GANGWAY_CONNECTION_ID_TEXT, SQUARE_SIZE, yStart + SQUARE_SIZE * 11 / 2 + TEXT_FIELD_PADDING * 5 / 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		graphicsHolder.drawText(TRAIN_BARRIER_ID_TEXT, SQUARE_SIZE, yStart + SQUARE_SIZE * 13 / 2 + TEXT_FIELD_PADDING * 7 / 2 + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
	}

	@Override
	public void onClose2() {
		super.onClose2();
		MinecraftClient.getInstance().openScreen(new Screen(resourcePackCreatorScreen));
	}

	private void updateControls(boolean formatTextFields) {
		final String modelFileName = RenderTrains.creatorProperties.getModelFileName();
		buttonChooseModelFile.setMessage2(new Text((modelFileName.isEmpty() ? TextHelper.translatable("gui.mtr.file_upload") : TextHelper.literal(modelFileName)).data));
		final String propertiesFileName = RenderTrains.creatorProperties.getPropertiesFileName();
		buttonChoosePropertiesFile.setMessage2(new Text((propertiesFileName.isEmpty() ? TextHelper.translatable("gui.mtr.file_upload") : TextHelper.literal(propertiesFileName)).data));
		final String textureFileName = RenderTrains.creatorProperties.getTextureFileName();
		buttonChooseTextureFile.setMessage2(new Text((textureFileName.isEmpty() ? TextHelper.translatable("gui.mtr.file_upload") : TextHelper.literal(textureFileName)).data));

		final JsonObject customTrainObject = RenderTrains.creatorProperties.getCustomTrainObject();
		final int sliderRiderOffsetValue = Math.round(customTrainObject.get(CUSTOM_TRAINS_RIDER_OFFSET).getAsFloat() * 4 + 2);
		if (sliderRiderOffsetValue != sliderRiderOffset.getIntValue()) {
			sliderRiderOffset.setValue(sliderRiderOffsetValue);
		}

		if (formatTextFields) {
			textFieldId.setText2(RenderTrains.creatorProperties.getCustomTrainId());
			final int color = CustomResources.colorStringToInt(customTrainObject.get(CUSTOM_TRAINS_COLOR).getAsString());
			colorSelector.setColor(color);
			if (color == 0) {
				RenderTrains.creatorProperties.editCustomResourcesColor(colorSelector.getColor());
			}
			textFieldName.setText2(customTrainObject.get(CUSTOM_TRAINS_NAME).getAsString());
			textFieldGangwayConnectionId.setText2(customTrainObject.get(CUSTOM_TRAINS_GANGWAY_CONNECTION_ID).getAsString());
			textFieldTrainBarrierId.setText2(customTrainObject.get(CUSTOM_TRAINS_TRAIN_BARRIER_ID).getAsString());
		}

		buttonExport.active = !textFieldId.getText2().isEmpty() && !textFieldName.getText2().isEmpty() && !RenderTrains.creatorProperties.getModelFileName().isEmpty() && !RenderTrains.creatorProperties.getTextureFileName().isEmpty();
	}

	private void onUpdateColor() {
		RenderTrains.creatorProperties.editCustomResourcesColor(colorSelector.getColor());
		updateControls(false);
	}

	private void buttonCallback(Consumer<Path> callback) {
		MinecraftClient.getInstance().openScreen(new Screen(new FileUploaderScreen(this, paths -> {
			if (!paths.isEmpty()) {
				try {
					callback.accept(paths.get(0));
				} catch (Exception e) {
					Init.logException(e);
				}
			}
		})));
	}

	private static String formatText(TextFieldWidgetExtension textField, String text, boolean isFileName) {
		String cutText = text.toLowerCase(Locale.ENGLISH).replaceAll(isFileName ? "[^\\w:/]" : "\\W", "");
		while (!cutText.isEmpty() && cutText.substring(0, 1).replaceAll("[^a-z]", "").isEmpty()) {
			cutText = cutText.substring(1);
		}
		if (!cutText.equals(text)) {
			textField.setText2(cutText);
		}
		return cutText;
	}
}
