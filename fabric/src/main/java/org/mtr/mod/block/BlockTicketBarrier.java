package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.SoundEvents;
import org.mtr.mod.data.TicketSystem;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockTicketBarrier extends BlockExtension implements DirectionHelper {

	private final boolean isEntrance;

	public static final EnumProperty<TicketSystem.EnumTicketBarrierOpen> OPEN = EnumProperty.of("open", TicketSystem.EnumTicketBarrierOpen.class);

	public BlockTicketBarrier(boolean isEntrance) {
		super(Blocks.createDefaultBlockSettings(true, blockState -> 5));
		this.isEntrance = isEntrance;
	}

	@Override
	public void onEntityCollision2(BlockState state, World world, BlockPos blockPos, Entity entity) {
		if (!world.isClient() && PlayerEntity.isInstance(entity)) {
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			final Vector3d playerPosRotated = entity.getPos().subtract(blockPos.getX() + 0.5, 0, blockPos.getZ() + 0.5).rotateY((float) Math.toRadians(facing.asRotation()));
			final TicketSystem.EnumTicketBarrierOpen open = IBlock.getStatePropertySafe(state, new Property<>(OPEN.data));

			if ((open == TicketSystem.EnumTicketBarrierOpen.OPEN || open == TicketSystem.EnumTicketBarrierOpen.OPEN_CONCESSIONARY) && playerPosRotated.getZMapped() > 0) {
				world.setBlockState(blockPos, state.with(new Property<>(OPEN.data), TicketSystem.EnumTicketBarrierOpen.CLOSED));
			} else if (open == TicketSystem.EnumTicketBarrierOpen.CLOSED && playerPosRotated.getZMapped() < 0) {
				final BlockPos blockPosCopy = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
				world.setBlockState(blockPosCopy, state.with(new Property<>(OPEN.data), TicketSystem.EnumTicketBarrierOpen.PENDING));
				TicketSystem.passThrough(
						world, blockPosCopy, PlayerEntity.cast(entity),
						isEntrance, !isEntrance,
						SoundEvents.TICKET_BARRIER.get(), SoundEvents.TICKET_BARRIER_CONCESSIONARY.get(),
						SoundEvents.TICKET_BARRIER.get(), SoundEvents.TICKET_BARRIER_CONCESSIONARY.get(),
						null,
						false,
						newOpen -> {
							world.setBlockState(blockPosCopy, state.with(new Property<>(OPEN.data), newOpen));
							if (newOpen != TicketSystem.EnumTicketBarrierOpen.CLOSED && !hasScheduledBlockTick(world, blockPosCopy, new Block(this))) {
								scheduleBlockTick(world, blockPosCopy, new Block(this), 40);
							}
						}
				);
			}
		}
	}

	@Override
	public void scheduledTick2(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.setBlockState(pos, state.with(new Property<>(OPEN.data), TicketSystem.EnumTicketBarrierOpen.CLOSED));
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		return getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data).with(new Property<>(OPEN.data), TicketSystem.EnumTicketBarrierOpen.CLOSED);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return IBlock.getVoxelShapeByDirection(12, 0, 0, 16, 15, 16, facing);
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final TicketSystem.EnumTicketBarrierOpen open = IBlock.getStatePropertySafe(state, new Property<>(OPEN.data));
		final VoxelShape base = IBlock.getVoxelShapeByDirection(15, 0, 0, 16, 24, 16, facing);
		return open == TicketSystem.EnumTicketBarrierOpen.OPEN || open == TicketSystem.EnumTicketBarrierOpen.OPEN_CONCESSIONARY ? base : VoxelShapes.union(IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 24, 9, facing), base);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(OPEN);
	}
}
