package MTR;

import java.util.List;

import MTR.blocks.BlockRailBooster;
import MTR.items.ItemKillTrain;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class MTRDispenserBehavior {

	public static class DispenseKillTrain extends BehaviorDefaultDispenseItem {
		@Override
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			World worldIn = source.getWorld();
			BlockPos pos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
			if (!worldIn.isRemote && stack.getItem() instanceof ItemKillTrain) {
				List<Entity> e = worldIn.getEntitiesWithinAABB(EntityTrainBase.class,
						AxisAlignedBB.fromBounds(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1,
								pos.getY() + 1, pos.getZ() + 1));
				if (e.size() > 0) {
					EntityTrainBase entity = (EntityTrainBase) e.get(0);
					entity.killTrain();
				}
			}
			return stack;
		}
	}

	public static class DispenseLightRail1 extends BehaviorDefaultDispenseItem {
		@Override
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			World worldIn = source.getWorld();
			BlockPos pos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
			if (!worldIn.isRemote && worldIn.getBlockState(pos).getBlock() instanceof BlockRailBooster)
				MTR.itemlightrail1.onItemUse(stack, null, worldIn, pos, EnumFacing.getFront(source.getBlockMetadata()),
						pos.getX(), pos.getY(), pos.getZ());
			return stack;
		}
	}

	public static class DispenseSP1900 extends BehaviorDefaultDispenseItem {
		@Override
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			World worldIn = source.getWorld();
			BlockPos pos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
			if (!worldIn.isRemote && worldIn.getBlockState(pos).getBlock() instanceof BlockRailBooster)
				MTR.itemsp1900.onItemUse(stack, null, worldIn, pos, EnumFacing.getFront(source.getBlockMetadata()),
						pos.getX(), pos.getY(), pos.getZ());
			return stack;
		}
	}
}
