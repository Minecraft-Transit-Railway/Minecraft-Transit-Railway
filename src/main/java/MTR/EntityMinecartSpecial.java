package MTR;

import java.util.List;

import javax.annotation.Nullable;

import MTR.blocks.BlockRailBase2;
import MTR.blocks.BlockRailDummy;
import MTR.blocks.BlockTrainTimer;
import MTR.items.ItemKillTrain;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMinecartSpecial extends EntityTrainBase {

	public EntityMinecartSpecial(World world) {
		super(world);
		setSize(1F, 1F);
	}

	public EntityMinecartSpecial(World world, double x, double y, double z, boolean f, int h) {
		super(world, x, y, z, f, h);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		List<Entity> entity = getPassengers();
		if (isBeingRidden() && entity.size() > 0 && entity.get(0) instanceof EntityPlayer) {
			ItemStack itemStack1 = ((EntityPlayer) entity.get(0)).getHeldItemMainhand();
			ItemStack itemStack2 = ((EntityPlayer) entity.get(0)).getHeldItemOffhand();
			if (itemStack1 != null && itemStack1.getItem() instanceof ItemPickaxe
					|| itemStack2 != null && itemStack2.getItem() instanceof ItemPickaxe)
				if (!worldObj.isRemote)
					for (int a = -1; a <= 1; a++)
						for (int b = 0; b <= 3; b++)
							for (int c = -1; c <= 1; c++) {
								BlockPos pos = getPosition().add(a, b, c);
								if (!worldObj.isAirBlock(pos)) {
									IBlockState state = worldObj.getBlockState(pos);
									Block block = state.getBlock();
									if (!(block instanceof BlockRailBase2) && !(block instanceof BlockRailDummy)
											&& !(block instanceof BlockTrainTimer) && !(block instanceof BlockDispenser)
											&& state.getMaterial().isOpaque())
										worldObj.setBlockToAir(pos);
								}
							}
				else
					Minecraft.getMinecraft().ingameGUI.setRecordPlaying(I18n.format("gui.digging", new Object[0]),
							false);
		}
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, @Nullable ItemStack stack, EnumHand hand) {
		super.processInitialInteract(player, stack, hand);
		if (!worldObj.isRemote
				&& (stack.getItem() == null || stack.getItem() != null && !(stack.getItem() instanceof ItemKillTrain)))
			player.startRiding(this);
		return true;
	}

	@Override
	public double getMountedYOffset() {
		return 0;
	}

	@Override
	public int getTrainLength() {
		return 1;
	}
}