package MTR.items;

import java.util.List;

import MTR.ItemBase;
import MTR.MTRBlocks;
import MTR.blocks.BlockPSD;
import MTR.blocks.BlockPSDTop;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPSD extends ItemBase {

	private static final String[] name = { "ItemPSD", "ItemPSDDoor", "ItemPSDEnd", "ItemPSD2015" };

	public ItemPSD() {
		super(name);
		setHasSubtypes(true);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		switch (stack.getMetadata()) {
		case 0:
			list.add(I18n.format("gui.glass", new Object[0]));
			break;
		case 1:
			list.add(I18n.format("gui.door", new Object[0]));
			break;
		case 2:
			list.add(I18n.format("gui.glassend", new Object[0]));
			break;
		case 3:
			list.add(I18n.format("gui.glass", new Object[0]));
			list.add("ICS Class of 2015 :)");
		}
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facingSide, float hitX, float hitY, float hitZ) {
		IBlockState stateThis = worldIn.getBlockState(pos);
		if (!stateThis.getBlock().isReplaceable(worldIn, pos))
			pos = pos.offset(facingSide);
		BlockPos pos1Up = pos.add(0, 1, 0), pos2Up = pos.add(0, 2, 0);
		stateThis = worldIn.getBlockState(pos);
		IBlockState state1Up = worldIn.getBlockState(pos1Up), state2Up = worldIn.getBlockState(pos2Up);
		if (!stateThis.getBlock().isReplaceable(worldIn, pos) || !state1Up.getBlock().isReplaceable(worldIn, pos1Up)
				|| !state2Up.getBlock().isReplaceable(worldIn, pos2Up))
			return EnumActionResult.FAIL;
		EnumFacing var3 = EnumFacing.fromAngle(playerIn.rotationYaw).rotateY();
		BlockPos pos2 = pos, pos3 = pos;
		switch (var3) {
		case NORTH:
			pos2 = pos.add(0, 0, 1);
			pos3 = pos.add(0, 0, -1);
			break;
		case SOUTH:
			pos2 = pos.add(0, 0, -1);
			pos3 = pos.add(0, 0, 1);
			break;
		case EAST:
			pos2 = pos.add(-1, 0, 0);
			pos3 = pos.add(1, 0, 0);
			break;
		case WEST:
			pos2 = pos.add(1, 0, 0);
			pos3 = pos.add(-1, 0, 0);
			break;
		default:
		}
		IBlockState state2 = worldIn.getBlockState(pos2);
		Block block2 = state2.getBlock();
		PropertyBool SIDE = BlockPSD.SIDE, TOP = BlockPSD.TOP;
		PropertyDirection FACING = BlockPSD.FACING;
		PropertyDirection TOPFACING = BlockPSDTop.FACING;
		boolean side = false;
		if (block2 instanceof BlockPSD)
			side = !(Boolean) state2.getValue(SIDE);
		switch (stack.getMetadata()) {
		case 1: // door
			worldIn.setBlockState(pos, MTRBlocks.blockpsddoorclosed.getDefaultState().withProperty(FACING, var3)
					.withProperty(SIDE, side).withProperty(TOP, false));
			worldIn.setBlockState(pos1Up, MTRBlocks.blockpsddoorclosed.getDefaultState().withProperty(FACING, var3)
					.withProperty(SIDE, side).withProperty(TOP, true));
			break;
		case 0: // glass
		case 3: // glass 2015
			Block block = stack.getMetadata() == 0 ? MTRBlocks.blockpsdglass : MTRBlocks.blockpsdglass2015;
			if (playerIn.isSneaking()) {
				worldIn.setBlockState(pos, MTRBlocks.blockpsdglassmiddle.getDefaultState().withProperty(FACING, var3)
						.withProperty(SIDE, true).withProperty(TOP, false));
				worldIn.setBlockState(pos1Up, MTRBlocks.blockpsdglassmiddle.getDefaultState().withProperty(FACING, var3)
						.withProperty(SIDE, true).withProperty(TOP, true));
			} else {
				worldIn.setBlockState(pos, block.getDefaultState().withProperty(FACING, var3).withProperty(SIDE, side)
						.withProperty(TOP, false));
				worldIn.setBlockState(pos1Up, block.getDefaultState().withProperty(FACING, var3)
						.withProperty(SIDE, side).withProperty(TOP, true));
			}
			break;
		case 2: // glassend
			if (!(block2 instanceof BlockPSD) || playerIn.isSneaking()) {
				worldIn.setBlockState(pos, MTRBlocks.blockpsdglassveryend.getDefaultState().withProperty(FACING, var3)
						.withProperty(SIDE, side).withProperty(TOP, false));
				worldIn.setBlockState(pos1Up, MTRBlocks.blockpsdglassveryend.getDefaultState()
						.withProperty(FACING, var3).withProperty(SIDE, side).withProperty(TOP, true));
			} else {
				worldIn.setBlockState(pos, MTRBlocks.blockpsdglassend.getDefaultState().withProperty(FACING, var3)
						.withProperty(SIDE, side).withProperty(TOP, false));
				worldIn.setBlockState(pos1Up, MTRBlocks.blockpsdglassend.getDefaultState().withProperty(FACING, var3)
						.withProperty(SIDE, side).withProperty(TOP, true));
			}
			break;
		}
		worldIn.setBlockState(pos2Up, MTRBlocks.blockpsdtop.getDefaultState().withProperty(TOPFACING, var3));
		--stack.stackSize;
		return EnumActionResult.FAIL;
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (int var4 = 0; var4 < 4; ++var4)
			subItems.add(new ItemStack(itemIn, 1, var4));
	}
}