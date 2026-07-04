package org.mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.UMatrixStack;
import gg.essential.universal.UMinecraft;
import gg.essential.universal.vertex.UVertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.Nullable;
import org.mtr.block.BlockEyeCandy;
import org.mtr.block.IBlock;
import org.mtr.client.CustomResourceLoader;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.packet.PacketUpdateEyeCandyConfig;
import org.mtr.registry.UConverters;
import org.mtr.resource.ObjectResource;
import org.mtr.tool.BlockRendererHelper;
import org.mtr.tool.GuiHelper;
import org.mtr.tool.ReleasedDynamicTextureRegistry;
import org.mtr.widget.*;

import java.awt.*;
import java.util.Random;

/**
 * Elementa screen for configuring eye-candy model and transform parameters.
 */
public final class EyeCandyScreen extends WindowBase {

	@Nullable
	private String selectedModelId;

	private final BlockPos blockPos;
	@Nullable
	private final ClientLevel clientWorld;
	private final BlockEyeCandy.EyeCandyBlockEntity blockEntity;

	private final NumberInputComponent translateXInputComponent;
	private final NumberInputComponent translateYInputComponent;
	private final NumberInputComponent translateZInputComponent;
	private final NumberInputComponent rotateXInputComponent;
	private final NumberInputComponent rotateYInputComponent;
	private final NumberInputComponent rotateZInputComponent;
	private final CheckboxComponent fullBrightnessCheckboxComponent;

	private final long renderKey = new Random().nextLong();

	private static final int LEFT_WIDTH = 96;
	private static final double CYLINDER_RADIUS = 1 / 64D;

	public EyeCandyScreen(BlockPos blockPos, BlockEyeCandy.EyeCandyBlockEntity blockEntity) {
		this.blockPos = blockPos;
		this.clientWorld = Minecraft.getInstance().level;
		this.blockEntity = blockEntity;
		this.selectedModelId = blockEntity.getModelId();

		final BackgroundComponent backgroundComponent = new BackgroundComponent(getWindow(), ObjectImmutableList.of(
			new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.BRUSH_TEXTURE.get(), TranslationProvider.BLOCK_MTRSTEAMLOCO_EYE_CANDY.getString())
		));
		final UIContainer leftContainer = (UIContainer) new UIContainer()
			.setChildOf(backgroundComponent.containers[0])
			.setWidth(new CoerceAtMostConstraint(new RelativeConstraint(0.5F), new PixelConstraint(LEFT_WIDTH)))
			.setHeight(new RelativeConstraint());

		new UIWrappedText(TranslationProvider.BLOCK_MTRSTEAMLOCO_EYE_CANDY.getString(), false)
			.setChildOf(leftContainer)
			.setWidth(new RelativeConstraint())
			.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		final ScrollComponent scrollComponent = ((ScrollPanelComponent) new ScrollPanelComponent(true)
			.setChildOf(leftContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)))).contentContainer;

		new PreviewBoxComponent(true, true, true, this::onDrawPreview)
			.setChildOf(backgroundComponent.containers[0])
			.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)))
			.setHeight(new RelativeConstraint());

		final ButtonComponent buttonSelectModel = (ButtonComponent) new ButtonComponent(true)
			.setChildOf(scrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		buttonSelectModel.setText(TranslationProvider.GUI_MTR_SELECT_MODEL.getString());
		buttonSelectModel.onClick(() -> UMinecraft.setCurrentScreenObj(createEyeCandySelectorScreen()));

		GuiHelper.createSpacing(scrollComponent);
		GuiHelper.createLabel(scrollComponent, TranslationProvider.GUI_MTR_MODEL_TRANSLATION.getString());
		translateXInputComponent = createNumberInput(scrollComponent, "X", blockEntity.getTranslateX(), 0x10);
		translateYInputComponent = createNumberInput(scrollComponent, "Y", blockEntity.getTranslateY(), 0x10);
		translateZInputComponent = createNumberInput(scrollComponent, "Z", blockEntity.getTranslateZ(), 0x10);

		GuiHelper.createSpacing(scrollComponent);
		GuiHelper.createLabel(scrollComponent, TranslationProvider.GUI_MTR_MODEL_ROTATION.getString());
		rotateXInputComponent = createNumberInput(scrollComponent, "X", Utilities.round(Math.toDegrees(blockEntity.getRotateX()), 4), 180);
		rotateYInputComponent = createNumberInput(scrollComponent, "Y", Utilities.round(Math.toDegrees(blockEntity.getRotateY()), 4), 180);
		rotateZInputComponent = createNumberInput(scrollComponent, "Z", Utilities.round(Math.toDegrees(blockEntity.getRotateZ()), 4), 180);

		GuiHelper.createSpacing(scrollComponent);
		fullBrightnessCheckboxComponent = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(scrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());
		fullBrightnessCheckboxComponent.setText(TranslationProvider.GUI_MTR_MODEL_FULL_BRIGHTNESS.getString());
		fullBrightnessCheckboxComponent.setChecked(blockEntity.isFullBrightness());
	}

	@Override
	public void onScreenClose() {
		new PacketUpdateEyeCandyConfig(
			blockPos,
			selectedModelId,
			(float) translateXInputComponent.getValue(),
			(float) translateYInputComponent.getValue(),
			(float) translateZInputComponent.getValue(),
			(float) Math.toRadians(rotateXInputComponent.getValue()),
			(float) Math.toRadians(rotateYInputComponent.getValue()),
			(float) Math.toRadians(rotateZInputComponent.getValue()),
			fullBrightnessCheckboxComponent.isChecked()
		).send(Minecraft.getInstance().level);

		super.onScreenClose();
	}

	private void onDrawPreview(PoseStack matrixStack) {
		if (clientWorld == null) {
			return;
		}

		final UMatrixStack uMatrixStack = UConverters.convert(matrixStack);
		ImageComponentBase.drawRectangle(vertexConsumer -> {
			drawCylindersAtVertex(uMatrixStack, vertexConsumer, false, false, false);
			drawCylindersAtVertex(uMatrixStack, vertexConsumer, false, true, true);
			drawCylindersAtVertex(uMatrixStack, vertexConsumer, true, false, true);
			drawCylindersAtVertex(uMatrixStack, vertexConsumer, true, true, false);
		}, false);

		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					if (x != 0 || y != 0 || z != 0) {
						BlockRendererHelper.renderBlock(uMatrixStack, clientWorld.getBlockState(blockPos.offset(x, y, z)), renderKey, x - 0.5, y - 0.5, z - 0.5, 0.5);
					}
				}
			}
		}

		final String modelId = blockEntity.getModelId();
		if (modelId != null) {
			final Direction facing = IBlock.getStatePropertySafe(clientWorld.getBlockState(blockPos), BlockStateProperties.HORIZONTAL_FACING);
			CustomResourceLoader.getObjectById(modelId, objectResource -> objectResource.getOptimizedModel().forEach((renderStage, newOptimizedModels) -> newOptimizedModels.forEach(newOptimizedModel -> {
				// TODO render model preview
			})));
		}
	}

	private EyeCandySelectorScreen createEyeCandySelectorScreen() {
		final ObjectImmutableList<ObjectResource> allObjectResources = CustomResourceLoader.getObjects();
		final EyeCandySelectorScreen eyeCandySelectorScreen = new EyeCandySelectorScreen(objectResource -> selectedModelId = objectResource.isEmpty() ? "" : objectResource.getFirst().getId(), this);
		eyeCandySelectorScreen.setAvailableList(allObjectResources);

		allObjectResources.forEach(objectResource -> {
			if (objectResource.getId().equals(selectedModelId)) {
				eyeCandySelectorScreen.selectData(objectResource);
			}
		});

		return eyeCandySelectorScreen;
	}

	private static NumberInputComponent createNumberInput(UIContainer container, String axis, double value, int bound) {
		final NumberInputComponent numberInputComponent = (NumberInputComponent) new NumberInputComponent(-bound, bound, 0.1, true, null)
			.setChildOf(container)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		numberInputComponent.setPrefix(axis + " ");
		numberInputComponent.setValue(value);
		return numberInputComponent;
	}

	private static void drawCylindersAtVertex(UMatrixStack uMatrixStack, UVertexConsumer vertexConsumer, boolean x, boolean y, boolean z) {
		final double signX = x ? 0.5 : -0.5;
		final double signY = y ? 0.5 : -0.5;
		final double signZ = z ? 0.5 : -0.5;
		ImageComponentBase.drawCylinder(uMatrixStack, vertexConsumer, signX, signY, signZ, CYLINDER_RADIUS, -signX, signY, signZ, CYLINDER_RADIUS, Color.WHITE);
		ImageComponentBase.drawCylinder(uMatrixStack, vertexConsumer, signX, signY, signZ, CYLINDER_RADIUS, signX, -signY, signZ, CYLINDER_RADIUS, Color.WHITE);
		ImageComponentBase.drawCylinder(uMatrixStack, vertexConsumer, signX, signY, signZ, CYLINDER_RADIUS, signX, signY, -signZ, CYLINDER_RADIUS, Color.WHITE);
	}
}
