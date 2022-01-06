package mtr.screen;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mtr.MTR;
import mtr.client.DynamicTrainModel;
import mtr.client.IDrawing;
import mtr.data.DataConverter;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.mappings.ScreenMapper;
import mtr.mappings.UtilitiesClient;
import mtr.render.RenderTrains;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ResourcePackCreatorScreen extends ScreenMapper implements IGui {

	private DynamicTrainModel model;
	private float translation;
	private float pitch;
	private float scale = 10;

	private final List<DataConverter> partsListData = new ArrayList<>();
	private final DashboardList partsList;
	private final Button buttonChooseBlockbenchFile;
	private final Button buttonChoosePropertiesFile;
	private final Button buttonChooseTextureFile;

	private static final int ARGB_INTERIOR = 0xFBBC04;
	private static final int ARGB_EXTERIOR = 0x34A853;

	public ResourcePackCreatorScreen() {
		super(new TextComponent(""));
		partsList = new DashboardList(null, null, this::onEdit, null, null, null, null, () -> "", text -> {
		});
		buttonChooseBlockbenchFile = new Button(0, 0, 0, SQUARE_SIZE, new TextComponent(""), button -> buttonCallback(path -> readJson(path, (fileName, jsonObject) -> {
			jsonObject.remove("textures");
			updateModelFile(fileName, jsonObject);
		})));
		buttonChoosePropertiesFile = new Button(0, 0, 0, SQUARE_SIZE, new TextComponent(""), button -> buttonCallback(path -> readJson(path, this::updatePropertiesFile)));
		buttonChooseTextureFile = new Button(0, 0, 0, SQUARE_SIZE, new TextComponent(""), button -> buttonCallback(path -> {
			if (minecraft != null) {
				try {
					final NativeImage nativeImage = NativeImage.read(Files.newInputStream(path, StandardOpenOption.READ));
					final ResourceLocation identifier = minecraft.getTextureManager().register(MTR.MOD_ID, new DynamicTexture(nativeImage));
					updateTextureFile(path.getFileName().toString(), identifier);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));
	}

	@Override
	protected void init() {
		super.init();
		updateModelFile(RenderTrains.creatorModelFileName, RenderTrains.creatorModel);
		updatePropertiesFile(RenderTrains.creatorPropertiesFileName, RenderTrains.creatorProperties);
		updateTextureFile(RenderTrains.creatorTextureFileName, RenderTrains.creatorTexture);

		partsList.y = SQUARE_SIZE * 3;
		partsList.width = PANEL_WIDTH;
		partsList.height = height - SQUARE_SIZE * 3;
		IDrawing.setPositionAndWidth(buttonChooseBlockbenchFile, 0, 0, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonChoosePropertiesFile, 0, SQUARE_SIZE, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonChooseTextureFile, 0, SQUARE_SIZE * 2, PANEL_WIDTH);

		partsList.init(this::addDrawableChild);
		addDrawableChild(buttonChooseBlockbenchFile);
		addDrawableChild(buttonChoosePropertiesFile);
		addDrawableChild(buttonChooseTextureFile);
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);

			if (model != null && RenderTrains.creatorTexture != null && minecraft != null) {
				matrices.pushPose();
				matrices.translate((width - PANEL_WIDTH) / 2F + PANEL_WIDTH + translation, height / 2F, 250);
				matrices.scale(scale, scale, -scale);
				matrices.mulPose(Vector3f.YP.rotationDegrees(90));
				matrices.mulPose(Vector3f.ZP.rotation(pitch));
				final MultiBufferSource.BufferSource immediate = minecraft.renderBuffers().bufferSource();
				model.render(matrices, immediate, RenderTrains.creatorTexture, MAX_LIGHT_INTERIOR, 0, 0, true, true, false, true, true, false, true);
				immediate.endBatch();
				matrices.popPose();
			}

			matrices.pushPose();
			matrices.translate(0, 0, 500);
			Gui.fill(matrices, 0, 0, PANEL_WIDTH, height, ARGB_BACKGROUND);
			partsList.render(matrices, font);
			super.render(matrices, mouseX, mouseY, delta);
			matrices.popPose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		partsList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		if (mouseX > PANEL_WIDTH) {
			scale += amount;
		}
		partsList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (mouseX > PANEL_WIDTH) {
			pitch += deltaY / scale;
			translation += deltaX;
		}
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public void tick() {
		partsList.tick();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void updateModelFile(String fileName, JsonObject jsonObject) {
		jsonObject.remove("textures");
		buttonChooseBlockbenchFile.setMessage(fileName.isEmpty() ? new TranslatableComponent("gui.mtr.choose_blockbench_file") : new TextComponent(fileName));
		RenderTrains.creatorModelFileName = fileName;
		RenderTrains.creatorModel = jsonObject;
		updateModel();

		final boolean isNotEmpty = jsonObject.size() > 0;
		partsList.x = isNotEmpty ? 0 : width;
		partsListData.clear();
		if (isNotEmpty) {
			try {
				jsonObject.getAsJsonArray("outliner").forEach(jsonElement -> partsListData.add(new DataConverter(jsonElement.getAsJsonObject().get("name").getAsString(), ARGB_EXTERIOR)));
			} catch (Exception ignored) {
			}
		}
		partsList.setData(partsListData, false, false, true, false, false, false);
	}

	private void updatePropertiesFile(String fileName, JsonObject jsonObject) {
		buttonChoosePropertiesFile.setMessage(fileName.isEmpty() ? new TranslatableComponent("gui.mtr.choose_properties_file") : new TextComponent(fileName));
		RenderTrains.creatorPropertiesFileName = fileName;
		RenderTrains.creatorProperties = jsonObject;
		updateModel();
	}

	private void updateTextureFile(String fileName, ResourceLocation identifier) {
		buttonChooseTextureFile.setMessage(fileName.isEmpty() ? new TranslatableComponent("gui.mtr.choose_texture_file") : new TextComponent(fileName));
		RenderTrains.creatorTextureFileName = fileName;
		RenderTrains.creatorTexture = identifier;
	}

	private void updateModel() {
		try {
			model = new DynamicTrainModel(RenderTrains.creatorModel, RenderTrains.creatorProperties);
		} catch (Exception ignored) {
		}
	}

	private void onEdit(NameColorDataBase data, int index) {
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

	private void readJson(Path path, BiConsumer<String, JsonObject> jsonCallback) {
		try {
			jsonCallback.accept(path.getFileName().toString(), new JsonParser().parse(String.join("", Files.readAllLines(path))).getAsJsonObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
