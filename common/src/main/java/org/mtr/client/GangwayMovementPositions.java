package org.mtr.client;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.mtr.render.PositionAndRotation;
import org.mtr.render.RenderVehicleHelper;

public class GangwayMovementPositions {

	private double xMin;
	private double xMax;
	private double xMinClamped;
	private double xMaxClamped;
	private double y;
	private double z;
	private final PositionAndRotation positionAndRotation;
	private final boolean getMax;

	public GangwayMovementPositions(PositionAndRotation positionAndRotation, boolean getMax) {
		this.positionAndRotation = positionAndRotation;
		this.getMax = getMax;
	}

	public void check(Box box) {
		if (getMax) {
			if (box.maxZ > z) {
				xMin = box.minX;
				xMinClamped = xMin + RenderVehicleHelper.HALF_PLAYER_WIDTH;
				xMax = box.maxX;
				xMaxClamped = xMax - RenderVehicleHelper.HALF_PLAYER_WIDTH;
				y = box.maxY;
				z = box.maxZ;
			}
		} else {
			if (box.minZ < z) {
				xMin = box.minX;
				xMinClamped = xMin + RenderVehicleHelper.HALF_PLAYER_WIDTH;
				xMax = box.maxX;
				xMaxClamped = xMax - RenderVehicleHelper.HALF_PLAYER_WIDTH;
				y = box.maxY;
				z = box.minZ;
			}
		}
	}

	public Box getBox() {
		return new Box(xMin, y, z, xMax, y, z + (getMax ? 1 : -1) * RenderVehicleHelper.HALF_PLAYER_WIDTH);
	}

	public double getPercentageX(double x) {
		return (Math.clamp(x, xMinClamped, xMaxClamped) - xMinClamped) / (xMaxClamped - xMinClamped);
	}

	public double getPercentageZ(double z) {
		if (getMax) {
			return Math.clamp(z - this.z, 0, 1);
		} else {
			return Math.clamp(z - this.z, -1, 0) + 1;
		}
	}

	public double getX(double percentageX) {
		return (xMaxClamped - xMinClamped) * percentageX + xMinClamped;
	}

	public double getZ() {
		return z;
	}

	public Vec3d getMinWorldPosition() {
		return positionAndRotation.transformForwards(new Vec3d(xMinClamped, y, z), Vec3d::rotateX, Vec3d::rotateY, Vec3d::rotateZ, Vec3d::add);
	}

	public Vec3d getMaxWorldPosition() {
		return positionAndRotation.transformForwards(new Vec3d(xMaxClamped, y, z), Vec3d::rotateX, Vec3d::rotateY, Vec3d::rotateZ, Vec3d::add);
	}
}
