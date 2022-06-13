package mtr.sound.bve;

public class MotorData4 extends MotorDataBase { // 4 for BVE4 and OpenBVE
    @Override
    public int getSoundCount() {
        return 0;
    }

    @Override
    public float getPitch(int index, float speed, int accel) {
        return 0;
    }

    @Override
    public float getVolume(int index, float speed, int accel) {
        return 0;
    }
}
