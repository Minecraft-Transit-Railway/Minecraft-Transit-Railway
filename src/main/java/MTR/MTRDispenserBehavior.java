package MTR;

import java.util.List;

import MTR.blocks.BlockRailBooster;
import MTR.items.ItemKillTrain;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MTRDispenserBehavior {

	public static class DispenseKillTrain extends BehaviorDefaultDispenseItem {
		@Override
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			World worldIn = source.getWorld();
			EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
			BlockPos pos = source.getBlockPos().offset(facing, 2);
			if (!worldIn.isRemote && stack.getItem() instanceof ItemKillTrain) {
				List<Entity> e = worldIn.getEntitiesWithinAABB(EntityTrainBase.class,
						new AxisAlignedBB(pos.north(2).east(2).up(3), pos.south(2).west(2).down()));
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
			EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
			BlockPos pos = source.getBlockPos().offset(facing);
			if (!(worldIn.getBlockState(pos).getBlock() instanceof BlockRailBooster))
				pos = source.getBlockPos().offset(facing, 2);
			if (!worldIn.isRemote && worldIn.getBlockState(pos).getBlock() instanceof BlockRailBooster)
				MTRItems.itemlightrail1.onItemUse(stack, null, worldIn, pos, EnumHand.MAIN_HAND, facing, pos.getX(),
						pos.getY(), pos.getZ());
			return stack;
		}
	}

	public static class DispenseSP1900 extends BehaviorDefaultDispenseItem {
		@Override
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			World worldIn = source.getWorld();
			EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
			BlockPos pos = source.getBlockPos().offset(facing);
			if (!(worldIn.getBlockState(pos).getBlock() instanceof BlockRailBooster))
				pos = source.getBlockPos().offset(facing, 2);
			if (!worldIn.isRemote && worldIn.getBlockState(pos).getBlock() instanceof BlockRailBooster)
				MTRItems.itemsp1900.onItemUse(stack, null, worldIn, pos, EnumHand.MAIN_HAND, facing, pos.getX(),
						pos.getY(), pos.getZ());
			return stack;
		}
	}

	public static class DispenseMTrain extends BehaviorDefaultDispenseItem {
		@Override
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			World worldIn = source.getWorld();
			EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
			BlockPos pos = source.getBlockPos().offset(facing);
			if (!(worldIn.getBlockState(pos).getBlock() instanceof BlockRailBooster))
				pos = source.getBlockPos().offset(facing, 2);
			if (!worldIn.isRemote && worldIn.getBlockState(pos).getBlock() instanceof BlockRailBooster)
				MTRItems.itemmtrain.onItemUse(stack, null, worldIn, pos, EnumHand.MAIN_HAND, facing, pos.getX(),
						pos.getY(), pos.getZ());
			return stack;
		}
	}
}
