package mtr.sound;

import mtr.data.TrainClient;
import mtr.mappings.TickableSoundInstanceMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class TrainLoopingSoundInstance extends TickableSoundInstanceMapper {

	private final TrainClient train;

	public TrainLoopingSoundInstance(SoundEvent event, TrainClient train) {
		super(event, SoundSource.BLOCKS);
		this.train = train;
		looping = true;
		delay = 0;
		volume = 0;
		pitch = 1;
	}

	public void setData(float volume, float pitch, BlockPos pos) {
		this.pitch = pitch;
		if (this.pitch == 0) {
			this.pitch = 1;
		}
		this.volume = volume;

		x = pos.getX();
		y = pos.getY();
		z = pos.getZ();

		final SoundManager soundManager = Minecraft.getInstance().getSoundManager();
		if (soundManager != null && !train.isRemoved && volume > 0 && !soundManager.isActive(this)) {
			looping = true;
			soundManager.play(this);
		}
	}

	@Override
	public void tick() {
		if (train.isRemoved) {
			stop();
		}
	}

	@Override
	public boolean canStartSilent() {
		return true;
	}

	@Override
	public boolean canPlaySound() {
		return true;
	}
}
