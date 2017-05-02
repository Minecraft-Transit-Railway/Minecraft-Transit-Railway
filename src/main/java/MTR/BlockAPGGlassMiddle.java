package MTR;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockAPGGlassMiddle extends BlockPSD {

	private static final String name = "BlockAPGGlassMiddle";

	protected BlockAPGGlassMiddle() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		EnumFacing var3 = access.getBlockState(pos).getValue(FACING);
		float height = 1F;
		if (access.getBlockState(pos).getValue(TOP))
			height = 0.5F;
		switch (var3) {
		case NORTH:
			setBlockBounds(0.0F, 0.0F, 0.0F, 0.125F, height, 1.0F);
			break;
		case SOUTH:
			setBlockBounds(0.875F, 0.0F, 0.0F, 1.0F, height, 1.0F);
			break;
		case EAST:
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, height, 0.125F);
			break;
		case WEST:
			setBlockBounds(0.0F, 0.0F, 0.875F, 1.0F, height, 1.0F);
			break;
		default:
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return MTR.itemapg;
	}

	@Override
	public Item getItem(World worldIn, BlockPos pos) {
		return MTR.itemapg;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 1;
	}

	public String getName() {
		return name;
	}
}
