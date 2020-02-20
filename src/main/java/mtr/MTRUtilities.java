package mtr;

import mtr.block.BlockPSDAPGBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MTRUtilities {

	public static final int DOOR_DURATION_MILLISECONDS = 2000;
	public static final int DOOR_DURATION_TICKS = DOOR_DURATION_MILLISECONDS / 50;

	/** Returns whichever one of the two angles (a1 or a2) is closer to angle. */
	public static double findCloserAngle(double angle, double a1, double a2) {
		double b1 = Math.abs(a1 - angle), b2 = Math.abs(a2 - angle);
		if (b1 > 180)
			b1 = 360 - b1;
		if (b2 > 180)
			b2 = 360 - b2;
		if (b2 < b1)
			return a2;
		else
			return a1;
	}

	/** Returns the geometric wrapped angle difference of a1 minus a2. */
	public static double angleDifference(double a1, double a2) {
		if (a1 < 0)
			a1 += 360;
		if (a2 < 0)
			a2 += 360;
		if (a1 - a2 > 180)
			a1 -= 360;
		else if (a2 - a1 > 180)
			a2 -= 360;
		return a1 - a2;
	}

	/** Returns the angle (between 0 and 2*PI) between two points. */
	public static double angleBetweenPoints(double xCentre, double zCentre, double x2, double z2) {
		final double distance = distanceBetweenPoints(xCentre, zCentre, x2, z2);
		if (distance == 0)
			return 0;
		double a = Math.acos((zCentre - z2) / distance);
		if (xCentre < x2)
			a = 2 * Math.PI - a;
		return a;
	}

	/** Returns the distance between two points. */
	public static double distanceBetweenPoints(double x1, double z1, double x2, double z2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(z1 - z2, 2));
	}

	/** Returns the distance between two points. */
	public static double distanceBetweenPoints(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
	}

	/** Returns the distance between two BlockPos. */
	public static double distanceBetweenPoints(BlockPos pos1, BlockPos pos2) {
		return distanceBetweenPoints(pos1.getX(), pos1.getZ(), pos2.getX(), pos2.getZ());
	}

	public static int roundToNearest45(double angle) {
		if (angle >= 337.5 || angle < 22.5)
			return 0;
		else if (angle >= 22.5 && angle < 67.5)
			return 45;
		else if (angle >= 67.5 && angle < 112.5)
			return 90;
		else if (angle >= 112.5 && angle < 157.5)
			return 135;
		else if (angle >= 157.5 && angle < 202.5)
			return 180;
		else if (angle >= 202.5 && angle < 247.5)
			return 225;
		else if (angle >= 247.5 && angle < 292.5)
			return 270;
		else if (angle >= 292.5 && angle < 337.5)
			return 315;
		return 0;
	}

	public static double[] circleIntersection(double h1, double k1, double r1, double h2, double k2, double r2) {
		final double d2 = Math.pow(h2 - h1, 2) + Math.pow(k2 - k1, 2);
		final double K = 0.25 * Math.sqrt((Math.pow(r1 + r2, 2) - d2) * (d2 - Math.pow(r1 - r2, 2)));
		final double x1 = 0.5 * (h2 + h1) + 0.5 * (h2 - h1) * (Math.pow(r1, 2) - Math.pow(r2, 2)) / d2 + 2 * (k2 - k1) * K / d2;
		final double x2 = 0.5 * (h2 + h1) + 0.5 * (h2 - h1) * (Math.pow(r1, 2) - Math.pow(r2, 2)) / d2 - 2 * (k2 - k1) * K / d2;
		final double y1 = 0.5 * (k2 + k1) + 0.5 * (k2 - k1) * (Math.pow(r1, 2) - Math.pow(r2, 2)) / d2 - 2 * (h2 - h1) * K / d2;
		final double y2 = 0.5 * (k2 + k1) + 0.5 * (k2 - k1) * (Math.pow(r1, 2) - Math.pow(r2, 2)) / d2 + 2 * (h2 - h1) * K / d2;
		final double[] a = { x1, y1, x2, y2 };
		return a;
	}

	public static boolean blocksAreReplacable(World worldIn, BlockPos pos, EnumFacing facing, int width, int height) {
		for (int x = 0; x < width; x++) {
			final BlockPos offsetPos = pos.offset(facing.rotateY(), x);
			final boolean isPSDAPGBelow = worldIn.getBlockState(offsetPos.down()).getBlock() instanceof BlockPSDAPGBase;
			final boolean isPSDAPGAbove = worldIn.getBlockState(offsetPos.up(2)).getBlock() instanceof BlockPSDAPGBase;

			for (int y = 0; y < height; y++) {
				final boolean isReplacable = worldIn.getBlockState(offsetPos.up(y)).getMaterial().isReplaceable();

				if (!isReplacable || isPSDAPGBelow || isPSDAPGAbove)
					return false;
			}
		}

		return true;
	}

	public static Item getItemFromPlayer(EntityPlayer player, EnumHand hand) {
		final ItemStack stack = player.getHeldItem(hand);
		if (stack != null)
			return stack.getItem();
		else
			return null;
	}

	@SideOnly(Side.CLIENT)
	public static Tuple updateDoor(boolean open, float door, long doorTime) {
		long newDoorTime;
		if (open) {
			if (door < 1) {
				newDoorTime = System.currentTimeMillis();
				if (newDoorTime - doorTime > DOOR_DURATION_MILLISECONDS)
					doorTime = newDoorTime;
				door += (newDoorTime - doorTime) / (float) DOOR_DURATION_MILLISECONDS;
			} else {
				newDoorTime = 0;
				door = 1;
			}
		} else {
			if (door > 0) {
				newDoorTime = System.currentTimeMillis();
				if (newDoorTime - doorTime > DOOR_DURATION_MILLISECONDS)
					doorTime = newDoorTime;
				door -= (newDoorTime - doorTime) / (float) DOOR_DURATION_MILLISECONDS;
			} else {
				newDoorTime = 0;
				door = 0;
			}
		}
		return new Tuple(door, newDoorTime);
	}

	@SideOnly(Side.CLIENT)
	public static ModelRenderer part(ModelBase model, int texOffsetX, int texOffsetZ, float offX, float offY, float offZ, int width, int height, int depth, float rotX, float rotY, float rotZ, int texSizeX, int texSizeZ) {
		final ModelRenderer part = new ModelRenderer(model, texOffsetX, texOffsetZ);
		part.addBox(offX, offY, offZ, width, height, depth);
		part.setRotationPoint(rotX, rotY, rotZ);
		part.setTextureSize(texSizeX, texSizeZ);
		part.mirror = true;
		return part;
	}
}
