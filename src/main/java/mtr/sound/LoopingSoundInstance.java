package mtr.sound;

import mtr.MTR;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class LoopingSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {

	private static final int MAX_DISTANCE = 32;

	public LoopingSoundInstance(String soundId) {
		super(new SoundEvent(new Identifier(MTR.MOD_ID, soundId)), SoundCategory.BLOCKS);
		repeat = true;
	}

	@Override
	public boolean isDone() {
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
			final ClientPlayerEntity player = MinecraftClient.getInstance().player;
			if (player == null) {
				return;
			}

			final BlockPos playerPos = player.getBlockPos();
			final int distance = playerPos.getManhattanDistance(pos);

			if (distance <= MAX_DISTANCE) {
				final int currentDistance = playerPos.getManhattanDistance(new BlockPos(x, y, z));

				if (distance < currentDistance) {
					x = pos.getX();
					y = pos.getY();
					z = pos.getZ();
				}

				final SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
				if (soundManager != null && !soundManager.isPlaying(this)) {
					soundManager.play(this);
				}
			}
		}
	}
}
