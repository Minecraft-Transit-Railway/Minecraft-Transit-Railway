package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.ScheduledTick;
import org.mtr.data.TicketSystem;
import org.mtr.registry.SoundEvents;

public class BlockTicketBarrier extends Block {

	private final boolean isEntrance;

	public static final EnumProperty<TicketSystem.EnumTicketBarrierOpen> OPEN = EnumProperty.create("open", TicketSystem.EnumTicketBarrierOpen.class);

	public BlockTicketBarrier(BlockBehaviour.Properties settings, boolean isEntrance) {
		super(settings.lightLevel(blockState -> 5));
		this.isEntrance = isEntrance;
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos blockPos, Entity entity) {
		if (!world.isClientSide() && entity instanceof Player) {
			final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
			final Vec3 playerPosRotated = entity.position().subtract(blockPos.getX() + 0.5, 0, blockPos.getZ() + 0.5).yRot((float) Math.toRadians(facing.toYRot()));
			final TicketSystem.EnumTicketBarrierOpen open = IBlock.getStatePropertySafe(state, OPEN);

			if ((open == TicketSystem.EnumTicketBarrierOpen.OPEN || open == TicketSystem.EnumTicketBarrierOpen.OPEN_CONCESSIONARY) && playerPosRotated.z > 0) {
				world.setBlockAndUpdate(blockPos, state.setValue(OPEN, TicketSystem.EnumTicketBarrierOpen.CLOSED));
			} else if (open == TicketSystem.EnumTicketBarrierOpen.CLOSED && playerPosRotated.z < 0) {
				final BlockPos blockPosCopy = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
				world.setBlockAndUpdate(blockPosCopy, state.setValue(OPEN, TicketSystem.EnumTicketBarrierOpen.PENDING));
				TicketSystem.passThrough(
					world, blockPosCopy, (Player) entity,
					isEntrance, !isEntrance,
					SoundEvents.TICKET_BARRIER.get(), SoundEvents.TICKET_BARRIER_CONCESSIONARY.get(),
					SoundEvents.TICKET_BARRIER.get(), SoundEvents.TICKET_BARRIER_CONCESSIONARY.get(),
					null,
					false,
					newOpen -> {
						world.setBlockAndUpdate(blockPosCopy, state.setValue(OPEN, newOpen));
						if (newOpen != TicketSystem.EnumTicketBarrierOpen.CLOSED && !world.getBlockTicks().hasScheduledTick(blockPosCopy, this)) {
							world.getBlockTicks().schedule(new ScheduledTick<>(this, blockPosCopy, 40, 0));
						}
					}
				);
			}
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		world.setBlockAndUpdate(pos, state.setValue(OPEN, TicketSystem.EnumTicketBarrierOpen.CLOSED));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection()).setValue(OPEN, TicketSystem.EnumTicketBarrierOpen.CLOSED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
		return IBlock.getVoxelShapeByDirection(12, 0, 0, 16, 15, 16, facing);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
		final TicketSystem.EnumTicketBarrierOpen open = IBlock.getStatePropertySafe(state, OPEN);
		final VoxelShape base = IBlock.getVoxelShapeByDirection(15, 0, 0, 16, 24, 16, facing);
		return open == TicketSystem.EnumTicketBarrierOpen.OPEN || open == TicketSystem.EnumTicketBarrierOpen.OPEN_CONCESSIONARY ? base : Shapes.or(IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 24, 9, facing), base);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(OPEN);
	}
}
