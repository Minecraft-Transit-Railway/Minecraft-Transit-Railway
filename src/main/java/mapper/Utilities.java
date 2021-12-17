package mapper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.function.Function;

public abstract class Utilities {

	public static <T extends BlockEntityMapper> BlockEntityType<T> getBlockEntityType(TileEntitySupplier<T> supplier, Block block) {
		return new BlockEntityType<>(() -> supplier.supplier(null, null), Collections.singleton(block), null);
	}

	public static float getYaw(Entity entity) {
		return entity.yRot;
	}

	public static void setYaw(Entity entity, float yaw) {
		entity.yRot = yaw;
	}

	public static void incrementYaw(Entity entity, float yaw) {
		entity.yRot += yaw;
	}

	public static boolean isHolding(Player player, Function<Item, Boolean> predicate) {
		return player.isHolding(predicate::apply);
	}

	public static Inventory getInventory(Player player) {
		return player.inventory;
	}

	@FunctionalInterface
	public interface TileEntitySupplier<T extends BlockEntityMapper> {
		T supplier(BlockPos pos, BlockState state);
	}
}
