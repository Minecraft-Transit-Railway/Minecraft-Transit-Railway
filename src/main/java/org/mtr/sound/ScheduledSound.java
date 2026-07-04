package org.mtr.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.jspecify.annotations.Nullable;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;

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
		final ClientLevel clientWorld = Minecraft.getInstance().level;
		if (clientWorld != null) {
			SCHEDULED_SOUNDS.values().forEach(scheduledSound -> clientWorld.playLocalSound(blockPos, soundEvent, SoundSource.BLOCKS, gain, pitch, false));
		}
	}

	public static void schedule(BlockPos blockPos, @Nullable SoundEvent soundEvent, float gain, float pitch) {
		final LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;
		if (soundEvent != null && clientPlayerEntity != null) {
//? if >= 1.21.4 {
			final String currentKey = String.format("%s_%s_%s", soundEvent.location(), gain, pitch);
//? } else {
		/*final String currentKey = String.format("%s_%s_%s", soundEvent.getLocation(), gain, pitch);
//
*///? }

			final ScheduledSound scheduledSound = SCHEDULED_SOUNDS.computeIfAbsent(currentKey, key -> new ScheduledSound(blockPos, soundEvent, gain, pitch));
			final BlockPos clientPos = clientPlayerEntity.blockPosition();
			if (blockPos.distManhattan(clientPos) < scheduledSound.blockPos.distManhattan(clientPos)) {
				scheduledSound.blockPos = blockPos;
			}
		}
	}

	public static void playScheduledSounds() {
		SCHEDULED_SOUNDS.values().forEach(ScheduledSound::play);
		SCHEDULED_SOUNDS.clear();
	}
}
