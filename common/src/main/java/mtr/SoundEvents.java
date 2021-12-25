package mtr;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public interface SoundEvents {

	SoundEvent TICKET_BARRIER = new SoundEvent(new ResourceLocation(MTR.MOD_ID, "ticket_barrier"));
	SoundEvent TICKET_BARRIER_CONCESSIONARY = new SoundEvent(new ResourceLocation(MTR.MOD_ID, "ticket_barrier_concessionary"));
	SoundEvent TICKET_PROCESSOR_ENTRY = new SoundEvent(new ResourceLocation(MTR.MOD_ID, "ticket_processor_entry"));
	SoundEvent TICKET_PROCESSOR_ENTRY_CONCESSIONARY = new SoundEvent(new ResourceLocation(MTR.MOD_ID, "ticket_processor_entry_concessionary"));
	SoundEvent TICKET_PROCESSOR_EXIT = new SoundEvent(new ResourceLocation(MTR.MOD_ID, "ticket_processor_exit"));
	SoundEvent TICKET_PROCESSOR_EXIT_CONCESSIONARY = new SoundEvent(new ResourceLocation(MTR.MOD_ID, "ticket_processor_exit_concessionary"));
	SoundEvent TICKET_PROCESSOR_FAIL = new SoundEvent(new ResourceLocation(MTR.MOD_ID, "ticket_processor_fail"));
}
