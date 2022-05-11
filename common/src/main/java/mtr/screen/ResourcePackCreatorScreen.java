package mtr.screen;

import com.google.gson.JsonArray;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mtr.client.IDrawing;
import mtr.data.DataConverter;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.mappings.ScreenMapper;
import mtr.mappings.UtilitiesClient;
import mtr.render.RenderTrains;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ResourcePackCreatorScreen extends ScreenMapper implements IGui {

	private float translation;
	private float pitch;
	private float scale = 10;
	private int editingPartIndex = -1;

	private final DashboardList availableModelPartsList;
	private final DashboardList usedModelPartsList;
	private final Button buttonChooseModelFile;
	private final Button buttonChoosePropertiesFile;
	private final Button buttonChooseTextureFile;

	private static final int ARGB_INTERIOR = 0xFBBC04;
	private static final int ARGB_EXTERIOR = 0x34A853;

	public ResourcePackCreatorScreen() {
		super(new TextComponent(""));
		availableModelPartsList = new DashboardList(null, null, null, null, this::onAdd, null, null, () -> "", text -> {
		});
		usedModelPartsList = new DashboardList(null, null, this::onEdit, null, null, this::onDelete, null, () -> "", text -> {
		});
		buttonChooseModelFile = new Button(0, 0, 0, SQUARE_SIZE, new TextComponent(""), button -> buttonCallback(path -> {
			RenderTrains.creatorProperties.loadModelFile(path);
			updateModelButtonAndPartsList();
		}));
		buttonChoosePropertiesFile = new Button(0, 0, 0, SQUARE_SIZE, new TextComponent(""), button -> buttonCallback(path -> {
			RenderTrains.creatorProperties.loadPropertiesFile(path);
			updatePropertiesButton();
		}));
		buttonChooseTextureFile = new Button(0, 0, 0, SQUARE_SIZE, new TextComponent(""), button -> buttonCallback(path -> {
			RenderTrains.creatorProperties.loadTextureFile(path);
			updateTextureButton();
		}));
	}

	@Override
	protected void init() {
		super.init();
		updateModelButtonAndPartsList();
		updatePropertiesButton();
		updateTextureButton();

		availableModelPartsList.y = SQUARE_SIZE * 4;
		availableModelPartsList.width = PANEL_WIDTH;
		availableModelPartsList.height = height - SQUARE_SIZE * 4;
		usedModelPartsList.x = width;
		usedModelPartsList.y = SQUARE_SIZE;
		usedModelPartsList.width = PANEL_WIDTH;
		usedModelPartsList.height = height;
		IDrawing.setPositionAndWidth(buttonChooseModelFile, 0, 0, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonChoosePropertiesFile, 0, SQUARE_SIZE, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonChooseTextureFile, 0, SQUARE_SIZE * 2, PANEL_WIDTH);

		availableModelPartsList.init(this::addDrawableChild);
		usedModelPartsList.init(this::addDrawableChild);
		addDrawableChild(buttonChooseModelFile);
		addDrawableChild(buttonChoosePropertiesFile);
		addDrawableChild(buttonChooseTextureFile);
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);

			matrices.pushPose();
			matrices.translate((width - PANEL_WIDTH) / 2F + PANEL_WIDTH + translation, height / 2F, 250);
			matrices.scale(scale, scale, -scale);
			matrices.mulPose(Vector3f.YP.rotationDegrees(90));
			matrices.mulPose(Vector3f.ZP.rotation(pitch));
			RenderTrains.creatorProperties.render(matrices);
			matrices.popPose();

			matrices.pushPose();
			matrices.translate(0, 0, 500);
			Gui.fill(matrices, 0, 0, PANEL_WIDTH, height, ARGB_BACKGROUND);
			Gui.fill(matrices, usedModelPartsList.x, 0, width, height, ARGB_BACKGROUND);
			availableModelPartsList.render(matrices, font);
			usedModelPartsList.render(matrices, font);
			super.render(matrices, mouseX, mouseY, delta);
			drawCenteredString(matrices, font, new TranslatableComponent("gui.mtr.available_model_parts"), PANEL_WIDTH / 2 + availableModelPartsList.x, SQUARE_SIZE * 3 + TEXT_PADDING, ARGB_WHITE);
			drawCenteredString(matrices, font, new TranslatableComponent("gui.mtr.used_model_parts"), PANEL_WIDTH / 2 + usedModelPartsList.x, TEXT_PADDING, ARGB_WHITE);
			matrices.popPose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		availableModelPartsList.mouseMoved(mouseX, mouseY);
		usedModelPartsList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		if (mouseX > PANEL_WIDTH) {
			scale += amount;
		}
		availableModelPartsList.mouseScrolled(mouseX, mouseY, amount);
		usedModelPartsList.mouseScrolled(mouseX, mouseY, amount);
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
		availableModelPartsList.tick();
		usedModelPartsList.tick();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private List<DataConverter> updatePartsList(DashboardList dashboardList, JsonArray jsonArray, int x) {
		final boolean isNotEmpty = jsonArray.size() > 0;
		dashboardList.x = isNotEmpty ? x : width;
		final List<DataConverter> partsListData = new ArrayList<>();
		if (isNotEmpty) {
			try {
				jsonArray.forEach(jsonElement -> partsListData.add(new DataConverter(jsonElement.getAsJsonObject().get("name").getAsString(), ARGB_BLACK)));
			} catch (Exception ignored) {
			}
		}
		return partsListData;
	}

	private void updateModelButtonAndPartsList() {
		final String fileName = RenderTrains.creatorProperties.getModelFileName();
		buttonChooseModelFile.setMessage(fileName.isEmpty() ? new TranslatableComponent("gui.mtr.choose_model_file") : new TextComponent(fileName));
		availableModelPartsList.setData(updatePartsList(availableModelPartsList, RenderTrains.creatorProperties.getModelPartsArray(), 0), false, false, false, false, true, false);
	}

	private void updatePropertiesButton() {
		final String fileName = RenderTrains.creatorProperties.getPropertiesFileName();
		buttonChoosePropertiesFile.setMessage(fileName.isEmpty() ? new TranslatableComponent("gui.mtr.choose_properties_file") : new TextComponent(fileName));
		usedModelPartsList.setData(updatePartsList(usedModelPartsList, RenderTrains.creatorProperties.getPropertiesPartsArray(), width - PANEL_WIDTH), false, false, true, false, false, true);
	}

	private void updateTextureButton() {
		final String fileName = RenderTrains.creatorProperties.getTextureFileName();
		buttonChooseTextureFile.setMessage(fileName.isEmpty() ? new TranslatableComponent("gui.mtr.choose_texture_file") : new TextComponent(fileName));
	}

	private void onAdd(NameColorDataBase data, int index) {
		RenderTrains.creatorProperties.addPart(data.name);
		usedModelPartsList.setData(updatePartsList(usedModelPartsList, RenderTrains.creatorProperties.getPropertiesPartsArray(), width - PANEL_WIDTH), false, false, true, false, false, true);
	}

	private void onEdit(NameColorDataBase data, int index) {
		editingPartIndex = index;
	}

	private void onDelete(NameColorDataBase data, int index) {
		RenderTrains.creatorProperties.removePart(index);
		editingPartIndex = -1;
		usedModelPartsList.setData(updatePartsList(usedModelPartsList, RenderTrains.creatorProperties.getPropertiesPartsArray(), width - PANEL_WIDTH), false, false, true, false, false, true);
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
