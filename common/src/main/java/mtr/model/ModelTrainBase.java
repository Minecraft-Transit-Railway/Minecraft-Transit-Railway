package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.MTRClient;
import mtr.client.DoorAnimationType;
import mtr.client.ScrollingText;
import mtr.data.*;
import mtr.mappings.ModelMapper;
import mtr.render.MoreRenderLayers;
import mtr.render.RenderTrains;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class ModelTrainBase extends EntityModel<Entity> implements IGui {

	public final DoorAnimationType doorAnimationType;
	public final boolean renderDoorOverlay;

	private final List<ScrollingText> tempScrollingTexts = new ArrayList<>();

	public ModelTrainBase(DoorAnimationType doorAnimationType, boolean renderDoorOverlay) {
		this.doorAnimationType = doorAnimationType;
		this.renderDoorOverlay = renderDoorOverlay;
	}

	@Override
	public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
	}

	public final void render(PoseStack matrices, MultiBufferSource vertexConsumers, NameColorDataBase data, ResourceLocation texture, int light, float doorLeftValue, float doorRightValue, boolean opening, int currentCar, int trainCars, boolean head1IsFront, boolean lightsOn, boolean isTranslucent, boolean renderDetails, boolean atPlatform) {
		final float doorLeftX = DoorAnimationType.getDoorAnimationX(doorAnimationType, doorLeftValue);
		final float doorRightX = DoorAnimationType.getDoorAnimationX(doorAnimationType, doorRightValue);
		final float doorLeftZ = DoorAnimationType.getDoorAnimationZ(doorAnimationType, getDoorMax(), getDoorDuration(), doorLeftValue, opening);
		final float doorRightZ = DoorAnimationType.getDoorAnimationZ(doorAnimationType, getDoorMax(), getDoorDuration(), doorRightValue, opening);

		final int lightOnInteriorLevel = lightsOn ? MAX_LIGHT_INTERIOR : light;
		final int lightOnGlowingLevel = lightsOn ? MAX_LIGHT_GLOWING : light;

		matrices.pushPose();
		baseTransform(matrices);

		if (isTranslucent) {
			if (renderDetails) {
				final RenderType renderLayerInteriorTranslucent = lightsOn ? MoreRenderLayers.getInteriorTranslucent(texture) : MoreRenderLayers.getExteriorTranslucent(texture);
				render(matrices, vertexConsumers.getBuffer(renderLayerInteriorTranslucent), RenderStage.INTERIOR_TRANSLUCENT, lightOnInteriorLevel, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, true);
			}
		} else {
			final RenderType renderLayerLight = lightsOn ? MoreRenderLayers.getLight(texture, false) : MoreRenderLayers.getExterior(texture);
			final RenderType renderLayerInterior = lightsOn ? MoreRenderLayers.getInterior(texture) : MoreRenderLayers.getExterior(texture);
			render(matrices, vertexConsumers.getBuffer(renderLayerLight), RenderStage.LIGHTS, lightOnGlowingLevel, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, renderDetails);
			render(matrices, vertexConsumers.getBuffer(renderLayerInterior), RenderStage.INTERIOR, lightOnInteriorLevel, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, renderDetails);

			if (renderDetails) {
				renderExtraDetails(matrices, vertexConsumers, light, lightOnInteriorLevel, lightsOn, doorLeftX, doorRightX, doorLeftZ, doorRightZ);
			}

			render(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(texture)), RenderStage.EXTERIOR, light, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, renderDetails);
			render(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(texture, true)), RenderStage.ALWAYS_ON_LIGHTS, MAX_LIGHT_GLOWING, doorLeftX, doorRightX, doorLeftZ, doorRightZ, currentCar, trainCars, head1IsFront, renderDetails);

			if (renderDetails) {
				final TrainClient train = data instanceof TrainClient ? (TrainClient) data : null;
				final MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
				final Route thisRoute = train == null ? null : train.getThisRoute();
				final Route nextRoute = train == null ? null : train.getNextRoute();
				final Station thisStation = train == null ? null : train.getThisStation();
				final Station nextStation = train == null ? null : train.getNextStation();
				final Station lastStation = train == null ? null : train.getLastStation();
				renderTextDisplays(matrices, vertexConsumers, Minecraft.getInstance().font, immediate, thisRoute, nextRoute, thisStation, nextStation, lastStation, thisRoute == null ? null : thisRoute.getDestination(train.getCurrentStationIndex()), currentCar, trainCars, atPlatform, train == null ? tempScrollingTexts : train.scrollingTexts);
				immediate.endBatch();
			}
		}

		matrices.popPose();
	}

	protected void renderExtraDetails(PoseStack matrices, MultiBufferSource vertexConsumers, int light, int lightOnInteriorLevel, boolean lightsOn, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ) {
	}

	protected void renderTextDisplays(PoseStack matrices, MultiBufferSource vertexConsumers, Font font, MultiBufferSource.BufferSource immediate, Route thisRoute, Route nextRoute, Station thisStation, Station nextStation, Station lastStation, String customDestination, int car, int totalCars, boolean atPlatform, List<ScrollingText> scrollingTexts) {
	}

	protected float getDoorDuration() {
		return 0.5F;
	}

	protected void baseTransform(PoseStack matrices) {
	}

	protected abstract void render(PoseStack matrices, VertexConsumer vertices, RenderStage renderStage, int light, float doorLeftX, float doorRightX, float doorLeftZ, float doorRightZ, int currentCar, int trainCars, boolean head1IsFront, boolean renderDetails);

	protected abstract int getDoorMax();

	protected String getDestinationString(Station station, String customDestination, ModelSimpleTrainBase.TextSpacingType textSpacingType, boolean toUpperCase) {
		final String text = customDestination == null ? station == null ? defaultDestinationString() : station.name : customDestination;
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

	protected static void setRotationAngle(ModelMapper bone, float x, float y, float z) {
		bone.setRotationAngle(x, y, z);
	}

	protected static void renderMirror(ModelMapper bone, PoseStack matrices, VertexConsumer vertices, int light, float position) {
		renderOnce(bone, matrices, vertices, light, position);
		renderOnceFlipped(bone, matrices, vertices, light, position);
	}

	protected static void renderOnce(ModelMapper bone, PoseStack matrices, VertexConsumer vertices, int light, float position) {
		bone.render(matrices, vertices, 0, position, 0, light, OverlayTexture.NO_OVERLAY);
	}

	protected static void renderOnce(ModelMapper bone, PoseStack matrices, VertexConsumer vertices, int light, float positionX, float positionZ) {
		bone.render(matrices, vertices, positionX, positionZ, 0, light, OverlayTexture.NO_OVERLAY);
	}

	protected static void renderOnce(ModelMapper bone, PoseStack matrices, VertexConsumer vertices, int light, float positionX, float positionY, float positionZ) {
		bone.render(matrices, vertices, positionX, positionY, positionZ, 0, light, OverlayTexture.NO_OVERLAY);
	}

	protected static void renderOnceFlipped(ModelMapper bone, PoseStack matrices, VertexConsumer vertices, int light, float position) {
		bone.render(matrices, vertices, 0, position, (float) Math.PI, light, OverlayTexture.NO_OVERLAY);
	}

	protected static void renderOnceFlipped(ModelMapper bone, PoseStack matrices, VertexConsumer vertices, int light, float positionX, float positionZ) {
		bone.render(matrices, vertices, -positionX, positionZ, (float) Math.PI, light, OverlayTexture.NO_OVERLAY);
	}

	protected static void renderOnceFlipped(ModelMapper bone, PoseStack matrices, VertexConsumer vertices, int light, float positionX, float positionY, float positionZ) {
		bone.render(matrices, vertices, -positionX, positionY, positionZ, (float) Math.PI, light, OverlayTexture.NO_OVERLAY);
	}

	protected static boolean isIndex(int index, int value, int[] array) {
		final int finalIndex = index < 0 ? array.length + index : index;
		return finalIndex < array.length && finalIndex >= 0 && array[finalIndex] == value;
	}

	protected static String getAlternatingString(String text) {
		final String[] textSplit = text.split("\\|");
		return textSplit[((int) Math.floor(MTRClient.getGameTick() / 30)) % textSplit.length];
	}

	protected static String getHongKongNextStationString(Station thisStation, Station nextStation, boolean atPlatform, boolean isKcr) {
		if (atPlatform && thisStation != null) {
			return thisStation.name;
		} else if (!atPlatform && nextStation != null) {
			return IGui.insertTranslation(isKcr ? "gui.mtr.next_station_cjk" : "gui.mtr.next_station_announcement_cjk", isKcr ? "gui.mtr.next_station" : "gui.mtr.next_station_announcement", 1, IGui.textOrUntitled(nextStation.name));
		} else {
			return "";
		}
	}

	protected static String getLondonNextStationString(Route thisRoute, Route nextRoute, Station thisStation, Station nextStation, Station lastStation, String destinationString, boolean atPlatform) {
		final Station station = atPlatform ? thisStation : nextStation;
		if (station == null || thisRoute == null) {
			return "";
		} else {
			final List<String> messages = new ArrayList<>();
			final boolean isTerminating = lastStation != null && station.id == lastStation.id && nextRoute == null;

			if (!isTerminating) {
				messages.add(IGui.insertTranslation("gui.mtr.london_train_route_announcement_cjk", "gui.mtr.london_train_route_announcement", 2, IGui.textOrUntitled(thisRoute.name), IGui.textOrUntitled(destinationString)));
			}

			if (atPlatform) {
				messages.add(IGui.insertTranslation("gui.mtr.london_train_this_station_announcement_cjk", "gui.mtr.london_train_this_station_announcement", 1, IGui.textOrUntitled(station.name)));
			} else {
				messages.add(IGui.insertTranslation("gui.mtr.london_train_next_station_announcement_cjk", "gui.mtr.london_train_next_station_announcement", 1, IGui.textOrUntitled(station.name)));
			}

			final String mergedInterchangeRoutes = RenderTrains.getInterchangeRouteNames(station, thisRoute, nextRoute);
			if (!mergedInterchangeRoutes.isEmpty()) {
				messages.add(IGui.insertTranslation("gui.mtr.london_train_interchange_announcement_cjk", "gui.mtr.london_train_interchange_announcement", 1, mergedInterchangeRoutes));
			}

			if (isTerminating) {
				messages.add(IGui.insertTranslation("gui.mtr.london_train_terminating_announcement_cjk", "gui.mtr.london_train_terminating_announcement", 1, IGui.textOrUntitled(station.name)));
			}

			return IGui.formatStationName(IGui.mergeStations(messages, "", " "));
		}
	}

	public enum RenderStage {LIGHTS, ALWAYS_ON_LIGHTS, INTERIOR, INTERIOR_TRANSLUCENT, EXTERIOR}

	protected enum TextSpacingType {NORMAL, SPACE_CJK, SPACE_CJK_FLIPPED, SPACE_CJK_LARGE, MLR_SPACING}
}
