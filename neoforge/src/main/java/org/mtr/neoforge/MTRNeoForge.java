package org.mtr.neoforge;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
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
	public static final DeferredRegister<ItemGroup> ITEM_GROUPS = DeferredRegister.create(Registries.ITEM_GROUP, MTR.MOD_ID);
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MTR.MOD_ID);

	public MTRNeoForge(IEventBus eventBus) {
		MTR.init();
		BLOCKS.register(eventBus);
		ITEMS.register(eventBus);
		ENTITY_TYPES.register(eventBus);
		ITEM_GROUPS.register(eventBus);
		SOUND_EVENTS.register(eventBus);
	}
}
