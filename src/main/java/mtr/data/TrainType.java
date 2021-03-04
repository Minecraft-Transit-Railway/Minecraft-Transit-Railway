package mtr.data;

import net.minecraft.text.TranslatableText;

public enum TrainType {
	MINECART(0x666666, 1, 0.01F, 1, 1, 1, false, "minecart"),
	SP1900(0x003399, 2, 0.01F, 24, 2, 50, true, "sp1900"),
	SP1900_MINI(0x003399, 2, 0.01F, 12, 2, 20, true, "sp1900"),
	C1141A(0xB42249, 2, 0.01F, 24, 2, 50, true, "c1141a"),
	C1141A_MINI(0xB42249, 2, 0.01F, 12, 2, 20, true, "c1141a"),
	M_TRAIN(0x999999, 2, 0.01F, 24, 2, 50, true, "m_train"),
	M_TRAIN_MINI(0x999999, 2, 0.01F, 9, 2, 20, true, "m_train"),
	LIGHT_RAIL_1(0xFA831F, 1.2F, 0.01F, 22, 2, 40, false, "light_rail_1");

	public final int color;
	public final float maxSpeed; // blocks per tick
	public final float acceleration;
	public final int width;
	public final int capacity;
	public final boolean shouldRenderConnection;
	public final String id;
	private final int length;

	TrainType(int color, float maxSpeed, float acceleration, int length, int width, int capacity, boolean shouldRenderConnection, String id) {
		this.color = color;
		this.maxSpeed = maxSpeed;
		this.acceleration = acceleration;
		this.length = length;
		this.width = width;
		this.capacity = capacity;
		this.shouldRenderConnection = shouldRenderConnection;
		this.id = id;
	}

	public String getName() {
		return new TranslatableText("train.mtr." + toString()).getString();
	}

	public int getLength() {
		return length;
	}

	public int getSpacing() {
		return length + 1;
	}
}
