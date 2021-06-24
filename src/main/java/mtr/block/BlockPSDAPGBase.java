package mtr.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public abstract class BlockPSDAPGBase extends BlockDirectionalDoubleBlockBase {

	public BlockPSDAPGBase() {
		super(FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().hardness(2).nonOpaque());
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(asItem());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final int height = isAPG() && IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 9 : 16;
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, height, 4, IBlock.getStatePropertySafe(state, FACING));
	}

	protected boolean isAPG() {
		return this instanceof BlockAPGDoor || this instanceof BlockAPGGlass || this instanceof BlockAPGGlassEnd;
	}
}
