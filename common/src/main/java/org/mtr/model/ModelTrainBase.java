package org.mtr.mod.model;

import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.core.data.NameColorDataBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.EntityAbstractMapping;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.OverlayTexture;
import org.mtr.mapping.mapper.EntityModelExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.DoorAnimationType;
import org.mtr.mod.client.ScrollingText;
import org.mtr.mod.data.IGui;
import org.mtr.mod.data.VehicleExtension;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;
import org.mtr.mod.resource.RenderStage;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class ModelTrainBase extends EntityModelExtension<EntityAbstractMapping> implements IGui {

	public final DoorAnimationType doorAnimationType;
	public final boolean renderDoorOverlay;

	private final List<ScrollingText> tempScrollingTexts = new ArrayList<>();

	public ModelTrainBase(int textureWidth, int textureHeight, DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		super(textureWidth, textureHeight);
		this.doorAnimationType = doorAnimationType;
		this.renderDoorOverlay = renderDoorOverlay;
	}

	@Override
	public void setAngles2(EntityAbstractMapping entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void render(GraphicsHolder graphicsHolder, int light, int overlay, float red, float green, float blue, float alpha) {
	}

	public final void render(StoredMatrixTransformations storedMatrixTransformations, @Nullable NameColorDataBase data, Identifier texture, int light, float doorLeftValue, float doorRightValue, boolean opening, int currentCar, int trainCars, boolean head1IsFront, boolean lightsOn, boolean isTranslucent, boolean renderDetails, boolean atPlatform) {
		final float doorLeftX = DoorAnimationType.getDoorAnimationX(doorAnimationType, doorLeftValue);
		final float doorRightX = DoorAnimationType.getDoorAnimationX(doorAnimationType, doorRightValue);
		final float doorLeftZ = DoorAnimationType.getDoorAnimationZ(doorAnimationType, getDoorMax(), getDoorDuration(), doorLeftValue, opening);
		final float doorRightZ = DoorAnimationType.getDoorAnimationZ(doorAnimationType, getDoorMax(), getDoorDuration(), doorRightValue, opening);

		final int lightOnInteriorLevel = lightsOn ? MAX_LIGHT_INTERIOR : light;
		final int lightOnGlowingLevel = lightsOn ? GraphicsHolder.getDefaultLight() : light;

		final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
		storedMatrixTransformationsNew.add(this::baseTransform);

		if (isTranslucent) {
			if (renderDetails) {
				MainRenderer.scheduleRender(texture, false, lightsOn ? QueuedRenderLayer.INTERIOR_TRANSLUCENT : QueuedRenderLayer.EXTERIOR_TRANSLUCENT, (graphicsHolder, offset) -> {
					storedMatrixTransformationsNew.transform(graphicsHolder, offset);
					render(graphicsHolder, RenderStage.INTERIOR_TRANSLUCENT, lightOnInteriorLevel, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, true);
					graphicsHolder.pop();
				});
			}
		} else {
			MainRenderer.scheduleRender(texture, false, lightsOn ? QueuedRenderLayer.LIGHT : QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				storedMatrixTransformationsNew.transform(graphicsHolder, offset);
				render(graphicsHolder, RenderStage.LIGHT, lightOnGlowingLevel, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, renderDetails);
				graphicsHolder.pop();
			});

			MainRenderer.scheduleRender(texture, false, lightsOn ? QueuedRenderLayer.INTERIOR : QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				storedMatrixTransformationsNew.transform(graphicsHolder, offset);
				render(graphicsHolder, RenderStage.INTERIOR, lightOnInteriorLevel, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, renderDetails);
				graphicsHolder.pop();
			});

			if (renderDetails) {
				renderExtraDetails(storedMatrixTransformationsNew, light, lightOnInteriorLevel, lightsOn, doorLeftX, doorRightX, doorLeftZ, doorRightZ);
			}

			MainRenderer.scheduleRender(texture, false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				storedMatrixTransformationsNew.transform(graphicsHolder, offset);
				render(graphicsHolder, RenderStage.EXTERIOR, light, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, renderDetails);
				graphicsHolder.pop();
			});

			MainRenderer.scheduleRender(texture, false, QueuedRenderLayer.LIGHT_TRANSLUCENT, (graphicsHolder, offset) -> {
				storedMatrixTransformationsNew.transform(graphicsHolder, offset);
				render(graphicsHolder, RenderStage.ALWAYS_ON_LIGHT, GraphicsHolder.getDefaultLight(), doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, renderDetails);
				graphicsHolder.pop();
			});

			if (renderDetails) {
				final VehicleExtension vehicle = data instanceof VehicleExtension ? (VehicleExtension) data : null;
				if (vehicle != null) {
					final String[] routeSplit = vehicle.vehicleExtraData.getThisRouteName().split("\\|\\|");
					renderTextDisplays(
							storedMatrixTransformationsNew,
							vehicle.vehicleExtraData.getThisRouteColor(),
							routeSplit[0],
							routeSplit.length > 1 ? routeSplit[1] : "",
							vehicle.vehicleExtraData.getThisStationName(),
							vehicle.vehicleExtraData.getThisRouteDestination(),
							vehicle.vehicleExtraData.getNextStationName(),
							vehicle.vehicleExtraData::iterateInterchanges,
							currentCar,
							trainCars,
							atPlatform,
							vehicle.vehicleExtraData.getIsTerminating(),
							vehicle.persistentVehicleData.getScrollingText(currentCar)
					);
				}
			}
		}
	}

	protected void renderExtraDetails(StoredMatrixTransformations storedMatrixTransformations, int light, int lightOnInteriorLevel, boolean lightsOn, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
	}

	protected void renderTextDisplays(StoredMatrixTransformations storedMatrixTransformations, int thisRouteColor, String thisRouteName, String thisRouteNumber, String thisStationName, String thisRouteDestination, String nextStationName, Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges, int car, int totalCars, boolean atPlatform, boolean isTerminating, ObjectArrayList<ScrollingText> scrollingTexts) {
	}

	protected float getDoorDuration() {
		return 0.5F;
	}

	protected void baseTransform(GraphicsHolder graphicsHolder) {
	}

	protected abstract void render(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, int currentCar, int trainCars, boolean head1IsFront, boolean renderDetails);

	protected abstract int getDoorMax();

	protected static void setRotationAngle(ModelPartExtension bone, float x, float y, float z) {
		bone.setRotation(x, y, z);
	}

	protected static void renderMirror(ModelPartExtension bone, GraphicsHolder graphicsHolder, int light, float position) {
		renderOnce(bone, graphicsHolder, light, position);
		renderOnceFlipped(bone, graphicsHolder, light, position);
	}

	protected static void renderOnce(ModelPartExtension bone, GraphicsHolder graphicsHolder, int light, float position) {
		bone.render(graphicsHolder, 0, position, 0, light, OverlayTexture.getDefaultUvMapped());
	}

	protected static void renderOnce(ModelPartExtension bone, GraphicsHolder graphicsHolder, int light, float positionX, float positionZ) {
		bone.render(graphicsHolder, positionX, positionZ, 0, light, OverlayTexture.getDefaultUvMapped());
	}

	protected static void renderOnce(ModelPartExtension bone, GraphicsHolder graphicsHolder, int light, float positionX, float positionY, float positionZ) {
		bone.render(graphicsHolder, positionX, positionY, positionZ, 0, light, OverlayTexture.getDefaultUvMapped());
	}

	protected static void renderOnceFlipped(ModelPartExtension bone, GraphicsHolder graphicsHolder, int light, float position) {
		bone.render(graphicsHolder, 0, position, (float) Math.PI, light, OverlayTexture.getDefaultUvMapped());
	}

	protected static void renderOnceFlipped(ModelPartExtension bone, GraphicsHolder graphicsHolder, int light, float positionX, float positionZ) {
		bone.render(graphicsHolder, -positionX, positionZ, (float) Math.PI, light, OverlayTexture.getDefaultUvMapped());
	}

	protected static void renderOnceFlipped(ModelPartExtension bone, GraphicsHolder graphicsHolder, int light, float positionX, float positionY, float positionZ) {
		bone.render(graphicsHolder, -positionX, positionY, positionZ, (float) Math.PI, light, OverlayTexture.getDefaultUvMapped());
	}

	protected static boolean isIndex(int index, int value, int[] array) {
		final int finalIndex = index < 0 ? array.length + index : index;
		return finalIndex < array.length && finalIndex >= 0 && array[finalIndex] == value;
	}

	protected static String getAlternatingString(String text) {
		final String[] textSplit = text.split("\\|");
		return textSplit[((int) Math.floor(InitClient.getGameTick() / 30)) % textSplit.length];
	}

	protected enum TextSpacingType {NORMAL, SPACE_CJK, SPACE_CJK_FLIPPED, SPACE_CJK_LARGE, MLR_SPACING}
}
