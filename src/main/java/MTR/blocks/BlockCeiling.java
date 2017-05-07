package MTR.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockCeiling extends BlockWithDirection {

	private static final String name = "BlockCeiling";
	public static final PropertyBool LIGHT = PropertyBool.create("light");
	public static final PropertyInteger STATION = PropertyInteger.create("station", 0, 8);

	public BlockCeiling() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
		setBlockBounds(0, 0.4375F, 0, 1, 0.625F, 1);
		setLightLevel(1F);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LIGHT, false));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		int station = 0;
		Block block = worldIn.getBlockState(pos.add(0, -1, 0)).getBlock();
		if (block instanceof BlockStationNameBase) {
			if (block instanceof BlockStationNameA)
				station = 1;
			if (block instanceof BlockStationNameB)
				station = 2;
			if (block instanceof BlockStationNameC)
				station = 3;
			if (block instanceof BlockStationNameD)
				station = 4;
			if (block instanceof BlockStationNameE)
				station = 5;
			if (block instanceof BlockStationNameF)
				station = 6;
			if (block instanceof BlockStationNameG)
				station = 7;
			if (block instanceof BlockStationNameH)
				station = 8;
		}
		if (((EnumFacing) state.getValue(FACING)).getAxis() == EnumFacing.Axis.X)
			return state.withProperty(LIGHT, pos.getZ() % 3 == 0).withProperty(STATION, station);
		else
			return state.withProperty(LIGHT, pos.getX() % 3 == 0).withProperty(STATION, station);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		EnumFacing var9 = placer.getHorizontalFacing();
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, var9);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, LIGHT, STATION });
	}

	public String getName() {
		return name;
	}
}
