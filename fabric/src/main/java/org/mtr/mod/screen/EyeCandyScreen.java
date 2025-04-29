package org.mtr.mod.screen;

import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockEyeCandy;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketUpdateEyeCandyConfig;
import org.mtr.mod.resource.ObjectResource;

public class EyeCandyScreen extends ScreenExtension implements IGui {

	private final ButtonWidgetExtension buttonSelectModel;
	private final TextFieldWidgetExtension textFieldTranslateX;
	private final TextFieldWidgetExtension textFieldTranslateY;
	private final TextFieldWidgetExtension textFieldTranslateZ;
	private final TextFieldWidgetExtension textFieldRotateX;
	private final TextFieldWidgetExtension textFieldRotateY;
	private final TextFieldWidgetExtension textFieldRotateZ;
	private final CheckboxWidgetExtension buttonFullBrightness;

	private static final MutableText SELECT_MODEL_TEXT = TranslationProvider.GUI_MTR_SELECT_MODEL.getMutableText();
	private static final MutableText MODEL_TRANSLATION_TEXT = TranslationProvider.GUI_MTR_MODEL_TRANSLATION.getMutableText();
	private static final MutableText MODEL_ROTATION_TEXT = TranslationProvider.GUI_MTR_MODEL_ROTATION.getMutableText();
	private static final MutableText X_TEXT = TextHelper.literal("X");
	private static final MutableText Y_TEXT = TextHelper.literal("Y");
	private static final MutableText Z_TEXT = TextHelper.literal("Z");
	private static final int MAX_NUMBER_TEXT_LENGTH = 10;

	private final BlockPos blockPos;
	private final BlockEyeCandy.BlockEntity blockEntity;
	private final ObjectImmutableList<ObjectResource> loadedObjects = CustomResourceLoader.getObjects();
	private final LongArrayList selectedModelIndices = new LongArrayList();
	private final int xStart;

	public EyeCandyScreen(BlockPos blockPos, BlockEyeCandy.BlockEntity blockEntity) {
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

		buttonSelectModel = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> MinecraftClient.getInstance().openScreen(new Screen(new EyeCandyObjectSelectionScreen(new ObjectImmutableList<>(objectsForList), selectedModelIndices, this::sendUpdate, this))));
		buttonSelectModel.setMessage2(new Text(TextHelper.translatable("selectWorld.edit").data));
		textFieldTranslateX = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_NUMBER_TEXT_LENGTH, TextCase.DEFAULT, "[^\\d.-]", null);
		textFieldTranslateX.setChangedListener2(text -> sendUpdate());
		textFieldTranslateY = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_NUMBER_TEXT_LENGTH, TextCase.DEFAULT, "[^\\d.-]", null);
		textFieldTranslateY.setChangedListener2(text -> sendUpdate());
		textFieldTranslateZ = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_NUMBER_TEXT_LENGTH, TextCase.DEFAULT, "[^\\d.-]", null);
		textFieldTranslateZ.setChangedListener2(text -> sendUpdate());
		textFieldRotateX = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_NUMBER_TEXT_LENGTH, TextCase.DEFAULT, "[^\\d.-]", null);
		textFieldRotateX.setChangedListener2(text -> sendUpdate());
		textFieldRotateY = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_NUMBER_TEXT_LENGTH, TextCase.DEFAULT, "[^\\d.-]", null);
		textFieldRotateY.setChangedListener2(text -> sendUpdate());
		textFieldRotateZ = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_NUMBER_TEXT_LENGTH, TextCase.DEFAULT, "[^\\d.-]", null);
		textFieldRotateZ.setChangedListener2(text -> sendUpdate());
		buttonFullBrightness = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> sendUpdate());
		buttonFullBrightness.setMessage2(TranslationProvider.GUI_MTR_MODEL_FULL_BRIGHTNESS.getText());

		xStart = Math.max(GraphicsHolder.getTextWidth(X_TEXT), Math.max(GraphicsHolder.getTextWidth(Y_TEXT), GraphicsHolder.getTextWidth(Z_TEXT)));
	}

	@Override
	protected void init2() {
		super.init2();

		IDrawing.setPositionAndWidth(buttonSelectModel, SQUARE_SIZE + GraphicsHolder.getTextWidth(SELECT_MODEL_TEXT) + TEXT_PADDING, SQUARE_SIZE, SQUARE_SIZE * 3);
		IDrawing.setPositionAndWidth(textFieldTranslateX, SQUARE_SIZE + xStart + TEXT_PADDING, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 4);
		IDrawing.setPositionAndWidth(textFieldTranslateY, SQUARE_SIZE + xStart + TEXT_PADDING, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 3 / 2, SQUARE_SIZE * 4);
		IDrawing.setPositionAndWidth(textFieldTranslateZ, SQUARE_SIZE + xStart + TEXT_PADDING, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING * 5 / 2, SQUARE_SIZE * 4);
		IDrawing.setPositionAndWidth(textFieldRotateX, width / 2 + xStart + TEXT_PADDING, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 4);
		IDrawing.setPositionAndWidth(textFieldRotateY, width / 2 + xStart + TEXT_PADDING, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 3 / 2, SQUARE_SIZE * 4);
		IDrawing.setPositionAndWidth(textFieldRotateZ, width / 2 + xStart + TEXT_PADDING, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING * 5 / 2, SQUARE_SIZE * 4);
		IDrawing.setPositionAndWidth(buttonFullBrightness, width / 2, SQUARE_SIZE, width);

		textFieldTranslateX.setText2(String.valueOf(blockEntity.getTranslateX()));
		textFieldTranslateY.setText2(String.valueOf(blockEntity.getTranslateY()));
		textFieldTranslateZ.setText2(String.valueOf(blockEntity.getTranslateZ()));
		textFieldRotateX.setText2(String.valueOf(Math.toDegrees(blockEntity.getRotateX())));
		textFieldRotateY.setText2(String.valueOf(Math.toDegrees(blockEntity.getRotateY())));
		textFieldRotateZ.setText2(String.valueOf(Math.toDegrees(blockEntity.getRotateZ())));
		buttonFullBrightness.setChecked(blockEntity.getFullBrightness());

		addChild(new ClickableWidget(buttonSelectModel));
		addChild(new ClickableWidget(textFieldTranslateX));
		addChild(new ClickableWidget(textFieldTranslateY));
		addChild(new ClickableWidget(textFieldTranslateZ));
		addChild(new ClickableWidget(textFieldRotateX));
		addChild(new ClickableWidget(textFieldRotateY));
		addChild(new ClickableWidget(textFieldRotateZ));
		addChild(new ClickableWidget(buttonFullBrightness));
	}

	@Override
	public void tick2() {
		super.tick2();
		textFieldTranslateX.tick2();
		textFieldTranslateY.tick2();
		textFieldTranslateZ.tick2();
		textFieldRotateX.tick2();
		textFieldRotateY.tick2();
		textFieldRotateZ.tick2();
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		renderBackground(graphicsHolder);
		super.render(graphicsHolder, mouseX, mouseY, delta);
		graphicsHolder.drawText(SELECT_MODEL_TEXT, SQUARE_SIZE, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.drawText(MODEL_TRANSLATION_TEXT, SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.drawText(X_TEXT, SQUARE_SIZE, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING / 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.drawText(Y_TEXT, SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 3 / 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.drawText(Z_TEXT, SQUARE_SIZE, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING * 5 / 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.drawText(MODEL_ROTATION_TEXT, width / 2, SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.drawText(X_TEXT, width / 2, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING / 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.drawText(Y_TEXT, width / 2, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 3 / 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		graphicsHolder.drawText(Z_TEXT, width / 2, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING * 5 / 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}

	private void sendUpdate() {
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateEyeCandyConfig(
				blockPos,
				selectedModelIndices.isEmpty() ? null : Utilities.getElement(loadedObjects, (int) selectedModelIndices.getLong(0)).getId(),
				parse(textFieldTranslateX.getText2()),
				parse(textFieldTranslateY.getText2()),
				parse(textFieldTranslateZ.getText2()),
				(float) Math.toRadians(parse(textFieldRotateX.getText2())),
				(float) Math.toRadians(parse(textFieldRotateY.getText2())),
				(float) Math.toRadians(parse(textFieldRotateZ.getText2())),
				buttonFullBrightness.isChecked2()
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
