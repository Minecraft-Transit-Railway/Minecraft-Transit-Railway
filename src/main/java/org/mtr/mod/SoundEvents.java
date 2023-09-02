package org.mtr.mod;

import org.mtr.init.MTR;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.registry.Registry;
import org.mtr.mapping.registry.SoundEventRegistryObject;

public final class SoundEvents {

	static {
		TICKET_BARRIER = Registry.registerSoundEvent(new Identifier(MTR.MOD_ID, "ticket_barrier"));
		TICKET_BARRIER_CONCESSIONARY = Registry.registerSoundEvent(new Identifier(MTR.MOD_ID, "ticket_barrier_concessionary"));
		TICKET_PROCESSOR_ENTRY = Registry.registerSoundEvent(new Identifier(MTR.MOD_ID, "ticket_processor_entry"));
		TICKET_PROCESSOR_ENTRY_CONCESSIONARY = Registry.registerSoundEvent(new Identifier(MTR.MOD_ID, "ticket_processor_entry_concessionary"));
		TICKET_PROCESSOR_EXIT = Registry.registerSoundEvent(new Identifier(MTR.MOD_ID, "ticket_processor_exit"));
		TICKET_PROCESSOR_EXIT_CONCESSIONARY = Registry.registerSoundEvent(new Identifier(MTR.MOD_ID, "ticket_processor_exit_concessionary"));
		TICKET_PROCESSOR_FAIL = Registry.registerSoundEvent(new Identifier(MTR.MOD_ID, "ticket_processor_fail"));
	}

	public static final SoundEventRegistryObject TICKET_BARRIER;
	public static final SoundEventRegistryObject TICKET_BARRIER_CONCESSIONARY;
	public static final SoundEventRegistryObject TICKET_PROCESSOR_ENTRY;
	public static final SoundEventRegistryObject TICKET_PROCESSOR_ENTRY_CONCESSIONARY;
	public static final SoundEventRegistryObject TICKET_PROCESSOR_EXIT;
	public static final SoundEventRegistryObject TICKET_PROCESSOR_EXIT_CONCESSIONARY;
	public static final SoundEventRegistryObject TICKET_PROCESSOR_FAIL;
}
