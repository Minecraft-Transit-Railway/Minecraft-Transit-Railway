package org.mtr.mod.sound;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.mapping.holder.*;

import javax.annotation.Nullable;

public final class ScheduledSound {

	private BlockPos blockPos;
	private final SoundEvent soundEvent;
	private final float gain;
	private final float pitch;

	private static final Object2ObjectAVLTreeMap<String, ScheduledSound> SCHEDULED_SOUNDS = new Object2ObjectAVLTreeMap<>();

	private ScheduledSound(BlockPos blockPos, SoundEvent soundEvent, float gain, float pitch) {
		this.blockPos = blockPos;
		this.soundEvent = soundEvent;
		this.gain = Math.min(1, gain);
		this.pitch = pitch;
	}

	private void play() {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld != null) {
			SCHEDULED_SOUNDS.values().forEach(scheduledSound -> clientWorld.playSoundAtBlockCenter(blockPos, soundEvent, SoundCategory.getBlocksMapped(), gain, pitch, false));
		}
	}

	public static void schedule(BlockPos blockPos, @Nullable SoundEvent soundEvent, float gain, float pitch) {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (soundEvent != null && clientPlayerEntity != null) {
			final String currentKey = String.format("%s_%s_%s", soundEvent.getId().data.toString(), gain, pitch);
			final ScheduledSound scheduledSound = SCHEDULED_SOUNDS.computeIfAbsent(currentKey, key -> new ScheduledSound(blockPos, soundEvent, gain, pitch));
			final Vector3i clientPos = new Vector3i(clientPlayerEntity.getBlockPos().data);
			if (blockPos.getManhattanDistance(clientPos) < scheduledSound.blockPos.getManhattanDistance(clientPos)) {
				scheduledSound.blockPos = blockPos;
			}
		}
	}

	public static void playScheduledSounds() {
		SCHEDULED_SOUNDS.values().forEach(ScheduledSound::play);
		SCHEDULED_SOUNDS.clear();
	}
}
