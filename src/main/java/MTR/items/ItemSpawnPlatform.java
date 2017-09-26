package MTR.items;

import java.util.List;

import MTR.ItemBase;
import MTR.MTRBlocks;
import MTR.TileEntityRailBoosterEntity;
import MTR.blocks.BlockPlatform;
import MTR.blocks.BlockRailBooster;
import MTR.blocks.BlockRailStation;
import MTR.blocks.BlockRailStraight;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemSpawnPlatform extends ItemBase {

	private static final String[] name = { "ItemSpawnPlatform1", "ItemSpawnPlatform2", "ItemSpawnPlatform3" };
	private static final String name2 = "ItemSpawnPlatform";

	public ItemSpawnPlatform() {
		super(name, name2);
		setHasSubtypes(true);
		maxStackSize = 1;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		switch (stack.getMetadata()) {
		case 0:
			list.add(I18n.format("gui.spawnp1", new Object[0]));
			break;
		case 1:
			list.add(I18n.format("gui.spawnp2", new Object[0]));
			break;
		case 2:
			list.add(I18n.format("gui.spawnp3", new Object[0]));
			break;
		}
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos))
			pos = pos.offset(facing);
		switch (stack.getMetadata()) {
		case 0:
			spawnRails(worldIn, pos, playerIn);
			break;
		case 1:
			spawnPlatformBlock(worldIn, pos, playerIn, false);
			break;
		case 2:
			spawnPlatformBlock(worldIn, pos, playerIn, true);
			break;
		}
		return EnumActionResult.PASS;
	}

	private void spawnRails(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		EnumFacing facing = EnumFacing.fromAngle(playerIn.rotationYaw);
		int rotation = MathHelper.floor_double((playerIn.rotationYaw + 180.0F) * 8.0F / 360.0F + 0.5D) & 3;
		int rotation2 = MathHelper.floor_double(playerIn.rotationYaw * 8.0F / 360.0F + 0.5D) & 7;
		for (int i = 1; i < 50; i++)
			worldIn.setBlockState(pos.offset(facing, i),
					MTRBlocks.blockrailstraight.getDefaultState().withProperty(BlockRailStraight.ROTATION, rotation));
		worldIn.setBlockState(pos,
				MTRBlocks.blockrailstation.getDefaultState().withProperty(BlockRailStation.ROTATION, rotation));
		worldIn.setBlockState(pos.offset(facing, 4),
				MTRBlocks.blockrailbooster.getDefaultState().withProperty(BlockRailBooster.ROTATION, rotation2));
		worldIn.setBlockState(pos.offset(facing, 50),
				MTRBlocks.blockrailbooster.getDefaultState().withProperty(BlockRailBooster.ROTATION, rotation2));
		try {
			TileEntityRailBoosterEntity te1 = (TileEntityRailBoosterEntity) worldIn
					.getTileEntity(pos.offset(facing, 4));
			TileEntityRailBoosterEntity te2 = (TileEntityRailBoosterEntity) worldIn
					.getTileEntity(pos.offset(facing, 50));
			te1.speedBoost = 50;
			te1.speedSlow = 10;
			te2.speedBoost = 50;
			te2.speedSlow = 50;
		} catch (Exception e) {
		}
	}

	private void spawnPlatformBlock(World worldIn, BlockPos pos, EntityPlayer playerIn, boolean right) {
		EnumFacing facing = EnumFacing.fromAngle(playerIn.rotationYaw);
		for (int i = 0; i < 16; i++) {
			worldIn.setBlockState(pos.offset(facing, i), MTRBlocks.blockplatform.getDefaultState()
					.withProperty(BlockPlatform.FACING, right ? facing.getOpposite() : facing));
			worldIn.setBlockState(pos.offset(facing, i).down(), Blocks.REDSTONE_WIRE.getDefaultState());
		}
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (int var4 = 0; var4 < 3; ++var4)
			subItems.add(new ItemStack(itemIn, 1, var4));
	}
}
