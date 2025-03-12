package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.tick.OrderedTick;
import org.mtr.data.TicketSystem;
import org.mtr.registry.SoundEvents;

import javax.annotation.Nonnull;

public class BlockTicketProcessor extends BlockDirectionalDoubleBlockBase {

	public final boolean hasLight;
	public final boolean canEnter;
	public final boolean canExit;

	public static final EnumProperty<EnumTicketProcessorLights> LIGHTS = EnumProperty.of("lights", EnumTicketProcessorLights.class);

	public BlockTicketProcessor(AbstractBlock.Settings settings, boolean hasLight, boolean canEnter, boolean canExit) {
		super(settings.luminance(blockState -> 5));
		this.hasLight = hasLight;
		this.canEnter = canEnter;
		this.canExit = canExit;
	}

	@Nonnull
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos blockPos, PlayerEntity player, BlockHitResult hit) {
		if (!world.isClient() && IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			final BlockPos blockPosCopy = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
			TicketSystem.passThrough(
					world, blockPosCopy, player,
					canEnter, canExit,
					SoundEvents.TICKET_PROCESSOR_ENTRY.createAndGet(), SoundEvents.TICKET_PROCESSOR_ENTRY_CONCESSIONARY.createAndGet(),
					SoundEvents.TICKET_PROCESSOR_EXIT.createAndGet(), SoundEvents.TICKET_PROCESSOR_EXIT_CONCESSIONARY.createAndGet(),
					SoundEvents.TICKET_PROCESSOR_FAIL.createAndGet(),
					true,
					open -> world.setBlockState(blockPosCopy, state.with(LIGHTS, open == TicketSystem.EnumTicketBarrierOpen.CLOSED ? EnumTicketProcessorLights.RED : EnumTicketProcessorLights.GREEN))
			);
			world.getBlockTickScheduler().scheduleTick(new OrderedTick<>(this, blockPosCopy, 20, 0));
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (hasLight) {
			world.setBlockState(pos, state.with(LIGHTS, EnumTicketProcessorLights.NONE));
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return VoxelShapes.union(IBlock.getVoxelShapeByDirection(4.75, 1, 0, 11.25, 13, 8, facing), IBlock.getVoxelShapeByDirection(7, 0, 2, 9, 1, 4, facing));
		} else {
			return VoxelShapes.union(IBlock.getVoxelShapeByDirection(5, 0, 0, 11, 1, 6, facing), IBlock.getVoxelShapeByDirection(7, 1, 2, 9, 16, 4, facing));
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
		builder.add(HALF);
		builder.add(LIGHTS);
	}

	public enum EnumTicketProcessorLights implements StringIdentifiable {

		NONE("none"), RED("red"), YELLOW_GREEN("yellow_green"), GREEN("green");
		private final String name;

		EnumTicketProcessorLights(String nameIn) {
			name = nameIn;
		}

		@Nonnull
		@Override
		public String asString() {
			return name;
		}
	}
}
