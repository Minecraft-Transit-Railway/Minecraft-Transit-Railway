package mtr;

import mtr.mappings.RegistryUtilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public interface SoundEvents {

	SoundEvent TICKET_BARRIER = RegistryUtilities.createSoundEvent(new ResourceLocation(MTR.MOD_ID, "ticket_barrier"));
	SoundEvent TICKET_BARRIER_CONCESSIONARY = RegistryUtilities.createSoundEvent(new ResourceLocation(MTR.MOD_ID, "ticket_barrier_concessionary"));
	SoundEvent TICKET_PROCESSOR_ENTRY = RegistryUtilities.createSoundEvent(new ResourceLocation(MTR.MOD_ID, "ticket_processor_entry"));
	SoundEvent TICKET_PROCESSOR_ENTRY_CONCESSIONARY = RegistryUtilities.createSoundEvent(new ResourceLocation(MTR.MOD_ID, "ticket_processor_entry_concessionary"));
	SoundEvent TICKET_PROCESSOR_EXIT = RegistryUtilities.createSoundEvent(new ResourceLocation(MTR.MOD_ID, "ticket_processor_exit"));
	SoundEvent TICKET_PROCESSOR_EXIT_CONCESSIONARY = RegistryUtilities.createSoundEvent(new ResourceLocation(MTR.MOD_ID, "ticket_processor_exit_concessionary"));
	SoundEvent TICKET_PROCESSOR_FAIL = RegistryUtilities.createSoundEvent(new ResourceLocation(MTR.MOD_ID, "ticket_processor_fail"));
}
