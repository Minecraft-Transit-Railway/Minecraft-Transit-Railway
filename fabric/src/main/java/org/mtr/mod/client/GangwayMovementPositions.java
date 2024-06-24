package org.mtr.mod.client;

import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.Box;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mod.render.RenderVehicleHelper;
import org.mtr.mod.render.RenderVehicleTransformationHelper;

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
			if (box.getMaxZMapped() > z) {
				xMin = box.getMinXMapped();
				xMinClamped = xMin + RenderVehicleHelper.HALF_PLAYER_WIDTH;
				xMax = box.getMaxXMapped();
				xMaxClamped = xMax - RenderVehicleHelper.HALF_PLAYER_WIDTH;
				y = box.getMaxYMapped();
				z = box.getMaxZMapped();
			}
		} else {
			if (box.getMinZMapped() < z) {
				xMin = box.getMinXMapped();
				xMinClamped = xMin + RenderVehicleHelper.HALF_PLAYER_WIDTH;
				xMax = box.getMaxXMapped();
				xMaxClamped = xMax - RenderVehicleHelper.HALF_PLAYER_WIDTH;
				y = box.getMaxYMapped();
				z = box.getMinZMapped();
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

	public Vector3d getMinWorldPosition() {
		return renderVehicleTransformationHelper.transformForwards(new Vector3d(xMinClamped, y, z), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
	}

	public Vector3d getMaxWorldPosition() {
		return renderVehicleTransformationHelper.transformForwards(new Vector3d(xMaxClamped, y, z), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
	}
}
