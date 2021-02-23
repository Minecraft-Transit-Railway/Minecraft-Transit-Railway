package mtr.data;

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

	public Pos3f rotateX(float angle) {
		float cos = MathHelper.cos(angle);
		float sin = MathHelper.sin(angle);
		return new Pos3f(x, y * cos + z * sin, z * cos - y * sin);
	}

	public Pos3f rotateY(float angle) {
		float cos = MathHelper.cos(angle);
		float sin = MathHelper.sin(angle);
		return new Pos3f(x * cos + z * sin, y, z * cos - x * sin);
	}

	public float getDistanceTo(Pos3f pos3f) {
		return (float) Math.sqrt(MathHelper.square(x - pos3f.x) + MathHelper.square(y - pos3f.y) + MathHelper.square(z - pos3f.z));
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + ", " + z + "]";
	}
}
