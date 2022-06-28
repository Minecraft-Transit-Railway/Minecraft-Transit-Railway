package mtr.sound;

import mtr.MTR;
import mtr.mappings.SoundInstanceMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class LoopingSoundInstance extends SoundInstanceMapper implements TickableSoundInstance {

	private static final int MAX_DISTANCE = 32;

	public LoopingSoundInstance(String soundId) {
		super(new SoundEvent(new ResourceLocation(MTR.MOD_ID, soundId)), SoundSource.BLOCKS);
		looping = true;
	}

	@Override
	public boolean isStopped() {
		return false;
	}

	@Override
	public void tick() {
	}

	public void setPos(BlockPos pos, boolean isRemoved) {
		if (isRemoved) {
			if (x == pos.getX() && y == pos.getY() && z == pos.getZ()) {
				x = 0;
				y = Integer.MAX_VALUE;
				z = 0;
			}
		} else {
			final LocalPlayer player = Minecraft.getInstance().player;
			if (player == null) {
				return;
			}

			final BlockPos playerPos = player.blockPosition();
			final int distance = playerPos.distManhattan(pos);

			if (distance <= MAX_DISTANCE) {
				final int currentDistance = playerPos.distManhattan(new BlockPos(x, y, z));

				if (distance < currentDistance) {
					x = pos.getX();
					y = pos.getY();
					z = pos.getZ();
				}

				final SoundManager soundManager = Minecraft.getInstance().getSoundManager();
				if (soundManager != null && !soundManager.isActive(this)) {
					soundManager.play(this);
				}
			}
		}
	}
}
