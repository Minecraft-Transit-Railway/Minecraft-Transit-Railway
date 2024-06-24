package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.SoundEvents;
import org.mtr.mod.data.TicketSystem;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockTicketBarrier extends BlockExtension implements DirectionHelper {

	private final boolean isEntrance;

	public static final EnumProperty<TicketSystem.EnumTicketBarrierOpen> OPEN = EnumProperty.of("open", TicketSystem.EnumTicketBarrierOpen.class);

	public BlockTicketBarrier(boolean isEntrance) {
		super(BlockHelper.createBlockSettings(true, blockState -> 5));
		this.isEntrance = isEntrance;
	}

	@Override
	public void onEntityCollision2(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isClient() && PlayerEntity.isInstance(entity)) {
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			final Vector3d playerPosRotated = entity.getPos().subtract(pos.getX() + 0.5, 0, pos.getZ() + 0.5).rotateY((float) Math.toRadians(facing.asRotation()));
			final TicketSystem.EnumTicketBarrierOpen open = IBlock.getStatePropertySafe(state, new Property<>(OPEN.data));

			if (open.isOpen() && playerPosRotated.getZMapped() > 0) {
				world.setBlockState(pos, state.with(new Property<>(OPEN.data), TicketSystem.EnumTicketBarrierOpen.CLOSED));
			} else if (!open.isOpen() && playerPosRotated.getZMapped() < 0) {
				final TicketSystem.EnumTicketBarrierOpen newOpen = TicketSystem.passThrough(world, pos, PlayerEntity.cast(entity), isEntrance, !isEntrance, SoundEvents.TICKET_BARRIER.get(), SoundEvents.TICKET_BARRIER_CONCESSIONARY.get(), SoundEvents.TICKET_BARRIER.get(), SoundEvents.TICKET_BARRIER_CONCESSIONARY.get(), null, false);
				world.setBlockState(pos, state.with(new Property<>(OPEN.data), newOpen));
				if (newOpen != TicketSystem.EnumTicketBarrierOpen.CLOSED && !hasScheduledTick(world, pos, new Block(this))) {
					scheduleBlockTick(world, pos, new Block(this), 40);
				}
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
		return open.isOpen() ? base : VoxelShapes.union(IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 24, 9, facing), base);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(OPEN);
	}
}
