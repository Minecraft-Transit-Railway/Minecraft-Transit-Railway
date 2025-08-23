package org.mtr.screen;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.mtr.block.BlockEyeCandy;
import org.mtr.client.CustomResourceLoader;
import org.mtr.client.IDrawing;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateEyeCandyConfig;
import org.mtr.registry.RegistryClient;
import org.mtr.resource.ObjectResource;
import org.mtr.widget.BetterTextFieldWidget;

public class EyeCandyScreen extends ScreenBase implements IGui {

	private final ButtonWidget buttonSelectModel;
	private final BetterTextFieldWidget textFieldTranslateX;
	private final BetterTextFieldWidget textFieldTranslateY;
	private final BetterTextFieldWidget textFieldTranslateZ;
	private final BetterTextFieldWidget textFieldRotateX;
	private final BetterTextFieldWidget textFieldRotateY;
	private final BetterTextFieldWidget textFieldRotateZ;
	private final CheckboxWidget buttonFullBrightness;

	private static final MutableText SELECT_MODEL_TEXT = TranslationProvider.GUI_MTR_SELECT_MODEL.getMutableText();
	private static final MutableText MODEL_TRANSLATION_TEXT = TranslationProvider.GUI_MTR_MODEL_TRANSLATION.getMutableText();
	private static final MutableText MODEL_ROTATION_TEXT = TranslationProvider.GUI_MTR_MODEL_ROTATION.getMutableText();
	private static final MutableText X_TEXT = Text.literal("X");
	private static final MutableText Y_TEXT = Text.literal("Y");
	private static final MutableText Z_TEXT = Text.literal("Z");
	private static final int MAX_NUMBER_TEXT_LENGTH = 10;

	private final BlockPos blockPos;
	private final BlockEyeCandy.EyeCandyBlockEntity blockEntity;
	private final ObjectImmutableList<ObjectResource> loadedObjects = CustomResourceLoader.getObjects();
	private final LongArrayList selectedModelIndices = new LongArrayList();
	private final int xStart;

	public EyeCandyScreen(BlockPos blockPos, BlockEyeCandy.EyeCandyBlockEntity blockEntity) {
		super();
		this.blockPos = blockPos;
		this.blockEntity = blockEntity;

		final ObjectArrayList<DashboardListItem> objectsForList = new ObjectArrayList<>();
		for (int i = 0; i < loadedObjects.size(); i++) {
			final ObjectResource objectResource = loadedObjects.get(i);
			objectsForList.add(new DashboardListItem(i, objectResource.getName(), objectResource.getColor() | ARGB_BLACK));
			if (objectResource.getId().equals(blockEntity.getModelId())) {
				selectedModelIndices.add(i);
			}
		}

		buttonSelectModel = ButtonWidget.builder(Text.translatable("selectWorld.edit"), button -> MinecraftClient.getInstance().setScreen(new EyeCandyObjectSelectionScreen(new ObjectImmutableList<>(objectsForList), selectedModelIndices, this::sendUpdate, this))).build();
		textFieldTranslateX = new BetterTextFieldWidget(MAX_NUMBER_TEXT_LENGTH, TextCase.DEFAULT, "[^\\d.-]", "0", 0, text -> sendUpdate());
		textFieldTranslateY = new BetterTextFieldWidget(MAX_NUMBER_TEXT_LENGTH, TextCase.DEFAULT, "[^\\d.-]", "0", 0, text -> sendUpdate());
		textFieldTranslateZ = new BetterTextFieldWidget(MAX_NUMBER_TEXT_LENGTH, TextCase.DEFAULT, "[^\\d.-]", "0", 0, text -> sendUpdate());
		textFieldRotateX = new BetterTextFieldWidget(MAX_NUMBER_TEXT_LENGTH, TextCase.DEFAULT, "[^\\d.-]", "0", 0, text -> sendUpdate());
		textFieldRotateY = new BetterTextFieldWidget(MAX_NUMBER_TEXT_LENGTH, TextCase.DEFAULT, "[^\\d.-]", "0", 0, text -> sendUpdate());
		textFieldRotateZ = new BetterTextFieldWidget(MAX_NUMBER_TEXT_LENGTH, TextCase.DEFAULT, "[^\\d.-]", "0", 0, text -> sendUpdate());
		buttonFullBrightness = CheckboxWidget.builder(TranslationProvider.GUI_MTR_MODEL_FULL_BRIGHTNESS.getText(), textRenderer).callback((checkboxWidget, checked) -> sendUpdate()).build();

		xStart = Math.max(textRenderer.getWidth(X_TEXT), Math.max(textRenderer.getWidth(Y_TEXT), textRenderer.getWidth(Z_TEXT)));
	}

	@Override
	protected void init() {
		super.init();

		IDrawing.setPositionAndWidth(buttonSelectModel, SQUARE_SIZE + textRenderer.getWidth(SELECT_MODEL_TEXT) + TEXT_PADDING, SQUARE_SIZE, SQUARE_SIZE * 3);
		IDrawing.setPositionAndWidth(textFieldTranslateX, SQUARE_SIZE + xStart + TEXT_PADDING, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 4);
		IDrawing.setPositionAndWidth(textFieldTranslateY, SQUARE_SIZE + xStart + TEXT_PADDING, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 3 / 2, SQUARE_SIZE * 4);
		IDrawing.setPositionAndWidth(textFieldTranslateZ, SQUARE_SIZE + xStart + TEXT_PADDING, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING * 5 / 2, SQUARE_SIZE * 4);
		IDrawing.setPositionAndWidth(textFieldRotateX, width / 2 + xStart + TEXT_PADDING, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 4);
		IDrawing.setPositionAndWidth(textFieldRotateY, width / 2 + xStart + TEXT_PADDING, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 3 / 2, SQUARE_SIZE * 4);
		IDrawing.setPositionAndWidth(textFieldRotateZ, width / 2 + xStart + TEXT_PADDING, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING * 5 / 2, SQUARE_SIZE * 4);
		IDrawing.setPositionAndWidth(buttonFullBrightness, width / 2, SQUARE_SIZE, width);

		textFieldTranslateX.setText(String.valueOf(blockEntity.getTranslateX()));
		textFieldTranslateY.setText(String.valueOf(blockEntity.getTranslateY()));
		textFieldTranslateZ.setText(String.valueOf(blockEntity.getTranslateZ()));
		textFieldRotateX.setText(String.valueOf(Utilities.round(Math.toDegrees(blockEntity.getRotateX()), 4)));
		textFieldRotateY.setText(String.valueOf(Utilities.round(Math.toDegrees(blockEntity.getRotateY()), 4)));
		textFieldRotateZ.setText(String.valueOf(Utilities.round(Math.toDegrees(blockEntity.getRotateZ()), 4)));
		IGui.setChecked(buttonFullBrightness, blockEntity.getFullBrightness());

		addDrawableChild(buttonSelectModel);
		addDrawableChild(textFieldTranslateX);
		addDrawableChild(textFieldTranslateY);
		addDrawableChild(textFieldTranslateZ);
		addDrawableChild(textFieldRotateX);
		addDrawableChild(textFieldRotateY);
		addDrawableChild(textFieldRotateZ);
		addDrawableChild(buttonFullBrightness);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		super.render(context, mouseX, mouseY, delta);
		context.drawText(textRenderer, SELECT_MODEL_TEXT, SQUARE_SIZE, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE, false);
		context.drawText(textRenderer, MODEL_TRANSLATION_TEXT, SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE, false);
		context.drawText(textRenderer, X_TEXT, SQUARE_SIZE, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING / 2 + TEXT_PADDING, ARGB_WHITE, false);
		context.drawText(textRenderer, Y_TEXT, SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 3 / 2 + TEXT_PADDING, ARGB_WHITE, false);
		context.drawText(textRenderer, Z_TEXT, SQUARE_SIZE, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING * 5 / 2 + TEXT_PADDING, ARGB_WHITE, false);
		context.drawText(textRenderer, MODEL_ROTATION_TEXT, width / 2, SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE, false);
		context.drawText(textRenderer, X_TEXT, width / 2, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING / 2 + TEXT_PADDING, ARGB_WHITE, false);
		context.drawText(textRenderer, Y_TEXT, width / 2, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 3 / 2 + TEXT_PADDING, ARGB_WHITE, false);
		context.drawText(textRenderer, Z_TEXT, width / 2, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING * 5 / 2 + TEXT_PADDING, ARGB_WHITE, false);
	}

	private void sendUpdate() {
		RegistryClient.sendPacketToServer(new PacketUpdateEyeCandyConfig(
				blockPos,
				selectedModelIndices.isEmpty() ? null : Utilities.getElement(loadedObjects, (int) selectedModelIndices.getLong(0)).getId(),
				parse(textFieldTranslateX.getText()),
				parse(textFieldTranslateY.getText()),
				parse(textFieldTranslateZ.getText()),
				(float) Math.toRadians(parse(textFieldRotateX.getText())),
				(float) Math.toRadians(parse(textFieldRotateY.getText())),
				(float) Math.toRadians(parse(textFieldRotateZ.getText())),
				buttonFullBrightness.isChecked()
		));
	}

	private static float parse(String text) {
		try {
			return Float.parseFloat(text);
		} catch (Exception ignored) {
			return 0;
		}
	}
}
