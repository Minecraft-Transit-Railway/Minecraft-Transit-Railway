package MTR.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPSDGlassEnd extends BlockPSD {

	private static final String name = "BlockPSDGlassEnd";
	public static final PropertyBool END = PropertyBool.create("end");

	public BlockPSDGlassEnd() {
		super();
		GameRegistry.registerBlock(this, name);
		setDefaultState(blockState.getBaseState().withProperty(END, false).withProperty(FACING, EnumFacing.NORTH)
				.withProperty(SIDE, false).withProperty(TOP, false));
		setUnlocalizedName(name);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		Block block1 = worldIn.getBlockState(pos.north()).getBlock();
		Block block2 = worldIn.getBlockState(pos.east()).getBlock();
		Block block3 = worldIn.getBlockState(pos.south()).getBlock();
		Block block4 = worldIn.getBlockState(pos.west()).getBlock();
		boolean end = false;
		if (block1 instanceof BlockPSDDoor || block2 instanceof BlockPSDDoor || block3 instanceof BlockPSDDoor
				|| block4 instanceof BlockPSDDoor || block1 instanceof BlockPSDDoorClosed
				|| block2 instanceof BlockPSDDoorClosed || block3 instanceof BlockPSDDoorClosed
				|| block4 instanceof BlockPSDDoorClosed)
			end = true;
		return state.withProperty(END, end);
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { END, FACING, SIDE, TOP });
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 2;
	}

	public String getName() {
		return name;
	}
}
