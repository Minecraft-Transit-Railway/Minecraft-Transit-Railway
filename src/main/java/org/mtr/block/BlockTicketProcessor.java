package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.ScheduledTick;
import org.mtr.data.TicketSystem;
import org.mtr.registry.SoundEvents;

public class BlockTicketProcessor extends BlockDirectionalDoubleBlockBase {

	public final boolean hasLight;
	public final boolean canEnter;
	public final boolean canExit;

	public static final EnumProperty<EnumTicketProcessorLights> LIGHTS = EnumProperty.create("lights", EnumTicketProcessorLights.class);

	public BlockTicketProcessor(BlockBehaviour.Properties settings, boolean hasLight, boolean canEnter, boolean canExit) {
		super(settings.lightLevel(blockState -> 5));
		this.hasLight = hasLight;
		this.canEnter = canEnter;
		this.canExit = canExit;
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos blockPos, Player player, BlockHitResult hit) {
		if (!world.isClientSide() && IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			final BlockPos blockPosCopy = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
			TicketSystem.passThrough(
				world, blockPosCopy, player,
				canEnter, canExit,
				SoundEvents.TICKET_PROCESSOR_ENTRY.get(), SoundEvents.TICKET_PROCESSOR_ENTRY_CONCESSIONARY.get(),
				SoundEvents.TICKET_PROCESSOR_EXIT.get(), SoundEvents.TICKET_PROCESSOR_EXIT_CONCESSIONARY.get(),
				SoundEvents.TICKET_PROCESSOR_FAIL.get(),
				true,
				open -> world.setBlockAndUpdate(blockPosCopy, state.setValue(LIGHTS, open == TicketSystem.EnumTicketBarrierOpen.CLOSED ? EnumTicketProcessorLights.RED : EnumTicketProcessorLights.GREEN))
			);
			world.getBlockTicks().schedule(new ScheduledTick<>(this, blockPosCopy, 20, 0));
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (hasLight) {
			world.setBlockAndUpdate(pos, state.setValue(LIGHTS, EnumTicketProcessorLights.NONE));
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return Shapes.or(IBlock.getVoxelShapeByDirection(4.75, 1, 0, 11.25, 13, 8, facing), IBlock.getVoxelShapeByDirection(7, 0, 2, 9, 1, 4, facing));
		} else {
			return Shapes.or(IBlock.getVoxelShapeByDirection(5, 0, 0, 11, 1, 6, facing), IBlock.getVoxelShapeByDirection(7, 1, 2, 9, 16, 4, facing));
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(HALF);
		builder.add(LIGHTS);
	}

	public enum EnumTicketProcessorLights implements StringRepresentable {

		NONE("none"), RED("red"), YELLOW_GREEN("yellow_green"), GREEN("green");
		private final String name;

		EnumTicketProcessorLights(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
