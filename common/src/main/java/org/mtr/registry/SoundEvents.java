package org.mtr.registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.mtr.MTR;

public final class SoundEvents {

	static {
		TICKET_BARRIER = registerSoundEvent("ticket_barrier");
		TICKET_BARRIER_CONCESSIONARY = registerSoundEvent("ticket_barrier_concessionary");
		TICKET_PROCESSOR_ENTRY = registerSoundEvent("ticket_processor_entry");
		TICKET_PROCESSOR_ENTRY_CONCESSIONARY = registerSoundEvent("ticket_processor_entry_concessionary");
		TICKET_PROCESSOR_EXIT = registerSoundEvent("ticket_processor_exit");
		TICKET_PROCESSOR_EXIT_CONCESSIONARY = registerSoundEvent("ticket_processor_exit_concessionary");
		TICKET_PROCESSOR_FAIL = registerSoundEvent("ticket_processor_fail");
	}

	public static final ObjectHolder<SoundEvent> TICKET_BARRIER;
	public static final ObjectHolder<SoundEvent> TICKET_BARRIER_CONCESSIONARY;
	public static final ObjectHolder<SoundEvent> TICKET_PROCESSOR_ENTRY;
	public static final ObjectHolder<SoundEvent> TICKET_PROCESSOR_ENTRY_CONCESSIONARY;
	public static final ObjectHolder<SoundEvent> TICKET_PROCESSOR_EXIT;
	public static final ObjectHolder<SoundEvent> TICKET_PROCESSOR_EXIT_CONCESSIONARY;
	public static final ObjectHolder<SoundEvent> TICKET_PROCESSOR_FAIL;

	public static void init() {
		MTR.LOGGER.info("Registering Minecraft Transit Railway sound events");
	}

	private static ObjectHolder<SoundEvent> registerSoundEvent(String registryName) {
		return Registry.registerSoundEvent(registryName, () -> SoundEvent.of(Identifier.of(MTR.MOD_ID, registryName)));
	}
}
