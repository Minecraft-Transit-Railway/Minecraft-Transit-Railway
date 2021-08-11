package mtr.data;

import mtr.render.QuadCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClientGame;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class Rail extends SerializedDataBase {

	public final RailType railType;
	public final String ballastTexture;
	public final Direction facingStart;
	public final Direction facingEnd;
	private final float h1, k1, r1, tStart1, tEnd1;
	private final float h2, k2, r2, tStart2, tEnd2;
	private final int yStart, yEnd;
	private final boolean reverseT1, isStraight1, reverseT2, isStraight2;

	private static final float TWO_PI = (float) (2 * Math.PI);
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
	private static final String KEY_BALLAST_TEXTURE = "ballast_texture";

	public QuadCache quadCache = new QuadCache();

	// for curves:
	// x = h + r*cos(T)
	// z = k + r*sin(T)
	// for straight lines:
	// x = h*T + k*r
	// z = k*T + h*r

	public Rail(BlockPos posStart, Direction facingStart, BlockPos posEnd, Direction facingEnd, RailType railType, String ballastTexture) {
		this.facingStart = facingStart;
		this.facingEnd = facingEnd;
		this.railType = railType;
		this.ballastTexture = ballastTexture;
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
			final float halfXLength = xLength / 2F;
			final float halfZLength = zLength / 2F;
			switch (facingStart.getAxis()) {
				case X:
					final float signedRadius1 = (halfZLength * halfZLength + halfXLength * halfXLength) / (2 * halfZLength);
					r1 = r2 = Math.abs(signedRadius1);
					h1 = xStart;
					h2 = xEnd;
					k1 = zStart + signedRadius1;
					k2 = zEnd - signedRadius1;
					break;
				case Z:
					final float signedRadius2 = (halfXLength * halfXLength + halfZLength * halfZLength) / (2 * halfXLength);
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
		h1 = nbtCompound.getFloat(KEY_H_1);
		k1 = nbtCompound.getFloat(KEY_K_1);
		h2 = nbtCompound.getFloat(KEY_H_2);
		k2 = nbtCompound.getFloat(KEY_K_2);
		r1 = nbtCompound.getFloat(KEY_R_1);
		r2 = nbtCompound.getFloat(KEY_R_2);
		tStart1 = nbtCompound.getFloat(KEY_T_START_1);
		tEnd1 = nbtCompound.getFloat(KEY_T_END_1);
		tStart2 = nbtCompound.getFloat(KEY_T_START_2);
		tEnd2 = nbtCompound.getFloat(KEY_T_END_2);
		yStart = nbtCompound.getInt(KEY_Y_START);
		yEnd = nbtCompound.getInt(KEY_Y_END);
		reverseT1 = nbtCompound.getBoolean(KEY_REVERSE_T_1);
		isStraight1 = nbtCompound.getBoolean(KEY_IS_STRAIGHT_1);
		reverseT2 = nbtCompound.getBoolean(KEY_REVERSE_T_2);
		isStraight2 = nbtCompound.getBoolean(KEY_IS_STRAIGHT_2);
		railType = RailType.valueOf(nbtCompound.getString(KEY_RAIL_TYPE));
		ballastTexture = nbtCompound.getString(KEY_BALLAST_TEXTURE);

		facingStart = getDirection(0, 0.1F);
		final float length = getLength();
		facingEnd = getDirection(length, length - 0.1F);
	}

	public Rail(PacketByteBuf packet) {
		h1 = packet.readFloat();
		k1 = packet.readFloat();
		h2 = packet.readFloat();
		k2 = packet.readFloat();
		r1 = packet.readFloat();
		r2 = packet.readFloat();
		tStart1 = packet.readFloat();
		tEnd1 = packet.readFloat();
		tStart2 = packet.readFloat();
		tEnd2 = packet.readFloat();
		yStart = packet.readInt();
		yEnd = packet.readInt();
		reverseT1 = packet.readBoolean();
		isStraight1 = packet.readBoolean();
		reverseT2 = packet.readBoolean();
		isStraight2 = packet.readBoolean();
		railType = RailType.valueOf(packet.readString(PACKET_STRING_READ_LENGTH));
		ballastTexture = packet.readString(PACKET_STRING_READ_LENGTH);

		facingStart = getDirection(0, 0.1F);
		final float length = getLength();
		facingEnd = getDirection(length, length - 0.1F);
	}

	@Override
	public NbtCompound toCompoundTag() {
		final NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.putFloat(KEY_H_1, h1);
		nbtCompound.putFloat(KEY_K_1, k1);
		nbtCompound.putFloat(KEY_H_2, h2);
		nbtCompound.putFloat(KEY_K_2, k2);
		nbtCompound.putFloat(KEY_R_1, r1);
		nbtCompound.putFloat(KEY_R_2, r2);
		nbtCompound.putFloat(KEY_T_START_1, tStart1);
		nbtCompound.putFloat(KEY_T_END_1, tEnd1);
		nbtCompound.putFloat(KEY_T_START_2, tStart2);
		nbtCompound.putFloat(KEY_T_END_2, tEnd2);
		nbtCompound.putInt(KEY_Y_START, yStart);
		nbtCompound.putInt(KEY_Y_END, yEnd);
		nbtCompound.putBoolean(KEY_REVERSE_T_1, reverseT1);
		nbtCompound.putBoolean(KEY_IS_STRAIGHT_1, isStraight1);
		nbtCompound.putBoolean(KEY_REVERSE_T_2, reverseT2);
		nbtCompound.putBoolean(KEY_IS_STRAIGHT_2, isStraight2);
		nbtCompound.putString(KEY_RAIL_TYPE, railType.toString());
		nbtCompound.putString(KEY_BALLAST_TEXTURE, ballastTexture);
		return nbtCompound;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		packet.writeFloat(h1);
		packet.writeFloat(k1);
		packet.writeFloat(h2);
		packet.writeFloat(k2);
		packet.writeFloat(r1);
		packet.writeFloat(r2);
		packet.writeFloat(tStart1);
		packet.writeFloat(tEnd1);
		packet.writeFloat(tStart2);
		packet.writeFloat(tEnd2);
		packet.writeInt(yStart);
		packet.writeInt(yEnd);
		packet.writeBoolean(reverseT1);
		packet.writeBoolean(isStraight1);
		packet.writeBoolean(reverseT2);
		packet.writeBoolean(isStraight2);
		packet.writeString(railType.toString());
		packet.writeString(ballastTexture.toString());
	}

	public Pos3f getPosition(float rawValue) {
		final float count1 = Math.abs(tEnd1 - tStart1);
		final float count2 = Math.abs(tEnd2 - tStart2);
		final float value = MathHelper.clamp(rawValue, 0, count1 + count2);
		final float y = getPositionY(value);

		if (value <= count1) {
			return getPositionXZ(h1, k1, r1, (reverseT1 ? -1 : 1) * value + tStart1, 0, isStraight1).add(0, y, 0);
		} else {
			return getPositionXZ(h2, k2, r2, (reverseT2 ? -1 : 1) * (value - count1) + tStart2, 0, isStraight2).add(0, y, 0);
		}
	}

	public float getLength() {
		return Math.abs(tEnd2 - tStart2) + Math.abs(tEnd1 - tStart1);
	}

	public void render(RenderRail callback) {
		renderSegment(h1, k1, r1, tStart1, tEnd1, 0, reverseT1, isStraight1, callback);
		renderSegment(h2, k2, r2, tStart2, tEnd2, Math.abs(tEnd1 - tStart1), reverseT2, isStraight2, callback);
	}

	private static class pitchCurve {
		public float a, r, k; // Pitch angle, Transition radius, Pitch slope
		public float L, lC, lT; // Length, Constant part, Transition part
		public float H, hC, hT; // Height, Constant part, Transition part
	}
	private pitchCurve pc = null;

	private float getPositionY(float value) {
		calculatePitchCurve();
		if (pc.H == 0) {
			return yStart;
		}
		final float yLow = Math.min(yStart, yEnd);
		final float yHigh = Math.max(yStart, yEnd);
		final float valueLow = yStart < yEnd ? value - 0.5F : pc.L - value + 0.5F;
		if (valueLow < 0) {
			return yLow;
		} else if (valueLow > pc.L) {
			return yHigh;
		} else if (valueLow < pc.lT) {
			final float cosA = (float)Math.sqrt(1 - Math.pow(valueLow / pc.r, 2));
			return pc.r - pc.r * cosA + yLow;
		} else if (valueLow >= pc.L - pc.lT) {
			final float cosA = (float)Math.sqrt(1 - Math.pow((pc.L - valueLow) / pc.r, 2));
			return yHigh - pc.r + pc.r * cosA;
		} else {
			return yLow + pc.hT + (valueLow - pc.lT) * pc.k;
		}
	}

	private void calculatePitchCurve() {
		if (pc == null) {
			pc = new pitchCurve();
			pc.L = getLength() - 1;
			pc.H = Math.abs(yStart - yEnd);
			if (pc.H == 0) {
				pc.lT = 0;
				pc.lC = pc.L;
				pc.hT = pc.hC = pc.a = pc.r = pc.k = 0;
				return;
			}
			if (pc.L < 5) {
				pc.lT = pc.L / 2;
				pc.lC = 0;
				pc.hT = pc.H / 2;
				pc.hC = 0;
				pc.r = (pc.H * pc.H + pc.L * pc.L) / (4 * pc.H);
				pc.a = 2 * (float)Math.atan(pc.H / pc.L);
				pc.k = (float)Math.tan(pc.a);
			} else {
				pc.lT = Math.min(Math.max(2, pc.L / 5), 6);
				pc.lC = pc.L - 2 * pc.lT;
				float deltaKMin = Float.MAX_VALUE;
				// Exact value requires solving a cubic equation. So just take an approximation.
				for (float a = 0; a < Math.PI / 2; a += Math.PI / 180) {
					final float r = pc.lT / (float)Math.sin(a);
					final float h = pc.H - 2 * r * (1 - (float)Math.cos(a));
					final float deltaK = Math.abs((float)Math.tan(a) - h / pc.lC);
					if (deltaK < deltaKMin) {
						deltaKMin = deltaK;
						pc.a = a;
						pc.r = r;
						pc.hC = h;
						pc.hT = (pc.H - h) / 2;
						pc.k = h / pc.lC;
					}
				}
			}
		}
	}

	public static Pos3f getPositionXZ(float h, float k, float r, float t, float radiusOffset, boolean isStraight) {
		if (isStraight) {
			return new Pos3f(h * t + k * (r + radiusOffset) + 0.5F, 0, k * t + h * (r + radiusOffset) + 0.5F);
		} else {
			return new Pos3f(h + (r + radiusOffset) * (float) Math.cos(t / r) + 0.5F, 0, k + (r + radiusOffset) * (float) Math.sin(t / r) + 0.5F);
		}
	}

	private void renderSegment(float h, float k, float r, float tStart, float tEnd, float rawValueOffset, boolean reverseT, boolean isStraight, RenderRail callback) {
		final float count = Math.abs(tEnd - tStart);
		final float increment = count / Math.round(count);

		// This is to make sure rail rendering are all aligned to block edges,
		// so that ballast rendering can produce meaningful results.
		float i = 0;
		// First segment, 0.5 block long
		renderSingleSegment(
				h, k, r,
				(reverseT ? -1 : 1) * i + tStart, (reverseT ? -1 : 1) * (i + increment / 2) + tStart,
				i + rawValueOffset, i + increment / 2 + rawValueOffset,
				isStraight, true, callback
		);
		if (isStraight() && isFlat()) {
			// Save some performance
			// Sacrifices lighting accuracy
			final float segmentLength = 8;
			final float midInc = (float)((count - increment) / Math.ceil((count - increment) / segmentLength));
			for (i = increment / 2; i < count - 0.1 - increment / 2; i += midInc) {
				renderSingleSegment(
						h, k, r,
						(reverseT ? -1 : 1) * i + tStart, (reverseT ? -1 : 1) * (i + midInc) + tStart,
						i + rawValueOffset, i + midInc + rawValueOffset,
						isStraight, false, callback
				);
			}
		} else {
			// Middle segments, 1 block long
			for (i = increment / 2; i < count - 0.1 - increment / 2; i += increment) {
				renderSingleSegment(
						h, k, r,
						(reverseT ? -1 : 1) * i + tStart, (reverseT ? -1 : 1) * (i + increment) + tStart,
						i + rawValueOffset, i + increment + rawValueOffset,
						isStraight, false, callback
				);
			}
		}
		// Last segment, 0.5 block long
		renderSingleSegment(
				h, k, r,
				(reverseT ? -1 : 1) * i + tStart, (reverseT ? -1 : 1) * (i + increment / 2) + tStart,
				i + rawValueOffset, i + increment / 2 + rawValueOffset,
				isStraight, true, callback
		);
	}

	private void renderSingleSegment(float h, float k, float r, float t1, float t2, float v1, float v2, boolean isStraight, boolean isEnd, RenderRail callback) {
		final float y1 = getPositionY(v1);
		final float y2 = getPositionY(v2);

		callback.renderRail(h, k, r, t1, t2, y1, y2, isStraight, isEnd);
	}

	private Direction getDirection(float start, float end) {
		final Pos3f pos1 = getPosition(start);
		final Pos3f pos2 = getPosition(end);
		return Direction.fromRotation(Math.toDegrees(Math.atan2(pos2.x - pos1.x, pos1.z - pos2.z)) + 180);
	}

	private static float getTBounds(float x, float h, float z, float k, float r) {
		return (float) (MathHelper.atan2(z - k, x - h) * r);
	}

	private static float getTBounds(float x, float h, float z, float k, float r, float tStart, boolean reverse) {
		final float t = getTBounds(x, h, z, k, r);
		if (t < tStart && !reverse) {
			return t + TWO_PI * r;
		} else if (t > tStart && reverse) {
			return t - TWO_PI * r;
		} else {
			return t;
		}
	}

	public boolean isStraight() {
		return isStraight1 && isStraight2;
	}

	public boolean isFlat() {
		return yStart == yEnd;
	}

	@FunctionalInterface
	public interface RenderRail {
		void renderRail(float h, float k, float r, float t1, float t2, float y1, float y2, boolean isStraight, boolean isEnd);
	}
}
