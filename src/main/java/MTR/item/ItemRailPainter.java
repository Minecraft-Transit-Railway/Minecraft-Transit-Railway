package mtr.item;

import javax.annotation.Nullable;

import mtr.Blocks;
import mtr.MathTools;
import mtr.block.BlockRailMarker;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRailPainter extends Item {

	private BlockPos startPos;

	public ItemRailPainter() {
		super();
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.TRANSPORTATION);
		addPropertyOverride(new ResourceLocation("connecting"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				return startPos == null ? 0 : 1;
			}
		});
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			if (!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos))
				pos = pos.offset(facing);
			if (!player.canPlayerEdit(pos, facing, player.getHeldItem(hand)) || !worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos))
				return EnumActionResult.FAIL;

			if (startPos == null || !(worldIn.getBlockState(startPos).getBlock() instanceof BlockRailMarker)) {
				worldIn.setBlockState(pos, Blocks.rail_marker.getDefaultState().withProperty(BlockRailMarker.FACING, BlockRailMarker.getStateFromPlayerRotation(player)).withProperty(BlockRailMarker.KEEP, true));
				startPos = pos;
				player.sendStatusMessage(new TextComponentTranslation("gui.rail_painter_connecting"), true);
			} else {
				paintTrack(worldIn, startPos, pos, worldIn.getBlockState(startPos).getValue(BlockRailMarker.FACING), BlockRailMarker.getStateFromPlayerRotation(player));
				startPos = null;
				player.sendStatusMessage(new TextComponentTranslation("gui.rail_painter_connected"), true);
			}
		}

		return EnumActionResult.SUCCESS;
	}

	private void paintTrack(World worldIn, BlockPos start, BlockPos end, boolean startXAxis, boolean endXAxis) {
		final int y = start.getY();
		if (startXAxis == endXAxis) {

		} else {
			final int diffX = end.getX() - start.getX();
			final int diffZ = end.getZ() - start.getZ();
			final int diffXAbs = Math.abs(diffX);
			final int diffZAbs = Math.abs(diffZ);
			final int straightEndX, straightEndZ;

			if (diffXAbs > diffZAbs) {
				if (endXAxis) {
					straightEndX = start.getX() + (int) Math.copySign(diffZAbs, diffX);
					straightEndZ = end.getZ();
					placeRailLine(worldIn, end.getX(), end.getZ(), straightEndX, straightEndZ, y);
					placeRailCircle(worldIn, straightEndX, start.getZ(), start.getX(), start.getZ(), straightEndX, straightEndZ, y);
				} else {
					straightEndX = end.getX() - (int) Math.copySign(diffZAbs, diffX);
					straightEndZ = start.getZ();
					placeRailLine(worldIn, start.getX(), start.getZ(), straightEndX, straightEndZ, y);
					placeRailCircle(worldIn, straightEndX, end.getZ(), end.getX(), end.getZ(), straightEndX, straightEndZ, y);
				}
			} else {
				if (endXAxis) {
					straightEndX = start.getX();
					straightEndZ = end.getZ() - (int) Math.copySign(diffXAbs, diffZ);
					placeRailLine(worldIn, start.getX(), start.getZ(), straightEndX, straightEndZ, y);
					placeRailCircle(worldIn, end.getX(), straightEndZ, end.getX(), end.getZ(), straightEndX, straightEndZ, y);
				} else {
					straightEndX = end.getX();
					straightEndZ = start.getZ() + (int) Math.copySign(diffXAbs, diffZ);
					placeRailLine(worldIn, end.getX(), end.getZ(), straightEndX, straightEndZ, y);
					placeRailCircle(worldIn, start.getX(), straightEndZ, start.getX(), start.getZ(), straightEndX, straightEndZ, y);
				}
			}
		}
	}

	private void placeRailLine(World worldIn, int x1, int z1, int x2, int z2, int y) {
		for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++)
			for (int z = Math.min(z1, z2); z <= Math.max(z1, z2); z++)
				worldIn.setBlockState(new BlockPos(x, y, z), Blocks.rail_scaffold.getDefaultState());
	}

	private void placeRailCircle(World worldIn, int centreX, int centreZ, int x1, int z1, int x2, int z2, int y) {
		double angle1 = MathTools.angleBetweenPoints(centreX, centreZ, x1, z1) + Math.PI;
		double angle2 = MathTools.angleBetweenPoints(centreX, centreZ, x2, z2) + Math.PI;

		if (angle1 - angle2 > Math.PI)
			angle2 += 2 * Math.PI;
		else if (angle2 - angle1 > Math.PI)
			angle1 += 2 * Math.PI;

		final int radius = Math.max(Math.abs(x1 - centreX), Math.abs(z1 - centreZ));

		for (double i = Math.min(angle1, angle2); i <= Math.max(angle1, angle2); i += 0.001) {
			final int x = (int) Math.floor(radius * Math.sin(i) + centreX + 0.5);
			final int z = (int) Math.floor(radius * Math.cos(i) + centreZ + 0.5);
			worldIn.setBlockState(new BlockPos(x, y, z), Blocks.rail_scaffold.getDefaultState());
		}
	}
}
