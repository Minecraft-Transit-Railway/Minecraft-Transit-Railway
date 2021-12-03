package mtr.block;

import mapper.Utilities;
import mtr.MTR;
import mtr.data.TicketSystem;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTicketProcessor extends BlockDirectionalDoubleBlockBase {

	public boolean hasLight;
	public boolean canEnter;
	public boolean canExit;

	public static final EnumProperty<EnumTicketProcessorLights> LIGHTS = EnumProperty.of("lights", EnumTicketProcessorLights.class);

	public BlockTicketProcessor(boolean hasLight, boolean canEnter, boolean canExit) {
		super(FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().hardness(2).luminance(5).nonOpaque());
		this.hasLight = hasLight;
		this.canEnter = canEnter;
		this.canExit = canExit;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient && IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			final TicketSystem.EnumTicketBarrierOpen open = TicketSystem.passThrough(world, pos, player, canEnter, canExit, MTR.TICKET_PROCESSOR_ENTRY, MTR.TICKET_PROCESSOR_ENTRY_CONCESSIONARY, MTR.TICKET_PROCESSOR_EXIT, MTR.TICKET_PROCESSOR_EXIT_CONCESSIONARY, MTR.TICKET_PROCESSOR_FAIL, true);
			world.setBlockState(pos, state.with(LIGHTS, open.isOpen() ? EnumTicketProcessorLights.GREEN : EnumTicketProcessorLights.RED));
			Utilities.scheduleBlockTick(world, pos, this, 20);
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (hasLight) {
			world.setBlockState(pos, state.with(LIGHTS, EnumTicketProcessorLights.NONE));
		}
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
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, LIGHTS);
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
}
