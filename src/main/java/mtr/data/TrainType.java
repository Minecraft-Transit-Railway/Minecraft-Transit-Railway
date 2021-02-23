package mtr.data;

import net.minecraft.text.TranslatableText;

public enum TrainType {
	MINECART(0x666666, 1, 0.01F, 1, 1, 1),
	SP1900(0xB42249, 2, 0.01F, 24, 2, 50),
	SP1900_MINI(0xB42249, 2, 0.01F, 12, 2, 20),
	M_TRAIN(0x999999, 2, 0.01F, 24, 2, 50),
	M_TRAIN_MINI(0x999999, 2, 0.01F, 9, 2, 20),
	LIGHT_RAIL_1(0xFA831F, 1.2F, 0.01F, 22, 2, 40);

	private final int color;
	private final float maxSpeed; // blocks per tick
	private final float acceleration;
	private final int length;
	private final int width;
	private final int capacity;

	TrainType(int color, float maxSpeed, float acceleration, int length, int width, int capacity) {
		this.color = color;
		this.maxSpeed = maxSpeed;
		this.acceleration = acceleration;
		this.length = length;
		this.width = width;
		this.capacity = capacity;
	}

	public String getName() {
		return new TranslatableText("train.mtr." + toString()).getString();
	}

	public int getColor() {
		return color;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public float getAcceleration() {
		return acceleration;
	}

	public int getLength() {
		return length;
	}

	public int getWidth() {
		return width;
	}

	public int getSpacing() {
		return length + 1;
	}

	public int getCapacity() {
		return capacity;
	}
}
