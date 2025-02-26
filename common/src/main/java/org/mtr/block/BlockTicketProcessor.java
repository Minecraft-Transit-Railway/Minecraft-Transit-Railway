package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.SoundEvents;
import org.mtr.mod.data.TicketSystem;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockTicketProcessor extends BlockDirectionalDoubleBlockBase {

	public final boolean hasLight;
	public final boolean canEnter;
	public final boolean canExit;

	public static final EnumProperty<EnumTicketProcessorLights> LIGHTS = EnumProperty.of("lights", EnumTicketProcessorLights.class);

	public BlockTicketProcessor(boolean hasLight, boolean canEnter, boolean canExit) {
		super(Blocks.createDefaultBlockSettings(true, blockState -> 5));
		this.hasLight = hasLight;
		this.canEnter = canEnter;
		this.canExit = canExit;
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient() && IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			final BlockPos blockPosCopy = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
			TicketSystem.passThrough(
					world, blockPosCopy, player,
					canEnter, canExit,
					SoundEvents.TICKET_PROCESSOR_ENTRY.get(), SoundEvents.TICKET_PROCESSOR_ENTRY_CONCESSIONARY.get(),
					SoundEvents.TICKET_PROCESSOR_EXIT.get(), SoundEvents.TICKET_PROCESSOR_EXIT_CONCESSIONARY.get(),
					SoundEvents.TICKET_PROCESSOR_FAIL.get(),
					true,
					open -> world.setBlockState(blockPosCopy, state.with(new Property<>(LIGHTS.data), open == TicketSystem.EnumTicketBarrierOpen.CLOSED ? EnumTicketProcessorLights.RED : EnumTicketProcessorLights.GREEN))
			);
			scheduleBlockTick(world, blockPosCopy, new Block(this), 20);
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void scheduledTick2(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (hasLight) {
			world.setBlockState(pos, state.with(new Property<>(LIGHTS.data), EnumTicketProcessorLights.NONE));
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return VoxelShapes.union(IBlock.getVoxelShapeByDirection(4.75, 1, 0, 11.25, 13, 8, facing), IBlock.getVoxelShapeByDirection(7, 0, 2, 9, 1, 4, facing));
		} else {
			return VoxelShapes.union(IBlock.getVoxelShapeByDirection(5, 0, 0, 11, 1, 6, facing), IBlock.getVoxelShapeByDirection(7, 1, 2, 9, 16, 4, facing));
		}
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(HALF);
		properties.add(LIGHTS);
	}

	public enum EnumTicketProcessorLights implements StringIdentifiable {

		NONE("none"), RED("red"), YELLOW_GREEN("yellow_green"), GREEN("green");
		private final String name;

		EnumTicketProcessorLights(String nameIn) {
			name = nameIn;
		}

		@Nonnull
		@Override
		public String asString2() {
			return name;
		}
	}
}
