package mapper;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.function.Function;

public interface Utilities {

	static <T extends BlockEntityMapper> BlockEntityType<T> registerTileEntity(String path, FabricBlockEntityTypeBuilder.Factory<T> factory, Block block) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, path, FabricBlockEntityTypeBuilder.create(factory, block).build());
	}

	static float getYaw(Entity entity) {
		return entity.getYaw();
	}

	static void setYaw(Entity entity, float yaw) {
		entity.setYaw(yaw);
	}

	static void incrementYaw(Entity entity, float yaw) {
		entity.setYaw(entity.getYaw() + yaw);
	}

	static NbtCompound getOrCreateNbt(ItemStack itemStack) {
		return itemStack.getOrCreateNbt();
	}

	static boolean isHolding(PlayerEntity player, Function<Item, Boolean> predicate) {
		return player.isHolding(itemStack -> predicate.apply(itemStack.getItem()));
	}

	static Inventory getInventory(PlayerEntity player) {
		return player.getInventory();
	}

	static void scheduleBlockTick(World world, BlockPos pos, Block block, int ticks) {
		world.createAndScheduleBlockTick(pos, block, ticks);
	}

	static boolean isScheduled(World world, BlockPos pos, Block block) {
		return world.getBlockTickScheduler().isTicking(pos, block);
	}
}
