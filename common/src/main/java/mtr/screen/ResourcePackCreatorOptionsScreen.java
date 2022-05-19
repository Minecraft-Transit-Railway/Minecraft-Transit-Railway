package mtr.screen;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
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
		textFieldId = new WidgetBetterTextField("my_custom_train");
		textFieldName = new WidgetBetterTextField("My Custom Train");
		colorSelector = new WidgetColorSelector(this, this::onUpdateColor);
	}

	@Override
	protected void init() {
		super.init();
		final int xStart = width / 2 - PANEL_WIDTH;
		final int yStart = (height - SQUARE_SIZE * 10) / 2;

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

		updateControls(true);

		addDrawableChild(buttonChooseModelFile);
		addDrawableChild(buttonChoosePropertiesFile);
		addDrawableChild(buttonChooseTextureFile);
		addDrawableChild(textFieldId);
		addDrawableChild(colorSelector);
		addDrawableChild(textFieldName);
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
			final int yStart = (height - SQUARE_SIZE * 10) / 2;
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

		if (formatTextFields) {
			final String id = RenderTrains.creatorProperties.getCustomTrainId();
			textFieldId.setValue(id);
			final JsonObject customTrainObject = RenderTrains.creatorProperties.getCustomTrainObject();
			final int color = customTrainObject.get(CUSTOM_TRAINS_COLOR).getAsInt();
			colorSelector.setColor(color);
			textFieldName.setValue(customTrainObject.get(CUSTOM_TRAINS_NAME).getAsString());
			if (color == 0) {
				RenderTrains.creatorProperties.editCustomResourcesColor(colorSelector.getColor());
			}
		}
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
