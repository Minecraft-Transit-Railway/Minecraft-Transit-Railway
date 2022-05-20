package mtr.screen;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.CustomResources;
import mtr.client.ICustomResources;
import mtr.client.IDrawing;
import mtr.client.IResourcePackCreatorProperties;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.UtilitiesClient;
import mtr.render.RenderTrains;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.nio.file.Path;
import java.util.function.Consumer;

public class ResourcePackCreatorOptionsScreen extends ScreenMapper implements IResourcePackCreatorProperties, ICustomResources, IGui {

	private final ResourcePackCreatorScreen resourcePackCreatorScreen;

	private final Button buttonChooseModelFile;
	private final Button buttonChoosePropertiesFile;
	private final Button buttonChooseTextureFile;

	private final WidgetBetterTextField textFieldId;
	private final WidgetBetterTextField textFieldName;
	private final WidgetColorSelector colorSelector;
	private final WidgetBetterCheckbox checkboxHasGangwayConnection;
	private final WidgetShorterSlider sliderRiderOffset;

	private final Button buttonDone;
	private final Button buttonExport;

	private static final TranslatableComponent FILE_MODEL_TEXT = new TranslatableComponent("gui.mtr.file_model");
	private static final TranslatableComponent FILE_PROPERTIES_TEXT = new TranslatableComponent("gui.mtr.file_properties");
	private static final TranslatableComponent FILE_TEXTURE_TEXT = new TranslatableComponent("gui.mtr.file_texture");

	public ResourcePackCreatorOptionsScreen(ResourcePackCreatorScreen resourcePackCreatorScreen) {
		super(new TextComponent(""));
		this.resourcePackCreatorScreen = resourcePackCreatorScreen;

		buttonChooseModelFile = new Button(0, 0, 0, SQUARE_SIZE, new TextComponent(""), button -> buttonCallback(path -> {
			RenderTrains.creatorProperties.loadModelFile(path);
			updateControls(false);
		}));
		buttonChoosePropertiesFile = new Button(0, 0, 0, SQUARE_SIZE, new TextComponent(""), button -> buttonCallback(path -> {
			RenderTrains.creatorProperties.loadPropertiesFile(path);
			updateControls(false);
		}));
		buttonChooseTextureFile = new Button(0, 0, 0, SQUARE_SIZE, new TextComponent(""), button -> buttonCallback(path -> {
			RenderTrains.creatorProperties.loadTextureFile(path);
			updateControls(false);
		}));

		textFieldId = new WidgetBetterTextField("my_custom_train_id");
		textFieldName = new WidgetBetterTextField("My Custom Train Name");
		colorSelector = new WidgetColorSelector(this, this::onUpdateColor);
		checkboxHasGangwayConnection = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, new TranslatableComponent("gui.mtr.custom_resources_has_gangway_connection"), checked -> {
			RenderTrains.creatorProperties.editCustomResourcesHasGangwayConnection(checked);
			updateControls(true);
		});
		sliderRiderOffset = new WidgetShorterSlider(0, PANEL_WIDTH, 18, value -> {
			RenderTrains.creatorProperties.editCustomResourcesRiderOffset((value - 2) / 4F);
			updateControls(true);
			return new TranslatableComponent("gui.mtr.custom_resources_rider_offset", (value - 2) / 4F).getString();
		}, null);

		buttonDone = new Button(0, 0, 0, SQUARE_SIZE, new TranslatableComponent("gui.done"), button -> onClose());
		buttonExport = new Button(0, 0, 0, SQUARE_SIZE, new TranslatableComponent("gui.mtr.custom_resources_export_resource_pack"), button -> RenderTrains.creatorProperties.export());
	}

	@Override
	protected void init() {
		super.init();
		final int xStart = width / 2 - PANEL_WIDTH;
		final int yStart = (height - SQUARE_SIZE * 10 - TEXT_FIELD_PADDING * 2) / 2;

		final int textWidth = Math.max(font.width(FILE_MODEL_TEXT), Math.max(font.width(FILE_PROPERTIES_TEXT), font.width(FILE_TEXTURE_TEXT))) + TEXT_PADDING;
		IDrawing.setPositionAndWidth(buttonChooseModelFile, xStart + textWidth, yStart, PANEL_WIDTH * 2 - textWidth);
		IDrawing.setPositionAndWidth(buttonChoosePropertiesFile, xStart + textWidth, yStart + SQUARE_SIZE, PANEL_WIDTH * 2 - textWidth);
		IDrawing.setPositionAndWidth(buttonChooseTextureFile, xStart + textWidth, yStart + SQUARE_SIZE * 2, PANEL_WIDTH * 2 - textWidth);

		IDrawing.setPositionAndWidth(textFieldId, xStart + TEXT_FIELD_PADDING / 2, yStart + SQUARE_SIZE * 4 + TEXT_FIELD_PADDING / 2, PANEL_WIDTH * 2 - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(colorSelector, xStart + PANEL_WIDTH * 2 + TEXT_FIELD_PADDING / 2 - SQUARE_SIZE * 2, yStart + SQUARE_SIZE * 4 + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2 - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldName, xStart + TEXT_FIELD_PADDING / 2, yStart + SQUARE_SIZE * 5 + TEXT_FIELD_PADDING * 3 / 2, PANEL_WIDTH * 2 - TEXT_FIELD_PADDING);

		textFieldId.setResponder(text -> {
			String cutText = text.toLowerCase().replaceAll("\\W", "");
			while (!cutText.isEmpty() && cutText.substring(0, 1).replaceAll("[^a-z]", "").isEmpty()) {
				cutText = cutText.substring(1);
			}
			if (!cutText.equals(text)) {
				textFieldId.setValue(cutText);
			}
			RenderTrains.creatorProperties.editCustomResourcesId(cutText);
			updateControls(false);
		});
		textFieldName.setResponder(text -> {
			RenderTrains.creatorProperties.editCustomResourcesName(text);
			updateControls(false);
		});

		IDrawing.setPositionAndWidth(checkboxHasGangwayConnection, xStart, yStart + SQUARE_SIZE * 6 + TEXT_FIELD_PADDING * 2, PANEL_WIDTH * 2);
		IDrawing.setPositionAndWidth(sliderRiderOffset, xStart, yStart + SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 2, PANEL_WIDTH);
		sliderRiderOffset.setHeight(SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonDone, xStart, yStart + SQUARE_SIZE * 9 + TEXT_FIELD_PADDING * 2, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonExport, xStart + PANEL_WIDTH, yStart + SQUARE_SIZE * 9 + TEXT_FIELD_PADDING * 2, PANEL_WIDTH);

		updateControls(true);

		addDrawableChild(buttonChooseModelFile);
		addDrawableChild(buttonChoosePropertiesFile);
		addDrawableChild(buttonChooseTextureFile);

		addDrawableChild(textFieldId);
		addDrawableChild(colorSelector);
		addDrawableChild(textFieldName);
		addDrawableChild(checkboxHasGangwayConnection);
		addDrawableChild(sliderRiderOffset);
		addDrawableChild(buttonDone);
		addDrawableChild(buttonExport);
	}

	@Override
	public void tick() {
		textFieldId.tick();
		textFieldName.tick();
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			super.render(matrices, mouseX, mouseY, delta);
			final int xStart = width / 2 - PANEL_WIDTH;
			final int yStart = (height - SQUARE_SIZE * 10 - TEXT_FIELD_PADDING * 2) / 2;
			drawString(matrices, font, new TranslatableComponent("gui.mtr.file_model"), xStart, yStart + TEXT_PADDING, ARGB_WHITE);
			drawString(matrices, font, new TranslatableComponent("gui.mtr.file_properties"), xStart, yStart + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			drawString(matrices, font, new TranslatableComponent("gui.mtr.file_texture"), xStart, yStart + SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		if (minecraft != null) {
			UtilitiesClient.setScreen(minecraft, resourcePackCreatorScreen);
		}
	}

	private void updateControls(boolean formatTextFields) {
		final String modelFileName = RenderTrains.creatorProperties.getModelFileName();
		buttonChooseModelFile.setMessage(modelFileName.isEmpty() ? new TranslatableComponent("gui.mtr.file_upload") : new TextComponent(modelFileName));
		final String propertiesFileName = RenderTrains.creatorProperties.getPropertiesFileName();
		buttonChoosePropertiesFile.setMessage(propertiesFileName.isEmpty() ? new TranslatableComponent("gui.mtr.file_upload") : new TextComponent(propertiesFileName));
		final String textureFileName = RenderTrains.creatorProperties.getTextureFileName();
		buttonChooseTextureFile.setMessage(textureFileName.isEmpty() ? new TranslatableComponent("gui.mtr.file_upload") : new TextComponent(textureFileName));

		final JsonObject customTrainObject = RenderTrains.creatorProperties.getCustomTrainObject();
		checkboxHasGangwayConnection.setChecked(customTrainObject.get(CUSTOM_TRAINS_HAS_GANGWAY_CONNECTION).getAsBoolean());
		final int sliderRiderOffsetValue = Math.round(customTrainObject.get(CUSTOM_TRAINS_RIDER_OFFSET).getAsFloat() * 4 + 2);
		if (sliderRiderOffsetValue != sliderRiderOffset.getIntValue()) {
			sliderRiderOffset.setValue(sliderRiderOffsetValue);
		}

		if (formatTextFields) {
			final String id = RenderTrains.creatorProperties.getCustomTrainId();
			textFieldId.setValue(id);
			final int color = CustomResources.colorStringToInt(customTrainObject.get(CUSTOM_TRAINS_COLOR).getAsString());
			colorSelector.setColor(color);
			textFieldName.setValue(customTrainObject.get(CUSTOM_TRAINS_NAME).getAsString());
			if (color == 0) {
				RenderTrains.creatorProperties.editCustomResourcesColor(colorSelector.getColor());
			}
		}

		buttonExport.active = !textFieldId.getValue().isEmpty() && !textFieldName.getValue().isEmpty() && !RenderTrains.creatorProperties.getModelFileName().isEmpty() && !RenderTrains.creatorProperties.getTextureFileName().isEmpty();
	}

	private void onUpdateColor() {
		RenderTrains.creatorProperties.editCustomResourcesColor(colorSelector.getColor());
		updateControls(false);
	}

	private void buttonCallback(Consumer<Path> callback) {
		if (minecraft != null) {
			UtilitiesClient.setScreen(minecraft, new FileUploaderScreen(this, paths -> {
				if (!paths.isEmpty()) {
					try {
						callback.accept(paths.get(0));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}));
		}
	}
}
