package org.mtr.neoforge;

//? if neoforge {

/*import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mtr.MTR;

@Mod(MTR.MOD_ID)
public final class MTRNeoForge {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MTR.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MTR.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MTR.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MTR.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> ITEM_GROUPS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MTR.MOD_ID);
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MTR.MOD_ID);
	public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MTR.MOD_ID);

	public MTRNeoForge(IEventBus eventBus) {
		MTR.init();
		BLOCKS.register(eventBus);
		ITEMS.register(eventBus);
		BLOCK_ENTITY_TYPES.register(eventBus);
		ENTITY_TYPES.register(eventBus);
		ITEM_GROUPS.register(eventBus);
		SOUND_EVENTS.register(eventBus);
		DATA_COMPONENT_TYPES.register(eventBus);
	}
}

*///? }
