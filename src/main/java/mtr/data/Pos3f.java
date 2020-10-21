package mtr.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class Pos3f {

	private float x;
	private float y;
	private float z;

	public Pos3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Pos3f(BlockPos pos) {
		x = pos.getX() + 0.5F;
		y = pos.getY() + 0.5F;
		z = pos.getZ() + 0.5F;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public void scale(float scale) {
		x *= scale;
		y *= scale;
		z *= scale;
	}

	public float lengthSquared() {
		return x * x + y * y + z * z;
	}

	public void add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	public void add(Pos3f pos3f) {
		add(pos3f.x, pos3f.y, pos3f.z);
	}

	public void normalize() {
		final double lengthSquared = lengthSquared();
		if (lengthSquared >= 1.0E-5) {
			final double length = MathHelper.fastInverseSqrt(lengthSquared);
			x *= length;
			y *= length;
			z *= length;
		}
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + ", " + z + "]";
	}
}
