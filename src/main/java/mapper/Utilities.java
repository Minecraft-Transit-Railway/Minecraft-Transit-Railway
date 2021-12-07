package mapper;

import mtr.MTR;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Utilities {

	static <T extends BlockEntityMapper> BlockEntityType<T> registerTileEntity(String path, Supplier<T> supplier, Block block) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, MTR.MOD_ID + ":" + path, BlockEntityType.Builder.create(supplier, block).build(null));
	}

	static <T extends BlockEntityMapper> BlockEntityType<T> registerTileEntity(String path, TileEntitySupplier<T> supplier, Block block) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, MTR.MOD_ID + ":" + path, BlockEntityType.Builder.create(() -> supplier.supplier(null, null), block).build(null));
	}

//	static <T extends BlockEntity> BlockEntityType<T> registerTileEntity(String path, Supplier<T> supplier, Block block) {
//		return Registry.register(Registry.BLOCK_ENTITY_TYPE, MTR.MOD_ID + ":" + path, BlockEntityType.Builder.create(supplier, block).build(null));
//	}

	static float getYaw(Entity entity) {
		return entity.yaw;
	}

	static void setYaw(Entity entity, float yaw) {
		entity.yaw = yaw;
	}

	static void incrementYaw(Entity entity, float yaw) {
		entity.yaw += yaw;
	}

	static NbtCompound getOrCreateNbt(ItemStack itemStack) {
		return itemStack.getOrCreateTag();
	}

	static boolean isHolding(PlayerEntity player, Function<Item, Boolean> predicate) {
		return player.isHolding(predicate::apply);
	}

	static Inventory getInventory(PlayerEntity player) {
		return player.inventory;
	}

	@FunctionalInterface
	interface TileEntitySupplier<T extends BlockEntityMapper> {
		T supplier(BlockPos pos, BlockState state);
	}
}
