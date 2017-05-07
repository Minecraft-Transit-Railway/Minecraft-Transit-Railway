package MTR;

import MTR.blocks.BlockRailStraight;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class RailTools {

	public static double[] createArray(World worldIn, int x1, int y1, int z1, int x2, int y2, int z2, int s) {
		double arrayX[] = new double[360];
		double arrayY[] = new double[360];
		double arrayZ[] = new double[360];
		int arrayMax = -1;
		int i1 = 0;
		try {
			i1 = (Integer) worldIn.getBlockState(new BlockPos(x1, y1, z1)).getValue(BlockRailStraight.ROTATION);
		} catch (Exception e) {
		}
		int i2 = 0;
		try {
			i2 = (Integer) worldIn.getBlockState(new BlockPos(x2, y2, z2)).getValue(BlockRailStraight.ROTATION);
		} catch (Exception e) {
		}
		int angle1 = (int) Math.round((-45D * i1 + 270D) % 180);
		int angle2 = (int) Math.round((-45D * i2 + 270D) % 180);
		if (angle1 == angle2) { // either straight line or use two circles
			if (angle1 == 0 && z1 == z2 || angle1 == 90 && x1 == x2 || angle1 == 45 && x2 - x1 == z1 - z2
					|| angle1 == 135 && x2 - x1 == z2 - z1) {
				// if straight line
				arrayX[0] = x1;
				arrayY[0] = y1;
				arrayZ[0] = z1;
				arrayX[1] = x2;
				arrayY[1] = y2;
				arrayZ[1] = z2;
				arrayMax = 1;
			} else if (angle1 == 0 && x1 == x2 || angle1 == 90 && z1 == z2 || angle1 == 45 && x2 - x1 == z2 - z1
					|| angle1 == 135 && x2 - x1 == z1 - z2) {
				// use semicircle

			} else {
				// use two circles
				double result[] = rotateAngle(x1, z1, x2, z2, -angle1);
				double x2d = result[0];
				double z2d = result[1];

				double midX = (x1 + x2d) / 2D;
				double midY = (y1 + y2) / 2D;
				double midZ = (z1 + z2d) / 2D;
				double a = Math.sqrt(Math.pow(midZ - z1, 2) + Math.pow(midX - x1, 2));
				double r = 1, h1 = 0, k1 = 0, h2 = 0, k2 = 0;
				double end1 = 0, end2 = 0;
				boolean reverse = false;

				a = Math.asin(Math.abs(midX - x1) / a);
				a = Math.PI - 2D * a;
				r = Math.abs(midX - x1) / Math.sin(a);
				// radius always positive
				h1 = z1 + (z2d > z1 ? r : -r);
				k1 = x1;
				h2 = z2d + (z1 > z2d ? r : -r);
				k2 = x2d;
				reverse = !(z2d > z1 ^ x2d > x1);
				end1 = z1 > z2d ? Math.PI : 0;
				end2 = z2d > z1 ? Math.PI : 0;

				for (double i = end1; reverse ? i > end1 - a : i < end1 + a; i += Math.PI / (reverse ? -100D : 100D)) {
					arrayMax++;
					arrayZ[arrayMax] = h1 + r * Math.cos(i);
					arrayX[arrayMax] = k1 + r * Math.sin(i);
					arrayY[arrayMax] = y1; // temp
				}
				for (double i = reverse ? end2 - a : end2 + a; reverse ? i < end2
						: i > end2; i += Math.PI / (reverse ? 100D : -100D)) {
					arrayMax++;
					arrayZ[arrayMax] = h2 + r * Math.cos(i);
					arrayX[arrayMax] = k2 + r * Math.sin(i);
					arrayY[arrayMax] = y2; // temp
				}
				arrayMax++;
				arrayX[arrayMax] = x2d;
				arrayY[arrayMax] = y2;
				arrayZ[arrayMax] = z2d;

				if (angle1 != 0)
					for (int i = 0; i <= arrayMax; i++) {
						result = rotateAngle(x1, z1, arrayX[i], arrayZ[i], angle1);
						arrayX[i] = result[0];
						arrayZ[i] = result[1];
					}
			}
		} else { // use a straight line and one circle
			;
			// 90 degree connections
			if (Math.abs(angle2 - angle1) == 90) {
				double x1d = x1, x2d = x2, z1d = z1, z2d = z2;
				boolean rotated = false;
				if (angle1 == 45 || angle1 == 135) {
					double result[] = rotateAngle(0, 0, x1, z1, -45);
					x1d = result[0];
					z1d = result[1];
					result = rotateAngle(0, 0, x2, z2, -45);
					x2d = result[0];
					z2d = result[1];
					angle1 -= 45;
					angle2 -= 45;
					rotated = true;
				}

				double dz = Math.abs(z2d - z1d);
				double dx = Math.abs(x2d - x1d);
				double r, h, k, start;
				boolean reverse, switched = false;
				if (dz > dx) {
					if (angle2 == 0) {
						double tx = x1d;
						double tz = z1d;
						x1d = x2d;
						z1d = z2d;
						x2d = tx;
						z2d = tz;
						switched = true;
					}
					r = dx;
					h = z1d + (z2d > z1d ? r : -r);
					k = x1d;
					reverse = !(z2d > z1d ^ x2d > x1d);
					start = z2d > z1d ? Math.PI : 0;
				} else {
					if (angle2 == 90) {
						double tx = x1d;
						double tz = z1d;
						x1d = x2d;
						z1d = z2d;
						x2d = tx;
						z2d = tz;
					}
					r = dz;
					h = z1d;
					k = x1d + (x2d > x1d ? r : -r);
					reverse = !(z2d > z1d ^ x2d < x1d);
					start = Math.PI / 2D * (x2d > x1d ? 3 : 1);
				}
				for (double i = start; reverse ? i > start - Math.PI / 2D
						: i < start + Math.PI / 2D; i += Math.PI / (reverse ? -100D : 100D)) {
					arrayMax++;
					arrayZ[arrayMax] = h + r * Math.cos(i);
					arrayX[arrayMax] = k + r * Math.sin(i);
					arrayY[arrayMax] = y2; // temp
				}
				arrayMax++;
				arrayX[arrayMax] = x2d;
				arrayY[arrayMax] = y2;
				arrayZ[arrayMax] = z2d;

				if (rotated)
					for (int i = 0; i <= arrayMax; i++) {
						double result[] = rotateAngle(0, 0, arrayX[i], arrayZ[i], 45);
						arrayX[i] = result[0];
						arrayZ[i] = result[1];
					}
				if (switched) {
					double tx[] = arrayX, ty[] = arrayY, tz[] = arrayZ;
					for (int i = 0; i <= arrayMax; i++) {
						tx[i] = arrayX[arrayMax - i];
						ty[i] = arrayY[arrayMax - i];
						tz[i] = arrayZ[arrayMax - i];
					}
					arrayX = tx;
					arrayY = ty;
					arrayZ = tz;
				}
			} else {
				// 45 degree connections
				double x1d = x1, x2d = x2, z1d = z1, z2d = z2;
				if (angle1 != 0) {
					double result[] = rotateAngle(0, 0, x1, z1, -angle1);
					x1d = result[0];
					z1d = result[1];
					result = rotateAngle(0, 0, x2, z2, -angle1);
					x2d = result[0];
					z2d = result[1];
				}

				double dz = Math.abs(z2d - z1d);
				double dx = Math.abs(x2d - x1d);
				dx -= dz;
				dz *= Math.sqrt(2);
				double r, h, k, start;
				if (dz > dx) {
					r = dx / Math.tan(Math.PI / 8D);
					k = x1d;
				} else {
					r = dz / Math.tan(Math.PI / 8D);
					k = x1d + dx - dz;
					arrayMax++;
					arrayX[arrayMax] = x1d;
					arrayY[arrayMax] = y2;
					arrayZ[arrayMax] = z1d;
				}
				h = z1d + (z2d > z1d ? r : -r);
				start = z2d > z1d ? Math.PI : 0;
				boolean reverse = !(z2d > z1d ^ x2d > x1d);
				for (double i = start; reverse ? i > start - Math.PI / 4D
						: i < start + Math.PI / 4D; i += Math.PI / (reverse ? -100D : 100D)) {
					arrayMax++;
					arrayZ[arrayMax] = h + r * Math.cos(i);
					arrayX[arrayMax] = k + r * Math.sin(i);
					arrayY[arrayMax] = y2; // temp
				}
				if (dz > dx) {
					arrayMax++;
					arrayX[arrayMax] = x2d;
					arrayY[arrayMax] = y2;
					arrayZ[arrayMax] = z2d;
				}

				if (angle1 != 0)
					for (int i = 0; i <= arrayMax; i++) {
						double result[] = rotateAngle(0, 0, arrayX[i], arrayZ[i], angle1);
						arrayX[i] = result[0];
						arrayZ[i] = result[1];
					}
			}
		}
		double am[] = { 0 };
		am[0] = arrayMax;
		switch (s) {
		case 0:
			return arrayX;
		case 1:
			return arrayY;
		case 2:
			return arrayZ;
		case 3:
			return am;
		default:
			return null;
		}
	}

	private static double[] rotateAngle(int pivotX, int pivotZ, double x, double z, int angle) {
		double r = Math.sqrt(Math.pow(x - pivotX, 2) + Math.pow(z - pivotZ, 2));
		double a = Math.acos((z - pivotZ) / r);
		if (x < pivotX)
			a = 2 * Math.PI - a;
		a = a + angle * Math.PI / 180D;
		double z2 = pivotZ + r * Math.cos(a);
		double x2 = pivotX + r * Math.sin(a);
		double result[] = { x2, z2 };
		return result;
	}
}
