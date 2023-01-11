package mtr.screen;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.CustomResources;
import mtr.client.ICustomResources;
import mtr.client.IDrawing;
import mtr.client.IResourcePackCreatorProperties;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.render.RenderTrains;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;
import java.util.Locale;
import java.util.function.Consumer;

public class ResourcePackCreatorOptionsScreen extends ScreenMapper implements IResourcePackCreatorProperties, ICustomResources, IGui {

	private final ResourcePackCreatorScreen resourcePackCreatorScreen;

	private final Button buttonChooseModelFile;
	private final Button buttonChoosePropertiesFile;
	private final Button buttonChooseTextureFile;

	private final WidgetBetterTextField textFieldId;
	private final WidgetBetterTextField textFieldName;
	private final WidgetColorSelector colorSelector;
	private final WidgetBetterTextField textFieldGangwayConnectionId;
	private final WidgetBetterTextField textFieldTrainBarrierId;
	private final WidgetShorterSlider sliderRiderOffset;

	private final Button buttonDone;
	private final Button buttonExport;

	private static final Component FILE_MODEL_TEXT = Text.translatable("gui.mtr.file_model");
	private static final Component FILE_PROPERTIES_TEXT = Text.translatable("gui.mtr.file_properties");
	private static final Component FILE_TEXTURE_TEXT = Text.translatable("gui.mtr.file_texture");
	private static final Component NAME_TEXT = Text.translatable("gui.mtr.custom_resources_name");
	private static final Component ID_TEXT = Text.translatable("gui.mtr.custom_resources_id");
	private static final Component GANGWAY_CONNECTION_ID_TEXT = Text.translatable("gui.mtr.custom_resources_gangway_connection_id");
	private static final Component TRAIN_BARRIER_ID_TEXT = Text.translatable("gui.mtr.custom_resources_train_barrier_id");

	public ResourcePackCreatorOptionsScreen(ResourcePackCreatorScreen resourcePackCreatorScreen) {
		super(Text.literal(""));
		this.resourcePackCreatorScreen = resourcePackCreatorScreen;

		buttonChooseModelFile = UtilitiesClient.newButton(button -> buttonCallback(path -> {
			RenderTrains.creatorProperties.loadModelFile(path);
			updateControls(false);
		}));
		buttonChoosePropertiesFile = UtilitiesClient.newButton(button -> buttonCallback(path -> {
			RenderTrains.creatorProperties.loadPropertiesFile(path);
			updateControls(false);
		}));
		buttonChooseTextureFile = UtilitiesClient.newButton(button -> buttonCallback(path -> {
			RenderTrains.creatorProperties.loadTextureFile(path);
			updateControls(false);
		}));

		textFieldId = new WidgetBetterTextField("my_custom_train_id");
		textFieldName = new WidgetBetterTextField("My Custom Train Name");
		colorSelector = new WidgetColorSelector(this, true, this::onUpdateColor);
		textFieldGangwayConnectionId = new WidgetBetterTextField("mtr:textures/entity/sp1900");
		textFieldTrainBarrierId = new WidgetBetterTextField("mtr:textures/entity/r211");
		sliderRiderOffset = new WidgetShorterSlider(0, PANEL_WIDTH, 18, value -> {
			RenderTrains.creatorProperties.editCustomResourcesRiderOffset((value - 2) / 4F);
			updateControls(true);
			return Text.translatable("gui.mtr.custom_resources_rider_offset", (value - 2) / 4F).getString();
		}, null);

		buttonDone = UtilitiesClient.newButton(Text.translatable("gui.done"), button -> onClose());
		buttonExport = UtilitiesClient.newButton(Text.translatable("gui.mtr.custom_resources_export_resource_pack"), button -> RenderTrains.creatorProperties.export());
	}

	@Override
	protected void init() {
		super.init();
		final int newWidth = width - SQUARE_SIZE * 2;
		final int yStart = (height - SQUARE_SIZE * 10 - TEXT_FIELD_PADDING * 4) / 2;

		final int textWidth = Math.max(Math.max(font.width(FILE_MODEL_TEXT), Math.max(font.width(FILE_PROPERTIES_TEXT), font.width(FILE_TEXTURE_TEXT))), Math.max(Math.max(font.width(NAME_TEXT), font.width(ID_TEXT)), Math.max(font.width(GANGWAY_CONNECTION_ID_TEXT), font.width(TRAIN_BARRIER_ID_TEXT)))) + TEXT_PADDING;
		IDrawing.setPositionAndWidth(buttonChooseModelFile, SQUARE_SIZE + textWidth, yStart, newWidth - textWidth);
		IDrawing.setPositionAndWidth(buttonChoosePropertiesFile, SQUARE_SIZE + textWidth, yStart + SQUARE_SIZE, newWidth - textWidth);
		IDrawing.setPositionAndWidth(buttonChooseTextureFile, SQUARE_SIZE + textWidth, yStart + SQUARE_SIZE * 2, newWidth - textWidth);

		IDrawing.setPositionAndWidth(textFieldId, SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2, yStart + SQUARE_SIZE * 7 / 2 + TEXT_FIELD_PADDING / 2, newWidth - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING - textWidth);
		IDrawing.setPositionAndWidth(colorSelector, SQUARE_SIZE + newWidth + TEXT_FIELD_PADDING / 2 - SQUARE_SIZE * 2, yStart + SQUARE_SIZE * 7 / 2 + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2 - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldName, SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2, yStart + SQUARE_SIZE * 9 / 2 + TEXT_FIELD_PADDING * 3 / 2, newWidth - TEXT_FIELD_PADDING - textWidth);
		IDrawing.setPositionAndWidth(textFieldGangwayConnectionId, SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2, yStart + SQUARE_SIZE * 11 / 2 + TEXT_FIELD_PADDING * 5 / 2, newWidth - TEXT_FIELD_PADDING - textWidth);
		IDrawing.setPositionAndWidth(textFieldTrainBarrierId, SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2, yStart + SQUARE_SIZE * 13 / 2 + TEXT_FIELD_PADDING * 7 / 2, newWidth - TEXT_FIELD_PADDING - textWidth);

		textFieldId.setResponder(text -> {
			RenderTrains.creatorProperties.editCustomResourcesId(formatText(textFieldId, text, false));
			updateControls(false);
		});
		textFieldName.setResponder(text -> {
			RenderTrains.creatorProperties.editCustomResourcesName(text);
			updateControls(false);
		});
		textFieldGangwayConnectionId.setResponder(text -> {
			RenderTrains.creatorProperties.editCustomResourcesGangwayConnectionId(formatText(textFieldGangwayConnectionId, text, true));
			updateControls(false);
		});
		textFieldTrainBarrierId.setResponder(text -> {
			RenderTrains.creatorProperties.editCustomResourcesTrainBarrierId(formatText(textFieldTrainBarrierId, text, true));
			updateControls(false);
		});

		IDrawing.setPositionAndWidth(sliderRiderOffset, SQUARE_SIZE, yStart + SQUARE_SIZE * 15 / 2 + TEXT_FIELD_PADDING * 4, textWidth);
		sliderRiderOffset.setHeight(SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonDone, SQUARE_SIZE, yStart + SQUARE_SIZE * 9 + TEXT_FIELD_PADDING * 4, textWidth);
		IDrawing.setPositionAndWidth(buttonExport, SQUARE_SIZE + textWidth, yStart + SQUARE_SIZE * 9 + TEXT_FIELD_PADDING * 4, newWidth - textWidth);

		updateControls(true);

		addDrawableChild(buttonChooseModelFile);
		addDrawableChild(buttonChoosePropertiesFile);
		addDrawableChild(buttonChooseTextureFile);

		addDrawableChild(textFieldId);
		addDrawableChild(colorSelector);
		addDrawableChild(textFieldName);
		addDrawableChild(textFieldGangwayConnectionId);
		addDrawableChild(textFieldTrainBarrierId);
		addDrawableChild(sliderRiderOffset);
		addDrawableChild(buttonDone);
		addDrawableChild(buttonExport);
	}

	@Override
	public void tick() {
		textFieldId.tick();
		textFieldName.tick();
		textFieldGangwayConnectionId.tick();
		textFieldTrainBarrierId.tick();
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			super.render(matrices, mouseX, mouseY, delta);
			final int yStart = (height - SQUARE_SIZE * 10 - TEXT_FIELD_PADDING * 4) / 2;
			drawString(matrices, font, FILE_MODEL_TEXT, SQUARE_SIZE, yStart + TEXT_PADDING, ARGB_WHITE);
			drawString(matrices, font, FILE_PROPERTIES_TEXT, SQUARE_SIZE, yStart + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			drawString(matrices, font, FILE_TEXTURE_TEXT, SQUARE_SIZE, yStart + SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
			drawString(matrices, font, ID_TEXT, SQUARE_SIZE, yStart + SQUARE_SIZE * 7 / 2 + TEXT_FIELD_PADDING / 2 + TEXT_PADDING, ARGB_WHITE);
			drawString(matrices, font, NAME_TEXT, SQUARE_SIZE, yStart + SQUARE_SIZE * 9 / 2 + TEXT_FIELD_PADDING * 3 / 2 + TEXT_PADDING, ARGB_WHITE);
			drawString(matrices, font, GANGWAY_CONNECTION_ID_TEXT, SQUARE_SIZE, yStart + SQUARE_SIZE * 11 / 2 + TEXT_FIELD_PADDING * 5 / 2 + TEXT_PADDING, ARGB_WHITE);
			drawString(matrices, font, TRAIN_BARRIER_ID_TEXT, SQUARE_SIZE, yStart + SQUARE_SIZE * 13 / 2 + TEXT_FIELD_PADDING * 7 / 2 + TEXT_PADDING, ARGB_WHITE);
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
		buttonChooseModelFile.setMessage(modelFileName.isEmpty() ? Text.translatable("gui.mtr.file_upload") : Text.literal(modelFileName));
		final String propertiesFileName = RenderTrains.creatorProperties.getPropertiesFileName();
		buttonChoosePropertiesFile.setMessage(propertiesFileName.isEmpty() ? Text.translatable("gui.mtr.file_upload") : Text.literal(propertiesFileName));
		final String textureFileName = RenderTrains.creatorProperties.getTextureFileName();
		buttonChooseTextureFile.setMessage(textureFileName.isEmpty() ? Text.translatable("gui.mtr.file_upload") : Text.literal(textureFileName));

		final JsonObject customTrainObject = RenderTrains.creatorProperties.getCustomTrainObject();
		final int sliderRiderOffsetValue = Math.round(customTrainObject.get(CUSTOM_TRAINS_RIDER_OFFSET).getAsFloat() * 4 + 2);
		if (sliderRiderOffsetValue != sliderRiderOffset.getIntValue()) {
			sliderRiderOffset.setValue(sliderRiderOffsetValue);
		}

		if (formatTextFields) {
			textFieldId.setValue(RenderTrains.creatorProperties.getCustomTrainId());
			final int color = CustomResources.colorStringToInt(customTrainObject.get(CUSTOM_TRAINS_COLOR).getAsString());
			colorSelector.setColor(color);
			if (color == 0) {
				RenderTrains.creatorProperties.editCustomResourcesColor(colorSelector.getColor());
			}
			textFieldName.setValue(customTrainObject.get(CUSTOM_TRAINS_NAME).getAsString());
			textFieldGangwayConnectionId.setValue(customTrainObject.get(CUSTOM_TRAINS_GANGWAY_CONNECTION_ID).getAsString());
			textFieldTrainBarrierId.setValue(customTrainObject.get(CUSTOM_TRAINS_TRAIN_BARRIER_ID).getAsString());
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

	private static String formatText(WidgetBetterTextField textField, String text, boolean isFileName) {
		String cutText = text.toLowerCase(Locale.ENGLISH).replaceAll(isFileName ? "[^\\w:/]" : "\\W", "");
		while (!cutText.isEmpty() && cutText.substring(0, 1).replaceAll("[^a-z]", "").isEmpty()) {
			cutText = cutText.substring(1);
		}
		if (!cutText.equals(text)) {
			textField.setValue(cutText);
		}
		return cutText;
	}
}
