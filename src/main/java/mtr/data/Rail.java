package mtr.data;

import mtr.EnumHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Rail extends SerializedDataBase {

	public final RailType railType;
	public final Direction facingStart;
	public final Direction facingEnd;
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

	// for curves:
	// x = h + r*cos(T)
	// z = k + r*sin(T)
	// for straight lines:
	// x = h*T + k*r
	// z = k*T + h*r

	public Rail(BlockPos posStart, Direction facingStart, BlockPos posEnd, Direction facingEnd, RailType railType) {
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

		final boolean turnLeft = facingStart == facingEnd.rotateYClockwise();
		final boolean turnRight = facingStart == facingEnd.rotateYCounterclockwise();

		if (turnLeft || turnRight) {
			final int lengthDifference = Math.abs(xLength) - Math.abs(zLength);
			switch (facingStart.getAxis()) {
				case X:
					if (Math.signum(xLength) == facingStart.getOffsetX() && Math.signum(zLength) == facingEnd.getOpposite().getOffsetZ()) {
						if (lengthDifference > 0) {
							r2 = Math.abs(zLength);
							h2 = xEnd - Math.copySign(r2, xLength);
							k2 = zEnd;
							reverseT2 = turnRight;
							tStart2 = getTBounds(0, 0, zStart, k2, r2);
							tEnd2 = getTBounds(xEnd, h2, 0, 0, r2, tStart2, reverseT2);
							isStraight2 = false;

							r1 = zStart;
							h1 = 1;
							k1 = 0;
							tStart1 = xStart;
							tEnd1 = h2;
							reverseT1 = tEnd1 < tStart1;
							isStraight1 = true;
						} else {
							r1 = Math.abs(xLength);
							h1 = xStart;
							k1 = zStart + Math.copySign(r1, zLength);
							reverseT1 = turnRight;
							tStart1 = getTBounds(0, 0, zStart, k1, r1);
							tEnd1 = getTBounds(xEnd, h1, 0, 0, r1, tStart1, reverseT1);
							isStraight1 = false;

							r2 = xEnd;
							h2 = 0;
							k2 = 1;
							tStart2 = k1;
							tEnd2 = zEnd;
							reverseT2 = tEnd2 < tStart2;
							isStraight2 = true;
						}
					} else {
						h1 = k1 = h2 = k2 = r1 = r2 = tStart1 = tEnd1 = tStart2 = tEnd2 = 0;
						reverseT1 = isStraight1 = reverseT2 = isStraight2 = false;
					}
					break;
				case Z:
					if (Math.signum(zLength) == facingStart.getOffsetZ() && Math.signum(xLength) == facingEnd.getOpposite().getOffsetX()) {
						if (lengthDifference < 0) {
							r2 = Math.abs(xLength);
							h2 = xEnd;
							k2 = zEnd - Math.copySign(r2, zLength);
							reverseT2 = turnRight;
							tStart2 = getTBounds(xStart, h2, 0, 0, r2);
							tEnd2 = getTBounds(0, 0, zEnd, k2, r2, tStart2, reverseT2);
							isStraight2 = false;

							r1 = xStart;
							h1 = 0;
							k1 = 1;
							tStart1 = zStart;
							tEnd1 = k2;
							reverseT1 = tEnd1 < tStart1;
							isStraight1 = true;
						} else {
							r1 = Math.abs(zLength);
							h1 = xStart + Math.copySign(r1, xLength);
							k1 = zStart;
							reverseT1 = turnRight;
							tStart1 = getTBounds(xStart, h1, 0, 0, r1);
							tEnd1 = getTBounds(0, 0, zEnd, k1, r1, tStart1, reverseT1);
							isStraight1 = false;

							r2 = zEnd;
							h2 = 1;
							k2 = 0;
							tStart2 = h1;
							tEnd2 = xEnd;
							reverseT2 = tEnd2 < tStart2;
							isStraight2 = true;
						}
					} else {
						h1 = k1 = h2 = k2 = r1 = r2 = tStart1 = tEnd1 = tStart2 = tEnd2 = 0;
						reverseT1 = isStraight1 = reverseT2 = isStraight2 = false;
					}
					break;
				default:
					h1 = k1 = h2 = k2 = r1 = r2 = tStart1 = tEnd1 = tStart2 = tEnd2 = 0;
					reverseT1 = isStraight1 = reverseT2 = isStraight2 = false;
					break;
			}
		} else if (xStart != xEnd && zStart != zEnd) {
			final double halfXLength = xLength / 2F;
			final double halfZLength = zLength / 2F;
			switch (facingStart.getAxis()) {
				case X:
					final double signedRadius1 = (halfZLength * halfZLength + halfXLength * halfXLength) / (2 * halfZLength);
					r1 = r2 = Math.abs(signedRadius1);
					h1 = xStart;
					h2 = xEnd;
					k1 = zStart + signedRadius1;
					k2 = zEnd - signedRadius1;
					break;
				case Z:
					final double signedRadius2 = (halfXLength * halfXLength + halfZLength * halfZLength) / (2 * halfXLength);
					r1 = r2 = Math.abs(signedRadius2);
					h1 = xStart + signedRadius2;
					h2 = xEnd - signedRadius2;
					k1 = zStart;
					k2 = zEnd;
					break;
				default:
					h1 = k1 = h2 = k2 = r1 = r2 = 0;
					break;
			}
			reverseT1 = facingStart.rotateYCounterclockwise() == Direction.fromVector((int) Math.signum(h1 - xStart), 0, (int) Math.signum(k1 - zStart));
			reverseT2 = !reverseT1;
			tStart1 = getTBounds(xStart, h1, zStart, k1, r1);
			tEnd1 = getTBounds(xStart + halfXLength, h1, zStart + halfZLength, k1, r1, tStart1, reverseT1);
			tStart2 = getTBounds(xStart + halfXLength, h2, zStart + halfZLength, k2, r2);
			tEnd2 = getTBounds(xEnd, h2, zEnd, k2, r2, tStart2, reverseT2);
			isStraight1 = isStraight2 = false;
		} else {
			if (xStart == xEnd) {
				h1 = h2 = k2 = r2 = 0;
				k1 = 1;
				r1 = xStart;
				tStart1 = zStart;
				tEnd1 = tStart2 = tEnd2 = zEnd;
			} else {
				h2 = k1 = k2 = r2 = 0;
				h1 = 1;
				r1 = zStart;
				tStart1 = xStart;
				tEnd1 = tStart2 = tEnd2 = xEnd;
			}
			reverseT1 = tStart1 > tEnd1;
			reverseT2 = false;
			isStraight1 = isStraight2 = true;
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

		facingStart = getDirection(0, 0.1F);
		final double length = getLength();
		facingEnd = getDirection(length, length - 0.1F);
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

		facingStart = getDirection(0, 0.1F);
		final double length = getLength();
		facingEnd = getDirection(length, length - 0.1F);
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

	public void render(RenderRail callback) {
		renderSegment(h1, k1, r1, tStart1, tEnd1, 0, reverseT1, isStraight1, callback);
		renderSegment(h2, k2, r2, tStart2, tEnd2, Math.abs(tEnd1 - tStart1), reverseT2, isStraight2, callback);
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
			return new Vec3d(h * t + k * (r + radiusOffset) + 0.5F, 0, k * t + h * (r + radiusOffset) + 0.5F);
		} else {
			return new Vec3d(h + (r + radiusOffset) * Math.cos(t / r) + 0.5F, 0, k + (r + radiusOffset) * Math.sin(t / r) + 0.5F);
		}
	}

	private void renderSegment(double h, double k, double r, double tStart, double tEnd, double rawValueOffset, boolean reverseT, boolean isStraight, RenderRail callback) {
		final double count = Math.abs(tEnd - tStart);
		final double increment = count / Math.round(count);

		for (double i = 0; i < count - 0.1; i += increment) {
			final double t1 = (reverseT ? -1 : 1) * i + tStart;
			final double t2 = (reverseT ? -1 : 1) * (i + increment) + tStart;
			final Vec3d corner1 = getPositionXZ(h, k, r, t1, -1, isStraight);
			final Vec3d corner2 = getPositionXZ(h, k, r, t1, 1, isStraight);
			final Vec3d corner3 = getPositionXZ(h, k, r, t2, 1, isStraight);
			final Vec3d corner4 = getPositionXZ(h, k, r, t2, -1, isStraight);

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

	@FunctionalInterface
	public interface RenderRail {
		void renderRail(double x1, double z1, double x2, double z2, double x3, double z3, double x4, double z4, double y1, double y2);
	}
}
