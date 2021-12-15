package mtr.gui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import minecraftmappings.ScreenMapper;
import minecraftmappings.UtilitiesClient;
import mtr.data.DataConverter;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.render.RenderTrains;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ResourcePackCreatorScreen extends ScreenMapper implements IGui {

	private final List<DataConverter> partsListData = new ArrayList<>();
	private final DashboardList partsList;
	private final ButtonWidget buttonChooseBlockbenchFile;
	private final ButtonWidget buttonChoosePropertiesFile;

	private static final int ARGB_INTERIOR = 0xFBBC04;
	private static final int ARGB_EXTERIOR = 0x34A853;

	public ResourcePackCreatorScreen() {
		super(new LiteralText(""));
		partsList = new DashboardList(null, null, this::onEdit, null, null, null, null, () -> "", text -> {
		});
		buttonChooseBlockbenchFile = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> buttonCallback((fileName, jsonObject) -> {
			jsonObject.remove("textures");
			updateModelFile(fileName, jsonObject);
		}));
		buttonChoosePropertiesFile = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> buttonCallback(this::updatePropertiesFile));
	}

	@Override
	protected void init() {
		super.init();
		updateModelFile(RenderTrains.creatorModelFileName, RenderTrains.creatorModel);
		updatePropertiesFile(RenderTrains.creatorPropertiesFileName, RenderTrains.creatorProperties);

		partsList.y = SQUARE_SIZE * 2;
		partsList.width = PANEL_WIDTH;
		partsList.height = height - SQUARE_SIZE * 2;
		IDrawing.setPositionAndWidth(buttonChooseBlockbenchFile, 0, 0, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonChoosePropertiesFile, 0, SQUARE_SIZE, PANEL_WIDTH);

		partsList.init(this::addDrawableChild);
		addDrawableChild(buttonChooseBlockbenchFile);
		addDrawableChild(buttonChoosePropertiesFile);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			DrawableHelper.fill(matrices, 0, 0, PANEL_WIDTH, height, ARGB_BACKGROUND);
			partsList.render(matrices, textRenderer);
			super.render(matrices, mouseX, mouseY, delta);
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
		partsList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
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
		buttonChooseBlockbenchFile.setMessage(fileName.isEmpty() ? new TranslatableText("gui.mtr.choose_blockbench_file") : new LiteralText(fileName));
		RenderTrains.creatorModelFileName = fileName;
		RenderTrains.creatorModel = jsonObject;

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
		buttonChoosePropertiesFile.setMessage(fileName.isEmpty() ? new TranslatableText("gui.mtr.choose_properties_file") : new LiteralText(fileName));
		RenderTrains.creatorPropertiesFileName = fileName;
		RenderTrains.creatorProperties = jsonObject;
	}

	private void onEdit(NameColorDataBase data, int index) {
	}

	private void buttonCallback(BiConsumer<String, JsonObject> jsonCallback) {
		if (client != null) {
			UtilitiesClient.setScreen(client, new FileUploaderScreen(this, paths -> {
				if (!paths.isEmpty()) {
					try {
						jsonCallback.accept(paths.get(0).getFileName().toString(), new JsonParser().parse(String.join("", Files.readAllLines(paths.get(0)))).getAsJsonObject());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}));
		}
	}
}
