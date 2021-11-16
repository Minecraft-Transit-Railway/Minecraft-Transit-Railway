package mtr.data;

import mtr.EnumHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static mtr.data.RailAngle.ACCEPT_THRESHOLD;

public class Rail extends SerializedDataBase {

	public final RailType railType;
	public final RailAngle facingStart;
	public final RailAngle facingEnd;
	private final double h1, k1, r1, tStart1, tEnd1;
	private final double h2, k2, r2, tStart2, tEnd2;
	private final int yStart, yEnd;
	private final boolean reverseT1, isStraight1, reverseT2, isStraight2;

	private static final double TWO_PI = 2 * Math.PI;
	private static final String KEY_H_1 = "h_1";
	private static final String KEY_K_1 = "k_1";
	private static final String KEY_H_2 = "h_2";
	private static final String KEY_K_2 = "k_2";
	private static final String KEY_R_1 = "r_1";
	private static final String KEY_R_2 = "r_2";
	private static final String KEY_T_START_1 = "t_start_1";
	private static final String KEY_T_END_1 = "t_end_1";
	private static final String KEY_T_START_2 = "t_start_2";
	private static final String KEY_T_END_2 = "t_end_2";
	private static final String KEY_Y_START = "y_start";
	private static final String KEY_Y_END = "y_end";
	private static final String KEY_REVERSE_T_1 = "reverse_t_1";
	private static final String KEY_IS_STRAIGHT_1 = "is_straight_1";
	private static final String KEY_REVERSE_T_2 = "reverse_t_2";
	private static final String KEY_IS_STRAIGHT_2 = "is_straight_2";
	private static final String KEY_RAIL_TYPE = "rail_type";
	private static final String KEY_FACING_START = "facing_start";
	private static final String KEY_FACING_END = "facing_end";

	// for curves:
	// x = h + r*cos(T)
	// z = k + r*sin(T)
	// for straight lines (both k and r >= 0.5):
	// x = h*T
	// z = k*T + h*r
	// for straight lines (otherwise):
	// x = h*T + k*r
	// z = k*T + h*r

	public Rail(BlockPos posStart, RailAngle facingStart, BlockPos posEnd, RailAngle facingEnd, RailType railType) {
		this.facingStart = facingStart;
		this.facingEnd = facingEnd;
		this.railType = railType;
		yStart = posStart.getY();
		yEnd = posEnd.getY();

		final int xStart = posStart.getX();
		final int zStart = posStart.getZ();
		final int xEnd = posEnd.getX();
		final int zEnd = posEnd.getZ();
		final int xLength = xEnd - xStart;
		final int zLength = zEnd - zStart;

		// Coordinate system translation and rotation
		Vec3d vecDiff = new Vec3d(posEnd.getX() - posStart.getX(), 0, posEnd.getZ() - posStart.getZ());
		Vec3d vecDiffRotated = vecDiff.rotateY(facingStart.getRadF());

		RailAngle actFacingStart, actFacingEnd;

		// Check if it needs invert
		if (vecDiffRotated.x < -ACCEPT_THRESHOLD) {
			actFacingStart = this.facingStart.getOpposite();
		} else {
			actFacingStart = this.facingStart;
		}

		if (facingEnd.toUnitVec3d().dotProduct(vecDiff) < -ACCEPT_THRESHOLD) {
			actFacingEnd = this.facingEnd.getOpposite();
		} else {
			actFacingEnd = this.facingEnd;
		}

		RailAngle AngleDiff = actFacingEnd.sub(actFacingStart);
		vecDiffRotated = vecDiff.rotateY(actFacingStart.getRadF());
		// dv dp for Delta Vertical and Delta Parallel
		// 1. If they are same angle
		// 1. a. If aligned -> Use One Segment
		// 1. b. If not aligned -> Use two Circle, r = (dv^2 + dp^2) / (4dv).
		// 2. If they are right angle -> r = min ( dx,dz ), work around, actually equation 3. can be used.
		// 3. Check if one segment and one circle is available
		// 3. a. If available -> (Segment First) r2 = dv / ( sin(diff) * tan(diff/2) ) = dv / ( 1 - cos(diff)
		// 							for case 2, diff = 90 degrees, r = dv
		//					-> (Circle First) r1 = ( dp - dv / tan(diff) ) / tan (diff/2)
		// TODO 3. b. If not -> r = very complex one.
		double deltaV = vecDiffRotated.z;
		double deltaP = vecDiffRotated.x;
		if (facingStart.isParallel(facingEnd)) { // 1
			if (Math.abs(deltaV) < ACCEPT_THRESHOLD) { // 1. a.
				h1 = actFacingStart.cos();
				k1 = actFacingStart.sin();
				if ((Math.abs(h1) >= 0.5) && (Math.abs(k1) >= 0.5)) {
					r1 = (h1 * zStart - k1 * xStart) / h1 / h1;
					tStart1 = xStart / h1;
					tEnd1 = xEnd / h1;
				} else {
					double div = actFacingStart.add(actFacingStart).cos(); // cos (2*aFS) = (h1^2 - k1^2)
					r1 = (h1 * zStart - k1 * xStart) / div;
					tStart1 = (h1 * xStart - k1 * zStart) / div;
					tEnd1 = (h1 * xEnd - k1 * zEnd) / div;
				}
				h2 = k2 = r2 = 0;
				reverseT1 = tStart1 > tEnd1;
				reverseT2 = false;
				isStraight1 = isStraight2 = true;
				tStart2 = tEnd2 = 0;
			} else { // 1. b
				double ar = (deltaV * deltaV + deltaP * deltaP) / (4 * deltaV);
				r1 = r2 = Math.abs(ar);
				h1 = xStart - ar * actFacingStart.sin();
				k1 = zStart + ar * actFacingStart.cos();
				h2 = xEnd + ar * actFacingEnd.sin();
				k2 = zEnd - ar * actFacingEnd.cos();
				reverseT1 = (deltaV < 0);
				reverseT2 = !reverseT1;
				tStart1 = getTBounds(xStart, h1, zStart, k1, r1);
				tEnd1 = getTBounds(xStart + vecDiff.x / 2, h1, zStart + vecDiff.z / 2, k1, r1, tStart1, reverseT1);
				tStart2 = getTBounds(xStart + vecDiff.x / 2, h2, zStart + vecDiff.z / 2, k2, r2);
				tEnd2 = getTBounds(xEnd, h2, zEnd, k2, r2, tStart2, reverseT2);
				isStraight1 = isStraight2 = false;
			}
		} else { // 3.
			double angleV = Math.atan2(deltaV, deltaP);
			double angleT = AngleDiff.getRadD();
			double halfT = angleT / 2;
			if (Math.signum(angleV) == Math.signum(halfT)) {
				double absAngleV = Math.abs(angleV);
				double absHalfT = Math.abs(halfT);
				if ( (absAngleV - absHalfT) < ACCEPT_THRESHOLD ) { // Segment First
					double offsetP = Math.abs( deltaV / AngleDiff.div(2).tan() );
					double remainP = deltaP - offsetP;
					double SXEnd = xStart + remainP * actFacingStart.cos();
					double SZEnd = zStart + remainP * actFacingStart.sin();
					h1 = actFacingStart.cos();
					k1 = actFacingStart.sin();
					if ((Math.abs(h1) >= 0.5) && (Math.abs(k1) >= 0.5)) {
						r1 = (h1 * zStart - k1 * xStart) / h1 / h1;
						tStart1 = xStart / h1;
						tEnd1 = SXEnd / h1;
					} else {
						double div = actFacingStart.add(actFacingStart).cos(); // cos (2*aFS) = (h1^2 - k1^2)
						r1 = (h1 * zStart - k1 * xStart) / div;
						tStart1 = (h1 * xStart - k1 * zStart) / div;
						tEnd1 = (h1 * SXEnd - k1 * SZEnd) / div;
					}
					isStraight1 = true;
					reverseT1 = tStart1 > tEnd1;
					double ar2 = deltaV / ( 1 - AngleDiff.cos() );
					r2 = Math.abs(ar2);
					h2 = SXEnd - ar2 * actFacingStart.sin();
					k2 = SZEnd + ar2 * actFacingStart.cos();
					reverseT2 = (deltaV < 0);
					tStart2 = getTBounds(SXEnd, h2, SZEnd, k2, r2);
					tEnd2 = getTBounds(xEnd, h2, zEnd, k2, r2, tStart2, reverseT2);
					isStraight2 = false;
				} else if ( (absAngleV - Math.abs(angleT)) < ACCEPT_THRESHOLD ) { // Circle First
					double crossP = deltaV / AngleDiff.tan();
					double remainP = (deltaP - crossP) * (1 + AngleDiff.cos());
					double remainV = (deltaP - crossP) * (AngleDiff.sin());
					double SXEnd = xStart + remainP * actFacingStart.cos() - remainV * actFacingStart.sin();
					double SZEnd = zStart + remainP * actFacingStart.sin() + remainV * actFacingStart.cos();
					double ar1 = ( deltaP - deltaV / AngleDiff.tan() ) / AngleDiff.div(2).tan();
					r1 = Math.abs(ar1);
					h1 = xStart - ar1 * actFacingStart.sin();
					k1 = zStart + ar1 * actFacingStart.cos();
					isStraight1 = false;
					reverseT1 = (deltaV < 0);
					tStart1 = getTBounds(xStart, h1, zStart, k1, r1);
					tEnd1 = getTBounds(SXEnd, h1, SZEnd, k1, r1, tStart1, reverseT1);
					h2 = actFacingEnd.cos();
					k2 = actFacingEnd.sin();
					if ((Math.abs(h2) >= 0.5) && (Math.abs(k2) >= 0.5)) {
						r2 = (h2 * SZEnd - k2 * SXEnd) / h2 / h2;
						tStart2 = SXEnd / h2;
						tEnd2 = xEnd / h2;
					} else {
						double div = actFacingEnd.add(actFacingEnd).cos(); // cos (2*aFE) = (h1^2 - k1^2)
						r2 = (h2 * SZEnd - k2 * SXEnd) / div;
						tStart2 = (h2 * SXEnd - k2 * SZEnd) / div;
						tEnd2 = (h2 * xEnd - k2 * zEnd) / div;
					}
					isStraight2 = true;
					reverseT2 = tStart2 > tEnd2;
				} else { // Out of available range
					// TODO complex one. Normally we don't need it.
					h1 = k1 = h2 = k2 = r1 = r2 = 0;
					tStart1 = tStart2 = tEnd1 = tEnd2 = 0;
					reverseT1 = false;
					reverseT2 = false;
					isStraight1 = isStraight2 = true;
				}
			} else {
				// TODO 3. b. If not -> r = very complex one. Normally we don't need it.
				h1 = k1 = h2 = k2 = r1 = r2 = 0;
				tStart1 = tStart2 = tEnd1 = tEnd2 = 0;
				reverseT1 = false;
				reverseT2 = false;
				isStraight1 = isStraight2 = true;
			}
		}
	}

	public Rail(NbtCompound nbtCompound) {
		h1 = nbtCompound.getDouble(KEY_H_1);
		k1 = nbtCompound.getDouble(KEY_K_1);
		h2 = nbtCompound.getDouble(KEY_H_2);
		k2 = nbtCompound.getDouble(KEY_K_2);
		r1 = nbtCompound.getDouble(KEY_R_1);
		r2 = nbtCompound.getDouble(KEY_R_2);
		tStart1 = nbtCompound.getDouble(KEY_T_START_1);
		tEnd1 = nbtCompound.getDouble(KEY_T_END_1);
		tStart2 = nbtCompound.getDouble(KEY_T_START_2);
		tEnd2 = nbtCompound.getDouble(KEY_T_END_2);
		yStart = nbtCompound.getInt(KEY_Y_START);
		yEnd = nbtCompound.getInt(KEY_Y_END);
		reverseT1 = nbtCompound.getBoolean(KEY_REVERSE_T_1);
		isStraight1 = nbtCompound.getBoolean(KEY_IS_STRAIGHT_1);
		reverseT2 = nbtCompound.getBoolean(KEY_REVERSE_T_2);
		isStraight2 = nbtCompound.getBoolean(KEY_IS_STRAIGHT_2);
		railType = EnumHelper.valueOf(RailType.IRON, nbtCompound.getString(KEY_RAIL_TYPE));

		facingStart = new RailAngle(nbtCompound.getInt(KEY_FACING_START));
		facingEnd = new RailAngle(nbtCompound.getInt(KEY_FACING_END));
	}

	public Rail(PacketByteBuf packet) {
		h1 = packet.readDouble();
		k1 = packet.readDouble();
		h2 = packet.readDouble();
		k2 = packet.readDouble();
		r1 = packet.readDouble();
		r2 = packet.readDouble();
		tStart1 = packet.readDouble();
		tEnd1 = packet.readDouble();
		tStart2 = packet.readDouble();
		tEnd2 = packet.readDouble();
		yStart = packet.readInt();
		yEnd = packet.readInt();
		reverseT1 = packet.readBoolean();
		isStraight1 = packet.readBoolean();
		reverseT2 = packet.readBoolean();
		isStraight2 = packet.readBoolean();
		railType = EnumHelper.valueOf(RailType.IRON, packet.readString(PACKET_STRING_READ_LENGTH));

		facingStart = new RailAngle(packet.readInt());
		facingEnd = new RailAngle(packet.readInt());
	}

	@Override
	public NbtCompound toCompoundTag() {
		final NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.putDouble(KEY_H_1, h1);
		nbtCompound.putDouble(KEY_K_1, k1);
		nbtCompound.putDouble(KEY_H_2, h2);
		nbtCompound.putDouble(KEY_K_2, k2);
		nbtCompound.putDouble(KEY_R_1, r1);
		nbtCompound.putDouble(KEY_R_2, r2);
		nbtCompound.putDouble(KEY_T_START_1, tStart1);
		nbtCompound.putDouble(KEY_T_END_1, tEnd1);
		nbtCompound.putDouble(KEY_T_START_2, tStart2);
		nbtCompound.putDouble(KEY_T_END_2, tEnd2);
		nbtCompound.putInt(KEY_Y_START, yStart);
		nbtCompound.putInt(KEY_Y_END, yEnd);
		nbtCompound.putBoolean(KEY_REVERSE_T_1, reverseT1);
		nbtCompound.putBoolean(KEY_IS_STRAIGHT_1, isStraight1);
		nbtCompound.putBoolean(KEY_REVERSE_T_2, reverseT2);
		nbtCompound.putBoolean(KEY_IS_STRAIGHT_2, isStraight2);
		nbtCompound.putString(KEY_RAIL_TYPE, railType.toString());
		nbtCompound.putInt(KEY_FACING_START, facingStart.angle);
		nbtCompound.putInt(KEY_FACING_END, facingEnd.angle);
		return nbtCompound;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		packet.writeDouble(h1);
		packet.writeDouble(k1);
		packet.writeDouble(h2);
		packet.writeDouble(k2);
		packet.writeDouble(r1);
		packet.writeDouble(r2);
		packet.writeDouble(tStart1);
		packet.writeDouble(tEnd1);
		packet.writeDouble(tStart2);
		packet.writeDouble(tEnd2);
		packet.writeInt(yStart);
		packet.writeInt(yEnd);
		packet.writeBoolean(reverseT1);
		packet.writeBoolean(isStraight1);
		packet.writeBoolean(reverseT2);
		packet.writeBoolean(isStraight2);
		packet.writeString(railType.toString());
		packet.writeInt(facingStart.angle);
		packet.writeInt(facingEnd.angle);
	}

	public Vec3d getPosition(double rawValue) {
		final double count1 = Math.abs(tEnd1 - tStart1);
		final double count2 = Math.abs(tEnd2 - tStart2);
		final double value = MathHelper.clamp(rawValue, 0, count1 + count2);
		final double y = getPositionY(value);

		if (value <= count1) {
			return getPositionXZ(h1, k1, r1, (reverseT1 ? -1 : 1) * value + tStart1, 0, isStraight1).add(0, y, 0);
		} else {
			return getPositionXZ(h2, k2, r2, (reverseT2 ? -1 : 1) * (value - count1) + tStart2, 0, isStraight2).add(0, y, 0);
		}
	}

	public double getLength() {
		return Math.abs(tEnd2 - tStart2) + Math.abs(tEnd1 - tStart1);
	}

	public void render(RenderRail callback, float offsetRadius1, float offsetRadius2) {
		renderSegment(h1, k1, r1, tStart1, tEnd1, 0, offsetRadius1, offsetRadius2, reverseT1, isStraight1, callback);
		renderSegment(h2, k2, r2, tStart2, tEnd2, Math.abs(tEnd1 - tStart1), offsetRadius1, offsetRadius2, reverseT2, isStraight2, callback);
	}

	private double getPositionY(double value) {
		final double intercept = getLength() / 2;
		final double yChange;
		final double yInitial;
		final double offsetValue;
		if (value < intercept) {
			yChange = (yEnd - yStart) / 2F;
			yInitial = yStart;
			offsetValue = value;
		} else {
			yChange = (yStart - yEnd) / 2F;
			yInitial = yEnd;
			offsetValue = getLength() - value;
		}
		return yChange * offsetValue * offsetValue / (intercept * intercept) + yInitial;
	}

	private static Vec3d getPositionXZ(double h, double k, double r, double t, double radiusOffset, boolean isStraight) {
		if (isStraight) {
			if (  (Math.abs(h) >= 0.5) && (Math.abs(k) >= 0.5)  ) {
				return new Vec3d(h * t + k * (radiusOffset) + 0.5F, 0, k * t + h * (r - radiusOffset) + 0.5F);
			} else {
				return new Vec3d(h * t + k * (r + radiusOffset) + 0.5F, 0, k * t + h * (r - radiusOffset) + 0.5F);
			}
		} else {
			return new Vec3d(h + (r + radiusOffset) * Math.cos(t / r) + 0.5F, 0, k + (r + radiusOffset) * Math.sin(t / r) + 0.5F);
		}
	}

	private void renderSegment(double h, double k, double r, double tStart, double tEnd, double rawValueOffset, float offsetRadius1, float offsetRadius2, boolean reverseT, boolean isStraight, RenderRail callback) {
		final double count = Math.abs(tEnd - tStart);
		final double increment = count / Math.round(count);

		for (double i = 0; i < count - 0.1; i += increment) {
			final double t1 = (reverseT ? -1 : 1) * i + tStart;
			final double t2 = (reverseT ? -1 : 1) * (i + increment) + tStart;
			final Vec3d corner1 = getPositionXZ(h, k, r, t1, offsetRadius1, isStraight);
			final Vec3d corner2 = getPositionXZ(h, k, r, t1, offsetRadius2, isStraight);
			final Vec3d corner3 = getPositionXZ(h, k, r, t2, offsetRadius2, isStraight);
			final Vec3d corner4 = getPositionXZ(h, k, r, t2, offsetRadius1, isStraight);

			final double y1 = getPositionY(i + rawValueOffset);
			final double y2 = getPositionY(i + increment + rawValueOffset);

			callback.renderRail(corner1.x, corner1.z, corner2.x, corner2.z, corner3.x, corner3.z, corner4.x, corner4.z, y1, y2);
		}
	}

	private Direction getDirection(double start, double end) {
		final Vec3d pos1 = getPosition(start);
		final Vec3d pos2 = getPosition(end);
		return Direction.fromRotation(Math.toDegrees(Math.atan2(pos2.x - pos1.x, pos1.z - pos2.z)) + 180);
	}

	private static double getTBounds(double x, double h, double z, double k, double r) {
		return MathHelper.atan2(z - k, x - h) * r;
	}

	private static double getTBounds(double x, double h, double z, double k, double r, double tStart, boolean reverse) {
		final double t = getTBounds(x, h, z, k, r);
		if (t < tStart && !reverse) {
			return t + TWO_PI * r;
		} else if (t > tStart && reverse) {
			return t - TWO_PI * r;
		} else {
			return t;
		}
	}

	public boolean isValid() {
		return !((h1 == 0) && (k1 == 0) && (h2 == 0) && (k2 == 0) && (r1 == 0) && (r2 == 0)
				&& (tStart1 == 0) && (tStart2 == 0)&& (tEnd1 == 0)&& (tEnd2 == 0)
				&& (!reverseT1) && (!reverseT2) //&& (reverseT1 == false) && (reverseT2 == false)
				&& (isStraight1) && (isStraight2)); //&& (isStraight1 == true) && (isStraight2 == true));
	}

	@FunctionalInterface
	public interface RenderRail {
		void renderRail(double x1, double z1, double x2, double z2, double x3, double z3, double x4, double z4, double y1, double y2);
	}
}
