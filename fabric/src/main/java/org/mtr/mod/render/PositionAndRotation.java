package org.mtr.mod.render;

import org.mtr.core.data.VehicleCar;
import org.mtr.core.tool.Utilities;
import org.mtr.core.tool.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mod.Init;

public final class PositionAndRotation {

	public final Vector position;
	public final double yaw;
	public final double pitch;
	public final int light;

	public PositionAndRotation(Vector position, double yaw, double pitch) {
		this.position = position;
		this.yaw = yaw;
		this.pitch = pitch;
		light = getLight(position);
	}

	public PositionAndRotation(Vector position1, Vector position2, boolean hasPitch) {
		this(Vector.getAverage(position1, position2), getYaw(position1, position2), hasPitch ? getPitch(position1, position2) : 0);
	}

	public PositionAndRotation(ObjectArrayList<PositionAndRotation> bogiePositions, VehicleCar vehicleCar, boolean hasPitch) {
		if (bogiePositions.size() == 1 || bogiePositions.size() == 2) {
			final Vector bogiesMidpoint;

			if (bogiePositions.size() == 1) {
				final PositionAndRotation bogiePositionAndRotation = bogiePositions.get(0);
				bogiesMidpoint = bogiePositionAndRotation.position;
				yaw = bogiePositionAndRotation.yaw;
				pitch = hasPitch ? bogiePositionAndRotation.pitch : 0;
			} else {
				final PositionAndRotation bogiePositionAndRotation1 = bogiePositions.get(0);
				final PositionAndRotation bogiePositionAndRotation2 = bogiePositions.get(1);
				final Vector average1 = bogiePositionAndRotation1.position;
				final Vector average2 = bogiePositionAndRotation2.position;
				bogiesMidpoint = Vector.getAverage(average1, average2);
				yaw = getYaw(average1, average2);
				pitch = hasPitch ? getPitch(average1, average2) : 0;
			}

			final double pivotOffset = Utilities.getAverage(vehicleCar.getBogie1Position(), vehicleCar.getBogie2Position());
			final double halfLength = vehicleCar.getLength() / 2;
			final double pivotOffset1 = -pivotOffset - halfLength;
			final double pivotOffset2 = -pivotOffset + halfLength;
			final Vector rotationalVector = new Vector(0, 0, 1).rotateX(pitch).rotateY(yaw);
			position = Vector.getAverage(rotationalVector.multiply(pivotOffset1, pivotOffset1, pivotOffset1).add(bogiesMidpoint), rotationalVector.multiply(pivotOffset2, pivotOffset2, pivotOffset2).add(bogiesMidpoint));
		} else {
			position = new Vector(0, 0, 0);
			yaw = 0;
			pitch = 0;
		}

		light = getLight(position);
	}

	public <T> T transformForwards(T initialValue, Rotate<T> rotateX, Rotate<T> rotateY, Translate<T> translate) {
		return translate.apply(rotateY.apply(rotateX.apply(initialValue, (float) pitch), (float) yaw), position.x, position.y, position.z);
	}

	public <T> T transformBackwards(T initialValue, Rotate<T> rotateX, Rotate<T> rotateY, Translate<T> translate) {
		return rotateX.apply(rotateY.apply(translate.apply(initialValue, -position.x, -position.y, -position.z), (float) -yaw), (float) -pitch);
	}

	private static int getLight(Vector position) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			return 0;
		} else {
			final BlockPos blockPos = Init.newBlockPos(position.x, position.y + 1, position.z);
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
