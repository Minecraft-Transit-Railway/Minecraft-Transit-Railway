package mtr.block;

import mtr.MTR;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.Random;

public class BlockTicketProcessorBase extends BlockDirectionalDoubleBlockBase {

	public static final EnumProperty<EnumTicketProcessorLights> LIGHTS = EnumProperty.of("lights", EnumTicketProcessorLights.class);

	public BlockTicketProcessorBase() {
		super(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().hardness(2).luminance(5).nonOpaque());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return VoxelShapes.union(IBlock.getVoxelShapeByDirection(4.75, 1, 0, 11.25, 13, 8, facing), IBlock.getVoxelShapeByDirection(7, 0, 2, 9, 1, 4, facing));
		} else {
			return VoxelShapes.union(IBlock.getVoxelShapeByDirection(5, 0, 0, 11, 1, 6, facing), IBlock.getVoxelShapeByDirection(7, 1, 2, 9, 16, 4, facing));
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.setBlockState(pos, state.with(LIGHTS, EnumTicketProcessorLights.NONE));
	}

	public enum EnumTicketProcessorLights implements StringIdentifiable {

		NONE("none"), RED("red"), YELLOW_GREEN("yellow_green"), GREEN("green");
		private final String name;

		EnumTicketProcessorLights(String nameIn) {
			name = nameIn;
		}

		@Override
		public String asString() {
			return name;
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, LIGHTS);
	}
}