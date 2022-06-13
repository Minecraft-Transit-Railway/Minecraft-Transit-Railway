package mtr.sound.bve;

public abstract class MotorDataBase {

    public abstract int getSoundCount();

    public abstract float getPitch(int index, float speed, int accel);

    public abstract float getVolume(int index, float speed, int accel);

}
