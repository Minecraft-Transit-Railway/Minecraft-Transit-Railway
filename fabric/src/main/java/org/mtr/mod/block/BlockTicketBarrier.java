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

	private final EnumTicketBarrierMode mode;
	public static final EnumProperty<TicketSystem.EnumTicketBarrierOpen> OPEN = EnumProperty.of("open", TicketSystem.EnumTicketBarrierOpen.class);
	public static final EnumProperty<EnumTicketBarrierDirection> TICKET_BARRIER_DIRECTION =
            EnumProperty.of("ticket_barrier_direction", EnumTicketBarrierDirection.class);

        private final java.util.Map<Entity, Long> twoWayCooldowns =
            new java.util.HashMap<>();

	public BlockTicketBarrier(EnumTicketBarrierMode mode) {
		super(Blocks.createDefaultBlockSettings(true, blockState -> 5));
		this.mode = mode;
	}

	@Override
    public void onEntityCollision2(
            BlockState state,
            World world,
            BlockPos blockPos,
            Entity entity
    ) {
        if (!world.isClient() && PlayerEntity.isInstance(entity)) {

            Direction facing = IBlock.getStatePropertySafe(state, FACING);

            Vector3d playerPosRotated = entity.getPos()
                    .subtract(
                            (double) blockPos.getX() + 0.5F,
                            0.0,
                            (double) blockPos.getZ() + 0.5F
                    )
                    .rotateY(
                            (float) Math.toRadians(
                                    (double) facing.asRotation()
                            )
                    );

            TicketSystem.EnumTicketBarrierOpen open =
                    (TicketSystem.EnumTicketBarrierOpen)
                            IBlock.getStatePropertySafe(
                                    state,
                                    new Property<>(OPEN.data)
                            );

            double z = playerPosRotated.getZMapped();

            /*
            * ENTRANCE and EXIT ticket barriers retain original behavior.
            */
            if (mode == EnumTicketBarrierMode.ENTRANCE
                    || mode == EnumTicketBarrierMode.EXIT) {

                if ((open == TicketSystem.EnumTicketBarrierOpen.OPEN
                        || open == TicketSystem.EnumTicketBarrierOpen.OPEN_CONCESSIONARY)
                        && z > 0.0) {

                    closeGate(world, state, blockPos);

                } else if (open == TicketSystem.EnumTicketBarrierOpen.CLOSED
                        && z < 0.0) {

                    passThrough(
                            world,
                            state,
                            blockPos,
                            entity,
                            mode == EnumTicketBarrierMode.ENTRANCE,
                            mode == EnumTicketBarrierMode.ENTRANCE
                                    ? EnumTicketBarrierDirection.ENTRANCE
                                    : EnumTicketBarrierDirection.EXIT
                    );
                }

                return;
            }

            /*
            * TWOWAY gate.
            */
            if (mode == EnumTicketBarrierMode.TWOWAY) {

                /*
                * Remove expired cooldowns.
                */
                long currentTime = System.currentTimeMillis();

                twoWayCooldowns.entrySet().removeIf(
                        entry -> currentTime - entry.getValue() > 1000
                );

                /*
                * While the gate is open, only the direction that
                * originally opened it can close it.
                */
                if (open == TicketSystem.EnumTicketBarrierOpen.OPEN
                        || open == TicketSystem.EnumTicketBarrierOpen.OPEN_CONCESSIONARY) {

                    EnumTicketBarrierDirection direction =
                            (EnumTicketBarrierDirection)
                                    IBlock.getStatePropertySafe(
                                            state,
                                            new Property<>(TICKET_BARRIER_DIRECTION.data)
                                    );

                    if (direction == EnumTicketBarrierDirection.ENTRANCE
                            && z > 0.0) {

                        closeGate(world, state, blockPos);

                        twoWayCooldowns.put(entity, currentTime);

                    } else if (direction == EnumTicketBarrierDirection.EXIT
                            && z < 0.0) {

                        closeGate(world, state, blockPos);

                        twoWayCooldowns.put(entity, currentTime);
                    }

                    return;
                }

                /*
                * Do not start another passage while the ticket system
                * is processing the previous one.
                */
                if (open != TicketSystem.EnumTicketBarrierOpen.CLOSED) {
                    return;
                }

                /*
                * Prevent the player who just crossed the gate from
                * immediately triggering the opposite direction.
                */
                Long cooldownTime = twoWayCooldowns.get(entity);

                if (cooldownTime != null
                        && currentTime - cooldownTime <= 1000) {
                    return;
                }

                /*
                * Negative Z -> positive Z:
                * entrance behavior.
                */
                if (z < 0.0) {

                    passThrough(
                            world,
                            state,
                            blockPos,
                            entity,
                            true,
                            EnumTicketBarrierDirection.ENTRANCE
                    );

                /*
                * Positive Z -> negative Z:
                * exit behavior.
                */
                } else if (z > 0.0) {

                    passThrough(
                            world,
                            state,
                            blockPos,
                            entity,
                            false,
                            EnumTicketBarrierDirection.EXIT
                    );
                }
            }
        }
    }

	@Override
    public void scheduledTick2(
            BlockState state,
            ServerWorld world,
            BlockPos pos,
            Random random
    ) {
        world.setBlockState(
                pos,
                state
                        .with(
                                new Property<>(OPEN.data),
                                TicketSystem.EnumTicketBarrierOpen.CLOSED
                        )
                        .with(
                                new Property<>(TICKET_BARRIER_DIRECTION.data),
                                EnumTicketBarrierDirection.CLOSED
                        )
        );
    }

	@Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        return this.getDefaultState2()
                .with(
                        new Property<>(FACING.data),
                        ctx.getPlayerFacing().data
                )
                .with(
                        new Property<>(OPEN.data),
                        TicketSystem.EnumTicketBarrierOpen.CLOSED
                )
                .with(
                        new Property<>(TICKET_BARRIER_DIRECTION.data),
                        EnumTicketBarrierDirection.CLOSED
                );
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
		properties.add(TICKET_BARRIER_DIRECTION);
	}

	private void passThrough(
            World world,
            BlockState state,
            BlockPos blockPos,
            Entity entity,
            boolean entrance,
            EnumTicketBarrierDirection direction
    ) {
        BlockPos blockPosCopy = new BlockPos(
                blockPos.getX(),
                blockPos.getY(),
                blockPos.getZ()
        );

        world.setBlockState(
                blockPosCopy,
                state
                        .with(
                                new Property<>(OPEN.data),
                                TicketSystem.EnumTicketBarrierOpen.PENDING
                        )
                        .with(
                                new Property<>(TICKET_BARRIER_DIRECTION.data),
                                direction
                        )
        );

        TicketSystem.passThrough(
                world,
                blockPosCopy,
                PlayerEntity.cast(entity),

                entrance,
                !entrance,

                SoundEvents.TICKET_BARRIER.get(), SoundEvents.TICKET_BARRIER_CONCESSIONARY.get(),
				SoundEvents.TICKET_BARRIER.get(), SoundEvents.TICKET_BARRIER_CONCESSIONARY.get(),

                null,
                false,

                newOpen -> {

                    EnumTicketBarrierDirection newDirection =
                            newOpen == TicketSystem.EnumTicketBarrierOpen.CLOSED
                                    ? EnumTicketBarrierDirection.CLOSED
                                    : direction;

                    world.setBlockState(
                            blockPosCopy,
                            state
                                    .with(
                                            new Property<>(OPEN.data),
                                            newOpen
                                    )
                                    .with(
                                            new Property<>(TICKET_BARRIER_DIRECTION.data),
                                            newDirection
                                    )
                    );

                    if (newOpen != TicketSystem.EnumTicketBarrierOpen.CLOSED
                            && !hasScheduledBlockTick(
                                    world,
                                    blockPosCopy,
                                    new Block(this)
                            )) {

                        scheduleBlockTick(
                                world,
                                blockPosCopy,
                                new Block(this),
                                40
                        );
                    }
                }
        );
    }

	private void closeGate(
            World world,
            BlockState state,
            BlockPos blockPos
    ) {
        world.setBlockState(
                blockPos,
                state
                        .with(
                                new Property<>(OPEN.data),
                                TicketSystem.EnumTicketBarrierOpen.CLOSED
                        )
                        .with(
                                new Property<>(TICKET_BARRIER_DIRECTION.data),
                                EnumTicketBarrierDirection.CLOSED
                        )
        );
    }

	public static enum EnumTicketBarrierDirection implements StringIdentifiable {

        CLOSED("closed"),
        ENTRANCE("entrance"),
        EXIT("exit");

        private final String name;

        private EnumTicketBarrierDirection(String name) {
            this.name = name;
        }

        @Override
        public String asString2() {
            return this.name;
        }
    }

    public static enum EnumTicketBarrierMode implements StringIdentifiable {

        ENTRANCE("entrance"),
        EXIT("exit"),
        TWOWAY("twoway");

        private final String name;

        private EnumTicketBarrierMode(String nameIn) {
            this.name = nameIn;
        }

        @Override
        public String asString2() {
            return this.name;
        }
    }
}
