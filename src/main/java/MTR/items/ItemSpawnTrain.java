package MTR.items;

import java.util.UUID;

import MTR.EntityTrainBase;
import MTR.ItemBase;
import MTR.blocks.BlockRailBooster;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpawnTrain<T extends EntityTrainBase> extends ItemBase {

	public ItemSpawnTrain(String[] name) {
		this(name, name[0]);
	}

	public ItemSpawnTrain(String[] name, String name2) {
		super(name, name2);
		maxStackSize = 1;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);
		if (state.getBlock() instanceof BlockRailBooster) {
			int angle = (-45 * state.getValue(BlockRailBooster.ROTATION) + 450) % 360;
			spawnTrain(worldIn, stack, pos, angle);
			return EnumActionResult.PASS;
		} else
			return EnumActionResult.FAIL;
	}

	protected void spawnTrain(World worldIn, ItemStack stack, BlockPos pos, int angle) {
		double a2 = Math.toRadians(angle - 90);
		int cars = getCars(stack);
		int[] order = getTrainOrder(stack);
		T[] trains = (T[]) new EntityTrainBase[cars * 2];
		int count = 0;
		for (int i = 0; i < cars; i++) {
			int j = 2 * i;
			trains[j] = createTrain(worldIn, pos.getX() + 0.5 + count * Math.sin(a2), pos.getY() + 0.0625,
					pos.getZ() + 0.5 + count * Math.cos(a2), true, order[i]);
			count += count > 100 ? 0.1 : trains[0].getTrainLength();
			trains[j + 1] = createTrain(worldIn, pos.getX() + 0.5 + count * Math.sin(a2), pos.getY() + 0.0625,
					pos.getZ() + 0.5 + count * Math.cos(a2), false, order[i]);
			count += count > 100 ? 0.1 : 9;
		}
		if (!worldIn.isRemote)
			for (int i = 0; i < cars; i++) {
				int j = 2 * i;
				trains[j].setID(trains[j + 1].getUniqueID(), j > 0 ? trains[j - 1].getUniqueID() : new UUID(0, 0));
				trains[j].rotationYaw = angle;
				worldIn.spawnEntityInWorld(trains[j]);
				trains[j + 1].setID(trains[j].getUniqueID(),
						j + 2 < cars * 2 ? trains[j + 2].getUniqueID() : new UUID(0, 0));
				trains[j + 1].rotationYaw = angle;
				worldIn.spawnEntityInWorld(trains[j + 1]);
			}
	}

	protected T createTrain(World worldIn, double x, double y, double z, boolean f, int h) {
		return null;
	}

	protected int getCars(ItemStack stack) {
		return 1;
	}

	protected int[] getTrainOrder(ItemStack stack) {
		int[] order = { 0 };
		return order;
	}
}