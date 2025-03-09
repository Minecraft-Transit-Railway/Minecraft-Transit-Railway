package org.mtr.sound;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.LocalRandom;
import org.mtr.MTR;

public class LoopingSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {

	private static final int MAX_DISTANCE = 32;

	public LoopingSoundInstance(String soundId) {
		super(SoundEvent.of(Identifier.of(MTR.MOD_ID, soundId)), SoundCategory.BLOCKS, new LocalRandom(0));
		repeat = true;
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public void tick() {
	}

	public void setPos(BlockPos blockPos, boolean isRemoved) {
		if (isRemoved) {
			if (x == blockPos.getX() && y == blockPos.getY() && z == blockPos.getZ()) {
				x = 0;
				y = Integer.MAX_VALUE;
				z = 0;
			}
		} else {
			final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
			if (clientPlayerEntity == null) {
				return;
			}

			final BlockPos playerPos = clientPlayerEntity.getBlockPos();
			final int distance = playerPos.getManhattanDistance(blockPos);

			if (distance <= MAX_DISTANCE) {
				final int currentDistance = playerPos.getManhattanDistance(BlockPos.ofFloored(x, y, z));

				if (distance < currentDistance) {
					x = blockPos.getX();
					y = blockPos.getY();
					z = blockPos.getZ();
				}

				final SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
				if (!soundManager.isPlaying(this)) {
					soundManager.play(this);
				}
			}
		}
	}
}
