package mtr.data;

import mtr.MTR;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public enum TrainType {
	SP1900(0x003399, 24, 2, true, MTR.SP1900_ACCELERATION, MTR.SP1900_DECELERATION, MTR.SP1900_DOOR_OPEN, MTR.SP1900_DOOR_CLOSE, 0.5F, "sp1900"),
	SP1900_MINI(0x003399, 12, 2, true, MTR.SP1900_ACCELERATION, MTR.SP1900_DECELERATION, MTR.SP1900_DOOR_OPEN, MTR.SP1900_DOOR_CLOSE, 0.5F, "sp1900"),
	C1141A(0xB42249, 24, 2, true, MTR.C1141A_ACCELERATION, MTR.C1141A_DECELERATION, MTR.SP1900_DOOR_OPEN, MTR.SP1900_DOOR_CLOSE, 0.5F, "c1141a"),
	C1141A_MINI(0xB42249, 12, 2, true, MTR.C1141A_ACCELERATION, MTR.C1141A_DECELERATION, MTR.SP1900_DOOR_OPEN, MTR.SP1900_DOOR_CLOSE, 0.5F, "c1141a"),
	M_TRAIN(0x999999, 24, 2, true, MTR.M_TRAIN_ACCELERATION, MTR.M_TRAIN_DECELERATION, MTR.M_TRAIN_DOOR_OPEN, MTR.M_TRAIN_DOOR_CLOSE, 0.5F, "m_train"),
	M_TRAIN_MINI(0x999999, 9, 2, true, MTR.M_TRAIN_ACCELERATION, MTR.M_TRAIN_DECELERATION, MTR.M_TRAIN_DOOR_OPEN, MTR.M_TRAIN_DOOR_CLOSE, 0.5F, "m_train"),
	K_TRAIN(0x0EAB52, 24, 2, true, MTR.K_TRAIN_ACCELERATION, MTR.K_TRAIN_DECELERATION, MTR.K_TRAIN_DOOR_OPEN, MTR.K_TRAIN_DOOR_CLOSE, 1, "k_train"),
	K_TRAIN_MINI(0x0EAB52, 9, 2, true, MTR.K_TRAIN_ACCELERATION, MTR.K_TRAIN_DECELERATION, MTR.K_TRAIN_DOOR_OPEN, MTR.K_TRAIN_DOOR_CLOSE, 1, "k_train"),
	A_TRAIN_TCL(0xF69447, 24, 2, true, MTR.A_TRAIN_ACCELERATION, MTR.A_TRAIN_DECELERATION, MTR.A_TRAIN_DOOR_OPEN, MTR.A_TRAIN_DOOR_CLOSE, 0.5F, "a_train_tcl"),
	A_TRAIN_TCL_MINI(0xF69447, 9, 2, true, MTR.A_TRAIN_ACCELERATION, MTR.A_TRAIN_DECELERATION, MTR.A_TRAIN_DOOR_OPEN, MTR.A_TRAIN_DOOR_CLOSE, 0.5F, "a_train_tcl"),
	A_TRAIN_AEL(0x008D8D, 24, 2, true, MTR.A_TRAIN_ACCELERATION, MTR.A_TRAIN_DECELERATION, MTR.A_TRAIN_DOOR_OPEN, MTR.A_TRAIN_DOOR_CLOSE, 0.5F, "a_train_ael"),
	A_TRAIN_AEL_MINI(0x008D8D, 14, 2, true, MTR.A_TRAIN_ACCELERATION, MTR.A_TRAIN_DECELERATION, MTR.A_TRAIN_DOOR_OPEN, MTR.A_TRAIN_DOOR_CLOSE, 0.5F, "a_train_ael"),
	LIGHT_RAIL_1(0xD2A825, 22, 2, false, MTR.LIGHT_RAIL_1_ACCELERATION, MTR.LIGHT_RAIL_1_DECELERATION, MTR.LIGHT_RAIL_1_DOOR_OPEN, MTR.LIGHT_RAIL_1_DOOR_CLOSE, 1, "light_rail_1"),
	MINECART(0x666666, 1, 1, false, null, null, null, null, 0.5F, "minecart");

	public final int color;
	public final int width;
	public final boolean shouldRenderConnection;
	public final SoundEvent doorOpenSoundEvent;
	public final SoundEvent doorCloseSoundEvent;
	public final float doorCloseSoundTime;
	public final String id;

	private final int speedCount;
	private final SoundEvent[] accelerationSoundEvents;
	private final SoundEvent[] decelerationSoundEvents;
	private final int length;

	private static final int TICKS_PER_SPEED_SOUND = 4;

	TrainType(int color, int length, int width, boolean shouldRenderConnection, SoundEvent[] accelerationSoundEvents, SoundEvent[] decelerationSoundEvents, SoundEvent doorOpenSoundEvent, SoundEvent doorCloseSoundEvent, float doorCloseSoundTime, String id) {
		this.color = color;
		this.length = length;
		this.width = width;
		this.accelerationSoundEvents = accelerationSoundEvents;
		this.decelerationSoundEvents = decelerationSoundEvents;
		this.doorOpenSoundEvent = doorOpenSoundEvent;
		this.doorCloseSoundEvent = doorCloseSoundEvent;
		this.shouldRenderConnection = shouldRenderConnection;
		this.doorCloseSoundTime = doorCloseSoundTime;
		this.id = id;
		speedCount = accelerationSoundEvents == null || decelerationSoundEvents == null ? 0 : Math.min(accelerationSoundEvents.length, decelerationSoundEvents.length);
	}

	public String getName() {
		return new TranslatableText("train.mtr." + this).getString();
	}

	public int getSpacing() {
		return length + 1;
	}

	public void playSpeedSoundEffect(WorldAccess world, BlockPos pos, float oldSpeed, float speed) {
		if (world.getLunarTime() % TICKS_PER_SPEED_SOUND == 0 && accelerationSoundEvents != null && decelerationSoundEvents != null) {
			final int floorSpeed = (int) Math.floor(speed / Siding.ACCELERATION / TICKS_PER_SPEED_SOUND);
			if (floorSpeed > 0) {
				final int index = Math.min(floorSpeed, speedCount) - 1;
				final boolean isAccelerating = speed == oldSpeed ? new Random().nextBoolean() : speed > oldSpeed;
				world.playSound(null, pos, isAccelerating ? accelerationSoundEvents[index] : decelerationSoundEvents[index], SoundCategory.BLOCKS, 1, 1);
			}
		}
	}
}
