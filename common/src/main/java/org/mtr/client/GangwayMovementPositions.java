package org.mtr.client;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.mtr.core.tool.Utilities;
import org.mtr.render.RenderVehicleHelper;
import org.mtr.render.RenderVehicleTransformationHelper;

public class GangwayMovementPositions {

	private double xMin;
	private double xMax;
	private double xMinClamped;
	private double xMaxClamped;
	private double y;
	private double z;
	private final RenderVehicleTransformationHelper renderVehicleTransformationHelper;
	private final boolean getMax;

	public GangwayMovementPositions(RenderVehicleTransformationHelper renderVehicleTransformationHelper, boolean getMax) {
		this.renderVehicleTransformationHelper = renderVehicleTransformationHelper;
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
		return (Utilities.clamp(x, xMinClamped, xMaxClamped) - xMinClamped) / (xMaxClamped - xMinClamped);
	}

	public double getPercentageZ(double z) {
		if (getMax) {
			return Utilities.clamp(z - this.z, 0, 1);
		} else {
			return Utilities.clamp(z - this.z, -1, 0) + 1;
		}
	}

	public double getX(double percentageX) {
		return (xMaxClamped - xMinClamped) * percentageX + xMinClamped;
	}

	public double getZ() {
		return z;
	}

	public Vec3d getMinWorldPosition() {
		return renderVehicleTransformationHelper.transformForwards(new Vec3d(xMinClamped, y, z), Vec3d::rotateX, Vec3d::rotateY, Vec3d::add);
	}

	public Vec3d getMaxWorldPosition() {
		return renderVehicleTransformationHelper.transformForwards(new Vec3d(xMaxClamped, y, z), Vec3d::rotateX, Vec3d::rotateY, Vec3d::add);
	}
}
