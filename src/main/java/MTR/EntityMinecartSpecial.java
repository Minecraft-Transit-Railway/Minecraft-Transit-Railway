package MTR;

import MTR.blocks.BlockRailBase2;
import MTR.blocks.BlockRailDummy;
import MTR.blocks.BlockTrainTimer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityMinecartSpecial extends EntityTrainBase {

	public EntityMinecartSpecial(World world) {
		super(world);
		setSize(1F, 1F);
		name = "";
	}

	public EntityMinecartSpecial(World world, double x, double y, double z) {
		super(world, x, y, z);
		name = "";
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (riddenByEntity != null && riddenByEntity instanceof EntityPlayer) {
			ItemStack itemStack = ((EntityPlayer) riddenByEntity).getHeldItem();
			if (itemStack != null && itemStack.getItem() instanceof ItemPickaxe)
				if (!worldObj.isRemote)
					for (int a = -1; a <= 1; a++)
						for (int b = 0; b <= 3; b++)
							for (int c = -1; c <= 1; c++) {
								BlockPos pos = getPosition().add(a, b, c);
								if (!worldObj.isAirBlock(pos)) {
									Block block = worldObj.getBlockState(pos).getBlock();
									if (!(block instanceof BlockRailBase2) && !(block instanceof BlockRailDummy)
											&& !(block instanceof BlockTrainTimer) && block.isFullCube())
										worldObj.setBlockToAir(pos);
								}
							}
				else
					Minecraft.getMinecraft().ingameGUI.setRecordPlaying(I18n.format("gui.digging", new Object[0]),
							false);
		}
	}

	@Override
	public boolean interactFirst(EntityPlayer playerIn) {
		super.interactFirst(playerIn);
		if (!worldObj.isRemote && (playerIn.getHeldItem() == null
				|| playerIn.getHeldItem() != null && playerIn.getHeldItem().getItem() != MTR.itemkilltrain))
			playerIn.mountEntity(this);
		return true;
	}

	@Override
	public double getMountedYOffset() {
		return 0;
	}

	@Override
	protected int getTrainLength() {
		return 1;
	}
}