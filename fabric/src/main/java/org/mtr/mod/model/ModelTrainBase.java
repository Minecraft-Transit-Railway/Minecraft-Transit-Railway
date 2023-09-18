package org.mtr.mod.model;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.core.data.InterchangeColorsForStationName;
import org.mtr.core.data.NameColorDataBase;
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
import org.mtr.mod.render.MoreRenderLayers;
import org.mtr.mod.render.RenderTrains;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

	public final void render(GraphicsHolder graphicsHolder, @Nullable NameColorDataBase data, Identifier texture, int light, float doorLeftValue, float doorRightValue, boolean opening, int currentCar, int trainCars, boolean head1IsFront, boolean lightsOn, boolean isTranslucent, boolean renderDetails, boolean atPlatform) {
		final float doorLeftX = DoorAnimationType.getDoorAnimationX(doorAnimationType, doorLeftValue);
		final float doorRightX = DoorAnimationType.getDoorAnimationX(doorAnimationType, doorRightValue);
		final float doorLeftZ = DoorAnimationType.getDoorAnimationZ(doorAnimationType, getDoorMax(), getDoorDuration(), doorLeftValue, opening);
		final float doorRightZ = DoorAnimationType.getDoorAnimationZ(doorAnimationType, getDoorMax(), getDoorDuration(), doorRightValue, opening);

		final int lightOnInteriorLevel = lightsOn ? MAX_LIGHT_INTERIOR : light;
		final int lightOnGlowingLevel = lightsOn ? MAX_LIGHT_GLOWING : light;

		graphicsHolder.push();
		baseTransform(graphicsHolder);

		if (isTranslucent) {
			if (renderDetails) {
				graphicsHolder.createVertexConsumer(lightsOn ? MoreRenderLayers.getInteriorTranslucent(texture) : MoreRenderLayers.getExteriorTranslucent(texture));
				render(graphicsHolder, RenderStage.INTERIOR_TRANSLUCENT, lightOnInteriorLevel, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, true);
			}
		} else {
			graphicsHolder.createVertexConsumer(lightsOn ? MoreRenderLayers.getLight(texture, false) : MoreRenderLayers.getExterior(texture));
			render(graphicsHolder, RenderStage.LIGHTS, lightOnGlowingLevel, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, renderDetails);
			graphicsHolder.createVertexConsumer(lightsOn ? MoreRenderLayers.getInterior(texture) : MoreRenderLayers.getExterior(texture));
			render(graphicsHolder, RenderStage.INTERIOR, lightOnInteriorLevel, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, renderDetails);

			if (renderDetails) {
				renderExtraDetails(graphicsHolder, light, lightOnInteriorLevel, lightsOn, doorLeftX, doorRightX, doorLeftZ, doorRightZ);
			}

			graphicsHolder.createVertexConsumer(MoreRenderLayers.getExterior(texture));
			render(graphicsHolder, RenderStage.EXTERIOR, light, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, renderDetails);
			graphicsHolder.createVertexConsumer(MoreRenderLayers.getLight(texture, true));
			render(graphicsHolder, RenderStage.ALWAYS_ON_LIGHTS, MAX_LIGHT_GLOWING, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, renderDetails);

			if (renderDetails) {
				final VehicleExtension vehicle = data instanceof VehicleExtension ? (VehicleExtension) data : null;
				if (vehicle != null) {
					final String[] routeSplit = vehicle.vehicleExtraData.getThisRouteName().split("\\|\\|");
					renderTextDisplays(
							graphicsHolder,
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
							vehicle.scrollingTexts);
				}
			}
		}

		graphicsHolder.pop();
	}

	protected void renderExtraDetails(GraphicsHolder graphicsHolder, int light, int lightOnInteriorLevel, boolean lightsOn, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
	}

	protected void renderTextDisplays(GraphicsHolder graphicsHolder, int thisRouteColor, String thisRouteName, String thisRouteNumber, String thisStationName, String thisRouteDestination, String nextStationName, Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges, int car, int totalCars, boolean atPlatform, boolean isTerminating, ObjectArrayList<ScrollingText> scrollingTexts) {
	}

	protected float getDoorDuration() {
		return 0.5F;
	}

	protected void baseTransform(GraphicsHolder graphicsHolder) {
	}

	protected abstract void render(GraphicsHolder graphicsHolder, RenderStage renderStage, int light, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, int currentCar, int trainCars, boolean head1IsFront, boolean renderDetails);

	protected abstract int getDoorMax();

	protected String getDestinationString(String text, ModelSimpleTrainBase.TextSpacingType textSpacingType, boolean toUpperCase) {
		final String finalResult;

		if (textSpacingType == ModelSimpleTrainBase.TextSpacingType.NORMAL) {
			finalResult = text;
		} else {
			final String[] textSplit = text.split("\\|");
			final List<String> result = new ArrayList<>();
			boolean hasCjk = false;

			for (final String textPart : textSplit) {
				final boolean isCjk = IGui.isCjk(textPart);
				if (textSpacingType == ModelSimpleTrainBase.TextSpacingType.SPACE_CJK || textSpacingType == ModelSimpleTrainBase.TextSpacingType.SPACE_CJK_FLIPPED) {
					result.add(textSpacingType == ModelSimpleTrainBase.TextSpacingType.SPACE_CJK ? result.size() : 0, isCjk && textPart.length() == 2 ? textPart.charAt(0) + " " + textPart.charAt(1) : textPart);
				} else if (textSpacingType == ModelSimpleTrainBase.TextSpacingType.SPACE_CJK_LARGE) {
					if (isCjk) {
						final StringBuilder cjkResult = new StringBuilder();
						for (int i = 0; i < textPart.length(); i++) {
							cjkResult.append(textPart.charAt(i));
							for (int j = 0; j < (textPart.length() == 2 ? 3 : 1); j++) {
								cjkResult.append("   ");
							}
						}
						result.add(cjkResult.toString().trim());
					} else {
						result.add(textPart);
					}
				} else if (textSpacingType == ModelSimpleTrainBase.TextSpacingType.MLR_SPACING) {
					final StringBuilder stringBuilder;
					if (isCjk) {
						stringBuilder = new StringBuilder(textPart);
						for (int i = textPart.length(); i < 3; i++) {
							stringBuilder.append(" ");
						}
						hasCjk = true;
					} else {
						stringBuilder = new StringBuilder();
						for (int i = textPart.length(); i < 9; i++) {
							stringBuilder.append(" ");
						}
						stringBuilder.append(textPart);
					}
					result.add(stringBuilder.toString());
				}
			}

			if (!hasCjk && textSpacingType == ModelSimpleTrainBase.TextSpacingType.MLR_SPACING) {
				result.add(0, " ");
				result.add(0, " ");
			}

			finalResult = String.join("|", result);
		}

		return toUpperCase ? finalResult.toUpperCase(Locale.ENGLISH) : finalResult;
	}

	protected String defaultDestinationString() {
		return "";
	}

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

	protected static String getHongKongNextStationString(String thisStationName, String nextStationName, boolean atPlatform, boolean isKcr) {
		if (atPlatform) {
			return thisStationName;
		} else {
			return IGui.insertTranslation(isKcr ? "gui.mtr.next_station_cjk" : "gui.mtr.next_station_announcement_cjk", isKcr ? "gui.mtr.next_station" : "gui.mtr.next_station_announcement", 1, IGui.textOrUntitled(nextStationName));
		}
	}

	protected static String getLondonNextStationString(String thisRouteName, String thisStationName, String nextStationName, Consumer<BiConsumer<String, InterchangeColorsForStationName>> getInterchanges, String destinationString, boolean atPlatform, boolean isTerminating) {
		final String stationName = atPlatform ? thisStationName : nextStationName;
		final List<String> messages = new ArrayList<>();

		if (!isTerminating) {
			messages.add(IGui.insertTranslation("gui.mtr.london_train_route_announcement_cjk", "gui.mtr.london_train_route_announcement", 2, IGui.textOrUntitled(thisRouteName), IGui.textOrUntitled(destinationString)));
		}

		if (atPlatform) {
			messages.add(IGui.insertTranslation("gui.mtr.london_train_this_station_announcement_cjk", "gui.mtr.london_train_this_station_announcement", 1, IGui.textOrUntitled(stationName)));
		} else {
			messages.add(IGui.insertTranslation("gui.mtr.london_train_next_station_announcement_cjk", "gui.mtr.london_train_next_station_announcement", 1, IGui.textOrUntitled(stationName)));
		}

		final String mergedInterchangeRoutes = RenderTrains.getInterchangeRouteNames(getInterchanges);
		if (!mergedInterchangeRoutes.isEmpty()) {
			messages.add(IGui.insertTranslation("gui.mtr.london_train_interchange_announcement_cjk", "gui.mtr.london_train_interchange_announcement", 1, mergedInterchangeRoutes));
		}

		if (isTerminating) {
			messages.add(IGui.insertTranslation("gui.mtr.london_train_terminating_announcement_cjk", "gui.mtr.london_train_terminating_announcement", 1, IGui.textOrUntitled(stationName)));
		}

		return IGui.formatStationName(IGui.mergeStations(messages, "", " "));
	}

	public enum RenderStage {LIGHTS, ALWAYS_ON_LIGHTS, INTERIOR, INTERIOR_TRANSLUCENT, EXTERIOR}

	protected enum TextSpacingType {NORMAL, SPACE_CJK, SPACE_CJK_FLIPPED, SPACE_CJK_LARGE, MLR_SPACING}
}
