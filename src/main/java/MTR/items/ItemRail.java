package MTR.items;

import java.util.List;

import MTR.ItemBase;
import MTR.MTRBlocks;
import MTR.MathTools;
import MTR.TileEntityRailEntity;
import MTR.blocks.BlockRailBase2;
import MTR.blocks.BlockRailBooster;
import MTR.blocks.BlockRailCurved;
import MTR.blocks.BlockRailDetector2;
import MTR.blocks.BlockRailDummy;
import MTR.blocks.BlockRailMarker;
import MTR.blocks.BlockRailReverse;
import MTR.blocks.BlockRailSlope1;
import MTR.blocks.BlockRailSlope2;
import MTR.blocks.BlockRailStation;
import MTR.blocks.BlockRailStraight;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemRail extends ItemBase {

	private static final String[] name = { "ItemRailNormal", "ItemRailBooster", "ItemRailSlopeUp", "ItemRailSlopeDown",
			"ItemRailDetector", "ItemRailStation", "ItemRailReverse", "ItemRailIntersection" };

	public ItemRail() {
		super(name);
		maxStackSize = 1;
		setHasSubtypes(true);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		switch (stack.getMetadata()) {
		case 0:
			list.add(I18n.format("gui.railnormal", new Object[0]));

			NBTTagCompound t = stack.getTagCompound();
			if (t != null)
				list.add(I18n.format("gui.connect", new Object[0]) + " " + t.getInteger("trackX") + ", "
						+ t.getInteger("trackY") + ", " + t.getInteger("trackZ"));
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
		case 6:
			list.add(I18n.format("gui.railreverse", new Object[0]));
			break;
		case 7:
			list.add(I18n.format("gui.railintersection", new Object[0]));
			break;
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		itemStackIn.setTagCompound(null);
		if (worldIn.isRemote)
			playerIn.addChatComponentMessage(new TextComponentString(I18n.format("gui.ready", new Object[0])));
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState stateThis = worldIn.getBlockState(pos);
		if (!stateThis.getBlock().isReplaceable(worldIn, pos))
			pos = pos.offset(facing);
		stateThis = worldIn.getBlockState(pos);
		if (!stateThis.getBlock().isReplaceable(worldIn, pos))
			return EnumActionResult.FAIL;
		int rotation = MathHelper.floor_double((playerIn.rotationYaw + 180.0F) * 8.0F / 360.0F + 0.5D) & 3;
		int rotation2 = MathHelper.floor_double((playerIn.rotationYaw + 180.0F) * 8.0F / 360.0F + 0.5D) & 7;
		switch (stack.getMetadata()) {
		case 0:
			placeNormalRails(stack, playerIn, worldIn, pos, rotation, rotation2);
			break;
		case 1:
			worldIn.setBlockState(pos,
					MTRBlocks.blockrailbooster.getDefaultState()
							.withProperty(BlockRailBooster.POWERED, worldIn.isBlockPowered(pos))
							.withProperty(BlockRailBooster.ROTATION, rotation2));
			break;
		case 2:
			placeSlopeRails(worldIn, pos, rotation2, true);
			break;
		case 3:
			placeSlopeRails(worldIn, pos, rotation2, false);
			break;
		case 4:
			worldIn.setBlockState(pos,
					MTRBlocks.blockraildetector.getDefaultState().withProperty(BlockRailDetector2.ROTATION, rotation));
			break;
		case 5:
			worldIn.setBlockState(pos,
					MTRBlocks.blockrailstation.getDefaultState().withProperty(BlockRailStation.ROTATION, rotation));
			break;
		case 6:
			worldIn.setBlockState(pos,
					MTRBlocks.blockrailreverse.getDefaultState().withProperty(BlockRailReverse.ROTATION, rotation));
			break;
		case 7:
			worldIn.setBlockState(pos, MTRBlocks.blockrailintersection.getDefaultState());
			break;
		}
		return EnumActionResult.SUCCESS;
	}

	private void placeNormalRails(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, int i1,
			int rotation2) {
		// place block
		int x1 = pos.getX(), y1 = pos.getY(), z1 = pos.getZ();
		worldIn.setBlockState(pos,
				MTRBlocks.blockrailmarker.getDefaultState().withProperty(BlockRailMarker.ROTATION, i1));

		if (stack.getTagCompound() == null) {
			// if no previous block
			stack.setTagCompound(new NBTTagCompound());
			NBTTagCompound t = stack.getTagCompound();
			// set item data to current coords
			t.setInteger("trackX", x1);
			t.setInteger("trackY", y1);
			t.setInteger("trackZ", z1);
			if (worldIn.isRemote)
				playerIn.addChatComponentMessage(new TextComponentString(I18n.format("gui.connect", new Object[0]) + " "
						+ t.getInteger("trackX") + ", " + t.getInteger("trackY") + ", " + t.getInteger("trackZ")));
		} else if (stack.getTagCompound().getInteger("trackY") == y1) {
			// if there is a previous block
			NBTTagCompound t = stack.getTagCompound();
			int x2 = t.getInteger("trackX");
			int y2 = t.getInteger("trackY");
			int z2 = t.getInteger("trackZ");
			BlockPos pos2 = new BlockPos(x2, y2, z2);
			IBlockState state = worldIn.getBlockState(pos2);
			int i2 = state.getValue(BlockRailBase2.ROTATION);
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
		if (r < 3 || r > 255) {
			if (worldIn.isRemote)
				playerIn.addChatComponentMessage(new TextComponentString(I18n.format("gui.railradius", new Object[0])));
			return;
		}
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
		boolean reverse = MathTools.findCloserAngle(angle2, angle1 + 1, angle1 - 1) == angle1 - 1;
		IBlockState curvedRail = MTRBlocks.blockrailcurved.getDefaultState();
		IBlockState dummyRail = MTRBlocks.blockraildummy.getDefaultState();
		double i = angle1;
		while (i != angle2) {
			i = Math.round((i + (reverse ? -0.01 : 0.01)) * 100) / 100D;
			if (i < 0)
				i += 360;
			if (i >= 360)
				i = i - 360;
			double a = Math.toRadians(i);
			int x = (int) Math.round(k + r * Math.sin(a));
			int z = (int) Math.round(h + r * Math.cos(a));
			worldIn.setBlockState(new BlockPos(x, y, z), dummyRail);
		}

		BlockPos pos1 = new BlockPos(x1, y, z1), pos2 = new BlockPos(x2, y, z2);
		int a = r % 16, b = Math.floorDiv(r, 16);
		BlockPos pos3 = pos1, pos4 = pos2;
		// pos3 = pos3.add(Math.signum(k - x1), 0, Math.signum(h - z1));
		// pos4 = pos4.add(Math.signum(k - x2), 0, Math.signum(h - z2));
		int angleDiff = (int) Math.round(MathTools.angleDifference(angle2, angle1));
		worldIn.setBlockState(pos1, curvedRail.withProperty(BlockRailCurved.ROTATION, i1));
		worldIn.setBlockState(pos2, curvedRail.withProperty(BlockRailCurved.ROTATION, i2));
		try {
			TileEntityRailEntity te1 = (TileEntityRailEntity) worldIn.getTileEntity(pos1);
			TileEntityRailEntity te2 = (TileEntityRailEntity) worldIn.getTileEntity(pos2);
			te1.radius = te2.radius = r;
			te1.xc = te2.xc = k;
			te1.zc = te2.zc = h;
			te1.startAngle = angle1;
			te2.startAngle = angle2;
			te1.angleChange = angleDiff;
			te2.angleChange = -te1.angleChange;
		} catch (Exception e) {
		}
	}

	private void connectWithStraightRails(World worldIn, int x1, int z1, int x2, int z2, int y, int i) {
		boolean reverse1 = z2 > z1;
		boolean reverse2 = x2 > x1;
		switch (i) {
		case 0:
			for (int j = reverse1 ? z1 : z2; j <= (reverse1 ? z2 : z1); j++)
				placeRails(worldIn, new BlockPos(x1, y, j), i);
			break;
		case 2:
			for (int j = reverse2 ? x1 : x2; j <= (reverse2 ? x2 : x1); j++)
				placeRails(worldIn, new BlockPos(j, y, z1), i);
			break;
		case 1:
			for (int j = 0; j <= Math.abs(z2 - z1); j++) {
				int x = (reverse2 ? x1 : x2) + j;
				int z = (reverse1 ? z2 : z1) - j;
				placeRails(worldIn, new BlockPos(x, y, z), i);
			}
			break;
		case 3:
			for (int j = 0; j <= Math.abs(z2 - z1); j++) {
				int x = (reverse2 ? x1 : x2) + j;
				int z = (reverse1 ? z1 : z2) + j;
				placeRails(worldIn, new BlockPos(x, y, z), i);
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
			worldIn.setBlockState(pos.add(i * x, 1, i * z), MTRBlocks.blockrailslope2.getDefaultState()
					.withProperty(BlockRailSlope2.ROTATION, up ? rotation : (rotation + 4) % 8));
			worldIn.setBlockState(pos.add(i * x, 0, i * z),
					MTRBlocks.blockrailslope1.getDefaultState().withProperty(BlockRailSlope1.LEVEL, up ? i : 15 - i));
		}
	}

	private void placeRails(World worldIn, BlockPos pos, int rotation) {
		IBlockState straightRail = MTRBlocks.blockrailstraight.getDefaultState()
				.withProperty(BlockRailStraight.ROTATION, rotation);
		Block block = worldIn.getBlockState(pos).getBlock();
		if (block instanceof BlockRailDummy)
			worldIn.setBlockState(pos,
					MTRBlocks.blockraildummy.getDefaultState().withProperty(BlockRailDummy.ROTATION, rotation + 4));
		else
			worldIn.setBlockState(pos,
					block instanceof BlockRailBase2 ? MTRBlocks.blockrailintersection.getDefaultState() : straightRail);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (stack.getTagCompound() != null) {
			NBTTagCompound t = stack.getTagCompound();
			int x = t.getInteger("trackX");
			int y = t.getInteger("trackY");
			int z = t.getInteger("trackZ");
			BlockPos pos = new BlockPos(x, y, z);
			if (!(worldIn.getBlockState(pos).getBlock() instanceof BlockRailMarker))
				stack.setTagCompound(null);
		}
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (int var4 = 0; var4 < 8; ++var4)
			subItems.add(new ItemStack(itemIn, 1, var4));
	}
}
