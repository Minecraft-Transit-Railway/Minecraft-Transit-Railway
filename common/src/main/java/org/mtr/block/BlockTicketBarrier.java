package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.tick.OrderedTick;
import org.mtr.data.TicketSystem;
import org.mtr.registry.SoundEvents;

import javax.annotation.Nonnull;

public class BlockTicketBarrier extends Block {

	private final boolean isEntrance;

	public static final EnumProperty<TicketSystem.EnumTicketBarrierOpen> OPEN = EnumProperty.of("open", TicketSystem.EnumTicketBarrierOpen.class);

	public BlockTicketBarrier(AbstractBlock.Settings settings, boolean isEntrance) {
		super(settings.luminance(blockState -> 5));
		this.isEntrance = isEntrance;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos blockPos, Entity entity) {
		if (!world.isClient() && entity instanceof PlayerEntity) {
			final Direction facing = IBlock.getStatePropertySafe(state, Properties.FACING);
			final Vec3d playerPosRotated = entity.getPos().subtract(blockPos.getX() + 0.5, 0, blockPos.getZ() + 0.5).rotateY((float) Math.toRadians(facing.getPositiveHorizontalDegrees()));
			final TicketSystem.EnumTicketBarrierOpen open = IBlock.getStatePropertySafe(state, OPEN);

			if ((open == TicketSystem.EnumTicketBarrierOpen.OPEN || open == TicketSystem.EnumTicketBarrierOpen.OPEN_CONCESSIONARY) && playerPosRotated.z > 0) {
				world.setBlockState(blockPos, state.with(OPEN, TicketSystem.EnumTicketBarrierOpen.CLOSED));
			} else if (open == TicketSystem.EnumTicketBarrierOpen.CLOSED && playerPosRotated.z < 0) {
				final BlockPos blockPosCopy = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
				world.setBlockState(blockPosCopy, state.with(OPEN, TicketSystem.EnumTicketBarrierOpen.PENDING));
				TicketSystem.passThrough(
						world, blockPosCopy, (PlayerEntity) entity,
						isEntrance, !isEntrance,
						SoundEvents.TICKET_BARRIER.createAndGet(), SoundEvents.TICKET_BARRIER_CONCESSIONARY.createAndGet(),
						SoundEvents.TICKET_BARRIER.createAndGet(), SoundEvents.TICKET_BARRIER_CONCESSIONARY.createAndGet(),
						null,
						false,
						newOpen -> {
							world.setBlockState(blockPosCopy, state.with(OPEN, newOpen));
							if (newOpen != TicketSystem.EnumTicketBarrierOpen.CLOSED && !world.getBlockTickScheduler().isQueued(blockPosCopy, this)) {
								world.getBlockTickScheduler().scheduleTick(new OrderedTick<>(this, blockPosCopy, 40, 0));
							}
						}
				);
			}
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.setBlockState(pos, state.with(OPEN, TicketSystem.EnumTicketBarrierOpen.CLOSED));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(Properties.FACING, ctx.getHorizontalPlayerFacing()).with(OPEN, TicketSystem.EnumTicketBarrierOpen.CLOSED);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.FACING);
		return IBlock.getVoxelShapeByDirection(12, 0, 0, 16, 15, 16, facing);
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.FACING);
		final TicketSystem.EnumTicketBarrierOpen open = IBlock.getStatePropertySafe(state, OPEN);
		final VoxelShape base = IBlock.getVoxelShapeByDirection(15, 0, 0, 16, 24, 16, facing);
		return open == TicketSystem.EnumTicketBarrierOpen.OPEN || open == TicketSystem.EnumTicketBarrierOpen.OPEN_CONCESSIONARY ? base : VoxelShapes.union(IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 24, 9, facing), base);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.FACING);
		builder.add(OPEN);
	}
}
