package MTR.items;

import java.util.List;

import MTR.ItemBase;
import MTR.MTRBlocks;
import MTR.blocks.BlockEscalatorSide;
import MTR.blocks.BlockEscalatorStep;
import net.minecraft.block.Block;
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

public class ItemEscalator extends ItemBase {

	private static final String[] name = { "ItemEscalator", "ItemEscalator5", "ItemEscalator10", "ItemEscalator20",
			"ItemEscalator50" };
	private static final int a[] = { 1, 5, 10, 20, 50 };

	public ItemEscalator() {
		super(name);
		setHasSubtypes(true);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		switch (stack.getMetadata()) {
		case 1:
			list.add(I18n.format("gui.escalator5", new Object[0]));
			break;
		case 2:
			list.add(I18n.format("gui.escalator10", new Object[0]));
			break;
		case 3:
			list.add(I18n.format("gui.escalator20", new Object[0]));
			break;
		case 4:
			list.add(I18n.format("gui.escalator50", new Object[0]));
			break;
		default:
			break;
		}
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facingSide, float hitX, float hitY, float hitZ) {
		IBlockState var9 = worldIn.getBlockState(pos);
		Block var10 = var9.getBlock();
		if (!var10.isReplaceable(worldIn, pos))
			pos = pos.offset(facingSide);
		Block blockStep = MTRBlocks.blockescalatorstep;
		Block blockSide = MTRBlocks.blockescalatorside;
		EnumFacing facing = EnumFacing.fromAngle(playerIn.rotationYaw).rotateY();

		BlockPos posRight = pos;
		switch (facing) {
		case NORTH:
			posRight = pos.add(0, 0, -1);
			break;
		case SOUTH:
			posRight = pos.add(0, 0, 1);
			break;
		case EAST:
			posRight = pos.add(1, 0, 0);
			break;
		case WEST:
			posRight = pos.add(-1, 0, 0);
			break;
		default:
		}
		for (int i = 0; i < a[stack.getMetadata()]; i++) {
			if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)
					&& worldIn.getBlockState(pos.up()).getBlock().isReplaceable(worldIn, pos.up())
					&& worldIn.getBlockState(posRight).getBlock().isReplaceable(worldIn, posRight)
					&& worldIn.getBlockState(posRight.up()).getBlock().isReplaceable(worldIn, posRight.up())) {
				worldIn.setBlockState(pos, blockStep.getDefaultState().withProperty(BlockEscalatorStep.FACING, facing));
				worldIn.setBlockState(pos.up(), blockSide.getDefaultState()
						.withProperty(BlockEscalatorSide.FACING, facing).withProperty(BlockEscalatorSide.SIDE, false));
				worldIn.setBlockState(posRight,
						blockStep.getDefaultState().withProperty(BlockEscalatorStep.FACING, facing));
				worldIn.setBlockState(posRight.up(), blockSide.getDefaultState()
						.withProperty(BlockEscalatorSide.FACING, facing).withProperty(BlockEscalatorSide.SIDE, true));
			}
			pos = pos.offset(facing.rotateYCCW()).up();
			posRight = posRight.offset(facing.rotateYCCW()).up();
		}
		return EnumActionResult.PASS;
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (int var4 = 0; var4 < 5; ++var4)
			subItems.add(new ItemStack(itemIn, 1, var4));
	}
}
