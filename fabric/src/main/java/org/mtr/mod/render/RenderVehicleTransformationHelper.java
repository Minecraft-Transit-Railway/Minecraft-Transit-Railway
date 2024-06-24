package org.mtr.mod.render;

import org.mtr.core.tool.Utilities;
import org.mtr.core.tool.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.EntityHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;

import java.util.function.Consumer;

public class RenderVehicleTransformationHelper {

	public final int light;
	public final Vector pivotPosition;
	public final double yaw;
	public final double pitch;
	private final boolean renderWithRespectToPlayerPosition;
	private final boolean renderWithRespectToPlayerRotation;
	private final double ridingYawDifference;
	private final double ridingPitchDifference;

	public RenderVehicleTransformationHelper(ObjectArrayList<ObjectObjectImmutablePair<Vector, Vector>> bogiePositionsList, double bogie1Position, double bogie2Position, double length, boolean renderWithRespectToPlayerPosition, boolean renderWithRespectToPlayerRotation, double ridingYawDifference, double ridingPitchDifference) {
		if (bogiePositionsList.size() == 1 || bogiePositionsList.size() == 2) {
			final Vector bogiesMidpoint;

			if (bogiePositionsList.size() == 1) {
				final ObjectObjectImmutablePair<Vector, Vector> bogiePositions = bogiePositionsList.get(0);
				bogiesMidpoint = Vector.getAverage(bogiePositions.left(), bogiePositions.right());
				yaw = getYaw(bogiePositions.left(), bogiePositions.right());
				pitch = getPitch(bogiePositions.left(), bogiePositions.right());
			} else {
				final Vector average1 = Vector.getAverage(bogiePositionsList.get(0).left(), bogiePositionsList.get(0).right());
				final Vector average2 = Vector.getAverage(bogiePositionsList.get(1).left(), bogiePositionsList.get(1).right());
				bogiesMidpoint = Vector.getAverage(average1, average2);
				yaw = getYaw(average1, average2);
				pitch = getPitch(average1, average2);
			}

			final double pivotOffset = Utilities.getAverage(bogie1Position, bogie2Position);
			final double halfLength = length / 2;
			final double pivotOffset1 = -pivotOffset - halfLength;
			final double pivotOffset2 = -pivotOffset + halfLength;
			final Vector rotationalVector = new Vector(0, 0, 1).rotateX(pitch).rotateY(yaw);
			pivotPosition = Vector.getAverage(rotationalVector.multiply(pivotOffset1, pivotOffset1, pivotOffset1).add(bogiesMidpoint), rotationalVector.multiply(pivotOffset2, pivotOffset2, pivotOffset2).add(bogiesMidpoint));
		} else {
			pivotPosition = new Vector(0, 0, 0);
			yaw = 0;
			pitch = 0;
		}

		light = getLight(pivotPosition);
		this.renderWithRespectToPlayerPosition = renderWithRespectToPlayerPosition;
		this.renderWithRespectToPlayerRotation = renderWithRespectToPlayerRotation;
		this.ridingYawDifference = ridingYawDifference;
		this.ridingPitchDifference = ridingPitchDifference;
	}

	public RenderVehicleTransformationHelper(ObjectObjectImmutablePair<Vector, Vector> bogiePositions, Vector averageAbsoluteBogiePosition, RenderVehicleTransformationHelper renderVehicleTransformationHelper) {
		pivotPosition = averageAbsoluteBogiePosition;
		yaw = getYaw(bogiePositions.left(), bogiePositions.right());
		pitch = getPitch(bogiePositions.left(), bogiePositions.right());
		light = getLight(pivotPosition);
		renderWithRespectToPlayerPosition = renderVehicleTransformationHelper.renderWithRespectToPlayerPosition;
		renderWithRespectToPlayerRotation = renderVehicleTransformationHelper.renderWithRespectToPlayerRotation;
		ridingYawDifference = renderVehicleTransformationHelper.ridingYawDifference;
		ridingPitchDifference = renderVehicleTransformationHelper.ridingPitchDifference;
	}

	public <T> T transformForwards(T initialValue, Rotate<T> rotateX, Rotate<T> rotateY, Translate<T> translate) {
		return translate.apply(rotateY.apply(rotateX.apply(initialValue, (float) pitch), (float) yaw), pivotPosition.x, pivotPosition.y, pivotPosition.z);
	}

	public <T> T transformBackwards(T initialValue, Rotate<T> rotateX, Rotate<T> rotateY, Translate<T> translate) {
		return rotateX.apply(rotateY.apply(translate.apply(initialValue, -pivotPosition.x, -pivotPosition.y, -pivotPosition.z), (float) -yaw), (float) -pitch);
	}

	public StoredMatrixTransformations getStoredMatrixTransformations() {
		final StoredMatrixTransformations storedMatrixTransformations = renderWithRespectToPlayerPosition ? new StoredMatrixTransformations() : new StoredMatrixTransformations(pivotPosition.x, pivotPosition.y, pivotPosition.z);
		storedMatrixTransformations.add(this::transformGraphicsHolder);
		return storedMatrixTransformations;
	}

	public void render(GraphicsHolder graphicsHolder, Vector3d offset, Consumer<Vector3d> newConsumer) {
		graphicsHolder.push();
		transformGraphicsHolder(graphicsHolder);
		newConsumer.accept(renderWithRespectToPlayerPosition ? Vector3d.getZeroMapped() : offset);
		graphicsHolder.pop();
	}

	private void transformGraphicsHolder(GraphicsHolder graphicsHolder) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final Camera camera = minecraftClient.getGameRendererMapped().getCamera();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
		if (clientPlayerEntity == null) {
			return;
		}

		if (renderWithRespectToPlayerRotation) {
			graphicsHolder.rotateYDegrees(-camera.getYaw());
			if (camera.isThirdPerson() && Math.abs(EntityHelper.getYaw(new Entity(clientPlayerEntity.data)) - camera.getYaw()) > 90) {
				graphicsHolder.rotateYDegrees(180);
			}
		}

		graphicsHolder.rotateYRadians((float) ridingYawDifference);
	}

	private static int getLight(Vector pivotPosition) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			return 0;
		} else {
			final BlockPos blockPos = Init.newBlockPos(pivotPosition.x, pivotPosition.y + 1, pivotPosition.z);
			return LightmapTextureManager.pack(clientWorld.getLightLevel(LightType.getBlockMapped(), blockPos), clientWorld.getLightLevel(LightType.getSkyMapped(), blockPos));
		}
	}

	private static double getYaw(Vector position1, Vector position2) {
		final double x1 = position1.x;
		final double z1 = position1.z;
		final double x2 = position2.x;
		final double z2 = position2.z;
		return Math.atan2(x2 - x1, z2 - z1);
	}

	private static double getPitch(Vector position1, Vector position2) {
		final double x1 = position1.x;
		final double y1 = position1.y;
		final double z1 = position1.z;
		final double x2 = position2.x;
		final double y2 = position2.y;
		final double z2 = position2.z;
		return Math.atan2(y2 - y1, Math.sqrt((x2 - x1) * (x2 - x1) + (z2 - z1) * (z2 - z1)));
	}

	@FunctionalInterface
	public interface Rotate<T> {
		T apply(T input, float amount);
	}

	@FunctionalInterface
	public interface Translate<T> {
		T apply(T input, double amountX, double amountY, double amountZ);
	}
}
