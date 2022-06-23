package mtr.sound;

import mtr.data.TrainClient;
import mtr.mappings.TickableSoundInstanceMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class TrainLoopingSoundInstance extends TickableSoundInstanceMapper {

	TrainClient binding;

	public TrainLoopingSoundInstance(SoundEvent event, TrainClient binding) {
		super(event, SoundSource.BLOCKS);
		looping = true;
		this.binding = binding;
		delay = 0;
		volume = 0.0f;
		pitch = 1.0f;
	}

	public void setVolumePitch(float volume, float pitch) {
		this.pitch = pitch;
		if (this.pitch == 0) {
			this.pitch = 1;
		}
		this.volume = volume;
	}

	public void setPos(BlockPos pos) {
		x = pos.getX();
		y = pos.getY();
		z = pos.getZ();

		final SoundManager soundManager = Minecraft.getInstance().getSoundManager();
		if (soundManager != null && !binding.isRemoved && volume > 0 && !soundManager.isActive(this)) {
			looping = true;
			soundManager.play(this);
		}
	}

	@Override
	public void tick() {
		if (binding.isRemoved) {
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
