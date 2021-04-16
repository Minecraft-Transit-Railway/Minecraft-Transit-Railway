package mtr.data;

import mtr.MTR;
import mtr.path.PathData;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public enum TrainType {
	MINECART(0x666666, 1, 0.01F, 1, 1, 1, false, null, null, null, null, "minecart"),
	SP1900(0x003399, 2, 0.01F, 24, 2, 50, true, MTR.SP1900_ACCELERATION, MTR.SP1900_DECELERATION, MTR.SP1900_DOOR_OPEN, MTR.SP1900_DOOR_CLOSE, "sp1900"),
	SP1900_MINI(0x003399, 2, 0.01F, 12, 2, 20, true, MTR.SP1900_ACCELERATION, MTR.SP1900_DECELERATION, MTR.SP1900_DOOR_OPEN, MTR.SP1900_DOOR_CLOSE, "sp1900"),
	C1141A(0xB42249, 2, 0.01F, 24, 2, 50, true, MTR.C1141A_ACCELERATION, MTR.C1141A_DECELERATION, MTR.SP1900_DOOR_OPEN, MTR.SP1900_DOOR_CLOSE, "c1141a"),
	C1141A_MINI(0xB42249, 2, 0.01F, 12, 2, 20, true, MTR.C1141A_ACCELERATION, MTR.C1141A_DECELERATION, MTR.SP1900_DOOR_OPEN, MTR.SP1900_DOOR_CLOSE, "c1141a"),
	M_TRAIN(0x999999, 2, 0.01F, 24, 2, 50, true, MTR.M_TRAIN_ACCELERATION, MTR.M_TRAIN_DECELERATION, MTR.M_TRAIN_DOOR_OPEN, MTR.M_TRAIN_DOOR_CLOSE, "m_train"),
	M_TRAIN_MINI(0x999999, 2, 0.01F, 9, 2, 20, true, MTR.M_TRAIN_ACCELERATION, MTR.M_TRAIN_DECELERATION, MTR.M_TRAIN_DOOR_OPEN, MTR.M_TRAIN_DOOR_CLOSE, "m_train"),
	A_TRAIN_TCL(0xF69447, 2, 0.01F, 24, 2, 50, true, MTR.A_TRAIN_ACCELERATION, MTR.A_TRAIN_DECELERATION, MTR.A_TRAIN_DOOR_OPEN, MTR.A_TRAIN_DOOR_CLOSE, "a_train_tcl"),
	A_TRAIN_TCL_MINI(0xF69447, 2, 0.01F, 9, 2, 20, true, MTR.A_TRAIN_ACCELERATION, MTR.A_TRAIN_DECELERATION, MTR.A_TRAIN_DOOR_OPEN, MTR.A_TRAIN_DOOR_CLOSE, "a_train_tcl"),
	A_TRAIN_AEL(0x008D8D, 2, 0.01F, 24, 2, 50, true, MTR.A_TRAIN_ACCELERATION, MTR.A_TRAIN_DECELERATION, MTR.A_TRAIN_DOOR_OPEN, MTR.A_TRAIN_DOOR_CLOSE, "a_train_ael"),
	A_TRAIN_AEL_MINI(0x008D8D, 2, 0.01F, 9, 2, 20, true, MTR.A_TRAIN_ACCELERATION, MTR.A_TRAIN_DECELERATION, MTR.A_TRAIN_DOOR_OPEN, MTR.A_TRAIN_DOOR_CLOSE, "a_train_ael"),
	LIGHT_RAIL_1(0xD2A825, 1.2F, 0.01F, 22, 2, 40, false, null, null, null, null, "light_rail_1");

	public final int color;
	public final float maxSpeed; // blocks per tick
	public final float acceleration;
	public final int width;
	public final int capacity;
	public final boolean shouldRenderConnection;
	public final SoundEvent doorOpenSoundEvent;
	public final SoundEvent doorCloseSoundEvent;
	public final String id;

	private final int speedCount;
	private final SoundEvent[] accelerationSoundEvents;
	private final SoundEvent[] decelerationSoundEvents;
	private final int length;

	private static final int TICKS_PER_SPEED_SOUND = 4;

	TrainType(int color, float maxSpeed, float acceleration, int length, int width, int capacity, boolean shouldRenderConnection, SoundEvent[] accelerationSoundEvents, SoundEvent[] decelerationSoundEvents, SoundEvent doorOpenSoundEvent, SoundEvent doorCloseSoundEvent, String id) {
		this.color = color;
		this.maxSpeed = maxSpeed;
		this.acceleration = acceleration;
		this.length = length;
		this.width = width;
		this.capacity = capacity;
		this.accelerationSoundEvents = accelerationSoundEvents;
		this.decelerationSoundEvents = decelerationSoundEvents;
		this.doorOpenSoundEvent = doorOpenSoundEvent;
		this.doorCloseSoundEvent = doorCloseSoundEvent;
		this.shouldRenderConnection = shouldRenderConnection;
		this.id = id;
		speedCount = accelerationSoundEvents == null || decelerationSoundEvents == null ? 0 : Math.min(accelerationSoundEvents.length, decelerationSoundEvents.length);
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

	public void playSpeedSoundEffect(WorldAccess world, float worldTime, BlockPos pos, float speed, float futureSpeed) {
		if (worldTime % TICKS_PER_SPEED_SOUND == 0 && accelerationSoundEvents != null && decelerationSoundEvents != null) {
			final int floorSpeed = (int) Math.ceil(futureSpeed / PathData.ACCELERATION / TICKS_PER_SPEED_SOUND);
			if (floorSpeed > 0) {
				final int index = Math.min(floorSpeed, speedCount) - 1;
				final boolean isAccelerating = futureSpeed == speed ? new Random().nextBoolean() : futureSpeed > speed;
				world.playSound(null, pos, isAccelerating ? accelerationSoundEvents[index] : decelerationSoundEvents[index], SoundCategory.BLOCKS, 1, 1);
			}
		}
	}
}
