package MTR;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRail extends Item {

	public static final String name1 = "ItemRailNormal";
	public static final String name2 = "ItemRailBooster";
	public static final String name3 = "ItemRailSlopeUp";
	public static final String name4 = "ItemRailSlopeDown";
	public static final String name5 = "ItemRailDetector";
	public static final String name6 = "ItemRailStation";

	public ItemRail() {
		maxStackSize = 1;
		setHasSubtypes(true);
		setCreativeTab(MTR.MTRtab);
		GameRegistry.registerItem(this, name1);
		setUnlocalizedName(name1);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		switch (stack.getMetadata()) {
		case 0:
			list.add(I18n.format("gui.railnormal", new Object[0]));

			NBTTagCompound t = stack.getTagCompound();
			if (t != null)
				list.add(I18n.format("gui.connect", new Object[0]) + " " + t.getInteger("posX") + ", "
						+ t.getInteger("posY") + ", " + t.getInteger("posZ"));
			else
				list.add(I18n.format("gui.ready", new Object[0]));

			break;
		case 1:
			list.add(I18n.format("gui.railbooster", new Object[0]));
			break;
		case 2:
			list.add(I18n.format("gui.railslopeup", new Object[0]));
			break;
		case 3:
			list.add(I18n.format("gui.railslopedown", new Object[0]));
			break;
		case 4:
			list.add(I18n.format("gui.raildetector", new Object[0]));
			break;
		case 5:
			list.add(I18n.format("gui.railstation", new Object[0]));
			break;
		}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		IBlockState stateThis = worldIn.getBlockState(pos);
		if (!stateThis.getBlock().isReplaceable(worldIn, pos))
			pos = pos.offset(side);
		stateThis = worldIn.getBlockState(pos);
		if (!stateThis.getBlock().isReplaceable(worldIn, pos))
			return false;
		int rotation = MathHelper.floor_double((playerIn.rotationYaw + 180.0F) * 8.0F / 360.0F + 0.5D) & 3;
		switch (stack.getMetadata()) {
		case 0:
			placeNormalRails(stack, playerIn, worldIn, pos, rotation);
			break;
		case 1:
			int rotation2 = MathHelper.floor_double((playerIn.rotationYaw + 180.0F) * 8.0F / 360.0F + 0.5D) & 7;
			worldIn.setBlockState(pos,
					MTR.blockrailbooster.getDefaultState()
							.withProperty(BlockRailBooster.POWERED, worldIn.isBlockPowered(pos))
							.withProperty(BlockRailBooster.ROTATION, rotation2));
			break;
		case 2:
			int rotation3 = MathHelper.floor_double((playerIn.rotationYaw + 180.0F) * 8.0F / 360.0F + 0.5D) & 7;
			placeSlopeRails(worldIn, pos, rotation3, true);
			break;
		case 3:
			int rotation4 = MathHelper.floor_double((playerIn.rotationYaw + 180.0F) * 8.0F / 360.0F + 0.5D) & 7;
			placeSlopeRails(worldIn, pos, rotation4, false);
			break;
		case 4:
			worldIn.setBlockState(pos,
					MTR.blockraildetector.getDefaultState().withProperty(BlockRailDetector2.ROTATION, rotation));
			break;
		case 5:
			worldIn.setBlockState(pos,
					MTR.blockrailstation.getDefaultState().withProperty(BlockRailStation.ROTATION, rotation));
			break;
		}
		return true;
	}

	private void placeNormalRails(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, int i1) {
		// place block
		int x1 = pos.getX(), y1 = pos.getY(), z1 = pos.getZ();
		worldIn.setBlockState(pos,
				MTR.blockrailstraight.getDefaultState().withProperty(BlockRailStraight.ROTATION, i1));

		if (stack.getTagCompound() == null) {
			// if no previous block
			stack.setTagCompound(new NBTTagCompound());
			NBTTagCompound t = stack.getTagCompound();
			// set item data to current coords
			t.setInteger("posX", x1);
			t.setInteger("posY", y1);
			t.setInteger("posZ", z1);
			if (worldIn.isRemote)
				playerIn.addChatComponentMessage(new ChatComponentText(I18n.format("gui.connect", new Object[0]) + " "
						+ t.getInteger("posX") + ", " + t.getInteger("posY") + ", " + t.getInteger("posZ")));
		} else if (stack.getTagCompound().getInteger("posY") == y1) {
			// if there is a previous block
			NBTTagCompound t = stack.getTagCompound();
			int x2 = t.getInteger("posX");
			int y2 = t.getInteger("posY");
			int z2 = t.getInteger("posZ");
			BlockPos pos2 = new BlockPos(x2, y2, z2);
			IBlockState state = worldIn.getBlockState(pos2);
			int i2 = (Integer) state.getValue(BlockRailBase2.ROTATION);
			int angle1 = (int) Math.round((-45D * i1 + 270D) % 180);
			int angle2 = (int) Math.round((-45D * i2 + 270D) % 180);

			if (i1 == i2) {
				; // connect with straight tracks
				if (i1 == 0 && x2 == x1 || i1 == 2 && z2 == z1 || i1 == 1 && z2 - z1 == x1 - x2
						|| i1 == 3 && z2 - z1 == x2 - x1)
					connectWithStraightRails(worldIn, x1, z1, x2, z2, y1, i1);
			} else {
				// 45 or 90 degree connection

				// x - x0 = m*(z - z0)
				// x - m1*z = -m1*z1 + x1
				// x - m2*z = -m2*z2 + x2
				int constants1[] = { 0, -1, 1000000, 1 }, constants2[] = { 1000000, 1, 0, -1 };
				int m1 = constants1[i1], m2 = constants1[i2];
				// determinant = a*d - c*b
				int d = m1 - m2;
				int dx = (-m1 * z1 + x1) * -m2 - (-m2 * z2 + x2) * -m1;
				int dz = -m2 * z2 + x2 - (-m1 * z1 + x1);
				// intercepts
				int xi = (int) Math.round((double) dx / (double) d), zi = (int) Math.round((double) dz / (double) d);
				double length1 = Math.sqrt(Math.pow(x1 - xi, 2) + Math.pow(z1 - zi, 2));
				double length2 = Math.sqrt(Math.pow(x2 - xi, 2) + Math.pow(z2 - zi, 2));
				// endpoint
				int x3, z3;
				if (length1 > length2) {
					double xd = length2 * Math.abs(Math.cos(angle1 * Math.PI / 180D));
					double zd = length2 * Math.abs(Math.sin(angle1 * Math.PI / 180D));
					x3 = xi + (int) (x1 > xi ? xd : -xd);
					z3 = zi + (int) (z1 > zi ? zd : -zd);
					connectWithStraightRails(worldIn, x1, z1, x3, z3, y1, i1);

					m1 = constants2[i1];
					m2 = constants2[i2];
					// determinant = a*d - c*b
					d = m1 - m2;
					dx = (-m1 * z3 + x3) * -m2 - (-m2 * z2 + x2) * -m1;
					dz = -m2 * z2 + x2 - (-m1 * z3 + x3);
					// intercepts
					xi = (int) Math.round((double) dx / (double) d);
					zi = (int) Math.round((double) dz / (double) d);
					int r = (int) Math.round(Math.sqrt(Math.pow(x2 - xi, 2) + Math.pow(z2 - zi, 2)));
					connectWithCurve(worldIn, playerIn, x2, z2, x3, z3, y2, i2, i1, r, zi, xi);
				} else {
					double xd = length1 * Math.abs(Math.cos(angle2 * Math.PI / 180D));
					double zd = length1 * Math.abs(Math.sin(angle2 * Math.PI / 180D));
					x3 = xi + (int) (x2 > xi ? xd : -xd);
					z3 = zi + (int) (z2 > zi ? zd : -zd);
					connectWithStraightRails(worldIn, x2, z2, x3, z3, y2, i2);

					m1 = constants2[i1];
					m2 = constants2[i2];
					// determinant = a*d - c*b
					d = m1 - m2;
					dx = (-m1 * z1 + x1) * -m2 - (-m2 * z3 + x3) * -m1;
					dz = -m2 * z3 + x3 - (-m1 * z1 + x1);
					// intercepts
					xi = (int) Math.round((double) dx / (double) d);
					zi = (int) Math.round((double) dz / (double) d);
					int r = (int) Math.round(Math.sqrt(Math.pow(x1 - xi, 2) + Math.pow(z1 - zi, 2)));
					connectWithCurve(worldIn, playerIn, x1, z1, x3, z3, y1, i1, i2, r, zi, xi);
				}
			}
			stack.setTagCompound(null);
		}
	}

	private void connectWithCurve(World worldIn, EntityPlayer playerIn, int x1, int z1, int x2, int z2, int y, int i1,
			int i2, int r, int h, int k) {
		if (r < 20 || r > 255) {
			if (worldIn.isRemote)
				playerIn.addChatComponentMessage(new ChatComponentText(I18n.format("gui.railradius", new Object[0])));
			return;
		}
		IBlockState curvedRail = MTR.blockrailcurved.getDefaultState();
		IBlockState dummyRail = MTR.blockraildummy.getDefaultState();
		int angle1 = (int) Math.round((-45D * i1 + 270D) % 180);
		int angle2 = (int) Math.round((-45D * i2 + 270D) % 180);
		if (x1 < k)
			angle1 += 180;
		if (angle1 == 0 && z1 < h)
			angle1 = 180;
		if (x2 < k)
			angle2 += 180;
		if (angle2 == 0 && z2 < h)
			angle2 = 180;
		boolean reverse = findCloserAngle(angle2, angle1 + 1, angle1 - 1) == angle1 - 1;
		double start = angle1;
		double i = start;
		while (i != angle2) {
			i = Math.round((i + (reverse ? -0.01 : 0.01)) * 100) / 100D;
			if (i < 0)
				i += 360;
			if (i >= 360)
				i = i - 360;
			double a = i * Math.PI / 180D;
			int x = (int) Math.round(k + r * Math.sin(a));
			int z = (int) Math.round(h + r * Math.cos(a));
			int a2 = (int) Math.round(i % 90 * 16D / 90D);
			if (a2 == 16)
				a2 = 0;
			worldIn.setBlockState(new BlockPos(x, y, z), dummyRail);
		}

		BlockPos pos1 = new BlockPos(x1, y, z1), pos2 = new BlockPos(x2, y, z2);
		int a = r % 16, b = Math.floorDiv(r, 16);
		BlockPos pos3 = pos1, pos4 = pos2;
		pos3 = pos3.add(Math.signum(k - x1), 0, Math.signum(h - z1));
		pos4 = pos4.add(Math.signum(k - x2), 0, Math.signum(h - z2));
		worldIn.setBlockState(pos1, curvedRail.withProperty(BlockRailCurved.ROTATION, a));
		worldIn.setBlockState(pos2, curvedRail.withProperty(BlockRailCurved.ROTATION, a));
		worldIn.setBlockState(pos3, curvedRail.withProperty(BlockRailCurved.ROTATION, b));
		worldIn.setBlockState(pos4, curvedRail.withProperty(BlockRailCurved.ROTATION, b));
	}

	private void connectWithStraightRails(World worldIn, int x1, int z1, int x2, int z2, int y, int i) {
		boolean reverse1 = z2 > z1;
		boolean reverse2 = x2 > x1;
		IBlockState straightRail = MTR.blockrailstraight.getDefaultState().withProperty(BlockRailStraight.ROTATION, i);
		switch (i) {
		case 0:
			for (int j = reverse1 ? z1 : z2; j <= (reverse1 ? z2 : z1); j++)
				worldIn.setBlockState(new BlockPos(x1, y, j), straightRail);
			break;
		case 2:
			for (int j = reverse2 ? x1 : x2; j <= (reverse2 ? x2 : x1); j++)
				worldIn.setBlockState(new BlockPos(j, y, z1), straightRail);
			break;
		case 1:
			for (int j = 0; j <= Math.abs(z2 - z1); j++) {
				int x = (reverse2 ? x1 : x2) + j;
				int z = (reverse1 ? z2 : z1) - j;
				worldIn.setBlockState(new BlockPos(x, y, z), straightRail);
			}
			break;
		case 3:
			for (int j = 0; j <= Math.abs(z2 - z1); j++) {
				int x = (reverse2 ? x1 : x2) + j;
				int z = (reverse1 ? z1 : z2) + j;
				worldIn.setBlockState(new BlockPos(x, y, z), straightRail);
			}
			break;
		}
	}

	private double[] rotateAngle(int pivotX, int pivotZ, double x, double z, int angle) {
		double r = Math.sqrt(Math.pow(x - pivotX, 2) + Math.pow(z - pivotZ, 2));
		double a = Math.acos((z - pivotZ) / r);
		if (x < pivotX)
			a = 2 * Math.PI - a;
		a = a + angle * Math.PI / 180D;
		double z2 = pivotZ + r * Math.cos(a);
		double x2 = pivotX + r * Math.sin(a);
		double result[] = { x2, z2 };
		return result;
	}

	private int findCloserAngle(double angle, int a1d, int a2d) {
		double b1 = Math.abs(a1d - angle), b2 = Math.abs(a2d - angle);
		if (b1 > 180)
			b1 = 360 - b1;
		if (b2 > 180)
			b2 = 360 - b2;
		if (b2 < b1)
			return a2d;
		else
			return a1d;
	}

	private void placeSlopeRails(World worldIn, BlockPos pos, int rotation, boolean up) {
		int x = 0, z = 0;
		switch (rotation) {
		case 0:
			z = -1;
			break;
		case 1:
			x = 1;
			z = -1;
			break;
		case 2:
			x = 1;
			break;
		case 3:
			x = 1;
			z = 1;
			break;
		case 4:
			z = 1;
			break;
		case 5:
			x = -1;
			z = 1;
			break;
		case 6:
			x = -1;
			break;
		case 7:
			x = -1;
			z = -1;
			break;
		}
		for (int i = 0; i < 16; i++) {
			worldIn.setBlockState(pos.add(i * x, 1, i * z), MTR.blockrailslope2.getDefaultState()
					.withProperty(BlockRailSlope2.ROTATION, up ? rotation : (rotation + 4) % 8));
			worldIn.setBlockState(pos.add(i * x, 0, i * z),
					MTR.blockrailslope1.getDefaultState().withProperty(BlockRailSlope1.LEVEL, up ? i : 15 - i));
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (stack.getTagCompound() != null) {
			NBTTagCompound t = stack.getTagCompound();
			int x = t.getInteger("posX");
			int y = t.getInteger("posY");
			int z = t.getInteger("posZ");
			BlockPos pos = new BlockPos(x, y, z);
			if (!(worldIn.getBlockState(pos).getBlock() instanceof BlockRailBase2))
				stack.setTagCompound(null);
		}
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (int var4 = 0; var4 < 6; ++var4)
			subItems.add(new ItemStack(itemIn, 1, var4));
	}

	public static String getName1() {
		return name1;
	}

	public static String getName2() {
		return name2;
	}

	public static String getName3() {
		return name3;
	}

	public static String getName4() {
		return name4;
	}

	public static String getName5() {
		return name5;
	}

	public static String getName6() {
		return name6;
	}
}
