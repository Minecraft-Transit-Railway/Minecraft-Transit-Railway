package org.mtr.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import org.mtr.MTR;

public class LoopingSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {

	private static final int MAX_DISTANCE = 32;

	public LoopingSoundInstance(String soundId) {
		super(SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, soundId)), SoundSource.BLOCKS, new SingleThreadedRandomSource(0));
		looping = true;
	}

	@Override
	public boolean isStopped() {
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
			final LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;
			if (clientPlayerEntity == null) {
				return;
			}

			final BlockPos playerPos = clientPlayerEntity.blockPosition();
			final int distance = playerPos.distManhattan(blockPos);

			if (distance <= MAX_DISTANCE) {
				final int currentDistance = playerPos.distManhattan(BlockPos.containing(x, y, z));

				if (distance < currentDistance) {
					x = blockPos.getX();
					y = blockPos.getY();
					z = blockPos.getZ();
				}

				final SoundManager soundManager = Minecraft.getInstance().getSoundManager();
				if (!soundManager.isActive(this)) {
					soundManager.play(this);
				}
			}
		}
	}
}
