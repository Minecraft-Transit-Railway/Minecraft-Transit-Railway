package org.mtr.mod.sound;

public abstract class BveMotorDataBase {

	public abstract int getSoundCount();

	public abstract float getPitch(int index, float speed, float accel);

	public abstract float getVolume(int index, float speed, float accel);
}
