package mtr.data;

import net.minecraft.util.math.MathHelper;

public class Pos3f {

	public final float x;
	public final float y;
	public final float z;

	public Pos3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Pos3f scale(float scale) {
		return new Pos3f(x * scale, y * scale, z * scale);
	}

	public Pos3f add(float x, float y, float z) {
		return new Pos3f(this.x + x, this.y + y, this.z + z);
	}

	public Pos3f add(Pos3f pos3f) {
		return add(pos3f.x, pos3f.y, pos3f.z);
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
